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

    public String whereValL = "";

    public String whereValR = "";

    public DriveConfigure() {
    }

    public DriveConfigure(String fieldL, String fieldR, String whereValL, String whereValR) {
        this.fieldL = fieldL;
        this.fieldR = fieldR;
        this.whereValL = whereValL;
        this.whereValR = whereValR;
    }
}
