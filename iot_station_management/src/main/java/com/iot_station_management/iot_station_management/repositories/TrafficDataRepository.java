package com.iot_station_management.iot_station_management.repositories;

import com.iot_station_management.iot_station_management.models.TrafficData;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.UUID;

@Repository
public interface TrafficDataRepository extends MongoRepository<TrafficData, UUID> {
    TrafficData findFirstByDeviceIdOrderByTimestampDesc(UUID deviceId);

    // mongo specific, aggregate results based on match and limit
    @Aggregation(pipeline = {
        "{ '$match': { 'deviceId' : ?0 } }",
        "{ '$limit' : ?1 }"
    })
    ArrayList<TrafficData> findByDeviceIdOrderByTimestampDesc(UUID deviceId, Integer limit);
}
