package org.quickjava.orm.contain;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
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
    private Map<String, ModelFieldMeta> fieldMap;

    // 全部关联关系
    private Map<String, Relation> relationMap = new LinkedHashMap<>();

    public ModelMeta() {
    }

    public ModelMeta(Class<?> clazz, String table, Map<String, ModelFieldMeta> fieldMap, Map<String, Relation> relationMap) {
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

    /**
     * 表别名
     */
    public String tableAlias(String alias) {
        return table + "." + alias;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public Map<String, ModelFieldMeta> fieldMap() {
        return fieldMap;
    }

    public void setFieldMap(Map<String, ModelFieldMeta> fieldMap) {
        this.fieldMap = fieldMap;
    }

    public List<ModelFieldMeta> getFieldList() {
        List<ModelFieldMeta> ret = new LinkedList<>();
        fieldMap().forEach((k, v) -> ret.add(v));
        return ret;
    }

    public Map<String, Relation> relationMap() {
        return relationMap;
    }

    public void setRelationMap(Map<String, Relation> relationMap) {
        this.relationMap = relationMap;
    }

    public String getPkName() {
        for (ModelFieldMeta field : fieldMap.values()) {
            if (field.getModelId() != null) {
                return field.getName();
            } else if (field.getTableId() != null) {
                return field.getName();
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "ModelMeta{" +
                "clazz=" + clazz +
                ", table='" + table + '\'' +
                ", fieldMap=" + fieldMap +
                ", relationMap=" + relationMap +
                '}';
    }
}
