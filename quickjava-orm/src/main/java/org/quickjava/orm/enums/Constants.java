package org.quickjava.orm.enums;

/*
 * Copyright (c) 2020~2023 http://www.quickjava.org All rights reserved.
 * +-------------------------------------------------------------------
 * Organization: QuickJava
 * +-------------------------------------------------------------------
 * Author: Qlo1062
 * +-------------------------------------------------------------------
 * File: Constants
 * +-------------------------------------------------------------------
 * Date: 2023-3-14 17:22
 * +-------------------------------------------------------------------
 * License: Apache Licence 2.0
 * +-------------------------------------------------------------------
 */
public class Constants {

    public final static FillMethod defaultFillMethod = value -> null;

    public interface FillMethod {
        Object call(Object value);
    }

}
