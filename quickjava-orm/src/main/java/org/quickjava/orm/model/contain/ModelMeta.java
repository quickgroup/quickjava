package org.quickjava.orm.model.contain;

import org.quickjava.orm.ORMContext;
import org.quickjava.orm.model.Model;
import org.quickjava.orm.model.annotation.ModelName;
import org.quickjava.orm.utils.SqlUtil;

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
 * File: ModelMeta
 * +-------------------------------------------------------------------
 * Date: 2023/4/27 23:19
 * +-------------------------------------------------------------------
 * License: Apache Licence 2.0
 * +-------------------------------------------------------------------
 */
public class ModelMeta {

    /**
     * 模型类
     * */
    private Class<Model> clazz;

    // 表名
    private String table;
    private String tableCache;

    /**
     * 属性字段
     * */
    private Map<String, ModelFieldMeta> fieldMap;

    // 全部关联关系
    private Map<String, Relation> relationMap = new LinkedHashMap<>();

    public ModelMeta() {
    }

    public ModelMeta(Class<Model> clazz, String table, Map<String, ModelFieldMeta> fieldMap, Map<String, Relation> relationMap) {
        this.clazz = clazz;
        this.table = table;
        this.fieldMap = fieldMap;
        this.relationMap = relationMap;
    }

    public Class<? extends Model> getClazz() {
        return clazz;
    }

    public void setClazz(Class<Model> clazz) {
        this.clazz = clazz;
    }

    public String table() {
        if (tableCache == null) {
            // 模型注解
            ModelName modelName = clazz.getAnnotation(ModelName.class);
            if (modelName != null) {
                tableCache = modelName.value();
            }
            // 三方支持
            if (tableCache == null && ORMContext.getModelStrut() != null) {
                tableCache = ORMContext.getModelStrut().getTableName(this);
            }
            // 默认类名
            if (tableCache == null) {
                tableCache = table;
                if (ORMContext.getDatabaseMeta().isUnderline()) {
                    tableCache = SqlUtil.toUnderlineCase(tableCache);
                }
            }
        }
        return tableCache;
    }

    /**
     * 表别名
     * @param alias 表别名
     * @return 表名加别名的声明sql
     */
    public String tableAlias(String alias) {
        String tableName = this.table();
        if (alias == null || alias.isEmpty()) {
            return tableName;
        }
        return tableName.equals(alias) ? tableName : tableName + " " + alias;
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
            }
            // 三方支持
            if (ORMContext.getModelStrut() != null) {
                return ORMContext.getModelStrut().tableIdName(field);
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "ModelMeta{" +
                "clazz=" + clazz +
                ", table='" + table + '\'' +
                ", tableCache='" + tableCache + '\'' +
                ", fieldMap=" + fieldMap +
                ", relationMap=" + relationMap +
                '}';
    }
}
