package com.quyet.superapp.repository;

import com.quyet.superapp.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Long> {
}
