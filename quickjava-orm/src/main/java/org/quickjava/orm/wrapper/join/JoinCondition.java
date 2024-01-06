package org.quickjava.orm.wrapper.join;

import org.quickjava.orm.Model;
import org.quickjava.orm.wrapper.MFunction;

/**
 * join条件
 * 左右两边对象类确定
 */
public class JoinCondition<Left extends Model, Right extends Model> extends JoinConditionBase<JoinCondition<Left, Right>, Left> {

    protected Class<Right> right;

    public JoinCondition(Class<Left> left, Class<Right> right) {
        super(left);
        this.right = right;
    }

    public JoinCondition(Class<Left> left, String leftAlias, MFunction<Right, ?> fieldFun, boolean loadLeftData, Class<Right> right) {
        super(left, leftAlias, fieldFun, loadLeftData);
        this.right = right;
    }

    public JoinCondition(Class<Left> left, String leftAlias, MFunction<Right, ?> fieldFun, Class<Right> right) {
        super(left, leftAlias, fieldFun);
        this.right = right;
    }

    public JoinCondition(Class<Left> left, MFunction<?, ?> fieldFun, Class<Right> right) {
        super(left, fieldFun);
        this.right = right;
    }

    public JoinCondition<Left, Right> eq(MFunction<Left, ?> lf, MFunction<Right, ?> rf) {
        return super.eq(lf, this.right, rf);
    }

    public JoinCondition<Left, Right> neq(MFunction<Left, ?> lf, MFunction<Right, ?> rf) {
        return super.neq(lf, this.right, rf);
    }

}
