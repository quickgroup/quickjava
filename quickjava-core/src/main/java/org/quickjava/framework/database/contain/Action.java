/*
 * Copyright (c) 2021.
 * More Info to http://www.quickjava.org
 */

package org.quickjava.framework.database.contain;

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

    public static class ActionString {

        static Map<String, String> nameMap = new HashMap<>();

        static {
            nameMap.put(INSERT.name(), "INSERT INTO");
            nameMap.put(DELETE.name(), "DELETE FROM");
            nameMap.put(UPDATE.name(), "UPDATE");
            nameMap.put(SELECT.name(), "SELECT ");
        }

        public static String str(Action action) {
            String name = action.name();
            return nameMap.containsKey(name) ? nameMap.get(name) : null;
        }
    }

}
