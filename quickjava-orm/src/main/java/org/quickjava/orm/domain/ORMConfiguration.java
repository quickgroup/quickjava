package org.quickjava.orm.domain;/*
 * Copyright (c) 2020~2024 http://www.quickjava.org All rights reserved.
 * +-------------------------------------------------------------------
 * Organization: QuickJava
 * +-------------------------------------------------------------------
 * Author: Qlo1062
 * +-------------------------------------------------------------------
 * File: ORMConfig
 * +-------------------------------------------------------------------
 * Date: 2024/9/14 15:22
 * +-------------------------------------------------------------------
 * License: Apache Licence 2.0
 * +-------------------------------------------------------------------
 */

/**
 * ORM配置
 */
public class ORMConfiguration {

    /**
     * 数据列左补齐字符
     */
    public String columnLeft;
    /**
     * 数据列右补齐字符
     */
    public String columnRight;

    /**
     * 字符串值前后拼接字符串
     */
    public String valueStrLeft;
    public String valueStrRight;

    /**
     * 表前缀
     */
    public String tablePrefix;

    /**
     * 将数据库字段的下划线命名风格
     */
    public Boolean mapUnderscoreToCamelCase = false;

    public String getColumnLeft() {
        return columnLeft;
    }

    public void setColumnLeft(String columnLeft) {
        this.columnLeft = columnLeft;
    }

    public String getColumnRight() {
        return columnRight;
    }

    public void setColumnRight(String columnRight) {
        this.columnRight = columnRight;
    }

    public String getValueStrLeft() {
        return valueStrLeft;
    }

    public void setValueStrLeft(String valueStrLeft) {
        this.valueStrLeft = valueStrLeft;
    }

    public String getValueStrRight() {
        return valueStrRight;
    }

    public void setValueStrRight(String valueStrRight) {
        this.valueStrRight = valueStrRight;
    }

    public String getTablePrefix() {
        return tablePrefix;
    }

    public void setTablePrefix(String tablePrefix) {
        this.tablePrefix = tablePrefix;
    }

    public Boolean getMapUnderscoreToCamelCase() {
        return mapUnderscoreToCamelCase;
    }

    public void setMapUnderscoreToCamelCase(Boolean mapUnderscoreToCamelCase) {
        this.mapUnderscoreToCamelCase = mapUnderscoreToCamelCase;
    }
}
