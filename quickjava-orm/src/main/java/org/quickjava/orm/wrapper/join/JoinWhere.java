package org.quickjava.orm.wrapper.join;

import org.quickjava.orm.Model;
import org.quickjava.orm.wrapper.MFunction;

/**
 * join条件
 * 左右两边对象类确定
 */
public class JoinWhere<Left extends Model, Right extends Model> extends JoinWhereBase<JoinWhere<Left, Right>, Left> {

    protected Class<Right> right;

    public JoinWhere(Class<Left> left, Class<Right> right) {
        super(left);
        this.right = right;
    }

    public JoinWhere(Class<Left> left, String leftAlias, MFunction<Right, ?> fieldFun, boolean loadLeftData, Class<Right> right) {
        super(left, leftAlias, fieldFun, loadLeftData);
        this.right = right;
    }

    public JoinWhere(Class<Left> left, String leftAlias, MFunction<Right, ?> fieldFun, Class<Right> right) {
        super(left, leftAlias, fieldFun);
        this.right = right;
    }

    public JoinWhere(Class<Left> left, MFunction<?, ?> fieldFun, Class<Right> right) {
        super(left, fieldFun);
        this.right = right;
    }

    public JoinWhere<Left, Right> eq(MFunction<Left, ?> lf, MFunction<Right, ?> rf) {
        return super.eq(lf, this.right, rf);
    }

    public JoinWhere<Left, Right> neq(MFunction<Left, ?> lf, MFunction<Right, ?> rf) {
        return super.neq(lf, this.right, rf);
    }

}
