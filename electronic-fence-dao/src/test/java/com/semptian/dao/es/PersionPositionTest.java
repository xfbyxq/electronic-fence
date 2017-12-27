package com.semptian.dao.es;

import com.semptian.entity.PersionPositionEntity;
import com.semptian.utils.UUIDUtils;
import org.elasticsearch.common.geo.GeoPoint;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.util.Date;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/spring-*.xml"})
public class PersionPositionTest {

    @Autowired
    private PersionPositionMapper persionPosition;

    private String index = "wifi";

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
                    .field("geo_point")
                    .startObject()
                    .field("type","geo_point")
                    .field("index","not_analyzed")
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
        entity.setImsi("12391723182");
        entity.setName("王力");
        entity.setType(1);
        entity.setTimestamp(new Date().getTime());
        entity.setGeoPoint(new GeoPoint(1112,233));
        boolean result = persionPosition.insert(index, type, entity);
        System.out.println(result);
        Assert.assertTrue(result);
    }
}
