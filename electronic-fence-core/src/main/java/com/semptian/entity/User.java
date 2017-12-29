package com.semptian.entity;

import java.io.Serializable;

public class User implements Serializable {
    public User(int id, String name, double lat, double lon) {
        this.id = id;
        this.name = name;
        this.lon = lon;
        this.lat = lat;
    }

    private Integer id;

    private String name;

    private double lon;

    private double lat;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }
}
