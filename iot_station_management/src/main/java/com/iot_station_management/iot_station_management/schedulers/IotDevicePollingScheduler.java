package com.iot_station_management.iot_station_management.schedulers;

import com.iot_station_management.iot_station_management.models.IotDevice;
import com.iot_station_management.iot_station_management.services.IotDeviceServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

// class that handles making API requests for traffic data for active IOT devices
@Component
public class IotDevicePollingScheduler {
    private final IotDeviceServiceImpl iotDeviceService;

    private final int POLL_DURATION = 600;

    private final int POLL_DURATION_PREDICTION_DEVICES_CALIFORNIA_ROADS = 900;

    private final int POLL_DURATION_PREDICTION_DEVICES_INTERSTATE_US_ROADS = 600;


    @Autowired
    public IotDevicePollingScheduler(IotDeviceServiceImpl iotDeviceService) {
        this.iotDeviceService = iotDeviceService;
    }

    /**
     * Scheduled method to poll traffic data for active IOT devices
     */
//    @Scheduled(fixedRate = POLL_DURATION, timeUnit = TimeUnit.SECONDS)
    @Async("asyncTaskExecutor")
    public void schedulePollTraffic() throws InterruptedException {
        // get active devices
        ArrayList<IotDevice> activeDevices = this.iotDeviceService.getActiveIotDevices();

        if (!activeDevices.isEmpty()) {
            Date createdDate = new Date();
            // poll traffic data
            for (IotDevice activeDevice : activeDevices) {
                Thread.sleep(500);
                this.iotDeviceService.pollTraffic(activeDevice.getId(), activeDevice.getLocation(), activeDevice.getMajorRoad(), activeDevice.getDeviceIdNo(), createdDate);
            }
        }
    }


    /**
     * Scheduled method to poll traffic data for active IOT prediction devices interstate and US roads
     */
//    @Scheduled(fixedRate = POLL_DURATION_PREDICTION_DEVICES_INTERSTATE_US_ROADS, timeUnit = TimeUnit.SECONDS)
    @Async("asyncTaskExecutor")
    public void schedulePollTrafficPredictionDevicesInterstateUSRoads() throws InterruptedException {
        // get active devices
        ArrayList<IotDevice> activePredictionDevices = this.iotDeviceService.getActiveIotPredictionDevicesInterstateUSRoads();

        if (!activePredictionDevices.isEmpty()) {
            Date createdDate = new Date();
            // poll traffic data
            for (IotDevice activeDevice : activePredictionDevices) {
                Thread.sleep(500);
                this.iotDeviceService.pollTraffic(activeDevice.getId(), activeDevice.getLocation(), activeDevice.getMajorRoad(), activeDevice.getDeviceIdNo(), createdDate);
            }
        }
    }

    /**
     * Scheduled method to poll traffic data for active IOT prediction devices California roads
     */
//    @Scheduled(fixedRate = POLL_DURATION_PREDICTION_DEVICES_CALIFORNIA_ROADS, timeUnit = TimeUnit.SECONDS)
    @Async("asyncTaskExecutor")
    public void schedulePollTrafficPredictionDevicesCaliforniaRoads() throws InterruptedException {
        // get active devices
        ArrayList<IotDevice> activePredictionDevices = this.iotDeviceService.getActiveIotPredictionDevicesCaliforniaRoads();

        if (!activePredictionDevices.isEmpty()) {
            Date createdDate = new Date();
            // poll traffic data
            for (IotDevice activeDevice : activePredictionDevices) {
                Thread.sleep(500);
                this.iotDeviceService.pollTraffic(activeDevice.getId(), activeDevice.getLocation(), activeDevice.getMajorRoad(), activeDevice.getDeviceIdNo(), createdDate);
            }
        }
    }
}
