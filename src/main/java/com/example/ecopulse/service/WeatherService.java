package com.example.ecopulse.service;

import com.example.ecopulse.dto.CurrentWeatherDto;
import com.example.ecopulse.dto.WeatherResponse;
import com.example.ecopulse.entity.WeatherCache;
import com.example.ecopulse.repository.WeatherCacheRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class WeatherService {

    private final WeatherCacheRepository weatherCacheRepository;
    private final RestTemplate restTemplate;

    @Value("${weather.api.key}")
    private String apiKey;

    @Value("${weather.api.url}")
    private String apiUrl;

    @Value("${weather.api.cache-duration-minutes:30}")
    private Integer cacheDurationMinutes;

    /**
     * Get current weather for a city.
     * First checks cache; if expired or missing, fetches from API
     */
    public CurrentWeatherDto getCurrentWeather(String city) {
        // Checking the cache first
        Optional<WeatherCache> cachedWeather = weatherCacheRepository.findFirstByCityOrderByTimestampDesc(city);

        if (cachedWeather.isPresent()) {
            WeatherCache weatherCache = cachedWeather.get();
            if(!isCacheExpired(weatherCache)){
                log.info("Returning cached weather for {}", city);
                return mapToDto(weatherCache);
            }
        }
        // Cache miss or expired-fetch from API
        log.info("Fetching fresh weather data for {}", city);
        CurrentWeatherDto weather = fetchFromApi(city);

        //Save to cache
        WeatherCache weatherCache = WeatherCache.builder()
                .city(city)
                .temperature(weather.getTemperature())
                .condition(weather.getCondition())
                .timestamp(LocalDateTime.now())
                .build();
        weatherCacheRepository.save(weatherCache);

        return weather;
    }

    /**
     * Fetch aka Response weather from OpenWeatherMap API
     */
    private CurrentWeatherDto fetchFromApi(String city) {
        try{
            String url = String.format("%s?q=%s&appid=%s&units=imperial", apiUrl, city, apiKey);
            WeatherResponse response = restTemplate.getForObject(url, WeatherResponse.class);

            if (response == null || response.getMain() == null) {
                throw new RuntimeException("Invalid response from weather API");
            }
            return CurrentWeatherDto.builder()
                    .city(response.getName())
                    .temperature(response.getMain().getTemp())
                    .humidity(response.getMain().getHumidity())
                    .pressure(response.getMain().getPressure())
                    .condition(response.getWeather()[0].getMain())
                    .description(response.getWeather()[0].getDescription())
                    .build();
        } catch (RestClientException e) {
            log.error("Error while fetching weather from API: {}", city, e);
            throw new RuntimeException("Failed to fetch weather data " + e);
        }
    }

    /**
     * Checking to see if the cache weather is still valid
     */
    private boolean isCacheExpired(WeatherCache weatherCache) {
        LocalDateTime expirationTime = weatherCache.getTimestamp().plusMinutes(cacheDurationMinutes);
        return LocalDateTime.now().isAfter(expirationTime);
    }

    /**
     * Convert WeatherCache entity to DTO
     */
    private CurrentWeatherDto mapToDto(WeatherCache weatherCache) {
        return CurrentWeatherDto.builder()
                .city(weatherCache.getCity())
                .temperature(weatherCache.getTemperature())
                .condition(weatherCache.getCondition())
                .build();
    }
}
