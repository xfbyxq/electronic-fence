package com.semptian.common;

import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Requests;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.util.concurrent.ExecutionException;

public abstract class EsBuilder {
    private static final Logger logger = LoggerFactory.getLogger(EsBuilder.class);

    @Autowired
    protected EsClient esClient;


    /**
     * 创建一个索引
     *
     * @param index
     */
    public boolean createIndex(String index) {
        if (StringUtils.isEmpty(index)) {
            logger.warn("index is empty");
            return false;
        }

        try {
            CreateIndexResponse indexResponse = esClient.getClient().admin().indices().prepareCreate(index).get();
            return indexResponse.isAcknowledged();
        } catch (Exception e) {
            logger.debug(String.format("function : carateIndexcreate(), createIndex is fail ,exception msg is %s", e.getMessage()));
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 创建mapping
     *
     * @param index   索引
     * @param type    type
     * @param builder mapping构件对象
     * @return true 创建成功  false 创建失败
     */
    public boolean createMapping(String index, String type, XContentBuilder builder) {
        if (StringUtils.isEmpty(index) || StringUtils.isEmpty(type) || builder == null) {
            logger.warn("index || type is empty");
            return false;
        }

        try {
            PutMappingRequest putMapping = Requests.putMappingRequest(index).source(builder).type(type);
            return esClient.getClient().admin().indices().putMapping(putMapping).get().isAcknowledged();
        } catch (ElasticsearchException e) {
            logger.debug(String.format("function : createMapping(), creageMapping is fail ,exception msg is %s", e.getMessage()));
            e.printStackTrace();
        } catch (InterruptedException e) {
            logger.debug(String.format("function : createMapping(), creageMapping is fail ,exception msg is %s", e.getMessage()));
            e.printStackTrace();
        } catch (ExecutionException e) {
            logger.debug(String.format("function : createMapping(), creageMapping is fail ,exception msg is %s", e.getMessage()));
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 创建doc
     *
     * @param index  索引
     * @param type   type
     * @param entity 对象
     * @return true 创建成功  false 创建失败
     */
    public <T> boolean createDoc(String index, String type, T entity, String id) {
        if (StringUtils.isEmpty(index) || StringUtils.isEmpty(type) || entity == null) {
            logger.warn("index || type is empty");
            return false;
        }
        try {
            String json = entity.toString();
            IndexRequestBuilder indexRequestBuilder = esClient.getClient().prepareIndex(index, type);
            if (!StringUtils.isEmpty(id)) {
                indexRequestBuilder.setId(id);
            }
            IndexResponse indexResponse = indexRequestBuilder.setSource(json).get();

            return indexResponse.getResult().getLowercase().equals("created") ? true : false;
        } catch (ElasticsearchException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 创建doc
     *
     * @param index  索引
     * @param type   type
     * @param entity 对象
     * @return true 创建成功  false 创建失败
     */
    public <T> boolean createDoc(String index, String type, T entity) {
        return createDoc(index, type, entity, null);
    }

    public boolean deleteDoc(String index, String type, String id) {
        if (StringUtils.isEmpty(index) || StringUtils.isEmpty(type) || StringUtils.isEmpty(id)) {
            return false;
        }
        DeleteResponse deleteResponse = esClient.getClient().prepareDelete(index, type, id).get();
        return deleteResponse.getResult().getLowercase().equals("deleted") ? true : false;
    }

}
