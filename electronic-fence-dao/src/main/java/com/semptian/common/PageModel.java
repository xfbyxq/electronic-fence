package com.semptian.common;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * es 返回的分页数据
 *
 * @param <T>
 */
public class PageModel<T> {

    public PageModel(List listData, long total, int size, int onPage) {
        this.listData = listData;
        this.total = total;
        this.size = size;
        this.onPage = onPage;
    }

    private List<T> listData;

    private Long total;

    private Integer size;

    private Integer onPage;


    public List<T> getListData() {
        return listData;
    }

    public void setListData(List<T> listData) {
        this.listData = listData;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        if (size == null || size < 10) {
            size = 10;
        }
        this.size = size;
    }

    public Integer getOnPage() {
        return onPage;
    }

    public void setOnPage(Integer onPage) {
        if (onPage == null || onPage < 1) {
            onPage = 1;
        }
        this.onPage = onPage;
    }

    public static PageModel getEmpty() {
        return new PageModel(Lists.newArrayList(), 0, 50, 1);
    }
}
