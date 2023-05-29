/*
 * Copyright (c) 2020~2021 http://www.quickjava.org All rights reserved.
 * +-------------------------------------------------------------------
 * Org: QuickJava
 * +-------------------------------------------------------------------
 * Author: Qlo1062
 * +-------------------------------------------------------------------
 * File: DBConfig.java
 * +-------------------------------------------------------------------
 * Date: 2021/06/24 17:19:24
 * +-------------------------------------------------------------------
 * License: Apache Licence 2.0
 * +-------------------------------------------------------------------
 *
 */

package org.quickjava.framework.database;

import org.quickjava.common.QuickLog;
import org.quickjava.framework.Kernel;
import org.quickjava.framework.Lang;
import org.quickjava.framework.bean.Dict;

public class DBMan {

    private QuickConnection quickConnection = null;

    private static class DBManClassInstance{
        private static final DBMan instance = new DBMan();
    }

    private DBMan()
    {
        Dict database = Kernel.config().get("database");
        quickConnection = new QuickConnection(database.getString("url"), database.getString("username"), database.getString("password"));
        quickConnection.connectStart();

        QuickLog.info(Lang.to("Database init Complete."));
    }

    public static void init() {

    }

    public static QuickConnection getQuickConnection() {
        return DBManClassInstance.instance.quickConnection;
    }
}
