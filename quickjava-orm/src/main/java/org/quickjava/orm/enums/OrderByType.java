package org.quickjava.orm.enums;

/*
 * Copyright (c) 2020~2024 http://www.quickjava.org All rights reserved.
 * +-------------------------------------------------------------------
 * Organization: QuickJava
 * +-------------------------------------------------------------------
 * Author: Qlo1062
 * +-------------------------------------------------------------------
 * File: OrderByType
 * +-------------------------------------------------------------------
 * Date: 2024/1/7 18:13
 * +-------------------------------------------------------------------
 * License: Apache Licence 2.0
 * +-------------------------------------------------------------------
 */
public enum OrderByType {
    ASC,
    DESC,
    ;

    public static OrderByType getByName(String name) {
        if (name != null) {
            for (OrderByType value : values()) {
                if (value.name().equalsIgnoreCase(name)) {
                    return value;
                }
            }
        }
        return null;
    }

}
