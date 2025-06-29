package com.quyet.superapp.controller;

import com.quyet.superapp.service.DonationRegistrationService;
import com.quyet.superapp.service.EligibilityEvaluationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/eligibility")
@RequiredArgsConstructor
public class EligibilityEvaluationController {

    private final EligibilityEvaluationService eligibilityEvaluationService;

    @PostMapping("/evaluate")
    public ResponseEntity<String> evaluateAndCreate(@RequestParam Long registrationId) {
        String result = eligibilityEvaluationService.evaluateAndCreateDonation(registrationId);
        return ResponseEntity.ok(result);
    }
}
