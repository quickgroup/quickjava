package org.quickjava.orm.wrapper.conditions;

import org.quickjava.orm.Model;
import org.quickjava.orm.wrapper.MFunction;
import org.quickjava.orm.wrapper.enums.JoinConditionType;

import java.util.LinkedList;
import java.util.List;

// 根model绑定条件
public class JoinConditionBasic<Children extends JoinConditionBasic<Children>> {

    public final List<Item<?, ?>> items = new LinkedList<>();

    public Children chain() {
        return (Children) this;
    }

    protected <L extends Model, R extends Model> Children eq(Class<L> left, MFunction<L, ?> lf, Class<R> right, MFunction<R, ?> rf) {
        return add(JoinConditionType.EQ, left, lf, right, rf);
    }

    protected <L extends Model, R extends Model> Children add(JoinConditionType type, Class<L> left, MFunction<L, ?> lf, MFunction<R, ?> rf) {
        items.add(new Item<>(type, left, lf, rf));
        return chain();
    }

    protected <L extends Model, R extends Model> Children add(JoinConditionType type, Class<L> left, MFunction<L, ?> lf, Class<R> right, MFunction<R, ?> rf) {
        items.add(new Item<>(type, left, lf, right, rf));
        return chain();
    }

    public static class Item<L extends Model, R extends Model> {
        JoinConditionType type;
        // 加载左表数据
        boolean loadLeftData = true;
        // 左表别名
        String leftAlias;
        Class<? extends Model> left;
        MFunction<L, ?> leftFun;
        // 右表别名
        String rightAlias;
        Class<? extends Model> right;
        MFunction<R, ?> rightFun;

        public Item(JoinConditionType type, Class<? extends Model> left, MFunction<L, ?> leftFun, Class<? extends Model> right, MFunction<R, ?> rightFun) {
            this.type = type;
            this.left = left;
            this.leftFun = leftFun;
            this.right = right;
            this.rightFun = rightFun;
        }

        public Item(JoinConditionType type, Class<? extends Model> left, MFunction<L, ?> leftFun, MFunction<R, ?> rightFun) {
            this.type = type;
            this.left = left;
            this.leftFun = leftFun;
            this.rightFun = rightFun;
        }

        public JoinConditionType getType() {
            return type;
        }

        public void setType(JoinConditionType type) {
            this.type = type;
        }

        public boolean isLoadLeftData() {
            return loadLeftData;
        }

        public void setLoadLeftData(boolean loadLeftData) {
            this.loadLeftData = loadLeftData;
        }

        public String getLeftAlias() {
            return leftAlias;
        }

        public void setLeftAlias(String leftAlias) {
            this.leftAlias = leftAlias;
        }

        public Class<? extends Model> getLeft() {
            return left;
        }

        public void setLeft(Class<? extends Model> left) {
            this.left = left;
        }

        public MFunction<L, ?> getLeftFun() {
            return leftFun;
        }

        public void setLeftFun(MFunction<L, ?> leftFun) {
            this.leftFun = leftFun;
        }

        public String getRightAlias() {
            return rightAlias;
        }

        public void setRightAlias(String rightAlias) {
            this.rightAlias = rightAlias;
        }

        public Class<? extends Model> getRight() {
            return right;
        }

        public void setRight(Class<? extends Model> right) {
            this.right = right;
        }

        public MFunction<R, ?> getRightFun() {
            return rightFun;
        }

        public void setRightFun(MFunction<R, ?> rightFun) {
            this.rightFun = rightFun;
        }
    }

}
