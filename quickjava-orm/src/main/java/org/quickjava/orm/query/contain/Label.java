package org.quickjava.orm.query.contain;


/*
 * Copyright (c) 2020~2025 http://www.quickjava.org All rights reserved.
 * +-------------------------------------------------------------------
 * Organization: QuickJava
 * +-------------------------------------------------------------------
 * Author: Qlo1062
 * +-------------------------------------------------------------------
 * File: Label
 * +-------------------------------------------------------------------
 * Date: 2025/3/13 22:32
 * +-------------------------------------------------------------------
 * License: Apache Licence 2.0
 * +-------------------------------------------------------------------
 */
public enum Label {
    INSERT_GET_ID("INSERT_GET_ID", "插入返回自增ID"),
    ;
    private final String code;
    private final String title;

    private Label(final String code, final String title) {
        this.code = code;
        this.title = title;
    }

    public String getCode() {
        return code;
    }

    public String getTitle() {
        return title;
    }
}
