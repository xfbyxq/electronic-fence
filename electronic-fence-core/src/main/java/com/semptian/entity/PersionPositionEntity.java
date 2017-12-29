package com.semptian.entity;

import org.elasticsearch.common.geo.GeoPoint;
import org.elasticsearch.common.xcontent.XContentBuilder;

import java.io.IOException;
import java.io.Serializable;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

public class PersionPositionEntity implements Serializable{

    private String id;

    private String name;

    private Integer type;

    private String imsi;

    private GeoPoint geoPoint;

    private Long timestamp;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getImsi() {
        return imsi;
    }

    public void setImsi(String imsi) {
        this.imsi = imsi;
    }

    public GeoPoint getGeoPoint() {
        return geoPoint;
    }

    public void setGeoPoint(GeoPoint geoPoint) {
        this.geoPoint = geoPoint;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString(){
        XContentBuilder jsonBuild = null;
        try {
            jsonBuild = jsonBuilder()
            .startObject()
                .field("id", getId())
                .field("name", getName())
                .field("type", getType())
                .field("imsi", getImsi())
                .field("timestamp", getTimestamp())
                .startArray("geoPoint")
                    .value(getGeoPoint().lon())
                    .value(getGeoPoint().lat())
                .endArray()
            .endObject();
            return  jsonBuild.string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

}
