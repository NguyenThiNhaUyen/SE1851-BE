package com.quyet.superapp.controller;

import com.quyet.superapp.dto.CreateLabTestRequest;
import com.quyet.superapp.dto.LabTestResultDTO;
import com.quyet.superapp.service.LabTestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lab-tests")
@RequiredArgsConstructor
@Validated
public class LabTestController {

    private final LabTestService labTestService;

    @PostMapping
    public ResponseEntity<LabTestResultDTO> createLabTest(@RequestBody CreateLabTestRequest request) {
        LabTestResultDTO result = labTestService.createLabTestResult(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @GetMapping("/by-blood-unit")
    public ResponseEntity<LabTestResultDTO> getByBloodUnit(@RequestParam Long bloodUnitId) {
        return labTestService.getByBloodUnit(bloodUnitId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/check")
    public ResponseEntity<Boolean> checkTested(@RequestParam  Long bloodUnitId) {
        return ResponseEntity.ok(labTestService.isTested(bloodUnitId));
    }

    @GetMapping
    public ResponseEntity<List<LabTestResultDTO>> getAll() {
        List<LabTestResultDTO> results = labTestService.getAllResults();
        return ResponseEntity.ok(results);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteResult(@RequestParam Long labTestResultId) {
        labTestService.deleteResult(labTestResultId);
        return ResponseEntity.noContent().build();
    }
}
