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

import org.quickjava.orm.loader.SpringLoader;
import org.quickjava.orm.utils.ReflectUtil;
import org.quickjava.orm.model.callback.ModelListener;
import org.quickjava.orm.contain.DatabaseConfig;
import org.quickjava.orm.drive.*;
import org.quickjava.orm.model.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * ORM唯一实例
 */
public class ORMContext {
    protected static final Logger logger = LoggerFactory.getLogger(ORMContext.class);

//    private static ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    private static ClassLoader classLoader = ORMContext.class.getClassLoader();

    public static Map<DatabaseConfig.DBType, Class<? extends Drive>> driveMap = new LinkedHashMap<>();

    static {
        driveMap.put(DatabaseConfig.DBType.MYSQL, Mysql.class);
        driveMap.put(DatabaseConfig.DBType.ORACLE, Oracle.class);
        driveMap.put(DatabaseConfig.DBType.DEFAULT, DefaultDrive.class);
    }

    public static ClassLoader getClassLoader() {
        return classLoader;
    }

    public static void setClassLoader(ClassLoader classLoader) {
        ORMContext.classLoader = classLoader;
    }

    public static<D> Class<D> loadClass(Class<D> clazz) {
        try {
            if (classLoader.equals(clazz.getClassLoader())) {
                return clazz;
            }
            return (Class<D>) getClassLoader().loadClass(clazz.getName());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static Drive getDrive() {
        DatabaseConfig config;
        synchronized (Drive.class) {
            if (SpringLoader.instance != null) {
                config = SpringLoader.instance.getConfig();  // spring方式
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
    public static Drive getDrive(DatabaseConfig config)
    {
        // 加载驱动操作类
        try {
            Drive drive = driveMap.get(config.type).newInstance();
            drive.setConfig(config);
            return drive;
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    // 默认配置
    private static DatabaseConfig defaultConfig = new DatabaseConfig(DatabaseConfig.DBSubject.CONFIG, DatabaseConfig.DBType.DEFAULT);

    /**
     * FIXME::从QuickJava读取数据库配置
     * @return 链接配置
     * */
    public static DatabaseConfig getQuickJavaConfig()
    {
        try {
            Class<?> kernelClazz = ORMContext.class.getClassLoader().loadClass("org.quickjava.framework.Kernel");
            Object configMap = ReflectUtil.getFieldValue(kernelClazz, "config");
            Map<String, Object> databaseMap = ReflectUtil.invoke(configMap, "getDict", "database");
            DatabaseConfig config1 = new DatabaseConfig();
            for (Map.Entry<String, Object> entry : databaseMap.entrySet()) {
                if (ReflectUtil.hasField(config1.getClass(), entry.getKey())) {
                    ReflectUtil.setFieldValue(config1, entry.getKey(), entry.getValue());
                }
            }
            config1.loadUrl(config1.url);
            config1.subject = DatabaseConfig.DBSubject.QUICKJAVA;
            return config1;
        } catch (Throwable th) {
            throw new RuntimeException(th.getMessage(), th);
        }
    }

    private static final List<ModelListener> MODEL_CALLBACKS = new LinkedList<>();

    public static final ModelListener MODEL_CALLBACK = new ModelListener() {
        @Override
        public void insert(Model model) {
            for (ModelListener callback : MODEL_CALLBACKS) {
                callback.insert(model);
            }
        }

        @Override
        public void delete(Model model) {
            for (ModelListener callback : MODEL_CALLBACKS) {
                callback.delete(model);
            }
        }

        @Override
        public void update(Model model) {
            for (ModelListener callback : MODEL_CALLBACKS) {
                callback.update(model);
            }
        }

        @Override
        public void select(Model model) {
            for (ModelListener callback : MODEL_CALLBACKS) {
                callback.select(model);
            }
        }
    };

    //模型回调
    public static void modelListener(ModelListener callback) {
        if (!MODEL_CALLBACKS.contains(callback)) {
            MODEL_CALLBACKS.add(callback);
        }
    }

    public static void modelUnListener(ModelListener callback) {
        MODEL_CALLBACKS.remove(callback);
    }

}
