package org.quickjava.orm.model.contain;/*
 * Copyright (c) 2020~2024 http://www.quickjava.org All rights reserved.
 * +-------------------------------------------------------------------
 * Organization: QuickJava
 * +-------------------------------------------------------------------
 * Author: Qlo1062
 * +-------------------------------------------------------------------
 * File: IdType
 * +-------------------------------------------------------------------
 * Date: 2024/5/8 18:19
 * +-------------------------------------------------------------------
 * License: Apache Licence 2.0
 * +-------------------------------------------------------------------
 */

public enum ModelIdType {
    AUTO(0),
    NONE(1),
    INPUT(2)
    ;

    private final int key;

    private ModelIdType(int key) {
        this.key = key;
    }

    public int getKey() {
        return this.key;
    }
}
