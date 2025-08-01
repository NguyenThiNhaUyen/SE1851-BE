package com.quyet.superapp.repository;

import com.quyet.superapp.entity.BloodSeparationSuggestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BloodSeparationSuggestionRepository extends JpaRepository<BloodSeparationSuggestion, Long> {
}
