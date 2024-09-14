package org.quickjava.orm.wrapper.join;

/*
 * Copyright (c) 2020~2024 http://www.quickjava.org All rights reserved.
 * +-------------------------------------------------------------------
 * Organization: QuickJava
 * +-------------------------------------------------------------------
 * Author: Qlo1062
 * +-------------------------------------------------------------------
 * File: JoinConditionClosureLeft
 * +-------------------------------------------------------------------
 * Date: 2024/1/7 0:42
 * +-------------------------------------------------------------------
 * License: Apache Licence 2.0
 * +-------------------------------------------------------------------
 */

/**
 * LEFT JOIN 闭包条件
 * @param <Left>
 * @param <Right>
 */
public interface JoinOnClosure<Left, Right> {
    void call(JoinOn<Left, Left, Right> query);
}
