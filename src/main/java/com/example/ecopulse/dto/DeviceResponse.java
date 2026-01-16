package com.example.ecopulse.dto;

import com.example.ecopulse.entity.DeviceType;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class DeviceResponse {
    private Long id;
    private String name;
    private String location;
    private DeviceType type;
    private Integer estimatePowerWatts;
}
