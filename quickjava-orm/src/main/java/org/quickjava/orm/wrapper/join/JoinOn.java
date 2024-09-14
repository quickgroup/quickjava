package org.quickjava.orm.wrapper.join;/*
 * Copyright (c) 2020~2024 http://www.quickjava.org All rights reserved.
 * +-------------------------------------------------------------------
 * Organization: QuickJava
 * +-------------------------------------------------------------------
 * Author: Qlo1062
 * +-------------------------------------------------------------------
 * File: JoinModelWhere
 * +-------------------------------------------------------------------
 * Date: 2024/9/13 18:14
 * +-------------------------------------------------------------------
 * License: Apache Licence 2.0
 * +-------------------------------------------------------------------
 */

import org.quickjava.orm.query.QuerySetHelper;
import org.quickjava.orm.query.build.Where;
import org.quickjava.orm.wrapper.MFunction;
import org.quickjava.orm.wrapper.ModelWhere;

import java.util.Arrays;
import java.util.List;

public class JoinOn<M, Left, Right> extends ModelWhere<JoinOn<M, Left, Right>, M, MFunction<M, ?>> {

    protected JoinOn<?, Left, Right> base;
    protected JoinOn<Left, Left, Right> left;
    protected JoinOn<Right, Left, Right> right;
    protected Class<M> mainClass;
    protected Class<Left> leftClass;
    protected String leftAlias;
    protected Class<Right> rightClass;
    protected String rightAlias;
    // 加载数据的列
    protected List<String> dataFields;

    public JoinOn() {
    }

    public JoinOn(Class<M> mainClass, Class<Left> leftClass, Class<Right> rightClass) {
        this.mainClass = mainClass;
        this.leftClass = leftClass;
        this.rightClass = rightClass;
    }

    public JoinOn(Class<Left> leftClass, Class<Right> rightClass) {
        this.mainClass = (Class<M>) leftClass;
        this.leftClass = leftClass;
        this.rightClass = rightClass;
    }

    public JoinOn<M, Left, Right> setMainClass(Class<M> mainClass) {
        this.mainClass = mainClass;
        return this;
    }

    @Override
    public JoinOn<M, Left, Right> where(Where where) {
        base().where(where);
        return chain();
    }

    private JoinOn<M, Left, Right> setBase(JoinOn<?, Left, Right> base) {
        this.base = base;
        return chain();
    }

    private JoinOn<M, Left, Right> setLeftClass(Class<Left> leftClass) {
        this.leftClass = leftClass;
        return chain();
    }

    public JoinOn<M, Left, Right> setLeftAlias(String leftAlias) {
        this.leftAlias = leftAlias;
        return chain();
    }

    private JoinOn<M, Left, Right> setRightClass(Class<Right> rightClass) {
        this.rightClass = rightClass;
        return chain();
    }

    public JoinOn<M, Left, Right> setRightAlias(String rightAlias) {
        this.rightAlias = rightAlias;
        return chain();
    }

    public Class<M> getMainClass() {
        return mainClass;
    }

    public Class<Left> getLeftClass() {
        return leftClass;
    }

    public String getLeftAlias() {
        return leftAlias;
    }

    public Class<Right> getRightClass() {
        return rightClass;
    }

    public String getRightAlias() {
        return rightAlias;
    }

    /**
     * 加载右表数据
     */
    @SafeVarargs
    public final JoinOn<M, Left, Right> rightField(MFunction<Right, ?>... fields) {
        JoinOn<?, Left, Right> base = base();
        base.dataFields = QuerySetHelper.initList(base.dataFields);
        for (MFunction<Right, ?> field : fields) {
            base.dataFields.add(field.getName());
        }
        return chain();
    }

    public final JoinOn<M, Left, Right> rightField(String... fields) {
        JoinOn<?, Left, Right> base = base();
        base.dataFields = QuerySetHelper.initList(base.dataFields);
        base.dataFields.addAll(Arrays.asList(fields));
        return chain();
    }


    // TODO::-------------------- 操作方法 --------------------
    public JoinOn<?, Left, Right> base() {
        return base == null ? this : base.base();
    }

    public JoinOn<Left, Left, Right> left() {
        JoinOn<?, Left, Right> base = base();
        if (base.left == null) {
            base.left = new JoinOn<Left, Left, Right>(leftClass, leftClass, rightClass).setBase(base);
        }
        return base.left;
    }

    public JoinOn<Right, Left, Right> right() {
        JoinOn<?, Left, Right> base = base();
        if (base.right == null) {
            base.right = new JoinOn<Right, Left, Right>(rightClass, leftClass, rightClass).setBase(base);
        }
        return base.right;
    }


    // TODO::-------------------- Join两表条件 --------------------
    public JoinOn<M, Left, Right> eq(MFunction<Left, ?> lf, MFunction<Right, ?> rf) {
        return chain();
    }

    public JoinOn<M, Left, Right> neq(MFunction<Left, ?> lf, MFunction<Right, ?> rf) {
        return chain();
    }
}
