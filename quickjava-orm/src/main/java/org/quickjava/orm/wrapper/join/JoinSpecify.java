package org.quickjava.orm.wrapper.join;

import org.quickjava.orm.model.Model;
import org.quickjava.orm.wrapper.MFunction;

/**
 * join条件
 * 左右两边对象类确定
 */
public class JoinSpecify<Left extends Model, Right extends Model> extends JoinSpecifyBase<JoinSpecify<Left, Right>, Left> {

    protected Class<Right> right;

    protected String rightAlias;

    public JoinSpecify(Class<Left> left, String leftAlias) {
        super(left, leftAlias);
    }

    public JoinSpecify(Class<Left> left) {
        super(left);
    }

    public JoinSpecify(Class<Left> left, Class<Right> right) {
        super(left);
        this.right = right;
        this.rightAlias = null;
    }

    public JoinSpecify<Left, Right> setRight(Class<Right> right) {
        this.right = right;
        this.rightAlias = null;
        return this;
    }

    public JoinSpecify<Left, Right> setRight(Class<Right> right, String rightAlias) {
        this.right = right;
        this.rightAlias = rightAlias;
        return this;
    }

    public JoinSpecify<Left, Right> eq(MFunction<Left, ?> lf, MFunction<Right, ?> rf) {
        return super.eq(lf, this.right, rf);
    }

    public JoinSpecify<Left, Right> neq(MFunction<Left, ?> lf, MFunction<Right, ?> rf) {
        return super.neq(lf, this.right, rf);
    }

}
