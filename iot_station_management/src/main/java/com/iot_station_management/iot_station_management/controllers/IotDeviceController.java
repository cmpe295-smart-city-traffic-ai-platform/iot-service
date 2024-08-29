package com.iot_station_management.iot_station_management.controllers;

import com.iot_station_management.iot_station_management.models.CreateIotDeviceRequest;
import com.iot_station_management.iot_station_management.models.IotDevice;
import com.iot_station_management.iot_station_management.models.TrafficData;
import com.iot_station_management.iot_station_management.models.UpdateIotDeviceRequest;
import com.iot_station_management.iot_station_management.services.IotDeviceServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

// controller class that handles requests mapped to URI
// dispatcher servlet routes request, controller handles request and calls corresponding methods to handle
// requests with URI /api/iot/v1 are handled by this controller
@Tag(name = "IOT Device", description = "IOT Device APIs")
@RestController
@RequestMapping("/api/iot/v1")
public class IotDeviceController {
    IotDeviceServiceImpl iotDeviceService;

    // @Autowired automatically wires beans for dependencies within Spring framework
    // Autowiring through constructor, dependency injection
    @Autowired
    IotDeviceController(IotDeviceServiceImpl iotDeviceService) {
        this.iotDeviceService = iotDeviceService;
    }

    // method that handles POST requests to /api/iot/v1
    @Operation(
            summary = "Create new IOT device",
            description = "Create new IOT device based on provided name, location, user ID, and active state"
    )
    @PostMapping
    public ResponseEntity<IotDevice> createIotDevice(@RequestBody CreateIotDeviceRequest createIotDeviceRequest) {
        Date createdDate = new Date();
        IotDevice newIotDevice = new IotDevice(createIotDeviceRequest.getName(),
                createIotDeviceRequest.getLocation(),
                createIotDeviceRequest.getUserId(),
                createIotDeviceRequest.getActive(),
                createdDate,
                createdDate);
        IotDevice createdIotDevice = this.iotDeviceService.createIotDevice(newIotDevice);
        return new ResponseEntity<>(createdIotDevice, HttpStatus.CREATED);
    }

    // method that handles GET requests to /api/iot/v1
    @Operation(
            summary = "Get IOT devices",
            description = "Get IOT devices for given user ID"
    )
    @GetMapping
    public ResponseEntity<ArrayList<IotDevice>> getIotDevices(@RequestParam String userId) {
        ArrayList<IotDevice> iotDevices = this.iotDeviceService.getUserIotDevices(UUID.fromString(userId));
        return new ResponseEntity<>(iotDevices, HttpStatus.OK);
    }

    // method that handles DELETE requests to /api/iot/v1/{userId}/{deviceId}
    @Operation(
            summary = "Delete IOT Device",
            description = "Delete IOT device for given user ID and device ID"
    )
    @DeleteMapping(path = "/{userId}/{deviceId}")
    public ResponseEntity<Void> deleteIotDevice(@PathVariable String userId, @PathVariable String deviceId) {
        this.iotDeviceService.deleteIotDevice(UUID.fromString(userId), UUID.fromString(deviceId));
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // method that handles PUT requests to /api/iot/v1/{userId}/{deviceId}
    @Operation(
            summary = "Update IOT Device",
            description = "Update IOT device for given user ID and device ID. Active state, location, and name can be updated"
    )
    @PutMapping(path = "/{userId}/{deviceId}")
    public ResponseEntity<IotDevice> updateIotDevice(@PathVariable String userId, @PathVariable String deviceId, @RequestBody UpdateIotDeviceRequest updateIotDeviceRequest) {
        IotDevice updatedDevice = this.iotDeviceService.updateIotDevice(UUID.fromString(userId), UUID.fromString(deviceId), updateIotDeviceRequest.getActive(), updateIotDeviceRequest.getName(), updateIotDeviceRequest.getLocation());
        return new ResponseEntity<>(updatedDevice, HttpStatus.OK);
    }

    // method that handles GET requests to /api/iot/v1/traffic/{deviceId}
    @Operation(
            summary = "Get Latest IOT Device Traffic Data",
            description = "Get latest IOT device traffic data for given device ID."
    )
    @GetMapping(path = "/traffic/{deviceId}")
    public ResponseEntity<TrafficData> getLatestDeviceTrafficData(@PathVariable String deviceId) {
        TrafficData recentTrafficData = this.iotDeviceService.getRecentTrafficData(UUID.fromString(deviceId));
        return new ResponseEntity<>(recentTrafficData, HttpStatus.OK);
    }
}
