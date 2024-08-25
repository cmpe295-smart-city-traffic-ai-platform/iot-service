package com.iot_station_management.iot_station_management.services;

import com.iot_station_management.iot_station_management.models.IotDevice;
import com.iot_station_management.iot_station_management.models.TrafficData;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.ArrayList;
import java.util.UUID;

public interface IotDeviceService {
    IotDevice createIotDevice(IotDevice iotDevice);

    Boolean deleteIotDevice(UUID userId, UUID deviceId);

    ArrayList<IotDevice> getUserIotDevices(UUID userId);

    IotDevice updateIotDevice(UUID userId, UUID deviceId, Boolean active, String name, String location);

    ArrayList<IotDevice> getActiveIotDevices();

    TrafficData pollTraffic(UUID deviceId, String location);

    TrafficData getTrafficData(UUID deviceId);
}
