package org.quickjava.orm.query.build;

import org.quickjava.orm.enums.JoinType;

import java.util.LinkedList;
import java.util.List;

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
public class Join {

    protected JoinType type;

    protected String table;

    protected List<JoinCondition> conditions;

    public Join(JoinType type, String table) {
        this.type = type;
        this.table = table;
    }

    public String getTable() {
        return table;
    }

    public Join setTable(String table) {
        this.table = table;
        return this;
    }

    public List<JoinCondition> getConditions() {
        conditions = conditions == null ? new LinkedList<>() : conditions;
        return conditions;
    }

    public Join setConditions(List<JoinCondition> conditions) {
        this.conditions = conditions;
        return this;
    }

    public Join addCondition(JoinCondition condition) {
        this.getConditions().add(condition);
        return this;
    }
}
