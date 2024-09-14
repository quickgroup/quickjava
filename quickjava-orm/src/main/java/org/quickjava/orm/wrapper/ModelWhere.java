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

import org.quickjava.orm.query.build.Where;

import java.util.LinkedList;
import java.util.List;

public abstract class ModelWhere<Children extends ModelWhere<Children, M, R>, M, R extends MFunction<M, ?>> extends AbstractWhere<Children, M, R> {

    protected List<Where> wheres;

    @Override
    public Children where(Where where) {
        if (wheres == null) {
            wheres = new LinkedList<>();
        }
        wheres.add(where);
        return chain();
    }

    protected List<Where> getWheres() {
        return wheres;
    }
}
