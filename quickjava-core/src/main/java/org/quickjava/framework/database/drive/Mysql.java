/*
 * Copyright (c) 2021.
 * More Info to http://www.quickjava.org
 */

package org.quickjava.framework.database.drive;

import org.quickjava.common.QLog;
import org.quickjava.framework.bean.DbConfig;
import org.quickjava.framework.database.Query;

import java.util.List;
import java.util.Map;

/**
 * @author Qlo1062-(QloPC-zs)
 * @date 2021/1/25 15:06
 */
public class Mysql implements Drive {

    @Override
    public Query connect(DbConfig config) {
        return null;
    }

    @Override
    public String buildSql(Query query)
    {
        StringBuffer sqlBuffer = new StringBuffer();

        // where
        return null;
    }

    @Override
    public List<Map> executeSql(String sql)
    {
        QLog.debug("SQL: " + sql);
        return null;
    }
}
