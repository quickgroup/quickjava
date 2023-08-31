package org.quickjava.orm;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.quickjava.common.utils.ComUtil;
import org.quickjava.common.utils.DatetimeUtil;
import org.quickjava.common.utils.ReflectUtil;
import org.quickjava.orm.annotation.ModelField;
import org.quickjava.orm.annotation.ModelName;
import org.quickjava.orm.annotation.OneToMany;
import org.quickjava.orm.annotation.OneToOne;
import org.quickjava.orm.callback.WhereCallback;
import org.quickjava.orm.callback.WhereOptCallback;
import org.quickjava.orm.contain.*;
import org.quickjava.orm.enums.Operator;
import org.quickjava.orm.enums.RelationType;
import org.quickjava.orm.utils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.lang.reflect.*;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

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
 * */
@JsonIgnoreProperties(value = {"__meta", "__parent", "__withs", "__data", "__modified", "__querySet"}, ignoreUnknown = true)
public class Model implements IModel {

    /**
     * 模型元信息
     * */
    @JsonIgnore
    @TableField(exist = false)
    protected ModelMeta __meta;

    /**
     * 预载入属性
     * */
    @JsonIgnore
    @TableField(exist = false)
    protected List<String> __withs;

    /**
     * 数据
     */
    @JsonIgnore
    @TableField(exist = false)
    protected final DataMap __data = new DataMap();

    /**
     * 修改的字段
     * */
    @JsonIgnore
    @TableField(exist = false)
    protected List<ModelFieldO> __modified;

    /**
     * 查询器
     * */
    @JsonIgnore
    @TableField(exist = false)
    protected QuerySet __querySet;

    /**
     * 关联的父模型对象
     * */
    @JsonIgnore
    @TableField(exist = false)
    protected Model __parent;

    /**
     * 是素模型
     * */
    @JsonIgnore
    @TableField(exist = false)
    protected boolean __vegetarian = true;


    //TODO:---------- 类方法 ----------
    // 强制修改对象是否是素模型
    public Model vegetarian(boolean vegetarian) {
        this.__vegetarian = vegetarian;
        return this;
    }

    @JsonIgnore
    @TableField(exist = false)
    private static final Logger logger = LoggerFactory.getLogger(Model.class);

    public Model() {
        initModel(this, getClass());
    }

    @JsonIgnore
    protected void __initialize() {}

    private synchronized QuerySet query() {
        synchronized (Model.class) {
            if (__querySet == null) {
                __querySet = QuerySet.table(parseModelTableName(getClass()));
                QueryReservoir reservoir = ReflectUtil.getFieldValue(__querySet, "reservoir");
                reservoir.setWhereOptCallback(whereOptCallback, this);
            }
        }
        return __querySet;
    }

    private QueryReservoir reservoir() {
        return ReflectUtil.getFieldValue(query(), "reservoir");
    }

    //TODO:---------- 查询方法 ----------
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
        query.forEach((name, val) -> queryRet.put(fieldToUnderlineCase(name), val));
        // 调用querySet加载条件
        QuerySetHelper.loadQuery(query(), queryRet);
        return this;
    }

    public Model where(String sql) {
        query().where(sql, Operator.RAW, null);
        return this;
    }

    public Model where(WhereCallback callback)
    {
        query().where(callback);
        return this;
    }

    public Model whereOr(WhereCallback callback)
    {
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

    public Model in(String field, Object ...args) {
        return where(field, Operator.IN, args);
    }

    public Model notIn(String field, Object ...args) {
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

    //TODO::---------- 操作方法-增删改查 ----------

    /**
     * 新增
     * @return D
     * @param <D> D
     */
    public <D extends IModel> D insert()
    {
        // 默认填充数据
        __meta.fieldMap().forEach((name, field) -> {
            if (!__data.containsKey(name)) {
                if (field.isInsertFill()) {
                    data(name, ModelUtil.fill(field.getModelField().insertFill(), field.getModelField().insertFillTarget()));
                } else if (field.isUpdateFill()) {
                    data(name, ModelUtil.fill(field.getModelField().updateFill(), field.getModelField().updateFillTarget()));
                }
            }
        });
        // 执行
        Long pkVal = query().insert(this.sqlData());
        // 编译sql
        if (reservoir().isFetchSql())
            return toD(new ModelSql(reservoir().getSql()));
        // 回填主键
        data(pk(), pkVal);
        return toD(ModelUtil.isProxyModel(this) ? this : newModel(getMClass(), data()));
    }

    /**
     * 使用数据集新增
     * @param data 数据集
     * @return 模型对象
     * @param <D> D
     */
    public <D extends IModel> D insert(DataMap data) {
        return insert((Map<String, Object>) data);
    }

    public <D extends IModel> D insert(Map<String, Object> data) {
        data(data);
        return insert();
    }

    /**
     * 删除
     * @return 1
     */
    public int delete()
    {
        // 软删除字段
        for (ModelFieldO field : __meta.fieldMap().values()) {
            if (field.isSoftDelete()) {
                if (Date.class.isAssignableFrom(field.getField().getType())) {
                    data(field.getName(), DatetimeUtil.now());      // 字符串去填充的数据
                    save();
                } else {
                    throw new QuickORMException("不支持的软删除字段");
                }
                return 1;
            }
        }
        // 真实删除
        return query().delete();
    }

    /**
     * 更新
     * @return 模型对象
     * @param <D> D
     */
    public <D extends IModel> D update()
    {
        // 默认填充数据
        __meta.fieldMap().forEach((name, field) -> {
            if (!__data.containsKey(name)) {
                if (field.isUpdateFill()) {
                    data(name, ModelUtil.fill(field.getModelField().updateFill(), field.getModelField().updateFillTarget()));
                }
            }
        });
        // 执行
        query().update(this.sqlData());
        // 编译sql
        if (reservoir().isFetchSql())
            return toD(new ModelSql(reservoir().getSql()));
        // 返回模型
        return toD(ModelUtil.isProxyModel(this) ? this : newModel(getMClass(), data()));
    }

    public <D extends IModel> D update(DataMap data) {
        return update((Map<String, Object>) data);
    }

    public <D extends IModel> D update(Map<String, Object> data) {
        data(data);
        return update();
    }

    public <D extends IModel> D updateById()
    {
        String pk = pk();
        where(pk, data(pk));
//        __data.remove(pk);  // 不去更新主键
        return update();
    }

    /**
     * 保存数据
     * - 自动判断主键是否为null，为null执行新增，否则进行更新
     * @return 模型对象
     * @param <D> D
     */
    public <D extends IModel> D save() {
        String pk = pk();
        Object pkVal = data(pk);
        return pkVal == null ? insert() : where(pk, pkVal).update();
    }

    public <D extends IModel> D save(DataMap data) {
        return save((Map<String, Object>) data);
    }

    public <D extends IModel> D save(Map<String, Object> data) {
        data(data);
        save();
        return toD(this);
    }

    /**
     * 查询一条数据
     * @return 模型对象
     * @param <D> 模型类
     */
    public <D extends IModel> D find()
    {
        // 查询前：预载入字段准备
        queryBefore();
        // 执行查询
        List<Map<String, Object>> dataList = query().limit(0, 1).select();
        // 编译sql
        if (reservoir().fetchSql) {
            return toD(new ModelSql(query().buildSql()));
        }
        // 结果为空
        if (ModelUtil.isEmpty(dataList)) {
            return null;
        }
        // 装载
        List<IModel> models = resultTranshipment(getClass(), dataList);
        // 查询后：一对多数据加载
        queryAfter(models);
        return toD(models.get(0));
    }

    public <D extends IModel> D find(Serializable id) {
        query().where(query().pk(), id);
        return find();
    }

    public <D extends IModel> List<D> select() {
        // 查询前处理：预载入
        queryBefore();
        // 执行查询
        List<Map<String, Object>> dataList = query().select();
        // 编译sql
        if (reservoir().fetchSql) {
            return toD(new ModelListSql(reservoir().sql));
        }
        // 装载
        List<IModel> models = resultTranshipment(getClass(), dataList);
        // 查询后
        queryAfter(models);
        return toD(models);
    }

    /**
     * 获取指定字段的数据
     * @param field 字段名
     * @return 字段数据
     * @param <D> 数据代理类
     */
    public <D> D value(String field) {
        // 指定字段
        query().field(field);
        // 查询
        D ret = find();
        return ret;
    }

    public <D extends IModel> D[] values(String field) {
        // 指定字段
        query().field(field);
        // 查询
        List<D> ret = select();
        return null;
    }

    //TODO::---------- 分页方法 ----------
    public <D> Pagination<D> pagination(Integer page, Integer pageSize) {
        // 查询前处理：预载入
        queryBefore();
        // 执行查询
        Pagination<Map<String, Object>> pagination = query().pagination(page, pageSize);
        // 数据组装
        Pagination<IModel> pagination1 = new Pagination<>(pagination);
        pagination1.rows = resultTranshipment(getMClass(), pagination.rows);
        // 查询后
        queryAfter(pagination1.rows);
        return toD(pagination1);
    }

    public <D> Pagination<D> pagination() {
        return pagination(1, 20);
    }

    //TODO::---------- 操作方法：排序、聚合等 START ----------
    /**
     * 排序
     * @param field 字段
     * @param asc 排序方式：ASC、DESC
     * @return 模型对象
     */
    public Model order(String field, String asc) {
        query().order(fieldToUnderlineCase(field), asc);
        return this;
    }

    public Model order(String field, boolean asc) {
        order(field, asc ? "ASC" : "DESC");
        return this;
    }

    public Model order(String fields)
    {
        if (ORMHelper.isEmpty(fields)) {
            return this;
        }
        if (fields.contains(",")) {
            return this.order(fields.split(","));
        }
        String[] arr = fields.trim().split(" ");
        return arr.length == 2 ? order(arr[0], arr[1]) : order(arr[0], "ASC");
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

    public Model limit(Integer index, Integer count) {
        query().limit(index, count);
        return this;
    }

    public Model limit(Integer count) {
        return limit(0, count);
    }

    /**
     * 分页
     * @param page 页数
     * @return 模型对象
     */
    public Model page(Integer page) {
        query().page(page);
        return this;
    }

    /**
     * 分页
     * @param page 页数
     * @param size 页大小
     * @return 模型对象
     */
    public Model page(Integer page, Integer size) {
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
    //TODO::---------- 操作方法：排序、聚合等 END ----------

    //TODO::---------- 数据方法 START ----------
    /**
     * 实体通过map装载数据
     * @param data 数据集
     * @return 模型对象
     */
    public Model data(DataMap data) {
        return data((Map<String, Object>) data);
    }

    public Model data(Map<String, Object> data) {
        data.forEach(this::data);
        return this;
    }

    /**
     * 获取data中的数据
     * @param field 属性名
     * @return 属性值
     */
    public Object data(String field) {
        return data().get(field);
    }

    /**
     * 装载数据
     * @param name 属性名
     * @param val 属性值
     * @return 模型对象
     */
    public Model data(String name, Object val)
    {
        // 数据保存
        name = ModelUtil.toCamelCase(name);
        ModelFieldO field = __meta.fieldMap().get(name);
        // 非本表属性或关联属性不设置
        if (field == null || field.getWay() != null) {
            return this;
        }

        __data.put(name, val);
        ReflectUtil.setFieldValue(this, name, val);

        // 被修改的字段
        if (__modified == null) {
            __modified = new LinkedList<>();
        }
        __modified.add(field);
        return this;
    }

    /**
     * 获取data全部数据
     * @return 模型数据
     */
    public DataMap data() {
        this.loadingVegetarianModel();
        return __data;
    }

    public String pk() {
        return __meta.getPkName();
//        return ModelUtil.toCamelCase(query().pk());
    }

    public Object pkVal() {
        return data(pk());
    }

    /**
     * 手机执行sql的数据
     * - insert、update的数据
     * @return 数据集
     */
    private DataMap sqlData()
    {
        DataMap data = data();
        DataMap ret = DataMap.one();
        if (__modified != null) {
            __modified.forEach(field -> {
                if (field.getWay() != null) {
                    return;
                }
                Object v = data.get(field.getName());
                ret.put(fieldToUnderlineCase(field.getName()), ModelUtil.valueToSqlValue(v));
            });
        }
        return ret;
    }
    //TODO::---------- 数据方法 END ----------

    //TODO::---------- 静态操作方法 START ----------
    /**
     * 通过 Map 创建对象
     * @param data 数据集
     * @return 模型对象
     */
    public Model create(Map<String, Object> data)
    {
        Model model = newModel(getMClass(), data);
        model.insert();
        return model;
    }

    /**
     * 通过 DataMap 创建对象
     * @param data 数据集
     * @return 模型对象
     */
    public Model create(DataMap data) {
        return create((Map<String, Object>) data);
    }

    public Model create(Model model) {
        return model.insert();
    }

    /**
     * 批量创建
     * @param dataList 数据列表
     * @return 对象数量
     */
    public Integer bulkCreate(List<DataMap> dataList)
    {
        dataList.forEach(data -> {
            Model model = newModel(this.getMClass());
            model.save(data);
        });
        return dataList.size();
    }
    //TODO::---------- 静态操作方法 END ----------

    //TODO::---------- 模型控制方法 ----------
    /**
     * 查询前处理预载入
     * T1：一对一的字段声明
     * */
    private void queryBefore()
    {
        // 软删除字段
        for (ModelFieldO field : __meta.fieldMap().values()) {
            if (field.isSoftDelete()) {
                if (Date.class.isAssignableFrom(field.getField().getType())) {
                    where(field.getName(), Operator.IS_NULL);
                } else {
                    throw new QuickORMException("不支持的软删除字段");
                }
            }
        }
        // 关联属性字段载入
        Map<String, Relation> relationMap = getWithRelation(new RelationType[]{RelationType.OneToOne});
        if (relationMap.size() == 0) {
            return;
        }

        // T1::字段声明
        List<String> fields = new LinkedList<>();
        // 本表字段声明
        __meta.fieldMap().forEach((name, field) -> {
            if (field.getWay() != null || Model.class.isAssignableFrom(field.getClazz())) {
                return;
            }
            name = toUnderlineCase(name);
            fields.add(fieldAlias(__meta.table(), name));
        });

        // 关联表
        relationMap.forEach((relationName, relation) -> {
            ModelMeta meta = ModelUtil.getMeta(relation.getClazz());
            // 关联表字段声明
            meta.fieldMap().forEach((name, field) -> {
                if (field.getWay() != null || Model.class.isAssignableFrom(field.getClazz())) {
                    return;
                }
                name = toUnderlineCase(name);
                fields.add(fieldAlias(relationName, name));
            });
            // 关联方式声明
            query().join(meta.table() + " " + relationName, String.format("%s.%s = %s.%s",
                            relationName, toUnderlineCase(relation.foreignKey()),
                            __meta.table(), toUnderlineCase(relation.localKey())
                    ), "LEFT");
        });
        // 装填字段
        query().field(fields);
    }

    /**
     * 查询后处理预载入
     * - 组装一对一数据
     * - 一对多的关联在主数据返回后再统一查询组装
     * */
    private <D extends IModel> List<D> resultTranshipment(Class<?> clazz, List<Map<String, Object>> dataList) {
        List<D> models = new LinkedList<>();
        Map<String, Relation> relationMap = getWithRelation(new RelationType[]{RelationType.OneToOne});
        // 逐个装载
        dataList.forEach(data -> {
            D model = newModel(clazz);
            // 装载关联属性
            if (relationMap.size() > 0) {
                // 主表数据
                resultTranshipmentWith(model, data, null);
                // 关联表数据
                relationMap.forEach((relationName, relation) -> {
                    Model relationModel = newModel(relation.getClazz(), null, model);
                    resultTranshipmentWith(relationModel, data, relationName);
                    ReflectUtil.setFieldValue(model, relationName, relationModel);
                });
            } else {
                ((Model) model).data(data);
            }
            models.add(model);
        });
        return models;
    }

    private void resultTranshipmentWith(IModel model, Map<String, Object> set, String alias) {
        Model modelAbs = ((Model) model);
        modelAbs.__meta.fieldMap().forEach((name, field) -> {
            if (field.getWay() != null || Model.class.isAssignableFrom(field.getClazz())) {
                return;
            }
            String tableName = alias == null ? modelAbs.__meta.table() : alias;
            String dataName = tableName + "__" + toUnderlineCase(name);
            modelAbs.data(name, set.get(dataName));
        });
    }

    /**
     * 查询后模型处理
     * @param models 数据集
     * */
    private void queryAfter(List<IModel> models) {
        // 预载入的数据查询后加载
        if (__withs != null) {
            // 超过500警告
            if (models.size() > 500) {
                logger.warn("QuickJava-ORM：The current query has too much data and may cause the service to crash. models.size=" + models.size());
            }
            // 数据关联条件：关联属性名=关联id
            Map<String, List<Object>> conditionMap = new LinkedHashMap<>();
            // 一对多数据条件准备
            Map<String, Relation> relationMap = getWithRelation(new RelationType[]{RelationType.OneToMany});
            relationMap.forEach((fieldName, relation) -> {
                if (!conditionMap.containsKey(fieldName)) {
                    conditionMap.put(fieldName, new LinkedList<>());
                }
                models.forEach(model -> {
                    Object localKeyValue = ReflectUtil.getFieldValue(model, relation.localKey());
                    if (!conditionMap.get(fieldName).contains(localKeyValue)) {  // 避免相同关联数据重复查询
                        conditionMap.get(fieldName).add(localKeyValue);
                    }
                });
            });
            // 查询
            relationMap.forEach((fieldName, relation) -> {
                if (conditionMap.get(fieldName).size() == 0) {
                    return;
                }
                Model queryModel = newModel(relation.getClazz());
                List<Model> rows = queryModel.where(relation.foreignKey(), Operator.IN, conditionMap.get(fieldName)).select();
                // 装填关联模型
                models.forEach(model -> {
                    Object modelKeyVal = ReflectUtil.getFieldValue(model, relation.localKey());
                    List<Model> set = rows.stream().filter(row -> modelKeyVal.equals(ReflectUtil.getFieldValue(row, relation.foreignKey())))
                            .collect(Collectors.toList());
                    ReflectUtil.setFieldValue(model, fieldName, set);
                });
            });
        }
    }

    /**
     * 获取预载入的属性名对应模型类
     * @param types 关联类型
     * @return 关联属性集合
     */
    private Map<String, Relation> getWithRelation(RelationType[] types) {
        Map<String, Relation> relationMap = new LinkedHashMap<>();
        if (__withs != null) {
            __withs.forEach(name -> {
                Relation relation = __meta.relationMap().get(name);
                if (relation != null && SqlUtil.inArray(types, relation.getType())) {
                    relationMap.put(name, __meta.relationMap().get(name));
                }
            });
        }
        return relationMap;
    }

    private static <D> D toD(Object model) {
        return (D) model;
    }

    //TODO::---------- 模型实例化----------
    public static<D extends IModel> D newModel(Class<?> clazz) {
        return newModel(clazz, null, null);
    }

    public static<D extends IModel> D newModel(Object entity) {
        return newModel(entity.getClass(), null, null);
    }

    private static<D extends IModel> D newModel(Class<?> clazz, Map<String, Object> data) {
        return newModel(clazz, data, null);
    }

    private static<D extends IModel> D newModel(Class<?> clazz, Model parent) {
        return newModel(clazz, null, parent);
    }

    private static<D extends IModel> D newModel(Class<?> clazz, Map<String, Object> data, IModel parent)
    {
        // 创建代理类信息
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(getModelClass(clazz));
        enhancer.setCallback(modelProxyMethodInterceptor);
        D model = toD(enhancer.create());
        // 设置
        ModelUtil.setFieldValue(model, "__table", parseModelTableName(clazz));
        // 加载数据
        if (!ModelUtil.isEmpty(data)) {
            ((Model) model).data((DataMap) data);
        }
        // 设置父类
        if (!ModelUtil.isEmpty(parent)) {
            ReflectUtil.setFieldValueDirect(model, "__parent", parent);
        }
        return model;
    }

    @JsonIgnore
    @TableField(exist = false)
    private static final MethodInterceptor modelProxyMethodInterceptor = new MethodInterceptor () {
        @Override
        public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
            /*
             * 1. 缓存全部需要代理的关联属性
             * 2. 判断当前方法是否是关联属性的查询方法、获取方法
             * 3. 如果是就对返回数据处理
             * */
            Class<?> clazz = getModelClass(o.getClass());
            String methodName = method.getName();
            ModelMeta meta = ModelUtil.getMeta(clazz);
            // getter方法和关联方法
            if (meta.relationMap().containsKey(methodName)) {
                return LoadingRelationGetter(clazz, o, method, objects);
            }
            // setter方法
            if (methodName.startsWith("set")) {
                String fieldName = ModelUtil.toCamelCase(methodName.substring(3));
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
    private void loadingVegetarianModel()
    {
        if (ModelUtil.isVegetarianModel(this) && __vegetarian) {
            // 收集字段数据
            __meta.fieldMap().forEach((name, field) -> {
                Object val = ReflectUtil.getFieldValue(this, field.getField());
                data(field.getName(), val);
            });
        }
    }

    //---------- TODO::关联方法 ----------//
    /*
     * 关联方法
     * */
    private <D> D relation(String fieldName, Class<?> clazz, RelationType type, String localKey, String foreignKey) {
        // 缓存关联关系
        if (!__meta.relationMap().containsKey(fieldName)) {
            __meta.relationMap().put(fieldName, new Relation(clazz, type, localKey, foreignKey));
        }
        // 返回查询模型
        return newModel(clazz, this);
    }

    public <D extends IModel> D relation(Class<?> clazz, RelationType type, String localKey, String foreignKey) {
        String fieldName = Thread.currentThread().getStackTrace()[1].getMethodName();
        return relation(fieldName, clazz, type, localKey, foreignKey);
    }

    public <D extends IModel> D relation(String clazzName, RelationType type, String localKey, String foreignKey) {
        try {
            Class<?> clazz = getClass().getClassLoader().loadClass(clazzName);
            String fieldName = Thread.currentThread().getStackTrace()[2].getMethodName();
            return relation(fieldName, clazz, type, localKey, foreignKey);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public <D extends IModel> D hasOne(Class<?> clazz, String localKey, String foreignKey) {
        String fieldName = Thread.currentThread().getStackTrace()[2].getMethodName();
        return relation(fieldName, clazz, RelationType.OneToOne, localKey, foreignKey);
    }

    public <D extends IModel> D hasMany(Class<?> clazz, String localKey, String foreignKey) {
        String fieldName = Thread.currentThread().getStackTrace()[2].getMethodName();
        return relation(fieldName, clazz, RelationType.OneToMany, localKey, foreignKey);
    }

    /**
     * 预载入方法
     * @param fields 需要预载入的属性名称
     * @return 模型对象
     */
    public Model with(String fields) {
        if (ModelUtil.isEmpty(fields)) {
            return this;
        }
        List<String> withs = Arrays.asList(fields.split(","));
        ComUtil.trimList(withs);
        this.__withs = QuerySetHelper.initList(this.__withs);
        this.__withs.addAll(withs);
        return this;
    }

    private static boolean isModel(Class<?> clazz) {
        return Model.class.isAssignableFrom(clazz);
    }

    private static boolean isCollection(Class<?> clazz) {
        return Collection.class.isAssignableFrom(clazz);
    }

    // 缓存字段和方法
    private static void initModel(Model model, Class<?> clazz)
    {
        if (!Model.class.isAssignableFrom(clazz)) {
            return;
        }
        if (Enhancer.isEnhanced(clazz)) {
            clazz = clazz.getSuperclass();
        }

        // 已加载模型元组信息
        if (ModelUtil.getMeta(clazz) != null) {
            model.__meta = ModelUtil.getMeta(clazz);
            return;
        }

        // 初始化模型信息
        ModelMeta meta = model.__meta = new ModelMeta();
        meta.setTable(parseModelTableName(clazz));
        meta.setClazz(clazz);
        meta.setFieldMap(new LinkedHashMap<>());
        ModelUtil.setMeta(clazz, meta);

        // 全部方法、属性
        Class<?> getClazz = clazz;
        List<Field> fieldList = new ArrayList<>();
        Map<String, Method> methodMap = new LinkedHashMap<>();
        while (getClazz != null){
            // 属性
            fieldList.addAll(new ArrayList<>(Arrays.asList(getClazz.getDeclaredFields())));
            // 方法
            for (Method method : getClazz.getDeclaredMethods()) {
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
            ModelFieldO fieldInfo = new ModelFieldO(field);
            String fieldName = fieldInfo.getName();
            // 兼容mybatis-plus字段说明
            TableField tableField = field.getAnnotation(TableField.class);
            // 模型字段说明
            ModelField modelField = field.getAnnotation(ModelField.class);
            if (modelField != null) {
                if (!"".equals(modelField.name())) {
                    fieldInfo.setName(modelField.name()); // 指定字段名称
                }
                fieldInfo.setModelField(modelField);
            }

            fieldInfo.setWay(findRelationAno(field));

            // 有关联方法
            Method method = methodMap.get(fieldName);
            if (method != null && Model.class.isAssignableFrom(method.getReturnType())) {
                try {
                    method.invoke(model);
                    fieldInfo.setWay(meta.relationMap().get(fieldName));
                } catch (IllegalAccessException | InvocationTargetException ignore) {
                }
            } else {
                // 非关联字段且隐藏
                if (tableField != null && !tableField.exist()) {
                    continue;
                }
                if (modelField != null && !modelField.exist()) {
                    continue;
                }
            }

            meta.fieldMap().put(field.getName(), fieldInfo);
        }

        // 模型初始化方法回调
        model.__initialize();
    }

    private static Object findRelationAno(Field field)
    {
        if (field.getDeclaredAnnotation(OneToOne.class) != null) {
            return field.getDeclaredAnnotation(OneToOne.class);
        } else if (field.getDeclaredAnnotation(OneToMany.class) != null) {
            return field.getDeclaredAnnotation(OneToMany.class);
        }
        return null;
    }

    private static String parseModelTableName(Class<?> clazz)
    {
        clazz = getModelClass(clazz);
        String tableName = null;
        // 获取mybatis-plus的tableName注解
        TableName tableNameAnno = clazz.getAnnotation(TableName.class);
        if (tableNameAnno != null && !"".equals(tableNameAnno.value())) {
            tableName = tableNameAnno.value();
        }
        ModelName ModelNameAnno = clazz.getAnnotation(ModelName.class);
        if (ModelNameAnno != null && !"".equals(ModelNameAnno.value())) {
            tableName = ModelNameAnno.value();
        }
        // 默认取模型名称转下划线
        if (tableName == null) {
            tableName = clazz.getSimpleName();
            if (tableName.endsWith("Model")) {
                tableName = tableName.substring(0, tableName.lastIndexOf("Model"));
            }
        }
        return toUnderlineCase(tableName);
    }

    /**
     * 模型setter数据处理
     * - 保存修改的数据
     * */
    private static void Setter(Class<?> clazz, Object o, Method method, Object arg, String fieldName)
    {
        try {
            Model model = (Model) o;
            model.data(fieldName, arg);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * getter懒加载数据
     * */
    private static Object LoadingRelationGetter(Class<?> clazz, Object o, Method method, Object ...objects)
    {
        try {
            // 加载
            ModelFieldO modelField = ModelUtil.getMeta(clazz).fieldMap().get(method.getName());
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
                if (modelField.getWay() != null && Relation.class.isAssignableFrom(modelField.getWay().getClass())) {
                    Relation relation = (Relation) modelField.getWay();
                    if (relation.getType() == RelationType.OneToOne) {
                        Model relationModel = newModel(fieldType, curr);
                        // 设置全部关联属性为空，避免多重查询
                        Object localValue = ReflectUtil.getFieldValue(curr.getMClass(), curr, relation.localKey());
                        Object fieldValue = relationModel.where(relation.foreignKey(), localValue).find();  // 查询结果
                        curr.data(fieldName, fieldValue);
                        return fieldValue;
                    } else if (relation.getType() == RelationType.OneToMany) {
                        if (field.getGenericType() instanceof ParameterizedType) {
                            ParameterizedType pt = (ParameterizedType) field.getGenericType();
                            Class<?> genericClazz = (Class<?>) pt.getActualTypeArguments()[0];
                            Model relationModel = newModel(genericClazz, curr);
                            Object localValue = ReflectUtil.getFieldValue(curr.getMClass(), curr, relation.localKey());
                            Object fieldValue = relationModel.where(relation.foreignKey(), localValue).select();  // 查询结果
                            curr.data(fieldName, fieldValue);
                            return fieldValue;
                        }
                    }
                }
                // 懒加载一
                else if (isModel(fieldType) && OneToOne.class.isAssignableFrom(modelField.getWay().getClass())) {
                    OneToOne one = (OneToOne) modelField.getWay();
                    Model relationModel = newModel(fieldType, curr);
                    // 设置全部关联属性为空，避免多重查询
                    Object localValue = ReflectUtil.getFieldValue(curr.getMClass(), curr, one.localKey());
                    Object fieldValue = relationModel.where(one.foreignKey(), localValue).find();  // 查询结果
                    curr.data(fieldName, fieldValue);
                    return fieldValue;
                } else if (isCollection(fieldType) && OneToMany.class.isAssignableFrom(modelField.getWay().getClass())) {
                    OneToMany many = (OneToMany) modelField.getWay();
                    if (field.getGenericType() instanceof ParameterizedType) {
                        ParameterizedType pt = (ParameterizedType) field.getGenericType();
                        Class<?> genericClazz = (Class<?>) pt.getActualTypeArguments()[0];
                        Model relationModel = newModel(genericClazz, curr);
                        Object localValue = ReflectUtil.getFieldValue(curr.getMClass(), curr, many.localKey());
                        Object fieldValue = relationModel.where(many.foreignKey(), localValue).select();  // 查询结果
                        curr.data(fieldName, fieldValue);   // 保存查询到的数据
                        return fieldValue;
                    }
                }
            }
            return null;

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("getter数据查询异常：" + e);
        }
    }

    private Class<?> getMClass() {
        return Enhancer.isEnhanced(getClass()) ? getClass().getSuperclass() : getClass();
    }

    private static Class<?> getModelClass(Class<?> clazz) {
        return ModelUtil.getModelClass(clazz);
    }

    private static String fieldAlias(String table, String field) {
        return table + "." + field + " AS " + table + "__" + field;
    }

    /**
     * 字段转下划线格式
     * @param field 属性名
     * @return 结果
     */
    private static String fieldToUnderlineCase(String field) {
        if (field.contains(".")) {
            String[] arr = field.split("\\.");
            return arr[0] + "." + toUnderlineCase(arr[1]);
        } else {
            return toUnderlineCase(field);
        }
    }

    private static String toUnderlineCase(String name) {
        return ModelUtil.toUnderlineCase(name);
    }

    // 默认转换字段大小写
    @JsonIgnore
    @TableField(exist = false)
    private static final WhereOptCallback whereOptCallback = (where, querySet, userData) -> {
        // 子查询条件不处理
        if (where.getChildren() != null) {
            return;
        }
        Model model = (Model) userData;
        String field = fieldToUnderlineCase(where.getField());
        if (ComUtil.isNotEmpty(model.__withs)) {
            if (!field.contains(".")) {
                field = model.__meta.table() + "." + field;
            }
        }
        where.setField(field);
    };

    @Override
    public String toString() {
        Map<String, Object> dataMap = new LinkedHashMap<>(data());
        __meta.relationMap().forEach((name, relation) -> dataMap.put(name, ReflectUtil.getFieldValue(this, name)));
        return getMClass().getSimpleName() + dataMap;
    }
}
