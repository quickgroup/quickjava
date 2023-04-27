package org.quickjava.framework.orm.set;

import org.quickjava.framework.orm.Model;
import org.quickjava.framework.orm.contain.DataMap;

import java.util.LinkedList;

/*
 * Copyright (c) 2020~2023 http://www.quickjava.org All rights reserved.
 * +-------------------------------------------------------------------
 * Organization: QuickJava
 * +-------------------------------------------------------------------
 * Author: Qlo1062
 * +-------------------------------------------------------------------
 * File: ModelList
 * +-------------------------------------------------------------------
 * Date: 2023-4-25 10:27
 * +-------------------------------------------------------------------
 * License: Apache Licence 2.0
 * +-------------------------------------------------------------------
 */
public class ModelList<E extends Model> extends LinkedList<E> {

    /*
    * 查询基础模型（存放一对多关系等
    * */
    private E model;

    protected Model __parent;

    protected LinkedList<DataMap> __data = new LinkedList<>();

    public ModelList<E> where(String field, Object val) {
        return this;
    }

    public ModelList<E> where(String field, String op, Object val) {
        return this;
    }

}
