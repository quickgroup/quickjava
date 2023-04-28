package org.quickjava.orm.utils;

/*
 * Copyright (c) 2020~2023 http://www.quickjava.org All rights reserved.
 * +-------------------------------------------------------------------
 * Organization: QuickJava
 * +-------------------------------------------------------------------
 * Author: Qlo1062
 * +-------------------------------------------------------------------
 * File: QueryException
 * +-------------------------------------------------------------------
 * Date: 2023-2-20 15:46
 * +-------------------------------------------------------------------
 * License: Apache Licence 2.0
 * +-------------------------------------------------------------------
 */
public class QueryException extends RuntimeException {

    public QueryException(Exception message) {
        super(message);
    }

    public QueryException(String message) {
        super(message);
    }
}
