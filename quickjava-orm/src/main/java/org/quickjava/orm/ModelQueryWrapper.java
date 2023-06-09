package org.quickjava.orm;

import org.quickjava.orm.utils.ModelUtil;

import java.util.function.Function;

public abstract class ModelQueryWrapper<T, R extends Function<?, ?>> {

    protected Model model;

    public ModelQueryWrapper<T, R> eq(R column, Object val)
    {
        this.model.eq(ModelUtil.getFunctionConvFieldName(column), val);
        return this;
    }

}
