package com.example.ecopulse.repository;

import com.example.ecopulse.entity.Device;
import com.example.ecopulse.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;

public interface DeviceRepository extends JpaRepository<Device, Long> {
    List<Device> findByUserId(Long userid);

    Long user(User user);
}
