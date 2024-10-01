package com.iot_station_management.iot_station_management.controllers;

import com.iot_station_management.iot_station_management.models.CreateIotDeviceRequest;
import com.iot_station_management.iot_station_management.models.IotDevice;
import com.iot_station_management.iot_station_management.models.TrafficData;
import com.iot_station_management.iot_station_management.models.UpdateIotDeviceRequest;
import com.iot_station_management.iot_station_management.services.IotDeviceServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

// controller class that handles requests mapped to URI
// dispatcher servlet routes request, controller handles request and calls corresponding methods to handle
// requests with URI /api/v1/iot are handled by this controller
@Tag(name = "IOT Device", description = "IOT Device APIs")
// @RestController annotation used for handling requests, returns response entity, @Controller and @ResponseBody annotations
@RestController
@RequestMapping("/api/v1/iot")
public class IotDeviceController {
    IotDeviceServiceImpl iotDeviceService;

    // @Autowired automatically wires beans for dependencies within Spring framework
    // Autowiring through constructor, dependency injection
    @Autowired
    IotDeviceController(IotDeviceServiceImpl iotDeviceService) {
        this.iotDeviceService = iotDeviceService;
    }

    /**
     * method that handles POST requests to /api/v1/iot
     * @param createIotDeviceRequest - request representing new IOT device fields
     * @return
     */
    @Operation(
            summary = "Create new IOT device",
            description = "Create new IOT device based on provided name, location, user ID, and active state"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "IOT Device created successfully.")
    })
    @PostMapping
    public ResponseEntity<IotDevice> createIotDevice(@Valid @RequestBody CreateIotDeviceRequest createIotDeviceRequest) {
        Date createdDate = new Date();
        long createdTimestamp = System.currentTimeMillis() / 1000L;
        IotDevice newIotDevice = new IotDevice(
                createIotDeviceRequest.getDeviceIdNo(),
                createIotDeviceRequest.getName(),
                createIotDeviceRequest.getLocation(),
                createIotDeviceRequest.getUserId(),
                createIotDeviceRequest.getActive(),
                createdDate,
                createdDate,
                createdTimestamp,
                createdTimestamp);
        if (createIotDeviceRequest.getMajorRoad() != null) {
            newIotDevice.setMajorRoad(IotDevice.MajorRoad.valueOf(createIotDeviceRequest.getMajorRoad()));
        }
        IotDevice createdIotDevice = this.iotDeviceService.createIotDevice(newIotDevice);
        return new ResponseEntity<>(createdIotDevice, HttpStatus.CREATED);
    }

    /**
     * method that handles GET requests to /api/v1/iot
     * @param userId - User ID to get devices for
     * @return - Array list of IOT Devices for user
     */
    @Operation(
            summary = "Get IOT devices",
            description = "Get IOT devices for given user ID"
    )
    @GetMapping
    public ResponseEntity<ArrayList<IotDevice>> getIotDevices(@RequestParam String userId) {
        ArrayList<IotDevice> iotDevices = this.iotDeviceService.getUserIotDevices(UUID.fromString(userId));
        return new ResponseEntity<>(iotDevices, HttpStatus.OK);
    }

    /**
     * method that handles GET requests to /api/v1/iot/predictionDevices
     * @param majorRoad - Major Road to get IOT prediction devices for
     * @return - Array list of IOT Devices for major road
     */
    @Operation(
            summary = "Get IOT prediction",
            description = "Get IOT prediction devices for given major road"
    )
    @GetMapping("/predictionDevice")
    public ResponseEntity<ArrayList<IotDevice>> getIotPredictionDevices(@RequestParam String majorRoad) {
        ArrayList<IotDevice> iotDevices = this.iotDeviceService.getIotPredictionDevices(IotDevice.MajorRoad.valueOf(majorRoad));
        return new ResponseEntity<>(iotDevices, HttpStatus.OK);
    }

    /**
     * method that handles DELETE requests to /api/v1/iot/{userId}/{deviceId}
     * @param userId - User ID of IOT Device
     * @param deviceId - IOT Device ID
     * @return - void operation if successful, exception is thrown if error during deletion
     */
    @Operation(
            summary = "Delete IOT Device",
            description = "Delete IOT device for given user ID and device ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "IOT Device deleted successfully."),
            @ApiResponse(responseCode = "404", description = "IOT Device not found for deletion.")
    })
    @DeleteMapping(path = "/{userId}/{deviceId}")
    public ResponseEntity<Void> deleteIotDevice(@PathVariable String userId, @PathVariable String deviceId) {
        this.iotDeviceService.deleteIotDevice(UUID.fromString(userId), UUID.fromString(deviceId));
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * method that handles PUT requests to /api/v1/iot/{userId}/{deviceId}
     * @param userId - User ID of IOT Device
     * @param deviceId - IOT Device ID
     * @param updateIotDeviceRequest - request representing which properties to update
     * @return - Updated IOT Device
     */
    @Operation(
            summary = "Update IOT Device",
            description = "Update IOT device for given user ID and device ID. Active state, location, and name can be updated"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "IOT Device updated successfully."),
            @ApiResponse(responseCode = "404", description = "IOT Device not found for update.")
    })
    @PutMapping(path = "/{userId}/{deviceId}")
    public ResponseEntity<IotDevice> updateIotDevice(@PathVariable String userId, @PathVariable String deviceId, @Valid @RequestBody UpdateIotDeviceRequest updateIotDeviceRequest) {
        IotDevice updatedDevice = this.iotDeviceService.updateIotDevice(UUID.fromString(userId), UUID.fromString(deviceId), updateIotDeviceRequest.getActive(), updateIotDeviceRequest.getName(), updateIotDeviceRequest.getLocation());
        return new ResponseEntity<>(updatedDevice, HttpStatus.OK);
    }

    /**
     * method that handles GET requests to /api/v1/iot/traffic/{deviceId}
     * @param deviceId - IOT Device ID
     * @return - Most recent traffic data
     */
    @Operation(
            summary = "Get Latest IOT Device Traffic Data",
            description = "Get latest IOT device traffic data for given device ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "IOT Device traffic data retrieved."),
    })
    @GetMapping(path = "/traffic/{deviceId}")
    public ResponseEntity<TrafficData> getLatestDeviceTrafficData(@PathVariable String deviceId) {
        TrafficData recentTrafficData = this.iotDeviceService.getRecentTrafficData(UUID.fromString(deviceId));
        return new ResponseEntity<>(recentTrafficData, HttpStatus.OK);
    }
}
