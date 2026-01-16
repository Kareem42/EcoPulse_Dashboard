package com.example.ecopulse.dto;

import com.example.ecopulse.entity.DeviceType;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DeviceRequest {
    @NotBlank
    private String name;
    private String location;
    private DeviceType type;
    private Integer estimatePowerWatts;
}
