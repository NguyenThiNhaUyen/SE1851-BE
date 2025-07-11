package com.quyet.superapp.dto;
// DTO: Khi đã biết bệnh nhân (có patientId)
import lombok.Data;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

@Data
public class BloodRequestWithExistingPatientDTO {
    @NotNull
    private Long patientId;

    @NotBlank
    private String requesterName;
    @NotBlank
    private String reason;
    @NotBlank
    private String urgencyLevel;
    @NotBlank
    private String triageLevel;
    @NotNull
    private Integer quantityBag;
    @NotNull
    private Integer quantityMl;
    @NotBlank
    private String componentName;
    @NotNull
    private LocalDateTime neededAt;

    private Boolean crossmatchRequired;
    private Boolean hasTransfusionHistory;
    private Boolean hasReactionHistory;
    private Boolean isPregnant;
    private Boolean hasAntibodyIssue;

    private String warningNote;
    private String specialNote;

    private Boolean deferredPayment;
    private String deferredPaymentReason;

}

