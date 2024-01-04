package org.quickjava.orm.wrapper;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import org.quickjava.orm.Model;
import org.quickjava.orm.QueryReservoir;
import org.quickjava.orm.QuerySet;
import org.quickjava.orm.contain.DataMap;
import org.quickjava.orm.contain.ModelMeta;
import org.quickjava.orm.contain.Pagination;
import org.quickjava.orm.utils.ModelUtil;
import org.quickjava.orm.utils.SqlUtil;
import org.quickjava.orm.wrapper.conditions.JoinConditionBasic;
import org.quickjava.orm.wrapper.enums.JoinType;

import java.io.Serializable;
import java.util.List;
import java.util.function.Function;

public abstract class AbstractModelWrapper<Children extends AbstractModelWrapper<Children, M, R>, M extends Model, R extends MFunction<M, ?>>
        implements ModelJoin<Children, M, R>, Serializable {

    protected M model;

    protected Model model() {
        return model;
    }

    private Children chain() {
        return (Children) this;
    }

    public Children eq(R function, Object val) {
        model().eq(findFieldName(function), val);
        return chain();
    }

    public Children neq(R function, Object val) {
        model().neq(findFieldName(function), val);
        return chain();
    }

    public Children gt(R function, Object val) {
        model().gt(findFieldName(function), val);
        return chain();
    }

    public Children gte(R function, Object val) {
        model().gte(findFieldName(function), val);
        return chain();
    }

    public Children lt(R function, Object val) {
        model().lt(findFieldName(function), val);
        return chain();
    }

    public Children lte(R function, Object val) {
        model().lte(findFieldName(function), val);
        return chain();
    }

    public Children in(R function, Object ...args) {
        model().in(findFieldName(function), args);
        return chain();
    }

    public Children notIn(R function, Object ...args) {
        model().notIn(findFieldName(function), args);
        return chain();
    }

    public Children isNull(R function) {
        model().isNull(findFieldName(function));
        return chain();
    }

    public Children isNotNull(R function) {
        model().isNotNull(findFieldName(function));
        return chain();
    }

    public Children between(R function, Object v1, Object v2) {
        model().between(findFieldName(function), v1, v2);
        return chain();
    }

    public Children order(R function, boolean desc) {
        model().order(findFieldName(function), desc);
        return chain();
    }

    public Children order(R function) {
        return this.order(function, false);
    }

    public Children orderByDesc(R function) {
        model().order(findFieldName(function), true);
        return chain();
    }

    public Children orderByAsc(R function) {
        model().order(findFieldName(function), false);
        return chain();
    }

    public Children limit(int index, int count) {
        model().limit(index, count);
        return chain();
    }

    public Children limit(int count) {
        model().limit(count);
        return chain();
    }

    public Children page(int page) {
        model().page(page);
        return chain();
    }

    public Children page(int page, int pageSize) {
        model().page(page, pageSize);
        return chain();
    }

    public Children group(String fields) {
        model().group(fields);
        return chain();
    }

    public Children having(String fields) {
        model().having(fields);
        return chain();
    }

    public Children union(String sql) {
        model().union(sql);
        return chain();
    }

    public Children union(String[] sqlArr) {
        model().union(sqlArr);
        return chain();
    }

    public Children distinct(boolean distinct) {
        model().distinct(distinct);
        return chain();
    }

    public Children lock(boolean lock) {
        model().lock(lock);
        return chain();
    }

    public Children with(R func) {
        model().with(findFieldName(func));
        return chain();
    }

    public M insert() {
        return model().insert();
    }

    public M insert(DataMap data) {
        return model().insert(data);
    }

    public int delete() {
        return model().delete();
    }

    public M update() {
        return model().update();
    }

    public M update(DataMap data) {
        return model().update(data);
    }

    public M save(DataMap data) {
        return model().save(data);
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

    private String findFieldName(Function<?, ?> function) {
        return WrapperUtils.getFieldName(function);
    }

    /**
     * 关联查询实现
     */
    @Override
    public Children join(JoinType type, JoinConditionBasic<?> condition) {
        condition.items.forEach(it -> {
            ModelMeta left = getModelMeta(it.getLeft());
            String leftAlias = SqlUtil.isNotEmpty(it.getLeftAlias()) ? it.getLeftAlias() : left.table();
            ModelMeta right = it.getRight() == null ? ModelUtil.getMeta(this.model.getClass()) : getModelMeta(it.getRight());
            String rightAlias = SqlUtil.isNotEmpty(it.getRightAlias()) ? it.getRightAlias() : right.table();
            // 查询器
            QuerySet querySet = ReflectUtil.invoke(this.model, "query");
            QueryReservoir reservoir = (QueryReservoir) ReflectUtil.getFieldValue(querySet, "reservoir");
            // 主表字段
            if (ObjectUtil.isEmpty(reservoir.fieldList)) {
                ModelMeta main = ModelUtil.getMeta(this.model.getClass());
                loadModelAccurateFields(querySet, main, main.table());
            }
            // 声明左表数据字段
            if (it.isLoadLeftData()) {
                loadModelAccurateFields(querySet, left, leftAlias);
            }
            // 放到查询器
            String conditionSql = ModelUtil.joinConditionSql(
                    left.table(), leftAlias, it.getLeftFun().getFieldName(),
                    it.getType().name(),
                    right.table(), rightAlias, it.getRightFun().getFieldName()
            );
            querySet.join(left.table(), conditionSql, type.name());
        });
        return chain();
    }

    private static ModelMeta getModelMeta(Class<?> clazz) {
        if (Model.class.isAssignableFrom(clazz)) {
            if (!ModelUtil.metaExist(clazz)) {
                try {
                    clazz = ModelUtil.getModelClass(clazz);
                    clazz.newInstance();
                } catch (InstantiationException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return ModelUtil.getMeta(clazz);
    }

    private void loadModelAccurateFields(QuerySet querySet, ModelMeta meta, String table) {
        meta.getFieldList().forEach(i -> {
            if (i.isRelation() || !i.isExist()) {
                return;
            }
            querySet.field(SqlUtil.fieldAlias(table, i.name()));
        });
    }
}
