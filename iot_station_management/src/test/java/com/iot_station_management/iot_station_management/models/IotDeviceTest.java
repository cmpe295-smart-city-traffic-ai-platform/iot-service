package com.iot_station_management.iot_station_management.models;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import java.util.Date;
import java.util.UUID;

public class IotDeviceTest {

    @Test
    public void test_IotDeviceConstructor() {
        String testName = "Test Device Name";
        String location = "37.3032067,-121.9695576";
        UUID userId = UUID.fromString("d04b59ff-4baf-47e2-986b-7a7d3e73091e");
        Boolean active = true;
        Date testDate = new Date();
        long testTimestamp = System.currentTimeMillis() / 1000L;


        IotDevice iotDevice = new IotDevice(testName, location, userId, active, testDate, testDate, testTimestamp, testTimestamp);

        Assertions.assertEquals(testName, iotDevice.getName());
        Assertions.assertEquals(location, iotDevice.getLocation());
        Assertions.assertEquals(userId, iotDevice.getUserId());
        Assertions.assertEquals(active, iotDevice.getActive());
        Assertions.assertEquals(testDate, iotDevice.getCreatedAt());
        Assertions.assertEquals(testDate, iotDevice.getUpdatedAt());
        Assertions.assertEquals(testTimestamp, iotDevice.getCreatedAtTimestamp());
        Assertions.assertEquals(testTimestamp, iotDevice.getUpdatedAtTimestamp());
    }

    @Test
    public void test_IotDeviceGettersSetters() {
        String testName = "Test Device Name";
        String location = "37.3032067,-121.9695576";
        UUID userId = UUID.fromString("d04b59ff-4baf-47e2-986b-7a7d3e73091e");
        Boolean active = true;
        Date testDate = new Date();
        long testTimestamp = System.currentTimeMillis() / 1000L;

        IotDevice iotDevice = new IotDevice();
        iotDevice.setName(testName);
        iotDevice.setUserId(userId);
        iotDevice.setActive(active);
        iotDevice.setLocation(location);
        iotDevice.setCreatedAt(testDate);
        iotDevice.setUpdatedAt(testDate);
        iotDevice.setCreatedAtTimestamp(testTimestamp);
        iotDevice.setUpdatedAtTimestamp(testTimestamp);

        Assertions.assertEquals(testName, iotDevice.getName());
        Assertions.assertEquals(location, iotDevice.getLocation());
        Assertions.assertEquals(userId, iotDevice.getUserId());
        Assertions.assertEquals(active, iotDevice.getActive());
        Assertions.assertEquals(testDate, iotDevice.getCreatedAt());
        Assertions.assertEquals(testDate, iotDevice.getUpdatedAt());
        Assertions.assertEquals(testTimestamp, iotDevice.getCreatedAtTimestamp());
        Assertions.assertEquals(testTimestamp, iotDevice.getUpdatedAtTimestamp());
    }

    @Test
    public void test_IotDeviceActive() {
        IotDevice iotDevice = new IotDevice();
        iotDevice.setActive(true);

        Assertions.assertTrue(iotDevice.isActive());

        iotDevice.setActive(false);
        Assertions.assertFalse(iotDevice.isActive());
    }
}
