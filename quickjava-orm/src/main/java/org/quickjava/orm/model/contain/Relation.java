package org.quickjava.orm.model.contain;

import org.quickjava.orm.model.IModel;
import org.quickjava.orm.model.enums.RelationType;

/*
 * Copyright (c) 2020~2023 http://www.quickjava.org All rights reserved.
 * +-------------------------------------------------------------------
 * Organization: QuickJava
 * +-------------------------------------------------------------------
 * Author: Qlo1062
 * +-------------------------------------------------------------------
 * File: Relation
 * +-------------------------------------------------------------------
 * Date: 2023-4-25 10:47
 * +-------------------------------------------------------------------
 * License: Apache Licence 2.0
 * +-------------------------------------------------------------------
 */
public class Relation {

    // 模型条件
    private IModel model;

    // 关联表类
    private Class<?> clazz;

    // 关联类型：一对一、一对多
    private RelationType type;

    // 本表字段
    private String localKey;

    // 关联表字段
    private String foreignKey;

    public Relation() {
    }

    public Relation(IModel model, Class<?> clazz, RelationType type, String localKey, String foreignKey) {
        this.model = model;
        this.clazz = clazz;
        this.type = type;
        this.localKey = localKey;
        this.foreignKey = foreignKey;
    }

    public RelationType getType() {
        return type;
    }

    public void setType(RelationType type) {
        this.type = type;
    }

    public String localKey() {
        return localKey;
    }

    public void setLocalKey(String localKey) {
        this.localKey = localKey;
    }

    public String foreignKey() {
        return foreignKey;
    }

    public void setForeignKey(String foreignKey) {
        this.foreignKey = foreignKey;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public void setClazz(Class<?> clazz) {
        this.clazz = clazz;
    }

    @Override
    public String toString() {
        return "Relation{" +
                "clazz=" + clazz +
                ", type=" + type +
                ", localKey='" + localKey + '\'' +
                ", foreignKey='" + foreignKey + '\'' +
                '}';
    }
}
