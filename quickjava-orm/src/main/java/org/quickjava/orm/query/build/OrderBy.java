package org.quickjava.orm.query.build;

import org.quickjava.orm.contain.DriveConfigure;
import org.quickjava.orm.query.enums.OrderByType;

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
public class OrderBy extends TableColumn {

    private OrderByType type;

    public OrderBy(String table, String field, OrderByType type) {
        super(table, field);
        this.type = type;
    }

    public OrderByType getType() {
        return type;
    }

    public void setType(OrderByType type) {
        this.type = type;
    }

    public String toSql(DriveConfigure driveConfigure) {
        this.driveConfigure = driveConfigure;
        return getColumnSql() + " " + getType().name();
    }
}
