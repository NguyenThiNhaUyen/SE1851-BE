package com.quyet.superapp.mapper;

import com.quyet.superapp.dto.ApheresisProcedureResultDTO;
import com.quyet.superapp.entity.ApheresisMachine;
import com.quyet.superapp.entity.ApheresisProcedureResult;
import com.quyet.superapp.entity.Donation;
import com.quyet.superapp.entity.User;

public class ApheresisProcedureResultMapper {
    public static ApheresisProcedureResultDTO toDTO(ApheresisProcedureResult entity) {
        ApheresisProcedureResultDTO dto = new ApheresisProcedureResultDTO();
        dto.setApheresisProcedureResultId(entity.getApheresisProcedureResultId());
        dto.setDonationId(
                entity.getDonation() != null ? entity.getDonation().getDonationId() : null
        );
        dto.setMachineId(
                entity.getMachine() != null ? entity.getMachine().getApheresisMachineId() : null
        );
        dto.setProcedureStart(entity.getProcedureStart());
        dto.setProcedureEnd(entity.getProcedureEnd());
        dto.setTotalVolumeMl(entity.getTotalVolumeMl());
        dto.setNote(entity.getNote());
        dto.setPerformedBy(
                entity.getPerformedBy() != null ? entity.getPerformedBy().getUsername() : null
        );
        return dto;
    }
    public static ApheresisProcedureResult fromDTO(
            ApheresisProcedureResultDTO dto,
            Donation donation,
            ApheresisMachine machine,
            User performedBy
    ) {
        ApheresisProcedureResult entity = new ApheresisProcedureResult();
        entity.setApheresisProcedureResultId(dto.getApheresisProcedureResultId());
        entity.setDonation(donation);
        entity.setMachine(machine);
        entity.setProcedureStart(dto.getProcedureStart());
        entity.setProcedureEnd(dto.getProcedureEnd());
        entity.setTotalVolumeMl(dto.getTotalVolumeMl());
        entity.setNote(dto.getNote());
        entity.setPerformedBy(performedBy);
        return entity;
    }
}
