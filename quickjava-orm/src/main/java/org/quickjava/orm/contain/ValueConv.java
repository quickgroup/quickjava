/*
 * Copyright (c) 2021.
 * More Info to http://www.quickjava.org
 */

package org.quickjava.orm.contain;

import cn.hutool.core.convert.Convert;
import org.quickjava.common.utils.DatetimeUtil;
import org.quickjava.orm.annotation.ModelField;
import org.quickjava.orm.utils.SqlUtil;

import java.util.Date;

public class ValueConv {

    private DriveConfigure configure;

    public ValueConv(DriveConfigure configure) {
        this.configure = configure;
    }

    public String conv(Object value) {
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
            String valStr = convDate((Date) value);
            return valueStringWrap(valStr);
        } else {
            String valueConv = Convert.convert(String.class, value);
            valueConv = SqlUtil.escapeSql(valueConv);
            return valueStringWrap(valueConv);
        }
    }

    public String valueStringWrap(String val) {
        return String.format("%s%s%s", configure.strL, val, configure.strR);
    }

    public String convDate(Date value) {
        ModelField modelFieldAno = value.getClass().getAnnotation(ModelField.class);
        if (modelFieldAno != null) {
            return DatetimeUtil.getSimpleDateFormat(modelFieldAno.format()).format(value);
        }
        return DatetimeUtil.getSimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(value);
    }

}
