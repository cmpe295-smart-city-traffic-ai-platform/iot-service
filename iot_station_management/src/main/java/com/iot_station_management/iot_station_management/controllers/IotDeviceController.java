package com.iot_station_management.iot_station_management.controllers;

import com.iot_station_management.iot_station_management.models.CreateIotDeviceRequest;
import com.iot_station_management.iot_station_management.models.IotDevice;
import com.iot_station_management.iot_station_management.models.UpdateIotDeviceRequest;
import com.iot_station_management.iot_station_management.services.IotDeviceServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

@RestController
@RequestMapping("/api/iot/v1")
public class IotDeviceController {
    IotDeviceServiceImpl iotDeviceService;

    @Autowired
    IotDeviceController(IotDeviceServiceImpl iotDeviceService) {
        this.iotDeviceService = iotDeviceService;
    }

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

    @GetMapping
    public ResponseEntity<ArrayList<IotDevice>> getIotDevices(@RequestParam String userId) {
        ArrayList<IotDevice> iotDevices = this.iotDeviceService.getUserIotDevices(UUID.fromString(userId));
        return new ResponseEntity<>(iotDevices, HttpStatus.OK);
    }

    @DeleteMapping(path = "/{userId}/{deviceId}")
    public ResponseEntity<Void> deleteIotDevice(@PathVariable String userId, @PathVariable String deviceId) {
        this.iotDeviceService.deleteIotDevice(UUID.fromString(userId), UUID.fromString(deviceId));
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping(path = "/{userId}/{deviceId}")
    public ResponseEntity<IotDevice> updateIotDevice(@PathVariable String userId, @PathVariable String deviceId, @RequestBody UpdateIotDeviceRequest updateIotDeviceRequest) {
        IotDevice updatedDevice = this.iotDeviceService.updateIotDevice(UUID.fromString(userId), UUID.fromString(deviceId), updateIotDeviceRequest.getActive(), updateIotDeviceRequest.getName(), updateIotDeviceRequest.getLocation());
        return new ResponseEntity<>(updatedDevice, HttpStatus.OK);
    }
}
