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

import org.quickjava.orm.contain.DriveConfigure;
import org.quickjava.orm.enums.CompareEnum;
import org.quickjava.orm.model.ModelUtil;
import org.quickjava.orm.utils.QuickORMException;

/**
 * join的on条件
 */
public class JoinCondition extends TableColumn {

    // 1=AND、2=OR
    protected int logic = 1;

    protected CompareEnum compare;

    protected String rightTable;

    protected String rightColumn;

    // 值、原始sql
    protected Object rightValue;

    public JoinCondition(String table, String column, String columnAlias, int logic, CompareEnum compare, String rightTable, String rightColumn, Object rightValue) {
        super(table, column, columnAlias);
        this.logic = logic;
        this.compare = compare;
        this.rightTable = rightTable;
        this.rightColumn = rightColumn;
        this.rightValue = rightValue;
    }

    public JoinCondition(String table, String column, int logic, CompareEnum compare, String rightTable, String rightColumn) {
        super(table, column);
        this.logic = logic;
        this.compare = compare;
        this.rightTable = rightTable;
        this.rightColumn = rightColumn;
    }

    public JoinCondition(String table, String column, int logic, CompareEnum compare, Object rightValue) {
        super(table, column);
        this.logic = logic;
        this.compare = compare;
        this.rightValue = rightValue;
    }

    public JoinCondition(String column, int logic, CompareEnum compare, String rightTable, String rightColumn, Object rightValue) {
        super(column);
        this.logic = logic;
        this.compare = compare;
        this.rightTable = rightTable;
        this.rightColumn = rightColumn;
        this.rightValue = rightValue;
    }

    public JoinCondition(String column) {
        super(column);
    }

    public int getLogic() {
        return logic;
    }

    public void setLogic(int logic) {
        this.logic = logic;
    }

    public CompareEnum getCompare() {
        return compare;
    }

    public void setCompare(CompareEnum compare) {
        this.compare = compare;
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

    public String getRaw() {
        return raw;
    }

    public JoinCondition setRaw(String raw) {
        this.raw = raw;
        return this;
    }

    @Override
    public String toSql(DriveConfigure driveConfigure) {
        super.toSql(driveConfigure);
        if (raw != null) {
            return raw;
        }
        // null
        if (compare == CompareEnum.IS_NULL || compare == CompareEnum.IS_NOT_NULL) {
            return getTable() + "." + ModelUtil.toUnderlineCase(getColumn()) + " " + compare.sql();
        } else if (compare == CompareEnum.IN || compare == CompareEnum.NOT_IN) {
            throw new QuickORMException("暂不支持");
        }
        if (getRightValue() != null) {
            return ModelUtil.joinConditionSql(getTable(), getColumn(), compare, ValueConv.getConv(driveConfigure).conv(getRightValue()));
        }
        return ModelUtil.joinConditionSql(getTable(), getColumn(), compare, getRightTable(), getRightColumn());
    }
}
