/*
 * Copyright (c) 2021.
 * More Info to http://www.quickjava.org
 */

package org.quickjava.framework.database.contain;

public class Where implements WhereBase {

    private String field = null;

    private String operator = null;

    private Object value = null;

    public Where(String field, String operator, Object value) {
        this.setField(field);
        this.setOperator(operator);
        this.setValue(value);
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public Object getValue() {
        Object result = null;
        if (value == null) {
            result = "NULL";
        } else if (value instanceof String) {
            result = String.format("\"%s\"", value);
        } else if (value instanceof Integer) {
            result = String.valueOf(value);
        } else {
            result = String.valueOf(value);
        }

        // 对象，复合where

        return result;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.format("%s %s %s", getField(), getOperator(), getValue());
    }
}
