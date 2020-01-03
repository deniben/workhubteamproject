package com.spring.utils;

import com.spring.exception.OtherException;

import java.util.ArrayList;
import java.util.List;

public class Page<T> {

    private Integer pageSize = 10;
    private List<T> data = new ArrayList<>();

    private static final String PAGE_SIZE_EXCEPTION_MESSAGE = "Page size must be grater than 0";

    public Page(List<T> data, Integer pageSize) {
        if (pageSize < 1) {
            throw new OtherException(PAGE_SIZE_EXCEPTION_MESSAGE);
        }
        this.data = data;
        this.pageSize = pageSize;
    }

    public Page(List<T> data) {
        this.data = data;
    }

    public Integer countPages() {
        Integer dataSize = data.size();
        float pages = (float) dataSize / (float) pageSize;
        if (pages * 10000 > ((int) pages) * 10000) {
            pages++;
        }
        return (int) pages;
    }

    public List<T> getItemsOnPage(Integer page) {
        if (page < 0 || page >= countPages()) {
            return new ArrayList<>();
        }
        Integer leftBound = pageSize * page;
        Integer rightBound = leftBound + pageSize;
        if (data.size() >= rightBound) {
            return data.subList(leftBound, rightBound);
        }
        return data.subList(leftBound, data.size());
    }

    public static Integer countPages(Integer totalSize, Integer pageSize) {
        Double pagesCount = (double)totalSize / (double)pageSize;
        final Integer CONST = 100000;

        if( (pagesCount * CONST - pagesCount.intValue() * CONST) > 0) {
            pagesCount++;
        }

        return pagesCount.intValue();
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public void setPageSize(Integer pageSize) {
        if (pageSize < 1) {
            throw new OtherException(PAGE_SIZE_EXCEPTION_MESSAGE);
        }
        this.pageSize = pageSize;
    }

    public Integer getPageSize() {
        return pageSize;
    }
}
