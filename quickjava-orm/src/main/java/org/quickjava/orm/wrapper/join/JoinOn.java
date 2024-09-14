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

import org.quickjava.orm.enums.Logic;
import org.quickjava.orm.model.ModelHelper;
import org.quickjava.orm.model.contain.ModelMeta;
import org.quickjava.orm.query.QuerySetHelper;
import org.quickjava.orm.query.build.Where;
import org.quickjava.orm.query.enums.Operator;
import org.quickjava.orm.utils.SqlUtil;
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
    // 加载数据
    protected String dataFieldName;
    protected List<String> dataRightFields;

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
        if (base == null) {
            super.where(where);
        } else {
            base.where(where);
        }
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
    public final JoinOn<M, Left, Right> rightField(MFunction<Left, ?> mField, MFunction<Right, ?>... fields) {
        JoinOn<?, Left, Right> base = base();
        base.dataFieldName = mField == null ? null : mField.getFieldName();
        base.dataRightFields = QuerySetHelper.initList(base.dataRightFields);
        for (MFunction<Right, ?> field : fields) {
            base.dataRightFields.add(field.name());
        }
        return chain();
    }

    @SafeVarargs
    public final JoinOn<M, Left, Right> rightField(MFunction<Right, ?>... fields) {
        return rightField(null, fields);
    }

    public final JoinOn<M, Left, Right> rightField(MFunction<Left, ?> mField, String... fields) {
        return rightField(mField == null ? null : mField.getFieldName(), fields);
    }

    public final JoinOn<M, Left, Right> rightField(String mField, String... fields) {
        JoinOn<?, Left, Right> base = base();
        base.dataFieldName = mField;
        base.dataRightFields = QuerySetHelper.initList(base.dataRightFields);
        base.dataRightFields.addAll(Arrays.asList(fields));
        return chain();
    }

    public final JoinOn<M, Left, Right> rightField(String... fields) {
        rightField("", fields);
        base.dataFieldName = null;
        return chain();
    }

    public String getDataFieldName() {
        return dataFieldName;
    }

    public List<String> getDataRightFields() {
        return dataRightFields;
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
        return where(Operator.EQ, lf, rf);
    }

    public JoinOn<M, Left, Right> neq(MFunction<Left, ?> lf, MFunction<Right, ?> rf) {
        return where(Operator.NEQ, lf, rf);
    }

    public JoinOn<M, Left, Right> where(Operator operator, MFunction<Left, ?> lf, MFunction<Right, ?> rf) {
        ModelMeta leftMeta = ModelHelper.getMeta(getLeftClass()),
                rightMeta = ModelHelper.getMeta(getRightClass());
        String leftName = leftMeta == null ? getLeftClass().getSimpleName() : leftMeta.table();
        leftName = SqlUtil.isEmpty(getLeftAlias()) ? leftName : getLeftAlias();
        String rightName = rightMeta == null ? getRightClass().getSimpleName() : rightMeta.table();
        rightName = SqlUtil.isEmpty(getRightAlias()) ? rightName : getRightAlias();

        where(new Where(Logic.AND.num(), leftName, lf.name(), operator, rightName, rf.name()));
        return chain();
    }
}
