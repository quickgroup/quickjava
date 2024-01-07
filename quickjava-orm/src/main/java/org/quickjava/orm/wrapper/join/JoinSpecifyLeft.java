package org.quickjava.orm.wrapper.join;

import org.quickjava.orm.Model;
import org.quickjava.orm.wrapper.MFunction;

/**
 * 左表确定，右表不确定
 */
public class JoinSpecifyLeft<Left extends Model> extends JoinSpecifyBase<JoinSpecifyLeft<Left>, Left> {

    public JoinSpecifyLeft(Class<Left> left) {
        super(left);
    }

    public <Right extends Model> JoinSpecifyLeft<Left> eq(MFunction<Left, ?> lf, Class<Right> right, MFunction<Right, ?> rf) {
        return super.eq(lf, right, rf);
    }

    public JoinSpecifyLeft<Left> eq(MFunction<Left, ?> lf, Object val) {
        return super.eq(lf, val);
    }

    public <Right extends Model> JoinSpecifyLeft<Left> neq(MFunction<Left, ?> lf, Class<Right> right, MFunction<Right, ?> rf) {
        return super.neq(lf, right, rf);
    }

}
