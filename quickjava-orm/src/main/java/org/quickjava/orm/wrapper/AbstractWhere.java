package org.quickjava.orm.wrapper;

import org.quickjava.orm.enums.Logic;

/**
 * 基础条件定义
 */
public abstract class AbstractWhere<Children extends AbstractWhere<Children, M, Func>, M, Func extends MFunction<M, ?>> implements WhereWrapper<Children, M, Func> {

    protected Logic logic = Logic.AND;

    @Override
    public Children and() {
        logic = Logic.AND;
        return chain();
    }

    @Override
    public Children or() {
        logic = Logic.OR;
        return chain();
    }

    @Override
    public Logic getLogic() {
        return logic;
    }

}
