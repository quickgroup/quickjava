package org.quickjava.orm.wrapper.join;

import org.quickjava.orm.Model;
import org.quickjava.orm.wrapper.MFunction;

/**
 * 左表确定，右表不确定
 */
public class JoinWhereLeft<Left extends Model> extends JoinWhereBase<JoinWhereLeft<Left>, Left> {

    public JoinWhereLeft(Class<Left> left) {
        super(left);
    }

    public <Right extends Model> JoinWhereLeft<Left> eq(MFunction<Left, ?> lf, Class<Right> right, MFunction<Right, ?> rf) {
        return super.eq(lf, right, rf);
    }

    public JoinWhereLeft<Left> eq(MFunction<Left, ?> lf, Object val) {
        return super.eq(lf, val);
    }

    public <Right extends Model> JoinWhereLeft<Left> neq(MFunction<Left, ?> lf, Class<Right> right, MFunction<Right, ?> rf) {
        return super.neq(lf, right, rf);
    }

}
