package org.quickjava.orm.wrapper.join;

import org.quickjava.orm.model.Model;
import org.quickjava.orm.query.enums.Operator;
import org.quickjava.orm.query.enums.OrderByType;
import org.quickjava.orm.wrapper.MFunction;
import org.quickjava.orm.wrapper.Wrapper;
import org.quickjava.orm.wrapper.WrapperUtil;
import org.quickjava.orm.enums.JoinType;

import java.io.Serializable;

public interface ModelJoinWrapper<
            Children,
            M extends Model,
            MF extends MFunction<M, ?>
        >
        extends Wrapper<Children>, Serializable {

    /**
     * 与主表一个条件关联
     * 默认根据类进行自动加载
     * @return Children
     */
    default <Relation extends Model> Children leftJoin(Class<Relation> left, MFunction<Relation, ?> lf, MF rf) {
        return join(JoinType.LEFT, new JoinSpecify<Relation, M>(left, null).eq(lf, rf));
    }

    /**
     * 与主表一个条件关联[指定属性]
     * @param alias 数据加载到的主模型属性，就是关联数据在模型撒谎功能的那个属性
     * @return Children
     */
    default <Relation extends Model> Children leftJoin(Class<Relation> left, MFunction<Relation, ?> lf, MF rf, MF alias) {
        return join(JoinType.LEFT, new JoinSpecify<Relation, M>(left, alias, null).eq(lf, rf));
    }

    /**
     * 与主表一个条件关联[指定属性]
     * @param alias 数据加载到的主模型属性，就是关联数据在模型撒谎功能的那个属性
     * @return Children
     */
    default <Relation extends Model> Children leftJoin(Class<Relation> left, MFunction<Relation, ?> lf, MF rf, String alias) {
        return join(JoinType.LEFT, new JoinSpecify<Relation, M>(left, alias, null).eq(lf, rf));
    }

    /**
     * 两张子表一个条件关联
     * @return Children
     */
    default <Left extends Model, Right extends Model> Children leftJoin(Class<Left> left, MFunction<Left, ?> lf,
                                                                        Class<Right> right, MFunction<Right, ?> rf) {
        return join(JoinType.LEFT, new JoinSpecify<Left, Right>(left, right).eq(lf, rf));
    }

    /**
     * 两张子表一个条件关联
     * @return Children
     */
    default <Left extends Model, Right extends Model> Children leftJoin(Class<Left> left, MFunction<Left, ?> lf,
                                                                        Class<Right> right, MFunction<Right, ?> rf, MF fieldFun) {
        return join(JoinType.LEFT, new JoinSpecify<Left, Right>(left, right).eq(lf, rf).setLeftAlias(fieldFun.getName()));
    }

    /**
     * 两张子表多个条件关联
     * @return Children
     */
    default <Left extends Model, Right extends Model> Children leftJoin(Class<Left> left, Class<Right> right, JoinSpecifyClosure<Left, Right> closure) {
        JoinSpecify<Left, Right> condition = new JoinSpecify<>(left, right);
        closure.call(condition);
        return join(JoinType.LEFT, condition);
    }

    /**
     * 两张子表多个条件关联
     * @return Children
     */
    default <Left extends Model, Right extends Model> Children leftJoin(Class<Left> left, Class<Right> right, JoinSpecifyClosure<Left, Right> closure, MF alias) {
        JoinSpecify<Left, Right> condition = new JoinSpecify<>(left, right);
        closure.call(condition);
        condition.setLeftAlias(alias.getName());
        return join(JoinType.LEFT, condition);
    }

    /**
     * 左表关联右多个表多个条件
     * @return Children
     */
    default <Left extends Model> Children leftJoin(Class<Left> left, JoinSpecifyLeftClosure<Left> closure) {
        return leftJoin(left, null, closure);
    }

    /**
     * 左表关联右多个表多个条件
     * @return Children
     */
    default <Left extends Model> Children leftJoin(Class<Left> left, String alias, JoinSpecifyLeftClosure<Left> closure) {
        JoinSpecifyLeft<Left> condition = new JoinSpecifyLeft<>(left);
        closure.call(condition);
        condition.setLeftAlias(alias);
        return join(JoinType.LEFT, condition);
    }

    /**
     * 对应sql的join默认行为为INNER JOIN
     * @return Children
     */
    default Children join(JoinSpecifyBase<?, ?> condition) {
        return join(JoinType.INNER, condition);
    }

    Children join(JoinType type, JoinSpecifyBase<?, ?> condition);

    //TODO::-------------------- 查询条件 START --------------------

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

    default <Left extends Model> Children where(boolean condition, String table, Class<Left> left, String column, Operator operator, Object val) {
        if (condition) {
            // 懒加载
            table = WrapperUtil.autoTable(table, this, left);
            WrapperUtil.getQuerySet(this).where(table, column, operator, val);
        }
        return chain();
    }

    Children where(boolean condition, String table, String column, Operator operator, Object val);

    default <Left> Children field(Class<Left> left, MFunction<Left, ?> ... lfs) {
        for (MFunction<Left, ?> lf : lfs) {
            field(left.getName(), lf.getName());
        }
        return chain();
    }

    Children field(String table, String column);

    //TODO::-------------------- 查询条件 END  --------------------

    //TODO::-------------------- 附加 START --------------------

    default <Left> Children order(Class<Left> left, MFunction<Left, ?> lf, OrderByType type) {
        String table = WrapperUtil.autoTable(null, this, left);
        return order(table, lf.getName(), type);
    }

    default <Left> Children order(Class<Left> left, MFunction<Left, ?> lf, boolean desc) {
        String table = WrapperUtil.autoTable(null, this, left);
        return order(table, lf.getName(), desc);
    }

    default <Left> Children order(Class<Left> left, MFunction<Left, ?> lf) {
        String table = WrapperUtil.autoTable(null, this, left);
        return order(table, lf.getName(), OrderByType.ASC);
    }

    default Children order(String table, String field, boolean desc) {
        return order(table, field, desc ? OrderByType.DESC : OrderByType.ASC);
    }

    Children order(String table, String field, OrderByType type);

    //TODO::-------------------- 附加 END --------------------

}
