package org.quickjava.orm.wrapper.join;

import org.quickjava.orm.enums.LogicType;
import org.quickjava.orm.model.Model;
import org.quickjava.orm.query.enums.Operator;
import org.quickjava.orm.query.enums.OrderByType;
import org.quickjava.orm.wrapper.AbstractWrapperWhere;
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
        extends AbstractWrapperWhere<Children, M, MF>, Serializable {

    /**
     * 与主表一个条件关联
     */
    default <Relation extends Model> Children leftJoin(Class<Relation> left, MFunction<Relation, ?> lf, MF rf) {
        return join(JoinType.LEFT, new JoinSpecify<Relation, M>(left).eq(lf, rf));
    }

    /**
     * 与主表一个条件关联，并指定关联表别名
     * TODO::涉及关联表别名的方法，表class后第一个参数就是指定别名
     * @param alias 关联表别名，用于：相同表多次关联、关联表数据加载到right表属性名
     */
    default <Relation extends Model> Children leftJoin(Class<Relation> left, MF alias, MFunction<Relation, ?> lf, MF rf) {
        return leftJoin(left, alias.getName(), lf, rf);
    }

    default <Relation extends Model> Children leftJoin(Class<Relation> left, String alias, MFunction<Relation, ?> lf, MF rf) {
        return join(JoinType.LEFT, new JoinSpecify<Relation, M>(left, alias).eq(lf, rf));
    }

    /**
     * 与主表一个条件关联[指定属性]
     * @param dataField 关联数据写入到right表属性
     */
    default <Relation extends Model> Children leftJoinData(Class<Relation> left, MF alias, MFunction<Relation, ?> lf, MF rf, MF dataField) {
        return leftJoinData(left, alias.getName(), lf, rf, dataField);
    }

    default <Relation extends Model> Children leftJoinData(Class<Relation> left, String alias, MFunction<Relation, ?> lf, MF rf, MF dataField) {
        return join(JoinType.LEFT, new JoinSpecify<Relation, M>(left, alias).setLoadData(dataField).eq(lf, rf));
    }

    /**
     * 两张子表一个条件关联
     * @return Children
     */
    default <Left extends Model, Right extends Model> Children leftJoin(Class<Left> left, MFunction<Left, ?> lf,
                                                                        Class<Right> right, MFunction<Right, ?> rf) {
        return join(JoinType.LEFT, new JoinSpecify<Left, Right>(left).setRight(right).eq(lf, rf));
    }

    /**
     * 两张子表一个条件关联
     * @param left 左表类
     * @param leftAlias 左表别名，传入null默认使用表全名
     * @param lf 左表属性名
     * @param right 右表类
     * @param rightAlias 右表别名，传入null默认使用表全名
     * @param rf 右表属性名
     */
    default <Left extends Model, Right extends Model> Children leftJoin(Class<Left> left, String leftAlias, MFunction<Left, ?> lf,
                                                                        Class<Right> right, String rightAlias, MFunction<Right, ?> rf) {
        return join(JoinType.LEFT, new JoinSpecify<Left, Right>(left, leftAlias).setRight(right, rightAlias).eq(lf, rf));
    }

    /**
     * 与主表多条件关联
     */
    default <Left extends Model> Children leftJoin(Class<Left> left, JoinSpecifyClosure<Left, M> closure) {
        JoinSpecify<Left, M> condition = new JoinSpecify<Left, M>(left);
        closure.call(condition);
        return join(JoinType.LEFT, condition);
    }

    default <Left extends Model> Children leftJoin(Class<Left> left, JoinSpecifyClosure<Left, M> closure, MF rightField) {
        JoinSpecify<Left, M> condition = new JoinSpecify<Left, M>(left).setLoadData(rightField);
        closure.call(condition);
        return join(JoinType.LEFT, condition);
    }

    /**
     * 两张子表多个条件关联
     * @return Children
     */
    default <Left extends Model, Right extends Model> Children leftJoin(Class<Left> left, Class<Right> right, JoinSpecifyClosure<Left, Right> closure) {
        JoinSpecify<Left, Right> condition = new JoinSpecify<Left, Right>(left, right);
        closure.call(condition);
        return join(JoinType.LEFT, condition);
    }

    /**
     * 两张子表多个条件关联
     * @return Children
     */
    default <Left extends Model, Right extends Model> Children leftJoin(Class<Left> left, Class<Right> right, JoinSpecifyClosure<Left, Right> closure, MF alias) {
        JoinSpecify<Left, Right> condition = new JoinSpecify<Left, Right>(left, right);
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

    //TODO::-------------------- 关联表查询条件 START --------------------
    default <Left extends Model> Children where(boolean condition, Class<Left> left, String alias, MFunction<Left, ?> lf, Operator operator, Object val) {
        String leftTable = WrapperUtil.autoTable(null, this, left);
        if (alias != null) {
            leftTable = alias;
        }
        return where(condition, leftTable, lf.getName(), operator, val);
    }

    default <Left extends Model> Children where(boolean condition, Class<Left> left, MF alias, MFunction<Left, ?> lf, Operator operator, Object val) {
        return where(condition, left, alias.getName(), lf, operator, val);
    }

    //TODO::-------------------- 关联表查询条件抽离 START --------------------
    // 自动识别查询表名
    default <Left extends Model> Children eq(boolean condition, Class<Left> left, String alias, MFunction<Left, ?> lf, Object val) {
        return where(condition, left, alias, lf, Operator.EQ, val);
    }

    default <Left extends Model> Children eq(Class<Left> left, MFunction<Left, ?> lf, Object val) {
        return eq(true, left, null, lf, val);
    }

    default <Left extends Model> Children eq(Class<Left> left, MF alias, MFunction<Left, ?> lf, Object val) {
        return eq(true, left, alias.getName(), lf, val);
    }

    default <Left extends Model> Children eq(Class<Left> left, String alias, MFunction<Left, ?> lf, Object val) {
        return eq(true, left, alias, lf, val);
    }


    // 不等于
    default <Left extends Model> Children neq(boolean condition, Class<Left> left, String alias, MFunction<Left, ?> lf, Object val) {
        return where(condition, left, alias, lf, Operator.NEQ, val);
    }

    default <Left extends Model> Children neq(Class<Left> left, MFunction<Left, ?> lf, Object val) {
        return neq(true, left, null, lf, val);
    }

    default <Left extends Model> Children neq(Class<Left> left, MF alias, MFunction<Left, ?> lf, Object val) {
        return neq(true, left, alias.getName(), lf, val);
    }

    default <Left extends Model> Children neq(Class<Left> left, String alias, MFunction<Left, ?> lf, Object val) {
        return neq(true, left, alias, lf, val);
    }


    // 大于
    default <Left extends Model> Children gt(boolean condition, Class<Left> left, String alias, MFunction<Left, ?> lf, Object val) {
        return where(condition, left, alias, lf, Operator.GT, val);
    }

    default <Left extends Model> Children gt(Class<Left> left, MFunction<Left, ?> lf, Object val) {
        return gt(true, left, null, lf, val);
    }

    default <Left extends Model> Children gt(Class<Left> left, MF alias, MFunction<Left, ?> lf, Object val) {
        return gt(true, left, alias.getName(), lf, val);
    }

    default <Left extends Model> Children gt(Class<Left> left, String alias, MFunction<Left, ?> lf, Object val) {
        return gt(true, left, alias, lf, val);
    }


    // 大于等于
    default <Left extends Model> Children gte(Class<Left> left, MFunction<Left, ?> lf, Object val) {
        return where(true, left, lf, Operator.GTE, val);
    }

    default <Left extends Model> Children gte(Class<Left> left, MF alias, MFunction<Left, ?> lf, Object val) {
        return where(true, left, alias, lf, Operator.GTE, val);
    }

    default <Left extends Model> Children gte(String left, MFunction<Left, ?> lf, Object val) {
        return where(true, left, lf, Operator.GTE, val);
    }


    // 小于
    default <Left extends Model> Children lt(Class<Left> left, MFunction<Left, ?> lf, Object val) {
        return where(true, left, lf, Operator.LT, val);
    }

    default <Left extends Model> Children lt(Class<Left> left, MF alias, MFunction<Left, ?> lf, Object val) {
        return where(true, left, alias, lf, Operator.LT, val);
    }

    default <Left extends Model> Children lt(String left, MFunction<Left, ?> lf, Object val) {
        return where(true, left, lf, Operator.LT, val);
    }


    // 小于等于
    default <Left extends Model> Children lte(Class<Left> left, MFunction<Left, ?> lf, Object val) {
        return where(true, left, lf, Operator.LTE, val);
    }

    default <Left extends Model> Children lte(Class<Left> left, MF alias, MFunction<Left, ?> lf, Object val) {
        return where(true, left, alias, lf, Operator.LTE, val);
    }

    default <Left extends Model> Children lte(String left, MFunction<Left, ?> lf, Object val) {
        return where(true, left, lf, Operator.LTE, val);
    }


    // in
    default <Left extends Model> Children in(Class<Left> left, MFunction<Left, ?> lf, Object ...args) {
        return where(true, left, lf, Operator.IN, args);
    }

    default <Left extends Model> Children in(Class<Left> left, MF alias, MFunction<Left, ?> lf, Object ...args) {
        return where(true, left, alias, lf, Operator.IN, args);
    }

    default <Left extends Model> Children in(String left, MFunction<Left, ?> lf, Object ...args) {
        return where(true, left, lf, Operator.IN, args);
    }


    // not in
    default <Left extends Model> Children notIn(Class<Left> left, MFunction<Left, ?> lf, Object ...args) {
        return where(true, left, lf, Operator.IN, args);
    }

    default <Left extends Model> Children notIn(Class<Left> left, MF alias, MFunction<Left, ?> lf, Object ...args) {
        return where(true, left, alias, lf, Operator.IN, args);
    }

    default <Left extends Model> Children notIn(String left, MFunction<Left, ?> lf, Object ...args) {
        return where(true, left, lf, Operator.IN, args);
    }


    // is null
    default <Left extends Model> Children isNull(Class<Left> left, MFunction<Left, ?> lf) {
        return where(true, left, lf, Operator.IS_NULL, null);
    }

    default <Left extends Model> Children isNull(Class<Left> left, MF alias, MFunction<Left, ?> lf) {
        return where(true, left, alias, lf, Operator.IS_NULL, null);
    }

    default <Left extends Model> Children isNull(String left, MFunction<Left, ?> lf) {
        return where(true, left, lf, Operator.IS_NULL, null);
    }


    // is not null
    default public <Left extends Model> Children isNotNull(Class<Left> left, MFunction<Left, ?> lf) {
        return where(true, left, lf, Operator.IS_NOT_NULL, null);
    }

    default public <Left extends Model> Children isNotNull(Class<Left> left, MF alias, MFunction<Left, ?> lf) {
        return where(true, left, alias, lf, Operator.IS_NOT_NULL, null);
    }

    default public <Left extends Model> Children isNotNull(String left, MFunction<Left, ?> lf) {
        return where(true, left, lf, Operator.IS_NOT_NULL, null);
    }


    // between
    default <Left extends Model> Children between(Class<Left> left, MFunction<Left, ?> lf, Object v1, Object v2) {
        return where(true, left, lf, Operator.IS_NOT_NULL, new Object[]{v1, v2});
    }

    default <Left extends Model> Children between(Class<Left> left, MF alias, MFunction<Left, ?> lf, Object v1, Object v2) {
        return where(true, left, alias, lf, Operator.IS_NOT_NULL, new Object[]{v1, v2});
    }

    default <Left extends Model> Children between(String left, MFunction<Left, ?> lf, Object v1, Object v2) {
        return where(true, left, lf, Operator.IS_NOT_NULL, new Object[]{v1, v2});
    }

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
