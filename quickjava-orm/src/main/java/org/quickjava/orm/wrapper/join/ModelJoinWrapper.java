package org.quickjava.orm.wrapper.join;

import org.quickjava.orm.Model;
import org.quickjava.orm.wrapper.MFunction;
import org.quickjava.orm.wrapper.enums.JoinType;

import java.io.Serializable;

public interface ModelJoinWrapper<Children, M extends Model, R extends MFunction<M, ?>> extends Serializable {

    /**
     * 与主表一个条件关联
     * 默认根据类进行自动加载
     */
    default <Relation extends Model> Children leftJoin(Class<Relation> left, MFunction<Relation, ?> lf, R rf) {
        return join(JoinType.LEFT, new JoinSpecify<Relation, M>(left, null).eq(lf, rf));
    }

    /**
     * 与主表一个条件关联[指定属性]
     * @param alias 数据加载到的主模型属性，就是关联数据在模型撒谎功能的那个属性
     */
    default <Relation extends Model> Children leftJoin(Class<Relation> left, MFunction<Relation, ?> lf, R rf, R alias) {
        return join(JoinType.LEFT, new JoinSpecify<Relation, M>(left, alias, null).eq(lf, rf));
    }

    /**
     * 与主表一个条件关联[指定属性]
     * @param alias 数据加载到的主模型属性，就是关联数据在模型撒谎功能的那个属性
     */
    default <Relation extends Model> Children leftJoin(Class<Relation> left, MFunction<Relation, ?> lf, R rf, String alias) {
        return join(JoinType.LEFT, new JoinSpecify<Relation, M>(left, alias, null).eq(lf, rf));
    }

    /**
     * 两张子表一个条件关联
     */
    default <Left extends Model, Right extends Model> Children leftJoin(Class<Left> left, MFunction<Left, ?> lf,
                                                                        Class<Right> right, MFunction<Right, ?> rf) {
        return join(JoinType.LEFT, new JoinSpecify<Left, Right>(left, right).eq(lf, rf));
    }

    /**
     * 两张子表一个条件关联
     */
    default <Left extends Model, Right extends Model> Children leftJoin(Class<Left> left, MFunction<Left, ?> lf,
                                                                        Class<Right> right, MFunction<Right, ?> rf, R fieldFun) {
        return join(JoinType.LEFT, new JoinSpecify<Left, Right>(left, right).eq(lf, rf).setLeftAlias(fieldFun.getName()));
    }

    /**
     * 两张子表多个条件关联
     */
    default <Left extends Model, Right extends Model> Children leftJoin(Class<Left> left, Class<Right> right, JoinSpecifyClosure<Left, Right> closure) {
        JoinSpecify<Left, Right> condition = new JoinSpecify<>(left, right);
        closure.call(condition);
        return join(JoinType.LEFT, condition);
    }

    /**
     * 两张子表多个条件关联
     */
    default <Left extends Model, Right extends Model> Children leftJoin(Class<Left> left, Class<Right> right, JoinSpecifyClosure<Left, Right> closure, R alias) {
        JoinSpecify<Left, Right> condition = new JoinSpecify<>(left, right);
        closure.call(condition);
        condition.setLeftAlias(alias.getName());
        return join(JoinType.LEFT, condition);
    }

    /**
     * 左表关联右多个表多个条件
     */
    default <Left extends Model> Children leftJoin(Class<Left> left, JoinSpecifyLeftClosure<Left> closure) {
        return leftJoin(left, null, closure);
    }

    /**
     * 左表关联右多个表多个条件
     */
    default <Left extends Model> Children leftJoin(Class<Left> left, String alias, JoinSpecifyLeftClosure<Left> closure) {
        JoinSpecifyLeft<Left> condition = new JoinSpecifyLeft<>(left);
        closure.call(condition);
        condition.setLeftAlias(alias);
        return join(JoinType.LEFT, condition);
    }

    /**
     * 对应sql的join默认行为为INNER JOIN
     */
    default Children join(JoinSpecifyBase<?, ?> condition) {
        return join(JoinType.INNER, condition);
    }

    Children join(JoinType type, JoinSpecifyBase<?, ?> condition);

}
