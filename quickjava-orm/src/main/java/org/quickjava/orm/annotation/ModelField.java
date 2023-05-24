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
     * @return 字段名称
     * */
    String name() default "";

    /**
     * @return 字段是否存在
     * */
    boolean exist() default true;

    /**
     * 字段填充处理
     * @return 数据填充处理
     * */
    ModelFieldFill fill() default ModelFieldFill.NONE;

    /**
     * 字段填充方法
     * */
//    Constants.FillMethod fillMethod() default Constants.defaultFillMethod;

    /**
     * 软删除字段
     * - 支持类属性是Date，字段类型是datetime，且可为NULL，查询时将自动追加条件，如：`delete_time` IS NULL
     * @return 软删除字段
     * */
    boolean softDelete() default false;
}
