package com.quyet.superapp.service;

import com.quyet.superapp.dto.BloodSeparationRequestDTO;
import com.quyet.superapp.dto.BloodSeparationResultDTO;
import com.quyet.superapp.entity.*;
import com.quyet.superapp.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class BloodSeparationService {

    private final DonationRepository donationRepo;
    private final BloodUnitRepository unitRepo;
    private final BloodComponentRepository componentRepo;
    private final BloodInventoryRepository inventoryRepo;
    private final BloodSeparationLogRepository logRepo;
    private final UserRepository userRepo;

    @Transactional
    public BloodSeparationResultDTO separateBlood(BloodSeparationRequestDTO dto) {
        Donation donation = donationRepo.findById(dto.getDonationId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bản ghi hiến máu"));

        if (logRepo.existsByDonation_DonationId(donation.getDonationId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Đơn hiến này đã được xử lý phân tách.");
        }

        User staff = userRepo.findById(dto.getStaffId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhân viên thực hiện."));

        // Lưu log phân tách
        BloodSeparationLog log = new BloodSeparationLog();
        log.setDonation(donation);
        log.setMethod(dto.getMethod());
        log.setTotalVolume(donation.getVolumeMl());
        log.setRedCellsMl(dto.getRedCellsMl());
        log.setPlasmaMl(dto.getPlasmaMl());
        log.setPlateletsMl(dto.getPlateletsMl());
        log.setLeukoreduced(dto.isLeukoreduced());
        log.setPerformedBy(staff);
        log.setSeparatedAt(LocalDateTime.now());
        logRepo.save(log);

        // Tạo 3 BloodUnit tương ứng với thành phần máu
        createBloodUnit(donation, "Hồng cầu", dto.getRedCellsMl());
        createBloodUnit(donation, "Huyết tương", dto.getPlasmaMl());
        createBloodUnit(donation, "Tiểu cầu", dto.getPlateletsMl());

        return new BloodSeparationResultDTO(
                log.getBloodSeparationLogId(),
                dto.getRedCellsMl(),
                dto.getPlasmaMl(),
                dto.getPlateletsMl(),
                staff.getUsername(), // hoặc getFullName()
                log.getSeparatedAt()
        );
    }

    private void createBloodUnit(Donation donation, String componentName, int quantity) {
        BloodComponent component = componentRepo.findByName(componentName)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thành phần máu: " + componentName));

        BloodUnit unit = new BloodUnit();
        unit.setDonation(donation);
        unit.setComponent(component);
        unit.setBloodType(donation.getBloodType());
        unit.setQuantityMl(quantity);
        unit.setStoredAt(LocalDateTime.now());
        unit.setStatus("Available");
        unit.setExpirationDate(LocalDate.now().plusDays(component.getStorageDays()));

        unitRepo.save(unit);


        // Cập nhật vào kho máu
        BloodInventory inventory = inventoryRepo
                .findByBloodTypeAndComponent(donation.getBloodType(), component)
                .orElseGet(() -> {
                    BloodInventory newInv = new BloodInventory();
                    newInv.setBloodType(donation.getBloodType());
                    newInv.setComponent(component);
                    newInv.setTotalQuantityMl(0);
                    return newInv;
                });

        inventory.setTotalQuantityMl(inventory.getTotalQuantityMl() + quantity);
        inventory.setLastUpdated(LocalDateTime.now());
        inventoryRepo.save(inventory);
    }



}
