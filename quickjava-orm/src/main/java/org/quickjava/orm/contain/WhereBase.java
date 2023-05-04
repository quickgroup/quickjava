/*
 * Copyright (c) 2021.
 * More Info to http://www.quickjava.org
 */

package org.quickjava.orm.contain;

import cn.hutool.core.collection.CollectionUtil;
import org.quickjava.orm.utils.SqlUtil;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public abstract class WhereBase {

    private int logic = 1;

    private String field = null;

    private String operator = null;

    private Object value = null;

    private List<WhereBase> children = null;

    public WhereBase(int logic, String field, String operator, Object value) {
        this.logic = logic;
        this.setField(field);
        this.setOperator(operator);
        this.setValue(value);
    }

    public WhereBase(WhereBase where) {
        if (children == null)
            children = new LinkedList<>();
        children.add(where);
    }

    public int getLogic() {
        return logic;
    }

    public String getLogicStr() {
        // 如果字段带有logic就返回空字符串
        String fieldClear = this.field.toUpperCase().trim();
        if (fieldClear.startsWith("AND") || fieldClear.startsWith("OR")) {
            return "";
        }
        return logic == 2 ? "OR" : "AND";
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public static Map<String, String> OpMap = new LinkedHashMap<>();

    static {
        OpMap.put("EQ", "=");
        OpMap.put("NEQ", "!=");
        OpMap.put("GT", ">");
        OpMap.put("GTE", ">=");
        OpMap.put("EGT", ">=");
        OpMap.put("LT", "<");
        OpMap.put("LTE", "<=");
        OpMap.put("ELT", "<=");
        // 扩展
        OpMap.put("LIKE", "LIKE");
        OpMap.put("IN", "IN");
        OpMap.put("NOT_IN", "NOT IN");
        OpMap.put("NOT IN", "NOT IN");
        OpMap.put("IS_NULL", "IS NULL");
        OpMap.put("IS NULL", "IS NULL");
        OpMap.put("IS_NOT_NULL", "IS NOT NULL");
        OpMap.put("IS NOT NULL", "IS NOT NULL");
    }

    public String getOperator() {
        return OpMap.getOrDefault(operator, "=");
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public Object getValue() {
        Object result;
        if (value == null) {
            result = "NULL";
        } else if (value instanceof Integer || value instanceof Long) {
            result = String.valueOf(value);
        } else if (value instanceof Float) {
            result = Float.toString((Float) value);
        } else if (value instanceof Double) {
            result = Double.toString((Double) value);
        } else if (value instanceof Iterable) {
            result = CollectionUtil.join(((Iterable<?>) value), ",");
        } else {
            result = String.format("\"%s\"", SqlUtil.escapeSql(String.valueOf(value)));
        }
        return result;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    @Override
    public String toString() {
        // 嵌套查询
        if (children != null) {
            return taskOutFirstLogic(SqlUtil.strJoin(" ", children));
        }
        // 输出
        if ("RAW".equals(operator)) {
            return field;
        } else if ("IN".equals(operator) || "NOT_IN".equals(operator)) {
            return getLogicStr() + " " + getField() + " " + getOperator() + " (" + getValue() + ")";
        }
        return getLogicStr() + " " + getField() + " " + getOperator() + " " + getValue();
    }

    public static String taskOutFirstLogic(String whereSql) {
        if (whereSql.startsWith("AND ")) {
            return whereSql.substring(4);
        } else if (whereSql.startsWith("OR ")) {
            return whereSql.substring(3);
        }
        return whereSql;
    }
}
