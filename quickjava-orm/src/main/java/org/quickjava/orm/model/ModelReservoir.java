package org.quickjava.orm.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.quickjava.orm.query.QuerySet;
import org.quickjava.orm.contain.DataMap;
import org.quickjava.orm.model.contain.ModelFieldMeta;
import org.quickjava.orm.model.contain.ModelMeta;

import java.util.LinkedList;
import java.util.List;

/*
 * Copyright (c) 2020~2024 http://www.quickjava.org All rights reserved.
 * +-------------------------------------------------------------------
 * Organization: QuickJava
 * +-------------------------------------------------------------------
 * Author: Qlo1062
 * +-------------------------------------------------------------------
 * File: ModelReservoir
 * +-------------------------------------------------------------------
 * Date: 2024/1/6 17:33
 * +-------------------------------------------------------------------
 * License: Apache Licence 2.0
 * +-------------------------------------------------------------------
 */
public class ModelReservoir {

    @JsonIgnore
    private Model model;

    /**
     * 模型元信息
     * */
    @JsonIgnore
    public ModelMeta meta;

    /**
     * 预载入属性
     * - 必须是模型必须有的属性
     * */
    @JsonIgnore
    public List<String> withs;

    /**
     * 数据
     */
    @JsonIgnore
    public final DataMap data = new DataMap();

    /**
     * 修改的字段
     * */
    @JsonIgnore
    public List<ModelFieldMeta> modified;

    /**
     * 查询器
     * */
    @JsonIgnore
    public QuerySet querySet;

    /**
     * 关联的父模型对象
     * */
    @JsonIgnore
    public IModel parent;

    /**
     * 是素模型
     * */
    @JsonIgnore
    public boolean vegetarian = true;

    public ModelReservoir(Model model) {
        Model.initModel(model, ModelHelper.getModelClass(model));
        this.model = model;
    }

    public List<ModelFieldMeta> getModified() {
        this.modified = modified == null ? new LinkedList<>() : modified;
        return modified;
    }

    public List<String> getWiths() {
        this.withs = withs == null ? new LinkedList<>() : withs;
        return withs;
    }
}
