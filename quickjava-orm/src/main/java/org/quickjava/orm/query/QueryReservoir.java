package org.quickjava.orm.query;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.quickjava.orm.query.callback.QuerySetCallback;
import org.quickjava.orm.query.contain.Action;
import org.quickjava.orm.contain.DriveConfigure;
import org.quickjava.orm.query.build.OrderBy;
import org.quickjava.orm.query.build.Where;
import org.quickjava.orm.utils.QuerySetHelper;

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
    public List<String> fieldList;

    @JsonIgnore
    public List<String[]> joinList;

    @JsonIgnore
    public List<String> unionList;

    @JsonIgnore
    public List<Where> whereList;

    @JsonIgnore
    public List<OrderBy> orderByList;

    @JsonIgnore
    public List<Map<String, Object>> dataList;

    @JsonIgnore
    public String groupBy;

    @JsonIgnore
    public String having;

    @JsonIgnore
    public Integer limitIndex;

    @JsonIgnore
    public Integer limitSize;

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

    public List<String> getFieldList() {
        this.fieldList = QuerySetHelper.initList(this.fieldList);
        return fieldList;
    }

    public void setFieldList(List<String> fieldList) {
        this.fieldList = fieldList;
    }

    public List<String[]> getJoinList() {
        joinList = QuerySetHelper.initList(joinList);
        return joinList;
    }

    public void setJoinList(List<String[]> joinList) {
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
        if (getDataList().size() == 0) {
            getDataList().add(new LinkedHashMap<>());
        }
        return getDataList().get(0);
    }

    public void setData(Map<String, Object> data) {
        getDataList().add(0, data);
    }

    public String getGroupBy() {
        return groupBy;
    }

    public void setGroupBy(String groupBy) {
        this.groupBy = groupBy;
    }

    public String getHaving() {
        return having;
    }

    public void setHaving(String having) {
        this.having = having;
    }

    public Integer getLimitIndex() {
        return limitIndex;
    }

    public void setLimitIndex(Integer limitIndex) {
        this.limitIndex = limitIndex;
    }

    public Integer getLimitSize() {
        return limitSize;
    }

    public void setLimitSize(Integer limitSize) {
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
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    @Override
    public String toString() {
        return "QueryReservoir{" +
                "table='" + table + '\'' +
                ", action=" + action +
                ", fieldList=" + fieldList +
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
