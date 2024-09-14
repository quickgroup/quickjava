package org.quickjava.orm.domain;

import org.quickjava.orm.utils.SqlUtil;

/*
 * Copyright (c) 2020~2023 http://www.quickjava.org All rights reserved.
 * +-------------------------------------------------------------------
 * Organization: QuickJava
 * +-------------------------------------------------------------------
 * Author: Qlo1062
 * +-------------------------------------------------------------------
 * File: StatementConfig
 * +-------------------------------------------------------------------
 * Date: 2023-5-25 17:52
 * +-------------------------------------------------------------------
 * License: Apache Licence 2.0
 * +-------------------------------------------------------------------
 */
public class DriveConfigure {

    /**
     * 字段前后拼接字符串，mysql是`
     */
    public String columnLeft = null;
    public String columnRight = null;

    /**
     * 字符串值前后拼接字符串
     */
    public String valueStrLeft = "\"";
    public String valueStrRight = "\"";

    public DriveConfigure() {
    }

    public DriveConfigure(String columnLeft, String columnRight, String valueStrLeft, String valueStrRight) {
        this.columnLeft = columnLeft;
        this.columnRight = columnRight;
        this.valueStrLeft = valueStrLeft;
        this.valueStrRight = valueStrRight;
    }

    /**
     * 使用驱动配置拼接表名+字段名
     */
    public String tableColumn(String table, String column, String columnAlias) {
        DriveConfigure driveConfigure = this;
        String columnSql = column;
        if (driveConfigure.columnLeft != null) {
            columnSql = driveConfigure.columnLeft + columnSql;
        }
        if (driveConfigure.columnRight != null) {
            columnSql = columnSql + driveConfigure.columnRight;
        }
        // 表名
        if (table != null) {
            columnSql = SqlUtil.tableColumn(table, columnSql);
        }
        // 别名
        if (columnAlias != null) {
            columnSql = columnSql + " AS " + columnAlias;
        }
        return columnSql;
    }

    public String tableColumn(String table, String column) {
        return tableColumn(table, column, null);
    }
}
