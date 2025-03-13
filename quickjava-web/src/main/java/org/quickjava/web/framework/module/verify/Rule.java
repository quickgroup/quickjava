/*
 * Copyright (c) 2020~2021 http://www.quickjava.org All rights reserved.
 * +-------------------------------------------------------------------
 * Org: QuickJava
 * +-------------------------------------------------------------------
 * Author: Qlo1062
 * +-------------------------------------------------------------------
 * File: Rule.java
 * +-------------------------------------------------------------------
 * Date: 2021/11/18 10:26:18
 * +-------------------------------------------------------------------
 * License: Apache Licence 2.0
 * +-------------------------------------------------------------------
 *
 */

package org.quickjava.web.framework.module.verify;

public class Rule {

    // =isEmpty
    public Boolean empty(Object val) {
        return false;
    }

    // =isBlank
    public Boolean blank(Object val) {
        return val == null;
    }

    public Boolean email() {
        return true;
    }
}
