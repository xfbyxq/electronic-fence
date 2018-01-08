package com.semptian.dao.es;

import com.google.common.collect.Lists;
import com.semptian.common.PageModel;
import com.semptian.entity.PersionPositionEntity;
import com.semptian.utils.UUIDUtils;
import org.elasticsearch.common.geo.GeoPoint;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/spring-*.xml"})
public class PersionPositionTest {

    @Autowired
    private PersionPositionMapper persionPosition;

    private String index = "electronicfence";

    private String type = "default";

    @Test
    public void createIndex(){
        Assert.assertTrue(persionPosition.createIndex(index));
    }

    @Test
    public void createMapping(){
        XContentBuilder builder = null;
        try {
            builder = XContentFactory.jsonBuilder()
                    .startObject()
                    .startObject("properties")
                    .field("id")
                    .startObject()
                    .field("type","string")
                    .field("index","not_analyzed")
                    .endObject()
                    .field("name")
                    .startObject()
                    .field("type","string")
                    .field("index","analyzed")
                    .endObject()
                    .field("imsi")
                    .startObject()
                    .field("type","string")
                    .field("index","not_analyzed")
                    .endObject()
                    .field("type")
                    .startObject()
                    .field("type","integer")
                    .field("index","not_analyzed")
                    .endObject()
                    .field("geoPoint")
                    .startObject()
                    .field("type","geo_point")
                    .endObject()
                    .field("timestamp")
                    .startObject()
                    .field("type","long")
                    .field("index","not_analyzed")
                    .endObject()
                    .endObject()
                    .endObject();
        } catch (IOException e) {
            e.printStackTrace();
        }

        boolean resut = persionPosition.createMapping(index, type, builder);
        Assert.assertTrue(resut);
    }


    @Test
    public void addData(){
        PersionPositionEntity entity = new PersionPositionEntity();
        entity.setId(UUIDUtils.getUUID());
        entity.setImsi("822229274577");
        entity.setName("北京金辉国际商务会议大酒店");
        entity.setType(1);
        entity.setTimestamp(new Date().getTime());
        entity.setGeoPoint(new GeoPoint(39.962617,116.356492));
        boolean result = persionPosition.insert(index, type, entity);
        System.out.println(result);
        Assert.assertEquals(result,true);
    }

    @Test
    public void addList(){
        List<PersionPositionEntity> list  = Lists.newArrayList();
        PersionPositionEntity entity = new PersionPositionEntity();
        entity.setId(UUIDUtils.getUUID());
        entity.setImsi("83322927377");
        entity.setName("汉庭酒店(亚运村店)");
        entity.setType(1);
        entity.setTimestamp(new Date().getTime());
        entity.setGeoPoint(new GeoPoint(39.989761,116.415808));
        list.add(entity);
        entity = new PersionPositionEntity();
        entity.setId(UUIDUtils.getUUID());
        entity.setImsi("822223322233");
        entity.setName("格林豪泰酒店(安贞鸟巢店)");
        entity.setType(1);
        entity.setTimestamp(new Date().getTime());
        entity.setGeoPoint(new GeoPoint(39.979514,116.402765));
        list.add(entity);
        entity = new PersionPositionEntity();
        entity.setId(UUIDUtils.getUUID());
        entity.setImsi("822229274577");
        entity.setName("北京金辉国际商务会议大酒店");
        entity.setType(1);
        entity.setTimestamp(new Date().getTime());
        entity.setGeoPoint(new GeoPoint(39.962617,116.356492));
        list.add(entity);
        entity = new PersionPositionEntity();
        entity.setId(UUIDUtils.getUUID());
        entity.setImsi("822229274577");
        entity.setName("玫瑰园酒店");
        entity.setType(1);
        entity.setTimestamp(new Date().getTime());
        entity.setGeoPoint(new GeoPoint(39.976557,116.388459));
        list.add(entity);

        for(PersionPositionEntity persion : list){
            boolean result = persionPosition.insert(index, type, entity);
            System.out.println(result);
        }
    }

    @Test
    public void searchData(){
        QueryBuilder builder = QueryBuilders.matchAllQuery();
        String[] indexs= new String[]{"electronicfence"};
        String[] types = new String[]{"default"};
        PageModel<PersionPositionEntity> entityByPage = persionPosition.findEntityByPage(indexs, types, builder, 1, 10);
        List<PersionPositionEntity> listData = entityByPage.getListData();
        if(listData.isEmpty()){
            System.out.println("没有查询到数据");
        }
        for(PersionPositionEntity entity : listData){
            System.out.println(entity);
        }
    }

    @Test
    public void deleteByID(){
        String[] indexs= new String[]{"electronicfence"};
        String[] types = new String[]{"default"};
        boolean delete = persionPosition.delete(indexs[0], types[0], "52bdc9d3dc1f465aa378bc03bb2e3cbc");
        System.out.println(delete);
        Assert.assertTrue(delete);
    }
}
