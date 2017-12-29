<%--
  Created by IntelliJ IDEA.
  User: 97176
  Date: 2017-12-29
  Time: 12:18
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta name="viewport" content="initial-scale=1.0, user-scalable=no" />
    <style type="text/css">
        body, html,#allmap {width: 100%;height: 100%;overflow: hidden;margin:0;font-family:"微软雅黑";}
    </style>
    <script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=1fb17cf5db5f99fb586c3e97a3268d04"></script>
    <!--加载鼠标绘制工具-->
    <script type="text/javascript" src="http://api.map.baidu.com/library/DrawingManager/1.4/src/DrawingManager_min.js"></script>
    <link rel="stylesheet" href="http://api.map.baidu.com/library/DrawingManager/1.4/src/DrawingManager_min.css" />
    <script type="text/javascript" src="js/jquery-1.4.2.js" ></script>
    <script type="text/javascript" src="js/common/functions.js" ></script>
</head>
<body>
<div id="result">
    <input type="button" value="清除所有覆盖物" onclick="clearAll()"/>
</div>
<div style="margin: 10px 0 10px 0">
    <select id="searchType">
        <option value="1">酒店</option>
    </select>
    <label>查询内容!!!!!!</label>
    <input type="text" id="searchContent">
</div>
<div id="allmap" style="overflow:hidden;zoom:1;position:relative;">
    <div id="map" style="height:100%;-webkit-transition: all 0.5s ease-in-out;transition: all 0.5s ease-in-out;"></div>
</div>
<div id="model"></div>

<script type="text/javascript">
    // 百度地图API功能
    var map = new BMap.Map('map');
    var poi = new BMap.Point(116.404, 39.915);
    map.centerAndZoom(poi, 13);
    map.enableScrollWheelZoom();
    var overlays = [];
    var params ={
        searchContent:null,
        searchType:null,
        drawingType:null,
        pointList:[
        ],
        distance:null,
    }
    var myDrawingManagerObject = new BMapLib.DrawingManager(map, {isOpen: true,
        drawingType: BMAP_DRAWING_MARKER,
        enableDrawingTool: true,
        enableCalculate: false,
        drawingToolOptions: {
            anchor: BMAP_ANCHOR_TOP_LEFT,
            offset: new BMap.Size(5, 5),
            drawingModes : [
                BMAP_DRAWING_CIRCLE,BMAP_DRAWING_RECTANGLE
            ]
        },
        polylineOptions: {
            strokeColor: "#333"
        }});
    /*myDrawingManagerObject.enableCalculate();*/
    myDrawingManagerObject.addEventListener("circlecomplete", function(e, overlay) {
        overlays.push(e.overlay);
        var point = overlay.point;
        var distance = overlay.xa;
        console.log(point);
        console.log(distance)
        this.close();
        var current ="position/search.json";
        params.searchContent =$("#searchContent").val();
        params.searchType = $("#searchType").val();
        params.distance = distance;
        params.drawingType = 1;
        params.pointList=[{lat:point.lat,lon:point.lng}]
        commit(current,JSON.stringify(params),"POST",function (data) {
            if(data.length!=0){
                for (var i = 0; i < data.length; i ++) {
                    var geoPoint = data[i];
                    var point = new BMap.Point(geoPoint.geoPoint.lon,geoPoint.geoPoint.lat);
                    var opts = {
                        width : 200,     // 信息窗口宽度
                        height: 100,     // 信息窗口高度
                        title : geoPoint.name , // 信息窗口标题
                        enableMessage:true,//设置允许信息窗发送短息
                    }
                    var infoWindow = new BMap.InfoWindow(""+geoPoint.name, opts);
                    addMarker(infoWindow,point);
                }
            }else{
                alert("没有查到数据")
            }
        });
    });

    myDrawingManagerObject.addEventListener("rectanglecomplete", function(e, overlay) {
        overlays.push(e.overlay);
        var topleft = overlay.Nu.vl;
        var bottonright = overlay.Nu.Ml;
        this.close();
        var current ="position/search.json";
        params.searchContent =$("#searchContent").val();
        params.searchType = $("#searchType").val();
        params.distance = 0;
        params.drawingType = 0;
        params.pointList=[{lat:topleft.lat,lon:topleft.lng},{lat:bottonright.lat,lon:bottonright.lng}]
        commit(current,JSON.stringify(params),"POST",function (data) {
            if(data.length!=0){
                /*$("data").each(function(index,item){
                    alert($(this).text())
                });*/
                for (var i = 0; i < data.length; i ++) {
                    var geoPoint = data[i];
                    var point = new BMap.Point(geoPoint.geoPoint.lon,geoPoint.geoPoint.lat);
                    var opts = {
                        width : 200,     // 信息窗口宽度
                        height: 100,     // 信息窗口高度
                        title : geoPoint.name , // 信息窗口标题
                        enableMessage:true,//设置允许信息窗发送短息
                    }
                    var infoWindow = new BMap.InfoWindow(geoPoint.name, opts);
                    addMarker(infoWindow,point);

                }
            }else{
                alert("没有查到数据")
            }
        });
    });

    function clearAll() {
        map.clearOverlays();
        overlays.length = 0
    }

    // 编写自定义函数,创建标注
    function addMarker(infoWindow,point){
        var marker = new BMap.Marker(point);
        map.addOverlay(marker);
        marker.addEventListener("click",function () {
          map.openInfoWindow(infoWindow,point);
        })
    }
</script>
</body>
</html>
