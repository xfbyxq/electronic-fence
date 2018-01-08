package com.semptian.service;

import com.google.common.collect.Lists;
import com.semptian.common.PageModel;
import com.semptian.dao.es.PersionPositionMapper;
import com.semptian.entity.GeoPointEntity;
import com.semptian.entity.PersionPositionEntity;
import com.semptian.entity.PositionView;
import org.elasticsearch.common.geo.GeoPoint;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.sort.GeoDistanceSortBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.management.Query;
import java.util.List;

@Service
public class PersionPositionService {

    @Autowired
    private PersionPositionMapper persionPositionMapper;

    public List<PersionPositionEntity> search(PositionView positionView){
        if(positionView.getDrawingType()==null || positionView.getPointList()==null || positionView.getPointList().isEmpty()){
            return Lists.newArrayList();
        }

        Integer searchType = positionView.getSearchType();
        String searchContent = positionView.getSearchContent();

        String[] indexs = new String[]{"pointdemo"};
        String[] types = new String[]{"default"};
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        if(searchType !=null && (searchType>=0|| searchType <=5)){
            queryBuilder.must(QueryBuilders.termQuery("type",searchType));
        }

        if(!StringUtils.isEmpty(searchContent)){
            queryBuilder.must(QueryBuilders.matchQuery("name","%"+searchContent+"%"));
        }

        Integer drawingType = positionView.getDrawingType();
        List<GeoPointEntity> pointList = positionView.getPointList();

        PageModel<PersionPositionEntity> pageModel =null;
        QueryBuilder postFilter =null;
        GeoDistanceSortBuilder sort=null;
        switch (drawingType){
            case 0://矩形
                if(pointList.isEmpty()){
                    return Lists.newArrayList();
                }
                GeoPointEntity topleft = pointList.get(0);
                GeoPointEntity buttonright = pointList.get(1);
                postFilter = QueryBuilders.geoBoundingBoxQuery("geoPoint")
                    .setCorners(topleft.getLat(),buttonright.getLon(),buttonright.getLat(),topleft.getLon());
                break;
            case 1://圆
                if(pointList.isEmpty()){
                    return Lists.newArrayList();
                }
                GeoPointEntity geoPoint = pointList.get(0);
                postFilter = QueryBuilders.geoDistanceQuery("geoPoint").point(geoPoint.getLat(),geoPoint.getLon())
                                                    .distance(positionView.getDistance(), DistanceUnit.METERS);
                // 获取距离多少公里 这个才是获取点与点之间的距离的
                sort = SortBuilders.geoDistanceSort("geoPoint",geoPoint.getLat(),geoPoint.getLon());
                sort.unit(DistanceUnit.METERS);
                sort.order(SortOrder.ASC);
                sort.point(geoPoint.getLat(),geoPoint.getLon());

                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
        }
        pageModel = persionPositionMapper.findEntityByPage(indexs, types, queryBuilder, postFilter, sort, 1, 100);

        if(pageModel!=null){
            return pageModel.getListData();
        }

        return Lists.newArrayList();
    }

    public List<PersionPositionEntity> findName(String name){
        if(StringUtils.isEmpty(name)){
            return  Lists.newArrayList();
        }

        QueryBuilder queryBuilder = QueryBuilders.matchQuery("name","%"+name+"%");

        String[] indexs = new String[]{"electronicfence"};
        String[] types = new String[]{"default"};

        PageModel<PersionPositionEntity> pageModel = persionPositionMapper.findEntityByPage(indexs, types, queryBuilder, 1, 100);

        if(pageModel.getListData()!=null){
            return pageModel.getListData();
        }
        return Lists.newArrayList();
    }
}
