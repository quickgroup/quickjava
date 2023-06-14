package org.quickjava.orm.modelQuery;

import org.quickjava.orm.Model;
import org.quickjava.orm.contain.DataMap;
import org.quickjava.orm.contain.Pagination;

import java.io.Serializable;
import java.util.List;
import java.util.function.Function;

public abstract class ModelQueryWrapper<M extends Model, R extends Function<M, ?>> implements Serializable {

    protected M model;

    protected Integer integer = 10;

    protected Model model() {
        return (Model) model;
    }

    public ModelQueryWrapper<M, R> eq(R function, Object val)
    {
        model().eq(findFieldName(function), val);
        return this;
    }

    public ModelQueryWrapper<M, R> neq(R function, Object val)
    {
        model().neq(findFieldName(function), val);
        return this;
    }

    public ModelQueryWrapper<M, R> gt(R function, Object val) {
        model().gt(findFieldName(function), val);
        return this;
    }

    public ModelQueryWrapper<M, R> gte(R function, Object val) {
        model().gte(findFieldName(function), val);
        return this;
    }

    public ModelQueryWrapper<M, R> lt(R function, Object val) {
        model().lt(findFieldName(function), val);
        return this;
    }

    public ModelQueryWrapper<M, R> lte(R function, Object val) {
        model().lte(findFieldName(function), val);
        return this;
    }

    public ModelQueryWrapper<M, R> in(R function, Object ...args) {
        model().in(findFieldName(function), args);
        return this;
    }

    public ModelQueryWrapper<M, R> notIn(R function, Object ...args) {
        model().notIn(findFieldName(function), args);
        return this;
    }

    public ModelQueryWrapper<M, R> isNull(R function) {
        model().isNull(findFieldName(function));
        return this;
    }

    public ModelQueryWrapper<M, R> isNotNull(R function) {
        model().isNotNull(findFieldName(function));
        return this;
    }

    public ModelQueryWrapper<M, R> between(R function, Object v1, Object v2) {
        model().between(findFieldName(function), v1, v2);
        return this;
    }

    public ModelQueryWrapper<M, R> order(R function, boolean desc) {
        model().order(findFieldName(function), desc);
        return this;
    }

    public ModelQueryWrapper<M, R> order(R function) {
        return this.order(function, false);
    }

    public ModelQueryWrapper<M, R> limit(int index, int count) {
        model().limit(index, count);
        return this;
    }

    public ModelQueryWrapper<M, R> limit(int count) {
        model().limit(count);
        return this;
    }

    public ModelQueryWrapper<M, R> page(int page) {
        model().page(page);
        return this;
    }

    public ModelQueryWrapper<M, R> page(int page, int pageSize) {
        model().page(page, pageSize);
        return this;
    }

    public ModelQueryWrapper<M, R> group(String fields) {
        model().group(fields);
        return this;
    }

    public ModelQueryWrapper<M, R> having(String fields) {
        model().having(fields);
        return this;
    }

    public ModelQueryWrapper<M, R> union(String sql) {
        model().union(sql);
        return this;
    }

    public ModelQueryWrapper<M, R> union(String[] sqlArr) {
        model().union(sqlArr);
        return this;
    }

    public ModelQueryWrapper<M, R> distinct() {
        model().distinct();
        return this;
    }

    public ModelQueryWrapper<M, R> distinct(boolean distinct) {
        model().distinct(distinct);
        return this;
    }

    public ModelQueryWrapper<M, R> lock() {
        model().lock();
        return this;
    }

    public ModelQueryWrapper<M, R> lock(boolean lock) {
        model().lock(lock);
        return this;
    }

    public M insert() {
        return (M) model().insert();
    }

    public M insert(DataMap data) {
        return (M) model().insert(data);
    }

    public int delete() {
        return model().delete();
    }

    public M update() {
        return (M) model().update();
    }

    public M update(DataMap data) {
        return (M) model().update(data);
    }

    public M find() {
        return model().find();
    }

    public List<M> select() {
        return model().select();
    }

    public Pagination<M> pagination(int page, int pageSize) {
        return model().pagination(page, pageSize);
    }

    public Pagination<M> pagination() {
        return model().pagination();
    }

    @Override
    public String toString() {
        return model().toString();
    }

    private String findFieldName(R function) {
        return FunctionReflectionUtil.getFieldName((Function<?, ?>) function);
    }

}
