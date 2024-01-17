package org.quickjava.orm.wrapper;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import org.quickjava.orm.contain.DataMap;
import org.quickjava.orm.contain.Pagination;
import org.quickjava.orm.enums.JoinType;
import org.quickjava.orm.model.Model;
import org.quickjava.orm.model.ModelHelper;
import org.quickjava.orm.model.contain.ModelFieldMeta;
import org.quickjava.orm.model.contain.ModelMeta;
import org.quickjava.orm.query.QueryReservoir;
import org.quickjava.orm.query.QuerySet;
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

    @Override
    public Children where(boolean condition, String table, String column, Operator operator, Object val) {
        if (condition) {
            getQuerySet().where(table, column, operator, val);
        }
        return chain();
    }

    //TODO::-------------------- 字段  --------------------
    public Children field(R ... fields) {
        for (R field : fields) {
            getQuerySet().field(field.getName());
        }
        return chain();
    }

    @Override
    @SafeVarargs
    public final <TM> Children field(Class<TM> tm, MFunction<TM, ?>... tfs) {
        for (MFunction<TM, ?> lf : tfs) {
            String table = WrapperUtil.autoTable(null, this, tm);
            getQuerySet().field(table, lf.getName());
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

    @SafeVarargs
    public final Children having(R... fields) {
        for (R field : fields) {
            model().having(field.getName());
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

    //TODO::--------------- 排序和数量限制 ---------------
    public <TM> Children order(Class<TM> tm, MFunction<TM, ?> tf, OrderByType type) {
        String table = WrapperUtil.autoTable(null, this, tm);
        getQuerySet().order(table, tf.getName(), type);
        return chain();
    }

    public Children order(R r, boolean desc) {
        return order(this.modelClazz, r, desc ? OrderByType.DESC : OrderByType.ASC);
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

    public Pagination<M> pagination(long page, long pageSize) {
        Pagination<Map<String, Object>> pagination = getQuerySet().pagination(page, pageSize);
        // 装载数据
        List<M> models = queryAfter(pagination.rows, getModelMeta(this.model.getClass()));
        return new Pagination<>(pagination, models);
    }

    public Pagination<M> pagination() {
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
        QueryReservoir queryReservoir = (QueryReservoir) ReflectUtil.getFieldValue(querySet, "reservoir");
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
