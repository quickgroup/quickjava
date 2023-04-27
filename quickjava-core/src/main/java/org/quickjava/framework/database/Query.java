/*
 * Copyright by QuickJava
 */

package org.quickjava.framework.database;

import org.quickjava.framework.bean.DbConfig;
import org.quickjava.framework.database.contain.Action;
import org.quickjava.framework.database.contain.Field;
import org.quickjava.framework.database.contain.Limit;
import org.quickjava.framework.database.drive.Drive;
import org.quickjava.framework.database.drive.Mysql;
import org.quickjava.framework.database.contain.Where;

import java.util.*;

/**
 * @author Qlo1062-(QloPC-zs)
 * @date 2021/1/19 10:18
 */
public class Query {

    private DbConfig dbConfig = null;

    private Drive drive = new Mysql();

    private String table = null;

    private String tableOriginal = null;

    private Action action = Action.SELECT;

    private List<Where> wheres = new LinkedList<>();

    private List<Where> whereOr = new LinkedList<>();

    private Map<String, Object> data = new LinkedHashMap<>();

    private String[] groupBy = null;

    private String order = null;

    private List<String> fields = new ArrayList<>();

    private Integer limitIndex = 0;

    private Integer limitCount = 20;    //App.config.get("app").getInteger("pageSize", 20);

    public Query() {}

    public Query(String table) {
        this.table = this.tableOriginal = table;
    }

    public static Query table(String table)
    {
        return new Query(table);
    }

    public Query where(String field, Object value)
    {
        return this.where(field, "=", value);
    }

    public Query where(String field, String operator, Object value)
    {
        this.wheres.add(new Where(field, operator, value));
        return this;
    }

    public Query whereOr(String field, String operator, String value)
    {
        this.whereOr.add(new Where(field, operator, value));
        return this;
    }

    public Query whereOr(String field, String value)
    {
        return this.whereOr(field, "=", value);
    }

    public Query join(String joinTable)
    {
        join(joinTable, "", "LEFT");
        return this;
    }

    public Query join(String joinTable, String str12)
    {
        join(joinTable, str12, "LEFT");
        return this;
    }

    public Query join(String joinTable, String str12, String str3)
    {
        return this;
    }

    public Query group(String field)
    {
        this.groupBy = field.split(",");
        return this;
    }

    public Query order(String field, String sort)
    {
        this.order = String.format("`%s` %s", field, sort);
        return this;
    }

    public Query order(String order)
    {
        this.order = order;
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

    public Query field(String fields)
    {
        Field field = null;
        String[] fieldArray = fields.split(",");
        for (String item : fieldArray) {
            String[] itemArr = item.split(".");
            if (itemArr.length == 2) {
                field = new Field(itemArr[0], itemArr[1]);
            } else if (itemArr.length == 1){
                field = new Field(this.table, itemArr[0]);
            } else {
                field = new Field(this.table, item);
            }
        }
        if (field != null)
            this.fields.add(field.toString());

        return this;
    }

    public List<Map> select()
    {
        List<Map> result = (List<Map>) this.drive.executeSql(this.drive.pretreatment(this));
        return result;
    }

    public Map find()
    {
        limitIndex = 0;
        limitCount = 1;

        List<Map> result = (List<Map>) this.drive.executeSql(this.drive.pretreatment(this));
        return (result == null || result.size() == 0) ? null : result.get(0);
    }

    // 分页数据
    public Query pagination(Integer page, Integer count)
    {
        this.page(page, count);
        // 执行查询
        // 获取总数
        return this;
    }

    public String fetchSql()
    {
        return this.drive.pretreatment(this);
    }

    public Object executeSql(String sql)
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

    public Map<String, Object> getData()
    {
        return this.data;
    }

    public Integer update(Map<String, Object> data)
    {
        this.action = Action.UPDATE;
        this.data = data;
        this.drive.executeSql(this.drive.pretreatment(this));
        return null;
    }

    public Integer update()
    {
        return null;
    }

    public Long insert(Map<String, Object> data)
    {
        this.action = Action.INSERT;
        this.data = data;
        Long pkValue = (Long) this.drive.executeSql(this.drive.pretreatment(this));
        return pkValue;
    }

    public Integer insert()
    {
        return null;
    }

    public Integer insertAll(List<Map<String, Object>> values)
    {
        return null;
    }

    public Integer delete(Map<String, String> data)
    {
        return null;
    }

    public Integer delete()
    {
        this.action = Action.DELETE;
        Long result = (Long) this.drive.executeSql(this.drive.pretreatment(this));
        return result.intValue();
    }


    /***
     * 类自用函数
     * @hide
     */
    public List<Where> _getWheres() {
        return this.wheres;
    }

    /**
     * @hide
     */
    public Action _getAction() {
        return this.action;
    }

    public List<String> _getSelectField() {
        if (this.fields.size() == 0)
            this.field("*");
        return this.fields;
    }

    public Field _getTable() {
        return new Field(this.table);
    }

    public Limit _getLimit() {
        return new Limit(limitIndex, limitCount);
    }

    public String _getOrder() {
        return this.order;
    }

    public Map<String, Object> _getData()
    {
        return this.data;
    }
}
