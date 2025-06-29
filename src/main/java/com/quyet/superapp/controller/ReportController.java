package com.quyet.superapp.controller;

import com.quyet.superapp.entity.Report;
import com.quyet.superapp.service.ReportService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
@Validated
public class ReportController {

    @Autowired
    private ReportService service;

    @GetMapping
    public List<Report> getAll() {
        return service.getAll();
    }

    @GetMapping("/by-id")
    public ResponseEntity<Report> getById(@RequestParam Long id) {
        return service.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/create")
    public Report create(@RequestBody Report report) {
        return service.create(report);
    }

    @PutMapping("/update")
    public ResponseEntity<Report> update(@RequestParam Long id, @RequestBody Report report) {
        Report updated = service.update(id, report);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/delete")
    public void delete(@RequestParam Long id) {
        service.deleteById(id);
    }
}

