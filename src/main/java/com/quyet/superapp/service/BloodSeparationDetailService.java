package com.quyet.superapp.service;

import com.quyet.superapp.dto.BloodSeparationDetailFullDTO;
import com.quyet.superapp.enums.BloodComponentType;
import com.quyet.superapp.mapper.BloodSeparationDetailMapper;
import com.quyet.superapp.repository.BloodSeparationDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service xử lý các thao tác liên quan đến chi tiết tách máu (BloodSeparationDetail).
 */
@Service
@RequiredArgsConstructor
public class BloodSeparationDetailService {

    private final BloodSeparationDetailRepository separationDetailRepo;

    /**
     * Lấy danh sách chi tiết tách máu theo ID kết quả tách máu.
     *
     * @param resultId ID của kết quả tách máu
     * @return Danh sách chi tiết dưới dạng DTO
     */
    public List<BloodSeparationDetailFullDTO> getByResultId(Long resultId) {
        return separationDetailRepo.findByResult_SeparationResultId(resultId).stream()
                .map(BloodSeparationDetailMapper::toFullDTO)
                .toList();
    }

    /**
     * Lấy danh sách chi tiết tách máu theo mã túi máu.
     *
     * @param bagCode Mã của túi máu
     * @return Danh sách chi tiết dưới dạng DTO
     */
    public List<BloodSeparationDetailFullDTO> getByBagCode(String bagCode) {
        return separationDetailRepo.findByResult_Order_BloodBag_BagCode(bagCode).stream()
                .map(BloodSeparationDetailMapper::toFullDTO)
                .toList();
    }

    /**
     * Lấy danh sách chi tiết tách máu theo loại thành phần máu (ví dụ: huyết tương, tiểu cầu,...).
     *
     * @param type Loại thành phần máu
     * @return Danh sách chi tiết dưới dạng DTO
     */
    public List<BloodSeparationDetailFullDTO> getByComponentType(BloodComponentType type) {
        return separationDetailRepo.findByComponentType(type).stream()
                .map(BloodSeparationDetailMapper::toFullDTO)
                .toList();
    }
}
