package org.quickjava.orm;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.quickjava.orm.callback.WhereOptCallback;
import org.quickjava.orm.contain.Action;
import org.quickjava.orm.contain.Where;
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

    @JsonIgnore
    public String table;

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
    public List<String> orderByList;

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
    public WhereOptCallback whereOptCallback;

    @JsonIgnore
    public Object whereOptCallbackData;

    @JsonIgnore
    public boolean fetchSql = false;

    @JsonIgnore
    public boolean printSql = false;

    @JsonIgnore
    public String sql = null;

    public String getTable() {
        return table;
    }

    public void setTable(String __table) {
        this.table = __table;
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
        return whereList;
    }

    public void setWhereList(List<Where> whereList) {
        this.whereList = whereList;
    }

    public List<String> getOrderByList() {
        orderByList = QuerySetHelper.initList(orderByList);
        return orderByList;
    }

    public void setOrderByList(List<String> orderByList) {
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

    public WhereOptCallback getWhereOptCallback() {
        return whereOptCallback;
    }

    public void setWhereOptCallback(WhereOptCallback whereOptCallback, Object userData) {
        this.whereOptCallback = whereOptCallback;
        this.whereOptCallbackData = userData;
    }

    public boolean isFetchSql() {
        return fetchSql;
    }

    public void setFetchSql(boolean fetchSql) {
        this.fetchSql = fetchSql;
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
                ", whereOptCallback=" + whereOptCallback +
                ", whereOptCallbackData=" + whereOptCallbackData +
                ", fetchSql=" + fetchSql +
                ", printSql=" + printSql +
                ", sql='" + sql + '\'' +
                '}';
    }
}
