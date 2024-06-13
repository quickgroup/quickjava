package org.quickjava.orm.wrapper.join;

import org.quickjava.orm.model.Model;

/**
 * 左表确定，右表不确定
 */
public class JoinSpecifyRight<Left extends Model, Right extends Model>
        extends JoinSpecifyAbs<JoinSpecifyLeft<Left, Right>, Left, Right>
{

    public JoinSpecifyRight(Class<Left> left, Class<Right> right) {
        super(left, right);
    }

    public JoinSpecifyRight(Class<Left> left, String leftAlias) {
        super(left, leftAlias, null, "");
    }

    //    public JoinSpecifyLeft(Class<Left> left) {
//        super(left);
//    }
//
//    public <Right extends Model> JoinSpecifyLeft<Left> eq(MFunction<Left, ?> lf, Class<Right> right, MFunction<Right, ?> rf) {
//        return super.eq(lf, right, rf);
//    }
//
//    public JoinSpecifyLeft<Left> eq(MFunction<Left, ?> lf, Object val) {
//        return super.eq(lf, val);
//    }
//
//    public <Right extends Model> JoinSpecifyLeft<Left> neq(MFunction<Left, ?> lf, Class<Right> right, MFunction<Right, ?> rf) {
//        return super.neq(lf, right, rf);
//    }
//
//    public JoinSpecifyLeft<Left> isNull(MFunction<Left, ?> lf) {
//        return super.isNull(lf);
//    }
//
//    public JoinSpecifyLeft<Left> isNotNull(MFunction<Left, ?> lf) {
//        return super.isNotNull(lf);
//    }

}
