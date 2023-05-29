/*
 * Copyright (c) 2021.
 * More Info to http://www.quickjava.org
 */

package org.quickjava.framework.database.contain;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Value {

    public static String parse(Object value) {
        if (value == null) {
            return "NULL";
        } else if (value instanceof Integer) {
            return String.valueOf(value);
        } else if (value instanceof Date) {
            value = datetime((Date) value);
        }

        // 数据转义

        return String.format("\"%s\"", value);
    }

    public static String datetime(Date date)
    {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return date == null ? "" : format.format(date);
    }
}
