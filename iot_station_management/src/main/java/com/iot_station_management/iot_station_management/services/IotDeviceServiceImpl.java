package com.iot_station_management.iot_station_management.services;

import com.iot_station_management.iot_station_management.models.IotDevice;
import com.iot_station_management.iot_station_management.models.TrafficData;
import com.iot_station_management.iot_station_management.repositories.IotDeviceRepository;
import com.iot_station_management.iot_station_management.repositories.TrafficDataRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;


/*
 * class that implements IotDeviceService, defines business logic for methods
 * @Service annotation, indicates this class is a specific bean handling business logic
*/

@Service
public class IotDeviceServiceImpl implements IotDeviceService {
    private static final Logger logger = LoggerFactory.getLogger(IotDeviceServiceImpl.class);

    private final IotDeviceRepository iotDeviceRepository;

    private final TrafficDataRepository trafficDataRepository;

    @Value("${traffic.api.url}")
    private String trafficApiUrl;

    @Value("${traffic.api.key}")
    private String trafficApiKey;

    private final String TRAFFIC_SPEED_UNIT = "unit=MPH";
    private final String OPEN_LR = "openLr=false";

    // @Autowired automatically wires beans for dependencies within Spring framework
    // Autowiring through constructor, dependency injection
    @Autowired
    public IotDeviceServiceImpl(IotDeviceRepository iotDeviceRepository, TrafficDataRepository trafficDataRepository) {
        this.iotDeviceRepository = iotDeviceRepository;
        this.trafficDataRepository = trafficDataRepository;
    }

    /**
     *
     * @param iotDevice - IOT Device to be created
     * @return created IOT Device
     */
    @Override
    public IotDevice createIotDevice(IotDevice iotDevice) {
        return this.iotDeviceRepository.save(iotDevice);
    }

    /**
     *
     * @param userId - User ID
     * @param deviceId - Device ID to be deleted
     * @return boolean indicating successful deletion
     */
    @Override
    public Boolean deleteIotDevice(UUID userId, UUID deviceId) {
        if (this.iotDeviceRepository.findIotDeviceByUserIdAndId(userId, deviceId).isEmpty()) {
            throw new NoSuchElementException("Device ID not found for user.");
        }

        this.iotDeviceRepository.deleteById(deviceId);
        return true;
    }

    /**
     *
     * @param userId to get devices for
     * @return array list of IOT devices for user
     */
    @Override
    public ArrayList<IotDevice> getUserIotDevices(UUID userId) {
        return this.iotDeviceRepository.findIotDeviceByUserId(userId);
    }

    /**
     *
     * @param userId - User ID
     * @param deviceId - Device ID to be updated
     * @param active - Active value of device being updated
     * @param name - New name of device being updated
     * @param location - New location of device being updated
     * @return - updated IOT device
     */
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

    /**
     *
     * @return - array list of active IOT devices
     */
    @Override
    public ArrayList<IotDevice> getActiveIotDevices() {
        return this.iotDeviceRepository.findIotDeviceByActiveIsTrue();
    }

    /**
     *
     * @param deviceId - IOT Device ID to poll traffic for
     * @param location - Location in latitude,longitude format
     * @return - created traffic data
     * @throws IOException
     * @throws InterruptedException
     * @throws URISyntaxException
     */
    @Override
    public TrafficData pollTraffic(UUID deviceId, String location) throws IOException, InterruptedException, URISyntaxException {
        logger.info("Polling traffic from device id: " + deviceId + " for location: " + location);
        UUID trafficDataID = UUID.randomUUID();

        // making API request
        String trafficApiUrlWithParams = trafficApiUrl + "?" + TRAFFIC_SPEED_UNIT + "&" + OPEN_LR + "&point=" +  location + "&key=" + trafficApiKey;

        logger.info("Traffic API URL with params: " + trafficApiUrlWithParams);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(trafficApiUrlWithParams))
                .GET()
                .build();

        // use HttpClient to make API request
        HttpClient client = HttpClient.newHttpClient();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String trafficDataResponseString = response.body();

        // create traffic data and save to mongo nosql database
        TrafficData trafficData = new TrafficData(trafficDataID, deviceId, trafficDataResponseString, IotDevice.IOT_DEVICE_TYPE, location, new Date());
        return this.trafficDataRepository.save(trafficData);
    }

    /**
     *
     * @param deviceId to get traffic data for
     * @return - traffic data for device id
     */
    @Override
    public TrafficData getTrafficData(UUID deviceId) {
        // TODO get latest traffic data for device id based on most recent timestamp
//        return this.trafficDataRepository.findById(UUID deviceID);
        return null;
    }
}
