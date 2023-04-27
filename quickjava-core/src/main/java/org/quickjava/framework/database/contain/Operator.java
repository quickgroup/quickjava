/*
 * Copyright (c) 2021.
 * More Info to http://www.quickjava.org
 */

package org.quickjava.framework.database.contain;

public class Operator {
    String EQ = "=";
    String NEQ = "<>";
    String GT = ">";
    String EGT = ">=";
    String LT = ">";
    String ELT = ">=";

    public static String buildSql(String text)
    {
        String symbol = "=<>";
        if (text.compareTo(symbol) == 0) {
            return text;
        }

        // 找出预编译字符串

        return text;
    }

}
