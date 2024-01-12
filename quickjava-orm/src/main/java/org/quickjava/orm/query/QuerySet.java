/*
 * Copyright by QuickJava
 */

package org.quickjava.orm.query;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreType;
import org.quickjava.common.enums.DatetimeCurrType;
import org.quickjava.common.enums.DatetimeRangeType;
import org.quickjava.common.utils.DatetimeUtil;
import org.quickjava.orm.ORMContext;
import org.quickjava.orm.enums.JoinType;
import org.quickjava.orm.model.ModelUtil;
import org.quickjava.orm.query.build.*;
import org.quickjava.orm.query.callback.OrderByOptCallback;
import org.quickjava.orm.model.callback.WhereClosure;
import org.quickjava.orm.query.callback.WhereOptCallback;
import org.quickjava.orm.contain.*;
import org.quickjava.orm.drive.Drive;
import org.quickjava.orm.query.contain.Action;
import org.quickjava.orm.query.contain.TableColumnMeta;
import org.quickjava.orm.query.enums.Operator;
import org.quickjava.orm.query.enums.OrderByType;
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
@JsonIgnoreType
public class QuerySet {

    @JsonIgnore
    private QueryReservoir reservoir = new QueryReservoir();

    public QuerySet() {}

    public QuerySet(String table) {
        reservoir.setTable(table);
    }

    public QuerySet(DatabaseConfig config) {
    }

    public static QuerySet table(String table)
    {
        return new QuerySet(table);
    }

    //TODO::-------------------- 关联查询 --------------------
    public QuerySet join(String table, String condition) {
        return join(table, condition, JoinType.INNER);
    }

    public QuerySet leftJoin(String table, String condition) {
        return join(table, condition, JoinType.LEFT);
    }

    public QuerySet rightJoin(String table, String condition) {
        return join(table, condition, JoinType.RIGHT);
    }

    public QuerySet join(String table, String condition, JoinType type) {
        return join(table, new JoinCondition(null).setRaw(condition), type);
    }

    public QuerySet join(String table, JoinCondition condition, JoinType type) {
        reservoir.getJoinList().add(new Join(type, table).addCondition(condition));
        return this;
    }

    /**
     * 限定查询返回的数据字段
     * @param columns 字段
     * @return 查询器
     */
    public QuerySet field(String ... columns)
    {
        if (ModelUtil.isEmpty(columns)) {
            return this;
        }
        for (String column : columns) {
            if (column.contains(",")) {
                Arrays.stream(column.split(",")).forEach(this::field);
            } else {
                field(parseColumn(column));
            }
        }
        return this;
    }

    public QuerySet field(String table, String column)
    {
        field(new TableColumn(table, column));
        return this;
    }

    /**
     * 限定查询返回的数据字段
     * @param columns 字段
     * @return 查询器
     */
    public QuerySet field(List<String> columns)
    {
        columns.forEach(this::field);
        return this;
    }

    public QuerySet field(TableColumn column)
    {
        reservoir.getColumnList().add(column);
        return this;
    }

    //TODO::-------------------- 查询条件 --------------------
    public QuerySet where(String field, Object value)
    {
        return this.where(field, Operator.EQ, value);
    }

    public QuerySet where(String field, Operator operator, Object value)
    {
        if (ModelUtil.isEmpty(field)) {
            return this;
        }
        where(new WhereAnd(field, operator, value));
        return this;
    }

    public QuerySet where(String table, String field, Operator operator, Object value)
    {
        if (ModelUtil.isEmpty(field)) {
            return this;
        }
        where(new WhereAnd(table, field, operator, value));
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

    public QuerySet where(Where where)
    {
        WhereOptCallback whereOptCallback = reservoir.getCallback(WhereOptCallback.class);
        if (whereOptCallback != null) {
            whereOptCallback.call(where, this, reservoir.getCallbackUserData(WhereOptCallback.class));
        }
        reservoir.getWhereList().add(where);
        return this;
    }

    public QuerySet where(List<Where> wheres)
    {
        reservoir.getWhereList().addAll(wheres);
        return this;
    }

    /**
     * 闭包查询
     * @param callback 闭包方法
     * @return 查询器
     * */
    public QuerySet where(WhereClosure callback)
    {
        QuerySet querySet = new QuerySet();
        callback.call(querySet);
        if (querySet.reservoir.whereList != null) {
            where(new WhereAnd(querySet.reservoir.getWhereList()));
        }
        return this;
    }

    public QuerySet where(String field, DatetimeRangeType range)
    {
        where(new WhereAnd(field, Operator.BETWEEN, DatetimeUtil.rangeType(range)));
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

    public QuerySet whereOr(WhereClosure callback)
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
        where(new WhereAnd(field, Operator.BETWEEN, new Object[]{v1, v2}));
        return this;
    }

    //TODO::-------------------- 特性 --------------------
    public QuerySet group(String ... columns)
    {
        for (String column : columns) {
            if (column.contains(",")) {
                Arrays.stream(column.split(",")).forEach(this::group);
            } else {
                group(parseColumn(column.trim()));
            }
        }
        return this;
    }

    private TableColumn parseColumn(String column) {
        column = column.trim();
        if (column.contains(".")) {
            String[] arr = column.split("\\.");
            return new TableColumn(arr[0].trim(), arr[1].trim());
        }
        return new TableColumn(column);
    }

    public QuerySet group(TableColumn ... columns)
    {
        for (TableColumn column : columns) {
            reservoir.getGroupBy().add(column);
        }
        return this;
    }

    public QuerySet having(String fields) {
        return having(fields.split(","));
    }

    public QuerySet having(String ... fields)
    {
        for (String field : fields) {
            having(new TableColumn(field.trim()));
        }
        return this;
    }

    public QuerySet having(TableColumn ... columns)
    {
        for (TableColumn column : columns) {
            reservoir.getHaving().add(column);
        }
        return this;
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

    //TODO::-------------------- 排序 --------------------
    public QuerySet order(String table, String field, OrderByType type)
    {
        OrderBy orderBy = new OrderBy(table, field, type);
        // 回调处理
        OrderByOptCallback orderByOptCallback = reservoir.getCallback(OrderByOptCallback.class);
        if (orderByOptCallback != null) {
            orderByOptCallback.call(orderBy, reservoir.getCallbackUserData(WhereOptCallback.class));
        }
        reservoir.getOrderByList().add(orderBy);
        return this;
    }

    public QuerySet order(String field, OrderByType type)
    {
        if (field.contains(".")) {
            String[] fieldArr = field.split("\\.");
            order(fieldArr[0], fieldArr[1], type);
        } else {
            order(null, field, type);
        }
        return this;
    }

    public QuerySet order(String field, boolean desc) {
        return order(field, desc ? OrderByType.DESC : OrderByType.ASC);
    }

    public QuerySet order(String fields)
    {
        if (ModelUtil.isEmpty(fields)) {
            return this;
        }
        if (fields.contains(",")) {
            return this.order(fields.split(","));
        }
        String[] arr = fields.trim().split(" ");
        return arr.length == 2 ? order(arr[0], OrderByType.getByName(arr[1])) : order(arr[0], OrderByType.ASC);
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

    //TODO::-------------------- 数量限制 --------------------
    public QuerySet limit(Long index, Long count)
    {
        reservoir.limitIndex = index;
        reservoir.limitSize = count;
        return this;
    }

    public QuerySet page(Long page) {
        return page(page, reservoir.limitSize);
    }

    public QuerySet page(Long page, Long size) {
        return limit((page - 1) * size, size);
    }

    //TODO::-------------------- 数据 --------------------
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

    //TODO::-------------------- 增删改查 --------------------
    public List<Map<String, Object>> select()
    {
        // 默认查询全部字段
        if (reservoir.getColumnList().size() == 0)
            field(new TableColumn(null).setRaw("*"));
        List<Map<String, Object>> resultSet = executeSql();
        return SqlUtil.isEmpty(resultSet) ? new LinkedList<>() : resultSet;
    }

    public Map<String, Object> find()
    {
        limit(0L, 1L);
        List<Map<String, Object>> resultSet = select();
        return SqlUtil.isEmpty(resultSet) ? null : resultSet.get(0);
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

    //TODO::-------------------- 扩展方法 --------------------
    /**
     * 获取表全部字段
     * @return 字段列表
     * */
    public List<TableColumnMeta> getColumns() {
        return getColumns(reservoir.table);
    }

    public List<TableColumnMeta> getColumns(String table)
    {
        // 先查内存缓存
        List<TableColumnMeta> ret = SqlUtil.getTableColumns(table);
        if (ret != null) {
            return ret;
        }
        String sql = "SHOW FULL COLUMNS FROM " + table;
        List<Map<String, String>> columns = this.executeSql(sql);
        if (ModelUtil.isEmpty(columns)) {
            return new LinkedList<>();
        }
        List<TableColumnMeta> columns1 = new LinkedList<>();
        columns.forEach(info -> columns1.add(new TableColumnMeta(info)));
        SqlUtil.setTableColumns(table, columns1);
        return columns1;
    }

    // 获取表主键字段
    public String pk() {
        List<TableColumnMeta> columns = getColumns();
        for (TableColumnMeta column : columns) {
            if ("PRI".equals(column.Key))
                return column.Field;
        }
        return null;
    }

    //TODO::--------------- 语句方法 ---------------
    public String buildSql()
    {
        reservoir.action = reservoir.action == null ? Action.SELECT : reservoir.action;
        if (reservoir.getColumnList().size() == 0)
            field(new TableColumn(null).setRaw("*"));
        return ORMContext.getDrive().pretreatment(this);
    }

    /**
     * 1.返回sql类，可以获取sql
     * 2.改成传入方法进行数据回填，java限制不能直接返回
     */
    public QuerySet fetchSql(boolean fetch) {
        reservoir.setFetchSql(true);
        return this;
    }

    private <T> T executeSql()
    {
        reservoir.action = reservoir.action == null ? Action.SELECT : reservoir.action;
        if (reservoir.fetchSql) {
            reservoir.setSql(ORMContext.getDrive().pretreatment(this));
            if (reservoir.printSql) {
                System.out.println(reservoir.getSql());
            }
            return null;
        }
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
        List<TableColumn> cacheFiledList = new LinkedList<>(reservoir.getColumnList());
        // 分页器开启直接打印sql
        reservoir.printSql = true;
        // 获取总数
        reservoir.getColumnList().clear();
        Integer total = count();
        // 执行查询
        reservoir.getColumnList().clear();
        reservoir.getColumnList().addAll(cacheFiledList);
        List<Map<String, Object>> rows = select();
        // 返回分页
        Long page = reservoir.limitIndex / reservoir.limitSize + 1;
        return new Pagination<>(page, reservoir.limitSize, total, rows);
    }

    public Pagination<Map<String, Object>> pagination(Long page, Long pageSize) {
        this.page(page, pageSize);
        return pagination();
    }

    public Pagination<Map<String, Object>> pagination(Long page) {
        return this.pagination(page, 20L);
    }

    //TODO::--------------- 统计方法 ---------------
    public Integer count() {
        return count(new TableColumn(null).setRaw("COUNT(*)"));
    }

    public Integer count(String field) {
        return count(new TableColumn(null).setRaw("COUNT("+field+")"));
    }

    public Integer count(TableColumn column)
    {
        QueryReservoir reservoirOld = this.reservoir;
        this.reservoir = new QueryReservoir();
        // 聚合count只需要field、where
        this.reservoir.table = reservoirOld.table;
        this.reservoir.fetchSql = reservoirOld.fetchSql;
        this.reservoir.printSql = reservoirOld.printSql;
        this.reservoir.joinList = reservoirOld.joinList;
        this.reservoir.unionList = reservoirOld.unionList;
        this.reservoir.whereList = reservoirOld.whereList;
        this.reservoir.groupBy = reservoirOld.groupBy;
        this.reservoir.having = reservoirOld.having;
        this.reservoir.distinct = reservoirOld.distinct;
        this.field(column);

        List<Map<String, Object>> resultSet = executeSql();
        // 恢复
        this.reservoir = reservoirOld;

        if (resultSet == null || resultSet.isEmpty()) {
            return 0;
        }
        return Math.toIntExact((Long) resultSet.get(0).get(column.getRaw()));
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
