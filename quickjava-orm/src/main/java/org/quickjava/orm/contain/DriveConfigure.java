package org.quickjava.orm.contain;

/*
 * Copyright (c) 2020~2023 http://www.quickjava.org All rights reserved.
 * +-------------------------------------------------------------------
 * Organization: QuickJava
 * +-------------------------------------------------------------------
 * Author: Qlo1062
 * +-------------------------------------------------------------------
 * File: StatementConfig
 * +-------------------------------------------------------------------
 * Date: 2023-5-25 17:52
 * +-------------------------------------------------------------------
 * License: Apache Licence 2.0
 * +-------------------------------------------------------------------
 */
public class DriveConfigure {

    /**
     * 字段前后拼接字符串，mysql是`
     */
    public String columnLeft = null;
    public String columnRight = null;

    /**
     * 字符串值前后拼接字符串
     */
    public String valueStrLeft = "\"";
    public String valueStrRight = "\"";

    public DriveConfigure(String columnLeft, String columnRight, String valueStrLeft, String valueStrRight) {
        this.columnLeft = columnLeft;
        this.columnRight = columnRight;
        this.valueStrLeft = valueStrLeft;
        this.valueStrRight = valueStrRight;
    }
}
