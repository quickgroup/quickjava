/*
 * Copyright (c) 2020~2021 http://www.quickjava.org All rights reserved.
 * +-------------------------------------------------------------------
 * Org: QuickJava
 * +-------------------------------------------------------------------
 * Author: Qlo1062
 * +-------------------------------------------------------------------
 * File: QuickSqlException.java
 * +-------------------------------------------------------------------
 * Date: 2021/01/20 10:21:20
 * +-------------------------------------------------------------------
 * License: Apache Licence 2.0
 * +-------------------------------------------------------------------
 *
 */

package org.quickjava.framework.exception;

/**
 * @author Qlo1062-(QloPC-Msi)
 * @date 2021/1/17 20:26
 * @ProjectName quickjava
 */
public class QuickSqlException extends QuickException {

    public QuickSqlException(String msg) {
        super(msg);
    }

    public QuickSqlException(Throwable throwable) {
        super(throwable);
    }
}
