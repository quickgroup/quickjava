package org.quickjava.framework.orm.contain;

import java.util.LinkedList;
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
public class Pagination<T> {

    public long page;

    public long pageSize;

    public long pages;

    public long total;

    public List<T> rows;

    public Pagination(Pagination<?> pagination) {
        this.page = pagination.page;
        this.pageSize = pagination.pageSize;
        this.pages = pagination.pages;
        this.total = pagination.total;
        this.rows = new LinkedList<>();
    }

    public Pagination(long page, long pageSize, long total, List<T> rows) {
        this.page = page;
        this.pageSize = pageSize;
        this.total = total;
        this.rows = rows;
        long totalMax = page * pageSize;
        this.pages = (total + totalMax -1) / totalMax;;
    }

    @Override
    public String toString() {
        return "Pagination{" +
                "page=" + page +
                ", pageSize=" + pageSize +
                ", lastPage=" + pages +
                ", total=" + total +
                ", rows=" + rows +
                '}';
    }
}
