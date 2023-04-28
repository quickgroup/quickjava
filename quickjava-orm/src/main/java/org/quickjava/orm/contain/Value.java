/*
 * Copyright (c) 2021.
 * More Info to http://www.quickjava.org
 */

package org.quickjava.orm.contain;

import org.quickjava.orm.utils.SqlUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Value {

    public static String pretreatment(Object value) {
        String ret;
        if (value == null) {
            ret = "NULL";
        } else if (value instanceof Integer) {
            ret = String.valueOf(value);
        } else if (value instanceof Date) {
            ret = datetime((Date) value);
        } else {
            ret = String.format("\"%s\"", SqlUtil.escapeSql(String.valueOf(value)));
        }
        return ret;
    }

    public static String datetime(Date date)
    {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return date == null ? "" : format.format(date);
    }
}
