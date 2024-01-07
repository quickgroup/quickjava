package org.quickjava.orm.wrapper;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import org.quickjava.orm.model.IModel;
import org.quickjava.orm.model.Model;
import org.quickjava.orm.model.ModelUtil;
import org.quickjava.orm.model.contain.ModelFieldMeta;
import org.quickjava.orm.model.contain.ModelMeta;
import org.quickjava.orm.query.QuerySet;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;

/*
 * Copyright (c) 2020~2024 http://www.quickjava.org All rights reserved.
 * +-------------------------------------------------------------------
 * Organization: QuickJava
 * +-------------------------------------------------------------------
 * Author: Qlo1062
 * +-------------------------------------------------------------------
 * File: WrapperUtil
 * +-------------------------------------------------------------------
 * Date: 2024/1/7 17:38
 * +-------------------------------------------------------------------
 * License: Apache Licence 2.0
 * +-------------------------------------------------------------------
 */
public class WrapperUtil {

    public static Model getWrapperModel(Wrapper<?> wrapper) {
        return (Model) ReflectUtil.getFieldValue(wrapper, "model");
    }

    public static QuerySet getQuerySet(IModel model) {
        return ReflectUtil.invoke(model, "query");
    }

    public static QuerySet getQuerySet(Wrapper<?> wrapper) {
        return ReflectUtil.invoke(getWrapperModel(wrapper), "query");
    }

    public static ModelMeta getModelMeta(Class<?> clazz) {
        if (Model.class.isAssignableFrom(clazz)) {
            if (!ModelUtil.metaExist(clazz)) {
                try {
                    clazz = ModelUtil.getModelClass(clazz);
                    clazz.newInstance();
                } catch (InstantiationException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return ModelUtil.getMeta(clazz);
    }

    public static Map<Class<?>, ModelFieldMeta> getModelClazzFieldMap(ModelMeta meta) {
        Map<Class<?>, ModelFieldMeta> fieldMetaClazzMap = new LinkedHashMap<>();
        for (Field field : meta.getClazz().getDeclaredFields()) {
            if (!fieldMetaClazzMap.containsKey(field.getType())) {
                fieldMetaClazzMap.put(field.getType(), new ModelFieldMeta(field));
            }
        }
        return fieldMetaClazzMap;
    }

    public static <Left> String autoTable(String table, Wrapper<?> wrapper, Class<Left> left) {
        return autoTable(table, getWrapperModel(wrapper).getClass(), left);
    }

    public static <Left> String autoTable(String table, Class<?> parentClazz, Class<Left> left) {
        // 如果指定在父实体上的属性
        // 在主表上的属性名
        if (table == null) {
            ModelFieldMeta fieldMeta = WrapperUtil.getModelClazzFieldMap(WrapperUtil.getModelMeta(parentClazz)).get(left);
            table = fieldMeta == null ? null : fieldMeta.getName();
        }
        // 表名
        if (table == null) {
            ModelMeta leftMeta = WrapperUtil.getModelMeta(left);
            table = leftMeta == null ? null : leftMeta.table();
        }
        return table;
    }

}
