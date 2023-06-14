/*
 * Copyright (c) 2021.
 * More Info to http://www.quickjava.org
 */

package org.quickjava.orm.contain;

import java.util.HashMap;
import java.util.Map;

/***
 * 支持操作语句类型
 */
public enum Action {
    INSERT,
    DELETE,
    UPDATE,
    SELECT,
    ;

    @Override
    public String toString() {
        return __NAME_MAP.getOrDefault(this.name(), null);
    }

    private static final Map<String, String> __NAME_MAP = new HashMap<>();

    static {
        __NAME_MAP.put(INSERT.name(), "INSERT INTO");
        __NAME_MAP.put(DELETE.name(), "DELETE FROM");
        __NAME_MAP.put(UPDATE.name(), "UPDATE");
        __NAME_MAP.put(SELECT.name(), "SELECT");
    }

}
