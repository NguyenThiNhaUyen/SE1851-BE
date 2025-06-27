package com.quyet.superapp.service;

import com.quyet.superapp.dto.ApproveBloodRequestDTO;
import com.quyet.superapp.dto.BloodRequestDTO;
import com.quyet.superapp.dto.CreateBloodRequestDTO;
import com.quyet.superapp.entity.*;
import com.quyet.superapp.mapper.BloodRequestMapper;
import com.quyet.superapp.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class    BloodRequestService {
    private final BloodRequestRepository requestRepo;
    private final UserRepository userRepo;
    private final BloodTypeRepository bloodTypeRepo;
    private final BloodComponentRepository componentRepo;

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


    public BloodRequest createRequest(CreateBloodRequestDTO dto) {
        User staff = userRepo.findById(dto.getRequesterId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người gửi"));

        BloodType bloodType = bloodTypeRepo.findById(dto.getBloodTypeId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhóm máu"));

        BloodComponent component = componentRepo.findById(dto.getComponentId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thành phần máu"));

        BloodRequest entity = BloodRequestMapper.toEntity(dto, staff, bloodType, component);
        entity.setCreatedAt(LocalDateTime.now());

        String urgency = dto.getUrgencyLevel();
        if ("BÌNH THƯỜNG".equalsIgnoreCase(urgency)) {
            if (inventoryService.hasEnough(dto.getBloodTypeId(), dto.getComponentId(), dto.getQuantityMl())) {
                entity.setStatus("APPROVED");
                entity.setConfirmedVolumeMl(dto.getQuantityMl());
            } else {
                entity.setStatus("REJECTED");
            }
        } else {
            entity.setStatus("PENDING"); // khẩn cấp hoặc cấp cứu → admin xử lý
        }

        return requestRepo.save(entity);
    }


    public List<BloodRequestDTO> getAllRequests() {
        return requestRepo.findAll().stream()
                .map(BloodRequestMapper::toDTO)
                .collect(Collectors.toList());
    }

    public BloodRequest updateStatus(Long id, String status) {
        BloodRequest req = requestRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy yêu cầu máu với ID: " + id));

        req.setStatus(status);
        return requestRepo.save(req);
    }

    public BloodRequest confirmReceivedVolume(Long requestId, int confirmedVolumeMl) {
        BloodRequest request = requestRepo.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy yêu cầu"));

        if (!"APPROVED".equalsIgnoreCase(request.getStatus())) {
            throw new RuntimeException("Chỉ xác nhận được yêu cầu đã được duyệt");
        }

        BloodInventory inventory = inventoryRepo.findByTypeAndComponent(
                request.getBloodType().getBloodTypeId(),
                request.getComponent().getBloodComponentId()
        ).orElse(null);

        if (inventory == null || inventory.getTotalQuantityMl() < confirmedVolumeMl) {
            List<UrgentDonorRegistry> urgentDonors = urgentDonorRepo.findAvailableDonors(request.getBloodType().getBloodTypeId());

            if (urgentDonors.isEmpty()) {
                throw new RuntimeException("Kho máu không đủ và không có người hiến khẩn cấp sẵn sàng");
            }

            for (UrgentDonorRegistry donor : urgentDonors) {
                UrgentDonorContactLog log = new UrgentDonorContactLog();
                log.setDonor(donor.getDonor());
                log.setBloodRequest(request);
                log.setContactedAt(LocalDateTime.now());
                log.setStatus("PENDING");
                contactLogRepo.save(log);
            }

            throw new RuntimeException("Kho máu không đủ. Đã liên hệ người hiến máu khẩn cấp.");
        }

        inventory.setTotalQuantityMl(inventory.getTotalQuantityMl() - confirmedVolumeMl);
        inventory.setLastUpdated(LocalDateTime.now());
        inventoryRepo.save(inventory);

        Transfusion transfusion = new Transfusion();
        transfusion.setRequest(request);
        transfusion.setRecipient(request.getRequester());
        transfusion.setTransfusionDate(LocalDateTime.now());
        transfusion.setStatus("COMPLETED");
        transfusion.setNotes("Lấy " + confirmedVolumeMl + "ml từ kho");

        transfusionRepo.save(transfusion);

        request.setConfirmedVolumeMl(confirmedVolumeMl);
        request.setStatus("COMPLETED");

        return requestRepo.save(request);
    }

    public BloodRequest approveRequest(ApproveBloodRequestDTO dto) {
        BloodRequest request = requestRepo.findById(dto.getBloodRequestId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy yêu cầu"));

        request.setStatus(dto.getStatus());
        request.setConfirmedVolumeMl(dto.getConfirmedVolumeMl());
        request.setEmergencyNote(dto.getEmergencyNote());
        request.setApprovedBy(dto.getApprovedBy());
        request.setApprovedAt(LocalDateTime.now());
        request.setIsUnmatched(dto.getIsUnmatched());

        // Trừ máu trong kho nếu đủ
        BloodInventory inventory = inventoryRepo.findByTypeAndComponent(
                request.getBloodType().getBloodTypeId(),
                request.getComponent().getBloodComponentId()
        ).orElse(null);

        if (inventory != null && dto.getStatus().startsWith("APPROVED")) {
            int remain = inventory.getTotalQuantityMl() - dto.getConfirmedVolumeMl();
            inventory.setTotalQuantityMl(Math.max(0, remain));
            inventory.setLastUpdated(LocalDateTime.now());
            inventoryRepo.save(inventory);
        }

        return requestRepo.save(request);
    }

    public BloodRequestDTO getById(Long id) {
        BloodRequest entity = requestRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy yêu cầu máu với ID: " + id));
        return BloodRequestMapper.toDTO(entity);
    }

}
