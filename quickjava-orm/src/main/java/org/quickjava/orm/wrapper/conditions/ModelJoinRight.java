package org.quickjava.orm.wrapper.conditions;

import org.quickjava.orm.Model;
import org.quickjava.orm.wrapper.MFunction;
import org.quickjava.orm.wrapper.enums.ConditionType;

/**
 * 左表确定，右表不确定
 */
public class ModelJoinRight<Left extends Model> extends ModelJoin<ModelJoinRight<Left>, Left> {

    public ModelJoinRight(Class<Left> left) {
        super(left);
    }

    public <Right extends Model> ModelJoinRight<Left> eq(MFunction<Left, ?> lf, Class<Right> right, MFunction<Right, ?> rf) {
        return add(ConditionType.EQ, lf, right, rf);
    }

}
