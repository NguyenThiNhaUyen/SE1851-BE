package com.quyet.superapp.controller;

import com.quyet.superapp.entity.CompatibilityRule;
import com.quyet.superapp.service.CompatibilityRuleService;

<<<<<<< HEAD
import org.springframework.beans.factory.annotation.Autowired;
=======
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
>>>>>>> origin/main
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/compatibility-rules")
@CrossOrigin(origins = "http://localhost:5713")
<<<<<<< HEAD
=======
@Validated
>>>>>>> origin/main
public class CompatibilityRuleController {

    @Autowired
    private CompatibilityRuleService service;

    @GetMapping
    public List<CompatibilityRule> getAll() {
        return service.getAllRules();
    }

    @GetMapping("/filter")
    public List<CompatibilityRule> getCompatibleRules(
            @RequestParam String recipientType,
            @RequestParam String component
    ) {
        return service.getCompatibleRules(recipientType, component);
    }

    @PostMapping("/create")
    public CompatibilityRule create(@RequestBody CompatibilityRule rule) {
        return service.addRule(rule);
    }

<<<<<<< HEAD
    @PutMapping("/{id}")
    public CompatibilityRule update(@PathVariable Long id, @RequestBody CompatibilityRule rule) {
        return service.updateRule(id, rule);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
=======
    @PutMapping("/update")
    public CompatibilityRule update(@RequestParam Long id, @RequestBody CompatibilityRule rule) {
        return service.updateRule(id, rule);
    }

    @DeleteMapping("/delete")
    public void delete(@RequestParam Long id) {
>>>>>>> origin/main
        service.deleteRule(id);
    }
}
