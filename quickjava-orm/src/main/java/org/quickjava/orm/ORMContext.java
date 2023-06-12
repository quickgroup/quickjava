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

import org.quickjava.common.utils.ReflectUtil;
import org.quickjava.orm.contain.Config;
import org.quickjava.orm.drive.*;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * ORM唯一实例
 */
public class ORMContext {

    public static Map<Config.DBType, Class<? extends Drive>> driveMap = new LinkedHashMap<>();

    static {
        driveMap.put(Config.DBType.MYSQL, Mysql.class);
        driveMap.put(Config.DBType.ORACLE, Oracle.class);
        driveMap.put(Config.DBType.DEFAULT, DefaultDrive.class);
    }

    public static Drive getDrive() {
        Config config;
        synchronized (Drive.class) {
            // 检测spring
            if (SpringAutoConfiguration.instance != null) {
                config = SpringAutoConfiguration.instance.getConfig();
            } else {
                config = getQuickJavaConfig();
            }
        }
        return getDrive(config);
    }

    /**
     * 获取当前环境数据库驱动
     * @param config 链接配置
     * @return 驱动连接
     */
    public static Drive getDrive(Config config)
    {
        // 加载驱动操作类
        try {
            Drive drive = driveMap.get(config.type).newInstance();
            ReflectUtil.setFieldValueDirect(drive, "config", config);
            return drive;
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * FIXME::从QuickJava读取数据库配置
     * @return 链接配置
     * */
    public static Config getQuickJavaConfig()
    {
        try {
            Class<?> kernelClazz = ORMContext.class.getClassLoader().loadClass("org.quickjava.framework.Kernel");
            Object configMap = ReflectUtil.getFieldValue(kernelClazz, "config");
            Object databaseMap = ReflectUtil.invoke(configMap, "get", "database");
            Config config1 = new Config(
                    Config.DBSubject.QUICKJAVA,
                    ReflectUtil.invoke(databaseMap, "getString", "url"),
                    ReflectUtil.invoke(databaseMap, "getString", "username"),
                    ReflectUtil.invoke(databaseMap, "getString", "password")
            );
            config1.subject = Config.DBSubject.QUICKJAVA;
            return config1;
        } catch (Throwable th) {
            return defaultConfig;
        }
    }

    private static Config defaultConfig = new Config(Config.DBSubject.CONFIG, Config.DBType.DEFAULT);

}
