package org.quickjava.orm.annotation;

import com.baomidou.mybatisplus.annotation.IdType;

import java.lang.annotation.*;

/*
 * Copyright (c) 2020~2023 http://www.quickjava.org All rights reserved.
 * +-------------------------------------------------------------------
 * Organization: QuickJava
 * +-------------------------------------------------------------------
 * Author: Qlo1062
 * +-------------------------------------------------------------------
 * File: ModelId
 * +-------------------------------------------------------------------
 * Date: 2023-3-8 16:15
 * +-------------------------------------------------------------------
 * License: Apache Licence 2.0
 * +-------------------------------------------------------------------
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
public @interface ModelId {

    /**
     * 字段名称
     * @return 字段名称
     * */
    String value() default "";

    /**
     * 主键类型
     * @return 主键类型
     * */
    IdType type() default IdType.NONE;
}
