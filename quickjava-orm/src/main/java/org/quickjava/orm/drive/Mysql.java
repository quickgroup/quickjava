package org.quickjava.orm.drive;

import cn.hutool.core.util.ReflectUtil;
import org.apache.ibatis.session.SqlSession;
import org.quickjava.orm.QuerySet;
import org.quickjava.orm.contain.Action;
import org.quickjava.orm.contain.StatementConfig;
import org.quickjava.orm.contain.Value;
import org.quickjava.orm.contain.WhereBase;
import org.quickjava.orm.utils.QueryException;
import org.quickjava.orm.utils.QuickConnection;
import org.quickjava.orm.utils.SqlUtil;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/*
 * Copyright (c) 2020~2023 http://www.quickjava.org All rights reserved.
 * +-------------------------------------------------------------------
 * Organization: QuickJava
 * +-------------------------------------------------------------------
 * Author: Qlo1062
 * +-------------------------------------------------------------------
 * File: Mysql
 * +-------------------------------------------------------------------
 * Date: 2023/3/12 15:29
 * +-------------------------------------------------------------------
 * License: Apache Licence 2.0
 * +-------------------------------------------------------------------
 */
public class Mysql extends Drive {

    private static final StatementConfig statementConfig = new StatementConfig(
            "`", "`",
            "\"", "\""
    );

    @Override
    public StatementConfig getStatementConfig() {
        return statementConfig;
    }
}
