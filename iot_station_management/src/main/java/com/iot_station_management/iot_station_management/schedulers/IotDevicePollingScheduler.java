package com.iot_station_management.iot_station_management.schedulers;

import com.iot_station_management.iot_station_management.models.IotDevice;
import com.iot_station_management.iot_station_management.services.IotDeviceServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

// class that handles making API requests for traffic data for active IOT devices
@Component
public class IotDevicePollingScheduler {
    private final IotDeviceServiceImpl iotDeviceService;

    private final int POLL_DURATION = 5000;

    @Autowired
    public IotDevicePollingScheduler(IotDeviceServiceImpl iotDeviceService) {
        this.iotDeviceService = iotDeviceService;
    }

    /**
     * Scheduled method to poll traffic data for active IOT devices
     */
    @Scheduled(fixedRate = POLL_DURATION)
    @Async("asyncTaskExecutor")
    public void schedulePollTraffic() {
        // get active devices
        ArrayList<IotDevice> activeDevices = this.iotDeviceService.getActiveIotDevices();

        if (!activeDevices.isEmpty()) {
            // poll traffic data
            for (IotDevice activeDevice : activeDevices) {
                this.iotDeviceService.pollTraffic(activeDevice.getId(), activeDevice.getLocation());
            }
        }
    }
}
