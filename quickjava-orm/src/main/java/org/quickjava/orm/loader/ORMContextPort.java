package org.quickjava.orm.loader;/*
 * Copyright (c) 2020~2024 http://www.quickjava.org All rights reserved.
 * +-------------------------------------------------------------------
 * Organization: QuickJava
 * +-------------------------------------------------------------------
 * Author: Qlo1062
 * +-------------------------------------------------------------------
 * File: OrmContextConfigure
 * +-------------------------------------------------------------------
 * Date: 2024/5/21 15:40
 * +-------------------------------------------------------------------
 * License: Apache Licence 2.0
 * +-------------------------------------------------------------------
 */

import org.quickjava.orm.contain.DatabaseMeta;

import java.sql.Connection;
import java.sql.SQLException;

public interface ORMContextPort {

    DatabaseMeta getDatabaseMeta();

    Connection getConnection() throws SQLException;

}
