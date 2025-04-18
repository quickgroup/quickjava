package org.quickjava.web.database;/*
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

import org.quickjava.orm.docking.ORMSqlExecutor;
import org.quickjava.web.common.QuickJavaVersion;
import org.quickjava.orm.ORMContext;
import org.quickjava.orm.domain.DatabaseMeta;
import org.quickjava.orm.docking.ORMContextPort;
import org.quickjava.orm.model.Model;
import org.quickjava.orm.utils.QueryException;
import org.quickjava.orm.utils.ReflectUtil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;

public class QuickJavaDB implements ORMContextPort {

    private static String subjectName = "QuickJava " + QuickJavaVersion.getVersion();

    // 默认配置
    private static DatabaseMeta defaultConfig = new DatabaseMeta(subjectName, DatabaseMeta.DBType.DEFAULT);

    public void init() {
        // appClassLoader
        ORMContext.setClassLoader(Model.class.getClassLoader());
        // 配置数据库来源
        ORMContext.setContextPort(this);
        // 加载模型
//        this.loadModels();
        // 更换classLoader
        ORMContext.setClassLoader(this.getClass().getClassLoader());
    }

//    @Override
//    public DatabaseMeta getDatabaseMeta() {
//        try {
//            Class<?> kernelClazz = ORMContext.class.getClassLoader().loadClass("org.quickjava.web.framework.Kernel");
//            Object configMap = ReflectUtil.getFieldValue(kernelClazz, "config");
//            Map<String, Object> databaseMap = ReflectUtil.invoke(configMap, "getDict", "database");
//            DatabaseMeta config1 = new DatabaseMeta();
//            for (Map.Entry<String, Object> entry : databaseMap.entrySet()) {
//                if (ReflectUtil.hasField(config1.getClass(), entry.getKey())) {
//                    ReflectUtil.setFieldValue(config1, entry.getKey(), entry.getValue());
//                }
//            }
//            config1.subject = subjectName;
//            return config1;
//        } catch (Throwable th) {
//            return defaultConfig;
//        }
//    }

//    @Override
//    public Connection getConnection() throws SQLException {
//        try {
//            DatabaseMeta config = getDatabaseMeta();
//            // 加载驱动
//            Class.forName(config.driver);
//            // 用户名和密码
//            Class<?> kernelClazz = ORMContext.class.getClassLoader().loadClass("org.quickjava.web.framework.Kernel");
//            Object configMap = ReflectUtil.getFieldValue(kernelClazz, "config");
//            Map<String, Object> databaseMap = ReflectUtil.invoke(configMap, "getDict", "database");
//            String username = ReflectUtil.invoke(databaseMap, "getString", "username");
//            String password = ReflectUtil.invoke(databaseMap, "getString", "password");
//            // 去连接
//            return DriverManager.getConnection(config.url, username, password);
//        }  catch (ClassNotFoundException e) {
//            throw new QueryException(e.toString());
//        }
//    }

    @Override
    public ORMSqlExecutor getORMSqlExecutor() {
        return null;
    }
}
