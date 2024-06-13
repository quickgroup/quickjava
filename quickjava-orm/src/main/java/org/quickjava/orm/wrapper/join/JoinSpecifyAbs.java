package org.quickjava.orm.wrapper.join;

import org.quickjava.orm.enums.CompareType;
import org.quickjava.orm.enums.LogicType;
import org.quickjava.orm.model.Model;
import org.quickjava.orm.wrapper.MFunction;

import java.util.LinkedList;
import java.util.List;

// 根model绑定条件
public class JoinSpecifyAbs<Children extends JoinSpecifyAbs<Children, Left, Right>, Left extends Model, Right extends Model>
        implements JoinSpecify<Left, Right>
{

    // 左表（为空时默认为主表）
    protected Class<Left> left;
    protected String leftAlias;
    // 右表
    protected Class<Right> right;
    protected String rightAlias;

    // 加载[右表]数据到[左表]属性的名称
    protected String loadDataFieldName = null;
    // 加载[右表]数据列（限定返回数据列）
    protected List<String> loadDataRightColumns = null;

    // join-on关联条件
    public final List<Item<? extends Model, ? extends Model>> onList = new LinkedList<>();

    public JoinSpecifyAbs(Class<Left> left, String leftAlias) {
        this.left = left;
        this.leftAlias = leftAlias;
    }

    public JoinSpecifyAbs(Class<Left> left, String leftAlias, Class<Right> right, String rightAlias) {
        this(left, leftAlias);
        this.right = right;
        this.rightAlias = rightAlias;
    }

    public JoinSpecifyAbs(Class<Left> left, Class<Right> right) {
        this(left, "");
        this.right = right;
    }

    public JoinSpecifyAbs(Class<Left> left) {
        this.left = left;
    }

    public Children chain() {
        return (Children) this;
    }

    //TODO:-------------------- 查询条件 --------------------
    public Children neq(MFunction<Left, ?> lf, MFunction<Right, ?> rf) {
        return where(CompareType.NEQ, lf, right, rf);
    }

    public Children eq(MFunction<Left, ?> lf, Object val) {
        return where(CompareType.EQ, lf, val);
    }

    public <MRight extends Model> Children eq(MFunction<Left, ?> lf, Class<MRight> right, MFunction<MRight, ?> rf) {
        return where(CompareType.EQ, lf, right, rf);
    }

    public <MRight extends Model> Children neq(MFunction<Left, ?> lf, Class<MRight> right, MFunction<MRight, ?> rf) {
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

    protected <MRight extends Model> Children where(CompareType type, MFunction<Left, ?> lf, MFunction<MRight, ?> rf) {
        onList.add(new Item<>(type, lf, rf));
        return chain();
    }

    protected Children where(CompareType type, MFunction<Left, ?> lf, Object value) {
        onList.add(new Item<>(type, lf, value));
        return chain();
    }

    protected <MRight extends Model> Children where(CompareType type, MFunction<Left, ?> lf, Class<MRight> right, MFunction<MRight, ?> rf) {
        onList.add(new Item<>(type, lf, right, rf));
        return chain();
    }

    public Children raw(String sql) {
        onList.add(new Item<>(CompareType.RAW, null, sql));
        return chain();
    }

    public static class Item<OL extends Model, OR extends Model> {
        // 逻辑类型
        private LogicType logic = LogicType.AND;
        // 条件运算符
        private final CompareType compare;
        // 左表
        private Class<OL> left;     // =null默认为主表
        private String leftAlias;
        private MFunction<OL, ?> leftFun;
        private Object leftValue;
        // 右表和字段
        private Class<OR> right;
        private String rightAlias;
        private MFunction<OR, ?> rightFun;
        private Object rightValue;
        // 原生sql
        private String sql;

        public Item(LogicType logic, CompareType compare, Class<OL> left, String leftAlias, MFunction<OL, ?> leftFun, Class<OR> right, String rightAlias, MFunction<OR, ?> rightFun) {
            this.logic = logic;
            this.compare = compare;
            this.left = left;
            this.leftAlias = leftAlias;
            this.leftFun = leftFun;
            this.right = right;
            this.rightAlias = rightAlias;
            this.rightFun = rightFun;
        }

        public Item(LogicType logic, CompareType compare, Class<OL> left, String leftAlias, Object leftVal, Class<OR> right, String rightAlias, MFunction<OR, ?> rightFun) {
            this(logic, compare, left, "", null, right, "", rightFun);
            this.leftValue = leftVal;
        }

        public Item(LogicType logic, CompareType compare, Class<OL> left, String leftAlias, MFunction<OL, ?> leftFun, Class<OR> right, String rightAlias, Object rightVal) {
            this(logic, compare, left, "", leftFun, right, "", null);
            this.rightValue = rightVal;
        }

        public Item(LogicType logic, CompareType compare, Class<OL> left, MFunction<OL, ?> leftFun, Class<OR> right, MFunction<OR, ?> rightFun) {
            this(logic, compare, left, "", leftFun, right, "", rightFun);
        }

        public Item(CompareType compare, MFunction<OL, ?> leftFun, Class<OR> right, MFunction<OR, ?> rightFun) {
            this.compare = compare;
            this.leftFun = leftFun;
            this.right = right;
            this.rightFun = rightFun;
        }

        public Item(CompareType compare, MFunction<OL, ?> leftFun, Object rightValue) {
            this.compare = compare;
            this.leftFun = leftFun;
            this.rightValue = rightValue;
        }

        public Item(CompareType compare, MFunction<OL, ?> leftFun, MFunction<OR, ?> rightFun) {
            this.compare = compare;
            this.leftFun = leftFun;
            this.rightFun = rightFun;
        }

        public LogicType getLogic() {
            return logic;
        }

        public void setLogic(LogicType logic) {
            this.logic = logic;
        }

        public CompareType getCompare() {
            return compare;
        }

        public Class<OL> getLeft() {
            return left;
        }

        public void setLeft(Class<? extends Model> left) {
            this.left = (Class<OL>) left;
        }

        public String getLeftAlias() {
            return leftAlias;
        }

        public void setLeftAlias(String leftAlias) {
            this.leftAlias = leftAlias;
        }

        public MFunction<OL, ?> getLeftFun() {
            return leftFun;
        }

        public Object getLeftValue() {
            return leftValue;
        }

        public void setLeftValue(Object leftValue) {
            this.leftValue = leftValue;
        }

        public Class<OR> getRight() {
            return right;
        }

        public void setRight(Class<? extends Model> right) {
            this.right = (Class<OR>) right;
        }

        public String getRightAlias() {
            return rightAlias;
        }

        public void setRightAlias(String rightAlias) {
            this.rightAlias = rightAlias;
        }

        public MFunction<OR, ?> getRightFun() {
            return rightFun;
        }

        public void setRightFun(MFunction<OR, ?> rightFun) {
            this.rightFun = rightFun;
        }

        public Object getRightValue() {
            return rightValue;
        }

        public void setRightValue(Object rightValue) {
            this.rightValue = rightValue;
        }

        public String getSql() {
            return sql;
        }

        public void setSql(String sql) {
            this.sql = sql;
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

    @Override
    public Class<Right> getRight() {
        return right;
    }

    @Override
    public String getRightAlias() {
        return null;
    }

    public String getLoadDataFieldName() {
        return loadDataFieldName;
    }

    // 加载数据到主表
    public<MRight extends Model> Children setLoadDataFieldName(MFunction<MRight, ?> rightField) {
        this.loadDataFieldName = rightField.getName();
        return chain();
    }

    public List<Item<?, ?>> getOnList() {
        return onList;
    }
}
