# IOT Station Management Service



Backend HTTP microservice dedicated to IOT station management functionalities. Built with Java Spring Boot, bootstrapped using https://start.spring.io/.

* Spring Boot 3.3.1
* Java JDK 17
* Maven 4.0.0

# Requirements
* IntelliJ - IDE used for Java development - https://www.jetbrains.com/idea/download/
* Docker - Used to spin up containers for development - https://www.docker.com/products/docker-desktop/. Docker Compose is used to run multiple containers at once.
* Java - JDK 17 or higher, installed on machine, should be setup within IDE
* Kubernetes - More info later... (kind cluster etc.)



# Project Structure
```bash
└── iot_station_management
    ├── IotStationManagementApplication.java
    ├── config
    │   └── AsyncConfiguration.java
    ├── controllers
    │   └── IotDeviceController.java
    ├── exceptions
    │   ├── IotDeviceEntityExceptionHandler.java
    │   └── IotExceptionDetails.java
    ├── models
    │   ├── CreateIotDeviceRequest.java
    │   ├── IotDevice.java
    │   ├── TrafficData.java
    │   └── UpdateIotDeviceRequest.java
    ├── repositories
    │   ├── IotDeviceRepository.java
    │   └── TrafficDataRepository.java
    ├── schedulers
    │   └── IotDevicePollingScheduler.java
    └── services
        ├── IotDeviceService.java
        └── IotDeviceServiceImpl.java
```


# Components:

### Entry Point:
`IotStationManagementApplication.java` is the entry point for service. Starts up Spring-Boot and Spring framework to handle HTTP requests.

## Controllers:

Handles URI mappings when a request is received.

## Services:

Handles business logic. Interactions between controller and database.

## Repositories:

Handles database interactions.

## Models:

Represent object classes. For database entites, models are mapped to database tables/collections.

# Development Instructions

1. Clone repository locally from https://github.com/cmpe295-smart-city-traffic-ai-platform/iot-service.git. 
2. Navigate to `iot_station_management` directory.
3. In IntelliJ open project directory for `iot_station_management`. This should open the project within IDE and start installing dependencies through Maven.
4. From root directory of `iot_station_management` run command `docker-compose up -d`. This will start up local Postgres and Mongo databases running in containers. The local Spring Boot application will try to connect to the databases configured in `application-dev.properties`. The configurations of the containers can be found within `docker-compose.yml`. 
5. Go to `IotStationManagementApplication.java`, in the top right corner press green play button to start the application. After project is compiled, service will be running locally at `localhost:8080` (port 8080)
6. Requests can be made via Postman or cURL to `http://localhost:8000/api/iot/v1` 
