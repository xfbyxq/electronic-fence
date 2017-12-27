package com.semptian.common;

import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.Requests;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:spring/spring-*.xml"})
public class EsTest {

    private TransportClient client;

    @Before
    public void connectionEs() throws UnknownHostException {
        Settings build = Settings.builder().put("cluster.name", "elastic-cluster").build();

        client = new PreBuiltTransportClient(build);


        client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("192.168.80.230"), 9300));
        client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("192.168.80.231"), 9300));
        client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("192.168.80.233"), 9300));
    }

    @Test
    public void createIndex() {
        String index = "blog";
        Assert.assertTrue(client.admin().indices().prepareCreate(index).get().isAcknowledged());
    }

    @Test
    public void createIndex2() throws IOException, ExecutionException, InterruptedException {
        String index = "blog";
        String type = "default";
        XContentBuilder source = XContentFactory.jsonBuilder()
                .startObject()
                .startObject("properties")
                .startObject("id")
                .field("type", "string")
                .field("index", "not_analyzed")
                .endObject()
                .startObject("title")
                .field("type", "string")
                .endObject()
                .startObject("posttime")
                .field("type", "string")
                .field("index", "not_analyzed")
                .endObject()
                .startObject("content")
                .field("type", "string")
                .endObject()
                .endObject()
                .endObject();
        PutMappingRequest putMappingRequest = Requests.putMappingRequest(index).type(type).source(source);
        boolean result = client.admin().indices().putMapping(putMappingRequest).get().isAcknowledged();
        System.out.println(result);
        Assert.assertTrue(result);
    }


    @Test
    public void search1(){
        String index = "bbb";
        String type = "default";
        GetResponse response = client.prepareGet(index, type, "AWCRyKyTask4SnG66dZn").get();
        System.out.println(response.getSourceAsString());
    }

    @Test
    public void search2() throws ExecutionException, InterruptedException {
        QueryBuilder queryBuilder = QueryBuilders.wildcardQuery("title","git*");
        String index = "blog";
        String type = "default";
        SearchRequestBuilder search = client.prepareSearch(index);
        search.setTypes(type);
        search.setQuery(queryBuilder);
        SearchResponse searchResponse = search.execute().get();
        SearchHits hits = searchResponse.getHits();
        System.out.println("共查询到："+(hits.totalHits==0?"0条":hits.totalHits));
        for(SearchHit hit : hits){
            System.out.println(hit.getSource().get("id"));
            System.out.println(hit.getSource().get("title"));
            System.out.println(hit.getSource().get("posttime"));
            System.out.println(hit.getSource().get("content"));
        }
    }

    @Test
    public void delete(){
        String index = "bbb";
        String type = "default";
        String id ="AWCRyKyTask4SnG66dZn";
        DeleteResponse response = client.prepareDelete(index, type, id).get();
        System.out.println(response.getIndex());
        System.out.println(response.getType());
        System.out.println(response.getId());
        System.out.println(response.getVersion());
    }

    @Test
    public void update() throws IOException, ExecutionException, InterruptedException {
        String index = "bbb";
        String type = "default";
        String id ="AWCRyKynIEIUjeH-l2Qd";
        UpdateRequest updateRequest = new UpdateRequest();
        updateRequest.index(index);
        updateRequest.type(type);
        updateRequest.id(id);
        updateRequest.doc(XContentFactory.jsonBuilder()
                .startObject()
                // 对没有的字段添加, 对已有的字段替换
                .field("age","12")
                .endObject());
        UpdateResponse response = client.update(updateRequest).get();
        System.out.println(response.getIndex());
        System.out.println(response.getType());
        System.out.println(response.getId());
        System.out.println(response.getVersion());
    }


    @Test
    public void createMapping() throws IOException, ExecutionException, InterruptedException {
        String index = "zww20171226";
        String type = "persion";

        XContentBuilder builder = XContentFactory.jsonBuilder()
                .startObject()
                .field("properties")
                .startObject()
                .field("id")
                .startObject()
                .field("index", "not_analyzed")
                .field("type", "string")
                .endObject()
                .field("name")
                .startObject()
                .field("index", "not_analyzed")
                .field("type", "string")
                .endObject()
                .field("type")
                .startObject()
                .field("index", "not_analyzed")
                .field("type", "byte")
                .endObject()
                .field("geo_point")
                .startObject()
                .field("index", "not_analyzed")
                .field("type", "geo_point")
                .endObject()
                .field("imsi")
                .startObject()
                .field("index", "not_analyzed")
                .field("type", "string")
                .endObject()
                .field("timestamp")
                .startObject()
                .field("index", "not_analyzed")
                .field("type", "long")
                .endObject()
                .endObject()
                .endObject();
        PutMappingRequest putMapping = Requests.putMappingRequest(index).source(builder).type(type);
        Assert.assertTrue(client.admin().indices().putMapping(putMapping).get().isAcknowledged());
    }


    @Test
    public void createIndexAndData() {
        String index = "temp";
        String type = "defaulst";
       /* List<String> list = getInitJsonData();
        for (int x = 0; x < list.size(); x++) {
            IndexResponse indexResponse = client.prepareIndex(index, type).setSource(list.get(x)).get();
        }*/
    }

    @Test
    public void search() {
        String index = "zww20171226";
        String type = "persion";
        IndexResponse indexResponse = client.prepareIndex(index, type).get();
        System.out.println(indexResponse.getId());
    }

    @Test
    public void deleteIndex() {
        DeleteIndexResponse deleteIndexResponse = client.admin().indices().prepareDelete("semptian").get();
        System.out.println(deleteIndexResponse.isAcknowledged());
        Assert.assertTrue(deleteIndexResponse.isAcknowledged());
    }

    @Test
    public void isexist() {
        String index = "zww20171226";
        IndicesExistsResponse indicesExistsResponse = client.admin().indices().prepareExists(index).get();
        boolean exists = indicesExistsResponse.isExists();
        System.out.println(exists);

        Assert.assertTrue(exists);
    }


    @After
    public void destroy() {
        if (client != null) {
            client.close();
        }
    }

}
