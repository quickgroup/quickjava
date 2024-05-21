package org.quickjava.orm.model.annotation;

import org.quickjava.orm.model.enums.ModelFieldFill;

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
    String value() default "";

    /**
     * @return 字段是否存在
     * */
    boolean exist() default true;

    /**
     * 插入时填充<br>
     * - ModelFieldFill.DATETIME=填充当前时间<br>
     * - ModelFieldFill.METHOD=调用方法填充数据<br>
     * @return 填充数据
     */
    ModelFieldFill insertFill() default ModelFieldFill.NULL;

    /**
     * 插入时填充补充内容<br>
     * - ModelFieldFill.DATETIME=填充当前时间<br>
     * - ModelFieldFill.METHOD=调用方法填充数据<br>
     * @return 填充数据
     */
    String insertFillTarget() default "";

    /**
     * 更新时填充
     * 与 {@link ModelField#insertFill()}类似
     * @return 填充数据
     */
    ModelFieldFill updateFill() default ModelFieldFill.NULL;

    /**
     * 更新时填充
     * 与 {@link ModelField#insertFill()}类似
     * @return 填充数据
     */
    String updateFillTarget() default "";

    /**
     * 数据写入格式
     * - Date时生效
     * @return 写入格式
     */
    String format() default "";

    /**
     * 软删除字段
     * - 支持类属性是Date，字段类型是datetime，且可为NULL，查询时将自动追加条件，如：`delete_time` IS NULL
     * @return 软删除字段
     * */
    boolean softDelete() default false;
}
