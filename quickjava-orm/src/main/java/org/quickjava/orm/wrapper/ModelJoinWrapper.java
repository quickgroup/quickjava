package org.quickjava.orm.wrapper;

import org.quickjava.orm.Model;
import org.quickjava.orm.wrapper.conditions.ModelJoinLR;
import org.quickjava.orm.wrapper.conditions.ModelJoin;
import org.quickjava.orm.wrapper.conditions.ModelJoinRight;
import org.quickjava.orm.wrapper.enums.JoinType;

import java.io.Serializable;

public interface ModelJoinWrapper<Children, M extends Model, R extends MFunction<M, ?>> extends Serializable {

    /**
     * 与主表一个条件关联
     * 默认根据类进行自动加载
     */
    default <Relation extends Model> Children leftJoin(Class<Relation> left, MFunction<Relation, ?> lf, R rf) {
        return join(JoinType.LEFT, new ModelJoinLR<Relation, M>(left, null).eq(lf, rf));
    }

    /**
     * 与主表一个条件关联[指定属性]
     * @param fieldFun 数据加载到的主模型属性，就是关联数据在模型撒谎功能的那个属性
     */
    default <Relation extends Model> Children leftJoin(Class<Relation> left, MFunction<Relation, ?> lf, R rf, R fieldFun) {
        return join(JoinType.LEFT, new ModelJoinLR<Relation, M>(left, null).eq(lf, rf).setFieldFun(fieldFun).setLeftAlias(fieldFun.getName()));
    }

    /**
     * 两张子表一个条件关联
     */
    default <Left extends Model, Right extends Model> Children leftJoin(Class<Left> left, MFunction<Left, ?> lf,
                                                                        Class<Right> right, MFunction<Right, ?> rf) {
        return join(JoinType.LEFT, new ModelJoinLR<>(left, right).eq(lf, rf));
    }

    /**
     * 两张子表多个条件关联
     */
    default <Left extends Model, Right extends Model> Children leftJoin(Class<Left> left, Class<Right> right, JoinClosure<Left, Right> closure) {
        ModelJoinLR<Left, Right> condition = new ModelJoinLR<>(left, right);
        closure.call(condition);
        return join(JoinType.LEFT, condition);
    }

    /**
     * 左表关联右多个表多个条件
     */
    default <Left extends Model> Children leftJoin(Class<Left> left, JoinClosureLeft<Left> closure) {
        ModelJoinRight<Left> condition = new ModelJoinRight<>(left);
        closure.call(condition);
        return join(JoinType.LEFT, condition);
    }

    /**
     * 对应sql的join默认行为为INNER JOIN
     */
    default Children join(ModelJoin<?, ?> condition) {
        return join(JoinType.INNER, condition);
    }

    Children join(JoinType type, ModelJoin<?, ?> condition);

    /**
     * 闭包调用
     */
    public interface JoinClosure<Left extends Model, Right extends Model> {
        void call(ModelJoinLR<Left, Right> condition);
    }

    public interface JoinClosureLeft<Left extends Model> {
        void call(ModelJoinRight<Left> condition);
    }

}
