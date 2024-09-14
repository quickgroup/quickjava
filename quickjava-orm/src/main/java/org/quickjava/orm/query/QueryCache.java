package org.quickjava.orm.query;/*
 * Copyright (c) 2020~2024 http://www.quickjava.org All rights reserved.
 * +-------------------------------------------------------------------
 * Organization: QuickJava
 * +-------------------------------------------------------------------
 * Author: Qlo1062
 * +-------------------------------------------------------------------
 * File: ORMCache
 * +-------------------------------------------------------------------
 * Date: 2024/9/14 18:56
 * +-------------------------------------------------------------------
 * License: Apache Licence 2.0
 * +-------------------------------------------------------------------
 */

import org.quickjava.orm.query.contain.TableColumnMeta;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class QueryCache {

    // 缓存原始表字段
    public static Map<String, List<TableColumnMeta>> tableColumnCache = new LinkedHashMap<>();

    public static List<TableColumnMeta> getTableColumns(String table) {
        return tableColumnCache.get(table);
    }

    public static void setTableColumns(String table, List<TableColumnMeta> columns) {
        tableColumnCache.put(table, columns);
    }

}
