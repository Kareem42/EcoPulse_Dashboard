package com.example.ecopulse.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class WeatherResponse {

    @JsonProperty("main")
    private MainWeatherData main;

    @JsonProperty("weather")
    private WeatherDetail[] weather;

    private String cityName;

    @Data
    public static class MainWeatherData {
        private Double temp;
        private Double feels_like;
        private Integer humidity;
        private Integer pressure;
    }

    @Data
    public static class WeatherDetail {
        private String main; //"Clear", "Rainy"
        private String description;
        private String icon;
    }
}
