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

import org.quickjava.orm.query.QueryReservoir;
import org.quickjava.orm.query.contain.SqlResult;

import java.util.List;

public interface ORMSqlExecutor {

    SqlResult insert(QueryReservoir reservoir);

    SqlResult delete(QueryReservoir reservoir);

    SqlResult update(QueryReservoir reservoir);

    <T> List<T> select(QueryReservoir reservoir, Class<T> resultType);

}
