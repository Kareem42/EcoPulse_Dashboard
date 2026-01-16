package com.example.ecopulse.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class DailyEnergyStats {
    private LocalDate date;
    private Double totalKwh;
}
