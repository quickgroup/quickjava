package org.quickjava.orm.loader;/*
 * Copyright (c) 2020~2024 http://www.quickjava.org All rights reserved.
 * +-------------------------------------------------------------------
 * Organization: QuickJava
 * +-------------------------------------------------------------------
 * Author: Qlo1062
 * +-------------------------------------------------------------------
 * File: MyBatisPlusHelper
 * +-------------------------------------------------------------------
 * Date: 2024/11/25 15:34
 * +-------------------------------------------------------------------
 * License: Apache Licence 2.0
 * +-------------------------------------------------------------------
 */

import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;

public class MyBatisPlusHelper {

    public static String getLogicNotDeletedValue(Class<?> clazz) {
        TableInfo tableInfo = TableInfoHelper.getTableInfo(clazz);
        if (tableInfo != null && tableInfo.getLogicDeleteFieldInfo() != null) {
            return tableInfo.getLogicDeleteFieldInfo().getLogicNotDeleteValue();
        }
        return null;
    }

    public static String getLogicDeletedValue(Class<?> clazz) {
        TableInfo tableInfo = TableInfoHelper.getTableInfo(clazz);
        if (tableInfo != null && tableInfo.getLogicDeleteFieldInfo() != null) {
            return tableInfo.getLogicDeleteFieldInfo().getLogicDeleteValue();
        }
        return null;
    }

}
