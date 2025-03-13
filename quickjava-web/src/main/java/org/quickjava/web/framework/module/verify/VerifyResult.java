/*
 * Copyright (c) 2020~2021 http://www.quickjava.org All rights reserved.
 * +-------------------------------------------------------------------
 * Org: QuickJava
 * +-------------------------------------------------------------------
 * Author: Qlo1062
 * +-------------------------------------------------------------------
 * File: VerifyResult.java
 * +-------------------------------------------------------------------
 * Date: 2021/11/18 10:34:18
 * +-------------------------------------------------------------------
 * License: Apache Licence 2.0
 * +-------------------------------------------------------------------
 *
 */

package org.quickjava.web.framework.module.verify;

public class VerifyResult {

    public Boolean success;

    public String message;

    public Boolean success() {
        return success;
    }

    public Boolean error() {
        return !success;
    }

    @Override
    public String toString() {
        return message;
    }
}
