package com.quyet.superapp.controller;

import com.quyet.superapp.entity.Report;
import com.quyet.superapp.service.ReportService;
<<<<<<< HEAD
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
=======
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
>>>>>>> origin/main
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
<<<<<<< HEAD

=======
@Validated
>>>>>>> origin/main
public class ReportController {

    @Autowired
    private ReportService service;

    @GetMapping
    public List<Report> getAll() {
        return service.getAll();
    }

<<<<<<< HEAD
    @GetMapping("/{id}")
    public ResponseEntity<Report> getById(@PathVariable Long id) {
=======
    @GetMapping("/by-id")
    public ResponseEntity<Report> getById(@RequestParam Long id) {
>>>>>>> origin/main
        return service.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/create")
    public Report create(@RequestBody Report report) {
        return service.create(report);
    }

<<<<<<< HEAD
    @PutMapping("/{id}")
    public ResponseEntity<Report> update(@PathVariable Long id, @RequestBody Report report) {
=======
    @PutMapping("/update")
    public ResponseEntity<Report> update(@RequestParam Long id, @RequestBody Report report) {
>>>>>>> origin/main
        Report updated = service.update(id, report);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

<<<<<<< HEAD
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
=======
    @DeleteMapping("/delete")
    public void delete(@RequestParam Long id) {
>>>>>>> origin/main
        service.deleteById(id);
    }
}

