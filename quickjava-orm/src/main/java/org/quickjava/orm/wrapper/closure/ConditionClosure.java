package org.quickjava.orm.wrapper.closure;

/*
 * Copyright (c) 2020~2024 http://www.quickjava.org All rights reserved.
 * +-------------------------------------------------------------------
 * Organization: QuickJava
 * +-------------------------------------------------------------------
 * Author: Qlo1062
 * +-------------------------------------------------------------------
 * File: ConditionClosure.java
 * +-------------------------------------------------------------------
 * Date: 2024/1/7 0:24
 * +-------------------------------------------------------------------
 * License: Apache Licence 2.0
 * +-------------------------------------------------------------------
 */

import org.quickjava.orm.wrapper.conditions.Condition;
import org.quickjava.orm.wrapper.MFunction;

import java.util.Map;
import java.util.function.BiPredicate;

/**
 * 闭包查询条件
 */
public class ConditionClosure<R extends MFunction<ConditionClosure<R>, ?>> implements Condition<ConditionClosure<R>, R> {

    @Override
    public <V> ConditionClosure<R> allEq(boolean condition, Map<R, V> params, boolean null2IsNull) {
        return null;
    }

    @Override
    public <V> ConditionClosure<R> allEq(boolean condition, BiPredicate<R, V> filter, Map<R, V> params, boolean null2IsNull) {
        return null;
    }

    @Override
    public ConditionClosure<R> eq(boolean condition, R column, Object val) {
        return null;
    }

    @Override
    public ConditionClosure<R> ne(boolean condition, R column, Object val) {
        return null;
    }

    @Override
    public ConditionClosure<R> gt(boolean condition, R column, Object val) {
        return null;
    }

    @Override
    public ConditionClosure<R> ge(boolean condition, R column, Object val) {
        return null;
    }

    @Override
    public ConditionClosure<R> lt(boolean condition, R column, Object val) {
        return null;
    }

    @Override
    public ConditionClosure<R> le(boolean condition, R column, Object val) {
        return null;
    }

    @Override
    public ConditionClosure<R> between(boolean condition, R column, Object val1, Object val2) {
        return null;
    }

    @Override
    public ConditionClosure<R> notBetween(boolean condition, R column, Object val1, Object val2) {
        return null;
    }

    @Override
    public ConditionClosure<R> like(boolean condition, R column, Object val) {
        return null;
    }

    @Override
    public ConditionClosure<R> notLike(boolean condition, R column, Object val) {
        return null;
    }

    @Override
    public ConditionClosure<R> likeLeft(boolean condition, R column, Object val) {
        return null;
    }

    @Override
    public ConditionClosure<R> likeRight(boolean condition, R column, Object val) {
        return null;
    }
}
