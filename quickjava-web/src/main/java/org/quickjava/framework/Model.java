/*
 * Copyright (c) 2020~2021 http://www.quickjava.org All rights reserved.
 * +-------------------------------------------------------------------
 * Org: QuickJava
 * +-------------------------------------------------------------------
 * Author: Qlo1062
 * +-------------------------------------------------------------------
 * File: Model.java
 * +-------------------------------------------------------------------
 * Date: 2021/06/17 14:47:17
 * +-------------------------------------------------------------------
 * License: Apache Licence 2.0
 * +-------------------------------------------------------------------
 *
 */

package org.quickjava.framework;

import org.quickjava.framework.database.Query;
import org.quickjava.framework.exception.QuickException;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.List;

@SuppressWarnings(value = {"unchecked"})
public class Model<T> {

    /**
     * @langCn 表名
     */
    public String table = null;

    /**
     * @langCn 主键，默认为“id”
     */
    public String pk = "id";

    private Query query = null;

    public Model() {
        if (isFromInit())
            return;
        this.initModel();
        this.query = new Query(table);
    }

    private boolean isFromInit()
    {
        try {
            throw new Exception("isFromReflect");
        } catch (Exception e) {
            int count = 0;
            for (StackTraceElement traceElement : e.getStackTrace()) {
                String initName = traceElement.getClassName() + "." + traceElement.getMethodName();
                if ((Model.class.getName() + ".<init>").equals(initName))
                    if (count++ >= 1)
                        return true;
            }
        }
        return false;
    }

    public void initModel()
    {
        try {
            // 本类信息
            Field[] thisFields = getClass().getDeclaredFields();
            // 子类
            Class childClazz = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
            Object child = childClazz.getDeclaredConstructor().newInstance();
            // 获取子类所有属性：表名、主键等等，覆盖到本类上，实现覆盖父类属性的实现
            Field[] fields = childClazz.getDeclaredFields();
            for (Field thisField : thisFields) {
                for (Field field : fields) {
                    if (thisField.getName().equals(field.getName())) {
                        field.setAccessible(true);
                        thisField.set(this, field.get(child));
                        break;
                    }
                }
            }

        } catch (Exception e) {
            throw new QuickException(e);
        }
    }

    public Class<T> getTClass() {
        return (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    /**
     * @langCn WHERE 条件
     * @param field
     * @param operator
     * @param value
     * @return
     */
    public T where(String field, String operator, String value)
    {
        query.where(field, operator, value);
        return (T) this;
    }

    public T find() {
        return (T) this.query.find();
    }

    public List<T> select() {
        return (List<T>) this.query.select();
    }
}

