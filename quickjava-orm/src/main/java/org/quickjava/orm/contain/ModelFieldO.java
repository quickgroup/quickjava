package org.quickjava.orm.contain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import org.quickjava.orm.annotation.ModelField;
import org.quickjava.orm.annotation.ModelId;
import org.quickjava.orm.enums.ModelFieldFill;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/*
 * Copyright (c) 2020~2023 http://www.quickjava.org All rights reserved.
 * +-------------------------------------------------------------------
 * Organization: QuickJava
 * +-------------------------------------------------------------------
 * Author: Qlo1062
 * +-------------------------------------------------------------------
 * File: RelationInfo
 * +-------------------------------------------------------------------
 * Date: 2023-3-9 15:07
 * +-------------------------------------------------------------------
 * License: Apache Licence 2.0
 * +-------------------------------------------------------------------
 */
public class ModelFieldO {

    private String name;

    // 原始属性名称
    private String _name;

    // 对应类
    private Class<?> clazz;

    private Field field;

    // 关联字段
    private Object way;

    private Method setter;

    private Method getter;

    private Method method;

    private ModelField modelField;

    private ModelId modelId;

    // 兼容mybatis-plus的注解
    private TableId tableId;

    private TableField tableField;

    public ModelFieldO() {
    }

    public ModelFieldO(Field field) {
        this.name = this._name = field.getName();
        this.clazz = field.getType();
        this.field = field;
    }

    public ModelFieldO(Field field, Object way, Method setter, Method getter) {
        this.name = this._name = field.getName();
        this.clazz = field.getType();
        this.field = field;
        this.way = way;
        this.setter = setter;
        this.getter = getter;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public void setClazz(Class<?> clazz) {
        this.clazz = clazz;
    }

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }

    public Object getWay() {
        return way;
    }

    public void setWay(Object way) {
        this.way = way;
    }

    public Method getSetter() {
        return setter;
    }

    public void setSetter(Method setter) {
        this.setter = setter;
    }

    public Method getGetter() {
        return getter;
    }

    public void setGetter(Method getter) {
        this.getter = getter;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public ModelField getModelField() {
        return modelField;
    }

    public void setModelField(ModelField modelField) {
        this.modelField = modelField;
    }

    public String get_name() {
        return _name;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public ModelId getModelId() {
        return modelId;
    }

    public void setModelId(ModelId modelId) {
        this.modelId = modelId;
    }

    public TableId getTableId() {
        return tableId;
    }

    public void setTableId(TableId tableId) {
        this.tableId = tableId;
    }

    public TableField getTableField() {
        return tableField;
    }

    public void setTableField(TableField tableField) {
        this.tableField = tableField;
    }

    public boolean isInsertFill() {
        return getModelField() != null && getModelField().insertFill() != ModelFieldFill.NULL;
    }

    public boolean isUpdateFill() {
        return getModelField() != null && getModelField().updateFill() != ModelFieldFill.NULL;
    }

    public boolean isSoftDelete() {
        return getModelField() != null && getModelField().softDelete();
    }
}
