package com.semptian.common;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.sort.SortBuilder;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.semptian.utils.EsUtils.*;

@Component
public class EsPageSearch extends EsBuilder {


    public<T> PageModel<T> getDataByPage(String[] indexs, String[] types, QueryBuilder queryBuilder, int onPage, int size, Class<T> entity) {
        return getDataByPage(indexs,types,queryBuilder,null,null,onPage,size,entity);
    }

    public<T> PageModel<T> getDataByPage(String[] indexs, String[] types, QueryBuilder queryBuilder, QueryBuilder postFilter, SortBuilder sortBuilder, int onPage, int size, Class<T> entity) {
        if (indexs == null || indexs.length == 0 || types == null || types.length == 0) {
            return PageModel.getEmpty();
        }
        int from = 1;
        if (onPage < 1) {
            onPage = 1;
        }
        from = (onPage - 1) * size;
        SearchRequestBuilder searchRequestBuilder = this.esClient.getClient().prepareSearch(indexs);
        searchRequestBuilder.setTypes(types);
        searchRequestBuilder.setQuery(queryBuilder);
        searchRequestBuilder.setFrom(from);
        searchRequestBuilder.setSize(size);
        if(postFilter!=null){
            searchRequestBuilder.setPostFilter(postFilter);
        }

        if(sortBuilder!=null){
            searchRequestBuilder.addSort(sortBuilder);
        }


        SearchResponse searchResponse = searchRequestBuilder
                                            .execute()
                                            .actionGet();
        SearchHits hits = searchResponse.getHits();
        long totalHits = hits.getTotalHits();
        List<T> list = conversion(hits, entity);

        if(list.isEmpty()){
            return PageModel.getEmpty();
        }
        PageModel pageModel = new PageModel<T>(list, totalHits, size, onPage);
        return pageModel;
    }

}
