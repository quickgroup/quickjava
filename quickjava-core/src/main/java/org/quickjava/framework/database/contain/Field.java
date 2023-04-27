/*
 * Copyright (c) 2021.
 * More Info to http://www.quickjava.org
 */

package org.quickjava.framework.database.contain;

public class Field {
    private String table;
    private String field;

    public Field(String table, String field) {
        this.table = table;
        this.field = field;
    }

    public Field(String field) {
        this.table = null;
        this.field = field;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    @Override
    public String toString() {

        // mysql版本差异，不加`
        if ("*".equals(field)) {
            if (table == null) {
                return String.format("*");
            }
            return String.format("`%s`.*", table);
        }

        if (table == null) {
            return String.format("`%s`", field);
        }
        return String.format("`%s`.`%s`", table, field);
    }
}
