package com.semptian.enums;

public enum DrawingTypeEnum {
    BMAP_DRAWING_MARKER(0),//画点
    BMAP_DRAWING_CIRCLE(1),//画圆
    BMAP_DRAWING_POLYLINE(2),//画线
    BMAP_DRAWING_POLYGON(3),//画多边形
    BMAP_DRAWING_RECTANGLE(4);//画矩形

    DrawingTypeEnum(int type) {
        this.type = type;
    }

    private Integer type;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
