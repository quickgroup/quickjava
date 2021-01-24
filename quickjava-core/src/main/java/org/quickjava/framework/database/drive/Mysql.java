package org.quickjava.framework.database.drive;

import org.quickjava.framework.bean.DbWhere;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Qlo1062-(QloPC-zs)
 * @date 2021/1/19 10:17
 */
public class Mysql extends DriveBase<Mysql> {

    /**
     * @langCn Where查询条件
     */
    List<DbWhere> dbWheres = new ArrayList<>();

    public static Mysql table(String table)
    {
        return DriveBase.table(Mysql.class, table);
    }

    public static DriveBase name(String name)
    {
        return DriveBase.name(Mysql.class, name);
    }

    @Override
    public Mysql where(String field, String operator, String value) {
        return null;
    }

    @Override
    public Mysql where(String field, String value) {
        return null;
    }

    @Override
    public Mysql whereOr(String field, String operator, String value) {
        return null;
    }

    @Override
    public Mysql whereOr(String field, String value) {
        return null;
    }

    @Override
    public Mysql group(String field) {
        return null;
    }

    @Override
    public Mysql order(String field, String sort) {
        return null;
    }

    @Override
    public Mysql limit(Integer count) {
        return null;
    }

    @Override
    public Mysql page(Integer page) {
        return null;
    }

    @Override
    public Mysql page(Integer page, Integer count) {
        return null;
    }

    @Override
    public Map<String, String> select(Map<String, String> data) {
        return null;
    }

    @Override
    public Map<String, String> select() {
        return null;
    }

    @Override
    public Map<String, String> find(Map<String, String> data) {
        return null;
    }

    @Override
    public Map<String, String> find() {
        return null;
    }

    @Override
    public String fetchSql() {
        return null;
    }

    @Override
    public Map<String, String> executeSql(String sql) {
        return null;
    }

    @Override
    public Mysql data(String field, String value) {
        return null;
    }

    @Override
    public Mysql data(String field) {
        return null;
    }

    @Override
    public Mysql setData(Map<String, String> data) {
        return null;
    }

    @Override
    public Map<String, String> getData(Map<String, String> data) {
        return null;
    }

    @Override
    public Integer update() {
        return null;
    }

    @Override
    public Integer insert() {
        return null;
    }
}
