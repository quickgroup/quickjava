package org.quickjava.orm.wrapper.conditions;

import org.quickjava.orm.Model;
import org.quickjava.orm.wrapper.MFunction;
import org.quickjava.orm.wrapper.enums.JoinConditionType;

/**
 * 左表确定，右表不确定
 */
public class JoinConditionLeft<Left extends Model> extends JoinConditionBasic<JoinConditionLeft<Left>> {

    protected Class<Left> left;

    public JoinConditionLeft(Class<Left> left) {
        this.left = left;
    }

    public <R extends Model> JoinConditionLeft<Left> eq(MFunction<Left, ?> lf, Class<R> right, MFunction<R, ?> rf) {
        return add(JoinConditionType.EQ, this.left, lf, right, rf);
    }

}
