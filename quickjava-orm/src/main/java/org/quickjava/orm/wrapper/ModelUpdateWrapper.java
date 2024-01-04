package org.quickjava.orm.wrapper;

import org.quickjava.orm.Model;
import org.quickjava.orm.contain.DataMap;
import org.quickjava.orm.contain.Pagination;

import java.io.Serializable;
import java.util.List;
import java.util.function.Function;

public abstract class ModelUpdateWrapper<M extends Model, R extends MFunction<M, ?>>
        implements Compare<ModelUpdateWrapper<M, R>, R>, Serializable {

    protected M model;

    protected Model model() {
        return (Model) model;
    }

    public ModelUpdateWrapper<M, R> eq(R function, Object val)
    {
        model().eq(findFieldName(function), val);
        return this;
    }

    public ModelUpdateWrapper<M, R> gt(R function, Object val) {
        model().gt(findFieldName(function), val);
        return this;
    }

    public ModelUpdateWrapper<M, R> gte(R function, Object val) {
        model().gte(findFieldName(function), val);
        return this;
    }

    public ModelUpdateWrapper<M, R> lt(R function, Object val) {
        model().lt(findFieldName(function), val);
        return this;
    }

    public ModelUpdateWrapper<M, R> lte(R function, Object val) {
        model().lte(findFieldName(function), val);
        return this;
    }

    public ModelUpdateWrapper<M, R> in(R function, Object ...args) {
        model().in(findFieldName(function), args);
        return this;
    }

    public ModelUpdateWrapper<M, R> notIn(R function, Object ...args) {
        model().notIn(findFieldName(function), args);
        return this;
    }

    public ModelUpdateWrapper<M, R> isNull(R function) {
        model().isNull(findFieldName(function));
        return this;
    }

    public ModelUpdateWrapper<M, R> isNotNull(R function) {
        model().isNotNull(findFieldName(function));
        return this;
    }

    public ModelUpdateWrapper<M, R> between(R function, Object v1, Object v2) {
        model().between(findFieldName(function), v1, v2);
        return this;
    }

    public ModelUpdateWrapper<M, R> order(R function, boolean desc) {
        model().order(findFieldName(function), desc);
        return this;
    }

    public ModelUpdateWrapper<M, R> order(R function) {
        return this.order(function, false);
    }

    public ModelUpdateWrapper<M, R> limit(int index, int count) {
        model().limit(index, count);
        return this;
    }

    public ModelUpdateWrapper<M, R> limit(int count) {
        model().limit(count);
        return this;
    }

    public ModelUpdateWrapper<M, R> page(int page) {
        model().page(page);
        return this;
    }

    public ModelUpdateWrapper<M, R> page(int page, int pageSize) {
        model().page(page, pageSize);
        return this;
    }

    public ModelUpdateWrapper<M, R> group(String fields) {
        model().group(fields);
        return this;
    }

    public ModelUpdateWrapper<M, R> having(String fields) {
        model().having(fields);
        return this;
    }

    public ModelUpdateWrapper<M, R> union(String sql) {
        model().union(sql);
        return this;
    }

    public ModelUpdateWrapper<M, R> union(String[] sqlArr) {
        model().union(sqlArr);
        return this;
    }

    public ModelUpdateWrapper<M, R> distinct(boolean distinct) {
        model().distinct(distinct);
        return this;
    }

    public ModelUpdateWrapper<M, R> lock(boolean lock) {
        model().lock(lock);
        return this;
    }

    public ModelUpdateWrapper<M, R> with(R func) {
        model().with(findFieldName(func));
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

    public M save(DataMap data) {
        return (M) model().save(data);
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
        return function.getName();
    }

}
