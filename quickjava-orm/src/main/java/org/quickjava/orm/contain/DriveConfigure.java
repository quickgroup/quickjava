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

    public String fieldBefore = "";

    public String fieldAfter = "";

    public String whereValBefore = "";

    public String whereValAfter = "";

    public DriveConfigure() {
    }

    public DriveConfigure(String fieldBefore, String fieldAfter, String whereValBefore, String whereValAfter) {
        this.fieldBefore = fieldBefore;
        this.fieldAfter = fieldAfter;
        this.whereValBefore = whereValBefore;
        this.whereValAfter = whereValAfter;
    }
}
