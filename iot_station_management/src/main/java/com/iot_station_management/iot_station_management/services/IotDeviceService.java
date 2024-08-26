package com.iot_station_management.iot_station_management.services;

import com.iot_station_management.iot_station_management.models.IotDevice;
import com.iot_station_management.iot_station_management.models.TrafficData;
import org.springframework.scheduling.annotation.EnableAsync;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.UUID;

// service that defines methods to be implemented by IotDeviceServie
public interface IotDeviceService {
    IotDevice createIotDevice(IotDevice iotDevice);

    Boolean deleteIotDevice(UUID userId, UUID deviceId);

    ArrayList<IotDevice> getUserIotDevices(UUID userId);

    IotDevice updateIotDevice(UUID userId, UUID deviceId, Boolean active, String name, String location);

    ArrayList<IotDevice> getActiveIotDevices();

    TrafficData pollTraffic(UUID deviceId, String location) throws IOException, InterruptedException, URISyntaxException;

    TrafficData getTrafficData(UUID deviceId);
}
