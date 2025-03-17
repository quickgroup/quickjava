package org.quickjava.orm.docking;

/*
 * Copyright (c) 2020~2025 http://www.quickjava.org All rights reserved.
 * +-------------------------------------------------------------------
 * Organization: QuickJava
 * +-------------------------------------------------------------------
 * Author: Qlo1062
 * +-------------------------------------------------------------------
 * File: ORMParameterHandler
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
public interface ORMParameterHandler {

    /**
     * 是否支持参数
     */
    public boolean checkParameter(PreparedStatement ps, int i, Object parameter, JDBCType jdbcType);

    /**
     * 设置参数
     */
    public void setParameter(PreparedStatement ps, int i, Object parameter, JDBCType jdbcType) throws SQLException;

}
