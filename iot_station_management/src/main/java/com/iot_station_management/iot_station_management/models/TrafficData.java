package com.iot_station_management.iot_station_management.models;

import jakarta.persistence.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.UUID;

@Document("trafficdata")
public class TrafficData {
    @Id
    private UUID id;
    private UUID deviceId;

    private int deviceIdNo;

    private String trafficData;

    private String deviceType;

    private String location;

    private String MAJOR_ROAD;

    private Date createdAt;

    private long timestamp;

    public TrafficData(UUID id, UUID deviceId, String trafficData, String deviceType, String location, Date createdAt, long timestamp) {
        this.id = id;
        this.deviceId = deviceId;
        this.trafficData = trafficData;
        this.deviceType = deviceType;
        this.location = location;
        this.createdAt = createdAt;
        this.timestamp = timestamp;
    }

    public TrafficData(UUID id, UUID deviceId, String trafficData, String deviceType, String location, String MAJOR_ROAD, Date createdAt, long timestamp) {
        this.id = id;
        this.deviceId = deviceId;
        this.trafficData = trafficData;
        this.deviceType = deviceType;
        this.location = location;
        this.MAJOR_ROAD = MAJOR_ROAD;
        this.createdAt = createdAt;
        this.timestamp = timestamp;
    }

    public TrafficData(UUID id, UUID deviceId, int deviceIdNo, String trafficData, String deviceType, String location, Date createdAt, long timestamp) {
        this.id = id;
        this.deviceId = deviceId;
        this.deviceIdNo = deviceIdNo;
        this.trafficData = trafficData;
        this.deviceType = deviceType;
        this.location = location;
        this.createdAt = createdAt;
        this.timestamp = timestamp;
    }

    public TrafficData(UUID id, UUID deviceId, int deviceIdNo, String trafficData, String deviceType, String MAJOR_ROAD, String location, Date createdAt, long timestamp) {
        this.id = id;
        this.deviceId = deviceId;
        this.deviceIdNo = deviceIdNo;
        this.trafficData = trafficData;
        this.deviceType = deviceType;
        this.MAJOR_ROAD = MAJOR_ROAD;
        this.location = location;
        this.createdAt = createdAt;
        this.timestamp = timestamp;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(UUID deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getTrafficData() {
        return trafficData;
    }

    public void setTrafficData(String trafficData) {
        this.trafficData = trafficData;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getMAJOR_ROAD() {
        return MAJOR_ROAD;
    }

    public void setMAJOR_ROAD(String MAJOR_ROAD) {
        this.MAJOR_ROAD = MAJOR_ROAD;
    }

    public int getDeviceIdNo() {
        return deviceIdNo;
    }

    public void setDeviceIdNo(int deviceIdNo) {
        this.deviceIdNo = deviceIdNo;
    }
}
