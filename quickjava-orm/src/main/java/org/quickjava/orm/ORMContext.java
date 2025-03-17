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

import org.quickjava.orm.domain.ORMConfiguration;
import org.quickjava.orm.docking.ORMContextPort;
import org.quickjava.orm.model.callback.ModelStrut;
import org.quickjava.orm.model.callback.ModelListener;
import org.quickjava.orm.domain.DatabaseMeta;
import org.quickjava.orm.drive.*;
import org.quickjava.orm.model.Model;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * ORM唯一实例
 */
public class ORMContext {

    private static ClassLoader classLoader = ORMContext.class.getClassLoader();
//    private static ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

    private static ORMContextPort contextPort = null;

    private static List<ModelListener> modelListenerList = new LinkedList<>();

    private static ORMConfiguration configuration = new ORMConfiguration();

    private static ModelStrut modelStrut = null;

    private static final Map<DatabaseMeta.DBType, Class<? extends Drive>> driveMap = new LinkedHashMap<>();

    static {
        driveMap.put(DatabaseMeta.DBType.MYSQL, Mysql.class);
        driveMap.put(DatabaseMeta.DBType.ORACLE, Oracle.class);
        driveMap.put(DatabaseMeta.DBType.DEFAULT, DefaultDrive.class);
    }

    /**
     * 设置不同环境的类加载器
     */
    public static void setClassLoader(ClassLoader classLoader) {
        ORMContext.classLoader = classLoader;
    }

    public static ClassLoader getClassLoader() {
        return classLoader;
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

    public static void setContextPort(ORMContextPort ormContextPort) {
        ORMContext.contextPort = ormContextPort;
    }

    public static ORMContextPort getContextPort() {
        return ORMContext.contextPort;
    }

    public static ModelStrut getModelStrut() {
        return modelStrut;
    }

    public static void setModelStrut(ModelStrut modelStrut) {
        ORMContext.modelStrut = modelStrut;
    }

    public static DatabaseMeta getDatabaseMeta() {
        return contextPort.getDatabaseMeta();
    }

    /**
     * 获取驱动器类
     */
    public static Drive getDrive() {
        return getDrive(getDatabaseMeta());
    }

    /**
     * 获取驱动器类
     */
    public static Drive getDrive(DatabaseMeta meta)
    {
        // 加载驱动操作类
        try {
            Drive drive = driveMap.get(meta.type).newInstance();
            drive.setConfig(meta);
            return drive;
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static final ModelListener MODEL_CALLBACK = new ModelListener() {
        @Override
        public void insert(Model model) {
            for (ModelListener callback : modelListenerList) {
                callback.insert(model);
            }
        }

        @Override
        public void delete(Model model) {
            for (ModelListener callback : modelListenerList) {
                callback.delete(model);
            }
        }

        @Override
        public void update(Model model) {
            for (ModelListener callback : modelListenerList) {
                callback.update(model);
            }
        }

        @Override
        public void select(Model model) {
            for (ModelListener callback : modelListenerList) {
                callback.select(model);
            }
        }
    };

    //模型回调
    public static void modelListener(ModelListener callback) {
        if (!modelListenerList.contains(callback)) {
            modelListenerList.add(callback);
        }
    }

    public static void modelUnListener(ModelListener callback) {
        modelListenerList.remove(callback);
    }

    public static ORMConfiguration getConfiguration() {
        return configuration;
    }
}
