package org.quickjava.orm.contain;

import org.quickjava.orm.enums.OrderByType;

/*
 * Copyright (c) 2020~2024 http://www.quickjava.org All rights reserved.
 * +-------------------------------------------------------------------
 * Organization: QuickJava
 * +-------------------------------------------------------------------
 * Author: Qlo1062
 * +-------------------------------------------------------------------
 * File: OrderBy
 * +-------------------------------------------------------------------
 * Date: 2024/1/7 18:37
 * +-------------------------------------------------------------------
 * License: Apache Licence 2.0
 * +-------------------------------------------------------------------
 */
public class OrderBy {

    private String table;

    private String field;

    private OrderByType type;

    public OrderBy(String table, String field, OrderByType type) {
        this.table = table;
        this.field = field;
        this.type = type;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public OrderByType getType() {
        return type;
    }

    public void setType(OrderByType type) {
        this.type = type;
    }
}
