package org.quickjava.orm.query;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.quickjava.orm.query.build.Join;
import org.quickjava.orm.query.build.TableColumn;
import org.quickjava.orm.query.callback.QuerySetCallback;
import org.quickjava.orm.query.contain.Action;
import org.quickjava.orm.contain.DriveConfigure;
import org.quickjava.orm.query.build.OrderBy;
import org.quickjava.orm.query.build.Where;
import org.quickjava.orm.query.contain.Label;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/*
 * Copyright (c) 2020~2023 http://www.quickjava.org All rights reserved.
 * +-------------------------------------------------------------------
 * Organization: QuickJava
 * +-------------------------------------------------------------------
 * Author: Qlo1062
 * +-------------------------------------------------------------------
 * File: QueryReservoir
 * +-------------------------------------------------------------------
 * Date: 2023/6/15 16:47
 * +-------------------------------------------------------------------
 * License: Apache Licence 2.0
 * +-------------------------------------------------------------------
 * QuerySet数据集合器
 */
public class QueryReservoir {

    /**
     * 表名
     */
    @JsonIgnore
    public String table;

    /**
     * 表查询别名
     */
    @JsonIgnore
    private String alias;

    @JsonIgnore
    public Action action;

    @JsonIgnore
    public List<TableColumn> columnList;

    @JsonIgnore
    public List<Join> joinList;

    @JsonIgnore
    public List<String> unionList;

    @JsonIgnore
    public List<Where> whereList;

    @JsonIgnore
    public List<OrderBy> orderByList;

    @JsonIgnore
    public List<Map<String, Object>> dataList;

    @JsonIgnore
    public List<TableColumn> groupBy;

    @JsonIgnore
    public List<TableColumn> having;

    @JsonIgnore
    public Long limitIndex;

    @JsonIgnore
    public Long limitSize;

    @JsonIgnore
    public boolean distinct = false;

    @JsonIgnore
    public boolean lock = false;

    @JsonIgnore
    public Map<Class<?>, Object> callbackMap;

    @JsonIgnore
    public Map<Class<?>, Object> callbackUserDataMap;

    @JsonIgnore
    public boolean fetchSql = false;

    @JsonIgnore
    public boolean printSql = false;

    @JsonIgnore
    public DriveConfigure driveConfigure = null;

    @JsonIgnore
    public String sql = null;

    @JsonIgnore
    public Map<Label, Object> labels;

    public static final QueryReservoir EMPTY = new QueryReservoir();

    public String getTable() {
        return table;
    }

    public String tableSql() {
        return alias == null ? table : (table.equals(alias) ? table : table + " " + alias);
    }

    public void setTable(String __table) {
        this.table = __table;
    }

    public String getAlias() {
        return alias == null ? table : alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public List<TableColumn> getColumnList() {
        this.columnList = QuerySetHelper.initList(this.columnList);
        return columnList;
    }

    public void setColumnList(List<TableColumn> columnList) {
        this.columnList = columnList;
    }

    public List<Join> getJoinList() {
        joinList = QuerySetHelper.initList(joinList);
        return joinList;
    }

    public void setJoinList(List<Join> joinList) {
        this.joinList = joinList;
    }

    public List<String> getUnionList() {
        unionList = QuerySetHelper.initList(unionList);
        return unionList;
    }

    public void setUnionList(List<String> unionList) {
        this.unionList = unionList;
    }

    public List<Where> getWhereList() {
        whereList = QuerySetHelper.initList(whereList);
        // 如果是join状态，加上表名前缀
        whereList.forEach(where -> {
            if (where.getTable() == null) {
                where.setTable(getAlias());
            }
        });
        return whereList;
    }

    public void setWhereList(List<Where> whereList) {
        this.whereList = whereList;
    }

    public List<OrderBy> getOrderByList() {
        orderByList = QuerySetHelper.initList(orderByList);
        return orderByList;
    }

    public void setOrderByList(List<OrderBy> orderByList) {
        this.orderByList = orderByList;
    }

    public List<Map<String, Object>> getDataList() {
        dataList = QuerySetHelper.initList(dataList);
        return dataList;
    }

    public void setDataList(List<Map<String, Object>> dataList) {
        this.dataList = dataList;
    }

    public Map<String, Object> getData() {
        if (getDataList().isEmpty()) {
            getDataList().add(new LinkedHashMap<>());
        }
        return getDataList().get(0);
    }

    public void setData(Map<String, Object> data) {
        getDataList().add(0, data);
    }

    public List<TableColumn> getGroupBy() {
        groupBy = QuerySetHelper.initList(groupBy);
        return groupBy;
    }

    public void setGroupBy(List<TableColumn> groupBy) {
        this.groupBy = groupBy;
    }

    public List<TableColumn> getHaving() {
        having = QuerySetHelper.initList(having);
        return having;
    }

    public void setHaving(List<TableColumn> having) {
        this.having = having;
    }

    public Long getLimitIndex() {
        return limitIndex;
    }

    public void setLimitIndex(Long limitIndex) {
        this.limitIndex = limitIndex;
    }

    public Long getLimitSize() {
        return limitSize;
    }

    public void setLimitSize(Long limitSize) {
        this.limitSize = limitSize;
    }

    public boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean getLock() {
        return lock;
    }

    public void setLock(boolean lock) {
        this.lock = lock;
    }

    public <D extends QuerySetCallback> void setCallback(Class<D> clazz, D callback, Object userData) {
        if (callbackMap == null) {
            callbackMap = new LinkedHashMap<>();
            callbackUserDataMap = new LinkedHashMap<>();
        }
        callbackMap.put(clazz, callback);
        callbackUserDataMap.put(clazz, userData);
    }

    public <D extends QuerySetCallback> D getCallback(Class<D> clazz) {
        if (callbackMap == null) {
            return null;
        }
        return (D) callbackMap.get(clazz);
    }

    public <D extends QuerySetCallback, V> V getCallbackUserData(Class<D> clazz) {
        if (callbackUserDataMap == null) {
            return null;
        }
        return (V) callbackUserDataMap.get(clazz);
    }

    public boolean isFetchSql() {
        return fetchSql;
    }

    public void setFetchSql(boolean fetchSql) {
        this.fetchSql = fetchSql;
    }

    public DriveConfigure getDriveConfigure() {
        return driveConfigure;
    }

    /**
     * 准备语句编译
     */
    public void pretreatment(DriveConfigure driveConfigure) {
        this.driveConfigure = driveConfigure;
        this.joinFill();
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public void setLabels(Map<Label, Object> labels) {
        this.labels = labels;
    }

    public Map<Label, Object> getLabels() {
        return labels;
    }

    public boolean labelContain(Label label) {
        return labels != null && getLabels().containsKey(label);
    }

    public QueryReservoir addLabel(Label label) {
        labels = labels == null ? new LinkedHashMap<>() : labels;
        if (!labels.containsKey(label)) {
            labels.put(label, null);
        }
        return this;
    }

    public <D extends TableColumn> void fillTable(boolean condition, List<D> columns) {
        if (condition) {
            columns.forEach(orderBy -> {
                orderBy.setTable(orderBy.getTable() == null ? getAlias() : orderBy.getTable());
            });
        }
    }

    public void joinFill() {
        if (joinList == null) {
            return;
        }
        if (columnList != null)
            fillTable(true, getColumnList());
        if (whereList != null)
            fillTable(true, getWhereList());
        if (groupBy != null)
            fillTable(true, getGroupBy());
        if (having != null)
            fillTable(true, getHaving());
        if (orderByList != null)
            fillTable(true, getOrderByList());
    }

    @Override
    public String toString() {
        return "QueryReservoir{" +
                "table='" + table + '\'' +
                ", action=" + action +
                ", columnList=" + columnList +
                ", joinList=" + joinList +
                ", unionList=" + unionList +
                ", whereList=" + whereList +
                ", orderByList=" + orderByList +
                ", dataList=" + dataList +
                ", groupBy='" + groupBy + '\'' +
                ", having='" + having + '\'' +
                ", limitIndex=" + limitIndex +
                ", limitSize=" + limitSize +
                ", distinct=" + distinct +
                ", lock=" + lock +
                ", fetchSql=" + fetchSql +
                ", printSql=" + printSql +
                ", sql='" + sql + '\'' +
                '}';
    }
}
