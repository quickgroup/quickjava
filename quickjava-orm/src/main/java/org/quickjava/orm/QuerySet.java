/*
 * Copyright by QuickJava
 */

package org.quickjava.orm;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.quickjava.common.enums.DatetimeCurrType;
import org.quickjava.common.enums.DatetimeRangeType;
import org.quickjava.common.utils.DatetimeUtil;
import org.quickjava.orm.callback.WhereCallback;
import org.quickjava.orm.contain.*;
import org.quickjava.orm.drive.Drive;
import org.quickjava.orm.enums.Operator;
import org.quickjava.orm.utils.ORMHelper;
import org.quickjava.orm.utils.QueryException;
import org.quickjava.orm.utils.SqlUtil;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 数据操作类，本类主要进行条件构建、查询、数据返回
 * - select 返回 {@code List<Map<String,Object>>}
 * - find 返回 {@code Map<String,Object>}
 * - QuerySet 是不支持层级模型返回的，只支持简单查询和join查询，数据层级封装由Model自己负责
 * @author Qlo1062-(QloPC-zs)
 * &#064;date  2021/1/19 10:18
 */
public class QuerySet {

    @JsonIgnore
    private final QueryReservoir reservoir = new QueryReservoir();

    public QuerySet() {}

    public QuerySet(String table) {
        reservoir.setTable(table);
    }

    public static QuerySet table(String table)
    {
        return new QuerySet(table);
    }

    public QuerySet join(String table, String condition) {
        return join(table, condition, "INNER");
    }

    public QuerySet join(String table, String condition, String type)
    {
        reservoir.getJoinList().add(new String[]{table, condition, type});
        return this;
    }

    /**
     * 限定查询返回的数据字段
     * @param field 字段
     * @return 查询器
     */
    public QuerySet field(String field)
    {
        if (ORMHelper.isEmpty(field)) {
            return this;
        }
        if (field.contains(",")) {
            Arrays.stream(field.split(",")).forEach(this::field);
        } else {
//            this.fieldList.add(field.contains("\\.") ? field.split("\\.") : new String[]{field});
            reservoir.getFieldList().add(field);
        }
        return this;
    }

    /**
     * 限定查询返回的数据字段
     * @param fields 字段
     * @return 查询器
     */
    public QuerySet field(List<String> fields)
    {
        fields.forEach(this::field);
        return this;
    }

    /**
     * 限定查询返回的数据字段
     * 支持书写格式：table.field、table.field tableField、table.field table_field、field
     * @param fields 字段
     * @return 查询器
     */
    public QuerySet field(String[] fields) {
        for (String item : fields) {
            field(item);
        }
        return this;
    }


    public QuerySet where(String field, Object value)
    {
        return this.where(field, Operator.EQ, value);
    }

    public QuerySet where(String field, Operator operator, Object value)
    {
        if (ORMHelper.isEmpty(field)) {
            return this;
        }
        where(new Where(field, operator, value));
        return this;
    }

    /**
     * 高级sql语句查询
     * @param sql SQL语句
     * @return 查询器
     * */
    public QuerySet where(String sql)
    {
        where(sql, Operator.RAW, null);
        return this;
    }

    public QuerySet where(WhereBase where)
    {
        reservoir.getWhereList().add(where);
        return this;
    }

    /**
     * 闭包查询
     * @param callback 闭包方法
     * @return 查询器
     * */
    public QuerySet where(WhereCallback callback)
    {
        QuerySet querySet = new QuerySet();
        callback.call(querySet);
        if (querySet.reservoir.whereList != null) {
            where(new Where(querySet.reservoir.getWhereList()));
        }
        return this;
    }

    public QuerySet where(String field, DatetimeRangeType range)
    {
        where(new WhereOr(field, Operator.BETWEEN, DatetimeUtil.rangeType(range)));
        return this;
    }

    public QuerySet where(String field, DatetimeCurrType currType)
    {
        where(new WhereOr(field, Operator.EQ, DatetimeUtil.currType(currType)));
        return this;
    }

    public QuerySet whereOr(String field, Operator operator, Object value)
    {
        where(new WhereOr(field, operator, value));
        return this;
    }

    /**
     * OR查询
     * @param field 字段名
     * @param value 数值
     * @return 查询器
     */
    public QuerySet whereOr(String field, Object value)
    {
        return this.whereOr(field, Operator.EQ, value);
    }

    public QuerySet whereOr(String sql)
    {
        where(sql, Operator.RAW, null);
        return this;
    }

    public QuerySet whereOr(WhereCallback callback)
    {
        QuerySet querySet = new QuerySet();
        callback.call(querySet);
        if (querySet.reservoir.whereList != null) {
            where(new WhereOr(querySet.reservoir.getWhereList()));
        }
        return this;
    }

    public QuerySet between(String field, Object v1, Object v2)
    {
        where(new Where(field, Operator.BETWEEN, new Object[]{v1, v2}));
        return this;
    }

    public QuerySet group(String fields)
    {
        if (ORMHelper.isEmpty(fields)) {
            return this;
        }
        reservoir.setGroupBy(fields);
        return this;
    }

    public QuerySet having(String fields)
    {
        if (ORMHelper.isEmpty(fields)) {
            return this;
        }
        reservoir.setHaving(fields);
        return this;
    }

    public QuerySet order(String field, String sort)
    {
        reservoir.getOrderByList().add(String.format("%s %s", field, sort.toUpperCase()));
        return this;
    }

    public QuerySet order(String field, boolean asc) {
        return order(field, asc ? "ASC" : "DESC");
    }

    public QuerySet order(String fields)
    {
        if (ORMHelper.isEmpty(fields)) {
            return this;
        }
        if (fields.contains(",")) {
            return this.order(fields.split(","));
        }
        String[] arr = fields.trim().split(" ");
        return arr.length == 2 ? order(arr[0], arr[1]) : order(arr[0], "ASC");
    }

    public QuerySet order(List<String> fields) {
        fields.forEach(this::order);
        return this;
    }

    public QuerySet order(String[] fields) {
        for (String field : fields) {
            order(field);
        }
        return this;
    }

    public QuerySet limit(Integer index, Integer count)
    {
        reservoir.limitIndex = index;
        reservoir.limitSize = count;
        return this;
    }

    public QuerySet page(Integer page) {
        return page(page, reservoir.limitSize);
    }

    public QuerySet page(Integer page, Integer size) {
        return limit((page - 1) * size, size);
    }

    public QuerySet union(String sql) {
        reservoir.getUnionList().add(sql);
        return this;
    }

    public QuerySet union(String[] sqlArr) {
        for (String s : sqlArr) {
            union(s);
        }
        return this;
    }

    public QuerySet distinct(boolean distinct) {
        reservoir.distinct = distinct;
        return this;
    }

    public QuerySet lock(boolean lock) {
        reservoir.lock = lock;
        return this;
    }

    //TODO::----------- 数据方法 -----------
    public List<Map<String, Object>> select()
    {
        // 默认查询全部字段
        if (reservoir.fieldList.size() == 0)
            field("*");
        List<Map<String, Object>> resultSet = executeSql();
        return SqlUtil.isEmpty(resultSet) ? new LinkedList<>() : resultSet;
    }

    public Map<String, Object> find()
    {
        limit(0, 1);
        List<Map<String, Object>> resultSet = select();
        return SqlUtil.isEmpty(resultSet) ? null : resultSet.get(0);
    }

    public QuerySet data(String field, Object value)
    {
        reservoir.getData().put(field, value);
        return this;
    }

    public QuerySet data(Map<String, Object> data)
    {
        reservoir.getData().putAll(data);
        return this;
    }

    public Map<String, Object> data()
    {
        return reservoir.dataList == null || reservoir.dataList.size() == 0 ? null : reservoir.getData();
    }

    public Integer update(Map<String, Object> data)
    {
        reservoir.action = Action.UPDATE;
        this.data(data);
        executeSql();
        return null;
    }

    public Integer update()
    {
        reservoir.action = Action.UPDATE;
        executeSql();
        return 1;
    }

    public Long insert(Map<String, Object> data)
    {
        reservoir.action = Action.INSERT;
        this.data(data);
        return executeSql();
    }

    public Integer insertAll(List<DataMap> dataList)
    {
        reservoir.action = Action.INSERT;
        reservoir.getDataList().addAll(dataList);
        return executeSql();
    }

    public Integer delete()
    {
        if (reservoir.whereList == null) {
            throw new QueryException("不允许空条件的删除执行");
        }
        reservoir.action = Action.DELETE;
        Long result = executeSql();
        return result.intValue();
    }

    /**
     * 获取表全部字段
     * @return 字段列表
     * */
    public List<TableColumn> getColumns() {
        return getColumns(reservoir.table);
    }

    public List<TableColumn> getColumns(String table)
    {
        // 先查内存缓存
        List<TableColumn> ret = SqlUtil.getTableColumns(table);
        if (ret != null) {
            return ret;
        }
//        table = (SqlUtil.isUpperString(table) ? table : SqlUtil.backQuote(table));
        String sql = "SHOW FULL COLUMNS FROM " + table;
        List<Map<String, String>> columns = this.executeSql(sql);
        if (ORMHelper.isEmpty(columns)) {
            return new LinkedList<>();
        }
        List<TableColumn> columns1 = new LinkedList<>();
        columns.forEach(info -> columns1.add(new TableColumn(info)));
        SqlUtil.setTableColumns(table, columns1);
        return columns1;
    }

    // 获取表主键字段
    public String pk() {
        List<TableColumn> columns = getColumns();
        for (TableColumn column : columns) {
            if ("PRI".equals(column.Key))
                return column.Field;
        }
        return null;
    }

    //TODO::--------------- 语句方法 ---------------
    public String buildSql()
    {
        reservoir.action = reservoir.action == null ? Action.SELECT : reservoir.action;
        return ORMContext.getDrive().pretreatment(this);
    }

    private <T> T executeSql()
    {
        reservoir.action = reservoir.action == null ? Action.SELECT : reservoir.action;
        return ORMContext.getDrive().executeSql(this);
    }

    private  <T> T executeSql(String sql)
    {
        return ORMContext.getDrive().executeSql(Action.SELECT, sql);
    }

    public <T> T execute(String sql) {
        return ORMContext.getDrive().executeSql(Action.INSERT, sql);
    }

    public <T> T query(String sql) {
        return ORMContext.getDrive().executeSql(Action.SELECT, sql);
    }

    //TODO::--------------- 增强方法 ---------------
    // 分页查询
    public Pagination<Map<String, Object>> pagination()
    {
        List<String> cacheFiledList = new LinkedList<>(reservoir.fieldList);
        // 获取总数
        reservoir.fieldList.clear();
        Integer total = count();
        // 执行查询
        reservoir.fieldList.clear();
        reservoir.fieldList.addAll(cacheFiledList);
        List<Map<String, Object>> rows = select();
        // 返回分页
        int page = reservoir.limitIndex / reservoir.limitSize + 1;
        return new Pagination<>(page, reservoir.limitSize, total, rows);
    }

    public Pagination<Map<String, Object>> pagination(Integer page, Integer pageSize) {
        this.page(page, pageSize);
        return pagination();
    }

    public Pagination<Map<String, Object>> pagination(Integer page) {
        return this.pagination(page, 20);
    }

    //TODO::--------------- 统计方法 ---------------
    public Integer count() {
        return count("COUNT(*)");
    }

    public Integer count(String field)
    {
        field(field);
        List<Map<String, Object>> resultSet = executeSql();
        return Math.toIntExact((Long) resultSet.get(0).get(field));
    }

    //TODO::--------------- 事务操作方法 ---------------
    /**
     * 开启事务
     * */
    public static void startTrans() {
        startTrans(ORMContext.getDrive());
    }

    public static void startTrans(Drive drive) {
        drive.startTrans();
    }

    /**
     * 提交事务
     * */
    public static void commit() {
        commit(ORMContext.getDrive());
    }

    public static void commit(Drive drive) {
        drive.commit();
        drive.setAutoCommit(true);
    }

    /**
     * 事务回滚
     * */
    public static void rollback() {
        rollback(ORMContext.getDrive());
    }

    public static void rollback(Drive drive) {
        drive.rollback();
        drive.setAutoCommit(true);
    }

    /**
     * 回调方法中执行事务
     * @param callback 事务回调方法
     * */
    public static void transaction(TransactionCallback callback)
    {
        transaction(ORMContext.getDrive(), callback);
    }

    public static void transaction(Drive drive, TransactionCallback callback)
    {
        startTrans(drive);
        try {
            callback.call();
            commit(drive);
        } catch (Throwable th){
            rollback(drive);
        }
    }

    public interface TransactionCallback {
        void call();
    }
}
