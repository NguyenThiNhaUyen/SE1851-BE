package com.quyet.superapp.service;

import com.quyet.superapp.dto.BloodRequestDTO;
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

    public BloodRequest createRequest(BloodRequestDTO dto) {
        User staff = userRepo.findById(dto.getRequesterId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người gửi"));

        BloodType bloodType = bloodTypeRepo.findById(dto.getBloodTypeId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhóm máu"));

        BloodComponent component = componentRepo.findById(dto.getComponentId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thành phần máu"));

        BloodRequest entity = BloodRequestMapper.toEntity(dto, staff, bloodType, component);
        entity.setStatus("PENDING");
        entity.setCreatedAt(LocalDateTime.now());

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
}
