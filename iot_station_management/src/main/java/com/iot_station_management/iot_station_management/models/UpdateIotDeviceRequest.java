package com.iot_station_management.iot_station_management.models;

import com.iot_station_management.iot_station_management.utils.RegExpClass;
import jakarta.validation.constraints.Pattern;

public class UpdateIotDeviceRequest {
    private Boolean active;

    @Pattern(regexp = RegExpClass.ALPHA_NUMERIC_DASH_REGEX, message = "Updating IOT Device name must be alphanumeric")
    private String name;

    @Pattern(regexp = RegExpClass.DD_COORDINATE_REGEX, message = "Updating IOT Device location must be latitude,longitude")
    private String location;

    public UpdateIotDeviceRequest(Boolean active, String name, String location) {
        this.active = active;
        this.name = name;
        this.location = location;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
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
}
