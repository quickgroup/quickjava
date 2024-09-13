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

import org.quickjava.orm.enums.CompareType;
import org.quickjava.orm.model.Model;
import org.quickjava.orm.query.build.Where;
import org.quickjava.orm.wrapper.MFunction;
import org.quickjava.orm.wrapper.ModelWhere;

public class JoinWhere<M, Left, Right> extends ModelWhere<JoinWhere<M, Left, Right>, M, MFunction<M, ?>> {

    private JoinWhere<?, Left, Right> base;
    private JoinWhere<Left, Left, Right> left;
    private JoinWhere<Right, Left, Right> right;
    private Class<M> mainClass;
    private Class<Left> leftClass;
    protected String leftAlias;
    private Class<Right> rightClass;
    protected String rightAlias;

    public JoinWhere() {
    }

    public JoinWhere(Class<M> mainClass, Class<Left> leftClass, Class<Right> rightClass) {
        this.mainClass = mainClass;
        this.leftClass = leftClass;
        this.rightClass = rightClass;
    }

    public JoinWhere<M, Left, Right> setMainClass(Class<M> mainClass) {
        this.mainClass = mainClass;
        return this;
    }

    public JoinWhere<?, Left, Right> base() {
        return base == null ? this : base;
    }

    public JoinWhere<Left, Left, Right> left() {
        if (base.left == null) {
            base.left = new JoinWhere<Left, Left, Right>().setMainClass(leftClass);
            base.left.base = base;
        }
        return base.left;
    }

    public JoinWhere<Right, Left, Right> right() {
        if (base.right == null) {
            base.right = new JoinWhere<Right, Left, Right>().setMainClass(rightClass);
            base.right.base = base;
        }
        return base.right;
    }

    @Override
    public JoinWhere<M, Left, Right> where(Where where) {
        base().where(where);
        return chain();
    }

    public JoinWhere<M, Left, Right> setBase(JoinWhere<?, Left, Right> base) {
        this.base = base;
        return chain();
    }

    public JoinWhere<M, Left, Right> setLeft(JoinWhere<Left, Left, Right> left) {
        this.left = left;
        return chain();
    }

    public JoinWhere<M, Left, Right> setRight(JoinWhere<Right, Left, Right> right) {
        this.right = right;
        return chain();
    }

    public JoinWhere<M, Left, Right> setLeftClass(Class<Left> leftClass) {
        this.leftClass = leftClass;
        return chain();
    }

    public JoinWhere<M, Left, Right> setLeftAlias(String leftAlias) {
        this.leftAlias = leftAlias;
        return chain();
    }

    public JoinWhere<M, Left, Right> setRightClass(Class<Right> rightClass) {
        this.rightClass = rightClass;
        return chain();
    }

    public JoinWhere<M, Left, Right> setRightAlias(String rightAlias) {
        this.rightAlias = rightAlias;
        return chain();
    }

    public JoinWhere<Left, Left, Right> getLeft() {
        return left;
    }

    public JoinWhere<Right, Left, Right> getRight() {
        return right;
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

    // TODO::-------------------- Join两表条件 --------------------
    public JoinWhere<M, Left, Right> eq(MFunction<Left, ?> lf, MFunction<Right, ?> rf) {
        return chain();
    }
}
