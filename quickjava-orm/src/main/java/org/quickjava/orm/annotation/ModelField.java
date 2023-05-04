package org.quickjava.orm.annotation;

import org.quickjava.orm.enums.ModelFieldFill;

import java.lang.annotation.*;

/*
 * Copyright (c) 2020~2023 http://www.quickjava.org All rights reserved.
 * +-------------------------------------------------------------------
 * Organization: QuickJava
 * +-------------------------------------------------------------------
 * Author: Qlo1062
 * +-------------------------------------------------------------------
 * File: ModelField
 * +-------------------------------------------------------------------
 * Date: 2023-3-8 16:15
 * +-------------------------------------------------------------------
 * License: Apache Licence 2.0
 * +-------------------------------------------------------------------
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
public @interface ModelField {

    /**
     * 字段名称
     * */
    String name() default "";

    /**
     * 字段是否存在
     * */
    boolean exist() default true;

    /**
     * 字段填充处理
     * */
    ModelFieldFill fill() default ModelFieldFill.NONE;

    /**
     * 字段填充处理方法
     * */
//    Constants.FillMethod fillMethod() default Constants.defaultFillMethod;

    /**
     * 软删除字段
     * - 字段类型必须是datetime，且可为NULL，属性类型是Date，查询时将自动追加条件： Field IS NULL
     * */
    boolean softDelete() default false;
}