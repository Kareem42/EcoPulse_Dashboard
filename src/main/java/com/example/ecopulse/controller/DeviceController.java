package com.example.ecopulse.controller;

import com.example.ecopulse.service.DeviceService;
import com.example.ecopulse.dto.DeviceResponse;
import com.example.ecopulse.dto.DeviceRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.net.URI;

@RestController
@RequestMapping("/api/users/{userId}/devices")
@RequiredArgsConstructor
@CrossOrigin

// Exposing two REST endpoints for managing devices belonging to a user.
public class DeviceController {

    private final DeviceService deviceService;

    // POST /api/users/{userId}/devices
    @PostMapping
    public ResponseEntity<DeviceResponse> createDevice(
            @PathVariable Long userId,
            @Valid @RequestBody DeviceRequest request
    ) {
        DeviceResponse created = deviceService.createDevice(userId, request);
        // Build a Location header like /api/users/1/devices/10
        URI location = URI.create(String.format("/api/users/%d/devices/%d", userId, created.getId()));
        return ResponseEntity.created(location).body(created);
    }

    // GET /api/users/{userId}/devices
    @GetMapping
    public ResponseEntity<List<DeviceResponse>> getUserDevices(@PathVariable Long userId) {
        List<DeviceResponse> devices = deviceService.getUserDevices(userId);
        return ResponseEntity.ok(devices);
    }
}
