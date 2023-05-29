package org.quickjava.orm.drive;

import org.quickjava.orm.contain.DriveConfigure;

/*
 * Copyright (c) 2020~2023 http://www.quickjava.org All rights reserved.
 * +-------------------------------------------------------------------
 * Organization: QuickJava
 * +-------------------------------------------------------------------
 * Author: Qlo1062
 * +-------------------------------------------------------------------
 * File: Mysql
 * +-------------------------------------------------------------------
 * Date: 2023/3/12 15:29
 * +-------------------------------------------------------------------
 * License: Apache Licence 2.0
 * +-------------------------------------------------------------------
 */
public class Mysql extends Drive {

    private static final DriveConfigure CONFIGURE = new DriveConfigure(
            "`", "`",
            "\"", "\""
    );

    @Override
    public DriveConfigure getConfigure() {
        return CONFIGURE;
    }
}
