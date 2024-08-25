package com.iot_station_management.iot_station_management.exceptions;

import java.time.LocalDateTime;

public class IotExceptionDetails {
    private LocalDateTime errorTimestamp;
    private String errorMessage;

    public IotExceptionDetails(LocalDateTime errorTimestamp, String errorMessage) {
        this.errorTimestamp = errorTimestamp;
        this.errorMessage = errorMessage;
    }

    public LocalDateTime getErrorTimestamp() {
        return errorTimestamp;
    }

    public void setErrorTimestamp(LocalDateTime errorTimestamp) {
        this.errorTimestamp = errorTimestamp;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
