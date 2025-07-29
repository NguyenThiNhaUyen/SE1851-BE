package com.quyet.superapp.service;

import com.quyet.superapp.entity.Report;
import com.quyet.superapp.repository.ReportRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;

    public List<Report> getAll() {
        return reportRepository.findAll();
    }

    public Optional<Report> getById(Long id) {
        return reportRepository.findById(id);
    }

    public Report create(Report report) {
        if (report.getContent() == null || report.getContent().isBlank()) {
            throw new IllegalArgumentException("Report content must not be empty.");
        }
        report.setCreatedAt(LocalDateTime.now());
        return reportRepository.save(report);
    }

    public void deleteById(Long id) {
        if (!reportRepository.existsById(id)) {
            throw new NoSuchElementException("Report with ID " + id + " not found.");
        }
        reportRepository.deleteById(id);
    }

    @Transactional
    public Report update(Long id, Report updatedReport) {
        Report existing = reportRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Report with ID " + id + " not found."));

        updateFields(existing, updatedReport);
        return reportRepository.save(existing);
    }

    private void updateFields(Report existing, Report updated) {
        if (updated.getReportType() != null) {
            existing.setReportType(updated.getReportType());
        }
        if (updated.getContent() != null) {
            existing.setContent(updated.getContent());
        }
        if (updated.getGeneratedBy() != null) {
            existing.setGeneratedBy(updated.getGeneratedBy());
        }
        // Không cho phép sửa createdAt từ bên ngoài
    }
}
