package org.quickjava.orm.drive;

import org.quickjava.orm.contain.DriveConfigure;

/*
 * Copyright (c) 2020~2023 http://www.quickjava.org All rights reserved.
 * +-------------------------------------------------------------------
 * Organization: QuickJava
 * +-------------------------------------------------------------------
 * Author: Qlo1062
 * +-------------------------------------------------------------------
 * File: Oracle
 * +-------------------------------------------------------------------
 * Date: 2023/3/12 15:27
 * +-------------------------------------------------------------------
 * License: Apache Licence 2.0
 * +-------------------------------------------------------------------
 */
public class Oracle extends Drive {

    private static final DriveConfigure CONFIGURE = new DriveConfigure(
            "", "",
            "'", "'"
    );

    @Override
    public DriveConfigure getConfigure() {
        return CONFIGURE;
    }

}
