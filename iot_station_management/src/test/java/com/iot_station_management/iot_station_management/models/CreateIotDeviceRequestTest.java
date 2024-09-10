package com.iot_station_management.iot_station_management.models;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.UUID;

public class CreateIotDeviceRequestTest {
    @Test
    public void test_CreateIotDeviceRequestConstructor() {
        String testName = "Test Name";
        String testLocation = "37.3032067,-121.9695576";
        UUID testUserId = UUID.randomUUID();
        Boolean testActive = true;

        CreateIotDeviceRequest createIotDeviceRequest = new CreateIotDeviceRequest(testName, testLocation, testUserId, testActive);

        Assertions.assertEquals(testName, createIotDeviceRequest.getName());
        Assertions.assertEquals(testLocation, createIotDeviceRequest.getLocation());
        Assertions.assertEquals(testUserId, createIotDeviceRequest.getUserId());
        Assertions.assertEquals(testActive, createIotDeviceRequest.getActive());
    }

    @Test
    public void test_CreateIotDeviceRequestBadLocation() {
        String testLocation = "37.3032067,-121.9695576";

        CreateIotDeviceRequest createIotDeviceRequest = new CreateIotDeviceRequest();
    }
}
