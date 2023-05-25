package org.quickjava.orm.drive;

import org.quickjava.orm.QuerySet;
import org.quickjava.orm.contain.Action;
import org.quickjava.orm.contain.StatementConfig;
import org.quickjava.orm.utils.QuickConnection;

/*
 * Copyright (c) 2020~2023 http://www.quickjava.org All rights reserved.
 * +-------------------------------------------------------------------
 * Organization: QuickJava
 * +-------------------------------------------------------------------
 * Author: Qlo1062
 * +-------------------------------------------------------------------
 * File: Oracle
 * +-------------------------------------------------------------------
 * Date: 2023/3/12 15:27
 * +-------------------------------------------------------------------
 * License: Apache Licence 2.0
 * +-------------------------------------------------------------------
 */
public class Oracle extends Drive {

    private static final StatementConfig statementConfig = new StatementConfig(
            "", "",
            "'", "'"
    );

    @Override
    public StatementConfig getStatementConfig() {
        return statementConfig;
    }

}
