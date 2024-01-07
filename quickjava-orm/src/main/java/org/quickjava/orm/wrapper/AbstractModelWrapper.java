package org.quickjava.orm.wrapper;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import org.quickjava.orm.model.Model;
import org.quickjava.orm.query.QueryReservoir;
import org.quickjava.orm.query.QuerySet;
import org.quickjava.orm.contain.*;
import org.quickjava.orm.query.enums.Operator;
import org.quickjava.orm.model.contain.ModelFieldMeta;
import org.quickjava.orm.model.contain.ModelMeta;
import org.quickjava.orm.model.ModelUtil;
import org.quickjava.orm.query.enums.OrderByType;
import org.quickjava.orm.utils.SqlUtil;
import org.quickjava.orm.wrapper.join.JoinSpecifyBase;
import org.quickjava.orm.wrapper.enums.JoinType;
import org.quickjava.orm.wrapper.join.ModelJoinWrapper;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public abstract class AbstractModelWrapper<Children extends AbstractModelWrapper<Children, M, R>, M extends Model, R extends MFunction<M, ?>>
        implements Wrapper<Children>, ModelJoinWrapper<Children, M, R>, Serializable {

    protected M model;

    protected Map<String, JoinSpecifyBase<?, ?>> joinMap;

    protected Model model() {
        return model;
    }

    public Children eq(R function, Object val) {
        model().eq(findFieldName(function), val);
        return chain();
    }

    @Override
    public Children where(boolean condition, String table, String column, Operator operator, Object val) {
        if (condition) {
            getQuerySet().where(table, column, operator, val);
        }
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

    @Override
    public Children order(String table, String field, OrderByType type) {
        getQuerySet().order(table, field, type);
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

    public Children group(R ... fields) {
        for (R field : fields) {
            getQuerySet().group(field.getName());
        }
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

        // 装载主表和一对一关联表数据（暂只支持两层即主模型.子模型，后续在关系上支持多层级，在关系上指定父模型的属性名
        List<M> models = new LinkedList<>();
        pagination.rows.forEach(data -> {
            // 主表
            M main = Model.newModel(mainMeta.getClazz());
            ModelUtil.resultTranshipmentWith(main, data, mainMeta.table());
            // 一对一的数据加载
            joinMap.forEach((alias, join) -> {
                // 未加载数据 || 未设置加载对应属性名 || 在父实体不存在
                if (!join.isLoadLeftData() || ModelUtil.isEmpty(join.getLeftAlias()) || !ReflectUtil.hasField(mainMeta.getClazz(), join.getLeftAlias())) {
                    return;
                }
                // 关联模型元数据
                ModelMeta leftMeta = getModelMeta(join.getLeft());
                // 是和主模型关联的条件
                JoinSpecifyBase.Item<?, ?> it = join.onList.get(0);
                if (!it.getRight().equals(mainMeta.getClazz())) {
                    return;
                }
                // 数据装载
                Model left = Model.newModel(leftMeta.getClazz(), null, main);
                ModelUtil.resultTranshipmentWith(left, data, alias);
                ReflectUtil.setFieldValue(main, join.getLeftAlias(), left);
            });
            //
            models.add(main);
        });
        // 一对多
        return new Pagination<>(pagination, models);
    }

    public Pagination<M> pagination() {
        return pagination(1, 20);
    }

    @Override
    public String toString() {
        return model().toString();
    }

    private String findFieldName(MFunction<?, ?> function) {
        return function.getName();
    }

    private QuerySet getQuerySet() {
        return WrapperUtil.getQuerySet(this.model);
    }

    /**
     * 关联查询实现
     */
    @Override
    public Children join(JoinType type, JoinSpecifyBase<?, ?> join) {
        // 为空过滤
        if (join.onList.isEmpty()) {
            return chain();
        }
        // 查询器
        ModelMeta mainMeta = getModelMeta(this.model.getClass());
        QuerySet querySet = getQuerySet();
        QueryReservoir queryReservoir = (QueryReservoir) ReflectUtil.getFieldValue(querySet, "reservoir");
        // 主表字段
        if (ObjectUtil.isEmpty(queryReservoir.fieldList)) {
            ModelMeta main = ModelUtil.getMeta(this.model.getClass());
            loadModelAccurateFields(querySet, main, main.table());
        }
        // 关联表元信息
        ModelMeta leftMeta = getModelMeta(join.getLeft());
        if (leftMeta == null) {
            return chain();
        }
        // 如果指定在父实体上的属性
        String leftAlias = join.getLeftAlias();
        // 在主表上的属性名
        if (leftAlias == null) {
            ModelFieldMeta fieldMeta = getModelClazzFieldMap(mainMeta).get(join.getLeft());
            leftAlias = fieldMeta == null ? null : fieldMeta.getName();
        }
        // 表名
        if (leftAlias == null) {
            leftAlias = leftMeta.table();
        }
        // 声明左表数据字段
        if (join.isLoadLeftData() && ModelUtil.isNotEmpty(leftAlias)) {
            loadModelAccurateFields(querySet, leftMeta, leftAlias);
        }
        // 缓存条件
        if (joinMap == null) {
            joinMap = new LinkedHashMap<>();
        }
        // 会覆盖之前的
        joinMap.put(leftAlias, join);
        join.setLeftAlias(leftAlias);

        // 查询条件设置（支持多个
        List<String> onConditions = new LinkedList<>();
        join.onList.forEach(it -> {
            // 右条件是主表
            it.setRight(it.getRight() == null ? (Class<? extends Model>) mainMeta.getClazz() : it.getRight());
            // 模型元信息
            ModelMeta right = getModelMeta(it.getRight());
            String rightAlias = SqlUtil.isNotEmpty(it.getRightAlias()) ? it.getRightAlias() : right.table();
            if (it.getRightValue() != null) {
                // 一：值条件
                onConditions.add(ModelUtil.joinConditionSql(
                        join.getLeftAlias(), it.getLeftFun().getName(),
                        it.getType().name(),
                        String.valueOf(it.getRightValue())   // 后面下放到驱动进行转换
                ));
            } else {
                // 一：方法引用
                onConditions.add(ModelUtil.joinConditionSql(
                        join.getLeftAlias(), it.getLeftFun().getName(),
                        it.getType().name(),
                        rightAlias, it.getRightFun().getName()
                ));
            }
        });
        querySet.join(leftMeta.tableAlias(leftAlias), StrUtil.join(" AND ", onConditions), type.name());

        return chain();
    }

    private static ModelMeta getModelMeta(Class<?> clazz) {
        return WrapperUtil.getModelMeta(clazz);
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

    private Map<Class<?>, ModelFieldMeta> getModelClazzFieldMap(ModelMeta meta) {
        return WrapperUtil.getModelClazzFieldMap(meta);
    }

    private Map<String, ModelFieldMeta> getModelNameFieldMap(ModelMeta meta) {
        Map<String, ModelFieldMeta> fieldMetaClazzMap = new LinkedHashMap<>();
        for (Field field : meta.getClazz().getDeclaredFields()) {
            if (!fieldMetaClazzMap.containsKey(field.getName())) {
                fieldMetaClazzMap.put(field.getName(), new ModelFieldMeta(field));
            }
        }
        return fieldMetaClazzMap;
    }
}
