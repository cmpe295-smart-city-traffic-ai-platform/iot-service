package com.iot_station_management.iot_station_management.models;

import com.iot_station_management.iot_station_management.utils.RegExpClass;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.util.Date;
import java.util.UUID;

// class that maps to a database table through JPA, Hibernate, @Entity annotation
@Entity
public class IotDevice {
    public static final String IOT_DEVICE_TYPE = "IOT";

    public enum MajorRoad {
        I280,
        CA85,
        US101,
        I680,
        I880,
        CA237,
        CA87
    }

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column
    private Integer deviceIdNo;

    @NotNull
    @Pattern(regexp = RegExpClass.ALPHA_NUMERIC_DASH_REGEX)
    private String name;

    @NotNull
    @Pattern(regexp = RegExpClass.DD_COORDINATE_REGEX, message = "IOT Device location must be latitude,longitude")
    private String location;

    @Nullable
    private UUID userId;

    @NotNull
    private Boolean active;

    @Nullable
    private MajorRoad majorRoad;

    @NotNull
    private Date createdAt;

    private Date updatedAt;

    @NotNull
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

    public IotDevice(String name, String location, UUID userId, Boolean active, @Nullable MajorRoad majorRoad, Date createdAt, Date updatedAt, long createdAtTimestamp, long updatedAtTimestamp) {
        this.name = name;
        this.location = location;
        this.userId = userId;
        this.active = active;
        this.majorRoad = majorRoad;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.createdAtTimestamp = createdAtTimestamp;
        this.updatedAtTimestamp = updatedAtTimestamp;
    }

    public IotDevice(UUID id, Integer deviceIdNo, String name, String location, UUID userId, Boolean active, @Nullable MajorRoad majorRoad, Date createdAt, Date updatedAt, long createdAtTimestamp, long updatedAtTimestamp) {
        this.id = id;
        this.deviceIdNo = deviceIdNo;
        this.name = name;
        this.location = location;
        this.userId = userId;
        this.active = active;
        this.majorRoad = majorRoad;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.createdAtTimestamp = createdAtTimestamp;
        this.updatedAtTimestamp = updatedAtTimestamp;
    }

    public IotDevice(Integer deviceIdNo, String name, String location, UUID userId, Boolean active, Date createdAt, Date updatedAt, long createdAtTimestamp, long updatedAtTimestamp) {
        this.deviceIdNo = deviceIdNo;
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

    @Nullable
    public MajorRoad getMajorRoad() {
        return majorRoad;
    }

    public void setMajorRoad(@Nullable MajorRoad majorRoad) {
        this.majorRoad = majorRoad;
    }

    public Integer getDeviceIdNo() {
        return deviceIdNo;
    }

    public void setDeviceIdNo(Integer deviceIdNo) {
        this.deviceIdNo = deviceIdNo;
    }
}
