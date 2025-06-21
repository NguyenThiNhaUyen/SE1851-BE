package com.quyet.superapp.service;

import com.quyet.superapp.dto.BloodSeparationDetailDTO;
import com.quyet.superapp.entity.BloodComponent;
import com.quyet.superapp.entity.BloodSeparationDetail;
import com.quyet.superapp.entity.BloodSeparationLog;
import com.quyet.superapp.entity.BloodType;
import com.quyet.superapp.exception.ResourceNotFoundException;
import com.quyet.superapp.mapper.BloodSeparationDetailMapper;
import com.quyet.superapp.repository.BloodComponentRepository;
import com.quyet.superapp.repository.BloodSeparationDetailRepository;
import com.quyet.superapp.repository.BloodSeparationLogRepository;
import com.quyet.superapp.repository.BloodTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BloodSeparationDetailService {

    private final BloodSeparationDetailRepository detailRepository;
    private final BloodSeparationLogRepository separationLogRepository;
    private final BloodTypeRepository bloodTypeRepository;
    private final BloodComponentRepository componentRepository;

    public List<BloodSeparationDetailDTO> getAllDetails() {
        return detailRepository.findAll()
                .stream()
                .map(BloodSeparationDetailMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<BloodSeparationDetailDTO> getBySeparationLogId(Long logId) {
        return detailRepository.findBySeparationLog_BloodSeparationLogId(logId)
                .stream()
                .map(BloodSeparationDetailMapper::toDTO)
                .collect(Collectors.toList());
    }

    public BloodSeparationDetailDTO create(BloodSeparationDetailDTO dto) {
        BloodSeparationDetail entity = BloodSeparationDetailMapper.toEntity(dto);

        // Gán các quan hệ
        BloodSeparationLog log = separationLogRepository.findById(dto.getSeparationLogId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy bản ghi tách máu (Separation Log)"));
        BloodType type = bloodTypeRepository.findById(dto.getBloodTypeId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy nhóm máu"));
        BloodComponent component = componentRepository.findById(dto.getComponentId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy thành phần máu"));

        entity.setSeparationLog(log);
        entity.setBloodType(type);
        entity.setComponent(component);

        return BloodSeparationDetailMapper.toDTO(detailRepository.save(entity));
    }

    public void delete(Long id) {
        if (!detailRepository.existsById(id)) {
            throw new ResourceNotFoundException("Không tìm thấy chi tiết tách máu cần xoá");
        }
        detailRepository.deleteById(id);
    }


}
