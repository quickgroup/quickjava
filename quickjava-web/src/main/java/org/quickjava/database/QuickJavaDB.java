package org.quickjava.database;/*
 * Copyright (c) 2020~2024 http://www.quickjava.org All rights reserved.
 * +-------------------------------------------------------------------
 * Organization: QuickJava
 * +-------------------------------------------------------------------
 * Author: Qlo1062
 * +-------------------------------------------------------------------
 * File: QuickJavaDB
 * +-------------------------------------------------------------------
 * Date: 2024/5/21 15:53
 * +-------------------------------------------------------------------
 * License: Apache Licence 2.0
 * +-------------------------------------------------------------------
 */

import org.quickjava.common.QuickJavaVersion;
import org.quickjava.orm.ORMContext;
import org.quickjava.orm.domain.DatabaseMeta;
import org.quickjava.orm.loader.ORMContextPort;
import org.quickjava.orm.utils.QueryException;
import org.quickjava.orm.utils.ReflectUtil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class QuickJavaDB implements ORMContextPort {

    private static String subjectName = "QuickJava " + QuickJavaVersion.getVersion();

    // 默认配置
    private static DatabaseMeta defaultConfig = new DatabaseMeta(subjectName, DatabaseMeta.DBType.DEFAULT);

    @Override
    public DatabaseMeta getDatabaseMeta() {
        try {
            Class<?> kernelClazz = ORMContext.class.getClassLoader().loadClass("org.quickjava.framework.Kernel");
            Object configMap = ReflectUtil.getFieldValue(kernelClazz, "config");
            Object databaseMap = ReflectUtil.invoke(configMap, "get", "database");
            DatabaseMeta config1 = new DatabaseMeta(
                    subjectName,
                    String.valueOf(ReflectUtil.invoke(databaseMap, "getString", "url"))
            );
            return config1;
        } catch (Throwable th) {
            return defaultConfig;
        }
    }

    @Override
    public Connection getConnection() throws SQLException {
        DatabaseMeta config = getDatabaseMeta();
        try {
            // 加载驱动
            Class.forName(config.driver);
            // 数据库配置
            Class<?> kernelClazz = ORMContext.class.getClassLoader().loadClass("org.quickjava.framework.Kernel");
            Object configMap = ReflectUtil.getFieldValue(kernelClazz, "config");
            Object databaseMap = ReflectUtil.invoke(configMap, "get", "database");
            // 获取数据库密码
            String username = ReflectUtil.invoke(databaseMap, "getString", "username");
            String password = ReflectUtil.invoke(databaseMap, "getString", "password");
            // 去连接
            return DriverManager.getConnection(config.url, username, password);
        }  catch (ClassNotFoundException e) {
            throw new QueryException(e.toString());
        }
    }

}
