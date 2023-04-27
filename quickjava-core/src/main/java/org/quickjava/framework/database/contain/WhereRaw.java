/*
 * Copyright (c) 2020~2021 http://www.quickjava.org All rights reserved.
 * +-------------------------------------------------------------------
 * Org: QuickJava
 * +-------------------------------------------------------------------
 * Author: Qlo1062
 * +-------------------------------------------------------------------
 * File: WhereRaw.java
 * +-------------------------------------------------------------------
 * Date: 2021/05/31 18:58:31
 * +-------------------------------------------------------------------
 * License: Apache Licence 2.0
 * +-------------------------------------------------------------------
 *
 */

package org.quickjava.framework.database.contain;

public class WhereRaw implements WhereBase {

    private String fieldString = null;

    public WhereRaw(String fieldString) {
        this.setFieldString(fieldString);
    }

    public String getFieldString() {
        return fieldString;
    }

    public void setFieldString(String fieldString) {
        this.fieldString = fieldString;
    }

    @Override
    public String toString() {
        return fieldString;
    }
}
