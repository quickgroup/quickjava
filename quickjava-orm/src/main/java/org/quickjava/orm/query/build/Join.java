package org.quickjava.orm.query.build;

import org.quickjava.orm.contain.DriveConfigure;
import org.quickjava.orm.enums.JoinType;
import org.quickjava.orm.utils.SqlUtil;

import java.util.LinkedList;
import java.util.List;

/*
 * Copyright (c) 2020~2024 http://www.quickjava.org All rights reserved.
 * +-------------------------------------------------------------------
 * Organization: QuickJava
 * +-------------------------------------------------------------------
 * Author: Qlo1062
 * +-------------------------------------------------------------------
 * File: Join
 * +-------------------------------------------------------------------
 * Date: 2024/1/7 21:14
 * +-------------------------------------------------------------------
 * License: Apache Licence 2.0
 * +-------------------------------------------------------------------
 */
public class Join {

    protected JoinType type;

    protected String table;

    protected List<JoinConditionAbs> conditions;

    private DriveConfigure driveConfigure;

    public Join(JoinType type, String table) {
        this.type = type;
        this.table = table;
    }

    public Join(JoinType type, String table, List<JoinConditionAbs> conditions) {
        this.type = type;
        this.table = table;
        this.conditions = conditions;
    }

    public Join(JoinType type, String table, JoinConditionAbs condition) {
        this.type = type;
        this.table = table;
        this.conditions = new LinkedList<>();
        this.conditions.add(condition);
    }

    public String getTable() {
        return table;
    }

    public Join setTable(String table) {
        this.table = table;
        return this;
    }

    public List<JoinConditionAbs> getConditions() {
        conditions = conditions == null ? new LinkedList<>() : conditions;
        return conditions;
    }

    public Join setConditions(List<JoinConditionAbs> conditions) {
        this.conditions = conditions;
        return this;
    }

    public Join addCondition(JoinConditionAbs condition) {
        this.getConditions().add(condition);
        return this;
    }

    public String toSql(DriveConfigure driveConfigure) {
        this.driveConfigure = driveConfigure;
        List<String> conditionsStrList = new LinkedList<>();
        getConditions().forEach(it -> conditionsStrList.add(it.toSql(driveConfigure)));
        String conditionsStr = SqlUtil.strJoin(" AND ", conditionsStrList);
        return String.format("%s JOIN %s ON %s", type.name(), table, conditionsStr);
    }
}
