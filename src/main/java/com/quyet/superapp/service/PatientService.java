package com.quyet.superapp.service;

import com.quyet.superapp.dto.BloodRequestWithNewPatientDTO;
import com.quyet.superapp.dto.CreateBloodRequestDTO;
import com.quyet.superapp.entity.Patient;
import com.quyet.superapp.mapper.PatientMapper;
import com.quyet.superapp.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepo;
    private final PatientMapper patientMapper;

    public Patient getOrCreateFromDTO(CreateBloodRequestDTO dto) {
        if (dto.getCitizenId() != null) {
            return patientRepo.findByCitizenId(dto.getCitizenId())
                    .orElseGet(() -> patientRepo.save(patientMapper.toEntity(dto)));
        }
        return patientRepo.save(patientMapper.toEntity(dto));
    }



    // ⚠️ Tạo mới nếu chưa tồn tại
    public Patient getOrCreateFromDTO(BloodRequestWithNewPatientDTO dto) {
        if (dto.getCitizenId() != null) {
            return patientRepo.findByCitizenId(dto.getCitizenId())
                    .orElseGet(() -> patientRepo.save(patientMapper.toEntity(dto)));
        }
        return patientRepo.save(patientMapper.toEntity(dto));
    }


    // ✅ Tìm theo ID nếu được truyền từ suspectedPatientId
    public Patient findById(Long id) {
        return patientRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bệnh nhân với ID = " + id));
    }
}
