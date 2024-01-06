package org.quickjava.orm;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.quickjava.orm.contain.DataMap;
import org.quickjava.orm.contain.ModelFieldMeta;
import org.quickjava.orm.contain.ModelMeta;

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
    public Model parent;

    /**
     * 是素模型
     * */
    @JsonIgnore
    public boolean vegetarian = true;

}
