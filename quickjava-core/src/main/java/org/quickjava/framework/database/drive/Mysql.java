/*
 * Copyright (c) 2021.
 * More Info to http://www.quickjava.org
 */

package org.quickjava.framework.database.drive;

import org.quickjava.common.QuickLog;
import org.quickjava.common.utils.StringUtils;
import org.quickjava.framework.bean.DbConfig;
import org.quickjava.framework.database.DBMan;
import org.quickjava.framework.database.Query;
import org.quickjava.framework.database.QuickConnection;
import org.quickjava.framework.database.contain.Action;
import org.quickjava.framework.database.contain.Limit;
import org.quickjava.framework.database.contain.Value;
import org.quickjava.framework.exception.QuickSqlException;

import java.sql.SQLException;
import java.util.*;

/**
 * @author Qlo1062-(QloPC-zs)
 * @date 2021/1/25 15:06
 */
public class Mysql implements Drive {

    private Action action = null;

    private String sql = null;

    private QuickConnection quickConnection = null;

    public Mysql() {
        quickConnection = DBMan.getQuickConnection();
    }

    @Override
    public Query connect(DbConfig config) {
        return null;
    }

    /***
     * 预编译部分
     * @param query
     * @return
     */
    @Override
    public String pretreatment(Query query)
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
        this.action = query._getAction();

        // action
        String action = Action.ActionString.str(query._getAction());
        sqlList.add(action);

        // action is SELECT
        if (query._getAction() == Action.SELECT) {
            sqlList.add(StringUtils.join(query._getSelectField(), ","));
            sqlList.add("FROM");
        }

        // table name
        sqlList.add(query._getTable().toString());

        // TODO::JOIN

        // UPDATE
        if (query._getAction() == Action.UPDATE) {
            StringBuffer tempSql = new StringBuffer();
            // 字段值
            int fi = 0;
            for (Map.Entry<String, Object> entry : query._getData().entrySet()) {
                if (fi++ > 0)
                    tempSql.append(",");
                String item = String.format("`%s`=%s", entry.getKey(), Value.parse(entry.getValue()));
                tempSql.append(item);
            }
            sqlList.add("SET");
            sqlList.add(tempSql.toString());
        }

        // INSERT-DATA
        if (query._getAction() == Action.INSERT) {
            Map<String, Object> data = query._getData();
            StringBuffer tempSql = new StringBuffer();
            String[] valueArr = new String[data.size()];
            // field
            tempSql.append("(");
            int fi = 0;
            for (Map.Entry<String, Object> entry : data.entrySet()) {
                if (fi > 0)
                    tempSql.append(",");
                tempSql.append(entry.getKey());
                valueArr[fi++] = Value.parse(entry.getValue());
            }
            // values
            tempSql.append(") ");
            tempSql.append(" VALUES ");
            tempSql.append("(");
            for (fi = 0; fi < valueArr.length; fi++) {
                if (fi > 0)
                    tempSql.append(",");
                tempSql.append(valueArr[fi]);
            }
            tempSql.append(")");
            sqlList.add(tempSql.toString());
        }

        // WHERE
        if (query._getWheres().size() > 0) {
            sqlList.add("WHERE");
            sqlList.add(StringUtils.join(query._getWheres(), " AND "));
        }

        // ORDER BY
        if (query._getOrder() != null) {
            sqlList.add(String.format("ORDER BY %s", query._getOrder()));
        }

        // Limit
        if (query._getAction() == Action.SELECT && query._getLimit() != null) {
            Limit limit = query._getLimit();
            sqlList.add(String.format("LIMIT %d,%d", limit.getLimit(), limit.getCount()));
        }

        sqlList.add(";");
        sql = StringUtils.join(sqlList, " ");

        return sql;
    }

    @Override
    public String assemble(Drive drive) {
        return null;
    }

    @Override
    public Object executeSql(String sql)
    {
        long startTime = System.nanoTime();
        Long number = null;

        try {
            if (action == Action.INSERT) {
                Map generatedKeys = JdbcDock.insert(quickConnection.connection, sql);
//                System.out.println("generatedKeys=" + generatedKeys);
                number = (Long) generatedKeys.get("GENERATED_KEY");

            } else if (action == Action.DELETE) {
                number = JdbcDock.delete(quickConnection.connection, sql).longValue();

            } else if (action == Action.UPDATE) {
                number = JdbcDock.update(quickConnection.connection, sql).longValue();

            } else if (action == Action.SELECT) {
                System.out.println("quickConnection="+ quickConnection);
                List<Map> rows = JdbcDock.select(quickConnection.connection, sql);
                return rows;

            }

            return number;

        } catch (SQLException sqlExc) {
            sqlExc.printStackTrace();
            throw new QuickSqlException(sqlExc);

        } finally {
            long endTime = System.nanoTime();
            QuickLog.debug("execution SQL time: " + ((double) (endTime - startTime)) / 1000000 + "ms，SQL=" + sql);

        }
    }
}
