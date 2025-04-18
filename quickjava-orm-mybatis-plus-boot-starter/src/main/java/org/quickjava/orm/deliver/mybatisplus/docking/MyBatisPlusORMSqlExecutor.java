package org.quickjava.orm.deliver.mybatisplus.docking;

/*
 * Copyright (c) 2020~2025 http://www.quickjava.org All rights reserved.
 * +-------------------------------------------------------------------
 * Organization: QuickJava
 * +-------------------------------------------------------------------
 * Author: Qlo1062
 * +-------------------------------------------------------------------
 * File: MyBatisPlusSqlExecutor
 * +-------------------------------------------------------------------
 * Date: 2025/3/17 10:27
 * +-------------------------------------------------------------------
 * License: Apache Licence 2.0
 * +-------------------------------------------------------------------
 */

import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.scripting.defaults.RawSqlSource;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.quickjava.orm.docking.ORMSqlExecutor;

import java.util.*;

public class MyBatisPlusORMSqlExecutor implements ORMSqlExecutor {

    private final SqlSession sqlSession;

    public MyBatisPlusORMSqlExecutor(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }

    @Override
    public <T> List<T> select(String sql, Map<String, Object> params, Class<T> resultType) {
        Configuration config = sqlSession.getConfiguration();
        SqlSource sqlSource = new RawSqlSource(config, sql, Map.class);

        String msId = "dynamic." + UUID.randomUUID();

        List<ResultMap> resultMaps = Collections.singletonList(
                new ResultMap.Builder(config, msId + ".resultMap", resultType, new LinkedList<>()).build()
        );

        MappedStatement ms = new MappedStatement.Builder(config, msId, sqlSource, SqlCommandType.SELECT)
                .resultMaps(resultMaps).build();

        config.addMappedStatement(ms);

        return sqlSession.selectList(msId, params);
    }

}
