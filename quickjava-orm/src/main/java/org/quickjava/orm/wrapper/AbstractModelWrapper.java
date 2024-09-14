package org.quickjava.orm.wrapper;

import cn.hutool.core.util.ReflectUtil;
import org.quickjava.orm.contain.DataMap;
import org.quickjava.orm.contain.IPagination;
import org.quickjava.orm.contain.Pagination;
import org.quickjava.orm.enums.JoinType;
import org.quickjava.orm.model.Model;
import org.quickjava.orm.model.ModelHelper;
import org.quickjava.orm.model.ModelReservoir;
import org.quickjava.orm.model.contain.ModelFieldMeta;
import org.quickjava.orm.model.contain.ModelMeta;
import org.quickjava.orm.query.QueryReservoir;
import org.quickjava.orm.query.QuerySet;
import org.quickjava.orm.query.QuerySetHelper;
import org.quickjava.orm.query.build.Where;
import org.quickjava.orm.query.enums.OrderByType;
import org.quickjava.orm.utils.SqlUtil;
import org.quickjava.orm.wrapper.join.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.*;

public abstract class AbstractModelWrapper<Children extends AbstractModelWrapper<Children, M, R>, M extends Model, R extends MFunction<M, ?>>
        extends AbstractWhere<Children, M, R>
        implements ModelJoinWrapper<Children, M, R>, Serializable {

    protected static final Logger logger = LoggerFactory.getLogger(AbstractModelWrapper.class);

    protected M model;
    protected Class<M> modelClazz;
    protected Map<String, JoinOn<?, ?, ?>> joinOnMap;

    public AbstractModelWrapper() {
    }

    protected Model model() {
        return model;
    }

    @Override
    public Class<M> getModelClass() {
        return modelClazz;
    }

    //TODO::-------------------- 查询条件  --------------------
    @Override
    public Children where(Where where) {
        querySet().where(where);
        return chain();
    }

    //TODO::-------------------- 查询字段  --------------------

    /**
     * 限定返回数据列
     */
    @SafeVarargs
    public final Children field(R... fields) {
        for (R field : fields) {
            querySet().field(field.getName());
        }
        return chain();
    }

    // 关联表支持
    @Override
    @SafeVarargs
    public final <TM> Children field(Class<TM> tm, MFunction<TM, ?>... tfs) {
        String table = WrapperUtil.autoTable(null, this, tm);
        for (MFunction<TM, ?> tf : tfs) {
            querySet().field(table, SqlUtil.toUnderlineCase(tf.getName()));
        }
        return chain();
    }

    //TODO::--------------- 特性 ---------------
    @SafeVarargs
    public final Children group(R... fields) {
        for (R field : fields) {
            querySet().group(field.getName());
        }
        return chain();
    }

    // 关联表支持
    @Override
    @SafeVarargs
    public final <TM> Children group(Class<TM> tm, MFunction<TM, ?>... tfs) {
        String table = WrapperUtil.autoTable(null, this, tm);
        for (MFunction<TM, ?> tf : tfs) {
            querySet().group(table, SqlUtil.toUnderlineCase(tf.getName()));
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
            querySet().having(table, SqlUtil.toUnderlineCase(tf.getName()));
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
        querySet().order(r.getName(), desc ? OrderByType.DESC : OrderByType.ASC);
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
        querySet().order(table, SqlUtil.toUnderlineCase(tf.getName()), type);
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

    public Long delete() {
        return model().delete();
    }

    public M update() {
        return model().update();
    }

    public M update(DataMap data) {
        return model().update(data);
    }

    public M save() {
        return model().save();
    }

    public M save(DataMap data) {
        return model().save(data);
    }

    //TODO:---------- 查询执行方法 ----------
    public M find() {
        queryBefore();
        Map<String, Object> data = querySet().find();
        M row = loadDataItem(data, getModelMeta(modelClazz));
        return row;
    }

    public List<M> select() {
        queryBefore();
        List<Map<String, Object>> dataList = querySet().select();
        List<M> rows = queryAfter(dataList, getModelMeta(modelClazz));
        return rows;
    }

    private Children queryBefore() {
        // 主表字段
        ModelReservoir reservoir = ModelHelper.getModelReservoir(model);
        QueryReservoir queryReservoir = QuerySetHelper.getQueryReservoir(reservoir.querySet);
//        if (queryReservoir.columnList == null || queryReservoir.columnList.isEmpty()) {
//            model.field(model);
//        }
        return chain();
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
        if (joinOnMap == null || joinOnMap.isEmpty()) {
            ModelHelper.resultTranshipmentWithOne(main, data, null);
            return main;
        }
        // 多表关联下加载
        ModelHelper.resultTranshipmentWith(main, data, mainMeta.table());
        // 一对一的数据加载
        joinOnMap.forEach((alias, on) -> {
            if (on.getDataRightFields() != null && !on.getDataRightFields().isEmpty()) {
                return;
            }
            // 关联模型元数据
            ModelMeta leftMeta = getModelMeta(on.getLeftClass());
            // 是和主模型关联的条件
//            Where where = CollectionUtil.isEmpty(on.getWheres()) ? null : on.getWheres().get(0);
//            if (CollectionUtil.isNotEmpty(on.getWheres()) && !(mainMeta.getClazz().equals())) {
//                return;
//            }
            // 数据装载
            Model left = Model.newModel(leftMeta.getClazz(), null, main);
            ModelHelper.resultTranshipmentWith(left, data, alias);
            ReflectUtil.setFieldValue(main, on.getDataFieldName(), left);
        });
        return main;
    }

    public IPagination<M> pagination(long page, long pageSize) {
        IPagination<Map<String, Object>> pagination = querySet().pagination(page, pageSize);
        // 装载数据
        List<M> models = queryAfter(pagination.getRows(), getModelMeta(this.model.getClass()));
        return new Pagination<>(pagination, models);
    }

    public IPagination<M> pagination() {
        return pagination(1L, 20L);
    }

    public Children data(String name, Object val) {
        model().data(name, val);
        return chain();
    }

    @SafeVarargs
    public final Children with(R... fields) {
        for (R field : fields) {
            model().with(field.getName());
        }
        return chain();
    }

    public Children with(String... fields) {
        model().with(fields);
        return chain();
    }

    @Override
    public String toString() {
        return model().toString();
    }

    private String parseFieldName(MFunction<?, ?> function) {
        return function.getName();
    }

    private QuerySet querySet() {
        return WrapperUtil.getQuerySet(this.model());
    }

    /**
     * 关联查询实现
     */
//    @Override
//    public Children join(JoinType type, JoinSpecify<?, ?> join) {
//        // 为空过滤
//        if (join.getOnList().isEmpty()) {
//            return chain();
//        }
//        // 查询器
//        ModelMeta mainMeta = getModelMeta(this.model.getClass());
//        QuerySet querySet = querySet();
//        QueryReservoir queryReservoir = QuerySetHelper.getQueryReservoir(querySet);
//        // 主表字段
//        if (ObjectUtil.isEmpty(queryReservoir.columnList)) {
//            ModelMeta main = ModelHelper.getMeta(this.model.getClass());
//            loadModelAccurateFields(querySet, main, main.table());
//        }
//        // 默认为主表
//        if (join.getLeft() == null) {
//            join.setLeft(this.model.getClass());
//        }
//        if (join.getRight() == null) {
//            join.setRight(this.model.getClass());
//        }
//        // 关联表
//        String relName = join.getRightAlias();
//        Class<Model> relClazz = (Class<Model>) join.getRight();
//
//        ModelMeta relMeta = ModelHelper.getMeta(relClazz);
//        if (relName == null) {
//            relName = relMeta.table();
//        }
//        if (relName == null) {
//            relName = relClazz.getSimpleName();
//        }
//
//        // 声明左表数据字段
//        if (join.getLoadDataFieldName() != null && ModelHelper.isNotEmpty(relName)) {
//            loadModelAccurateFields(querySet, relMeta, relName);
//        }
//
//        // 缓存
//        if (joinOnMap == null) {
//            joinOnMap = new LinkedHashMap<>();
//        }
//        joinOnMap.put(relName, join);
//
//        // join条件（支持多个
////        List<JoinConditionAbs> conditions = new LinkedList<>();
////        join.getOnList().forEach(it -> {
////            // 为空取主表
////            it.setLeft(it.getLeft() == null ? mainMeta.getClazz() : it.getLeft());
////            ModelMeta onLeft = getModelMeta(it.getLeft());
////            String onLeftAlias = SqlUtil.isNotEmpty(it.getLeftAlias()) ? it.getLeftAlias() : onLeft.table();
////            String onLeftColumn = it.getLeftFun() == null ? null : it.getLeftFun().getName();
////            // 右表信息
////            it.setRight(it.getRight() == null ? mainMeta.getClazz() : it.getRight());
////            ModelMeta onRight = getModelMeta(it.getRight());
////            String onRightAlias = SqlUtil.isNotEmpty(it.getRightAlias()) ? it.getRightAlias() : onRight.table();
////            String onRightColumn = it.getRightFun() == null ? null : it.getRightFun().getName();
////            // 一：方法引用
////            JoinConditionAbs joinCondition = new JoinConditionAbs(it.getLogic(), it.getCompare(),
////                    onLeftAlias, onLeftColumn, onRightAlias, onRightColumn);
////            joinCondition.setRightValue(it.getRightValue());
////            conditions.add(joinCondition);
////        });
////        querySet.join(relMeta.tableAlias(relName), conditions, type);
//
//        return chain();
//    }
    @Override
    public Children join(JoinType type, JoinOn<?, ?, ?> joinOn) {
        joinOn = joinOn.base();
        QuerySet querySet = querySet();
        // join
        Class<?> leftClass = joinOn.getLeftClass() == null ? getModelClass() : joinOn.getLeftClass();
        ModelMeta leftMeta = ModelHelper.getMeta(leftClass);
        String leftName = joinOn.getRightAlias();
        querySet.join(leftMeta.tableAlias(leftName), joinOn.wheres, type);
        // 缓存
        if (joinOnMap == null) {
            joinOnMap = new LinkedHashMap<>();
        }
        joinOnMap.put(joinOn.getRightAlias(), joinOn);
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
