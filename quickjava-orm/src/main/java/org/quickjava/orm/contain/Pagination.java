package org.quickjava.orm.contain;

import java.util.List;

/*
 * Copyright (c) 2020~2023 http://www.quickjava.org All rights reserved.
 * +-------------------------------------------------------------------
 * Organization: QuickJava
 * +-------------------------------------------------------------------
 * Author: Qlo1062
 * +-------------------------------------------------------------------
 * File: Pagination
 * +-------------------------------------------------------------------
 * Date: 2023-2-20 15:59
 * +-------------------------------------------------------------------
 * License: Apache Licence 2.0
 * +-------------------------------------------------------------------
 */
public class Pagination<T> implements IPagination<T> {

    private static final long serialVersionUID = 240202L;

    public long page;

    public long pageSize;

    public long pages;

    /**
     * 分页数据id（大数据分页使用
     * */
    public long currentId;

    public long total;

    public List<T> rows;

    public Pagination() {
    }

    public Pagination(IPagination<?> pagination, List<T> rows) {
        this.page = pagination.getPage();
        this.pageSize = pagination.getPageSize();
        this.pages = pagination.getPages();
        this.total = pagination.getTotal();
        this.rows = rows;
    }

    public Pagination(long page, long pageSize, long total, List<T> rows) {
        this.page = page;
        this.pageSize = pageSize;
        this.total = total;
        this.rows = rows;
        long totalMax = page * pageSize;
        this.pages = totalMax <= 0 ? 0 : (total + totalMax -1) / totalMax;
    }

    public long getPage() {
        return page;
    }

    public void setPage(long page) {
        this.page = page;
    }

    public long getPageSize() {
        return pageSize;
    }

    public void setPageSize(long pageSize) {
        this.pageSize = pageSize;
    }

    public long getPages() {
        return pages;
    }

    public void setPages(long pages) {
        this.pages = pages;
    }

    public long getCurrentId() {
        return currentId;
    }

    public void setCurrentId(long currentId) {
        this.currentId = currentId;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }

    @Override
    public String toString() {
        return "Pagination{" +
                "page=" + page +
                ", pageSize=" + pageSize +
                ", pages=" + pages +
                ", total=" + total +
                ", rows=" + rows +
                '}';
    }
}
