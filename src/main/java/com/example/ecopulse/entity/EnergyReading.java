package com.example.ecopulse.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.*;
import jakarta.persistence.*;


@Entity
@Table(name = "energy_readings")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class EnergyReading {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "device_id", nullable = false)
    private Device device;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private Double energyKwh;
    private String source; // Ex: "manual", "stimulated"
}
