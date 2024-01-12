package org.quickjava.orm.contain;

import java.util.List;

/*
 * Copyright (c) 2020~2024 http://www.quickjava.org All rights reserved.
 * +-------------------------------------------------------------------
 * Organization: QuickJava
 * +-------------------------------------------------------------------
 * Author: Qlo1062
 * +-------------------------------------------------------------------
 * File: IPagination
 * +-------------------------------------------------------------------
 * Date: 2024/1/12 10:17
 * +-------------------------------------------------------------------
 * License: Apache Licence 2.0
 * +-------------------------------------------------------------------
 */
public interface IPagination<T> {

    public long getPage();

    public void setPage(long page);

    public long getPageSize();

    public void setPageSize(long pageSize);

    public long getPages();

    public void setPages(long pages);

    public long getCurrentId();

    public void setCurrentId(long currentId);

    public long getTotal();

    public void setTotal(long total);

    public List<T> getRows();

    public void setRows(List<T> rows);

}
