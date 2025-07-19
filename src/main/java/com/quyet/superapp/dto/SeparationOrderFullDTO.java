package com.quyet.superapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SeparationOrderFullDTO {
    private SeparationOrderDTO order;
    private List<BloodUnitDTO> units;
}
