package org.quickjava.framework.orm.drive;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import org.quickjava.framework.Kernel;
import org.quickjava.framework.bean.Dict;
import org.quickjava.framework.orm.QuerySet;
import org.quickjava.framework.orm.contain.Action;
import org.quickjava.framework.orm.contain.Value;
import org.quickjava.framework.orm.contain.WhereBase;
import org.quickjava.framework.orm.utils.QueryException;
import org.quickjava.framework.orm.utils.QuickConnection;
import org.quickjava.framework.orm.utils.SqlUtil;

import javax.sql.DataSource;
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
 * File: Mysql2
 * +-------------------------------------------------------------------
 * Date: 2023/3/12 15:29
 * +-------------------------------------------------------------------
 * License: Apache Licence 2.0
 * +-------------------------------------------------------------------
 */
public class Mysql implements Drive {

    static ThreadLocal<QuickConnection> __quickConnection = new ThreadLocal<>();

    private String sql = null;

    @Override
    public QuickConnection getConnection() {
        synchronized (Drive.class) {
            if (__quickConnection.get() == null) {
                // FIXME::获取Spring数据库连接
                try {
                    DataSource dataSource = SpringUtil.getBean("dataSource");
                    __quickConnection.set(new QuickConnection(dataSource.getConnection()));
                } catch (Exception e) {
                    // FIXME::从QuickJava读取配置连接数据库
                    try {
                        Dict database = Kernel.config.get("database");
                        QuickConnection quickConnection = new QuickConnection(
                                database.getString("url"),
                                database.getString("username"),
                                database.getString("password")
                        );
                        quickConnection.connectStart();
                        __quickConnection.set(quickConnection);
                    } catch (Exception e2) {
                        throw new RuntimeException(e2);
                    }
                }
            }
        }
        return __quickConnection.get();
    }

    /***
     * 预编译部分
     * @param query 查询器
     * @return 组合sql语句
     */
    @Override
    public String pretreatment(QuerySet query)
    {
        /*
        SQL执行顺序：
        FROM table1 left join table2 on 将table1和table2中的数据产生笛卡尔积，生成Temp1
        JOIN table2 所以先是确定表，再确定关联条件
        ON table1.column = table2.columu 确定表的绑定条件 由Temp1产生中间表Temp2
        WHERE 对中间表Temp2产生的结果进行过滤 产生中间表Temp3
        GROUP BY 对中间表Temp3进行分组，产生中间表Temp4
        HAVING 对分组后的记录进行聚合 产生中间表Temp5
        SELECT 对中间表Temp5进行列筛选，产生中间表 Temp6
        DISTINCT 对中间表 Temp6进行去重，产生中间表 Temp7
        ORDER BY 对Temp7中的数据进行排序，产生中间表Temp8
        LIMIT 对中间表Temp8进行分页，产生中间表Temp9
        **/

        List<String> sqlList = new ArrayList<>();

        // action
        Action action = query.__Action();
        sqlList.add(action.beginSymbol());

        // action is SELECT
        if (action == Action.SELECT) {
            sqlList.add(StrUtil.join(",", query.__FieldList()));
            sqlList.add("FROM");
        }

        // table name
        sqlList.add(query.__Table().toString());

        // TODO::JOIN

        // UPDATE
        if (action == Action.UPDATE) {
            StringBuilder dataSql = new StringBuilder();
            int fi = 0;
            for (Map.Entry<String, Object> entry : query.__Data().entrySet()) {
                if (fi++ > 0)
                    dataSql.append(",");
                String item = String.format("`%s`=%s", entry.getKey(), Value.pretreatment(entry.getValue()));
                dataSql.append(item);
            }
            sqlList.add("SET");
            sqlList.add(dataSql.toString());
        }

        // INSERT-DATA
        if (action == Action.INSERT) {
            StringBuilder dataSql = new StringBuilder();
            // field
            SqlUtil.mapKeyJoin(dataSql, query.__DataList().isEmpty() ? query.__Data() : query.__DataList().get(0));
            // 数据处理方法
            SqlUtil.MapJoinCallback joinCallback = entry -> Value.pretreatment(entry.getValue());
            // values
            dataSql.append(" VALUES ");
            if (query.__DataList().isEmpty()) {
                SqlUtil.mapValueJoin(dataSql, query.__Data(), joinCallback);
            } else {
                for (int i = 0; i < query.__DataList().size(); i++) {
                    if (i > 0)
                        dataSql.append(',');
                    SqlUtil.mapValueJoin(dataSql, query.__DataList().get(i), joinCallback);
                }
            }
            sqlList.add(dataSql.toString());
        }

        // WHERE
        if (query.__WhereList().size() > 0) {
            sqlList.add("WHERE");
            sqlList.add(WhereBase.taskOutFirstLogic(StrUtil.join(" ", query.__WhereList())));
        }

        // ORDER BY
        if (query.__Orders().size() > 0) {
            sqlList.add(String.format("ORDER BY %s", StrUtil.join(",", query.__Orders())));
        }

        // Limit
        if (action == Action.SELECT && query.__Limit() != null) {
            sqlList.add(String.format("LIMIT %d,%d", query.__Limit(), query.__LimitSize()));
        }

        sql = StrUtil.join(" ", sqlList);
        return sql;
    }

    @Override
    public String assemble(Drive drive) {
        return null;
    }

    @Override
    public <T> T executeSql(QuerySet query) {
        return executeSql(query.__Action(), pretreatment(query));
    }

    @Override
    public <T> T executeSql(Action action, String sql)
    {
        long startTime = System.nanoTime();
        Object number = null;

        try {
            // 执行操作
            if (action == Action.INSERT) {
                Map<String, Object> generatedKeys = JdbcDock.insert(getConnection().connection, sql);
                if (generatedKeys == null || generatedKeys.isEmpty()) {
                    return null;
                }
                Object gk = generatedKeys.get("GENERATED_KEY");
                number = gk instanceof Long ? (Long) gk : Long.valueOf(String.valueOf(gk));

            } else if (action == Action.DELETE) {
                number = JdbcDock.delete(getConnection().connection, sql).longValue();

            } else if (action == Action.UPDATE) {
                number = JdbcDock.update(getConnection().connection, sql).longValue();

            } else if (action == Action.SELECT) {
                List<Map<String, Object>> rows = JdbcDock.select(getConnection().connection, sql);
                return (T) rows;
            }
            return (T) number;

        } catch (SQLException sqlExc) {
            sqlExc.printStackTrace();
            throw new QueryException(sqlExc);

        } finally {
            long endTime = System.nanoTime();
            String printSql = sql.length() < 512 ? sql: sql.substring(0, 512);
            String msg = "SQL execution time " + ((double) (endTime - startTime)) / 1000000 + "ms " + printSql;
            System.out.println(msg);
        }
    }

    @Override
    public void setAutoCommit(boolean autoCommit) {
        try {
            getConnection().connection.setAutoCommit(autoCommit);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void startTrans() {
        setAutoCommit(false);
    }

    @Override
    public void commit() {
        try {
            getConnection().connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void rollback() {
        try {
            getConnection().connection.rollback();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
