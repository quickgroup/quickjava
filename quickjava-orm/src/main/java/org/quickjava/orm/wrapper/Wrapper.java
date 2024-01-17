package org.quickjava.orm.wrapper;

import org.quickjava.orm.query.enums.OrderByType;

public interface Wrapper<Children> {

    /**
     * 返回自己
     */
    default Children chain() {
        return (Children) this;
    }

    /**
     * 声明字段
     * @param tm 目标模型类
     * @param tfs 目标模型字段
     * @return 查询器
     * @param <TM> 目标模型类
     */
    <TM> Children field(Class<TM> tm, MFunction<TM, ?>... tfs);

    /**
     * 指定类方法做排序列名
     */
    <TM> Children order(Class<TM> tm, MFunction<TM, ?> tf, OrderByType type);

    default <Left> Children order(Class<Left> left, MFunction<Left, ?> lf, boolean desc) {
        return order(left, lf, desc ? OrderByType.DESC : OrderByType.ASC);
    }

    default <Left> Children order(Class<Left> left, MFunction<Left, ?> lf) {
        return order(left, lf, OrderByType.ASC);
    }
}
