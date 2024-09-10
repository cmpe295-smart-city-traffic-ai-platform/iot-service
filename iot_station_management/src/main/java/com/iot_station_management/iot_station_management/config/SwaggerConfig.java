package com.iot_station_management.iot_station_management.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;

public class SwaggerConfig {
    @Bean
    GroupedOpenApi iotApi() {
        return GroupedOpenApi.builder().group("iot").pathsToMatch("/**").build();
    }
}
