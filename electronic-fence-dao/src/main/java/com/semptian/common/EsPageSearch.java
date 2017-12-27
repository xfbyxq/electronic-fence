package com.semptian.common;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.SearchHits;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.semptian.utils.EsUtils.*;

@Component
public class EsPageSearch extends EsBuilder {


    public<T> PageModel<T> getDataByPage(String[] indexs, String[] types, QueryBuilder queryBuilder, int onPage, int size, Class<T> entity) {
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
        SearchResponse searchResponse = searchRequestBuilder.execute().actionGet();
        SearchHits hits = searchResponse.getHits();
        long totalHits = hits.getTotalHits();
        List<T> list = conversion(hits, entity);

        PageModel pageModel = new PageModel<T>(list, totalHits, size, onPage);
        return pageModel;
    }

}
