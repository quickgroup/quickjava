package org.quickjava.orm.wrapper.conditions;

import java.io.Serializable;
import java.util.Map;
import java.util.function.BiPredicate;

public interface Condition<Children, R> extends Serializable {

    default Children eq(R column, Object val) {
        return this.eq(true, column, val);
    }

    Children eq(boolean condition, R column, Object val);

    default Children ne(R column, Object val) {
        return this.ne(true, column, val);
    }

    default Children neq(R column, Object val) {
        return this.ne(true, column, val);
    }

    Children ne(boolean condition, R column, Object val);

    default Children gt(R column, Object val) {
        return this.gt(true, column, val);
    }

    Children gt(boolean condition, R column, Object val);

    default Children ge(R column, Object val) {
        return this.ge(true, column, val);
    }

    default Children gte(R column, Object val) {
        return this.ge(true, column, val);
    }

    Children ge(boolean condition, R column, Object val);

    default Children lt(R column, Object val) {
        return this.lt(true, column, val);
    }

    Children lt(boolean condition, R column, Object val);

    default Children le(R column, Object val) {
        return this.le(true, column, val);
    }

    default Children lte(R column, Object val) {
        return this.le(true, column, val);
    }

    Children le(boolean condition, R column, Object val);

    default Children between(R column, Object val1, Object val2) {
        return this.between(true, column, val1, val2);
    }

    Children between(boolean condition, R column, Object val1, Object val2);

    default Children notBetween(R column, Object val1, Object val2) {
        return this.notBetween(true, column, val1, val2);
    }

    Children notBetween(boolean condition, R column, Object val1, Object val2);

    default Children like(R column, Object val) {
        return this.like(true, column, val);
    }

    Children like(boolean condition, R column, Object val);

    default Children notLike(R column, Object val) {
        return this.notLike(true, column, val);
    }

    Children notLike(boolean condition, R column, Object val);

    default Children likeLeft(R column, Object val) {
        return this.likeLeft(true, column, val);
    }

    Children likeLeft(boolean condition, R column, Object val);

    default Children likeRight(R column, Object val) {
        return this.likeRight(true, column, val);
    }

    Children likeRight(boolean condition, R column, Object val);

    default Children in(R column, Object ... val) {
        return this.in(true, column, val);
    }

    Children in(boolean condition, R column, Object ... val);

    default Children notIn(R column, Object ... val) {
        return this.notIn(true, column, val);
    }

    Children notIn(boolean condition, R column, Object ... val);

    default Children isNull(R column) {
        return this.isNull(true, column);
    }

    Children isNull(boolean condition, R column);

    default Children isNotNull(R column) {
        return this.isNotNull(true, column);
    }

    Children isNotNull(boolean condition, R column);
}
