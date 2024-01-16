package org.quickjava.orm.wrapper;

import org.quickjava.orm.model.Model;

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
    <TM extends Model> Children field(Class<TM> tm, MFunction<TM, ?> ... tfs);
}
