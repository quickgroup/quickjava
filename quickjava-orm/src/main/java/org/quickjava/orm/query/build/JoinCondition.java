package org.quickjava.orm.query.build;

/*
 * Copyright (c) 2020~2024 http://www.quickjava.org All rights reserved.
 * +-------------------------------------------------------------------
 * Organization: QuickJava
 * +-------------------------------------------------------------------
 * Author: Qlo1062
 * +-------------------------------------------------------------------
 * File: JoinCondition
 * +-------------------------------------------------------------------
 * Date: 2024/1/7 23:59
 * +-------------------------------------------------------------------
 * License: Apache Licence 2.0
 * +-------------------------------------------------------------------
 */

/**
 * join的on条件
 */
public class JoinCondition extends TableColumn {

    protected String rightTable;

    protected String rightColumn;

    // 值、原始sql
    protected Object rightValue;

    public JoinCondition(String table, String column, String rightTable, String rightColumn) {
        super(table, column);
        this.rightTable = rightTable;
        this.rightColumn = rightColumn;
    }

    public JoinCondition(String table, String column, Object rightValue) {
        super(table, column);
        this.rightValue = rightValue;
    }

    public String getRightTable() {
        return rightTable;
    }

    public void setRightTable(String rightTable) {
        this.rightTable = rightTable;
    }

    public String getRightColumn() {
        return rightColumn;
    }

    public void setRightColumn(String rightColumn) {
        this.rightColumn = rightColumn;
    }

    public Object getRightValue() {
        return rightValue;
    }

    public void setRightValue(Object rightValue) {
        this.rightValue = rightValue;
    }
}
