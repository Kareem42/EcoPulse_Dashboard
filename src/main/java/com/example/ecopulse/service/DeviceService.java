package com.example.ecopulse.service;

import java.util.*;
import java.util.stream.Collectors;

import com.example.ecopulse.dto.DeviceRequest;
import com.example.ecopulse.dto.DeviceResponse;
import com.example.ecopulse.entity.Device;
import com.example.ecopulse.entity.User;
import com.example.ecopulse.repository.DeviceRepository;
import com.example.ecopulse.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class DeviceService {
    private final DeviceRepository deviceRepository;
    private final UserRepository userRepository;

    @Transactional
    public DeviceResponse createDevice(Long userId, DeviceRequest deviceRequest) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        Device device = Device.builder()
                .user(user)
                .name(deviceRequest.getName())
                .location(deviceRequest.getLocation())
                .type(deviceRequest.getType())
                .estimatedPowerWatts(deviceRequest.getEstimatePowerWatts())
                .build();

       deviceRepository.save(device);

       return mapToResponse(device);
    }

    public List<DeviceResponse> getUserDevices(Long userId) {
        return deviceRepository.findByUserId(userId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private DeviceResponse mapToResponse(Device device) {
        return DeviceResponse.builder()
                .id(device.getId())
                .name(device.getName())
                .location(device.getLocation())
                .type(device.getType())
                .estimatePowerWatts(device.getEstimatedPowerWatts())
                .build();
    }
}
