package com.semptian.common;

import org.apache.lucene.queryparser.flexible.core.parser.EscapeQuerySyntax;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Requests;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.cluster.node.DiscoveryNode;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath*:spring/spring-*.xml"})
public class EsClientTest {

    private EsClient esClent;


    @Before
    public void init(){
        esClent.init();
    }
    /**
     * 查看集群信息
     */
    @Test
    public void testInfo() {
        TransportClient client = esClent.getClient();
        List<DiscoveryNode> nodes = client.connectedNodes();
        for (DiscoveryNode node : nodes) {
            System.out.println("getHostAddress:"+node.getHostAddress()+"    getEphemeralId:"+node.getEphemeralId()+"    hostName:"+node.getHostName()+"     address:"+node.getAddress());
        }
    }

    @Test
    public void createIndex(){
        /*String index="demo3";
        esClent.createIndex(index);*/
    }

    /**
     * 创建一个索引
     * @param indexName 索引名
     */
    public void createIndex(String indexName) {
        try {
            CreateIndexResponse indexResponse = esClent.getClient()
                    .admin()
                    .indices()
                    .prepareCreate(indexName)
                    .get();

            System.out.println(indexResponse.isAcknowledged()); // true表示创建成功
        } catch (ElasticsearchException e) {
            e.printStackTrace();
        }
    }



    @Test
    public void createMapping() throws IOException {
        /*String index="demo3";
        String type="default";
        XContentBuilder builder =
                XContentFactory.jsonBuilder()
                        .startObject()
                        .field("properties")
                        .startObject()
                        .field("name")
                        .startObject()
                        .field("index", "analyzed")
                        .field("type", "string")
                        .endObject()
                        .field("type")
                        .startObject()
                        .field("index", "not_analyzed")
                        .field("type", "integer")
                        .endObject()
                        .field("imsi")
                        .startObject()
                        .field("index", "not_analyzed")
                        .field("type", "string")
                        .endObject()
                        .endObject()
                        .endObject();
        Assert.assertTrue(esClent.createMapping(index, type, builder));*/
    }

    /**
     * 给索引增加mapping。
     * @param index 索引名
     * @param type mapping所对应的type
     */
    public void addMapping(String index, String type) {
        try {
            // 使用XContentBuilder创建Mapping
            XContentBuilder builder =
                    XContentFactory.jsonBuilder()
                            .startObject()
                                .field("properties")
                                .startObject()
                                    .field("name")
                                    .startObject()
                                        .field("index", "not_analyzed")
                                        .field("type", "string")
                                    .endObject()
                                     .field("age")
                                    .startObject()
                                        .field("index", "not_analyzed")
                                        .field("type", "integer")
                                    .endObject()
                                .endObject()
                            .endObject();
            System.out.println(builder.string());
            PutMappingRequest mappingRequest = Requests.putMappingRequest(index).source(builder).type(type);
            esClent.getClient().admin().indices().putMapping(mappingRequest).actionGet();
        } catch (ElasticsearchException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void addDoc(){
        String index="electronicfence20171225";
        String type="default";
        createDoc(index,type);
    }

    /**
     * 创建一个文档
     * @param index index
     * @param type type
     */
    public void createDoc(String index, String type) {

        try {
            // 使用XContentBuilder创建一个doc source
            XContentBuilder builder =
                    XContentFactory.jsonBuilder()
                            .startObject()
                            .field("name", "zhangsan")
                            .field("age", "14")
                            .endObject();

            IndexResponse indexResponse = esClent.getClient()
                    .prepareIndex()
                    .setIndex(index)
                    .setType(type)
                    // .setId(id) // 如果没有设置id，则ES会自动生成一个id
                    .setSource(builder.string())
                    .get();
        } catch (ElasticsearchException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    @After
    public void destroy(){
        esClent.destroy();
    }
}
