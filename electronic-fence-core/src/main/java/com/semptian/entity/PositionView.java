package com.semptian.entity;

import org.elasticsearch.common.geo.GeoPoint;

import java.io.Serializable;
import java.util.List;

public class PositionView implements Serializable{

    private String searchContent;//查询内容

    private Integer searchType;//查询类型

    private Integer drawingType;//绘制类型


    private List<GeoPointEntity> pointList;//坐标点

    private Double distance;

    public String getSearchContent() {
        return searchContent;
    }

    public void setSearchContent(String searchContent) {
        this.searchContent = searchContent;
    }

    public Integer getSearchType() {
        return searchType;
    }

    public void setSearchType(Integer searchType) {
        this.searchType = searchType;
    }

    public Integer getDrawingType() {
        return drawingType;
    }

    public void setDrawingType(Integer drawingType) {
        this.drawingType = drawingType;
    }

    public List<GeoPointEntity> getPointList() {
        return pointList;
    }

    public void setPointList(List<GeoPointEntity> pointList) {
        this.pointList = pointList;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }
}
