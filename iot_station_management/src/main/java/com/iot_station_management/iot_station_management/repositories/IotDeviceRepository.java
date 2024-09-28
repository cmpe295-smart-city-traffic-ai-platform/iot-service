package com.iot_station_management.iot_station_management.repositories;

import com.iot_station_management.iot_station_management.models.IotDevice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
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

    @Query(
            value = "SELECT * FROM iot_device WHERE major_road IN ('CA237', 'CA85', 'CA87')",
            nativeQuery = true)
    ArrayList<IotDevice> findIotPredictionDevicesCaliforniaRoads();


    @Query(
            value = "SELECT * FROM iot_device WHERE major_road IN ('I280', 'I880', 'I680', 'US101')",
            nativeQuery = true)
    ArrayList<IotDevice> findIotPredictionDevicesInterstateUSRoads();

}
