package com.quyet.superapp.mapper;

import com.quyet.superapp.dto.BloodUnitDTO;
import com.quyet.superapp.dto.SeparationOrderDTO;
import com.quyet.superapp.dto.SeparationOrderFullDTO;
import com.quyet.superapp.entity.SeparationOrder;

import java.util.List;

public class SeparationOrderFullMapper {
    public static SeparationOrderFullDTO toFullDTO(SeparationOrder order, List<BloodUnitDTO> units) {
        SeparationOrderDTO orderDTO = SeparationOrderMapper.toDTO(order);
        return new SeparationOrderFullDTO(orderDTO, units);
    }
}
