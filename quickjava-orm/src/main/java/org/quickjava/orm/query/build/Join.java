package org.quickjava.orm.query.build;

import org.quickjava.orm.domain.DriveConfigure;
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

    protected List<Where> conditions;

    private DriveConfigure driveConfigure;

    public Join(JoinType type, String table, List<Where> conditions) {
        this.type = type;
        this.table = table;
        this.conditions = conditions;
    }

    public Join(JoinType type, String table, Where condition) {
        this(type, table, new LinkedList<>());
        this.conditions.add(condition);
    }

    public String getTable() {
        return table;
    }

    public Join setTable(String table) {
        this.table = table;
        return this;
    }

    public List<Where> getConditions() {
        conditions = conditions == null ? new LinkedList<>() : conditions;
        return conditions;
    }

    public Join setConditions(List<Where> conditions) {
        this.conditions = conditions;
        return this;
    }

    public String toSql(DriveConfigure driveConfigure) {
        this.driveConfigure = driveConfigure;

        StringBuilder sb = new StringBuilder();
        for (Where condition : getConditions()) {
            sb.append(condition.toSql(driveConfigure)).append(" ");
        }
        // 去掉最后一个空格
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        String conditionsStr = Where.cutFirstLogic(sb.toString());
        return String.format("%s JOIN %s ON %s", type.name(), table, conditionsStr);
    }
}
