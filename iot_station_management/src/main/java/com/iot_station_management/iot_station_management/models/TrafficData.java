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

    private String trafficData;

    private String deviceType;

    private String location;

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
}
