package com.sentics.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class InputMessage  implements Serializable {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private String t;

    @JsonProperty("pos")
    private List<List<Double>> pos;

    @JsonProperty("heading")
    private List<Double> heading;

    @JsonProperty("speed")
    private List<Double> speed;

    @JsonProperty("classType")
    private List<String> classType; // Renamed from 'class' to 'classType' due to Java keyword

    @JsonProperty("id")
    private List<Long> id;

    @JsonProperty("area")
    private List<String> area;

    @JsonProperty("attributes")
    private List<Map<String, String>> attributes;

    @JsonProperty("overall_area")
    private Map<String, Integer> overallArea;


    // Getters and setters
    public String getT() {
        return t;
    }

    public void setT(String t) {
        this.t = t;
    }


    public List<List<Double>> getPos() {
        return pos;
    }

    public void setPos(List<List<Double>> pos) {
        this.pos = pos;
    }


    public List<Double> getHeading() {
        return heading;
    }

    public void setHeading(List<Double> heading) {
        this.heading = heading;
    }

    public List<Double> getSpeed() {
        return speed;
    }

    public void setSpeed(List<Double> speed) {
        this.speed = speed;
    }


    public List<String> getClassType() {
        return classType;
    }


    public void setClassType(List<String> classType) {
        this.classType = classType;
    }


    public List<Long> getId() {
        return id;
    }

    public void setId(List<Long> id) {
        this.id = id;
    }


    public List<String> getArea() {
        return area;
    }

    public void setArea(List<String> area) {
        this.area = area;
    }

    public List<Map<String, String>> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<Map<String, String>> attributes) {
        this.attributes = attributes;
    }

    public Map<String, Integer> getOverallArea() {
        return overallArea;
    }

    public void setOverallArea(Map<String, Integer> overallArea) {
        this.overallArea = overallArea;
    }



}
