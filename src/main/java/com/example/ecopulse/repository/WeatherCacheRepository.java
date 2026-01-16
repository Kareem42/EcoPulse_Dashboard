package com.example.ecopulse.repository;

import com.example.ecopulse.entity.WeatherCache;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;

public interface WeatherCacheRepository extends JpaRepository<WeatherCache, Long> {
    Optional<WeatherCache> findFirstByCityOrderByTimestampDesc(String city);
}
