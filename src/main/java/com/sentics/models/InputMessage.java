package com.sentics.models;

import java.util.List;
import java.util.Map;

public class InputMessage {
    private String t;
    private List<List<Double>> pos;
    private List<Double> heading;
    private List<Double> speed;
    private List<String> classType; // Renamed from 'class' to 'classType' due to Java keyword
    private List<Long> id;
    private List<String> area;
    private List<Map<String, String>> attributes;
    private Map<String, Integer> overall_area;

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

    public Map<String, Integer> getOverall_area() {
        return overall_area;
    }

    public void setOverall_area(Map<String, Integer> overall_area) {
        this.overall_area = overall_area;
    }

}
