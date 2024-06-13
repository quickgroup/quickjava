package org.quickjava.orm.query.build;/*
 * Copyright (c) 2020~2024 http://www.quickjava.org All rights reserved.
 * +-------------------------------------------------------------------
 * Organization: QuickJava
 * +-------------------------------------------------------------------
 * Author: Qlo1062
 * +-------------------------------------------------------------------
 * File: Condition
 * +-------------------------------------------------------------------
 * Date: 2024/6/13 10:32
 * +-------------------------------------------------------------------
 * License: Apache Licence 2.0
 * +-------------------------------------------------------------------
 */

import org.quickjava.orm.contain.DriveConfigure;
import org.quickjava.orm.enums.CompareType;
import org.quickjava.orm.enums.LogicType;

public abstract class ConditionAbs<Children extends ConditionAbs<Children>> {

    protected LogicType logic = LogicType.AND;
    protected CompareType compare;
    protected String raw;

    public ConditionAbs() {
    }

    public ConditionAbs(LogicType logic, CompareType compare) {
        this.logic = logic;
        this.compare = compare;
    }

    public ConditionAbs(String raw) {
        this.logic = LogicType.RAW;
        this.raw = raw;
    }

    public ConditionAbs(LogicType logic, String raw) {
        this.logic = logic;
        this.raw = raw;
    }

    public LogicType getLogic() {
        return logic;
    }

    public void setLogic(LogicType logic) {
        this.logic = logic;
    }

    public CompareType getCompare() {
        return compare;
    }

    public void setCompare(CompareType compare) {
        this.compare = compare;
    }

    public String getRaw() {
        return raw;
    }

    public Children setRaw(String raw) {
        this.raw = raw;
        return (Children) this;
    }

    public String toSql(DriveConfigure driveConfigure) {
        if (compare == CompareType.RAW) {
            return getRaw();
        }
        return null;
    }

}
