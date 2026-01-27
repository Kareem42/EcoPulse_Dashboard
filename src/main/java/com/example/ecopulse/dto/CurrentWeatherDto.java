package com.example.ecopulse.dto;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CurrentWeatherDto {
    private String city;
    private Double temperature;
    private String condition;
    private String description;
    private Integer humidity;
    private Integer pressure;
}
