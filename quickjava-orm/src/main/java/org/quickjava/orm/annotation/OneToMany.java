package org.quickjava.orm.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/*
 * Copyright (c) 2020~2023 http://www.quickjava.org All rights reserved.
 * +-------------------------------------------------------------------
 * Organization: QuickJava
 * +-------------------------------------------------------------------
 * Author: Qlo1062
 * +-------------------------------------------------------------------
 * File: ModelRelation
 * +-------------------------------------------------------------------
 * Date: 2023-3-8 16:15
 * +-------------------------------------------------------------------
 * License: Apache Licence 2.0
 * +-------------------------------------------------------------------
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@RelationAno
public @interface OneToMany {

    String foreignKey() default "";

    /**
     * 关联模型的字段
     * - 支持属性名称、字段名
     * - 默认取关联模型类名加_id（下划线id）
     * */
    String localKey() default "";
}
