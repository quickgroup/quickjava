package org.quickjava.orm;

/*
 * Copyright (c) 2020~2023 http://www.quickjava.org All rights reserved.
 * +-------------------------------------------------------------------
 * Organization: QuickJava
 * +-------------------------------------------------------------------
 * Author: Qlo1062
 * +-------------------------------------------------------------------
 * File: OrmConfigure
 * +-------------------------------------------------------------------
 * Date: 2023-5-24 18:47
 * +-------------------------------------------------------------------
 * License: Apache Licence 2.0
 * +-------------------------------------------------------------------
 */

import cn.hutool.core.util.ReflectUtil;
import org.quickjava.orm.drive.*;
import org.quickjava.orm.utils.QuickConnection;

import java.sql.Connection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * ORM唯一实例
 */
public class ORMContext {

    private static final ThreadLocal<QuickConnection> __quickConnection = new ThreadLocal<>();

    public static Map<QuickConnection.DBType, Class<? extends Drive>> driveMap = new LinkedHashMap<>();

    static {
        driveMap.put(QuickConnection.DBType.MYSQL, Mysql.class);
        driveMap.put(QuickConnection.DBType.ORACLE, Oracle.class);
        driveMap.put(QuickConnection.DBType.UNKNOWN, DefaultDrive.class);
    }

    /**
     * 获取当前环境数据库驱动
     * @return 驱动连接
     */
    public static Drive getDrive()
    {
        synchronized (Drive.class) {
            // 为空时、连接不存在时
            if (__quickConnection.get() == null || __quickConnection.get().connection == null) {
                try {
                    // TODO::使用
                    Connection connection = SpringAutoConfiguration.instance.getDataSource().getConnection();

                    __quickConnection.set(new QuickConnection(connection, 2));

                } catch (Exception e) {
                    try {
                        __quickConnection.set(getConnectionByQuick());
                    } catch (Exception e2) {
                        throw new RuntimeException(e2);
                    }
                }
            }
        }

        // 实例化驱动
        QuickConnection quickConnection = __quickConnection.get();
        if (quickConnection == null) {
            throw new RuntimeException("未连接到数据库");
        }

        // 加载驱动操作类
        try {
            Drive drive = driveMap.get(quickConnection.type).newInstance();
            drive.setQuickConnection(quickConnection);
            return drive;
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * FIXME::从QuickJava读取配置连接数据库
     * @return 新的数据库连接
     * @throws ClassNotFoundException 异常
     * */
    public static QuickConnection getConnectionByQuick()
            throws ClassNotFoundException
    {
        Class<?> kernelClazz = ORMContext.class.getClassLoader().loadClass("org.quickjava.framework.Kernel");
        Object config = ReflectUtil.getFieldValue(kernelClazz, "config");
        Object database = ReflectUtil.invoke(config, "get", "database");
        QuickConnection quickConnection = new QuickConnection(
                ReflectUtil.invoke(database, "getString", "url"),
                ReflectUtil.invoke(database, "getString", "username"),
                ReflectUtil.invoke(database, "getString", "password")
        );
        quickConnection.connectionFormType = 1;
        quickConnection.connectStart();
        return quickConnection;
    }

}
