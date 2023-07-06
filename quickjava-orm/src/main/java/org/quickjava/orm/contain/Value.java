/*
 * Copyright (c) 2021.
 * More Info to http://www.quickjava.org
 */

package org.quickjava.orm.contain;

import cn.hutool.core.convert.Convert;
import org.quickjava.orm.utils.SqlUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Value {

    public static String pretreatment(Object value, DriveConfigure config) {
        if (value == null) {
            return "NULL";
        } else if (value instanceof Integer || value instanceof Long) {
            return String.valueOf(value);
        } else if (value instanceof Float) {
            return Float.toString((Float) value);
        } else if (value instanceof Double) {
            return Double.toString((Double) value);
        } else if (value instanceof Iterable) {
            return SqlUtil.collJoin(",", ((Iterable<?>) value));
        } else {
            String valueConv = Convert.convert(String.class, value);
            valueConv = SqlUtil.escapeSql(valueConv);
            return String.format("%s%s%s", config.strL, valueConv, config.strR);
        }
    }

}
