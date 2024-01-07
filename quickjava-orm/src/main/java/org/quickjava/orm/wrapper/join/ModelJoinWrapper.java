package org.quickjava.orm.wrapper.join;

import org.quickjava.orm.Model;
import org.quickjava.orm.query.enums.Operator;
import org.quickjava.orm.wrapper.MFunction;
import org.quickjava.orm.wrapper.enums.JoinType;

import java.io.Serializable;

public interface ModelJoinWrapper<
            Children,
            M extends Model,
            MF extends MFunction<M, ?>
        >
        extends Serializable {

    /**
     * 与主表一个条件关联
     * 默认根据类进行自动加载
     */
    default <Relation extends Model> Children leftJoin(Class<Relation> left, MFunction<Relation, ?> lf, MF rf) {
        return join(JoinType.LEFT, new JoinSpecify<Relation, M>(left, null).eq(lf, rf));
    }

    /**
     * 与主表一个条件关联[指定属性]
     * @param alias 数据加载到的主模型属性，就是关联数据在模型撒谎功能的那个属性
     */
    default <Relation extends Model> Children leftJoin(Class<Relation> left, MFunction<Relation, ?> lf, MF rf, MF alias) {
        return join(JoinType.LEFT, new JoinSpecify<Relation, M>(left, alias, null).eq(lf, rf));
    }

    /**
     * 与主表一个条件关联[指定属性]
     * @param alias 数据加载到的主模型属性，就是关联数据在模型撒谎功能的那个属性
     */
    default <Relation extends Model> Children leftJoin(Class<Relation> left, MFunction<Relation, ?> lf, MF rf, String alias) {
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
                                                                        Class<Right> right, MFunction<Right, ?> rf, MF fieldFun) {
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
    default <Left extends Model, Right extends Model> Children leftJoin(Class<Left> left, Class<Right> right, JoinSpecifyClosure<Left, Right> closure, MF alias) {
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

    //TODO::-------------------- join关联表的查询条件 START --------------------

    // 自动识别查询表名
    default <Left extends Model> Children eq(Class<Left> left, MFunction<Left, ?> lf, Object val) {
        return where(true, null, left, lf.getName(), Operator.EQ, val);
    }

    // 使用在父实体的属性名做表名
    default <Left extends Model> Children eq(MF mf, Class<Left> left, MFunction<Left, ?> lf, Object val) {
        return where(true, mf.getName(), left, lf.getName(), Operator.EQ, val);
    }

    default <Left extends Model> Children eq(String table, MFunction<Left, ?> lf, Object val) {
        return where(true, table, lf.getName(), Operator.EQ, val);
    }

    <Left extends Model> Children where(boolean condition, String leftAlias, Class<Left> left, String column, Operator operator, Object val);

    Children where(boolean condition, String table, String column, Operator operator, Object val);

    //TODO::-------------------- join的查询条件 END  --------------------

}
