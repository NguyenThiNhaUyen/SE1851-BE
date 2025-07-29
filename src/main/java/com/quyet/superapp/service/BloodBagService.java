package com.quyet.superapp.service;

import com.quyet.superapp.dto.BloodBagDTO;
import com.quyet.superapp.dto.UpdateBloodBagRequest;
import com.quyet.superapp.entity.BloodBag;
import com.quyet.superapp.entity.BloodType;
import com.quyet.superapp.entity.Donation;
import com.quyet.superapp.entity.User;
import com.quyet.superapp.enums.BloodBagStatus;
import com.quyet.superapp.enums.TestStatus;
import com.quyet.superapp.exception.ResourceNotFoundException;
import com.quyet.superapp.mapper.BloodBagMapper;
import com.quyet.superapp.repository.BloodBagRepository;
import com.quyet.superapp.repository.DonationRepository;
import com.quyet.superapp.util.CodeGeneratorUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BloodBagService {

    private final BloodBagRepository bloodBagRepository;
    private final DonationRepository donationRepository;

    /**
     * Lấy tất cả túi máu
     */
    public List<BloodBagDTO> getAll() {
        return bloodBagRepository.findAll().stream()
                .map(BloodBagMapper::toDTO)
                .toList();
    }

    /**
     * Lấy túi máu theo ID
     */
    public BloodBagDTO getById(Long id) {
        BloodBag bloodBag = bloodBagRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy túi máu với ID " + id));
        return BloodBagMapper.toDTO(bloodBag);
    }

    /**
     * Tạo túi máu từ thông tin DTO và donationId
     */
    public BloodBagDTO createFromDonation(BloodBagDTO dto, Long donationId) {
        Donation donation = donationRepository.findById(donationId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy đơn hiến máu với ID " + donationId));

        BloodType bloodType = resolveBloodTypeFromDonation(donation);
        User donor = donation.getUser();

        BloodBag bloodBag = BloodBagMapper.fromDTO(dto, bloodType, donor);
        bloodBag.setDonation(donation);

        BloodBag saved = bloodBagRepository.save(bloodBag);
        return BloodBagMapper.toDTO(saved);
    }

    /**
     * Cập nhật thông tin túi máu
     */
    @Transactional
    public BloodBagDTO update(Long id, UpdateBloodBagRequest request) {
        BloodBag bloodBag = bloodBagRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy túi máu với ID " + id));

        if (request.getVolume() != null) bloodBag.setVolume(request.getVolume());
        if (request.getHematocrit() != null) bloodBag.setHematocrit(request.getHematocrit());
        if (request.getStatus() != null) bloodBag.setStatus(request.getStatus());
        if (request.getTestStatus() != null) bloodBag.setTestStatus(request.getTestStatus());
        if (request.getNote() != null) bloodBag.setNote(request.getNote());

        return BloodBagMapper.toDTO(bloodBag);
    }

    /**
     * Xoá túi máu theo ID
     */
    public void delete(Long id) {
        if (!bloodBagRepository.existsById(id)) {
            throw new ResourceNotFoundException("Không tồn tại túi máu với ID " + id);
        }
        bloodBagRepository.deleteById(id);
    }

    /**
     * Tìm túi máu theo mã code
     */
    public Optional<BloodBagDTO> findByCode(String code) {
        return bloodBagRepository.findByBagCode(code)
                .map(BloodBagMapper::toDTO);
    }

    /**
     * Tạo túi máu mới từ đơn hiến máu (được sử dụng nội bộ)
     */
    public BloodBag createFromDonation(Donation donation) {
        BloodBag bloodBag = new BloodBag();
        bloodBag.setBagCode(CodeGeneratorUtil.generateBloodBagCode());
        bloodBag.setDonor(donation.getUser());
        bloodBag.setBloodType(donation.getBloodType());
        bloodBag.setVolume(donation.getVolumeMl());
        bloodBag.setCollectedAt(LocalDateTime.now());
        bloodBag.setStatus(BloodBagStatus.COLLECTED);
        bloodBag.setTestStatus(TestStatus.PENDING);
        bloodBag.setDonation(donation);

        return bloodBagRepository.save(bloodBag);
    }

    /**
     * Lấy nhóm máu từ đơn hiến máu hoặc đăng ký nếu chưa có
     */
    private BloodType resolveBloodTypeFromDonation(Donation donation) {
        BloodType bloodType = donation.getBloodType();

        if (bloodType == null && donation.getRegistration() != null) {
            bloodType = donation.getRegistration().getBloodType();
            if (bloodType != null) {
                donation.setBloodType(bloodType);
                donationRepository.save(donation);
            }
        }

        if (bloodType == null) {
            throw new IllegalArgumentException("Không xác định được nhóm máu từ Donation hoặc Registration.");
        }

        return bloodType;
    }
}
