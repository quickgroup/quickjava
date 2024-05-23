package org.quickjava.orm.wrapper;

import org.quickjava.orm.enums.LogicType;
import org.quickjava.orm.model.Model;
import org.quickjava.orm.query.build.Where;
import org.quickjava.orm.query.build.WhereAnd;
import org.quickjava.orm.query.build.WhereOr;
import org.quickjava.orm.query.enums.Operator;

/**
 * 基础条件定义
 */
public interface AbstractWhere<Children, M extends Model, Func extends MFunction<M, ?>> extends Wrapper<Children> {

    Children where(Where where);

    default Children where(LogicType logic, boolean condition, String table, String column, Operator operator, Object value) {
        if (condition) {
            if (logic == LogicType.AND) {
                where(new WhereAnd(table, column, operator, value));
            } else {
                where(new WhereOr(table, column, operator, value));
            }
        }
        return chain();
    }

    /**
     * 指定表查询条件
     * @param table 表名（为空时使用主表名称
     * @param column 字段名
     * @param operator 条件
     * @param val 查询值
     */
    default Children where(boolean condition, String table, String column, Operator operator, Object val) {
        return this.where(LogicType.AND, condition, table, column, operator, val);
    }

    default Children where(boolean condition, String table, String column, Object val) {
        return this.where(LogicType.AND, condition, table, column, Operator.EQ, val);
    }

    /**
     * 主表查询条件
     */
    default Children where(boolean condition, String column, Operator operator, Object val) {
        return this.where(condition, null, column, operator, val);
    }

    default Children where(String column, Operator operator, Object val) {
        return this.where(true, null, column, operator, val);
    }

    default Children where(String column, Object val) {
        return this.where(true, null, column, Operator.EQ, val);
    }

    /// 子条件组
//    default Children where(String column, Object val) {
//        return this.where(true, null, column, Operator.EQ, val);
//    }

    /**
     * or查询
     */
    default Children whereOr(boolean condition, String table, String column, Operator operator, Object val) {
        return this.where(LogicType.OR, condition, null, column, operator, val);
    }

    //TODO::--------------- 抽离条件 ---------------
    default Children eq(Func column, Object val) {
        return this.eq(true, column, val);
    }

    default Children eq(boolean condition, Func column, Object val) {
        this.where(condition, column.getName(), Operator.EQ, val);
        return chain();
    }

    default Children ne(Func column, Object val) {
        return this.ne(true, column, val);
    }

    default Children neq(Func column, Object val) {
        return this.ne(true, column, val);
    }

    default Children ne(boolean condition, Func column, Object val) {
        return where(condition, parseColumnName(column), Operator.NEQ, val);
    }

    default Children gt(Func column, Object val) {
        return this.gt(true, column, val);
    }

    default Children gt(boolean condition, Func column, Object val) {
        return where(condition, parseColumnName(column), Operator.GT, val);
    }

    default Children ge(Func column, Object val) {
        return this.ge(true, column, val);
    }

    default Children gte(Func column, Object val) {
        return this.ge(true, column, val);
    }

    default Children ge(boolean condition, Func column, Object val) {
        return where(condition, parseColumnName(column), Operator.GTE, val);
    }

    default Children lt(Func column, Object val) {
        return this.lt(true, column, val);
    }

    default Children lt(boolean condition, Func column, Object val) {
        return where(condition, parseColumnName(column), Operator.LT, val);
    }

    default Children le(Func column, Object val) {
        return this.le(true, column, val);
    }

    default Children lte(Func column, Object val) {
        return this.le(true, column, val);
    }

    default Children le(boolean condition, Func column, Object val) {
        return where(condition, parseColumnName(column), Operator.LTE, val);
    }

    default Children between(Func column, Object val1, Object val2) {
        return this.between(true, column, val1, val2);
    }

    default Children between(boolean condition, Func column, Object v1, Object v2) {
        return where(condition, parseColumnName(column), Operator.BETWEEN, new Object[]{v1, v2});
    }

    default Children notBetween(Func column, Object val1, Object val2) {
        return this.notBetween(true, column, val1, val2);
    }

    default Children notBetween(boolean condition, Func column, Object v1, Object v2) {
        return where(condition, parseColumnName(column), Operator.NOT_BETWEEN, new Object[]{v1, v2});
    }

    default Children like(Func column, Object val) {
        return this.like(true, column, val);
    }

    default Children like(boolean condition, Func column, Object val) {
        return where(condition, parseColumnName(column), Operator.LIKE, val);
    }

    default Children notLike(Func column, Object val) {
        return this.notLike(true, column, val);
    }

    default Children notLike(boolean condition, Func column, Object val) {
        return where(condition, parseColumnName(column), Operator.NOT_LIKE, val);
    }

    default Children likeLeft(Func column, Object val) {
        return this.likeLeft(true, column, val);
    }

    default Children likeLeft(boolean condition, Func column, Object val) {
        return where(condition, parseColumnName(column), Operator.LIKE_LEFT, val);
    }

    default Children likeRight(Func column, Object val) {
        return this.likeRight(true, column, val);
    }

    default Children likeRight(boolean condition, Func column, Object val) {
        return where(condition, parseColumnName(column), Operator.LIKE_RIGHT, val);
    }

    default Children in(Func column, Object ... val) {
        return this.in(true, column, val);
    }

    default Children in(boolean condition, Func column, Object ... valArr) {
        return where(condition, parseColumnName(column), Operator.IN, valArr);
    }

    default Children notIn(Func column, Object ... val) {
        return this.notIn(true, column, val);
    }

    default Children notIn(boolean condition, Func column, Object ... valArr) {
        return where(condition, parseColumnName(column), Operator.NOT_IN, valArr);
    }

    default Children isNull(Func column) {
        return this.isNull(true, column);
    }

    default Children isNull(boolean condition, Func column) {
        return where(condition, parseColumnName(column), Operator.IS_NULL, null);
    }

    default Children isNotNull(Func column) {
        return this.isNotNull(true, column);
    }

    default Children isNotNull(boolean condition, Func column) {
        return where(condition, parseColumnName(column), Operator.IS_NOT_NULL, null);
    }

    default String parseColumnName(MFunction<?, ?> function) {
        return function.getName();
    }
}
