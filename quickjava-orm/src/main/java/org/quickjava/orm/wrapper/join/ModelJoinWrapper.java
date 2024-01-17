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
     * @param  condition 关联条件
     * @return Children
     */
    default Children join(JoinSpecifyBase<?, ?> condition) {
        return join(JoinType.INNER, condition);
    }

    Children join(JoinType type, JoinSpecifyBase<?, ?> condition);

    //TODO::-------------------- 查询条件 START --------------------
    // 自动识别查询表名
    default <Left extends Model> Children eq(Class<Left> left, MFunction<Left, ?> lf, Object val) {
        return where(true, left, lf, Operator.EQ, val);
    }

    // 使用在父实体的属性名做表别名
    default <Left extends Model> Children eq(MF mf, Class<Left> left, MFunction<Left, ?> lf, Object val) {
        return where(true, mf, left, lf, Operator.EQ, val);
    }

    default <Left extends Model> Children eq(String table, MFunction<Left, ?> lf, Object val) {
        return where(true, table, lf.getName(), Operator.EQ, val);
    }


    // 不等于
    default <Left extends Model> Children neq(Class<Left> left, MFunction<Left, ?> lf, Object val) {
        return where(true, left, lf, Operator.NEQ, val);
    }

    default <Left extends Model> Children neq(MF mf, Class<Left> left, MFunction<Left, ?> lf, Object val) {
        return where(true, mf, left, lf, Operator.NEQ, val);
    }

    default <Left extends Model> Children neq(String table, MFunction<Left, ?> lf, Object val) {
        return where(true, table, lf, Operator.NEQ, val);
    }


    // 大于
    default <Left extends Model> Children gt(Class<Left> left, MFunction<Left, ?> lf, Object val) {
        return where(true, left, lf, Operator.GT, val);
    }

    default <Left extends Model> Children gt(MF mf, Class<Left> left, MFunction<Left, ?> lf, Object val) {
        return where(true, mf, left, lf, Operator.GT, val);
    }

    default <Left extends Model> Children gt(String table, MFunction<Left, ?> lf, Object val) {
        return where(true, table, lf, Operator.GT, val);
    }


    // 大于等于
    default <Left extends Model> Children gte(Class<Left> left, MFunction<Left, ?> lf, Object val) {
        return where(true, left, lf, Operator.GTE, val);
    }

    default <Left extends Model> Children gte(MF mf, Class<Left> left, MFunction<Left, ?> lf, Object val) {
        return where(true, mf, left, lf, Operator.GTE, val);
    }

    default <Left extends Model> Children gte(String left, MFunction<Left, ?> lf, Object val) {
        return where(true, left, lf, Operator.GTE, val);
    }


    // 小于
    default <Left extends Model> Children lt(Class<Left> left, MFunction<Left, ?> lf, Object val) {
        return where(true, left, lf, Operator.LT, val);
    }

    default <Left extends Model> Children lt(MF mf, Class<Left> left, MFunction<Left, ?> lf, Object val) {
        return where(true, mf, left, lf, Operator.LT, val);
    }

    default <Left extends Model> Children lt(String left, MFunction<Left, ?> lf, Object val) {
        return where(true, left, lf, Operator.LT, val);
    }


    // 小于等于
    default <Left extends Model> Children lte(Class<Left> left, MFunction<Left, ?> lf, Object val) {
        return where(true, left, lf, Operator.LTE, val);
    }

    default <Left extends Model> Children lte(MF mf, Class<Left> left, MFunction<Left, ?> lf, Object val) {
        return where(true, mf, left, lf, Operator.LTE, val);
    }

    default <Left extends Model> Children lte(String left, MFunction<Left, ?> lf, Object val) {
        return where(true, left, lf, Operator.LTE, val);
    }


    // in
    default <Left extends Model> Children in(Class<Left> left, MFunction<Left, ?> lf, Object ...args) {
        return where(true, left, lf, Operator.IN, args);
    }

    default <Left extends Model> Children in(MF mf, Class<Left> left, MFunction<Left, ?> lf, Object ...args) {
        return where(true, mf, left, lf, Operator.IN, args);
    }

    default <Left extends Model> Children in(String left, MFunction<Left, ?> lf, Object ...args) {
        return where(true, left, lf, Operator.IN, args);
    }


    // not in
    default <Left extends Model> Children notIn(Class<Left> left, MFunction<Left, ?> lf, Object ...args) {
        return where(true, left, lf, Operator.IN, args);
    }

    default <Left extends Model> Children notIn(MF mf, Class<Left> left, MFunction<Left, ?> lf, Object ...args) {
        return where(true, mf, left, lf, Operator.IN, args);
    }

    default <Left extends Model> Children notIn(String left, MFunction<Left, ?> lf, Object ...args) {
        return where(true, left, lf, Operator.IN, args);
    }


    // is null
    default <Left extends Model> Children isNull(Class<Left> left, MFunction<Left, ?> lf) {
        return where(true, left, lf, Operator.IS_NULL, null);
    }

    default <Left extends Model> Children isNull(MF mf, Class<Left> left, MFunction<Left, ?> lf) {
        return where(true, mf, left, lf, Operator.IS_NULL, null);
    }

    default <Left extends Model> Children isNull(String left, MFunction<Left, ?> lf) {
        return where(true, left, lf, Operator.IS_NULL, null);
    }


    // is not null
    default public <Left extends Model> Children isNotNull(Class<Left> left, MFunction<Left, ?> lf) {
        return where(true, left, lf, Operator.IS_NOT_NULL, null);
    }

    default public <Left extends Model> Children isNotNull(MF mf, Class<Left> left, MFunction<Left, ?> lf) {
        return where(true, mf, left, lf, Operator.IS_NOT_NULL, null);
    }

    default public <Left extends Model> Children isNotNull(String left, MFunction<Left, ?> lf) {
        return where(true, left, lf, Operator.IS_NOT_NULL, null);
    }


    // between
    default <Left extends Model> Children between(Class<Left> left, MFunction<Left, ?> lf, Object v1, Object v2) {
        return where(true, left, lf, Operator.IS_NOT_NULL, new Object[]{v1, v2});
    }

    default <Left extends Model> Children between(MF mf, Class<Left> left, MFunction<Left, ?> lf, Object v1, Object v2) {
        return where(true, mf, left, lf, Operator.IS_NOT_NULL, new Object[]{v1, v2});
    }

    default <Left extends Model> Children between(String left, MFunction<Left, ?> lf, Object v1, Object v2) {
        return where(true, left, lf, Operator.IS_NOT_NULL, new Object[]{v1, v2});
    }

    default <Left extends Model> Children where(boolean condition, String table, Class<Left> left, String column, Operator operator, Object val) {
        table = WrapperUtil.autoTable(table, this, left);
        WrapperUtil.getQuerySet(this).where(table, column, operator, val);
        return chain();
    }

    default <Left extends Model> Children where(boolean condition, Class<Left> left, MFunction<Left, ?> lf, Operator operator, Object val) {
        String table = WrapperUtil.autoTable(null, this, left);
        return where(condition, table, lf.getName(), operator, val);
    }

    default <Left extends Model> Children where(boolean condition, MF mf, Class<Left> left, MFunction<Left, ?> lf, Operator operator, Object val) {
        return where(condition, mf.getName(), left, lf.getName(), operator, val);
    }

    default <Left extends Model> Children where(boolean condition, String left, MFunction<Left, ?> lf, Operator operator, Object val) {
        return where(condition, left, lf.getName(), operator, val);
    }

    Children where(boolean condition, String table, String column, Operator operator, Object val);

    //TODO::-------------------- 查询条件 END  --------------------
    //TODO::-------------------- 特性 START --------------------
    /**
     * 声明字段
     * @param tm 目标模型类
     * @param tfs 目标模型字段
     * @return 查询器
     * @param <TM> 目标模型类
     */
    <TM> Children field(Class<TM> tm, MFunction<TM, ?>... tfs);

    <TM> Children group(Class<TM> tm, MFunction<TM, ?>... tfs);

    <TM> Children having(Class<TM> tm, MFunction<TM, ?>... tfs);

    //TODO::-------------------- 特性 END --------------------
    //TODO::-------------------- 排序和数量 START --------------------
    <TM> Children order(Class<TM> tm, MFunction<TM, ?> tf, OrderByType type);

    default <Left> Children order(Class<Left> left, MFunction<Left, ?> lf, boolean desc) {
        return order(left, lf, desc ? OrderByType.DESC : OrderByType.ASC);
    }

    default <Left> Children order(Class<Left> left, MFunction<Left, ?> lf) {
        return order(left, lf, OrderByType.ASC);
    }

    //TODO::-------------------- 排序和数量 END --------------------

}
