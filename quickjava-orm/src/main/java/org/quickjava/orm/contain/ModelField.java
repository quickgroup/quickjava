package org.quickjava.orm.contain;

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
public class ModelField {

    private String name;

    // 对应类
    private Class<?> clazz;

    private Field field;

    // 关联字段
    private Object way;

    private Method setter;

    private Method getter;

    private Method method;

    private String insertFill = "";

    private String updateFill = "";

    private boolean softDelete = false;

    public ModelField() {
    }

    public ModelField(Field field) {
        this.name = field.getName();
        this.clazz = field.getType();
        this.field = field;
    }

    public ModelField(Field field, Object way, Method setter, Method getter) {
        this.name = field.getName();
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

    public String getInsertFill() {
        return insertFill;
    }

    public void setInsertFill(String insertFill) {
        this.insertFill = insertFill;
    }

    public String getUpdateFill() {
        return updateFill;
    }

    public void setUpdateFill(String updateFill) {
        this.updateFill = updateFill;
    }

    public boolean isSoftDelete() {
        return softDelete;
    }

    public void setSoftDelete(boolean softDelete) {
        this.softDelete = softDelete;
    }
}
