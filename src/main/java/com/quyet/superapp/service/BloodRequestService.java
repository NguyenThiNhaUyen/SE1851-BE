package com.quyet.superapp.service;

import com.quyet.superapp.dto.*;
import com.quyet.superapp.entity.*;
import com.quyet.superapp.enums.EmailType;
import com.quyet.superapp.enums.BloodRequestStatus;
import com.quyet.superapp.enums.PaymentStatus;
import com.quyet.superapp.mapper.BloodRequestMapper;
import com.quyet.superapp.repository.*;
import com.quyet.superapp.util.AppEmailSender;
import com.quyet.superapp.validator.BloodRequestValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import static com.quyet.superapp.enums.BloodRequestStatus.*;
import static com.quyet.superapp.enums.PaymentStatus.PENDING;

@Service
@RequiredArgsConstructor
public class BloodRequestService {

    private final BloodRequestRepository requestRepo;
    private final UserRepository userRepo;
    private final BloodTypeRepository bloodTypeRepo;
    private final BloodComponentRepository componentRepo;
    private final BloodComponentPricingRepository pricingRepo;
    private final UserRepository userRepository;
    private final AppEmailSender appEmailSender;
    private final UrgentDonorRegistryService urgentDonorService;
    private final NotificationService notificationService;

    @Autowired
    private BloodPricingService bloodPricingService;

    @Autowired
    private TransfusionRepository transfusionRepo;

    @Autowired
    private BloodInventoryRepository inventoryRepo;

    @Autowired
    private BloodUnitRepository bloodUnitRepo;

    @Autowired
    private UrgentDonorRegistryRepository urgentDonorRepo;

    @Autowired
    private UrgentDonorContactLogRepository contactLogRepo;

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private PatientUserService patientUserService;

    @Autowired
    private BloodComponentRepository bloodComponentRepo;

    @Autowired
    private BloodRequestRepository bloodRequestRepository;


    private BloodRequest bloodRequest;


    private final PatientService patientService;

    private final BloodRequestValidator bloodRequestValidator;

    private final BloodPriceRepository bloodPriceRepository;




    public BloodRequest findById(Long id) {
        return requestRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn truyền máu với ID: " + id));
    }


    public BloodRequest rejectRequest(Long requestId, String reason) {
        BloodRequest request = requestRepo.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn truyền máu"));

        if (request.getStatus() != BloodRequestStatus.PENDING) {
            throw new IllegalStateException("Chỉ có thể từ chối đơn ở trạng thái PENDING");
        }


        request.setStatus(REJECTED);
        request.setCancelReason(reason);
        request.setUpdatedAt(LocalDateTime.now());
        request.setCancelledAt(LocalDateTime.now());

        return requestRepo.save(request);
    }


    public void cancelByAdmin(Long requestId, String reason) {
        BloodRequest request = requestRepo.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn"));

        if (!List.of(PENDING, WAITING_PAYMENT, WAITING_DONOR).contains(request.getStatus())) {
            throw new IllegalStateException("Không thể huỷ đơn ở trạng thái hiện tại");
        }

        request.setStatus(BloodRequestStatus.CANCELLED);
        request.setCancelReason(reason);
        request.setCancelledAt(LocalDateTime.now());
        requestRepo.save(request);
    }


    public void handleUnhappyCaseWaitingDonor(Long requestId, Long adminId) {
        BloodRequest request = bloodRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn yêu cầu máu"));

        // 1. Cập nhật trạng thái đơn
        request.setStatus(WAITING_DONOR);
        bloodRequestRepository.save(request);

        // 2. Gửi notify/email đến các bên liên quan
        sendWaitingDonorNotifications(request, adminId);
    }

    private String generateUniqueRequestCode() {
        String prefix = "BR-" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        long countToday = requestRepo.countByCreatedAtBetween(
                LocalDate.now().atStartOfDay(),
                LocalDate.now().plusDays(1).atStartOfDay()
        );
        return "%s-%03d".formatted(prefix, countToday + 1);
    }



    private void sendWaitingDonorNotifications(BloodRequest request, Long adminId) {
        User admin = userRepository.findById(adminId).orElseThrow();
        User staff = request.getRequester();

        // 2.1 Gửi notify/email đến staff
        appEmailSender.sendEmail(
                EmailType.BLOOD_REQUEST_WAITING_DONOR,
                request,
                staff.getEmail(),
                staff
        );

        // 2.2 Gửi email xác nhận lại cho admin (tuỳ chọn)
        appEmailSender.sendEmail(
                EmailType.BLOOD_REQUEST_STATUS_UPDATE,
                request,
                admin.getEmail(),
                admin
        );

        // 2.3 Gửi đến người hiến máu khẩn cấp gần bệnh viện
        List<NearbyDonorDTO> donors = urgentDonorService.findNearbyVerifiedDonors(request, 10.0); // bán kính 10km

        for (NearbyDonorDTO donor : donors) {
            appEmailSender.sendEmail(
                    EmailType.URGENT_DONOR_ALERT,
                    request,
                    donor.getPhoneNumber(), // hoặc donor.getUserId() để lấy email từ user
                    null  // nếu cần user entity thì phải load lại
            );

            notificationService.sendNotification(
                    donor.getUserId(),
                    "Có yêu cầu truyền máu khẩn cấp cần bạn hỗ trợ!",
                    "/urgent-donations"
            );
        }

    }

    private String generateUniqueMedicalRecordCode() {
        String code;
        do {
            code = generateMedicalRecordCode();
        } while (requestRepo.existsByPatientRecordCode(code));
        return code;
    }

    private String generateMedicalRecordCode() {
        String prefix = "BR";
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        int random = (int) (Math.random() * 1000);
        return String.format("%s-%s-%03d", prefix, date, random);
    }



    public int getPriceForComponent(Long componentId) {
        return bloodPricingService.getLatestPriceForComponent(componentId);
    }

    @Transactional
    public BloodRequest createRequestWithNewPatient(BloodRequestWithNewPatientDTO dto) {
        // 1. Tạo hoặc tìm bệnh nhân (dựa trên citizenId nếu có)
        Patient patient = patientService.getOrCreateFromDTO(dto);
        if (patient == null) {
            throw new RuntimeException("Không thể tạo bệnh nhân mới.");
        }

        // 2. Tìm nhân viên gửi yêu cầu và bác sĩ phụ trách
        User staff = userRepo.findById(dto.getRequesterId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhân viên gửi yêu cầu"));

        User doctor = userRepo.findById(dto.getDoctorId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bác sĩ phụ trách"));

        // 3. Tìm thành phần máu
        BloodComponent component = bloodComponentRepo.findById(dto.getComponentId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thành phần máu"));

        // 4. Tìm nhóm máu
        BloodType bloodType = bloodTypeRepo.findById(dto.getBloodTypeId())
                .orElse(null); // optional

        // 5. Khởi tạo yêu cầu máu
        BloodRequest request = new BloodRequest();
        request.setPatient(patient);
        request.setRequester(staff);
        request.setDoctor(doctor);
        request.setComponent(component);
        request.setBloodType(bloodType);

        request.setReason(dto.getReason());
        request.setUrgencyLevel(dto.getUrgencyLevel());
        request.setTriageLevel(dto.getTriageLevel());
        request.setQuantityBag(dto.getQuantityBag());
        request.setQuantityMl(dto.getQuantityMl());
        request.setNeededAt(dto.getNeededAt());
        request.setCrossmatchRequired(dto.getCrossmatchRequired());
        request.setHasTransfusionHistory(dto.getHasTransfusionHistory());
        request.setHasReactionHistory(dto.getHasReactionHistory());
        request.setIsPregnant(dto.getIsPregnant());
        request.setHasAntibodyIssue(dto.getHasAntibodyIssue());
        request.setWarningNote(dto.getWarningNote());
        request.setSpecialNote(dto.getSpecialNote());
        request.setCreatedAt(LocalDateTime.now());
        request.setStatus(BloodRequestStatus.PENDING);

        // 6. Sinh mã bệnh án
        String code = dto.getPatientRecordCode();
        if (code == null || code.isBlank()) {
            code = generateUniqueMedicalRecordCode();
        }
        request.setPatientRecordCode(code);

        return bloodRequestRepository.save(request);
    }


    @Transactional
    public BloodRequest createRequest(CreateBloodRequestDTO dto) {
        // 1️⃣ Lấy thông tin người gửi yêu cầu (staff)
        User staff = userRepo.findById(dto.getRequesterId())
                .orElseThrow(() -> new RuntimeException("❌ Không tìm thấy người gửi yêu cầu"));

        // 2️⃣ Lấy thông tin bác sĩ phụ trách
        User doctor = userRepo.findById(dto.getDoctorId())
                .orElseThrow(() -> new IllegalArgumentException("❌ Không tìm thấy bác sĩ phụ trách"));

        // 3️⃣ Kiểm tra role bác sĩ
        UserProfile doctorProfile = doctor.getUserProfile();
        if (doctorProfile == null || !"Doctor".equalsIgnoreCase(doctorProfile.getStaffPosition())) {
            throw new IllegalArgumentException("❌ Người được chọn không phải là bác sĩ phụ trách hợp lệ");
        }

        // 4️⃣ Kiểm tra xem bác sĩ có đang phụ trách ca khác không
        boolean isBusy = requestRepo.existsByDoctorAndStatusIn(
                doctor, List.of("PENDING", "APPROVED", "WAITING_DONOR")
        );
        if (isBusy) {
            throw new RuntimeException("⚠️ Bác sĩ đang phụ trách một bệnh nhân khác. Vui lòng chọn người khác.");
        }

        // 5️⃣ Kiểm tra hồ sơ nhân viên gửi yêu cầu
        UserProfile staffProfile = staff.getUserProfile();
        if (staffProfile == null) {
            throw new IllegalArgumentException("❌ Không tìm thấy hồ sơ nhân viên gửi yêu cầu");
        }

        // 6️⃣ Lấy nhóm máu và thành phần máu
        BloodType bloodType = bloodTypeRepo.findById(dto.getBloodTypeId())
                .orElseThrow(() -> new IllegalArgumentException("❌ Không tìm thấy nhóm máu với ID: " + dto.getBloodTypeId()));

        BloodComponent component = bloodComponentRepo.findById(dto.getComponentId())
                .orElseThrow(() -> new IllegalArgumentException("❌ Không tìm thấy thành phần máu với ID: " + dto.getComponentId()));

        // 7️⃣ Lấy hoặc tạo bệnh nhân
        Patient patient = (dto.getSuspectedPatientId() != null)
                ? patientService.findById(dto.getSuspectedPatientId())
                : patientService.getOrCreateFromDTO(dto);

        // 8️⃣ Sinh mã bệnh án & mã đơn yêu cầu
        String patientRecordCode = (dto.getPatientRecordCode() != null && !dto.getPatientRecordCode().isBlank())
                ? dto.getPatientRecordCode()
                : generateUniqueMedicalRecordCode();
        String requestCode = generateUniqueRequestCode();

        // 9️⃣ Mapping sang Entity (gọi đúng mapper với đủ 8 tham số)
        BloodRequest entity = BloodRequestMapper.toEntity(
                dto, staff, doctor, bloodType, component,
                patientRecordCode, requestCode, patient
        );
        entity.setCreatedAt(LocalDateTime.now());

        // 🔟 Gán trạng thái thanh toán
        entity.setPaymentStatus(Boolean.TRUE.equals(dto.getDeferredPayment())
                ? PaymentStatus.WAIVED // hoặc DEFERRED nếu bạn thêm mới enum này
                : PENDING);

        entity.setDeferredPayment(dto.getDeferredPayment());
        entity.setDeferredPaymentReason(dto.getDeferredPaymentReason());

        // 🔍 Kiểm tra tồn kho máu
        boolean hasStock = inventoryService.hasEnough(dto.getBloodTypeId(), dto.getComponentId(), dto.getQuantityMl());

        // ⚙️ Xử lý trạng thái theo mức độ khẩn cấp
        String urgency = dto.getUrgencyLevel() != null ? dto.getUrgencyLevel().trim().toUpperCase() : "";
        switch (urgency) {
            case "BÌNH THƯỜNG" -> entity.setStatus(hasStock ? BloodRequestStatus.APPROVED : REJECTED);
            case "KHẨN CẤP", "CẤP CỨU" -> entity.setStatus(hasStock ? BloodRequestStatus.APPROVED : WAITING_DONOR);
            default -> entity.setStatus(BloodRequestStatus.PENDING);
        }

        // 💵 Tính chi phí dựa trên bảng giá
        int unitPrice = bloodPriceRepository
                .findByBloodTypeAndBloodComponent(bloodType, component)
                .map(bp -> bp.getPricePerBag().intValue())
                .orElseThrow(() -> new RuntimeException("❌ Không tìm thấy bảng giá áp dụng"));

        int totalAmount = unitPrice * dto.getQuantityBag();
        entity.setTotalAmount(totalAmount);

        // 💾 Lưu vào database
        entity = requestRepo.save(entity);

        // 📢 Gửi thông báo liên hệ người hiến máu khẩn nếu cần
        if ((urgency.equals("KHẨN CẤP") || urgency.equals("CẤP CỨU")) && !hasStock) {
            List<UrgentDonorRegistry> donors = urgentDonorRepo.findAvailableDonors(dto.getBloodTypeId());
            for (UrgentDonorRegistry donor : donors) {
                UrgentDonorContactLog log = new UrgentDonorContactLog();
                log.setDonor(donor.getDonor());
                log.setBloodRequest(entity);
                log.setContactedAt(LocalDateTime.now());
                log.setStatus("PENDING");
                contactLogRepo.save(log);
            }
        }

        return entity;
    }


    private String generatePatientRecordCode() {
        String prefix = "PR"; // Patient Record
        String datePart = java.time.LocalDate.now().toString().replaceAll("-", ""); // yyyyMMdd
        String randomPart = String.valueOf((int) (Math.random() * 900) + 100); // 3 số ngẫu nhiên
        return prefix + "-" + datePart + "-" + randomPart;
    }



// Tạm comment để test commit GitHub


    public List<BloodRequestDTO> getAllRequests() {
        return requestRepo.findAll().stream()
                .map(BloodRequestMapper::toDTO)
                .collect(Collectors.toList());
    }

    public BloodRequest updateStatus(Long id, String status) {
        BloodRequest req = requestRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy yêu cầu máu với ID: " + id));
        req.setStatus(BloodRequestStatus.valueOf(status));
        return requestRepo.save(req);
    }

    public BloodRequest confirmReceivedVolume(Long requestId, int confirmedVolumeMl) {
        BloodRequest request = requestRepo.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy yêu cầu"));

        if (!BloodRequestStatus.APPROVED.equals(request.getStatus())) {
            throw new RuntimeException("Chỉ xác nhận được yêu cầu đã được duyệt");
        }

        List<BloodInventory> inventories = inventoryRepo.findByTypeAndComponent(
                request.getBloodType().getBloodTypeId(),
                request.getComponent().getBloodComponentId()
        );

        BloodInventory inventory = inventories.isEmpty() ? null : inventories.get(0);

        if (inventory == null || inventory.getTotalQuantityMl() < confirmedVolumeMl) {
            List<UrgentDonorRegistry> urgentDonors = urgentDonorRepo.findAvailableDonors(
                    request.getBloodType().getBloodTypeId()
            );

            for (UrgentDonorRegistry donor : urgentDonors) {
                UrgentDonorContactLog log = new UrgentDonorContactLog();
                log.setDonor(donor.getDonor());
                log.setBloodRequest(request);
                log.setContactedAt(LocalDateTime.now());
                log.setStatus("PENDING");
                contactLogRepo.save(log);
            }

            request.setStatus(WAITING_DONOR);
            request.setUpdatedAt(LocalDateTime.now());
            requestRepo.save(request);

            // Nếu không có donor thì ghi log nhưng vẫn chuyển trạng thái
            if (urgentDonors.isEmpty()) {
                throw new RuntimeException("Kho máu không đủ và không có người hiến máu sẵn sàng.");
            } else {
                throw new RuntimeException("Kho máu không đủ. Đã liên hệ người hiến máu khẩn cấp.");
            }
        }


        inventory.setTotalQuantityMl(inventory.getTotalQuantityMl() - confirmedVolumeMl);
        inventory.setLastUpdated(LocalDateTime.now());
        inventoryRepo.save(inventory);

        Transfusion transfusion = new Transfusion();
        transfusion.setRequest(request);
        transfusion.setTransfusionDate(LocalDateTime.now());
        transfusion.setStatus("COMPLETED");
        transfusion.setVolumeTakenMl(confirmedVolumeMl);
        transfusion.setNotes("Truyền " + confirmedVolumeMl + "ml cho bệnh nhân: " + request.getPatient().getFullName());
        transfusion.setRecipientName(request.getPatient().getFullName() != null ? request.getPatient().getFullName() : request.getRequesterName());
        transfusion.setRecipientPhone(request.getPatient().getPhone() != null ? request.getPatient().getPhone() : request.getRequesterPhone());
        transfusionRepo.save(transfusion);

        if (request.getPatientRecordCode() == null || request.getPatientRecordCode().isEmpty()) {
            request.setPatientRecordCode(generatePatientRecordCode());
        }

        request.setConfirmedVolumeMl(confirmedVolumeMl);
        request.setStatus(BloodRequestStatus.COMPLETED);

        return requestRepo.save(request);
    }

    public BloodRequest approveRequest(ApproveBloodRequestDTO dto) {
        BloodRequest request = requestRepo.findById(dto.getId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy yêu cầu"));

        if (request.getPatientRecordCode() == null || request.getPatientRecordCode().isEmpty()) {
            request.setPatientRecordCode(generatePatientRecordCode());
        }

        request.setStatus(BloodRequestStatus.valueOf(dto.getStatus().toUpperCase()));
        request.setEmergencyNote(dto.getEmergencyNote());
        request.setApprovedAt(LocalDateTime.now());
        request.setIsUnmatched(dto.getIsUnmatched());

        return requestRepo.save(request);
    }

    public BloodRequestDTO getById(Long id) {
        BloodRequest entity = requestRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy yêu cầu máu với ID: " + id));
        return BloodRequestMapper.toDTO(entity);
    }

    public List<BloodRequestDTO> filterRequests(String urgency, String status) {
        return requestRepo.findAll().stream()
                .filter(req -> urgency == null || urgency.equalsIgnoreCase(req.getUrgencyLevel()))
                .filter(req -> {
                    if (status == null) return true;
                    try {
                        return BloodRequestStatus.valueOf(status.toUpperCase()) == req.getStatus();
                    } catch (IllegalArgumentException e) {
                        return false;
                    }
                })

                .map(BloodRequestMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<BloodRequestDTO> getUrgentPendingRequests() {
        return requestRepo.findAll().stream()
                .filter(req ->
                        "KHẨN CẤP".equalsIgnoreCase(req.getUrgencyLevel()) &&
                                req.getStatus() == BloodRequestStatus.PENDING)
                .map(BloodRequestMapper::toDTO)
                .collect(Collectors.toList());
    }


    public List<BloodRequestDTO> getUrgentRequestHistory() {
        List<String> validStatuses = List.of("PENDING", "APPROVED", "REJECTED", "APPROVED_FULL", "COMPLETED");
        return requestRepo.findAll().stream()
                .filter(req -> "KHẨN CẤP".equalsIgnoreCase(req.getUrgencyLevel()))
                .filter(req -> validStatuses.contains(req.getStatus()))
                .map(BloodRequestMapper::toDTO)
                .collect(Collectors.toList());
    }

    public BloodRequest markAsWaitingDonor(Long requestId, String reason) {
        BloodRequest request = requestRepo.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn"));

        if (!(request.getStatus() == BloodRequestStatus.APPROVED ||
                request.getStatus() == BloodRequestStatus.PENDING)) {
            throw new RuntimeException("Chỉ có thể chuyển đơn chưa hoàn tất");
        }

        request.setStatus(WAITING_DONOR);
        request.setEmergencyNote(reason);
        request.setUpdatedAt(LocalDateTime.now());
        requestRepo.save(request);

        List<UrgentDonorRegistry> urgentDonors =
                urgentDonorRepo.findAvailableDonors(request.getBloodType().getBloodTypeId());

        for (UrgentDonorRegistry donor : urgentDonors) {
            boolean alreadyContacted = contactLogRepo.existsByDonorAndRequest(
                    donor.getDonor().getUserId(), request.getId());

            if (!alreadyContacted) {
                UrgentDonorContactLog log = new UrgentDonorContactLog();
                log.setDonor(donor.getDonor());
                log.setBloodRequest(request);
                log.setContactedAt(LocalDateTime.now());
                log.setStatus("PENDING");
                contactLogRepo.save(log);

                notificationService.sendEmergencyContact(
                        donor.getDonor(),
                        "🩸 Có yêu cầu truyền máu khẩn cấp cần bạn hỗ trợ. Vui lòng phản hồi sớm."
                );
            }
        }


        return request;
    }


    public List<BloodRequestDTO> getActiveRequests() {
        List<String> statuses = List.of("APPROVED", "REJECTED", "CANCELLED", "WAITING_DONOR");
        return requestRepo.findByStatusIn(statuses)
                .stream()
                .map(BloodRequestMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<BloodRequestDTO> getCompletedRequests() {
        return requestRepo.findByStatusIn(List.of("COMPLETED"))
                .stream()
                .map(BloodRequestMapper::toDTO)
                .collect(Collectors.toList());
    }
//    public List<BloodRequestDTO> getUrgentActiveRequests() {
//        List<String> statuses = List.of("PENDING", "APPROVED", "REJECTED", "CANCELLED", "WAITING_DONOR");
//        return requestRepo.findUrgentActiveRequests(statuses)
//                .stream()
//                .map(BloodRequestMapper::toDTO)
//                .collect(Collectors.toList());
//    }

    public List<BloodRequestDTO> getUrgentActiveRequests() {
        List<BloodRequestStatus> statuses = List.of(
                BloodRequestStatus.PENDING,
                BloodRequestStatus.APPROVED,
                BloodRequestStatus.WAITING_DONOR
        );

        return requestRepo.findUrgentActiveRequests(statuses)
                .stream()
                .map(BloodRequestMapper::toDTO)
                .collect(Collectors.toList());
    }

}
