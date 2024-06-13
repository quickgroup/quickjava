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
import org.quickjava.orm.enums.CompareType;
import org.quickjava.orm.enums.LogicType;
import org.quickjava.orm.model.ModelHelper;
import org.quickjava.orm.utils.QuickORMException;

/**
 * join的on条件
 */
public class JoinConditionAbs extends ConditionAbs<JoinConditionAbs> {

    // 左表
    protected String leftTable;
    protected String leftColumn;
    // 右表
    protected String rightTable;
    protected String rightColumn;
    protected Object rightValue;

    public JoinConditionAbs(LogicType logic, CompareType compare, String leftTable, String leftColumn, String rightTable, String rightColumn) {
        this.logic = logic;
        this.compare = compare;
        this.leftTable = leftTable;
        this.leftColumn = leftColumn;
        this.rightTable = rightTable;
        this.rightColumn = rightColumn;
    }

    public JoinConditionAbs(LogicType logic, CompareType compare, String leftTable, String rightTable) {
        this.logic = logic;
        this.compare = compare;
        this.leftTable = leftTable;
        this.rightTable = rightTable;
    }

    public JoinConditionAbs() {
    }

    public String getLeftTable() {
        return leftTable;
    }

    public void setLeftTable(String leftTable) {
        this.leftTable = leftTable;
    }

    public String getLeftColumn() {
        return leftColumn;
    }

    public void setLeftColumn(String leftColumn) {
        this.leftColumn = leftColumn;
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

    @Override
    public String toSql(DriveConfigure driveConfigure) {
        if (compare == CompareType.RAW) {
            return getRaw();
        }
        // null
        if (compare == CompareType.IS_NULL || compare == CompareType.IS_NOT_NULL) {
            return getLeftTable() + "." + ModelHelper.toUnderlineCase(getLeftColumn()) + " " + compare.sql();
        } else if (compare == CompareType.IN || compare == CompareType.NOT_IN) {
            throw new QuickORMException("暂不支持");
        }
        if (rightValue != null) {
            return ModelHelper.joinConditionSql(getLeftTable(), getLeftColumn(), compare, ValueConv.getConv(driveConfigure).convWrap(getRightValue()));
        }
        return ModelHelper.joinConditionSql(getLeftTable(), getLeftColumn(), compare, getRightTable(), getRightColumn());
    }
}
