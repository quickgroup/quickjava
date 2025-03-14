package org.quickjava.orm.docking;

/*
 * Copyright (c) 2020~2025 http://www.quickjava.org All rights reserved.
 * +-------------------------------------------------------------------
 * Organization: QuickJava
 * +-------------------------------------------------------------------
 * Author: Qlo1062
 * +-------------------------------------------------------------------
 * File: ValueConversion
 * +-------------------------------------------------------------------
 * Date: 2025/3/14 18:38
 * +-------------------------------------------------------------------
 * License: Apache Licence 2.0
 * +-------------------------------------------------------------------
 */

import java.sql.JDBCType;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * 数据转换对接
 */
public interface ORMParameterHandlerDocking {

    /**
     *
     */
    public void setParameter(PreparedStatement ps, int i, Object parameter, JDBCType jdbcType) throws SQLException;
    /*
        @Override
    public void setNonNullParameter(PreparedStatement ps, int i, E parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, parameter.name()); // 将枚举值存储为字符串
    }
    * */

}
