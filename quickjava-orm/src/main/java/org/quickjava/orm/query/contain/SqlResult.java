package org.quickjava.orm.query.contain;


/*
 * Copyright (c) 2020~2025 http://www.quickjava.org All rights reserved.
 * +-------------------------------------------------------------------
 * Organization: QuickJava
 * +-------------------------------------------------------------------
 * Author: Qlo1062
 * +-------------------------------------------------------------------
 * File: ExecuteResult
 * +-------------------------------------------------------------------
 * Date: 2025/3/14 1:06
 * +-------------------------------------------------------------------
 * License: Apache Licence 2.0
 * +-------------------------------------------------------------------
 */
public class SqlResult {

    /**
     * 影响行数
     */
    private int count;

    /**
     * 返回数据
     */
    private Object data;

    public SqlResult() {
    }

    public SqlResult(int count) {
        this.count = count;
    }

    public SqlResult(int count, Object data) {
        this.count = count;
        this.data = data;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public <T> T getData() {
        return (T) data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
