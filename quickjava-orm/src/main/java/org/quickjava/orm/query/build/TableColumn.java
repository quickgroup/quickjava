package org.quickjava.orm.query.build;

import org.quickjava.orm.contain.Chain;
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

    protected String columnAlias;

    // 原生sql语句
    protected String raw;

    protected DriveConfigure driveConfigure;

    public TableColumn(String table, String column, String columnAlias) {
        this.table = table;
        this.column = column;
        this.columnAlias = columnAlias;
    }

    public TableColumn(String table, String column) {
        this.table = table;
        this.column = column;
    }

    public TableColumn(String column) {
        this.column = column;
    }

    public TableColumn() {
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
        if (raw != null) {
            return getRaw();
        }
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

    public String getColumnAlias() {
        return columnAlias;
    }

    public void setColumnAlias(String columnAlias) {
        this.columnAlias = columnAlias;
    }

    public String getRaw() {
        return raw;
    }

    public TableColumn setRaw(String raw) {
        this.raw = raw;
        return this;
    }

    public String toSql(DriveConfigure driveConfigure) {
        this.driveConfigure = driveConfigure;
        return getColumnSql();
    }
}
