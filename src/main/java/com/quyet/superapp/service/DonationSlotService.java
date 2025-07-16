package com.quyet.superapp.service;


import com.quyet.superapp.dto.DonationSlotDTO;
import com.quyet.superapp.dto.SlotLoadDTO;
import com.quyet.superapp.entity.DonationRegistration;
import com.quyet.superapp.entity.DonationSlot;
import com.quyet.superapp.enums.SlotStatus;
import com.quyet.superapp.exception.ResourceNotFoundException;
import com.quyet.superapp.mapper.DonationSlotMapper;
import com.quyet.superapp.repository.DonationSlotRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DonationSlotService {

    private final DonationSlotRepository donationSlotRepository;


    // === CRUD cơ bản ===
    public DonationSlotDTO create(DonationSlotDTO dto) {
        DonationSlot entity = DonationSlotMapper.toEntity(dto);
        entity.setRegisteredCount(0);
        entity.setStatus(SlotStatus.ACTIVE);
        return DonationSlotMapper.toDTO(donationSlotRepository.save(entity));
    }

    public List<DonationSlotDTO> getAll() {
        return donationSlotRepository.findAll().stream()
                .map(DonationSlotMapper::toDTO)
                .collect(Collectors.toList());
    }

    public DonationSlotDTO getById(Long id) {
        DonationSlot slot = donationSlotRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy slot"));
        return DonationSlotMapper.toDTO(slot);
    }

    public List<DonationSlotDTO> getSlotsByDate(LocalDate date) {
        return donationSlotRepository.findBySlotDate(date).stream()
                .map(DonationSlotMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<DonationSlotDTO> getSlotsByStatus(SlotStatus status) {
        return donationSlotRepository.findByStatus(status).stream()
                .map(DonationSlotMapper::toDTO)
                .collect(Collectors.toList());
    }

    public boolean isSlotAvailable(Long slotId) {
        DonationSlot slot = donationSlotRepository.findById(slotId).orElse(null);
        return slot != null && slot.getRegisteredCount() < slot.getMaxCapacity();
    }

    public void validateSlotAvailable(Long slotId) {
        DonationSlot slot = donationSlotRepository.findById(slotId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy slot"));

        if (slot.getRegisteredCount() >= slot.getMaxCapacity()) {
            throw new IllegalStateException("Slot đã đầy, vui lòng chọn slot khác.");
        }
    }

//    public void incrementRegistrationCount(Long slotId) {
//        DonationSlot slot = donationSlotRepository.findById(slotId)
//                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy slot"));
//
//        if (slot.getRegisteredCount() >= slot.getMaxCapacity()) {
//            throw new IllegalStateException("Slot này đã đầy");
//        }
//
//        slot.setRegisteredCount(slot.getRegisteredCount() + 1);
//        donationSlotRepository.save(slot);
//    }

    @Transactional
    public DonationSlot assignSlotToRegistration(DonationRegistration registration, Long slotId) {
        validateSlotAvailable(slotId);
        DonationSlot slot = donationSlotRepository.findById(slotId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy slot"));

        registration.setSlot(slot);
        slot.setRegisteredCount(slot.getRegisteredCount() + 1);
        donationSlotRepository.save(slot);

        return slot;
    }

    // === Gợi ý slot nếu cần ===
    public List<DonationSlotDTO> getSuggestedSlots(int requiredCapacity) {
        return donationSlotRepository
                .findByStatusAndRegisteredCountLessThan(SlotStatus.ACTIVE, requiredCapacity)
                .stream()
                .map(DonationSlotMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<DonationSlotDTO> getTodayAvailableSlots() {
        LocalDate today = LocalDate.now();
        return donationSlotRepository
                .findBySlotDateAndStatusAndRegisteredCountLessThan(today, SlotStatus.ACTIVE, Integer.MAX_VALUE)
                .stream()
                .map(DonationSlotMapper::toDTO)
                .collect(Collectors.toList());
    }

    public DonationSlotDTO getBestAvailableSlot() {
        return donationSlotRepository.findByStatus(SlotStatus.ACTIVE).stream()
                .filter(slot -> slot.getRegisteredCount() < slot.getMaxCapacity())
                .sorted(Comparator.comparing(DonationSlot::getSlotDate)
                        .thenComparing(DonationSlot::getRegisteredCount))
                .findFirst()
                .map(DonationSlotMapper::toDTO)
                .orElse(null);
    }

    // === Thống kê tải của các slot ===
    public List<SlotLoadDTO> getSlotLoadStats() {
        return donationSlotRepository.findAll().stream()
                .map(slot -> SlotLoadDTO.builder()
                        .slotId(slot.getSlotId())
                        .slotDate(slot.getSlotDate())
                        .startTime(slot.getStartTime())
                        .endTime(slot.getEndTime())
                        .maxCapacity(slot.getMaxCapacity())
                        .registeredCount(slot.getRegisteredCount())
                        .loadPercent(slot.getMaxCapacity() == 0 ? 0 :
                                (double) slot.getRegisteredCount() * 100 / slot.getMaxCapacity())
                        .build())
                .collect(Collectors.toList());
    }
}
