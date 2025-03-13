/*
 * Copyright (c) 2021.
 * More Info to http://www.quickjava.org
 */

package org.quickjava.orm.drive;

import org.quickjava.orm.domain.DatabaseMeta;
import org.quickjava.orm.domain.DriveConfigure;
import org.quickjava.orm.query.QuerySetHelper;
import org.quickjava.orm.query.QueryReservoir;
import org.quickjava.orm.query.QuerySet;
import org.quickjava.orm.query.build.ValueConv;
import org.quickjava.orm.query.build.Where;
import org.quickjava.orm.query.contain.Action;
import org.quickjava.orm.query.contain.Label;
import org.quickjava.orm.utils.QueryException;
import org.quickjava.orm.utils.QuickORMException;
import org.quickjava.orm.utils.SqlUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Qlo1062-(QloPC-zs)
 * &#064;date  2021/1/25 15:54
 */
public abstract class Drive {

    protected static final Logger logger = LoggerFactory.getLogger(Drive.class);

    private DatabaseMeta config = null;

    // 当前事务连接
    private static final ThreadLocal<QuickConnection> __transactionConnection = new ThreadLocal<>();

    // 默认mysql风格
    private static final DriveConfigure DRIVE_CONFIGURE_DEF = new DriveConfigure(
            "", "",
            "\"", "\""
    );

    public DriveConfigure getDriveConfigure() {
        return DRIVE_CONFIGURE_DEF;
    }

    public void setConfig(DatabaseMeta config) {
        this.config = config;
    }

    /**
     * 获取连接
     * @return 连接
     */
    public QuickConnection getQuickConnection() {
        // 事务中、线程中的数据库连接
        if (__transactionConnection.get() != null) {
            return __transactionConnection.get();
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
        QueryReservoir reservoir = QuerySetHelper.getQueryReservoir(query);
        reservoir.pretreatment(config);
        ValueConv valueConv = new ValueConv(config);

        // action
        Action action = reservoir.action;
        sqlList.add(reservoir.action.toString());

        // SELECT
        if (action == Action.SELECT) {
            // DISTINCT
            if (isTrue(reservoir.distinct)) {
                sqlList.add("DISTINCT");
            }
            // columns
            checkEmpty(reservoir.columnList, "Missing column");
            sqlList.add(SqlUtil.listJoin(reservoir.getColumnList(), ", ", e -> e.toSql(config)));
            sqlList.add("FROM");
        }

        // 主表名称
        sqlList.add(reservoir.tableSql());
        checkEmpty(reservoir.tableSql(), "table is empty");

        // JOIN
        if (reservoir.joinList != null) {
            sqlList.add(SqlUtil.listJoin(reservoir.getJoinList(), " ", e -> e.toSql(config)));
        }

        // INSERT-DATA
        if (action == Action.INSERT) {
            checkEmpty(reservoir.dataList, "Missing insert data");
            checkEmpty(reservoir.dataList.get(0), "Missing update data");
            StringBuilder dataSql = new StringBuilder();
            // column
            SqlUtil.mapBracketsJoin(dataSql, reservoir.dataList.get(0));
            // values
            dataSql.append(" VALUES ");
            // 数据处理方法
            SqlUtil.MapJoinCallback joinCallback = entry -> valueConv.convWrap(entry.getValue());
            for (int i = 0; i < reservoir.dataList.size(); i++) {
                if (i > 0)
                    dataSql.append(',');
                SqlUtil.mapBracketsJoin(dataSql, reservoir.dataList.get(i), joinCallback);
            }
            sqlList.add(dataSql.toString());
        }

        // UPDATE-DATA
        if (action == Action.UPDATE) {
            checkEmpty(reservoir.dataList, "Missing update data");
            checkEmpty(reservoir.dataList.get(0), "Missing update data");
            StringBuilder dataSql = new StringBuilder();
            sqlList.add("SET");
            SqlUtil.MapJoinCallback joinCallback = entry -> String.format("%s=%s", entry.getKey(), valueConv.convWrap(entry.getValue()));
            SqlUtil.mapJoin(dataSql, ", ", reservoir.dataList.get(0), joinCallback);
            sqlList.add(dataSql.toString());
        }

        // WHERE
        if (reservoir.whereList != null) {
            sqlList.add("WHERE");
            sqlList.add(Where.cutFirstLogic(Where.collectSql(reservoir.getWhereList(), config)));
        }

        // GROUP BY
        if (action == Action.SELECT && reservoir.groupBy != null) {
            sqlList.add("GROUP BY");
            sqlList.add(SqlUtil.listJoin(reservoir.getGroupBy(), ", ", e -> e.toSql(config)));
        }

        // HAVING
        if (action == Action.SELECT && reservoir.having != null) {
            sqlList.add("HAVING");
            sqlList.add(SqlUtil.listJoin(reservoir.getHaving(), ", ", e -> e.toSql(config)));
        }

        // ORDER BY
        if (action == Action.SELECT && reservoir.orderByList != null) {
            sqlList.add("ORDER BY");
            sqlList.add(SqlUtil.listJoin(reservoir.getOrderByList(), ", ", e -> e.toSql(config)));
        }

        // Limit
        if (action == Action.SELECT && reservoir.limitIndex != null) {
            reservoir.limitSize = reservoir.limitSize == null ? 20 : reservoir.limitSize;
            if (reservoir.limitIndex == 0) {
                sqlList.add(String.format("LIMIT %d", reservoir.limitSize));
            } else {
                sqlList.add(String.format("LIMIT %d,%d", reservoir.limitIndex, reservoir.limitSize));
            }
        }

        // lock
        if (action == Action.SELECT && isTrue(reservoir.lock)) {
            sqlList.add("FOR UPDATE");  // 不同数据库可能改变，故需要驱动自行处理
        }

        return SqlUtil.collJoin(" ", sqlList);
    }

    public <T> T executeSql(QuerySet query) {
        QueryReservoir reservoir = QuerySetHelper.getQueryReservoir(query);
        return executeSql(reservoir.action, pretreatment(query), reservoir);
    }

    /**
     * 执行原生sql语句
     * @param action action
     * @param sql 语句
     * @param reservoir 执行信息
     * @return 数据
     * @param <T> 数据类型
     */
    public <T> T executeSql(Action action, String sql, QueryReservoir reservoir)
    {
        long startTime = System.currentTimeMillis();
        boolean isError = false;
        Object execResult = null;
        QuickConnection quickConnection = getQuickConnection();

        try {
            // 执行操作
            if (action == Action.INSERT) {
                Map<String, Object> generatedKeys = quickConnection.insert(sql, reservoir.getLabels());
                if (generatedKeys == null || generatedKeys.isEmpty()) {
                    return null;
                }
                // 返回自增id
                if (reservoir.labelContain(Label.INSERT_GET_ID)) {
                    Object gk = generatedKeys.get("GENERATED_KEY");
                    if (gk instanceof Long) {
                        execResult = gk;
                    } else if (gk instanceof Integer) {
                        execResult = gk;
                    } else {
                        execResult = null;
                    }
                }

            } else if (action == Action.DELETE) {
                execResult = quickConnection.delete(sql);

            } else if (action == Action.UPDATE) {
                execResult = quickConnection.update(sql);

            } else if (action == Action.SELECT) {
                List<Map<String, Object>> rows = quickConnection.select(sql);
                return (T) rows;
            }
            return (T) execResult;

        } catch (SQLException e) {
            isError = true;
            logger.error("SQL execution error:{}", e.getMessage(), e);
            throw new QueryException(e);

        } finally {
            long endTime = System.currentTimeMillis();
            String printSql = sql;
            if (action == Action.INSERT) {
                printSql = sql.length() < 2560 ? sql: sql.substring(0, 2560);
            }
            String msg = "SQL execution " + (endTime - startTime) + "ms: " + printSql;
            if (isError) {
                logger.error(msg);
            } else {
                logger.debug(msg);
            }

            // 关闭连接（每次使用完必须关闭，要实现长连接，就需要实现连接池+线程变量
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

    private static void checkEmpty(Object obj, String msg) {
        if (obj == null) {
            throw new QuickORMException(msg);
        } else if (List.class.isAssignableFrom(obj.getClass())) {
            List<?> list = (List<?>) obj;
            if (list.isEmpty()) {
                throw new QuickORMException(msg);
            }
        } else if (Map.class.isAssignableFrom(obj.getClass())) {
            Map<?, ?> map = (Map<?, ?>) obj;
            if (map.isEmpty()) {
                throw new QuickORMException(msg);
            }
        } else if (String.class.isAssignableFrom(obj.getClass())) {
            String str = (String) obj;
            if (str.isEmpty()) {
                throw new QuickORMException(msg);
            }
        }
    }

    private static boolean isTrue(Boolean bool) {
        return bool != null && bool;
    }

    //NOTE::---------- 事务方法 ----------

    public void setAutoCommit(boolean autoCommit)
    {
        if (__transactionConnection.get() != null && !autoCommit) {
//            throw new QuickORMException("已处于事务中");
            logger.warn("已处于事务中");
            return;
        }
        QuickConnection connection = getQuickConnection();
        __transactionConnection.set(autoCommit ? null : connection);
        // 事务隔离
        if (!autoCommit) {
            logger.info("Transaction start");
            connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
        }
        connection.setAutoCommit(autoCommit);
    }

    public void startTrans() {
        setAutoCommit(false);
    }

    /**
     * 提交事务，并关闭事务连接
     */
    public void commit() {
        QuickConnection connection = __transactionConnection.get();
        if (connection != null) {
            logger.info("Transaction commit");
            connection.commit();
            connection.setAutoCommit(true);
            connection.close();   // FIXME::关闭连接
            __transactionConnection.set(null);
        }
    }

    /**
     * 回滚事务，并关闭事务连接
     */
    public void rollback() {
        QuickConnection connection = __transactionConnection.get();
        if (connection != null) {
            logger.info("Transaction rollback {}", connection);
            connection.rollback();
            connection.setAutoCommit(true);
            connection.close();   // FIXME::关闭连接
            __transactionConnection.set(null);
        }
    }
}
