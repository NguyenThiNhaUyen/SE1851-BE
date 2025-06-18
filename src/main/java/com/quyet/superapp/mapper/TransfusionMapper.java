package com.quyet.superapp.mapper;

import org.springframework.stereotype.Component;
import com.quyet.superapp.entity.Transfusion;
import com.quyet.superapp.dto.TransfusionDTO;
import com.quyet.superapp.entity.User;
import com.quyet.superapp.entity.BloodRequest;
import com.quyet.superapp.entity.BloodUnit;

@Component
public class TransfusionMapper {

    /** Entity → DTO */
    public TransfusionDTO toDTO(Transfusion e) {
        if (e == null) {
            return null;
        }

        TransfusionDTO dto = new TransfusionDTO();
        // Map các trường cơ bản
        dto.setId(e.getTransfusionId());
        dto.setTransfusionDate(e.getTransfusionDate());
        dto.setStatus(e.getStatus());
        dto.setNotes(e.getNotes());

        // Map quan hệ, chỉ lấy ID
        User recipient = e.getRecipient();
        if (recipient != null) {
            dto.setRecipientId(recipient.getUserId());
        }

        BloodRequest request = e.getRequest();
        if (request != null) {
            dto.setRequestId(request.getBloodRequestId());
        }

        BloodUnit unit = e.getBloodUnit();
        if (unit != null) {
            dto.setBloodUnitId(unit.getBloodUnitId());
        }

        return dto;
    }

    /** (Tùy chọn) DTO → Entity */
    public Transfusion toEntity(TransfusionDTO dto) {
        if (dto == null) {
            return null;
        }

        Transfusion e = new Transfusion();
        e.setTransfusionId(dto.getId());
        e.setTransfusionDate(dto.getTransfusionDate());
        e.setStatus(dto.getStatus());
        e.setNotes(dto.getNotes());

        if (dto.getRecipientId() != null) {
            User u = new User();
            u.setUserId(dto.getRecipientId());
            e.setRecipient(u);
        }
        if (dto.getRequestId() != null) {
            BloodRequest r = new BloodRequest();
            r.setBloodRequestId(dto.getRequestId());
            e.setRequest(r);
        }
        if (dto.getBloodUnitId() != null) {
            BloodUnit bu = new BloodUnit();
            bu.setBloodUnitId(dto.getBloodUnitId());
            e.setBloodUnit(bu);
        }

        return e;
    }
}
