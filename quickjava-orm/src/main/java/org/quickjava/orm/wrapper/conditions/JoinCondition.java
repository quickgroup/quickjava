package org.quickjava.orm.wrapper.conditions;

import org.quickjava.orm.Model;
import org.quickjava.orm.wrapper.MFunction;
import org.quickjava.orm.wrapper.enums.JoinConditionType;

/**
 * join条件
 * 左右两边对象类确定
 */
public class JoinCondition<Left extends Model, Right extends Model> extends JoinConditionBasic<JoinCondition<Left, Right>> {

    protected Class<Left> left;
    protected Class<Right> right;

    public JoinCondition(Class<Left> left, Class<Right> right) {
        this.left = left;
        this.right = right;
    }

    public JoinCondition<Left, Right> eq(MFunction<Left, ?> lf, MFunction<Right, ?> rf) {
        return add(JoinConditionType.EQ, this.left, lf, this.right, rf);
    }

}
