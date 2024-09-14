package org.quickjava.orm.contain;

public interface Chain<Children> {

    /**
     * 返回自己
     */
    default Children chain() {
        return (Children) this;
    }

}
