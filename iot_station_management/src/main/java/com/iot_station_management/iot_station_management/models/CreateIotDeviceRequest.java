package com.iot_station_management.iot_station_management.models;

import com.iot_station_management.iot_station_management.utils.RegExpClass;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.util.UUID;

public class CreateIotDeviceRequest {

    @NotBlank
    @Pattern(regexp = RegExpClass.ALPHA_NUMERIC_DASH_REGEX, message = "IOT Device name must be alpha numeric")
    String name;

    @NotBlank
    @Pattern(regexp = RegExpClass.DD_COORDINATE_REGEX, message="Location must be in latitude,longitude format")
    String location;

    @NotNull
    UUID userId;

    @NotNull
    Boolean active;

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

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
