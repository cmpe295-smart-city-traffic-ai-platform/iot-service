package com.iot_station_management.iot_station_management.repositories;

import com.iot_station_management.iot_station_management.models.IotDevice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

// class that handles database interactions to PostgreSQL via JPA
// @Repository annotation indicates bean will handle SQL queries
@Repository
public interface IotDeviceRepository extends JpaRepository<IotDevice, UUID> {
    ArrayList<IotDevice> findIotDeviceByUserId(UUID userId);

    Optional<IotDevice> findIotDeviceByUserIdAndId(UUID userId, UUID deviceID);

    ArrayList<IotDevice> findIotDeviceByActiveIsTrue();
}
