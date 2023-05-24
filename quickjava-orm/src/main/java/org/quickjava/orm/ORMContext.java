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

import org.quickjava.orm.drive.Drive;

import java.util.Map;

/**
 * ORM唯一实例
 */
public class ORMContext {

    /**
     * 获取当前环境数据库驱动
     * @return 驱动连接
     */
    public Drive getDrive() {
        return null;
    }

    /**
     * 获取指定配置的数据库驱动
     * @return 驱动连接
     */
    public Drive getDrive(Map<String, String> config) {
        return null;
    }

}
