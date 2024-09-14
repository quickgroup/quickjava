package org.quickjava.orm.enums;

/**
 * 语句逻辑
 */
public enum Logic {
    /** 与 */
    AND("AND"),
    /** 或 */
    OR("OR"),
    /** 原生语句 */
    RAW(null),
    ;

    private final String sql;

    Logic(String sql) {
        this.sql = sql;
    }

    public String sql() {
        return sql;
    }
}
