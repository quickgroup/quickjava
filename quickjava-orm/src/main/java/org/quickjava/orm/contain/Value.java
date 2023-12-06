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

    public static final SimpleDateFormat FORMAT_yyyy_MM_dd = new SimpleDateFormat("yyyy-MM-dd");

    public static final SimpleDateFormat FORMAT_yyyy_MM_dd_HH_mm_ss = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static final SimpleDateFormat FORMAT_HH_mm_ss = new SimpleDateFormat("HH:mm:ss");

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
        } else if (value instanceof Date) {
            return FORMAT_yyyy_MM_dd_HH_mm_ss.format((Date) value);
        } else {
            String valueConv = Convert.convert(String.class, value);
            valueConv = SqlUtil.escapeSql(valueConv);
            return String.format("%s%s%s", config.strL, valueConv, config.strR);
        }
    }

}
