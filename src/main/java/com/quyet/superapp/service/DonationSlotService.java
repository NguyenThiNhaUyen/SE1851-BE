package com.quyet.superapp.service;

import com.quyet.superapp.constant.MessageConstants;
import com.quyet.superapp.dto.DonationSlotDTO;
import com.quyet.superapp.dto.SlotLoadDTO;
import com.quyet.superapp.entity.DonationRegistration;
import com.quyet.superapp.entity.DonationSlot;
import com.quyet.superapp.enums.SlotStatus;
import com.quyet.superapp.exception.ResourceNotFoundException;
import com.quyet.superapp.mapper.DonationSlotMapper;
import com.quyet.superapp.repository.DonationRegistrationRepository;
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
    private final DonationRegistrationRepository donationRegistrationRepository;

    // === CRUD ===
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

    public void incrementRegistrationCount(Long slotId) {
        DonationSlot slot = donationSlotRepository.findById(slotId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy slot"));

        if (slot.getRegisteredCount() >= slot.getMaxCapacity()) {
            throw new IllegalStateException("Slot này đã đầy");
        }

        slot.setRegisteredCount(slot.getRegisteredCount() + 1);
        donationSlotRepository.save(slot);
    }

    // === Gợi ý slot ===
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

    @Transactional
    public DonationSlot assignSlotToRegistration(DonationRegistration registration, Long slotId) {
        DonationSlot slot = donationSlotRepository.findById(slotId)
                .orElseThrow(() -> new ResourceNotFoundException(MessageConstants.SLOT_NOT_FOUND));

        // Nếu slot đầy → chọn slot khác tự động
        if (slot.getRegisteredCount() >= slot.getMaxCapacity()) {
            List<DonationSlot> available = donationSlotRepository.findByStatus(SlotStatus.ACTIVE).stream()
                    .filter(s -> s.getRegisteredCount() < s.getMaxCapacity())
                    .sorted(Comparator.comparing(DonationSlot::getSlotDate)
                            .thenComparing(DonationSlot::getStartTime))
                    .collect(Collectors.toList());

            if (available.isEmpty()) {
                throw new IllegalStateException("Tất cả các khung giờ đều đã đầy, vui lòng quay lại sau.");
            }

            slot = available.get(0); // Slot tốt nhất
            log.info("Slot ban đầu đã đầy, chuyển sang slot mới: {}", slot.getSlotId());
        }

        registration.setSlot(slot);
        slot.setRegisteredCount(slot.getRegisteredCount() + 1);
        donationSlotRepository.save(slot);

        return slot;
    }

    @Transactional
    public void autoAssignSlotToRegistration(DonationRegistration registration) {
        DonationSlotDTO bestSlot = getBestAvailableSlot();
        if (bestSlot == null) {
            throw new IllegalStateException("Không tìm thấy slot phù hợp");
        }
        assignSlotToRegistration(registration, bestSlot.getSlotId());
    }

    // === Thống kê ===
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
