package org.quickjava.orm.enums;

/**
 * 语句逻辑
 */
public enum Logic {
    /** 与 */
    AND("AND", 1),
    /** 或 */
    OR("OR", 2),
    /** 原生语句 */
    RAW(null, 3),
    ;

    private final String sql;
    private final int num;

    Logic(String sql, int num) {
        this.sql = sql;
        this.num = num;
    }

    public String sql() {
        return sql;
    }

    public int num() {
        return num;
    }
}
