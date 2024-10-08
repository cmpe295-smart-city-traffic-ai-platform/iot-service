package com.iot_station_management.iot_station_management.models;

import jakarta.persistence.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Date;

@Document("trafficprediction")
public class TrafficPrediction {
    @Id
    private int deviceIdNo;

    private Date createdAt;

    private long timestamp;

    ArrayList<Integer> speedPredictionValues;

    ArrayList<String> predictionTimestamps;


    public TrafficPrediction() {

    }

    public TrafficPrediction(int deviceIdNo, Date createdAt, long timestamp, ArrayList<Integer> speedPredictionValues, ArrayList<String> predictionTimestamps) {
        this.deviceIdNo = deviceIdNo;
        this.createdAt = createdAt;
        this.timestamp = timestamp;
        this.speedPredictionValues = speedPredictionValues;
        this.predictionTimestamps = predictionTimestamps;
    }


    public int getDeviceIdNo() {
        return deviceIdNo;
    }

    public void setDeviceIdNo(int deviceIdNo) {
        this.deviceIdNo = deviceIdNo;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public ArrayList<Integer> getSpeedPredictionValues() {
        return speedPredictionValues;
    }

    public void setSpeedPredictionValues(ArrayList<Integer> speedPredictionValues) {
        this.speedPredictionValues = speedPredictionValues;
    }

    public ArrayList<String> getPredictionTimestamps() {
        return predictionTimestamps;
    }

    public void setPredictionTimestamps(ArrayList<String> predictionTimestamps) {
        this.predictionTimestamps = predictionTimestamps;
    }
}
