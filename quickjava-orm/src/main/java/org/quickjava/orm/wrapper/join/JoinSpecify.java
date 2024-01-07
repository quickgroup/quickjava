package org.quickjava.orm.wrapper.join;

import org.quickjava.orm.Model;
import org.quickjava.orm.wrapper.MFunction;

/**
 * join条件
 * 左右两边对象类确定
 */
public class JoinSpecify<Left extends Model, Right extends Model> extends JoinSpecifyBase<JoinSpecify<Left, Right>, Left> {

    protected Class<Right> right;

    public JoinSpecify(Class<Left> left, Class<Right> right) {
        super(left);
        this.right = right;
    }

    public JoinSpecify(Class<Left> left, String leftAlias, Class<Right> right) {
        super(left, leftAlias);
        this.right = right;
    }

    public JoinSpecify(Class<Left> left, MFunction<?, ?> leftAlias, Class<Right> right) {
        super(left, leftAlias.getName());
        this.right = right;
    }

    public JoinSpecify<Left, Right> eq(MFunction<Left, ?> lf, MFunction<Right, ?> rf) {
        return super.eq(lf, this.right, rf);
    }

    public JoinSpecify<Left, Right> neq(MFunction<Left, ?> lf, MFunction<Right, ?> rf) {
        return super.neq(lf, this.right, rf);
    }

}
