package com.quyet.superapp.repository;

import com.quyet.superapp.entity.SeparationPresetConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SeparationPresetConfigRepository extends JpaRepository<SeparationPresetConfig, Long> {
    Optional<SeparationPresetConfig> findBestMatch(String gender, int weight, String method, boolean leukoreduced);

}
