package org.quickjava.orm.docking;

/*
 * Copyright (c) 2020~2025 http://www.quickjava.org All rights reserved.
 * +-------------------------------------------------------------------
 * Organization: QuickJava
 * +-------------------------------------------------------------------
 * Author: Qlo1062
 * +-------------------------------------------------------------------
 * File: SqlExecutor
 * +-------------------------------------------------------------------
 * Date: 2025/3/17 10:26
 * +-------------------------------------------------------------------
 * License: Apache Licence 2.0
 * +-------------------------------------------------------------------
 */

import java.util.List;
import java.util.Map;

public interface SqlExecutor {

    <T> List<T> query(String sql, Map<String, Object> params, Class<T> resultType);

}
