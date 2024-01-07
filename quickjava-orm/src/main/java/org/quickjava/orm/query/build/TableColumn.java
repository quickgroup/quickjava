package org.quickjava.orm.query.build;

import org.quickjava.orm.contain.DriveConfigure;
import org.quickjava.orm.utils.SqlUtil;

/*
 * Copyright (c) 2020~2024 http://www.quickjava.org All rights reserved.
 * +-------------------------------------------------------------------
 * Organization: QuickJava
 * +-------------------------------------------------------------------
 * Author: Qlo1062
 * +-------------------------------------------------------------------
 * File: TableColumn
 * +-------------------------------------------------------------------
 * Date: 2024/1/7 21:14
 * +-------------------------------------------------------------------
 * License: Apache Licence 2.0
 * +-------------------------------------------------------------------
 */
public class TableColumn {

    protected String table;

    protected String column;

    protected DriveConfigure driveConfigure;

    public TableColumn(String table, String column) {
        this.table = table;
        this.column = column;
    }

    public TableColumn(String column) {
        this.column = column;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public String getColumnSql() {
        String columnSql = column;
        if (driveConfigure.columnLeft != null) {
            columnSql = driveConfigure.columnLeft + columnSql;
        }
        if (driveConfigure.columnRight != null) {
            columnSql = columnSql + driveConfigure.columnRight;
        }
        return table == null ? columnSql : SqlUtil.tableColumn(table, columnSql);
    }

    public String toSql(DriveConfigure driveConfigure) {
        this.driveConfigure = driveConfigure;
        return getColumnSql();
    }
}
