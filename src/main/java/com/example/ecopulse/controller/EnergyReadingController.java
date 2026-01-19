package com.example.ecopulse.controller;

import com.example.ecopulse.dto.DailyEnergyStats;
import com.example.ecopulse.dto.EnergyReadingRequest;
import com.example.ecopulse.service.EnergyReadingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;


@RestController
@RequestMapping("/api/users/{userId}/readings")
@RequiredArgsConstructor
@CrossOrigin
public class EnergyReadingController {
    private final EnergyReadingService energyReadingService;

    // POST /api/users/{userId}/readings
    @PostMapping
    public ResponseEntity<Void> logReading(
            @PathVariable Long userId,
            @Valid @RequestBody EnergyReadingRequest energyReadingRequest
    ) {
        energyReadingService.logReading(userId, energyReadingRequest);
        return ResponseEntity.noContent().build();
    }

    //GET /api/users/{userId}/readings/daily-stats?days=7
    @GetMapping("daily-stats")
    public ResponseEntity<List<DailyEnergyStats>> getDailyEnergyStats(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "7") int days
    ) {
        List<DailyEnergyStats> stats = energyReadingService.getDailyEnergyStats(userId, days);
        return ResponseEntity.ok(stats);
    }
}
