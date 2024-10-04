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

import java.net.URI;
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

    @Value("${traffic.api.california.key}")
    private String trafficApiKeyCaliforniaRoads;

    private final String TRAFFIC_SPEED_UNIT = "unit=MPH";
    private final String OPEN_LR = "openLr=false";

    /**
     * @Autowired automatically wires beans for dependencies within Spring framework
     * Autowiring through constructor, dependency injection
     * @param iotDeviceRepository - IOT Device repository for SQL database operations
     * @param trafficDataRepository - Traffic Data repository for NoSQL database operations
     */
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

    public ArrayList<IotDevice> getIotPredictionDevices(IotDevice.MajorRoad majorRoad) {
        return this.iotDeviceRepository.findIotDeviceByMajorRoadAndDeviceIdNoIsNotNull(majorRoad);
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

        long updatedTimestamp = System.currentTimeMillis() / 1000L;

        iotDevice.setUpdatedAtTimestamp(updatedTimestamp);

        return this.iotDeviceRepository.save(iotDevice);
    }

    /**
     *
     * @return - array list of active IOT devices
     */
    @Override
    public ArrayList<IotDevice> getActiveIotDevices() {
        return this.iotDeviceRepository.findIotDeviceByActiveIsTrueAndDeviceIdNoIsNull();
    }

    /**
     *
     * @return - array list of active IOT prediction devices stationed around California roads
     */
    @Override
    public ArrayList<IotDevice> getActiveIotPredictionDevicesCaliforniaRoads() {
        return this.iotDeviceRepository.findIotPredictionDevicesCaliforniaRoads();
    }

    /**
     *
     * @return - array list of active IOT prediction devices stationed around interstate/US roads
     */
    @Override
    public ArrayList<IotDevice> getActiveIotPredictionDevicesInterstateUSRoads() {
        return this.iotDeviceRepository.findIotPredictionDevicesInterstateUSRoads();
    }

    /**
     *
     * @param deviceId - IOT Device ID to poll traffic for
     * @param location - Location in latitude,longitude format
     * @return - created traffic data
     */
    @Override
    public TrafficData pollTraffic(UUID deviceId, String location, IotDevice.MajorRoad majorRoad, Integer deviceIdNo, Date createdDate, Boolean isCaliforniaPolling) {
        try {

            if (deviceIdNo != null) {
                logger.info("Polling traffic from device id no: " + deviceIdNo + " for location: " + location);
            } else {
                logger.info("Polling traffic from device id: " + deviceId + " for location: " + location);
            }
            UUID trafficDataID = UUID.randomUUID();

            String apiKey = !isCaliforniaPolling ? trafficApiKey : trafficApiKeyCaliforniaRoads;

            // making API request
            String trafficApiUrlWithParams = trafficApiUrl + "?" + TRAFFIC_SPEED_UNIT + "&" + OPEN_LR + "&point=" +  location + "&key=" + apiKey;

            logger.info("Traffic API URL with params: " + trafficApiUrlWithParams);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(trafficApiUrlWithParams))
                    .GET()
                    .build();

            // use HttpClient to make API request
            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            // TODO convert JSON string to JSON
            String trafficDataResponseString = response.body();

            // convert timestamp from milliseconds to seconds
            long timestamp = System.currentTimeMillis() / 1000L;

            // create traffic data and save to mongo nosql database
            TrafficData trafficData = new TrafficData(trafficDataID, deviceId, deviceIdNo, trafficDataResponseString, IotDevice.IOT_DEVICE_TYPE, majorRoad.toString(), location, createdDate, timestamp);
            return this.trafficDataRepository.save(trafficData);
        } catch(Exception e) {
            logger.error("Exception from pollTraffic: " + e.getMessage());
            return null;
        }
    }

    /**
     *
     * @param deviceId to get traffic data for
     * @return - traffic data for device id
     */
    @Override
    public TrafficData getRecentTrafficData(UUID deviceId) {
        return this.trafficDataRepository.findFirstByDeviceIdOrderByTimestampDesc(deviceId);
    }


    /**
     *
     * @param deviceId to get traffic data for
     * @param limit - amount of records to return
     * @return - list of traffic data for given device id
     */
    @Override
    public ArrayList<TrafficData> getTrafficDataHistory(UUID deviceId, Integer limit) {
        return this.trafficDataRepository.findByDeviceIdOrderByTimestampDesc(deviceId, limit);
    }
}
