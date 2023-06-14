/*
 * Copyright (c) 2021.
 * More Info to http://www.quickjava.org
 */

package org.quickjava.orm.drive;

import org.quickjava.orm.QuerySet;
import org.quickjava.orm.contain.*;
import org.quickjava.orm.utils.QueryException;
import org.quickjava.orm.utils.QuerySetHelper;
import org.quickjava.orm.utils.QuickORMException;
import org.quickjava.orm.utils.SqlUtil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Qlo1062-(QloPC-zs)
 * &#064;date  2021/1/25 15:54
 */
public abstract class Drive {

    private Config config = null;

    // 开启事务后将连接放到线程缓存中
    private static final ThreadLocal<QuickConnection> __THREAD_CONNECTION = new ThreadLocal<>();

    private static final DriveConfigure DRIVE_CONFIGURE_DEF = new DriveConfigure(
            "", "",
            "'", "'"
    );

    public DriveConfigure getDriveConfigure() {
        return DRIVE_CONFIGURE_DEF;
    }

    /**
     * 获取连接
     * @return 连接
     */
    public QuickConnection getQuickConnection() {
        // 事务中、线程中的数据库连接
        if (__THREAD_CONNECTION.get() != null) {
            return __THREAD_CONNECTION.get();
        }
        // 连接数据库
        return new QuickConnection(config).connect();
    }

    /**
     * 语句编译
     * @param query 查询器
     * @return 准备的语句
     */
    public String pretreatment(QuerySet query)
    {
        /*
         * SQL关键字顺序
            以下是 SQL 查询中常见的关键字的一种典型顺序：
            SELECT：选择要检索的列或表达式。
            FROM：指定要查询的表名或视图。
            JOIN：用于连接多个表的关键字。根据连接类型，可能还有 LEFT JOIN、RIGHT JOIN、INNER JOIN 等。
            WHERE：用于筛选要返回的行，根据指定的条件。
            GROUP BY：用于对结果进行分组，通常与聚合函数一起使用。
            HAVING：用于在 GROUP BY 之后对结果进行筛选，根据指定的条件。
            ORDER BY：用于对结果进行排序，可以指定一个或多个列，并选择升序（ASC）或降序（DESC）。
            LIMIT：用于限制结果集的行数，指定要返回的行数。
            OFFSET（可选）：用于跳过结果集中的前几行，通常与 LIMIT 一起使用。
         */

        List<String> sqlList = new ArrayList<>();
        DriveConfigure config = getDriveConfigure();

        // action
        Action action = QuerySetHelper.__Action(query);
        sqlList.add(action.toString());

        // SELECT
        if (action == Action.SELECT) {
            // DISTINCT
            if (isTrue(QuerySetHelper.__distinct(query))) {
                sqlList.add("DISTINCT");
            }
            // field
            checkNull(QuerySetHelper.__FieldList(query), "Missing field");
            sqlList.add(SqlUtil.collJoin(",", QuerySetHelper.__FieldList(query)));
            sqlList.add("FROM");
        }

        // table name
        sqlList.add(QuerySetHelper.__Table(query));

        // TODO::JOIN
        if (QuerySetHelper.__JoinList(query) != null) {
            QuerySetHelper.__JoinList(query).forEach(arr -> {
                sqlList.add(String.format("%s JOIN %s ON %s", arr[2], arr[0], arr[1]));
            });
        }

        List<Map<String, Object>> dataList = QuerySetHelper.__DataList(query);
        // INSERT-DATA
        if (action == Action.INSERT) {
            checkNull(dataList, "Missing insert data");
            StringBuilder dataSql = new StringBuilder();
            // field
            SqlUtil.mapKeyJoin(dataSql, dataList.get(0));
            // values
            dataSql.append(" VALUES ");
            // 数据处理方法
            SqlUtil.MapJoinCallback joinCallback = entry -> Value.pretreatment(entry.getValue());
            for (int i = 0; i < dataList.size(); i++) {
                if (i > 0)
                    dataSql.append(',');
                SqlUtil.mapBracketsJoin(dataSql, dataList.get(i), joinCallback);
            }
            sqlList.add(dataSql.toString());
        }

        // UPDATE
        if (action == Action.UPDATE) {
            checkNull(dataList, "Missing update data");
            StringBuilder dataSql = new StringBuilder();
            int fi = 0;
            for (Map.Entry<String, Object> entry : dataList.get(0).entrySet()) {
                if (fi++ > 0)
                    dataSql.append(",");
                String item = String.format("%s%s%s=%s", config.fieldL, entry.getKey(), config.fieldR,
                        Value.pretreatment(entry.getValue())
                );
                dataSql.append(item);
            }
            sqlList.add("SET");
            sqlList.add(dataSql.toString());
        }

        // WHERE
        if (QuerySetHelper.__WhereList(query) != null) {
            sqlList.add("WHERE");
            sqlList.add(WhereBase.cutFirstLogic(WhereBase.collectSql(QuerySetHelper.__WhereList(query), config)));
        }

        // GROUP BY
        if (QuerySetHelper.__GroupBy(query) != null) {
            sqlList.add(QuerySetHelper.__GroupBy(query));
        }

        // HAVING
        if (QuerySetHelper.__Having(query) != null) {
            sqlList.add(QuerySetHelper.__Having(query));
        }

        // ORDER BY
        if (QuerySetHelper.__Orders(query) != null) {
            sqlList.add(String.format("ORDER BY %s", SqlUtil.collJoin(",", QuerySetHelper.__Orders(query))));
        }

        // Limit
        if (action == Action.SELECT && QuerySetHelper.__Limit(query) != null) {
            sqlList.add(String.format("LIMIT %d,%d", QuerySetHelper.__Limit(query), QuerySetHelper.__LimitSize(query)));
        }

        // lock
        if (isTrue(QuerySetHelper.__lock(query))) {
            sqlList.add("FOR UPDATE");
        }

        return SqlUtil.collJoin(" ", sqlList);
    }

    public <T> T executeSql(QuerySet query) {
        return executeSql(QuerySetHelper.__Action(query), pretreatment(query));
    }

    /**
     * 执行原生sql语句
     * @param action 语句类型
     * @param sql 语句
     * @return 数据
     * @param <T> 数据类型
     */
    public <T> T executeSql(Action action, String sql)
    {
        long startTime = System.nanoTime();
        Object number = null;
        QuickConnection quickConnection = getQuickConnection();

        try {
            // 执行操作
            if (action == Action.INSERT) {
                Map<String, Object> generatedKeys = quickConnection.insert(sql);
                if (generatedKeys == null || generatedKeys.isEmpty()) {
                    return null;
                }
                Object gk = generatedKeys.get("GENERATED_KEY");
                number = gk instanceof Long ? (Long) gk : Long.valueOf(String.valueOf(gk));

            } else if (action == Action.DELETE) {
                number = quickConnection.delete(sql).longValue();

            } else if (action == Action.UPDATE) {
                number = quickConnection.update(sql).longValue();

            } else if (action == Action.SELECT) {
                List<Map<String, Object>> rows = quickConnection.select(sql);
                return (T) rows;
            }
            return (T) number;

        } catch (SQLException sqlExc) {
            sqlExc.printStackTrace();
            throw new QueryException(sqlExc);

        } finally {
            long endTime = System.nanoTime();
            String printSql = sql;
//            if (action == Action.INSERT) {
//                printSql = sql.length() < 512 ? sql: sql.substring(0, 512);
//            }
            String msg = "SQL Execution " + ((double) (endTime - startTime)) / 1000000 + "ms: " + printSql;
            System.out.println(msg);

            // 主动关闭连接
            if (quickConnection.autoCommit) {
                quickConnection.close();
            }
        }
    }

    private static void checkNull(Object obj, String msg) {
        if (obj == null) {
            throw new QuickORMException(msg);
        }
    }

    private static boolean isTrue(Boolean bool) {
        return bool != null && bool;
    }

    public void setAutoCommit(boolean autoCommit) {
        QuickConnection connection = getQuickConnection();
        if (autoCommit) {
            __THREAD_CONNECTION.set(null);
        } else {
            __THREAD_CONNECTION.set(connection);
        }
        connection.setAutoCommit(autoCommit);
    }

    public void startTrans() {
        setAutoCommit(false);
    }

    public void commit() {
        QuickConnection connection = __THREAD_CONNECTION.get();
        if (connection != null) {
            connection.commit();
            connection.setAutoCommit(true);
            connection.close();
            __THREAD_CONNECTION.set(null);
        }
    }

    public void rollback() {
        QuickConnection connection = __THREAD_CONNECTION.get();
        if (connection != null) {
            connection.rollback();
            connection.setAutoCommit(true);
            connection.close();
            __THREAD_CONNECTION.set(null);
        }
    }
}
