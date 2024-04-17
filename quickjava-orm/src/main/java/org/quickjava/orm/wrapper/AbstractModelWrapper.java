package org.quickjava.orm.wrapper;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import org.quickjava.orm.contain.DataMap;
import org.quickjava.orm.contain.IPagination;
import org.quickjava.orm.contain.Pagination;
import org.quickjava.orm.enums.JoinType;
import org.quickjava.orm.model.Model;
import org.quickjava.orm.model.ModelHelper;
import org.quickjava.orm.model.contain.ModelFieldMeta;
import org.quickjava.orm.model.contain.ModelMeta;
import org.quickjava.orm.query.QueryReservoir;
import org.quickjava.orm.query.QuerySet;
import org.quickjava.orm.query.QuerySetHelper;
import org.quickjava.orm.query.build.JoinCondition;
import org.quickjava.orm.query.enums.Operator;
import org.quickjava.orm.query.enums.OrderByType;
import org.quickjava.orm.utils.SqlUtil;
import org.quickjava.orm.wrapper.join.JoinSpecifyBase;
import org.quickjava.orm.wrapper.join.ModelJoinWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public abstract class AbstractModelWrapper<Children extends AbstractModelWrapper<Children, M, R>, M extends Model, R extends MFunction<M, ?>>
        implements Wrapper<Children>, ModelJoinWrapper<Children, M, R>, Serializable {

    protected static final Logger logger = LoggerFactory.getLogger(AbstractModelWrapper.class);

    protected M model;

    protected Class<M> modelClazz;

    protected Map<String, JoinSpecifyBase<?, ?>> joinMap;

    protected Model model() {
        return model;
    }

    public Children eq(R func, Object val) {
        return eq(true, func, val);
    }

    public Children eq(boolean condition, R func, Object val) {
        return where(condition, null, findFieldName(func), Operator.EQ, val);
    }

    public Children neq(R func, Object val) {
        return neq(true, func, val);
    }

    public Children ne(R func, Object val) {
        return neq(true, func, val);
    }

    public Children neq(boolean condition, R func, Object val) {
        return where(condition, null, findFieldName(func), Operator.NEQ, val);
    }

    public Children gt(R func, Object val) {
        return gt(true, func, val);
    }

    public Children gt(boolean condition, R func, Object val) {
        return where(condition, null, findFieldName(func), Operator.GT, val);
    }

    public Children gte(R function, Object val) {
        return gte(true, function, val);
    }

    public Children ge(R function, Object val) {
        return gte(true, function, val);
    }

    public Children gte(boolean condition, R func, Object val) {
        return where(condition, null, findFieldName(func), Operator.GTE, val);
    }

    public Children lt(R func, Object val) {
        return lt(true, func, val);
    }

    public Children lt(boolean condition, R function, Object val) {
        return where(condition, null, findFieldName(function), Operator.LT, val);
    }

    public Children lte(R func, Object val) {
        return lte(true, func, val);
    }

    public Children le(R func, Object val) {
        return lte(true, func, val);
    }

    public Children lte(boolean condition, R function, Object val) {
        return where(condition, null, findFieldName(function), Operator.LTE, val);
    }

    public Children in(R func, Object ...args) {
        return in(true, func, args);
    }

    public Children in(boolean condition, R func, Object ... args) {
        return where(condition, null, findFieldName(func), Operator.IN, args);
    }

    public Children notIn(R func, Object ...args) {
        return notIn(true, func, args);
    }

    public Children notIn(boolean condition, R function, Object ... args) {
        return where(condition, null, findFieldName(function), Operator.NOT_IN, args);
    }

    public Children isNull(R func) {
        return isNull(true, func);
    }

    public Children isNull(boolean condition, R func) {
        return where(condition, null, findFieldName(func), Operator.IS_NULL, null);
    }

    public Children isNotNull(R func) {
        return isNotNull(true, func);
    }

    public Children isNotNull(boolean condition, R func) {
        return where(condition, null, findFieldName(func), Operator.IS_NOT_NULL, null);
    }

    public Children between(R func, Object v1, Object v2) {
        return between(true, func, v1, v2);
    }

    public Children between(boolean condition, R func, Object v1, Object v2) {
        return where(condition, null, findFieldName(func), Operator.BETWEEN, new Object[]{v1, v2});
    }

    public Children like(R func, Object val) {
        return like(true, func, val);
    }

    public Children like(boolean condition, R function, Object val) {
        return where(condition, null, findFieldName(function), Operator.LIKE_LR, val);
    }

    public Children notLike(R func, Object val) {
        return like(true, func, val);
    }

    public Children notLike(boolean condition, R function, Object val) {
        return where(condition, null, findFieldName(function), Operator.NOT_LIKE_LR, val);
    }

    @Override
    public Children where(boolean condition, String table, String column, Operator operator, Object val) {
        if (condition) {
            getQuerySet().where(table, column, operator, val);
        }
        return chain();
    }

    //TODO::-------------------- 字段  --------------------
    @SafeVarargs
    public final Children field(R... fields) {
        for (R field : fields) {
            getQuerySet().field(field.getName());
        }
        return chain();
    }

    // 关联表支持
    @Override
    @SafeVarargs
    public final <TM> Children field(Class<TM> tm, MFunction<TM, ?>... tfs) {
        String table = WrapperUtil.autoTable(null, this, tm);
        for (MFunction<TM, ?> tf : tfs) {
            getQuerySet().field(table, SqlUtil.toUnderlineCase(tf.getName()));
        }
        return chain();
    }

    //TODO::--------------- 特性 ---------------
    @SafeVarargs
    public final Children group(R... fields) {
        for (R field : fields) {
            getQuerySet().group(field.getName());
        }
        return chain();
    }

    // 关联表支持
    @Override
    @SafeVarargs
    public final <TM> Children group(Class<TM> tm, MFunction<TM, ?>... tfs) {
        String table = WrapperUtil.autoTable(null, this, tm);
        for (MFunction<TM, ?> tf : tfs) {
            getQuerySet().group(table, SqlUtil.toUnderlineCase(tf.getName()));
        }
        return chain();
    }

    @SafeVarargs
    public final Children having(R... fields) {
        for (R field : fields) {
            model().having(field.getName());
        }
        return chain();
    }

    // 关联表支持
    @Override
    @SafeVarargs
    public final <TM> Children having(Class<TM> tm, MFunction<TM, ?>... tfs) {
        String table = WrapperUtil.autoTable(null, this, tm);
        for (MFunction<TM, ?> tf : tfs) {
            getQuerySet().having(table, SqlUtil.toUnderlineCase(tf.getName()));
        }
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

    //TODO::--------------- 排序和数量 ---------------
    public Children order(R r, boolean desc) {
        getQuerySet().order(r.getName(), desc ? OrderByType.DESC : OrderByType.ASC);
        return chain();
    }

    public Children order(R r) {
        return order(r, false);
    }

    public Children orderByDesc(R r) {
        return order(r, true);
    }

    public Children orderByAsc(R r) {
        return order(r, false);
    }

    // 关联表支持
    public <TM> Children order(Class<TM> tm, MFunction<TM, ?> tf, OrderByType type) {
        String table = WrapperUtil.autoTable(null, this, tm);
        getQuerySet().order(table, SqlUtil.toUnderlineCase(tf.getName()), type);
        return chain();
    }

    public Children limit(long index, long count) {
        model().limit(index, count);
        return chain();
    }

    public Children limit(long count) {
        model().limit(count);
        return chain();
    }

    public Children page(long page) {
        model().page(page);
        return chain();
    }

    public Children page(long page, long pageSize) {
        model().page(page, pageSize);
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
        Map<String, Object> row = getQuerySet().find();
        return loadDataItem(row, getModelMeta(this.model.getClass()));
    }

    public List<M> select() {
        List<Map<String, Object>> rows = getQuerySet().select();
        // 封装数据
        List<M> models = queryAfter(rows, getModelMeta(this.model.getClass()));
        logger.debug("Count:" + models.size());
        return models;
    }

    private List<M> queryAfter(List<Map<String, Object>> rows, ModelMeta mainMeta) {
        List<M> models = new LinkedList<>();
        // 主表和一对一数据
        rows.forEach(data -> models.add(loadDataItem(data, mainMeta)));
        // 一对多
        return models;
    }

    private M loadDataItem(Map<String, Object> data, ModelMeta mainMeta) {
        // 主表
        M main = Model.newModel(mainMeta.getClazz());
        if (joinMap == null || joinMap.isEmpty()) {
            ModelHelper.resultTranshipmentWithOne(main, data, null);
            return main;
        }
        // 多表关联下加载
        ModelHelper.resultTranshipmentWith(main, data, mainMeta.table());
        // 一对一的数据加载
        joinMap.forEach((alias, join) -> {
            // 未加载数据 || 未设置加载对应属性名 || 在父实体不存在
            if (!join.isLoadLeftData() || ModelHelper.isEmpty(join.getLeftAlias()) || !ReflectUtil.hasField(mainMeta.getClazz(), join.getLeftAlias())) {
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
            ModelHelper.resultTranshipmentWith(left, data, alias);
            ReflectUtil.setFieldValue(main, join.getLeftAlias(), left);
        });
        return main;
    }

    public IPagination<M> pagination(long page, long pageSize) {
        IPagination<Map<String, Object>> pagination = getQuerySet().pagination(page, pageSize);
        // 装载数据
        List<M> models = queryAfter(pagination.getRows(), getModelMeta(this.model.getClass()));
        return new Pagination<>(pagination, models);
    }

    public IPagination<M> pagination() {
        return pagination(1L, 20L);
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
        QueryReservoir queryReservoir = QuerySetHelper.getQueryReservoir(querySet);
        // 主表字段
        if (ObjectUtil.isEmpty(queryReservoir.columnList)) {
            ModelMeta main = ModelHelper.getMeta(this.model.getClass());
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
        if (join.isLoadLeftData() && ModelHelper.isNotEmpty(leftAlias)) {
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
        List<JoinCondition> conditions = new LinkedList<>();
        join.onList.forEach(it -> {
            // 右条件是主表
            it.setRight(it.getRight() == null ? (Class<? extends Model>) mainMeta.getClazz() : it.getRight());
            // 模型元信息
            ModelMeta right = getModelMeta(it.getRight());
            String rightAlias = SqlUtil.isNotEmpty(it.getRightAlias()) ? it.getRightAlias() : right.table();
            // 一：方法引用
            JoinCondition joinCondition = new JoinCondition(
                    join.getLeftAlias(), it.getLeftFun().getName(),
                    1, it.getType(),
                    rightAlias, null
            );
            joinCondition.setRightColumn(it.getRightFun() == null ? null : it.getRightFun().getName());
            joinCondition.setRightValue(it.getRightValue());
            conditions.add(joinCondition);
        });
        querySet.join(leftMeta.tableAlias(leftAlias), conditions, type);

        return chain();
    }

    private static ModelMeta getModelMeta(Class<?> clazz) {
        return WrapperUtil.getModelMeta(clazz);
    }

    private void loadModelAccurateFields(QuerySet querySet, ModelMeta meta, String table) {
        ModelHelper.loadModelAccurateFields(querySet, meta, table);
    }

    private Map<Class<?>, ModelFieldMeta> getModelClazzFieldMap(ModelMeta meta) {
        return WrapperUtil.getModelClazzFieldMap(meta);
    }
}
