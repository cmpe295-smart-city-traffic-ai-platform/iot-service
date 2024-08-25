package com.iot_station_management.iot_station_management.schedulers;

import com.iot_station_management.iot_station_management.models.IotDevice;
import com.iot_station_management.iot_station_management.repositories.IotDeviceRepository;
import com.iot_station_management.iot_station_management.services.IotDeviceServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class IotDevicePollingScheduler {
    private final IotDeviceServiceImpl iotDeviceService;

    @Autowired
    public IotDevicePollingScheduler(IotDeviceServiceImpl iotDeviceService) {
        this.iotDeviceService = iotDeviceService;
    }

    @Scheduled(fixedRate = 3000)
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
