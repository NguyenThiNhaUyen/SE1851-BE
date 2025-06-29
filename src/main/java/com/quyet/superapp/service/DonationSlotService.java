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
    private final DonationSlotMapper donationSlotMapper;
    private final DonationRegistrationRepository donationRegistrationRepository;

    public DonationSlotDTO create(DonationSlotDTO dto) {
        DonationSlot entity = donationSlotMapper.toEntity(dto);
        entity.setRegisteredCount(0); // default
        entity.setStatus(SlotStatus.ACTIVE);
        return donationSlotMapper.toDTO(donationSlotRepository.save(entity));
    }

    public List<DonationSlotDTO> getAll() {
        return donationSlotRepository.findAll().stream()
                .map(donationSlotMapper::toDTO)
                .collect(Collectors.toList());
    }

    public DonationSlotDTO getById(Long id) {
        DonationSlot slot = donationSlotRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy slot"));
        return donationSlotMapper.toDTO(slot);
    }

    public List<DonationSlotDTO> getSuggestedSlots(int requiredCapacity) {
        return donationSlotRepository
                .findByStatusAndRegisteredCountLessThan(SlotStatus.ACTIVE, requiredCapacity)
                .stream()
                .map(donationSlotMapper::toDTO)
                .collect(Collectors.toList());
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

    public boolean isSlotAvailable(Long slotId) {
        DonationSlot slot = donationSlotRepository.findById(slotId)
                .orElse(null);
        return slot != null && slot.getRegisteredCount() < slot.getMaxCapacity();
    }

    public List<DonationSlotDTO> getSlotsByDate(LocalDate date) {
        return donationSlotRepository.findBySlotDate(date).stream()
                .map(donationSlotMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<DonationSlotDTO> getSlotsByStatus(SlotStatus status) {
        return donationSlotRepository.findByStatus(status).stream()
                .map(donationSlotMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<DonationSlotDTO> getTodayAvailableSlots() {
        LocalDate today = LocalDate.now();
        // Giả định 1 slot tối đa bao nhiêu người, dùng Integer.MAX_VALUE để không giới hạn
        List<DonationSlot> slots = donationSlotRepository
                .findBySlotDateAndStatusAndRegisteredCountLessThan(today, SlotStatus.ACTIVE, Integer.MAX_VALUE);

        return slots.stream()
                .map(donationSlotMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void assignSlotToRegistration(DonationRegistration registration, Long slotId) {
        DonationSlot slot = donationSlotRepository.findById(slotId)
                .orElseThrow(() -> new ResourceNotFoundException(MessageConstants.SLOT_NOT_FOUND));

        if (slot.getRegisteredCount() >= slot.getMaxCapacity()) {
            log.warn("❌ Slot {} đã đầy. Không thể gán cho đăng ký {}", slotId, registration.getRegistrationId());
            throw new IllegalStateException(MessageConstants.SLOT_FULL);
        }

        registration.setSlot(slot);
        slot.setRegisteredCount(slot.getRegisteredCount() + 1);

        donationSlotRepository.save(slot);
        donationRegistrationRepository.save(registration);

        log.info("✅ Gán slot {} cho đơn đăng ký {} thành công.", slotId, registration.getRegistrationId());
    }

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
                        .build()
                ).collect(Collectors.toList());
    }

    public DonationSlotDTO getBestAvailableSlot() {
        List<DonationSlot> candidates = donationSlotRepository.findByStatus(SlotStatus.ACTIVE);

        return candidates.stream()
                .filter(slot -> slot.getRegisteredCount() < slot.getMaxCapacity()) // chỉ lấy slot còn trống
                .sorted(Comparator.comparing(DonationSlot::getSlotDate)
                        .thenComparing(DonationSlot::getRegisteredCount)) // Ưu tiên ngày gần và ít người
                .findFirst()
                .map(donationSlotMapper::toDTO)
                .orElse(null);
    }

}
