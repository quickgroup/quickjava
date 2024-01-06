package org.quickjava.orm.wrapper;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import org.quickjava.orm.Model;
import org.quickjava.orm.QueryReservoir;
import org.quickjava.orm.QuerySet;
import org.quickjava.orm.contain.DataMap;
import org.quickjava.orm.contain.ModelFieldMeta;
import org.quickjava.orm.contain.ModelMeta;
import org.quickjava.orm.contain.Pagination;
import org.quickjava.orm.utils.ModelUtil;
import org.quickjava.orm.utils.ORMHelper;
import org.quickjava.orm.utils.SqlUtil;
import org.quickjava.orm.wrapper.conditions.ModelJoin;
import org.quickjava.orm.wrapper.enums.JoinType;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public abstract class AbstractModelWrapper<Children extends AbstractModelWrapper<Children, M, R>, M extends Model, R extends MFunction<M, ?>>
        implements ModelJoinWrapper<Children, M, R>, Serializable {

    protected M model;

    protected Map<String, ModelJoin<?, ?>> joinMap;

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
        Pagination<Map<String, Object>> pagination = getQuerySet().pagination(page, pageSize);
        System.out.println("pagination=" + pagination);
        if (joinMap == null || joinMap.isEmpty()) {
            return null;
        }
        ModelMeta mainMeta = getModelMeta(this.model.getClass());
        Map<Class<?>, ModelFieldMeta> fieldMetaClazzMap = new LinkedHashMap<>();
        for (Field field : mainMeta.getClazz().getDeclaredFields()) {
            if (!fieldMetaClazzMap.containsKey(field.getType())) {
                fieldMetaClazzMap.put(field.getType(), new ModelFieldMeta(field));
            }
        }
        // 收集一对一关联信息（暂只支持两层即主模型.子模型，后续在关系上支持多层级，在关系上指定父模型的属性名
        List<M> models = new LinkedList<>();
        pagination.rows.forEach(data -> {
            // 主表
            M main = Model.newModel(mainMeta.getClazz());
            ORMHelper.resultTranshipmentWith(main, data, mainMeta.table());
            // 一对一的数据加载
            joinMap.forEach((alias, join) -> {
                Class<? extends Model> relationClazz = join.getLeft();
                if (!fieldMetaClazzMap.containsKey(relationClazz)) {
                    return;
                }
                // 关联模型元数据
                ModelMeta leftMeta = getModelMeta(join.getLeft());
                if (ObjectUtil.isNotEmpty(join.getLeftAlias()) && !ReflectUtil.hasField(mainMeta.getClazz(), join.getLeftAlias())) {
                    return;
                }
                // 是和主模型关联的条件
                ModelJoin.Item<?, ?> it = join.items.get(0);
                if (!it.getRight().equals(mainMeta.getClazz())) {
                    return;
                }
                String parentFieldName = join.getFieldFun() == null ? fieldMetaClazzMap.get(relationClazz).getName() : join.getFieldFun().getName();
                // 一对一
                Model left = Model.newModel(leftMeta.getClazz(), null, main);
                ORMHelper.resultTranshipmentWith(left, data, alias);
                ReflectUtil.setFieldValue(main, parentFieldName, left);
            });
            //
            models.add(main);
        });
        return new Pagination<>(pagination, models);
    }

    public Pagination<M> pagination() {
        return pagination(1, 20);
    }

    @Override
    public String toString() {
        return model().toString();
    }

    private String findFieldName(Function<?, ?> function) {
        return WrapperUtils.getFieldName(function);
    }

    private QuerySet getQuerySet() {
        return ReflectUtil.invoke(this.model, "query");
    }

    /**
     * 关联查询实现
     */
    @Override
    public Children join(JoinType type, ModelJoin<?, ?> join) {
        // 查询器
        ModelMeta mainMeta = getModelMeta(this.model.getClass());
        QuerySet querySet = getQuerySet();
        QueryReservoir reservoir = (QueryReservoir) ReflectUtil.getFieldValue(querySet, "reservoir");
        // 主表字段
        if (ObjectUtil.isEmpty(reservoir.fieldList)) {
            ModelMeta main = ModelUtil.getMeta(this.model.getClass());
            loadModelAccurateFields(querySet, main, main.table());
        }
        // 关联表元信息
        ModelMeta left = getModelMeta(join.getLeft());
        String leftAlias = SqlUtil.isNotEmpty(join.getLeftAlias()) ? join.getLeftAlias() : left.table();
        // 声明左表数据字段
        if (join.isLoadLeftData()) {
            loadModelAccurateFields(querySet, left, leftAlias);
        }
        // 缓存条件
        if (joinMap == null) {
            joinMap = new LinkedHashMap<>();
        }
        // 会覆盖之前的
        joinMap.put(leftAlias, join);

        // 查询条件设置（支持多个
        join.items.forEach(it -> {
            // 右条件是主表
            it.setRight(it.getRight() == null ? (Class<? extends Model>) mainMeta.getClazz() : it.getRight());
            // 模型元信息
            ModelMeta right = getModelMeta(it.getRight());
            String rightAlias = SqlUtil.isNotEmpty(it.getRightAlias()) ? it.getRightAlias() : right.table();
            // 放到查询器
            String conditionSql = ModelUtil.joinConditionSql(
                    leftAlias, it.getLeftFun().getFieldName(),
                    it.getType().name(),
                    rightAlias, it.getRightFun().getFieldName()
            );
            querySet.join(left.tableAlias(leftAlias), conditionSql, type.name());
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
            // 忽略：关联属性、不存在字段、模型字段
            if (i.isRelation() || !i.isExist() || Model.class.isAssignableFrom(i.getClazz())) {
                return;
            }
            querySet.field(SqlUtil.fieldAlias(table, i.name()));
        });
    }
}
