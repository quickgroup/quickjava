package org.quickjava.orm.drive;

import org.quickjava.orm.contain.DriveConfigure;

/*
 * Copyright (c) 2020~2023 http://www.quickjava.org All rights reserved.
 * +-------------------------------------------------------------------
 * Organization: QuickJava
 * +-------------------------------------------------------------------
 * Author: Qlo1062
 * +-------------------------------------------------------------------
 * File: DefaultDrive
 * +-------------------------------------------------------------------
 * Date: 2023/3/12 15:29
 * +-------------------------------------------------------------------
 * License: Apache Licence 2.0
 * +-------------------------------------------------------------------
 */
public class DefaultDrive extends Drive {

    // 默认驱动默认ORACLE风格，mysql、oracle、达梦均支持
    private static final DriveConfigure CONFIGURE = new DriveConfigure(
            "", "",
            "'", "'"
    );

    @Override
    public DriveConfigure getDriveConfigure() {
        return CONFIGURE;
    }

}
