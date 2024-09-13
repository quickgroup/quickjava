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

import org.quickjava.orm.query.build.Where;
import org.quickjava.orm.wrapper.MFunction;
import org.quickjava.orm.wrapper.ModelWhere;

public class JoinOn<M, Left, Right> extends ModelWhere<JoinOn<M, Left, Right>, M, MFunction<M, ?>> {

    private JoinOn<?, Left, Right> base;
    private JoinOn<Left, Left, Right> left;
    private JoinOn<Right, Left, Right> right;
    private Class<M> mainClass;
    private Class<Left> leftClass;
    protected String leftAlias;
    private Class<Right> rightClass;
    protected String rightAlias;

    public JoinOn() {
    }

    public JoinOn(Class<M> mainClass, Class<Left> leftClass, Class<Right> rightClass) {
        this.mainClass = mainClass;
        this.leftClass = leftClass;
        this.rightClass = rightClass;
    }

    public JoinOn<M, Left, Right> setMainClass(Class<M> mainClass) {
        this.mainClass = mainClass;
        return this;
    }

    public JoinOn<?, Left, Right> base() {
        return base == null ? this : base;
    }

    public JoinOn<Left, Left, Right> left() {
        if (base.left == null) {
            base.left = new JoinOn<Left, Left, Right>().setMainClass(leftClass);
            base.left.base = base;
        }
        return base.left;
    }

    public JoinOn<Right, Left, Right> right() {
        if (base.right == null) {
            base.right = new JoinOn<Right, Left, Right>().setMainClass(rightClass);
            base.right.base = base;
        }
        return base.right;
    }

    public JoinOn<M, Left, Right> on(MFunction<Left, ?> lf, MFunction<Right, ?> rf) {
        eq(lf, rf);
        return chain();
    }

    @Override
    public JoinOn<M, Left, Right> where(Where where) {
        base().where(where);
        return chain();
    }

    public JoinOn<M, Left, Right> setBase(JoinOn<?, Left, Right> base) {
        this.base = base;
        return chain();
    }

    public JoinOn<M, Left, Right> setLeft(JoinOn<Left, Left, Right> left) {
        this.left = left;
        return chain();
    }

    public JoinOn<M, Left, Right> setRight(JoinOn<Right, Left, Right> right) {
        this.right = right;
        return chain();
    }

    public JoinOn<M, Left, Right> setLeftClass(Class<Left> leftClass) {
        this.leftClass = leftClass;
        return chain();
    }

    public JoinOn<M, Left, Right> setLeftAlias(String leftAlias) {
        this.leftAlias = leftAlias;
        return chain();
    }

    public JoinOn<M, Left, Right> setRightClass(Class<Right> rightClass) {
        this.rightClass = rightClass;
        return chain();
    }

    public JoinOn<M, Left, Right> setRightAlias(String rightAlias) {
        this.rightAlias = rightAlias;
        return chain();
    }

    public JoinOn<Left, Left, Right> getLeft() {
        return left;
    }

    public JoinOn<Right, Left, Right> getRight() {
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
    public JoinOn<M, Left, Right> eq(MFunction<Left, ?> lf, MFunction<Right, ?> rf) {

        return chain();
    }
}
