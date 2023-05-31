/*
 * Copyright (c) 2021.
 * More Info to http://www.quickjava.org
 */

package org.quickjava.orm.drive;

import org.quickjava.orm.QuerySet;
import org.quickjava.orm.contain.Action;
import org.quickjava.orm.contain.DriveConfigure;
import org.quickjava.orm.contain.Value;
import org.quickjava.orm.contain.WhereBase;
import org.quickjava.orm.utils.QueryException;
import org.quickjava.orm.utils.SqlUtil;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Qlo1062-(QloPC-zs)
 * &#064;date  2021/1/25 15:54
 */
public abstract class Drive {

    private DriveConfigure configure = null;

    private static final DriveConfigure CONFIGURE_DEFAULT = new DriveConfigure(
            "", "",
            "'", "'"
    );

    private QuickConnection quickConnection = null;

    public DriveConfigure getConfigure() {
        return configure == null ? CONFIGURE_DEFAULT : configure;
    }

    public void setConfigure(DriveConfigure configure) {
        this.configure = configure;
    }

    public void setQuickConnection(QuickConnection quickConnection) {
        this.quickConnection = quickConnection;
    }

    public QuickConnection getQuickConnection() {
        return quickConnection;
    }

    /**
     * 语句准备
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
        DriveConfigure config = getConfigure();

        // action
        Action action = query.__Action();
        sqlList.add(action.beginSymbol());

        // action is SELECT
        if (action == Action.SELECT) {
            sqlList.add(SqlUtil.collJoin(",", query.__FieldList()));
            sqlList.add("FROM");
        }

        // table name
        sqlList.add(query.__Table().toString());

        // TODO::JOIN
        if (query.__JoinList().size() > 0) {
            query.__JoinList().forEach(arr -> {
                sqlList.add(String.format("%s JOIN %s ON %s", arr[2], arr[0], arr[1]));
            });
        }

        // INSERT-DATA
        if (action == Action.INSERT) {
            StringBuilder dataSql = new StringBuilder();
            // field
            SqlUtil.mapKeyJoin(dataSql, query.__DataList().get(0));
            // values
            dataSql.append(" VALUES ");
            // 数据处理方法
            SqlUtil.MapJoinCallback joinCallback = entry -> Value.pretreatment(entry.getValue());
            for (int i = 0; i < query.__DataList().size(); i++) {
                if (i > 0)
                    dataSql.append(',');
                SqlUtil.mapBracketsJoin(dataSql, query.__DataList().get(i), joinCallback);
            }
            sqlList.add(dataSql.toString());
        }

        // UPDATE
        if (action == Action.UPDATE) {
            StringBuilder dataSql = new StringBuilder();
            int fi = 0;
            for (Map.Entry<String, Object> entry : query.__DataList().get(0).entrySet()) {
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
        if (query.__WhereList().size() > 0) {
            sqlList.add("WHERE");
            sqlList.add(WhereBase.cutFirstLogic(WhereBase.toSql(query.__WhereList(), config)));
        }

        // GROUP BY
        // HAVING

        // ORDER BY
        if (query.__Orders().size() > 0) {
            sqlList.add(String.format("ORDER BY %s", SqlUtil.collJoin(",", query.__Orders())));
        }

        // Limit
        if (action == Action.SELECT && query.__Limit() != null) {
            sqlList.add(String.format("LIMIT %d,%d", query.__Limit(), query.__LimitSize()));
        }

        return SqlUtil.collJoin(" ", sqlList);
    }

    public <T> T executeSql(QuerySet query) {
        return executeSql(query.__Action(), pretreatment(query));
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
                Map<String, Object> generatedKeys = JdbcDock.insert(quickConnection.connection, sql);
                if (generatedKeys == null || generatedKeys.isEmpty()) {
                    return null;
                }
                Object gk = generatedKeys.get("GENERATED_KEY");
                number = gk instanceof Long ? (Long) gk : Long.valueOf(String.valueOf(gk));

            } else if (action == Action.DELETE) {
                number = JdbcDock.delete(quickConnection.connection, sql).longValue();

            } else if (action == Action.UPDATE) {
                number = JdbcDock.update(quickConnection.connection, sql).longValue();

            } else if (action == Action.SELECT) {
                List<Map<String, Object>> rows = JdbcDock.select(quickConnection.connection, sql);
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

    public void setAutoCommit(boolean autoCommit) {
        try {
            quickConnection.setAutoCommit(autoCommit);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void startTrans() {
        setAutoCommit(false);
    }

    public void commit() {
        try {
            quickConnection.commit();
            quickConnection.setAutoCommit(true);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            quickConnection.close();        // druid连接池：事务完成后返回连接
        }
    }

    public void rollback() {
        try {
            quickConnection.rollback();
            quickConnection.setAutoCommit(true);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            quickConnection.close();        // druid连接池：事务完成后返回连接
        }
    }
}
