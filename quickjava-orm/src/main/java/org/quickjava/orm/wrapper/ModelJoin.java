package org.quickjava.orm.wrapper;

import org.quickjava.orm.Model;
import org.quickjava.orm.wrapper.conditions.JoinCondition;
import org.quickjava.orm.wrapper.conditions.JoinConditionBasic;
import org.quickjava.orm.wrapper.conditions.JoinConditionLeft;
import org.quickjava.orm.wrapper.conditions.JoinConditionRight;
import org.quickjava.orm.wrapper.enums.JoinType;

import java.io.Serializable;

public interface ModelJoin<Children, M extends Model, R extends MFunction<M, ?>> extends Serializable {

    /**
     * 与主表一个条件关联
     */
    default <Relation extends Model> Children leftJoin(Class<Relation> clazz, MFunction<Relation, ?> lf, R rf) {
        return join(JoinType.LEFT, new JoinConditionRight<M>(null).eq(clazz, lf, rf));
    }

    /**
     * 两张子表一个条件关联
     */
    default <Left extends Model, Right extends Model> Children leftJoin(Class<Left> left, MFunction<Left, ?> lf,
                                                                        Class<Right> right, MFunction<Right, ?> rf) {
        return join(JoinType.LEFT, new JoinCondition<>(left, right).eq(lf, rf));
    }

    /**
     * 两张子表多个条件关联
     */
    default <Left extends Model, Right extends Model> Children leftJoin(Class<Left> left, Class<Right> right, JoinClosureLR<Left, Right> closure) {
        JoinCondition<Left, Right> condition = new JoinCondition<>(left, right);
        closure.call(condition);
        return join(JoinType.LEFT, condition);
    }

    /**
     * 左表关联右多个表多个条件
     */
    default <Left extends Model> Children leftJoin(Class<Left> left, JoinClosureLeft<Left> closure) {
        JoinConditionLeft<Left> condition = new JoinConditionLeft<>(left);
        closure.call(condition);
        return join(JoinType.LEFT, condition);
    }

    /**
     * 对应sql的join默认行为为INNER JOIN
     */
    default Children join(JoinConditionBasic<?> condition) {
        return join(JoinType.INNER, condition);
    }

    Children join(JoinType type, JoinConditionBasic<?> condition);

    /**
     * 闭包调用
     */
    public interface JoinClosure<Right extends Model> {
        void call(JoinConditionRight<Right> condition);
    }

    public interface JoinClosureLR<Left extends Model, Right extends Model> {
        void call(JoinCondition<Left, Right> condition);
    }

    public interface JoinClosureLeft<Left extends Model> {
        void call(JoinConditionLeft<Left> condition);
    }

}
