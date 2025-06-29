package com.quyet.superapp.service;

import com.quyet.superapp.dto.BloodSeparationDetailFullDTO;
import com.quyet.superapp.enums.BloodComponentType;
import com.quyet.superapp.mapper.BloodSeparationDetailMapper;
import com.quyet.superapp.repository.BloodSeparationDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BloodSeparationDetailService {

    private final BloodSeparationDetailRepository detailRepository;

    public List<BloodSeparationDetailFullDTO> getDetailsByResultId(Long resultId) {
        return detailRepository.findByResult_SeparationResultId(resultId).stream()
                .map(BloodSeparationDetailMapper::toFullDTO)
                .collect(Collectors.toList());
    }

    public List<BloodSeparationDetailFullDTO> getDetailsByBagCode(String bagCode) {
        return detailRepository.findByResult_Order_BloodBag_BagCode(bagCode).stream()
                .map(BloodSeparationDetailMapper::toFullDTO)
                .collect(Collectors.toList());
    }

    public List<BloodSeparationDetailFullDTO> getDetailsByComponentType(BloodComponentType type) {
        return detailRepository.findByComponentType(type).stream()
                .map(BloodSeparationDetailMapper::toFullDTO)
                .collect(Collectors.toList());
    }


}
