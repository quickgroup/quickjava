package org.quickjava.orm.wrapper.conditions;

import org.quickjava.orm.Model;
import org.quickjava.orm.wrapper.MFunction;
import org.quickjava.orm.wrapper.enums.ConditionType;

/**
 * join条件
 * 左右两边对象类确定
 */
public class ModelJoinLR<Left extends Model, Right extends Model> extends ModelJoin<ModelJoinLR<Left, Right>, Left> {

    protected Class<Right> right;

    public ModelJoinLR(Class<Left> left, Class<Right> right) {
        super(left);
        this.right = right;
    }

    public ModelJoinLR<Left, Right> eq(MFunction<Left, ?> lf, MFunction<Right, ?> rf) {
        return add(ConditionType.EQ, lf, this.right, rf);
    }

}
