package org.quickjava.framework.database.drive;

import java.util.Map;

/**
 * @author Qlo1062-(QloPC-zs)
 * @date 2021/1/19 10:18
 */
public abstract class DriveBase<T> {



    public static<T> T table(Class<T> clazz, String table)
    {
        return null;
    }

    public static<T> T name(Class<T> clazz, String name)
    {
        return null;
    }

    public abstract T where(String field, String operator, String value);

    public abstract T where(String field, String value);

    public abstract T whereOr(String field, String operator, String value);

    public abstract T whereOr(String field, String value);

    public abstract T group(String field);

    public abstract T order(String field, String sort);

    public abstract T limit(Integer count);

    public abstract T page(Integer page);

    public abstract T page(Integer page, Integer count);

    public abstract Map<String, String> select(Map<String, String> data);

    public abstract Map<String, String> select();

    public abstract Map<String, String> find(Map<String, String> data);

    public abstract Map<String, String> find();

    public abstract String fetchSql();

    public abstract Map<String, String> executeSql(String sql);

    public abstract T data(String field, String value);

    public abstract T data(String field);

    public abstract T setData(Map<String, String> data);

    public abstract Map<String, String> getData(Map<String, String> data);

    public abstract Integer update();

    public abstract Integer insert();

}
