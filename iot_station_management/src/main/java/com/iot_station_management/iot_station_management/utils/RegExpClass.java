package com.iot_station_management.iot_station_management.utils;

import org.springframework.stereotype.Component;

@Component
public class RegExpClass {
    // reference: https://www.baeldung.com/java-geo-coordinates-validation
    public static final String DD_COORDINATE_REGEX = "^(-?\\d+\\.\\d+)(\\s*,\\s*)?(-?\\d+\\.\\d+)$";

    public static final String ALPHA_NUMERIC_DASH_REGEX = "^[a-zA-Z0-9_\s]*$";
}
