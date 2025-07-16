package com.quyet.superapp.service;

import com.quyet.superapp.dto.CreateLabTestRequest;
import com.quyet.superapp.dto.LabTestResultDTO;
import com.quyet.superapp.entity.BloodUnit;
import com.quyet.superapp.entity.LabTestResult;
import com.quyet.superapp.entity.User;
import com.quyet.superapp.mapper.LabTestResultMapper;
import com.quyet.superapp.repository.BloodUnitRepository;
import com.quyet.superapp.repository.LabTestResultRepository;
import com.quyet.superapp.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LabTestService {

    private final LabTestResultRepository labTestRepo;
    private final BloodUnitRepository unitRepo;
    private final UserRepository userRepo;

    /**
     * ✅ Tạo kết quả xét nghiệm cho đơn vị máu
     */
    @Transactional
    public LabTestResultDTO createLabTestResult(CreateLabTestRequest request) {
        BloodUnit unit = getValidBloodUnit(request.getBloodUnitId());
        validateUnitNotTested(unit.getBloodUnitId());
        User tester = getValidUser(request.getTestedById());

        LabTestResult result = buildLabTestResult(unit, tester, request);
        LabTestResult saved = labTestRepo.save(result);

        return LabTestResultMapper.toDTO(saved);
    }

    /**
     * ✅ Truy xuất kết quả xét nghiệm theo ID đơn vị máu
     */
    public Optional<LabTestResultDTO> getByBloodUnit(Long bloodUnitId) {
        return labTestRepo.findByBloodUnit_BloodUnitId(bloodUnitId)
                .map(LabTestResultMapper::toDTO);
    }

    /**
     * ✅ Kiểm tra đơn vị máu đã được xét nghiệm chưa
     */
    public boolean isTested(Long bloodUnitId) {
        return labTestRepo.existsByBloodUnit_BloodUnitId(bloodUnitId);
    }

    /**
     * ✅ Lấy toàn bộ kết quả
     */
    public List<LabTestResultDTO> getAllResults() {
        return labTestRepo.findAll().stream()
                .map(LabTestResultMapper::toDTO)
                .toList();
    }

    /**
     * ✅ Xoá kết quả xét nghiệm theo ID
     */
    @Transactional
    public void deleteResult(Long id) {
        labTestRepo.deleteById(id);
    }

    // 🔍 Đảm bảo đơn vị máu tồn tại
    private BloodUnit getValidBloodUnit(Long unitId) {
        return unitRepo.findById(unitId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy đơn vị máu."));
    }

    // 🔍 Đảm bảo user tồn tại
    private User getValidUser(Long userId) {
        return userRepo.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy nhân viên xét nghiệm."));
    }

    // ❌ Nếu đã xét nghiệm thì throw lỗi
    private void validateUnitNotTested(Long unitId) {
        if (labTestRepo.existsByBloodUnit_BloodUnitId(unitId)) {
            throw new IllegalStateException("Đơn vị máu đã được xét nghiệm.");
        }
    }

    // 🧪 Tạo entity LabTestResult từ request
    private LabTestResult buildLabTestResult(BloodUnit unit, User tester, CreateLabTestRequest req) {
        LabTestResult result = new LabTestResult();
        result.setBloodUnit(unit);
        result.setTestedBy(tester);
        result.setTestedAt(LocalDateTime.now());

        result.setHivNegative(req.isHivNegative());
        result.setHbvNegative(req.isHbvNegative());
        result.setHcvNegative(req.isHcvNegative());
        result.setSyphilisNegative(req.isSyphilisNegative());
        result.setMalariaNegative(req.isMalariaNegative());

        boolean passed = req.isHivNegative() &&
                req.isHbvNegative() &&
                req.isHcvNegative() &&
                req.isSyphilisNegative() &&
                req.isMalariaNegative();
        result.setPassed(passed);

        return result;
    }
}
