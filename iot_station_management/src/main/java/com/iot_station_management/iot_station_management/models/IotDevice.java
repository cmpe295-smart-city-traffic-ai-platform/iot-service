package com.iot_station_management.iot_station_management.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.Date;
import java.util.UUID;

// class that maps to a database table through JPA, Hibernate, @Entity annotation
@Entity
public class IotDevice {
    public static final String IOT_DEVICE_TYPE = "IOT_CAMERA";

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;

    private String location;

    private UUID userId;

    private Boolean active;

    private Date createdAt;

    private Date updatedAt;

    private long createdAtTimestamp;

    private long updatedAtTimestamp;

    public IotDevice() {
    }

    public IotDevice(String name, String location, UUID userId, Boolean active, Date createdAt, Date updatedAt, long createdAtTimestamp, long updatedAtTimestamp) {
        this.name = name;
        this.location = location;
        this.userId = userId;
        this.active = active;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.createdAtTimestamp = createdAtTimestamp;
        this.updatedAtTimestamp = updatedAtTimestamp;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public Boolean isActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Boolean getActive() {
        return active;
    }

    public long getCreatedAtTimestamp() {
        return createdAtTimestamp;
    }

    public void setCreatedAtTimestamp(long createdAtTimestamp) {
        this.createdAtTimestamp = createdAtTimestamp;
    }

    public long getUpdatedAtTimestamp() {
        return updatedAtTimestamp;
    }

    public void setUpdatedAtTimestamp(long updatedAtTimestamp) {
        this.updatedAtTimestamp = updatedAtTimestamp;
    }
}
