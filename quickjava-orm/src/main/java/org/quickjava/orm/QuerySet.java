/*
 * Copyright by QuickJava
 */

package org.quickjava.orm;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import org.quickjava.orm.contain.*;
import org.quickjava.orm.contain.*;
import org.quickjava.orm.drive.Drive;
import org.quickjava.orm.drive.Mysql;
import org.quickjava.orm.utils.DBConfig;
import org.quickjava.orm.utils.QueryException;
import org.quickjava.orm.utils.SqlUtil;

import java.util.*;

/**
 * 数据操作类，本类主要进行条件构建、查询、数据返回
 * - select 返回 List<Map<String,Object>>
 * - find 返回 Map<String,Object>
 * - QuerySet 是不支持层级模型返回的，只支持简单查询和join查询，数据层级封装由Model自己负责
 * @author Qlo1062-(QloPC-zs)
 * @date 2021/1/19 10:18
 */
public class QuerySet {

    private DBConfig dbConfig = null;

    private static QuerySet __defaultQuerySet = null;

    private Drive drive = new Mysql();

    private String table = null;

    private Action action = Action.SELECT;

    private final List<String> fieldList = new ArrayList<>();

    private final List<WhereBase> whereList = new LinkedList<>();

    private final List<String> orderByList = new LinkedList<>();

    private final Map<String, Object> data = new LinkedHashMap<>();

    private final List<Map<String, Object>> dataList = new LinkedList<>();

    private String groupBy = null;

    private Integer limitIndex = null;

    private Integer limitSize = null;

    private final List<String[]> joinList = new LinkedList<>();

    public QuerySet() {}

    public QuerySet(String table) {
        this.table = table;
    }

    /**
     * 连接数据库（返回默认链接
     * */
    public static QuerySet connect() {
        synchronized (QuerySet.class) {
            if (__defaultQuerySet == null) {
                __defaultQuerySet = new QuerySet();
            }
        }
        return __defaultQuerySet;
    }

    public static QuerySet table(String table)
    {
        return new QuerySet(table);
    }

    public QuerySet where(String field, Object value)
    {
        return this.where(field, "=", value);
    }

    public QuerySet where(String field, String operator, Object value)
    {
        whereList.add(new Where(field, operator, value));
        return this;
    }

    public QuerySet between(String field, Object val1, Object val2)
    {
        whereList.add(new Where(field, "BETWEEN", new Object[]{val1, val2}));
        return this;
    }

    public QuerySet between(String field, Object[] valArr)
    {
        whereList.add(new Where(field, "BETWEEN", valArr));
        return this;
    }

    public QuerySet whereOr(String field, String operator, String value)
    {
        whereList.add(new WhereOr(field, operator, value));
        return this;
    }

    public QuerySet whereOr(String field, String value)
    {
        return this.whereOr(field, "=", value);
    }

    public QuerySet join(String table, String condition) {
        return join(table, condition, "INNER");
    }

    public QuerySet join(String table, String condition, String type)
    {
        joinList.add(new String[]{table, condition, type});
        return this;
    }

    public QuerySet group(String fields)
    {
        groupBy = fields; //field.split(",");
        return this;
    }

    public QuerySet order(String field, String sort)
    {
        orderByList.add(String.format("`%s` %s", field, sort.toUpperCase()));
        return this;
    }

    public QuerySet order(String field, boolean asc) {
        orderByList.add(String.format("`%s` %s", field, asc ? "ASC" : "DESC"));
        return this;
    }

    public QuerySet order(String field)
    {
        if (field.contains(",")) {
            return order(Arrays.asList(field.split(",")));
        }
        String[] arr = field.split(" ");
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
        this.limitIndex = index;
        this.limitSize = count;
        return this;
    }

    public QuerySet page(Integer page) {
        return page(page, limitSize);
    }

    public QuerySet page(Integer page, Integer size) {
        return limit((page - 1) * size, size);
    }

    public QuerySet field(String fields)
    {
        field(fields.split(","));
        return this;
    }

    public QuerySet field(List<String> fields)
    {
        field(fields.toArray(new String[0]));
        return this;
    }

    public QuerySet field(String[] fieldArr) {
        for (String item : fieldArr) {
            String[] arr = item.split("\\.");
            if (arr.length == 2) {
                this.fieldList.add(new Field(arr[0], arr[1]).toString());
            } else {
                this.fieldList.add(new Field(arr[0]).toString());
            }
        }
        return this;
    }

    public List<Map<String, Object>> select()
    {
        List<Map<String, Object>> resultSet = drive.executeSql(this);
        return ObjectUtil.isEmpty(resultSet) ? new LinkedList<>() : resultSet;
    }

    public <T> List<T> select(Class<T> clazz) {
        List<Map<String, Object>> resultSet = select();
        return toBeanList(clazz, resultSet);
    }

    public Map<String, Object> find()
    {
        limit(0, 1);
        List<Map<String, Object>> resultSet = select();
        return ObjectUtil.isEmpty(resultSet) ? null : resultSet.get(0);
    }

    public <T> T find(Class<T> clazz)
    {
        Map<String, Object> result = find();
        return result == null ? null : BeanUtil.mapToBeanIgnoreCase(result, clazz, true);
    }

    public QuerySet data(String field, String value)
    {
        this.data.put(field, value);
        return this;
    }

    public QuerySet data(Map<String, String> data)
    {
        this.data.putAll(data);
        return this;
    }

    public Map<String, Object> data()
    {
        return this.data;
    }

    public Integer update(Map<String, Object> data)
    {
        action = Action.UPDATE;
        this.data.putAll(data);
        drive.executeSql(this);
        return null;
    }

    public Integer update()
    {
        this.action = Action.UPDATE;
        this.drive.executeSql(this);
        return 1;
    }

    public Long insert(Map<String, Object> data)
    {
        this.action = Action.INSERT;
        this.data.putAll(data);
        return this.drive.executeSql(this);
    }

    public Integer insertAll(List<DataMap> dataList)
    {
        action = Action.INSERT;
        this.dataList.clear();
        this.dataList.addAll(dataList);
        return drive.executeSql(this);
    }

    public Integer delete()
    {
        if (whereList.size() == 0) {
            throw new QueryException("不允许空条件的删除执行");
        }
        this.action = Action.DELETE;
        Long result = drive.executeSql(this);
        return result.intValue();
    }

    /**
     * 获取表全部字段
     * */
    public List<TableColumn> getColumns() {
        return getColumns(table);
    }

    public List<TableColumn> getColumns(String table)
    {
        List<TableColumn> ret = SqlUtil.getTableColumns(table);
        if (ret != null) {
            return ret;
        }
        String sql = "SHOW FULL COLUMNS FROM `"+table+"`";
        List<Map<String, String>> columns = (List<Map<String, String>>) this.executeSql(sql);
        if (SqlUtil.isEmpty(columns)) {
            return new LinkedList<>();
        }
        List<TableColumn> columns1 = new LinkedList<>();
        columns.forEach(info -> columns1.add(new TableColumn(info)));
        SqlUtil.setTableColumns(table, columns1);
        return columns1;
    }

    // 获取表主键字段
    public String getColumnPk() {
        List<TableColumn> columns = getColumns();
        for (TableColumn column : columns) {
            if ("PRI".equals(column.Key))
                return column.Field;
        }
        return null;
    }

    public String fetchSql()
    {
        return this.drive.pretreatment(this);
    }

    public Object executeSql(String sql)
    {
        return drive.executeSql(Action.SELECT, sql);
    }

    //TODO::--------------- 增强方法 ---------------
    // 分页查询
    public Pagination<Map<String, Object>> pagination()
    {
        List<String> cacheFiledList = new LinkedList<>(fieldList);
        // 获取总数
        fieldList.clear();
        Integer total = count();
        // 执行查询
        fieldList.clear();
        fieldList.addAll(cacheFiledList);
        List<Map<String, Object>> rows = select();
        // 返回分页
        int page = limitIndex / limitSize + 1;
        return new Pagination<>(page, limitSize, total, rows);
    }

    public Pagination<Map<String, Object>> pagination(Integer page, Integer pageSize) {
        this.page(page, pageSize);
        return pagination();
    }

    public Pagination<Map<String, Object>> pagination(Integer page) {
        return this.pagination(page, 20);
    }

    public <T> Pagination<T> pagination(Class<T> clazz, Integer page, Integer pageSize)
    {
        Pagination<Map<String, Object>> pag = pagination(page, pageSize);
        return new Pagination<T>(pag.page, pag.pageSize, pag.total, toBeanList(clazz, pag.rows));
    }

    public <T> Pagination<T> pagination(Class<T> clazz)
    {
        Pagination<Map<String, Object>> pag = pagination();
        return new Pagination<T>(pag.page, pag.pageSize, pag.total, toBeanList(clazz, pag.rows));
    }

    //TODO::--------------- 统计方法 ---------------
    public Integer count() {
        return count("COUNT(*)");
    }

    public Integer count(String field) {
        fieldList.add(field);
        List<Map<String, Object>> resultSet = drive.executeSql(this);
        return Math.toIntExact((Long) resultSet.get(0).get(field));
    }


    //TODO::--------------- 助手方法 ---------------
    public static<T> List<T> toBeanList(Class<T> clazz, List<Map<String, Object>> resultSet) {
        if (resultSet == null) {
            return new LinkedList<>();
        }
        List<T> beanList = new LinkedList<>();
        resultSet.forEach(row -> beanList.add(BeanUtil.toBeanIgnoreCase(row, clazz, true)));
        return beanList;
    }

    public static<T> T toBean(Class<T> clazz, Map<String, Object> row) {
        if (row == null) {
            return null;
        }
        return BeanUtil.toBeanIgnoreCase(row, clazz, true);
    }

    //TODO::--------------- 事务操作方法 ---------------
    /**
     * 开启事务
     * */
    public static void startTrans() {
        startTrans(connect());
    }

    public static void startTrans(QuerySet querySet) {
        startTrans(querySet.drive);
    }

    public static void startTrans(Drive drive) {
        drive.startTrans();
    }

    /**
     * 提交事务
     * */
    public static void commit() {
        commit(connect());
    }

    public static void commit(QuerySet querySet) {
        commit(querySet.drive);
    }

    public static void commit(Drive drive) {
        drive.commit();
        drive.setAutoCommit(true);
    }

    /**
     * 事务回滚
     * */
    public static void rollback() {
        rollback(connect());
    }

    public static void rollback(QuerySet querySet) {
        rollback(querySet.drive);
    }

    public static void rollback(Drive drive) {
        drive.rollback();
        drive.setAutoCommit(true);
    }

    /**
     * 回调方法中执行事务
     * */
    public static void transaction(TransactionCallback callback)
    {
        startTrans();
        try {
            callback.call();
            commit();
        } catch (Throwable th){
            rollback();
        }
    }

    public static void transaction(QuerySet querySet, TransactionCallback callback)
    {
        startTrans(querySet);
        try {
            callback.call();
            commit(querySet);
        } catch (Throwable th){
            rollback(querySet);
        }
    }

    public interface TransactionCallback {
        void call();
    }


    //TODO::--------------- 类自用方法 ---------------
    public List<WhereBase> __WhereList() {
        return this.whereList;
    }

    public Action __Action() {
        return this.action;
    }

    public List<String> __FieldList() {
        if (fieldList.size() == 0)
            field("*");
        return fieldList;
    }

    public Field __Table() {
        return new Field(this.table);
    }

    public Integer __Limit() {
        return limitIndex;
    }

    public Integer __LimitSize() {
        return limitSize;
    }

    public List<String> __Orders() {
        return orderByList;
    }

    public Map<String, Object> __Data()
    {
        return data;
    }

    public List<Map<String, Object>> __DataList() {
        return dataList;
    }

    public List<String[]> __JoinList() {
        return joinList;
    }
}
