package org.quickjava.orm.model;/*
 * Copyright (c) 2020~2024 http://www.quickjava.org All rights reserved.
 * +-------------------------------------------------------------------
 * Organization: QuickJava
 * +-------------------------------------------------------------------
 * Author: Qlo1062
 * +-------------------------------------------------------------------
 * File: ModelExtend
 * +-------------------------------------------------------------------
 * Date: 2024/5/8 18:14
 * +-------------------------------------------------------------------
 * License: Apache Licence 2.0
 * +-------------------------------------------------------------------
 */

import org.quickjava.orm.model.contain.IdType;
import org.quickjava.orm.model.enums.ModelFieldFill;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 注解支持
 */
public class ModelAnoHelper {

    /**
     * 主键id、属性注解
     */
    public static Map<Class<?>, TableNameInfo> tableNameAnoMap = new LinkedHashMap<>();
    public static Map<Class<?>, TableIdInfo> tableIdAnoMap = new LinkedHashMap<>();
    public static Map<Class<?>, TableFieldInfo> tableFieldAnoMap = new LinkedHashMap<>();

    public static interface TableNameInfo {
        String name(Class<?> ano);
        String schema(Class<?> ano);
        String resultMap(Class<?> ano);
        String[] excludeProperty(Class<?> ano);
    }

    public static interface TableIdInfo {
        String value(Class<?> ano);
        IdType type(Class<?> ano);
    }

    public static interface TableFieldInfo {
        String value(Class<?> ano);
        boolean exist(Class<?> ano);
        String condition(Class<?> ano);
        String update(Class<?> ano);

        ModelFieldFill insertFill(Class<?> ano);
        String insertFillTarget(Class<?> ano);

        ModelFieldFill updateFill(Class<?> ano);
        String updateFillTarget(Class<?> ano);

        boolean softDelete(Class<?> ano);
    }

}
