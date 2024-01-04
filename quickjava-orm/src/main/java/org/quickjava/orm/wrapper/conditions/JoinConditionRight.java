package org.quickjava.orm.wrapper.conditions;

import org.quickjava.orm.Model;
import org.quickjava.orm.wrapper.MFunction;
import org.quickjava.orm.wrapper.enums.JoinConditionType;

/**
 * join条件
 * 左表不确定，右表确定
 */
public class JoinConditionRight<Right extends Model> extends JoinConditionBasic<JoinConditionRight<Right>> {

    protected Class<Right> right;

    public JoinConditionRight(Class<Right> right) {
        this.right = right;
    }

    public <L extends Model> JoinConditionRight<Right> eq(Class<L> left, MFunction<L, ?> lf, MFunction<Right, ?> rf) {
        return add(JoinConditionType.EQ, left, lf, rf);
    }

    public <L extends Model> JoinConditionRight<Right> ne(Class<L> left, MFunction<L, ?> lf, MFunction<Right, ?> rf) {
        return add(JoinConditionType.NEQ, left, lf, rf);
    }

}
