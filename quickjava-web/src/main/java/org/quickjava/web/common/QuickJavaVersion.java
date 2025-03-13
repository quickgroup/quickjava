package org.quickjava.web.common;/*
 * Copyright (c) 2020~2024 http://www.quickjava.org All rights reserved.
 * +-------------------------------------------------------------------
 * Organization: QuickJava
 * +-------------------------------------------------------------------
 * Author: Qlo1062
 * +-------------------------------------------------------------------
 * File: QuickJavaVersion
 * +-------------------------------------------------------------------
 * Date: 2024/5/21 16:10
 * +-------------------------------------------------------------------
 * License: Apache Licence 2.0
 * +-------------------------------------------------------------------
 */

import org.springframework.core.SpringVersion;
import org.springframework.lang.Nullable;

public class QuickJavaVersion {

    private QuickJavaVersion() {
    }

    @Nullable
    public static String getVersion() {
        Package pkg = SpringVersion.class.getPackage();
        return pkg != null ? pkg.getImplementationVersion() : null;
    }
}
