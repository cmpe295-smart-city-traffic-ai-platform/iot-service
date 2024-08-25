package com.iot_station_management.iot_station_management.models;

public class UpdateIotDeviceRequest {
    private Boolean active;

    private String name;

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
