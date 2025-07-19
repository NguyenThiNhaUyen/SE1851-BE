package com.quyet.superapp.mapper;

<<<<<<< HEAD
import com.quyet.superapp.dto.TransfusionDTO;
import com.quyet.superapp.entity.Transfusion;
import org.springframework.stereotype.Component;
=======
import org.springframework.stereotype.Component;
import com.quyet.superapp.entity.Transfusion;
import com.quyet.superapp.dto.TransfusionDTO;
import com.quyet.superapp.entity.User;
import com.quyet.superapp.entity.BloodRequest;
import com.quyet.superapp.entity.BloodUnit;
>>>>>>> origin/main

@Component
public class TransfusionMapper {

<<<<<<< HEAD
    public TransfusionDTO toDTO(Transfusion t) {
        TransfusionDTO dto = new TransfusionDTO();

        dto.setId(t.getTransfusionId());
        dto.setTransfusionDate(t.getTransfusionDate());
        dto.setStatus(t.getStatus());
        dto.setNotes(t.getNotes());

        // Liên kết ID
        dto.setRequestId(t.getRequest() != null ? t.getRequest().getId() : null);
        dto.setRecipientId(t.getRecipient() != null ? t.getRecipient().getUserId() : null);
        dto.setBloodUnitId(t.getBloodUnit() != null ? t.getBloodUnit().getBloodUnitId() : null);

        // Thông tin người nhận
        dto.setRecipientName(t.getRecipientName());
        dto.setRecipientPhone(t.getRecipientPhone());

        // Từ yêu cầu máu
        if (t.getRequest() != null) {
            dto.setUrgencyLevel(t.getRequest().getUrgencyLevel().name());
            dto.setTriageLevel(t.getRequest().getTriageLevel());
        }

        // Từ túi máu
        if (t.getBloodUnit() != null) {
            dto.setBloodType(t.getBloodUnit().getBloodType().getDescription());
            dto.setBloodBagCode(t.getBloodUnit().getUnitCode());
            dto.setBloodUnitId(t.getBloodUnit().getBloodUnitId());
            dto.setVolumeMl(t.getVolumeTakenMl() != null ? t.getVolumeTakenMl() : t.getBloodUnit().getQuantityMl());
            dto.setBagCount(1); // hoặc tính tổng nếu có bảng phụ
        }



        // Người phê duyệt
        dto.setApprovedBy(t.getApprovedBy()); // thêm cột này vào entity nếu chưa có
=======
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
>>>>>>> origin/main

        return dto;
    }

<<<<<<< HEAD
=======
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
>>>>>>> origin/main
}
