package org.quickjava.orm.wrapper.join;

import org.quickjava.orm.Model;
import org.quickjava.orm.wrapper.MFunction;

/**
 * 左表确定，右表不确定
 */
public class JoinConditionRight<Left extends Model> extends JoinConditionBase<JoinConditionRight<Left>, Left> {

    public JoinConditionRight(Class<Left> left) {
        super(left);
    }

    public <Right extends Model> JoinConditionRight<Left> eq(MFunction<Left, ?> lf, Class<Right> right, MFunction<Right, ?> rf) {
        return super.eq(lf, right, rf);
    }

    public <Right extends Model> JoinConditionRight<Left> neq(MFunction<Left, ?> lf, Class<Right> right, MFunction<Right, ?> rf) {
        return super.neq(lf, right, rf);
    }

}
