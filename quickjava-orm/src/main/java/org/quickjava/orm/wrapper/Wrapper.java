package org.quickjava.orm.wrapper;

public interface Wrapper<Children> {

    /**
     * 返回自己
     */
    default Children chain() {
        return (Children) this;
    }

}
