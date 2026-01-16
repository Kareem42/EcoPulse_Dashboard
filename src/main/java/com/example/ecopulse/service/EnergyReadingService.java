package com.example.ecopulse.service;

import com.example.ecopulse.dto.DailyEnergyStats;
import com.example.ecopulse.dto.EnergyReadingRequest;
import com.example.ecopulse.entity.Device;
import com.example.ecopulse.entity.EnergyReading;
import com.example.ecopulse.repository.DeviceRepository;
import com.example.ecopulse.repository.EnergyReadingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class EnergyReadingService {

    private final EnergyReadingRepository energyReadingRepository;
    private final DeviceRepository deviceRepository;

    @Transactional
    public void logReading(Long userId, EnergyReadingRequest request) {
        Device device = deviceRepository.findById(request.getDeviceId())
                .orElseThrow(() -> new RuntimeException("Device not found"));

        // Security check: we are ensuring the device belongs to the user
        if(!device.getUser().getId().equals(userId)){
            throw new RuntimeException("Not Authorized");
        }

        EnergyReading reading = EnergyReading.builder()
                .device(device)
                .date(LocalDate.from(request.getDate()))
                .energyKwh(request.getEnergyKwh())
                .source(request.getSource())
                .build();

        energyReadingRepository.save(reading);
    }

    public List<DailyEnergyStats> getDailyEnergyStats(Long userId, int days) {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(days);

        List<EnergyReading> readings = energyReadingRepository.findByUserIdAndDateBetween(userId, startDate, endDate);

        Map<LocalDate, Double> dailyTotals = readings.stream()
                .collect(Collectors.groupingBy(EnergyReading::getDate,
                         Collectors.summingDouble(EnergyReading::getEnergyKwh)));

        return dailyTotals.entrySet().stream()
                .map(entry -> new DailyEnergyStats(entry.getKey(), entry.getValue()))
                .sorted((a, b) -> a.getDate().compareTo(b.getDate()))
                .collect(Collectors.toList());

    }
}
