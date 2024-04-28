package org.quickjava.orm.wrapper;

import org.quickjava.orm.query.enums.Operator;

public interface Wrapper<Children> {

    /**
     * 返回自己
     */
    default Children chain() {
        return (Children) this;
    }

    /**
     * where and查询
     */
    Children where(boolean condition, String table, String column, Operator operator, Object val);

    /**
     * where or查询
     */
    Children whereOr(boolean condition, String table, String column, Operator operator, Object val);

}
