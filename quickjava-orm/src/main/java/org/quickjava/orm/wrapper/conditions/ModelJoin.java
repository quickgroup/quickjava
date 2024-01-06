package org.quickjava.orm.wrapper.conditions;

import org.quickjava.orm.Model;
import org.quickjava.orm.wrapper.MFunction;
import org.quickjava.orm.wrapper.enums.ConditionType;

import java.util.LinkedList;
import java.util.List;

// 根model绑定条件
public class ModelJoin<Children extends ModelJoin<Children, Left>, Left extends Model> {

    // 关联模型
    protected Class<Left> left;

    // 关联模型的别名（查询时相同表区分
    protected String leftAlias;

    // 数据对应属性
    private MFunction<?, ?> fieldFun;

    // 加载左表数据
    protected boolean loadLeftData = true;

    // 关联条件
    public final List<Item<?, ?>> items = new LinkedList<>();

    public ModelJoin(Class<Left> left, String leftAlias, MFunction<?, ?> fieldFun, boolean loadLeftData) {
        this.left = left;
        this.leftAlias = leftAlias;
        this.fieldFun = fieldFun;
        this.loadLeftData = loadLeftData;
    }

    public ModelJoin(Class<Left> left, String leftAlias, MFunction<?, ?> fieldFun) {
        this.left = left;
        this.leftAlias = leftAlias;
        this.fieldFun = fieldFun;
    }

    public ModelJoin(Class<Left> left) {
        this.left = left;
    }

    public Children chain() {
        return (Children) this;
    }

    protected <Right extends Model> Children eq(MFunction<Left, ?> lf, Class<Right> right, MFunction<Right, ?> rf) {
        return add(ConditionType.EQ, lf, right, rf);
    }

    protected <Right extends Model> Children add(ConditionType type, MFunction<Left, ?> lf, MFunction<Right, ?> rf) {
        items.add(new Item<>(type, lf, rf));
        return chain();
    }

    protected <Right extends Model> Children add(ConditionType type, MFunction<Left, ?> lf, Class<Right> right, MFunction<Right, ?> rf) {
        items.add(new Item<>(type, lf, right, rf));
        return chain();
    }

    public static class Item<L extends Model, R extends Model> {
        private final ConditionType type;
        // 左表别名
        private final MFunction<L, ?> leftFun;
        // 右表别名
        private String rightAlias;
        // 右表模型
        private Class<? extends Model> right;
        // 右表条件方法
        private MFunction<R, ?> rightFun;
        // 右表条件数据
        private Object rightValue;

        public Item(ConditionType type, MFunction<L, ?> leftFun, Class<? extends Model> right, MFunction<R, ?> rightFun) {
            this.type = type;
            this.leftFun = leftFun;
            this.right = right;
            this.rightFun = rightFun;
        }

        public Item(ConditionType type, MFunction<L, ?> leftFun, Class<? extends Model> right, Object rightValue) {
            this.type = type;
            this.leftFun = leftFun;
            this.right = right;
            this.rightValue = rightValue;
        }

        public Item(ConditionType type, MFunction<L, ?> leftFun, MFunction<R, ?> rightFun) {
            this.type = type;
            this.leftFun = leftFun;
            this.rightFun = rightFun;
        }

        public ConditionType getType() {
            return type;
        }

        public MFunction<L, ?> getLeftFun() {
            return leftFun;
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

        public Object getRightValue() {
            return rightValue;
        }

        public void setRightValue(Object rightValue) {
            this.rightValue = rightValue;
        }
    }

    public Class<Left> getLeft() {
        return left;
    }

    public Children setLeft(Class<Left> left) {
        this.left = left;
        return chain();
    }

    public String getLeftAlias() {
        return leftAlias;
    }

    public Children setLeftAlias(String leftAlias) {
        this.leftAlias = leftAlias;
        return chain();
    }

    public MFunction<?, ?> getFieldFun() {
        return fieldFun;
    }

    public Children setFieldFun(MFunction<?, ?> fieldFun) {
        this.fieldFun = fieldFun;
        return chain();
    }

    public boolean isLoadLeftData() {
        return loadLeftData;
    }

    public Children setLoadLeftData(boolean loadLeftData) {
        this.loadLeftData = loadLeftData;
        return chain();
    }

    public List<Item<?, ?>> getItems() {
        return items;
    }
}
