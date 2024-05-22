package org.quickjava.orm.wrapper.join;

import org.quickjava.orm.enums.LogicType;
import org.quickjava.orm.model.Model;
import org.quickjava.orm.wrapper.MFunction;
import org.quickjava.orm.enums.CompareType;

import java.util.LinkedList;
import java.util.List;

// 根model绑定条件
public class JoinSpecifyBase<Children extends JoinSpecifyBase<Children, Left>, Left extends Model> {

    // 关联模型
    protected Class<Left> left;

    // 关联模型的别名（查询表别名和在父实体的属性名
    protected String leftAlias;

    // 加载left表数据到right模型属性上
    protected String loadDataField = null;

    // join-on关联条件
    public final List<Item<?, ?>> onList = new LinkedList<>();

    public JoinSpecifyBase(Class<Left> left, String leftAlias) {
        this.left = left;
        this.leftAlias = leftAlias;
    }

    public JoinSpecifyBase(Class<Left> left) {
        this.left = left;
    }

    public Children chain() {
        return (Children) this;
    }

    protected <Right extends Model> Children eq(MFunction<Left, ?> lf, Class<Right> right, MFunction<Right, ?> rf) {
        return where(CompareType.EQ, lf, right, rf);
    }

    public Children eq(MFunction<Left, ?> lf, Object val) {
        return where(CompareType.EQ, lf, val);
    }

    protected <Right extends Model> Children neq(MFunction<Left, ?> lf, Class<Right> right, MFunction<Right, ?> rf) {
        return where(CompareType.NEQ, lf, right, rf);
    }

    public Children neq(MFunction<Left, ?> lf, Object val) {
        return where(CompareType.NEQ, lf, val);
    }

    public Children in(MFunction<Left, ?> lf, Object val) {
        return where(CompareType.IN, lf, val);
    }

    public Children notIn(MFunction<Left, ?> lf, Object val) {
        return where(CompareType.NOT_IN, lf, val);
    }

    public Children isNull(MFunction<Left, ?> lf) {
        return where(CompareType.IS_NULL, lf, null);
    }

    public Children isNotNull(MFunction<Left, ?> lf) {
        return where(CompareType.IS_NOT_NULL, lf, null);
    }

    public Children between(MFunction<Left, ?> lf, Object val) {
        return where(CompareType.BETWEEN, lf, val);
    }

    protected <Right extends Model> Children where(CompareType type, MFunction<Left, ?> lf, MFunction<Right, ?> rf) {
        onList.add(new Item<>(type, lf, rf));
        return chain();
    }

    protected Children where(CompareType type, MFunction<Left, ?> lf, Object value) {
        onList.add(new Item<>(type, lf, value));
        return chain();
    }

    public Children raw(String sql) {
        onList.add(new Item<>(CompareType.RAW, null, sql));
        return chain();
    }

    protected <Right extends Model> Children where(CompareType type, MFunction<Left, ?> lf, Class<Right> right, MFunction<Right, ?> rf) {
        onList.add(new Item<>(type, lf, right, rf));
        return chain();
    }

    public static class Item<L extends Model, R extends Model> {
        // 逻辑类型
        private LogicType logic = LogicType.AND;
        // 条件运算符
        private final CompareType type;
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

        public Item(LogicType logic, CompareType type, MFunction<L, ?> leftFun, Class<? extends Model> right, MFunction<R, ?> rightFun) {
            this.logic = logic;
            this.type = type;
            this.leftFun = leftFun;
            this.right = right;
            this.rightFun = rightFun;
        }

        public Item(CompareType type, MFunction<L, ?> leftFun, Class<? extends Model> right, MFunction<R, ?> rightFun) {
            this.type = type;
            this.leftFun = leftFun;
            this.right = right;
            this.rightFun = rightFun;
        }

        public Item(CompareType type, MFunction<L, ?> leftFun, Object rightValue) {
            this.type = type;
            this.leftFun = leftFun;
            this.rightValue = rightValue;
        }

        public Item(CompareType type, MFunction<L, ?> leftFun, MFunction<R, ?> rightFun) {
            this.type = type;
            this.leftFun = leftFun;
            this.rightFun = rightFun;
        }

        public CompareType getType() {
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

    public boolean isLoadData() {
        return loadDataField != null;
    }

    // 加载数据到主表
    public<Right extends Model> Children setLoadData(MFunction<Right, ?> rightField) {
        this.loadDataField = rightField.getName();
        return chain();
    }

    public String getLoadDataField() {
        return loadDataField;
    }

    public List<Item<?, ?>> getOnList() {
        return onList;
    }
}
