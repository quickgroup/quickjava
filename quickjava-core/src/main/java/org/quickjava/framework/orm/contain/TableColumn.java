package org.quickjava.framework.orm.contain;

import org.quickjava.framework.orm.utils.SqlUtil;

import java.util.Map;

/*
 * Copyright (c) 2020~2023 http://www.quickjava.org All rights reserved.
 * +-------------------------------------------------------------------
 * Organization: QuickJava
 * +-------------------------------------------------------------------
 * Author: Qlo1062
 * +-------------------------------------------------------------------
 * File: TableField
 * +-------------------------------------------------------------------
 * Date: 2023-3-14 11:15
 * +-------------------------------------------------------------------
 * License: Apache Licence 2.0
 * +-------------------------------------------------------------------
 */
public class TableColumn {
    public String Field;
    public String Type;
    public String Collation;
    public String Null;
    public String Key;
    public String Default;
    public String Extra;
    public String Privileges;   // 特权
    public String Comment;

    public TableColumn() {
    }

    public TableColumn(Map<String, String> info) {
        info.forEach((k, v) -> SqlUtil.setFieldValue(this, k, v));
    }

    @Override
    public String toString() {
        return "TableColumn{" +
                "Field='" + Field + '\'' +
                ", Type='" + Type + '\'' +
                ", Collation='" + Collation + '\'' +
                ", Null='" + Null + '\'' +
                ", Key='" + Key + '\'' +
                ", Default='" + Default + '\'' +
                ", Extra='" + Extra + '\'' +
                ", Privileges='" + Privileges + '\'' +
                ", Comment='" + Comment + '\'' +
                '}';
    }
}
