/*
 * Copyright (c) 2021.
 * More Info to http://www.quickjava.org
 */

package org.quickjava.orm.query.build;

import cn.hutool.core.convert.Convert;
import org.quickjava.common.utils.DatetimeUtil;
import org.quickjava.orm.model.annotation.ModelField;
import org.quickjava.orm.contain.DriveConfigure;
import org.quickjava.orm.utils.SqlUtil;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

public class ValueConv {

    private DriveConfigure driveConfigure;

    private static final Map<DriveConfigure, ValueConv> valueConvCache = new LinkedHashMap<>();

    public ValueConv(DriveConfigure driveConfigure) {
        this.driveConfigure = driveConfigure;
    }

    public static ValueConv getConv(DriveConfigure configure) {
        if (!valueConvCache.containsKey(configure)) {
            valueConvCache.put(configure, new ValueConv(configure));
        }
        return valueConvCache.get(configure);
    }

    public String convWrap(Object value) {
        String valStr = convValue(value);
        if (isValueStringWrap(value)) {
            return valueStringWrap(valStr);
        }
        return valStr;
    }

    public String convValue(Object value) {
        if (value == null) {
            // return "";   // UPDATE SET name=, age=1  有问腿，name应该是NULL
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
            return valStr;
        } else {
            String valueConv = Convert.convert(String.class, value);
            valueConv = SqlUtil.escapeSql(valueConv);
            return valueConv;
        }
    }

    /**
     * 字符串包含数据类型
     */
    public boolean isValueStringWrap(Object value) {
        if (value == null) {
            return false;
        } else if (value instanceof Integer || value instanceof Long) {
            return false;
        } else if (value instanceof Float) {
            return false;
        } else if (value instanceof Double) {
            return false;
        } else if (value instanceof Iterable) {
            return false;
        } else {
            return true;
        }
    }

    public String valueStringWrap(String val) {
        return String.format("%s%s%s", driveConfigure.valueStrLeft, val, driveConfigure.valueStrRight);
    }

    public String convDate(Date value) {
        ModelField modelFieldAno = value.getClass().getAnnotation(ModelField.class);
        if (modelFieldAno != null) {
            return DatetimeUtil.getSimpleDateFormat(modelFieldAno.format()).format(value);
        }
        return DatetimeUtil.getSimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(value);
    }

}
