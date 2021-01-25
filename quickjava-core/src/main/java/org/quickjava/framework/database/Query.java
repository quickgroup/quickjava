/*
 * Copyright by QuickJava
 */

package org.quickjava.framework.database;

import org.quickjava.framework.bean.DbConfig;
import org.quickjava.framework.bean.DbWhere;
import org.quickjava.framework.database.drive.Drive;
import org.quickjava.framework.database.drive.Mysql;

import java.util.*;

/**
 * @author Qlo1062-(QloPC-zs)
 * @date 2021/1/19 10:18
 */
public class Query {

    private DbConfig dbConfig = null;

    private Drive drive = new Mysql();

    private String table = null;

    private String action = "SELECT";

    public enum Action {
        SELECT,
        UPDATE,
        DELETE,
        INSERT,
    }

    private List<DbWhere> whereList = new LinkedList<>();

    private List<DbWhere> whereOrList = new LinkedList<>();

    private Map<String, String> dataMap = new LinkedHashMap<>();

    private String groupByField = null;

    private Map<String, String> orderByMap = new LinkedHashMap<>();

    private Integer limitIndex = 0;

    private Integer limitCount = 20;

    public Query(String table) {
        this.table = table;
    }

    public static Query table(String table)
    {
        return new Query(table);
    }

    public Query where(String field, String operator, String value)
    {
        field = String.format("`%s`", field);
        value = String.format("\"%s\"", value);
        this.whereList.add(new DbWhere(field, operator, value));
        return this;
    }

    public Query where(String field, String value)
    {
        return this.where(field, "=", value);
    }

    public Query whereOr(String field, String operator, String value)
    {
        field = String.format("`%s`", field);
        value = String.format("\"%s\"", value);
        this.whereOrList.add(new DbWhere(field, operator, value));
        return this;
    }

    public Query whereOr(String field, String value)
    {
        return this.whereOr(field, "=", value);
    }

    public Query group(String field)
    {
        this.groupByField = field;
        return this;
    }

    public Query order(String field, String sort)
    {
        field = String.format("`%s`", field);
        this.orderByMap.put(field, sort);
        return this;
    }

    public Query limit(Integer index, Integer count)
    {
        this.limitCount = count;
        this.limitIndex = index;
        return this;
    }

    public Query page(Integer page)
    {
        this.limitIndex = page * this.limitCount;
        return this;
    }

    public Query page(Integer page, Integer count)
    {
        this.limitCount = count;
        this.limitIndex = page * this.limitCount;
        return this;
    }

    public List<Map> select()
    {
        return this.drive.executeSql(this.drive.buildSql(this));
    }

    public Map find()
    {
        List<Map> result = this.drive.executeSql(this.drive.buildSql(this));
        return (result == null || result.size() == 0) ? null : result.get(0);
    }

    public String fetchSql()
    {
        return this.drive.buildSql(this);
    }

    public List<Map> executeSql(String sql)
    {
        return this.drive.executeSql(sql);
    }

    public Query addData(String field, String value)
    {
        return this;
    }

    public Query setData(Map<String, String> data)
    {
        return this;
    }

    public Map<String, String> getData()
    {
        return this.dataMap;
    }

    public Integer update(Map<String, String> data)
    {
        return null;
    }

    public Integer update()
    {
        return null;
    }

    public Integer insert(Map<String, String> data)
    {
        return null;
    }

    public Integer insert()
    {
        return null;
    }

    public Integer delete(Map<String, String> data)
    {
        return null;
    }

    public Integer delete()
    {
        return null;
    }

}
