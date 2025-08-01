package com.quyet.superapp.repository;

import com.quyet.superapp.entity.SeparationResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SeparationResultRepository extends JpaRepository<SeparationResult, Long> {

}
