package com.iot_station_management.iot_station_management.schedulers;

import com.iot_station_management.iot_station_management.models.IotDevice;
import com.iot_station_management.iot_station_management.models.TrafficData;
import com.iot_station_management.iot_station_management.services.IotDeviceServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

// class that handles making API requests for traffic data for active IOT devices
@Component
public class IotDevicePollingScheduler {
    private final IotDeviceServiceImpl iotDeviceService;

    private static final Logger logger = LoggerFactory.getLogger(IotDevicePollingScheduler.class);

    // duration in seconds when poll should execute again
    private final int POLL_DURATION = 600;

    private final int POLL_DURATION_PREDICTION_DEVICES_CALIFORNIA_ROADS = 600;

    private final int POLL_DURATION_PREDICTION_DEVICES_INTERSTATE_US_ROADS = 600;

    // start and end time for polling schedule
    private final LocalTime START_POLL_TIME = LocalTime.parse("06:30");
    private final LocalTime END_POLL_TIME = LocalTime.parse("20:00");

    @Value("${traffic.poll}")
    private boolean BEGIN_POLLING;


    @Autowired
    public IotDevicePollingScheduler(IotDeviceServiceImpl iotDeviceService) {
        this.iotDeviceService = iotDeviceService;
    }

    public boolean isValidPollingTime() {
        LocalTime currentTime = LocalTime.now().withSecond(0).withNano(0);;
        if (currentTime.isAfter(START_POLL_TIME) && !currentTime.isBefore(END_POLL_TIME)) {
            logger.warn("Invalid polling time: " + currentTime);
            return false;
        }

        if (currentTime.isBefore(END_POLL_TIME) && !currentTime.isAfter(START_POLL_TIME)) {
            logger.warn("Invalid polling time: " + currentTime);
            return false;
        }
        return true;
    }

    /**
     * Scheduled method to poll traffic data for active IOT devices
     */
    @Scheduled(fixedRate = POLL_DURATION, timeUnit = TimeUnit.SECONDS)
    @Async("asyncTaskExecutor")
    public void schedulePollTraffic() throws InterruptedException {
        if (!BEGIN_POLLING || !isValidPollingTime()) {
            return;
        }
        // get active devices
        ArrayList<IotDevice> activeDevices = this.iotDeviceService.getActiveIotDevices();

        if (!activeDevices.isEmpty()) {
            Date createdDate = new Date();
            // poll traffic data
            for (IotDevice activeDevice : activeDevices) {
                Thread.sleep(500);
                TrafficData result = this.iotDeviceService.pollTraffic(activeDevice.getId(), activeDevice.getLocation(), activeDevice.getMajorRoad(), activeDevice.getDeviceIdNo(), createdDate, false);
                if (result != null) {
                    logger.info("Saved traffic data: " + result.getId());
                }
            }
        }
    }


    /**
     * Scheduled method to poll traffic data for active IOT prediction devices interstate and US roads
     */
    @Scheduled(fixedRate = POLL_DURATION_PREDICTION_DEVICES_INTERSTATE_US_ROADS, timeUnit = TimeUnit.SECONDS)
    @Async("asyncTaskExecutor")
    public void schedulePollTrafficPredictionDevicesInterstateUSRoads() throws InterruptedException {
        if (!BEGIN_POLLING || !isValidPollingTime()) {
            return;
        }
        // get active devices
        ArrayList<IotDevice> activePredictionDevices = this.iotDeviceService.getActiveIotPredictionDevicesInterstateUSRoads();

        if (!activePredictionDevices.isEmpty()) {
            Date createdDate = new Date();
            // poll traffic data
            for (IotDevice activeDevice : activePredictionDevices) {
                Thread.sleep(500);
                this.iotDeviceService.pollTraffic(activeDevice.getId(), activeDevice.getLocation(), activeDevice.getMajorRoad(), Integer.valueOf(activeDevice.getDeviceIdNo()), createdDate, false);
            }
        }
    }

    /**
     * Scheduled method to poll traffic data for active IOT prediction devices California roads
     */
    @Scheduled(fixedRate = POLL_DURATION_PREDICTION_DEVICES_CALIFORNIA_ROADS, timeUnit = TimeUnit.SECONDS)
    @Async("asyncTaskExecutor")
    public void schedulePollTrafficPredictionDevicesCaliforniaRoads() throws InterruptedException {
        if (!BEGIN_POLLING || !isValidPollingTime()) {
            return;
        }
        // get active devices
        ArrayList<IotDevice> activePredictionDevices = this.iotDeviceService.getActiveIotPredictionDevicesCaliforniaRoads();

        if (!activePredictionDevices.isEmpty()) {
            Date createdDate = new Date();
            // poll traffic data
            for (IotDevice activeDevice : activePredictionDevices) {
                Thread.sleep(500);
                this.iotDeviceService.pollTraffic(activeDevice.getId(), activeDevice.getLocation(), activeDevice.getMajorRoad(), activeDevice.getDeviceIdNo(), createdDate, true);
            }
        }
    }
}
