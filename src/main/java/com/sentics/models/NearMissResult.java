package com.sentics.models;

import java.util.List;

public class NearMissResult {
    private String timestamp;
    private int vehicleId;
    private double speed;
    private double distance;
    private String classType;
    private boolean isNearMiss;


    public NearMissResult(String t, List<Long> id, double speedDifference, double distance, String classType, boolean b) {
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public long getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(int vehicleId) {
        this.vehicleId = vehicleId;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getClassType() {
        return classType;
    }

    public void setClassType(String classType) {
        this.classType = classType;
    }

    public boolean isNearMiss() {
        return isNearMiss;
    }

    public void setNearMiss(boolean nearMiss) {
        isNearMiss = nearMiss;
    }
}
