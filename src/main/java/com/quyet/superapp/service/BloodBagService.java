package com.quyet.superapp.service;

import com.quyet.superapp.dto.BloodBagDTO;
import com.quyet.superapp.dto.UpdateBloodBagRequest;
import com.quyet.superapp.entity.BloodBag;
import com.quyet.superapp.entity.BloodType;
import com.quyet.superapp.entity.Donation;
import com.quyet.superapp.entity.User;
import com.quyet.superapp.enums.BloodBagStatus;
import com.quyet.superapp.enums.TestStatus;
import com.quyet.superapp.mapper.BloodBagMapper;
import com.quyet.superapp.repository.BloodBagRepository;
import com.quyet.superapp.repository.DonationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.quyet.superapp.util.CodeGeneratorUtil;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BloodBagService {

    private final BloodBagRepository bloodBagRepository;
    private final DonationRepository donationRepository;

    public List<BloodBagDTO> getAll() {
        return bloodBagRepository.findAll()
                .stream()
                .map(BloodBagMapper::toDTO)
                .collect(Collectors.toList());
    }

    public BloodBagDTO getById(Long id) {
        BloodBag bag = bloodBagRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy túi máu với ID " + id));
        return BloodBagMapper.toDTO(bag);
    }

    /**
     * ✅ Tạo túi máu từ DTO + donationId
     * - Nếu không có bloodTypeId → tự lấy từ donation hoặc registration
     */
    public BloodBagDTO createFromDonation(BloodBagDTO dto, Long donationId) {
        Donation donation = donationRepository.findById(donationId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy donation ID " + donationId));

        BloodType bloodType = donation.getBloodType();
        if (bloodType == null) {
            if (donation.getRegistration() != null && donation.getRegistration().getBloodType() != null) {
                bloodType = donation.getRegistration().getBloodType();
                donation.setBloodType(bloodType);
                donationRepository.save(donation);
            } else {
                throw new IllegalArgumentException("Không xác định được nhóm máu từ Donation hoặc Registration.");
            }
        }

        // ✅ Sửa đúng số lượng tham số truyền vào mapper
        User donor = donation.getUser();
        BloodBag bag = BloodBagMapper.fromDTO(dto, bloodType, donor);
        bag.setDonation(donation);

        BloodBag saved = bloodBagRepository.save(bag);
        return BloodBagMapper.toDTO(saved);
    }

    @Transactional
    public BloodBagDTO update(Long id, UpdateBloodBagRequest req) {
        BloodBag bag = bloodBagRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy túi máu với ID " + id));

        if (req.getVolume() != null) bag.setVolume(req.getVolume());
        if (req.getHematocrit() != null) bag.setHematocrit(req.getHematocrit());
        if (req.getStatus() != null) bag.setStatus(req.getStatus());
        if (req.getTestStatus() != null) bag.setTestStatus(req.getTestStatus());
        if (req.getNote() != null) bag.setNote(req.getNote());

        return BloodBagMapper.toDTO(bag);
    }

    public void delete(Long id) {
        if (!bloodBagRepository.existsById(id)) {
            throw new IllegalArgumentException("Không tồn tại túi máu với ID " + id);
        }
        bloodBagRepository.deleteById(id);
    }

    public Optional<BloodBagDTO> findByCode(String code) {
        return bloodBagRepository.findByBagCode(code)
                .map(BloodBagMapper::toDTO);
    }

    public BloodBag createFromDonation(Donation donation) {
        BloodBag bag = new BloodBag();
        bag.setBagCode(CodeGeneratorUtil.generateBloodBagCode()); // ✅ sinh mã tự động
        bag.setDonor(donation.getUser());
        bag.setBloodType(donation.getBloodType()); // ✅ chỉ cần BloodType có description
        bag.setVolume(donation.getVolumeMl());
        bag.setCollectedAt(LocalDateTime.now());
        bag.setStatus(BloodBagStatus.COLLECTED);
        bag.setTestStatus(TestStatus.PENDING);
        bag.setDonation(donation);

        return bloodBagRepository.save(bag);
    }
}
