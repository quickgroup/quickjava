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

package org.quickjava.orm.utils;


public class ConnectionManager {

    private QuickConnection quickConnection = null;

    private static class DBManClassInstance{
        private static final ConnectionManager instance = new ConnectionManager();
    }

    private ConnectionManager()
    {
//        Dict database = Kernel.config.get("database");
//        quickConnection = new QuickConnection(
//                database.getString("url"), database.getString("username"), database.getString("password"));
//        quickConnection.connectStart();
//
//        QuickLog.info(Lang.to("Database init Complete."));
    }

    public static void init() {

    }

    public static QuickConnection getConnection() {
        return DBManClassInstance.instance.quickConnection;
    }
}
