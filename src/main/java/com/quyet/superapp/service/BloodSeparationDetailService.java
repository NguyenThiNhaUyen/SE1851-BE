package com.quyet.superapp.service;

import com.quyet.superapp.dto.BloodSeparationDetailFullDTO;
import com.quyet.superapp.enums.BloodComponentType;
import com.quyet.superapp.mapper.BloodSeparationDetailMapper;
import com.quyet.superapp.repository.BloodSeparationDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service xử lý các thao tác liên quan đến chi tiết tách máu (BloodSeparationDetail).
 */
@Service
@RequiredArgsConstructor
public class BloodSeparationDetailService {

    private final BloodSeparationDetailRepository detailRepo;

    /**
     * Lấy danh sách chi tiết tách máu theo ID kết quả tách máu.
     * @param resultId ID kết quả tách máu
     * @return Danh sách DTO chi tiết
     */
    public List<BloodSeparationDetailFullDTO> getByResultId(Long resultId) {
        return detailRepo.findByResult_SeparationResultId(resultId)
                .stream()
                .map(BloodSeparationDetailMapper::toFullDTO)
                .collect(Collectors.toList());
    }

    /**
     * Lấy danh sách chi tiết tách máu theo mã túi máu.
     * @param bagCode Mã túi máu
     * @return Danh sách DTO chi tiết
     */
    public List<BloodSeparationDetailFullDTO> getByBagCode(String bagCode) {
        return detailRepo.findByResult_Order_BloodBag_BagCode(bagCode)
                .stream()
                .map(BloodSeparationDetailMapper::toFullDTO)
                .collect(Collectors.toList());
    }

    /**
     * Lấy danh sách chi tiết tách máu theo loại thành phần (tiểu cầu, huyết tương...).
     * @param type Loại thành phần máu
     * @return Danh sách DTO chi tiết
     */
    public List<BloodSeparationDetailFullDTO> getByComponentType(BloodComponentType type) {
        return detailRepo.findByComponentType(type)
                .stream()
                .map(BloodSeparationDetailMapper::toFullDTO)
                .collect(Collectors.toList());
    }
}
