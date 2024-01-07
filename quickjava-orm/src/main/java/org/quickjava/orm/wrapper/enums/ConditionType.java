package org.quickjava.orm.wrapper.enums;

/*
 * Copyright (c) 2020~2023 http://www.quickjava.org All rights reserved.
 * +-------------------------------------------------------------------
 * Organization: QuickJava
 * +-------------------------------------------------------------------
 * Author: Qlo1062
 * +-------------------------------------------------------------------
 * File: ConditionType
 * +-------------------------------------------------------------------
 * Date: 2023/12/29 17:28
 * +-------------------------------------------------------------------
 * License: Apache Licence 2.0
 * +-------------------------------------------------------------------
 */
// Where.OPMap
public enum ConditionType {
    /** 等于 */
    EQ("="),
    /** 不等于 */
    NEQ("!="),
    /** 小于 */
    LT("<"),
    /** 小于等于 */
    LTE("<="),
    /** 大于 */
    GT(">"),
    /** 大于等于 */
    GTE(">="),
    /** LIKE */
    LIKE("LIKE"),
    /** IN */
    IN("IN"),
    NOT_IN("NOT IN"),
    IS_NULL("IS NULL"),
    IS_NOT_NULL("IS NOT NULL"),
    BETWEEN("BETWEEN"),
    RAW(null),
    ;

    private final String sql;

    ConditionType(String sql) {
        this.sql = sql;
    }

    public String sql() {
        return sql;
    }
}
