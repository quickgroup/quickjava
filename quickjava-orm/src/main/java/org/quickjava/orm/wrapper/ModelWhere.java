package org.quickjava.orm.wrapper;/*
 * Copyright (c) 2020~2024 http://www.quickjava.org All rights reserved.
 * +-------------------------------------------------------------------
 * Organization: QuickJava
 * +-------------------------------------------------------------------
 * Author: Qlo1062
 * +-------------------------------------------------------------------
 * File: ModelWhere
 * +-------------------------------------------------------------------
 * Date: 2024/9/13 18:08
 * +-------------------------------------------------------------------
 * License: Apache Licence 2.0
 * +-------------------------------------------------------------------
 */

import org.quickjava.orm.enums.Logic;
import org.quickjava.orm.query.build.Where;

import java.util.LinkedList;
import java.util.List;

public abstract class ModelWhere<Children extends ModelWhere<Children, M, R>, M, R extends MFunction<M, ?>>
        implements AbstractWhere<Children, M, R> {

    protected List<Where> wheres;
    protected Logic logic = Logic.AND;

    @Override
    public Children where(Where where) {
        if (wheres == null) {
            wheres = new LinkedList<>();
        }
        wheres.add(where);
        return chain();
    }

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
