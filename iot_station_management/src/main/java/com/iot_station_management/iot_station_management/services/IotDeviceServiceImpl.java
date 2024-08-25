package com.iot_station_management.iot_station_management.services;

import com.iot_station_management.iot_station_management.models.IotDevice;
import com.iot_station_management.iot_station_management.models.TrafficData;
import com.iot_station_management.iot_station_management.repositories.IotDeviceRepository;
import com.iot_station_management.iot_station_management.repositories.TrafficDataRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class IotDeviceServiceImpl implements IotDeviceService {
    private static final Logger logger = LoggerFactory.getLogger(IotDeviceServiceImpl.class);

    private final IotDeviceRepository iotDeviceRepository;

    private final TrafficDataRepository trafficDataRepository;

    @Autowired
    public IotDeviceServiceImpl(IotDeviceRepository iotDeviceRepository, TrafficDataRepository trafficDataRepository) {
        this.iotDeviceRepository = iotDeviceRepository;
        this.trafficDataRepository = trafficDataRepository;
    }

    @Override
    public IotDevice createIotDevice(IotDevice iotDevice) {
        return this.iotDeviceRepository.save(iotDevice);
    }

    @Override
    public Boolean deleteIotDevice(UUID userId, UUID deviceId) {
        if (this.iotDeviceRepository.findIotDeviceByUserIdAndId(userId, deviceId).isEmpty()) {
            throw new NoSuchElementException("Device ID not found for user.");
        }

        this.iotDeviceRepository.deleteById(deviceId);
        return true;
    }

    @Override
    public ArrayList<IotDevice> getUserIotDevices(UUID userId) {
        return this.iotDeviceRepository.findIotDeviceByUserId(userId);
    }

    @Override
    public IotDevice updateIotDevice(UUID userId, UUID deviceId, Boolean active, String name, String location) {
        Optional<IotDevice> existingIotDevice = this.iotDeviceRepository.findIotDeviceByUserIdAndId(userId, deviceId);

        if (existingIotDevice.isEmpty()) {
            throw new NoSuchElementException("Device ID not found for user.");
        }

        IotDevice iotDevice = existingIotDevice.get();

        if (active != null) {
            iotDevice.setActive(active);
        }

        if (name != null) {
            iotDevice.setName(name);
        }

        if (location != null) {
            iotDevice.setLocation(location);
        }

        iotDevice.setUpdatedAt(new Date());

        return this.iotDeviceRepository.save(iotDevice);
    }

    @Override
    public ArrayList<IotDevice> getActiveIotDevices() {
        return this.iotDeviceRepository.findIotDeviceByActiveIsTrue();
    }

    @Override
    public TrafficData pollTraffic(UUID deviceId, String location) {
        // TODO make public API call to get traffic data for given location
        logger.info("Polling traffic from device id: " + deviceId + " for location: " + location);
        UUID trafficDataID = UUID.randomUUID();
        // TODO add traffic data to model
        TrafficData trafficData = new TrafficData(trafficDataID, trafficDataID, IotDevice.IOT_DEVICE_TYPE, location, new Date());
        return this.trafficDataRepository.save(trafficData);
    }

    @Override
    public TrafficData getTrafficData(UUID deviceId) {
        // TODO get latest traffic data for device id based on most recent timestamp
//        return this.trafficDataRepository.findById(UUID deviceID);
    }
}
