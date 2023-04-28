package org.quickjava.framework.orm.contain;

import java.util.LinkedHashMap;
import java.util.Map;

/*
 * Copyright (c) 2020~2023 http://www.quickjava.org All rights reserved.
 * +-------------------------------------------------------------------
 * Organization: QuickJava
 * +-------------------------------------------------------------------
 * Author: Qlo1062
 * +-------------------------------------------------------------------
 * File: ModelInfo
 * +-------------------------------------------------------------------
 * Date: 2023/4/27 23:19
 * +-------------------------------------------------------------------
 * License: Apache Licence 2.0
 * +-------------------------------------------------------------------
 */
public class ModelMeta {

    // 模型类
    private Class<?> clazz;

    // 表名
    private String table;

    // 属性字段
    private Map<String, ModelField> fieldMap;

    // 全部关联关系
    private Map<String, Relation> relationMap = new LinkedHashMap<>();

    public ModelMeta() {
    }

    public ModelMeta(Class<?> clazz, String table, Map<String, ModelField> fieldMap, Map<String, Relation> relationMap) {
        this.clazz = clazz;
        this.table = table;
        this.fieldMap = fieldMap;
        this.relationMap = relationMap;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public void setClazz(Class<?> clazz) {
        this.clazz = clazz;
    }

    public String table() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public Map<String, ModelField> getFieldMap() {
        return fieldMap;
    }

    public void setFieldMap(Map<String, ModelField> fieldMap) {
        this.fieldMap = fieldMap;
    }

    public Map<String, Relation> relationMap() {
        return relationMap;
    }

    public void setRelationMap(Map<String, Relation> relationMap) {
        this.relationMap = relationMap;
    }
}
