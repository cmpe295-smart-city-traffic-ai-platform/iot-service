package com.iot_station_management.iot_station_management.services;

import com.iot_station_management.iot_station_management.models.IotDevice;
import com.iot_station_management.iot_station_management.models.TrafficData;
import com.iot_station_management.iot_station_management.models.TrafficPrediction;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

// service that defines methods to be implemented by IotDeviceServie
public interface IotDeviceService {
    IotDevice createIotDevice(IotDevice iotDevice);

    Boolean deleteIotDevice(UUID userId, UUID deviceId);

    ArrayList<IotDevice> getUserIotDevices(UUID userId);

    ArrayList<IotDevice> getIotPredictionDevices(String majorRoad);

    IotDevice updateIotDevice(UUID userId, UUID deviceId, Boolean active, String name, String location);

    ArrayList<IotDevice> getActiveIotDevices();

    ArrayList<IotDevice> getActiveIotPredictionDevicesInterstateUSRoads();

    ArrayList<IotDevice> getActiveIotPredictionDevicesCaliforniaRoads();


    TrafficData pollTraffic(UUID deviceId, String location, IotDevice.MajorRoad majorRoad, Integer deviceIdNo, Date createdDate, Boolean isCaliforniaPolling) throws IOException, InterruptedException, URISyntaxException;

    TrafficData getRecentTrafficData(UUID deviceId);

    ArrayList<TrafficData> getTrafficDataHistory(UUID deviceId, Integer limit);

    TrafficPrediction getTrafficPredictions(Integer deviceIdNo);
}
