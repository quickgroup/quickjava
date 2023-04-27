package org.quickjava.framework.orm.drive;

import org.quickjava.framework.orm.QuerySet;
import org.quickjava.framework.orm.contain.Action;
import org.quickjava.framework.orm.utils.QuickConnection;

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
public class Oracle implements Drive {

    @Override
    public QuickConnection getConnection() {
        return null;
    }

    @Override
    public String pretreatment(QuerySet query) {
        return null;
    }

    @Override
    public String assemble(Drive drive) {
        return null;
    }

    @Override
    public <T> T executeSql(QuerySet query) {
        return null;
    }

    @Override
    public <T> T executeSql(Action action, String sql) {
        return null;
    }

    @Override
    public void setAutoCommit(boolean autoCommit) {

    }

    @Override
    public void startTrans() {

    }

    @Override
    public void commit() {

    }

    @Override
    public void rollback() {

    }
}
