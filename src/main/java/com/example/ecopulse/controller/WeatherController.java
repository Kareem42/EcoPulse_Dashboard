package com.example.ecopulse.controller;

import com.example.ecopulse.service.WeatherService;
import com.example.ecopulse.dto.CurrentWeatherDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/weather")
@RequiredArgsConstructor
@CrossOrigin
public class WeatherController {
    private final WeatherService weatherService;

    //GET /api/weather?city=Austin
    @GetMapping
    public ResponseEntity<CurrentWeatherDto> getCurrentWeather(
        @RequestParam String city
    ) {
        CurrentWeatherDto weather = weatherService.getCurrentWeather(city);
        return ResponseEntity.ok(weather);
    }
}
