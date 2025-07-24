package com.quyet.superapp.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateBloodRequestDTO {
    private Long requesterId;
    private Long bloodTypeId;
    private Long componentId;
    private Integer quantityMl;
    private String urgencyLevel;
    private String reason;

    // Optional for cấp cứu / code red
    private Boolean isUnmatched;
    private String triageLevel;
    private Long codeRedId;
}