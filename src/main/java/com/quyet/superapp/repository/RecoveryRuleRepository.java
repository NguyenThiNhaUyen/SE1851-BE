package com.quyet.superapp.repository;

import com.quyet.superapp.entity.RecoveryRule;
import com.quyet.superapp.enums.BloodComponentType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RecoveryRuleRepository extends JpaRepository<RecoveryRule, Long> {

    Optional<RecoveryRule> findByComponentType(BloodComponentType componentType);
}
