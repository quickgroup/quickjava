package org.quickjava.orm.modelQuery;

import org.quickjava.orm.Model;
import org.quickjava.orm.contain.Operator;
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

    public ModelQueryWrapper<T, R> neq(R function, Object val)
    {
        model().neq(findFieldName(function), val);
        return this;
    }

    public ModelQueryWrapper<T, R> gt(R function, Object val) {
        model().gt(findFieldName(function), val);
        return this;
    }

    public ModelQueryWrapper<T, R> gte(R function, Object val) {
        model().gte(findFieldName(function), val);
        return this;
    }

    public ModelQueryWrapper<T, R> lt(R function, Object val) {
        model().lt(findFieldName(function), val);
        return this;
    }

    public ModelQueryWrapper<T, R> lte(R function, Object val) {
        model().lte(findFieldName(function), val);
        return this;
    }

    public ModelQueryWrapper<T, R> in(R function, Object ...args) {
        model().in(findFieldName(function), args);
        return this;
    }

    public ModelQueryWrapper<T, R> notIn(R function, Object ...args) {
        model().notIn(findFieldName(function), args);
        return this;
    }

    public ModelQueryWrapper<T, R> isNull(R function) {
        model().isNull(findFieldName(function));
        return this;
    }

    public ModelQueryWrapper<T, R> isNotNull(R function) {
        model().isNotNull(findFieldName(function));
        return this;
    }

    public ModelQueryWrapper<T, R> between(R function, Object v1, Object v2) {
        model().between(findFieldName(function), v1, v2);
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
