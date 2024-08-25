package com.iot_station_management.iot_station_management;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class IotStationManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(IotStationManagementApplication.class, args);
	}

}
