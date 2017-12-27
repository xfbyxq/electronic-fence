package com.semptian.common;

import org.apache.ibatis.annotations.Insert;
import org.apache.lucene.queryparser.flexible.core.parser.EscapeQuerySyntax;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Requests;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.net.InetAddress;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;

@Component
public class EsClient {

    private static final Logger logger= LoggerFactory.getLogger(EsClient.class);


    private TransportClient client;

    @Value("#{configProperties['es.cluster.name']}")
    private String clusterName;

    @Value("#{configProperties['es.address']}")
    private String address;

   /* private EsClient(){
    }

    private static EsClient esClient;

    public static EsClient getInstance(){
        if(esClient==null){
            synchronized (EsClient.class){
                if(esClient== null){
                    esClient = new EsClient();
                }
            }
        }

        return esClient;
    }*/

    @PostConstruct
    public void init(){
        if(client!=null){
            return;
        }
        if(StringUtils.isEmpty(clusterName) || StringUtils.isEmpty(address)){
            logger.warn("esClient init() clusterName || address is Empty");
            return;
        }

        String[] ipAddressArr = address.split(",");
        if(ipAddressArr.length==0){
            logger.warn("esClient init() address is Empty");
            return;
        }
        try {
            //设置集群名称
            Settings settings = Settings.builder().put("cluster.name", clusterName).build();
            //创建client
            client = new PreBuiltTransportClient(settings);

            for(int x=0;x<ipAddressArr.length;x++){
                if(StringUtils.isEmpty(ipAddressArr[x])){
                    logger.warn("current Ip is Empty");
                    continue;
                }
                String[] ipPort = ipAddressArr[x].split(":");
                if(ipPort.length==1){
                    client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(ipPort[0]), 9300));
                }else{
                    int port=9300;
                    try {
                        port = Integer.parseInt(ipPort[1]);
                    }catch (NumberFormatException e1){
                        logger.warn("esClient init() port is error");
                        continue;
                    }
                    client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(ipPort[0]), port));
                }
            }
        }
        catch (Exception e) {
            logger.warn(String.format("init client is fail ,Exception is %s",e.getMessage()));
            e.printStackTrace();
        }

        logger.info("es connection success!");
    }


    public TransportClient getClient() {
        return client;
    }

    public void destroy(){
        if(client!=null){
            client.close();
            logger.info("es destroy success!");
        }
    }



    public static void main(String[] args){
        EsClient client = new EsClient();

        System.out.println("es ");
        client.destroy();
    }

}
