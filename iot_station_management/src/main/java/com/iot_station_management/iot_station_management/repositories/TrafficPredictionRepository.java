package com.iot_station_management.iot_station_management.repositories;

import com.iot_station_management.iot_station_management.models.TrafficPrediction;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TrafficPredictionRepository extends MongoRepository<TrafficPrediction, UUID> {
    TrafficPrediction findFirstByDeviceIdNoOrderByTimestampDesc(Integer deviceIdNo);
}
