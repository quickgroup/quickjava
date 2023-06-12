package org.quickjava.orm.modelQuery;

import org.quickjava.orm.Model;
import org.quickjava.orm.utils.ModelUtil;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

public abstract class ModelQueryWrapper<T, R extends Function<T, ?>> implements Serializable {

    protected T model;

    protected Integer integer = 10;

    protected Model model() {
        return (Model) model;
    }

    public ModelQueryWrapper<T, R> eq(R function, Object val)
    {
        model().eq(findFieldName(function), val);
        return this;
    }

    @Override
    public String toString() {
        return model().toString();
    }

    private String findFieldName(R function) {
        return FunctionReflectionUtil.getFieldName((Function<?, ?>) function);
    }

}
