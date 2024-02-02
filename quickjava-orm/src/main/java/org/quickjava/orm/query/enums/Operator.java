package org.quickjava.orm.query.enums;

public enum Operator {
    /**
     * 等于
     */
    EQ,
    /**
     * 不等于
     */
    NEQ,
    /**
     * 大于
     */
    GT,
    /**
     * 大于等于
     */
    GTE,
    EGT,
    /**
     * 小于
     */
    LT,
    /**
     * 小于等于
     */
    LTE,
    ELT,
    LIKE,
    LIKE_LEFT,
    LIKE_RIGHT,
    LIKE_LR,
    NOT_LIKE,
    NOT_LIKE_LR,
    IN,
    NOT_IN,
    IS_NULL,
    IS_NOT_NULL,
    BETWEEN,
    NOT_BETWEEN,
    RAW,
    ;
}
