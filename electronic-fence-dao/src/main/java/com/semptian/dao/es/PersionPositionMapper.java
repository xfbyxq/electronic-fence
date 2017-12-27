package com.semptian.dao.es;


import com.semptian.common.EsPageSearch;
import com.semptian.common.PageModel;
import com.semptian.entity.PersionPositionEntity;
import org.elasticsearch.common.recycler.Recycler;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

@Repository
public class PersionPositionMapper {

    @Autowired
    private EsPageSearch esPageSearch;

    /**
     * 创建索引
     *
     * @param index
     * @return
     */
    public boolean createIndex(String index) {
        if (StringUtils.isEmpty(index)) {
            return false;
        }
        boolean result = esPageSearch.createIndex(index);
        return result;
    }

    /**
     * 创建mapping
     *
     * @param index   索引
     * @param type    type
     * @param builder 对象
     * @return
     */
    public boolean createMapping(String index, String type, XContentBuilder builder) {
        if (StringUtils.isEmpty(index) || StringUtils.isEmpty(type) || builder == null) {
            return false;
        }
        return esPageSearch.createMapping(index, type, builder);
    }

    /**
     * 插入数据
     *
     * @param index
     * @param type
     * @param entity
     * @return
     */
    public boolean insert(String index, String type, PersionPositionEntity entity) {
        String id = entity.getId();
        if(!StringUtils.isEmpty(id)){
            return esPageSearch.createDoc(index, type, entity,id);
        }else{
            return esPageSearch.createDoc(index, type, entity);
        }
    }


    /**
     * 分业查询
     *
     * @param index        要查询的索引
     * @param type         要查询的type
     * @param queryBuilder 查询对象
     * @param onPage       当前页
     * @param size         每页数量
     * @return
     */
    public PageModel<PersionPositionEntity> findEntityByPage(String[] index, String[] type, QueryBuilder queryBuilder, int onPage, int size) {
        if (index == null || index.length == 0 || type == null || type.length == 0) {
            return PageModel.getEmpty();
        }

        if (onPage < 1) {
            onPage = 1;
        }

        if (size < 10) {
            size = 10;
        }
        PageModel<PersionPositionEntity> dataByPage = esPageSearch.getDataByPage(index, type, queryBuilder, onPage, size, PersionPositionEntity.class);
        return dataByPage;
    }

}
