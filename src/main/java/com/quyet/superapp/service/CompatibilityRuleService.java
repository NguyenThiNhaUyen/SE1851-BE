package com.quyet.superapp.service;

import com.quyet.superapp.entity.BloodType;
import com.quyet.superapp.entity.CompatibilityRule;
import com.quyet.superapp.repository.CompatibilityRuleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CompatibilityRuleService {

    private final CompatibilityRuleRepository compatibilityRuleRepository;

    /**
     * Lấy tất cả các quy tắc tương thích
     */
    public List<CompatibilityRule> getAllRules() {
        return compatibilityRuleRepository.findAll();
    }

    /**
     * Thêm mới một quy tắc tương thích
     */
    public CompatibilityRule addRule(CompatibilityRule rule) {
        return compatibilityRuleRepository.save(rule);
    }

    /**
     * Cập nhật một quy tắc tương thích dựa trên ID
     */
    public Optional<CompatibilityRule> updateRule(Long id, CompatibilityRule updatedRule) {
        return compatibilityRuleRepository.findById(id).map(existingRule -> {
            existingRule.setRecipientType(updatedRule.getRecipientType());
            existingRule.setDonorType(updatedRule.getDonorType());
            existingRule.setComponent(updatedRule.getComponent());
            existingRule.setIsCompatible(updatedRule.getIsCompatible());
            return compatibilityRuleRepository.save(existingRule);
        });
    }

    /**
     * Xoá một quy tắc theo ID
     */
    public void deleteRule(Long id) {
        compatibilityRuleRepository.deleteById(id);
    }

    /**
     * Lấy tất cả các quy tắc tương thích dựa trên nhóm máu người nhận và loại thành phần máu
     */
    public List<CompatibilityRule> getCompatibleRules(String recipientType, String component) {
        return compatibilityRuleRepository
                .findByRecipientType_DescriptionAndComponent_NameAndIsCompatibleTrue(recipientType, component);
    }

    /**
     * Lấy danh sách các nhóm máu người cho tương thích với người nhận
     */
    public List<BloodType> getCompatibleDonors(String recipientType, String component) {
        return getCompatibleRules(recipientType, component).stream()
                .map(CompatibilityRule::getDonorType)
                .distinct()
                .toList();
    }
}
