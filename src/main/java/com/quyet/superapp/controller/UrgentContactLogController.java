package com.quyet.superapp.controller;

import com.quyet.superapp.entity.UrgentDonorContactLog;
import com.quyet.superapp.repository.UrgentDonorContactLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/contact-log")
@RequiredArgsConstructor
public class UrgentContactLogController {

    private final UrgentDonorContactLogRepository contactLogRepo;

    @GetMapping("/by-request")
    public ResponseEntity<List<UrgentDonorContactLog>> getLogsByRequest(@RequestParam Long requestId) {
        return ResponseEntity.ok(contactLogRepo.findByRequestId(requestId));
    }

    @PutMapping("/confirm")
    public ResponseEntity<?> confirmDonorResponse(@RequestParam Long logId) {
        return contactLogRepo.findById(logId).map(log -> {
            log.setStatus("CONFIRMED");
            contactLogRepo.save(log);
            return ResponseEntity.ok("Đã xác nhận người hiến máu phản hồi");
        }).orElse(ResponseEntity.notFound().build());
    }
}
