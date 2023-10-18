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

    public String fieldL = "";

    public String fieldR = "";

    /**
     * 字符串数据左边连接符
     */
    public String strL = "\"";

    /**
     * 字符串数据右边连接符
     */
    public String strR = "\"";

    public DriveConfigure() {
    }

    public DriveConfigure(String fieldL, String fieldR, String strL, String strR) {
        this.fieldL = fieldL;
        this.fieldR = fieldR;
        this.strL = strL;
        this.strR = strR;
    }

    @Override
    public String toString() {
        return "DriveConfigure{" +
                "fieldL='" + fieldL + '\'' +
                ", fieldR='" + fieldR + '\'' +
                ", strL='" + strL + '\'' +
                ", strR='" + strR + '\'' +
                '}';
    }
}
