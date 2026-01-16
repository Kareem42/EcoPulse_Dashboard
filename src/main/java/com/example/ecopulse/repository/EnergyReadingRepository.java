package com.example.ecopulse.repository;

import com.example.ecopulse.entity.EnergyReading;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.*;

public interface EnergyReadingRepository extends JpaRepository<EnergyReading, Long> {
    @Query("SELECT r FROM EnergyReading r WHERE r.device.user.id = :userId AND r.date BETWEEN :startDate AND :endDate")
    List<EnergyReading> findByUserIdAndDateBetween(
            @Param("userId") Long userId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
}
