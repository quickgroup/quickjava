package org.quickjava.orm.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.quickjava.common.utils.ComUtil;
import org.quickjava.common.utils.DatetimeUtil;
import org.quickjava.orm.ORMContext;
import org.quickjava.orm.query.QuerySetHelper;
import org.quickjava.orm.utils.ReflectUtil;
import org.quickjava.orm.enums.JoinType;
import org.quickjava.orm.model.annotation.ModelName;
import org.quickjava.orm.model.annotation.OneToMany;
import org.quickjava.orm.model.annotation.OneToOne;
import org.quickjava.orm.query.QueryReservoir;
import org.quickjava.orm.query.QuerySet;
import org.quickjava.orm.query.build.TableColumn;
import org.quickjava.orm.query.callback.OrderByOptCallback;
import org.quickjava.orm.model.callback.WhereClosure;
import org.quickjava.orm.query.callback.WhereOptCallback;
import org.quickjava.orm.contain.*;
import org.quickjava.orm.query.enums.Operator;
import org.quickjava.orm.query.enums.OrderByType;
import org.quickjava.orm.model.enums.RelationType;
import org.quickjava.orm.model.contain.ModelFieldMeta;
import org.quickjava.orm.model.contain.ModelMeta;
import org.quickjava.orm.model.contain.Relation;
import org.quickjava.orm.model.out.ModelListSql;
import org.quickjava.orm.model.out.ModelSql;
import org.quickjava.orm.utils.*;
import org.quickjava.orm.enums.CompareType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.lang.reflect.Field;
import java.util.*;

/*
 * Copyright (c) 2020~2023 http://www.quickjava.org All rights reserved.
 * +-------------------------------------------------------------------
 * Organization: QuickJava
 * +-------------------------------------------------------------------
 * Author: Qlo1062
 * +-------------------------------------------------------------------
 * File: Model
 * +-------------------------------------------------------------------
 * Date: 2023-2-20 16:39
 * +-------------------------------------------------------------------
 * License: Apache Licence 2.0
 * +-------------------------------------------------------------------
 */

/**
 * 模型
 */
@JsonIgnoreProperties(value = {"reservoir"}, ignoreUnknown = true)
public class Model implements IModel {

    @JsonIgnore
    private static final Logger logger = LoggerFactory.getLogger(Model.class);

    /**
     * 模型对象数据储存器
     */
    @JsonIgnore
    private final ModelReservoir reservoir = new ModelReservoir(this);

    private QuerySet query() {
        synchronized (reservoir) {
            if (reservoir.querySet == null) {
                if (reservoir.meta == null) {
                    throw new RuntimeException("元信息丢失：" + this.getClass());
                }
                reservoir.querySet = QuerySet.table(reservoir.meta.table());
                QueryReservoir queryReservoir = QuerySetHelper.getQueryReservoir(reservoir.querySet);
                queryReservoir.setCallback(WhereOptCallback.class, ModelHelper.whereOptCallback, this);
                queryReservoir.setCallback(OrderByOptCallback.class, ModelHelper.orderByOptCallback, this);
            }
        }
        return reservoir.querySet;
    }

    private QueryReservoir querySetReservoir() {
        return QuerySetHelper.getQueryReservoir(query());
    }

    //NOTE:---------- 查询方法 ----------
    public Model where(String field, Operator opr, Object val) {
        query().where(field, opr, val);
        return this;
    }

    public Model where(String field, String opr, Object val) {
        query().where(field, Operator.valueOf(opr), val);
        return this;
    }

    public Model where(String field, Object val) {
        where(field, Operator.EQ, val);
        return this;
    }

    public Model where(String field, Operator operator) {
        where(field, operator, null);
        return this;
    }

    public Model where(Map<String, Object> query) {
        // 处理字段名：标准字段名你：驼峰转下划线组成
        Map<String, Object> queryRet = new LinkedHashMap<>();
        query.forEach((name, val) -> queryRet.put(ModelHelper.fieldToUnderlineCase(name), val));
        // 调用querySet加载条件
        QuerySetHelper.loadQuery(query(), queryRet);
        return this;
    }

    public Model where(String sql) {
        query().where(sql, Operator.RAW, null);
        return this;
    }

    public Model where(WhereClosure callback) {
        query().where(callback);
        return this;
    }

    public Model whereOr(WhereClosure callback) {
        query().whereOr(callback);
        return this;
    }

    public Model eq(String field, Object val) {
        return where(field, val);
    }

    public Model neq(String field, Object val) {
        return where(field, Operator.NEQ, val);
    }

    public Model gt(String field, Object val) {
        return where(field, Operator.GT, val);
    }

    public Model gte(String field, Object val) {
        return where(field, Operator.GTE, val);
    }

    public Model lt(String field, Object val) {
        return where(field, Operator.LT, val);
    }

    public Model lte(String field, Object val) {
        return where(field, Operator.LTE, val);
    }

    public Model in(String field, Object... args) {
        return where(field, Operator.IN, args);
    }

    public Model notIn(String field, Object... args) {
        return where(field, Operator.IN, args);
    }

    public Model isNull(String field) {
        return where(field, Operator.IS_NULL, null);
    }

    public Model isNotNull(String field) {
        return where(field, Operator.IS_NOT_NULL, null);
    }

    public Model between(String field, Object val1, Object val2) {
        return where(field, Operator.BETWEEN, new Object[]{val1, val2});
    }

    //NOTE::---------- 查询方法二 ----------
    public Model field(String ... fields) {
        query().field(fields);
        return this;
    }

    //NOTE::---------- 操作方法-增删改查 ----------

    /**
     * 新增
     */
    public int insert()
    {
        insertBefore();
        // 执行
        Long pkVal = query().insertGetId(this.sqlData());
        // 编译sql
        if (querySetReservoir().isFetchSql())
            return toD(new ModelSql(querySetReservoir().getSql()));
        // 回填主键
        if (pkVal != null) {
            setData(pk(), pkVal);
        }
        return 1;
    }

    public int insert(DataMap data) {
        return insert((Map<String, Object>) data);
    }

    public int insert(Map<String, Object> data) {
        data(data);
        return insert();
    }

    public Long insertGetId() {
        return query().insertGetId(this.data());
    }

    public Long insertGetId(Map<String, Object> data) {
        data(data);
        return insertGetId();
    }

    public int insertAll(List<Map<String, Object>> dataList) {
        List<Map<String, Object>> ret = new LinkedList<>();
        for (Map<String, Object> data : dataList) {
            data(data);
            ret.add(new LinkedHashMap<>(this.sqlData()));
        }
        return query().insertAll(ret);
    }

    protected void insertBefore() {
        // 默认填充数据
        reservoir.meta.fieldMap().forEach((name, field) -> {
            if (!reservoir.data.containsKey(name)) {
                if (field.hasInsertFill()) {
                    setData(name, field.insertFill());     // 新增时填充
                }
            }
        });
        // 雪花id
    }

    /**
     * 删除
     *
     * @return 1
     */
    public int delete() {
        // 软删除字段
        for (ModelFieldMeta field : reservoir.meta.fieldMap().values()) {
            if (field.isSoftDelete()) {
                if (Date.class.isAssignableFrom(field.getField().getType())) {
                    setData(field.getName(), DatetimeUtil.now());      // 字符串去填充的数据
                    save();
                } else {
                    throw new QuickORMException("不支持的软删除字段");
                }
                return 1;
            }
        }
        // 真实删除默认条件
        if (ModelHelper.isEmpty(QuerySetHelper.getQueryReservoir(query()).whereList) && pk() != null) {
            where(pk(), pkVal());
        }
        // 真实删除
        return query().delete();
    }

    /**
     * 更新
     */
    public int update() {
        // 默认填充数据
        reservoir.meta.fieldMap().forEach((name, field) -> {
            if (!reservoir.data.containsKey(name)) {
                if (field.hasUpdateFill()) {
                    setData(name, field.updateFill());
                }
            }
        });
        // 编译sql
        if (querySetReservoir().isFetchSql())
            return 0;
        // 执行
        return query().update(this.sqlData());
    }

    public int update(DataMap data) {
        return update((Map<String, Object>) data);
    }

    public int update(Map<String, Object> data) {
        data(data);
        return update();
    }

    public int updateById() {
        String pk = pk();
        where(pk, data(pk));
        reservoir.data.remove(pk);  // 不去更新主键
        return update();
    }

    /**
     * 保存数据
     * - 自动判断主键是否为null，为null执行新增，否则进行更新
     */
    public int save() {
        String pk = pk();
        Object pkVal = data(pk);
        return pkVal == null ? insert() : where(pk, pkVal).update();
    }

    public int save(DataMap data) {
        return save((Map<String, Object>) data);
    }

    public int save(Map<String, Object> data) {
        data(data);
        return save();
    }

    /**
     * 查询一条数据
     *
     * @param <D> 模型类
     * @return 模型对象
     */
    public <D extends IModel> D find() {
        // 查询前：预载入字段准备
        queryBefore();
        // 执行查询
        List<Map<String, Object>> dataList = query().limit(0L, 1L).select();
        // 编译sql
        if (querySetReservoir().fetchSql) {
            return toD(new ModelSql(query().buildSql()));
        }
        // 结果为空
        if (ModelHelper.isEmpty(dataList)) {
            return null;
        }
        // 装载数据
        List<IModel> models = ModelHelper.resultTranshipment(this, getClass(), dataList);
        return toD(models.get(0));
    }

    /**
     * 查询一条数据
     */
    public <D extends IModel> D find(Serializable id) {
        query().where(query().pk(), id);
        return find();
    }

    /**
     * 查询多条数据
     */
    public <D extends IModel> List<D> select() {
        // 查询前处理：预载入
        queryBefore();
        // 执行查询
        List<Map<String, Object>> dataList = query().select();
        // 编译sql
        if (querySetReservoir().fetchSql) {
            return toD(new ModelListSql(querySetReservoir().sql));
        }
        // 装载
        List<IModel> models = ModelHelper.resultTranshipment(this, getClass(), dataList);
        return toD(models);
    }

    /**
     * 获取指定字段的数据
     *
     * @param field 字段名
     * @param <D>   数据代理类
     * @return 字段数据
     */
    public <D> D value(String field) {
        // 指定字段
        query().field(field);
        // 查询
        return toD(find());
    }

    //NOTE::---------- 分页方法 ----------
    public <D> IPagination<D> pagination(Long page, Long pageSize) {
        // 查询前处理：预载入
        queryBefore();
        // 执行查询
        IPagination<Map<String, Object>> pagination = query().pagination(page, pageSize);
        // 数据组装
        IPagination<IModel> pagination1 = new Pagination<>(pagination, new LinkedList<>());
        pagination1.setRows(ModelHelper.resultTranshipment(this, getMClass(), pagination.getRows()));
        return toD(pagination1);
    }

    public <D> IPagination<D> pagination(Integer page, Integer pageSize) {
        return pagination(page.longValue(), pageSize.longValue());
    }

    public <D> IPagination<D> pagination() {
        return pagination(1L, 20L);
    }

    //NOTE::---------- 操作方法：排序、聚合等 START ----------

    /**
     * 排序
     *
     * @param field 字段
     * @param type  排序方式
     * @return 模型对象
     */
    public Model order(String field, OrderByType type) {
        query().order(ModelHelper.fieldToUnderlineCase(field), type);
        return this;
    }

    public Model order(String field, boolean desc) {
        query().order(field, desc);
        return this;
    }

    public Model order(String fields) {
        query().order(fields);
        return this;
    }

    public Model order(List<String> fields) {
        fields.forEach(this::order);
        return this;
    }

    public Model order(String[] fields) {
        for (String field : fields) {
            order(field);
        }
        return this;
    }

    public Model limit(Long index, Long count) {
        query().limit(index, count);
        return this;
    }

    public Model limit(Long count) {
        return limit(0L, count);
    }

    /**
     * 分页
     *
     * @param page 页数
     * @return 模型对象
     */
    public Model page(Long page) {
        query().page(page);
        return this;
    }

    /**
     * 分页
     *
     * @param page 页数
     * @param size 页大小
     * @return 模型对象
     */
    public Model page(Long page, Long size) {
        query().page(page, size);
        return this;
    }

    public Model group(String fields) {
        query().group(fields);
        return this;
    }

    public Model having(String fields) {
        query().having(fields);
        return this;
    }

    public Model union(String sql) {
        query().union(sql);
        return this;
    }

    public Model union(String[] sqlArr) {
        query().union(sqlArr);
        return this;
    }

    public Model distinct(boolean distinct) {
        query().distinct(distinct);
        return this;
    }

    public Model lock(boolean lock) {
        query().lock(lock);
        return this;
    }

    // 编译sql，当前只支持查询
    public Model fetchSql(boolean fetch) {
        query().fetchSql(fetch);
        return this;
    }
    //NOTE::---------- 操作方法：排序、聚合等 END ----------

    //NOTE::---------- 数据方法 START ----------

    /**
     * 实体通过map装载数据
     *
     * @param data 数据集
     * @return 模型对象
     */
    public Model data(Map<String, Object> data) {
        data.forEach(this::data);
        return this;
    }

    /**
     * 获取data中的数据
     *
     * @param field 属性名
     * @return 属性值
     */
    public Object data(String field) {
        return data().get(field);
    }

    /**
     * 装载数据
     *
     * @param name 属性名
     * @param val  属性值
     * @return 模型对象
     */
    public Model data(String name, Object val) {
        // 设置值
        ModelFieldMeta field = setData(name, val);
        if (field == null || field.getRelationWay() != null) {
            return this;
        }
        // 被修改的字段
        reservoir.getModified().add(field);
        return this;
    }

    // 设置值（内部方法）
    protected ModelFieldMeta setData(String name, Object val) {
        // 数据保存
        name = ModelHelper.toCamelCase(name);
        ModelFieldMeta field = reservoir.meta.fieldMap().get(name);
        // 非本表属性或关联属性不设置
        if (field == null || field.getRelationWay() != null) {
            return null;
        }
        // 缓存值
        reservoir.data.put(name, val);
        // 对象属性值
        ModelHelper.setFieldValue(this, name, val, field);
        return field;
    }

    /**
     * 获取data全部数据
     *
     * @return 模型数据
     */
    public DataMap data() {
        this.loadingVegetarianModel();
        return reservoir.data;
    }

    public String pk() {
        return reservoir.meta.getPkName();
//        return ModelUtil.toCamelCase(query().pk());
    }

    public Object pkVal() {
        return data(pk());
    }

    /**
     * 数据转sql格式：驼峰转下划线等
     * - insert、update的数据
     *
     * @return 数据集
     */
    private DataMap sqlData() {
        DataMap data = data();
        DataMap ret = DataMap.one();
        if (reservoir.modified != null) {
            reservoir.modified.forEach(field -> {
                if (field.getRelationWay() != null) {
                    return;
                }
                Object v = data.get(field.getName());
                ret.put(ModelHelper.fieldToUnderlineCase(field.getName()), ModelHelper.valueToSqlValue(v));
            });
        }
        return ret;
    }
    //NOTE::---------- 数据方法 END ----------

    //NOTE::---------- 静态方法、批量方法 START ----------

    /**
     * 通过 Map 创建对象
     *
     * @param data 数据集
     * @return 模型对象
     */
    public Model create(Map<String, Object> data) {
        return create(getMClass(), data);
    }

    /**
     * 通过 DataMap 创建对象
     *
     * @param data 数据集
     * @return 模型对象
     */
    public Model create(DataMap data) {
        return create((Map<String, Object>) data);
    }

    public static<T extends Model> T create(Class<? extends Model> clazz, Map<String, Object> data) {
        T model = newModel(clazz, data);
        model.insert();
        return model;
    }

    public static<MC extends Model> List<MC> batchCreateWithMap(Class<MC> clazz, List<Map<String, Object>> dataList) {
        if (dataList == null || dataList.isEmpty()) {
            return new LinkedList<>();
        }
        List<MC> models = new LinkedList<>();
        List<DataMap> modelDataList = new LinkedList<>();
        dataList.forEach(data -> {
            MC model = newModel(clazz);
            model.data(data);
            model.insertBefore();
            modelDataList.add(model.data());
            models.add(model);
        });
        QuerySet querySet = ModelHelper.getModelQuery(models.get(0));
        Integer insertId = querySet.insertAll(new LinkedList<>(modelDataList));
        // 回写自增id
        if (insertId != null) {
            String pkName = models.get(0).pk();
            for (MC model : models) {
                model.setData(pkName, insertId++);
            }
        }
        return models;
    }

    public static<MC extends Model> int batchCreate(List<MC> models) {
        if (models == null || models.isEmpty()) {
            return 0;
        }
        List<DataMap> modelDataList = new LinkedList<>();
        for (Model model : models) {
            model.insertBefore();
            modelDataList.add(model.data());
        }
        QuerySet querySet = ModelHelper.getModelQuery(models.get(0));
        return querySet.insertAll(new LinkedList<>(modelDataList));
    }

    /**
     * 批量创建
     *
     * @param dataList 数据列表
     * @return 对象数量
     */
    public int bulkCreate(List<DataMap> dataList) {
        dataList.forEach(data -> {
            Model model = newModel(this.getMClass());
            model.save(data);
        });
        return dataList.size();
    }
    //NOTE::---------- 静态操作方法 END ----------

    //NOTE::---------- 模型控制方法 ----------

    /**
     * 查询前处理预载入
     * T1：一对一的字段声明
     */
    private void queryBefore() {
        // 软删除字段
        for (ModelFieldMeta field : reservoir.meta.fieldMap().values()) {
            if (field.isSoftDelete()) {
                if (Date.class.isAssignableFrom(field.getField().getType())) {
                    where(field.getName(), Operator.IS_NULL);
                } else {
                    throw new QuickORMException("不支持的软删除字段");
                }
            }
        }

        // NOTE::一对一关联查询
        Map<String, Relation> relationMap = getWithRelation(new RelationType[]{RelationType.OneToOne});
        logger.debug("relationMap=" + relationMap);
        if (!relationMap.isEmpty()) {
            // T1::字段声明
            List<String> fields = new LinkedList<>();
            // 本表字段声明
            ModelHelper.loadModelAccurateFields(query(), reservoir.meta, reservoir.meta.table());
            // 关联表
            relationMap.forEach((relationName, relation) -> {
                ModelMeta meta = ModelHelper.getMeta(relation.getClazz());
                // 关联表字段
                ModelHelper.loadModelAccurateFields(query(), meta, relationName);
                // 关联方式声明
                String conditionSql = ModelHelper.joinConditionSql(
                        relationName, relation.foreignKey(),
                        CompareType.EQ,
                        reservoir.meta.table(), relation.localKey()
                );
                query().join(meta.table() + " " + relationName, conditionSql, JoinType.LEFT);
            });
            // 装填字段
            query().field(fields);
        }
    }

    /**
     * 获取预载入的属性名对应模型类
     *
     * @param types 关联类型
     * @return 关联属性集合
     */
    private Map<String, Relation> getWithRelation(RelationType[] types) {
        Map<String, Relation> relationMap = new LinkedHashMap<>();
        if (reservoir.withs != null) {
            reservoir.withs.forEach(name -> {
                Relation relation = reservoir.meta.relationMap().get(name);
                if (relation != null && SqlUtil.inArray(types, relation.getType())) {
                    relationMap.put(name, reservoir.meta.relationMap().get(name));
                }
            });
        }
        return relationMap;
    }

    private static <D> D toD(Object model) {
        return (D) model;
    }

    //NOTE::---------- 模型实例化----------
    public static <D extends IModel> D newModel(Class<?> clazz) {
        return newModel(clazz, null, null);
    }

    public static <D extends IModel> D newModel(Object entity) {
        return newModel(entity.getClass(), null, null);
    }

    private static <D extends IModel> D newModel(Class<?> clazz, Map<String, Object> data) {
        return newModel(clazz, data, null);
    }

    private static <D extends IModel> D newModel(Class<?> clazz, Model parent) {
        return newModel(clazz, null, parent);
    }

    public static <D extends IModel> D newModel(Class<?> clazz, Map<String, Object> data, IModel parent) {
        // 从当前环境重新获取clazz
        clazz = ORMContext.loadClass(clazz);
        // 创建代理类信息
        Enhancer enhancer = new Enhancer();
//        enhancer.setClassLoader(Model.class.getClassLoader());
//        enhancer.setClassLoader(Thread.currentThread().getContextClassLoader());
        enhancer.setClassLoader(ORMContext.getClassLoader());
        enhancer.setSuperclass(getModelClass(clazz));
        enhancer.setCallback(modelProxyMethodInterceptor);
        D model = toD(enhancer.create());
        ModelReservoir reservoir = ModelHelper.getModelReservoir(model);
        // 元信息
        if (reservoir.meta == null) {
            reservoir.meta = ModelHelper.getMeta(clazz);
        }
        if (reservoir.meta == null) {
            throw new RuntimeException("元信息丢失：" + model.getClass());
        }
        // 加载数据
        if (!ModelHelper.isEmpty(data)) {
            ((Model) model).data((DataMap) data);
        }
        // 设置父类
        if (!ModelHelper.isEmpty(parent)) {
            reservoir.parent = parent;
        }
//        System.out.println("clazz getClassLoader=" + clazz.getClassLoader());
//        System.out.println("model getClassLoader=" + model.getClass().getClassLoader());
        return model;
    }

    @JsonIgnore
    private static final MethodInterceptor modelProxyMethodInterceptor = new MethodInterceptor() {
        @Override
        public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
            /*
             * 1. 缓存全部需要代理的关联属性
             * 2. 判断当前方法是否是关联属性的查询方法、获取方法
             * 3. 如果是就对返回数据处理
             * */
            Class<?> clazz = getModelClass(o.getClass());
            String methodName = method.getName();
            ModelMeta meta = ModelHelper.getMeta(clazz);
            // getter方法和关联方法
            if (meta.relationMap().containsKey(methodName)) {
                return LoadingRelationGetter(clazz, o, method, objects);
            }
            // setter方法
            if (methodName.startsWith("set")) {
                String fieldName = ModelHelper.toCamelCase(methodName.substring(3));
                if (meta.fieldMap().containsKey(fieldName) && objects.length == 1 && method.getReturnType().equals(Void.TYPE)) {
                    Setter(clazz, o, method, objects[0], fieldName);
                }
            }
            return methodProxy.invokeSuper(o, objects); // 执行方法后返回数据
        }
    };

    /**
     * 如果当前对象素模型（不是代理模型）
     * - 素模型：在 insert、update 时收集数据，包含null（因为java无法表明该属性是否被修改）
     */
    private void loadingVegetarianModel() {
        if (ModelHelper.isVegetarianModel(this) && reservoir.vegetarian) {
            // 收集字段数据
            reservoir.meta.fieldMap().forEach((name, field) -> {
                Object val = ReflectUtil.getFieldValue(this, field.getField());
                data(field.getName(), val);
            });
        }
    }

    //---------- NOTE::关联方法 ----------//
    /*
     * 关联方法
     * */
    protected <D extends IModel> D relation(String fieldName, Class<?> clazz, RelationType type, String localKey, String foreignKey) {
        if (fieldName == null || fieldName.contains("CGLIB$")) {
            throw new RuntimeException("关联属性名称错误：" + fieldName);
        }
        // 关联模型进行条件存储
        Model queryModel = newModel(clazz, this);
        queryModel.where(foreignKey, Operator.EQ, fieldAlias(this.pk(), localKey));
        // 缓存关联关系
        if (!reservoir.meta.relationMap().containsKey(fieldName)) {
            reservoir.meta.relationMap().put(fieldName, new Relation(queryModel, clazz, type, localKey, foreignKey));
        }
        // 返回被关联的模型对象，实现嵌套查询条件
        return toD(queryModel);
    }

    protected <D extends Model> D relation(Class<?> clazz, RelationType type, String localKey, String foreignKey) {
        String fieldName = getRelationMethodName(1);
        return relation(fieldName, clazz, type, localKey, foreignKey);
    }

    protected <D extends Model> D relation(String clazzName, RelationType type, String localKey, String foreignKey) {
        try {
            Class<?> clazz = getClass().getClassLoader().loadClass(clazzName);
            String fieldName = getRelationMethodName(1);
            return relation(fieldName, clazz, type, localKey, foreignKey);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected <D extends Model> D hasOne(Class<?> clazz, String localKey, String foreignKey) {
        String fieldName = getRelationMethodName(1);
        return relation(fieldName, clazz, RelationType.OneToOne, localKey, foreignKey);
    }

    protected <D extends Model> D hasMany(Class<?> clazz, String localKey, String foreignKey) {
        String fieldName = getRelationMethodName(1);
        return relation(fieldName, clazz, RelationType.OneToMany, localKey, foreignKey);
    }

    /**
     * 向上获取调用关联方法的调用方法名称（关联方法名称）
     * 从3开始
     */
    private String getRelationMethodName(int upperPos) {
        try {
            Class<?> modelClass = Model.class;
            // +2避开本方法和getStackTrace
            upperPos += 2;
            // getStackTrace,getRelationMethodName,本model调用方法, 调用者方法
            StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
            for (int i = upperPos; i < stackTrace.length; i++) {
                StackTraceElement element = stackTrace[i];
                String className = element.getClassName();
                Class<?> clazz = Class.forName(className);
                if (modelClass.getName().equals(className)) {     // 避开model方法
                } else if (element.getMethodName().startsWith("CGLIB$")) {      // 避开代理类初始化调用
                } else if (Enhancer.isEnhanced(clazz)) {      // 避开代理类初始化调用
                } else if (modelClass.isAssignableFrom(Class.forName(className))) {   // 必须是子类内部方法调用
                    return element.getMethodName();
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    /**
     * 预载入方法
     *
     * @param fields 需要预载入的属性名称
     * @return 模型对象
     */
    public Model with(String... fields) {
        if (ModelHelper.isEmpty(fields)) {
            return this;
        }
        if (fields.length == 1) {
            return this.with(fields[0].split(","));
        }
        List<String> withs = Arrays.asList(fields);
        ComUtil.trimList(withs);
        withs.forEach(name -> {
            if (!reservoir.meta.relationMap().containsKey(name)) {
                throw new RuntimeException("不存在关联注入：" + name);
            }
        });
        reservoir.getWiths().addAll(withs);
        return this;
    }

    private static boolean isModel(Class<?> clazz) {
        return Model.class.isAssignableFrom(clazz);
    }

    private static boolean isCollection(Class<?> clazz) {
        return Collection.class.isAssignableFrom(clazz);
    }

    // 缓存字段和方法
    protected static void initModel(Model model, Class<?> clazz) {
        if (!ModelHelper.metaExist(clazz)) {
            synchronized (Model.class) {
                model.reservoir.meta = initModel(clazz);
            }
        }
    }

    // 缓存字段和方法
    protected static ModelMeta initModel(Class<?> clazz) {
        if (!Model.class.isAssignableFrom(clazz)) {
            return null;
        }
        if (Enhancer.isEnhanced(clazz)) {
            clazz = clazz.getSuperclass();
        }
        // 初始化模型信息
        ModelMeta meta = new ModelMeta();
        meta.setClazz((Class<Model>) clazz);
//        meta.setTable(clazz.getSimpleName());   // 设置表名
        meta.setTable(meta.table());   // 设置表名
        meta.setFieldMap(new LinkedHashMap<>());
        ModelHelper.setMeta(clazz, meta);
        logger.debug("cache model meta. clazz={}", clazz);

        // 全部方法、属性
        Class<?> getClazz = clazz;
        List<Field> fieldList = new ArrayList<>();
        while (getClazz != null) {
            // 属性
            fieldList.addAll(new ArrayList<>(Arrays.asList(getClazz.getDeclaredFields())));
            // 父类
            getClazz = getClazz.getSuperclass();
        }
        // 加载字段
        for (Field field : fieldList) {
            // 排除静态字段
            if (Modifier.isStatic(field.getModifiers())) {
                continue;
            }
            ModelFieldMeta fieldMeta = new ModelFieldMeta(field);
            // 隐藏字段
            if (!fieldMeta.isExist()) {
                continue;
            }
            // 没有getter和setter方法
            if (!ReflectUtil.hasGetter(clazz, field.getName()) || !ReflectUtil.hasSetter(clazz, field.getName())) {
                continue;
            }
            meta.fieldMap().put(field.getName(), fieldMeta);
        }

        return meta;
    }

    /**
     * 初始化关联关系
     */
    private static void initRelation(IModel model)
    {
        ModelMeta meta = ModelHelper.getModelReservoir(model).meta;

        // 全部方法、属性
        Class<?> getClazz = model.getClass();
        List<Field> fieldList = new LinkedList<>();
        Map<String, Method> methodMap = new LinkedHashMap<>();
        while (getClazz != null) {
            // 属性
            fieldList.addAll(new ArrayList<>(Arrays.asList(getClazz.getDeclaredFields())));
            // 方法
            for (Method method : getClazz.getDeclaredMethods()) {
                if (method.getDeclaringClass().equals(Model.class)) {
                    continue;
                }
                if (!methodMap.containsKey(method.getName())) { // 子类覆写的方法不允许重复添加
                    methodMap.put(method.getName(), method);
                }
            }
            // 父类
            getClazz = getClazz.getSuperclass();
        }
        for (Field field : fieldList) {
            // 排除静态字段
            if (Modifier.isStatic(field.getModifiers())) {
                continue;
            }
            ModelFieldMeta fieldMeta = new ModelFieldMeta(field);
            boolean isModelChild = Model.class.isAssignableFrom(fieldMeta.getClazz());
            boolean isModelChildList = List.class.isAssignableFrom(fieldMeta.getClazz());
            if (isModelChildList) {
                try {
                    ParameterizedType listType = (ParameterizedType) field.getGenericType();
                    Class<?> listElementType = (Class<?>) listType.getActualTypeArguments()[0];
                    isModelChildList = Model.class.isAssignableFrom(listElementType);
                } catch (Exception e) {
                    isModelChildList = false;
                }
            }
            // 可能的关联属性
            if (isModelChild || isModelChildList) {
                // 属性上关联注解
                fieldMeta.setRelationWay(findRelationAno(field));
                // 有关联方法
                Method method = methodMap.get(fieldMeta.getName());
                if (method != null && Model.class.isAssignableFrom(method.getReturnType())) {
                    try {
//                        System.out.println("getReturnType=" + method.getReturnType().getClassLoader());
//                        System.out.println("Model=" + new Model().getClass().getClassLoader());
                        method.setAccessible(true);
                        method.invoke(model);
                        fieldMeta.setRelationWay(meta.relationMap().get(fieldMeta.getName()));
                    } catch (InvocationTargetException e) {
                        logger.error("关联方法调用失败：{}", e.getTargetException().getMessage(), e.getTargetException());
                    } catch (IllegalAccessException e) {
                        logger.error("关联方法调用失败：{}", e.getMessage(), e);
                    }
                }
            }
        }
    }

    private static Object findRelationAno(Field field) {
        if (field.getDeclaredAnnotation(OneToOne.class) != null) {
            return field.getDeclaredAnnotation(OneToOne.class);
        } else if (field.getDeclaredAnnotation(OneToMany.class) != null) {
            return field.getDeclaredAnnotation(OneToMany.class);
        }
        return null;
    }

    /**
     * 模型setter数据处理
     * - 保存修改的数据
     */
    private static void Setter(Class<?> clazz, Object o, Method method, Object arg, String fieldName) {
        try {
            Model model = (Model) o;
            model.data(fieldName, arg);
        } catch (Exception e) {
            logger.error("Setter error", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * getter懒加载数据
     */
    private static Object LoadingRelationGetter(Class<?> clazz, Object o, Method method, Object... objects) {
        try {
            // 加载
            ModelFieldMeta modelField = ModelHelper.getMeta(clazz).fieldMap().get(method.getName());
            Field field = modelField.getField();
            if (Model.class.isAssignableFrom(o.getClass())) {
                Model curr = (Model) o;
                String fieldName = field.getName();
                Class<?> fieldType = field.getType();
                // 存在数据直接返回
                if (curr.data().containsKey(fieldName)) {
                    return curr.data().get(fieldName);
                }

                // 存在递归和toString调用
                StackTraceElement[] stackTraceArr = Thread.currentThread().getStackTrace();
                for (StackTraceElement ele : stackTraceArr) {
                    if ("toString".equals(ele.getMethodName())) {
                        return curr.data().get(fieldName);
                    }
//                    else if (ele.getClassName().startsWith("org.springframework.http.converter.AbstractGenericHttpMessageConverter")) {
//                        return curr.data().get(fieldName);
//                    }
                }

                // 懒加载二
                if (modelField.getRelationWay() != null && Relation.class.isAssignableFrom(modelField.getRelationWay().getClass())) {
                    Relation relation = (Relation) modelField.getRelationWay();
                    if (relation.getType() == RelationType.OneToOne) {
                        Model relationModel = newModel(fieldType, curr);
                        // 设置全部关联属性为空，避免多重查询
                        Object localValue = ReflectUtil.getFieldValue(curr.getMClass(), curr, relation.localKey());
                        Object fieldValue = relationModel.where(relation.foreignKey(), localValue).find();  // 查询结果
                        curr.setData(fieldName, fieldValue);
                        return fieldValue;
                    } else if (relation.getType() == RelationType.OneToMany) {
                        if (field.getGenericType() instanceof ParameterizedType) {
                            ParameterizedType pt = (ParameterizedType) field.getGenericType();
                            Class<?> genericClazz = (Class<?>) pt.getActualTypeArguments()[0];
                            Model relationModel = newModel(genericClazz, curr);
                            Object localValue = ReflectUtil.getFieldValue(curr.getMClass(), curr, relation.localKey());
                            Object fieldValue = relationModel.where(relation.foreignKey(), localValue).select();  // 查询结果
                            curr.setData(fieldName, fieldValue);
                            return fieldValue;
                        }
                    }
                }
                // 懒加载一
                else if (isModel(fieldType) && OneToOne.class.isAssignableFrom(modelField.getRelationWay().getClass())) {
                    OneToOne one = (OneToOne) modelField.getRelationWay();
                    Model relationModel = newModel(fieldType, curr);
                    // 设置全部关联属性为空，避免多重查询
                    Object localValue = ReflectUtil.getFieldValue(curr.getMClass(), curr, one.localKey());
                    Object fieldValue = relationModel.where(one.foreignKey(), localValue).find();  // 查询结果
                    curr.setData(fieldName, fieldValue);
                    return fieldValue;
                } else if (isCollection(fieldType) && OneToMany.class.isAssignableFrom(modelField.getRelationWay().getClass())) {
                    OneToMany many = (OneToMany) modelField.getRelationWay();
                    if (field.getGenericType() instanceof ParameterizedType) {
                        ParameterizedType pt = (ParameterizedType) field.getGenericType();
                        Class<?> genericClazz = (Class<?>) pt.getActualTypeArguments()[0];
                        Model relationModel = newModel(genericClazz, curr);
                        Object localValue = ReflectUtil.getFieldValue(curr.getMClass(), curr, many.localKey());
                        Object fieldValue = relationModel.where(many.foreignKey(), localValue).select();  // 查询结果
                        curr.setData(fieldName, fieldValue);   // 保存查询到的数据
                        return fieldValue;
                    }
                }
            }
            return null;

        } catch (Exception e) {
            logger.error("getter数据查询异常：{}", e.getMessage(), e);
            throw new RuntimeException("getter数据查询异常：" + e);
        }
    }

    private Class<? extends Model> getMClass() {
        return ModelHelper.getModelClass(getClass());
    }

    private static Class<? extends Model> getModelClass(Class<?> clazz) {
        return ModelHelper.getModelClass(clazz);
    }

    private static TableColumn fieldAlias(String table, String field) {
        return ModelHelper.fieldAlias(table, field);
    }

    private static String toUnderlineCase(String name) {
        return ModelHelper.toUnderlineCase(name);
    }

    @Override
    public String toString() {
        try {
            Map<String, Object> dataMap = new LinkedHashMap<>(data());
            reservoir.meta.relationMap().forEach((name, relation) -> dataMap.put(name, ReflectUtil.getFieldValue(this, name)));
            return getMClass().getSimpleName() + dataMap;
        } catch (Exception e) {
            logger.error("toString err：{}", e.getMessage(), e);
        }
        return null;
    }
}
