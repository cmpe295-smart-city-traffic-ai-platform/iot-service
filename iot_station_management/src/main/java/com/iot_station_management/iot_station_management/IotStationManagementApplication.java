package com.iot_station_management.iot_station_management;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;


/**
 * entry point into spring boot application
 * @SpringBootApplication annotation
 * - wrapper for following annotations
 * @SpringBootConfiguration - defines bean configuration
 * @EnableAutoConfiguration - enables auto configuration of beans, configure beans
 * @ComponentScan - scan beans throughout project within spring framework
 */
@SpringBootApplication
@EnableScheduling
@EnableAsync
public class IotStationManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(IotStationManagementApplication.class, args);
	}

}
