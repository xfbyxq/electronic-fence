package com.semptian.common;

import com.semptian.entity.BlogEntity;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath*:spring/spring-*.xml"})
public class EsPageBuilderTest {

    @Autowired
    private EsPageSearch pageSearch;

    @Test
    public void findEntityByPage(){
        QueryBuilder queryBuilder = QueryBuilders.wildcardQuery("title","git*");
        String[] indexs = new String[]{"blog"};
        String[] types = new String[]{"default"};
        PageModel<BlogEntity> dataByPage = pageSearch.getDataByPage(indexs, types, queryBuilder, 1, 10, BlogEntity.class);
        System.out.println(dataByPage.getTotal());
        List<BlogEntity> listData = dataByPage.getListData();
        for(BlogEntity entity : listData){
            System.out.println(entity);
        }
    }
}
