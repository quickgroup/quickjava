package org.quickjava.orm.wrapper;

import org.quickjava.orm.query.enums.OrderByType;

public interface Wrapper<Children> {

    /**
     * 返回自己
     */
    default Children chain() {
        return (Children) this;
    }

}
