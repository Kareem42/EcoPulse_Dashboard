package com.example.ecopulse.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class WeatherResponse {

    @JsonProperty("main")
    private MainWeatherData main;

    @JsonProperty("weather")
    private WeatherDetail[] weather;

    @JsonProperty("name")
    private String name;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class MainWeatherData {
        @JsonProperty("temp")
        private Double temp;

        @JsonProperty("feels_like")
        private Double feels_like;

        @JsonProperty("humidity")
        private Integer humidity;

        @JsonProperty("pressure")
        private Integer pressure;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class WeatherDetail {
        @JsonProperty("main")
        private String main; //"Clear", "Rainy"

        @JsonProperty("description")
        private String description;

        @JsonProperty("icon")
        private String icon;
    }
}
