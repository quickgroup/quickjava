/*
 * Copyright (c) 2021.
 * More Info to http://www.quickjava.org
 */

package org.quickjava.orm.drive;

import org.quickjava.orm.QuerySet;
import org.quickjava.orm.contain.Action;
import org.quickjava.orm.utils.QuickConnection;

/**
 * @author Qlo1062-(QloPC-zs)
 * &#064;date  2021/1/25 15:54
 */
public interface Drive {

    static QuickConnection __quickConnection = null;

    QuickConnection getConnection();

    String pretreatment(QuerySet query);

    String assemble(Drive drive);

    <T> T executeSql(QuerySet query);

    <T> T executeSql(Action action, String sql);

    default void setAutoCommit(boolean autoCommit) {
        getConnection().setAutoCommit(autoCommit);
    }

    default void startTrans() {
        setAutoCommit(false);
    }

    default void commit() {
        getConnection().commit();
    }

    default void rollback() {
        getConnection().rollback();
    }
}
