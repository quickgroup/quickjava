package org.quickjava.orm.wrapper.join;

import org.quickjava.orm.model.Model;

/*
 * Copyright (c) 2020~2024 http://www.quickjava.org All rights reserved.
 * +-------------------------------------------------------------------
 * Organization: QuickJava
 * +-------------------------------------------------------------------
 * Author: Qlo1062
 * +-------------------------------------------------------------------
 * File: JoinConditionClosure
 * +-------------------------------------------------------------------
 * Date: 2024/1/7 0:42
 * +-------------------------------------------------------------------
 * License: Apache Licence 2.0
 * +-------------------------------------------------------------------
 */
public interface JoinSpecifyClosure<Left extends Model, Right extends Model> {
    void call(JoinSpecify<Left, Right> condition);
}
