package com.example.ecopulse.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EnergyReadingRequest {
    @NotNull
    private Long deviceId;

    @NotNull
    private LocalDateTime date;

    @NotNull
    @Positive
    private Double energyKwh;
    private String source = "manual";

}
