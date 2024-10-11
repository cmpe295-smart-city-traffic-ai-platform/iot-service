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

    ArrayList<IotDevice> findIotDeviceByMajorRoadAndDeviceIdNoIsNotNull(IotDevice.MajorRoad majorRoad);

    // get active prediction devices
    ArrayList<IotDevice> findIotDeviceByActiveIsTrueAndDeviceIdNoIsNotNull();

    // get active traffic agent devices
    ArrayList<IotDevice> findIotDeviceByActiveIsTrueAndDeviceIdNoIsNull();

    @Query(
            value = "SELECT * FROM iot_device WHERE major_road IN (1, 6, 7) AND device_id_no IS NOT NULL AND active = true",
            nativeQuery = true)
    ArrayList<IotDevice> findIotPredictionDevicesCaliforniaRoads();


    @Query(
            value = "SELECT * FROM iot_device WHERE major_road IN (0, 3, 4, 5) AND device_id_no IS NOT NULL AND active = true",
            nativeQuery = true)
    ArrayList<IotDevice> findIotPredictionDevicesInterstateUSRoads();

}
