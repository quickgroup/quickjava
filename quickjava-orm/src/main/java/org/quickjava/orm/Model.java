package org.quickjava.orm;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.quickjava.common.utils.ComUtil;
import org.quickjava.common.utils.ReflectUtil;
import org.quickjava.orm.annotation.ModelField;
import org.quickjava.orm.annotation.ModelName;
import org.quickjava.orm.annotation.OneToMany;
import org.quickjava.orm.annotation.OneToOne;
import org.quickjava.orm.contain.*;
import org.quickjava.orm.enums.RelationType;
import org.quickjava.orm.utils.*;

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
public class Model {

    /**
     * 模型元信息
     * */
    @JsonIgnore
    @TableField(exist = false)
    private ModelMeta __meta;

    /**
     * 关联的父模型对象
     * */
    @JsonIgnore
    @TableField(exist = false)
    private Model __parent;

    /**
     * 预载入属性
     * */
    @JsonIgnore
    @TableField(exist = false)
    private List<String> __withs;

    /**
     * 数据
     */
    @JsonIgnore
    @TableField(exist = false)
    private DataMap __data = new DataMap();

    /**
     * 修改的字段
     * */
    @JsonIgnore
    @TableField(exist = false)
    private List<String> __modified;

    @JsonIgnore
    @TableField(exist = false)
    private QuerySet __querySet = null;

    public Model() {
        initModel(this, getClass());
    }

    private synchronized QuerySet query() {
        synchronized (Model.class) {
            if (__querySet == null) {
                __querySet = QuerySet.table(parseModelTableName(getClass()));
            }
        }
        return __querySet;
    }

    public Model where(String field, Operator opr, Object val) {
        query().where(whereFieldName(field), opr, val);
        return this;
    }

    public Model where(String field, String opr, Object val) {
        query().where(whereFieldName(field), Operator.valueOf(opr), val);
        return this;
    }

    public Model where(String field, Object val) {
        where(field, Operator.EQ, val);
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

    /**
     * sql语句查询
     * @param sql 原生sql语句
     * @return 模型对象
     */
    public Model where(String sql) {
        query().where(sql, Operator.RAW, null);
        return this;
    }

    /**
     * 闭包查询
     * @param callback 闭包方法
     * @return 模型对象
     */
    public Model where(WhereCallback callback)
    {
        query().where(callback);
        return this;
    }

    /**
     * 闭包查询
     * @param callback 闭包方法
     * @return 模型对象
     */
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

    /**
     * 补全字段名称
     * - 预载入补全表名
     * */
    private String whereFieldName(String field) {
        field = fieldToUnderlineCase(field);
        if (__withs != null) {
            if (!field.contains(".")) {
                field = __meta.table() + "." + field;
            }
        }
        return field;
    }

    /**
     *
     * 如果当前对象素模型（不是代理模型），就加载一次对象上的数据
     * - 不是：在 insert、update 时需要收集字段数据
     * - 收集字段数据
     */
    private void loadingVegetarianModel()
    {
        if (ModelUtil.isVegetarianModel(this)) {
            // 收集字段数据
            __meta.fieldMap().forEach((name, field) -> {
                Object val = ReflectUtil.getFieldValue(this, field.getField());
                if (__data.containsKey(name) && !ModelUtil.objectEquals(__data.get(name), val)) {
                    data(field.getName(), val);
                }
            });
        }
    }

    /**
     * 新增
     * @return 模型对象
     */
    public Model insert()
    {
        Long pkVal = query().insert(this.sqlData());
        data(pk(), pkVal);
        return ModelUtil.isProxyModel(this) ? this : newProxyModel(getMClass(), data());
    }

    /**
     * 使用数据集新增
     * @param data 数据集
     * @return 模型对象
     */
    public Model insert(DataMap data) {
        data(data);
        return insert();
    }

    /**
     * 更新
     * @return 模型对象
     */
    public Model update()
    {
        query().update(this.sqlData());
        return ModelUtil.isProxyModel(this) ? this : newProxyModel(getMClass(), data());
    }

    public Model update(DataMap data) {
        data(data);
        return update();
    }

    public Model updateById() {
        String pk = pk();
        where(pk, data(pk));
        __data.remove(pk);  // 不去更新主键
        return update();
    }

    public String pk() {
        return ModelUtil.toCamelCase(query().getColumnPk());
    }

    public String pkOri() {
        return query().getColumnPk();
    }

    public Object pkVal() {
        return data(pk());
    }

    /**
     * 保存数据
     * - 自动判断主键是否为null，为null执行新增，否则进行更新
     * @return 模型对象
     */
    public Model save() {
        String pk = pk();
        Object pkVal = data(pk);
        return pkVal == null ? insert() : where(pk, pkVal).update();
    }

    public Model save(DataMap data) {
        data(data);
        save();
        return this;
    }

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

    public String fetchSql() {
        return query().fetchSql();
    }

    //TODO::--------------模型自用方法--------------

    /**
     * 字段转下划线格式
     * @param field 属性名
     * @return 结果
     */
    private static String fieldToUnderlineCase(String field) {
        if (field.contains(".")) {
            String[] arr = field.split("\\.");
            return arr[0] + "." + ModelUtil.toUnderlineCase(arr[1]);
        } else {
            return ModelUtil.toUnderlineCase(field);
        }
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
            return order(Arrays.asList(fields.split(",")));
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

    //---------- TODO::数据查询方法 ----------//

    /**
     * 查询一条数据
     * @return 模型对象
     * @param <D> 模型类
     */
    public <D extends Model> D find() {
        // 查询前：预载入字段准备
        queryBefore();
        // 数据装填
        List<Map<String, Object>> dataList = query().limit(0, 1).select();
        if (ModelUtil.isEmpty(dataList)) {
            return null;
        }
        // 装载
        List<Model> models = resultTranshipment(getClass(), dataList);
        // 查询后：一对多数据加载
        queryAfter(models);
        return (D) models.get(0);
    }

    public <D extends Model> D find(Serializable id) {
        query().where(query().getColumnPk(), id);
        return find();
    }

    public <D extends Model> List<D> select() {
        // 查询前处理：预载入
        queryBefore();
        // 执行查询
        List<Map<String, Object>> dataList = query().select();
        // 装载
        List<Model> models = resultTranshipment(getClass(), dataList);
        // 查询后
        queryAfter(models);
        return (List<D>) models;
    }

    /**
     * 查询前处理预载入
     * - 一对一的字段声明
     * */
    private void queryBefore() {
        Map<String, Relation> relationMap = getWithRelation(new RelationType[]{RelationType.OneToOne});
        if (relationMap == null || relationMap.size() == 0) {
            return;
        }

        // 本表字段声明
        List<String> fields = new LinkedList<>();
        __meta.fieldMap().forEach((name, field) -> {
            if (field.getWay() != null || Model.class.isAssignableFrom(field.getClazz())) {
                return;
            }
            name = ModelUtil.toUnderlineCase(name);
            fields.add(__meta.table() + "." + name + " AS " + __meta.table() + "__" + name);
        });

        // 关联表字段声明
        relationMap.forEach((aliasName, relation) -> {
            ModelMeta meta = ModelUtil.getMeta(relation.getClazz());
            meta.fieldMap().forEach((name, field) -> {
                if (field.getWay() != null || Model.class.isAssignableFrom(field.getClazz())) {
                    return;
                }
                name = ModelUtil.toUnderlineCase(name);
                fields.add(aliasName + "." + name + " AS " + aliasName + "__" + name);
            });
            // join
            query().join(meta.table() + " " + aliasName,
                    String.format("%s.%s = %s.%s", aliasName, ModelUtil.toUnderlineCase(relation.foreignKey()),
                    __meta.table(), ModelUtil.toUnderlineCase(relation.localKey())), "LEFT");
        });
        // 查询器
        query().field(fields);
    }

    /**
     * 查询后处理预载入
     * - 组装一对一数据
     * - 一对多的关联在主数据返回后再统一查询组装
     * */
    private <D extends Model> List<D> resultTranshipment(Class<?> clazz, List<Map<String, Object>> dataList) {
        // 集合对象
        List<D> models = new LinkedList<>();
        dataList.forEach(data -> {
            D model = newProxyModel(clazz);
            // 装载关联属性
            Map<String, Relation> relationMap = getWithRelation(new RelationType[]{RelationType.OneToOne});
            if (relationMap.size() > 0) {
                // 主表数据
                resultTranshipmentWith(model, data, null);
                // 关联表数据
                relationMap.forEach((relationName, relation) -> {
                    Model relationModel = newProxyModel(relation.getClazz());
                    resultTranshipmentWith(relationModel, data, relationName);
                    ReflectUtil.setFieldValue(model, relationName, relationModel);
                });
            } else {
                model.data(data);
            }
            models.add(model);
        });
        return models;
    }

    private void resultTranshipmentWith(Model model, Map<String, Object> set, String relationName) {
        model.__meta.fieldMap().forEach((name, field) -> {
            if (field.getWay() != null || Model.class.isAssignableFrom(field.getClazz())) {
                return;
            }
            String aliasName = relationName == null ? model.__meta.table() : relationName;
            String dataName = aliasName + "__" + ModelUtil.toUnderlineCase(name);
            model.data(name, set.get(dataName));
        });
    }

    /**
     * 查询后模型处理
     * @param models 数据集
     * */
    private void queryAfter(List<Model> models) {
        // 预载入的数据查询后加载
        if (__withs != null) {
            // 数据关联条件：关联属性名=关联id
            Map<String, List<Object>> conditionMap = new LinkedHashMap<>();
            // 一对多数据条件准备
            Map<String, Relation> relationMap = getWithRelation(new RelationType[]{RelationType.OneToMany});
            relationMap.forEach((fieldName, relation) -> {
                if (!conditionMap.containsKey(fieldName)) {
                    conditionMap.put(fieldName, new LinkedList<>());
                }
                models.forEach(model -> conditionMap.get(fieldName).add(ReflectUtil.getFieldValue(model, relation.localKey())));
            });
            // 查询
            relationMap.forEach((fieldName, relation) -> {
                if (conditionMap.get(fieldName).size() == 0) {
                    return;
                }
                Model queryModel = newModel(relation.getClazz());
                List<Model> rows = queryModel.where(fieldToUnderlineCase(relation.foreignKey()), Operator.IN, conditionMap.get(fieldName)).select();
                // 数据装填
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

    //---------- TODO::分页方法 ----------//
    public <D> Pagination<D> pagination(Integer page, Integer pageSize) {
        // 查询前处理：预载入
        queryBefore();
        // 执行查询
        Pagination<Map<String, Object>> pagination = query().pagination(page, pageSize);
        // 数据组装
        Pagination<Model> pagination1 = new Pagination<>(pagination);
        pagination1.rows = resultTranshipment(getMClass(), pagination.rows);
        // 查询后
        queryAfter(pagination1.rows);
        return (Pagination<D>) pagination1;
    }

    public <D> Pagination<D> pagination() {
        return pagination(1, 20);
    }

    // 提取某字段为list
    public <D> D selectFieldList(String field) {
        String fieldLine = fieldToUnderlineCase(field);
        List<Map<String, Object>> dataList = query().field(fieldLine).select();
        List<Object> ret = new LinkedList<>();
        dataList.forEach(v -> ret.add(v.get(fieldLine)));
        return (D) ret;
    }

    // 提取某字段为数组
    public <D> D selectFieldArray(String field) {
        List<Object> ret = selectFieldList(field);
        return (D) ret.toArray();
    }

    public int delete() {
        return query().delete();
    }

    //---------- TODO::数据操作方法 ----------//

    /**
     * 实体通过map装载数据
     * @param data 数据集
     * @return 模型对象
     */
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
        org.quickjava.orm.contain.ModelField field = __meta.fieldMap().get(name);
        if (field != null && field.getWay() == null) {
            __data.put(name, val);      // 只保存本类字段数据，关联数据不缓存
        }
        ReflectUtil.setFieldValue(this, name, val);

        // 被修改的字段
        if (__modified == null) {
            __modified = new LinkedList<>();
        }
        __modified.add(name);

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

    public static Map<String, Object> dataFieldConvLine(Map<String, Object> data, Class<?> clazz) {
        Map<String, org.quickjava.orm.contain.ModelField> fieldMap = ModelUtil.getMeta(clazz).fieldMap();
        Map<String, Object> ret = new LinkedHashMap<>();
        data.forEach((k, v) -> {
            if (fieldMap.containsKey(ModelUtil.toCamelCase(k))) {
                ret.put(fieldToUnderlineCase(k), v);
            }
        });
        return ret;
    }

    /**
     * 执行sql的数据
     * - insert、update的数据
     * @return 数据集
     */
    public DataMap sqlData()
    {
        DataMap data = data();
        DataMap ret = DataMap.one();
        __modified.forEach(name -> {
            Object v = data.get(name);
            if (v instanceof Model) {
                // 关联模型数据不能一起写入
            } else {
                ret.put(fieldToUnderlineCase(name), ModelUtil.valueToSqlValue(v));
            }
        });
        return ret;
    }

    //---------- TODO::静态操作方法 ----------//

    /**
     * 通过 Map 创建对象
     * @param data 数据集
     * @return 模型对象
     */
    public Model create(Map<String, Object> data)
    {
        Model model = newProxyModel(getMClass(), data);
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
        List<DataMap> dataList2 = new LinkedList<>();
        dataList.forEach(map -> {
            DataMap data = new DataMap();
            map.forEach((k, v) -> data.put(fieldToUnderlineCase(k), v));
            dataList2.add(data);
        });
        query().insertAll(dataList2);
        return dataList2.size();
    }

    public static<D extends Model> D newProxyModel(Class<?> clazz) {
        return newProxyModel(clazz, null, null);
    }

    public static<D extends Model> D newProxyModel(Class<?> clazz, Map<String, Object> data) {
        return newProxyModel(clazz, data, null);
    }

    public static<D extends Model> D newProxyModel(Class<?> clazz, Map<String, Object> data, Model parent)
    {
        // 创建代理类信息
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(clazz);
        enhancer.setCallback(modelProxyMethodInterceptor);
        D model = (D) enhancer.create();
        // 加载数据
        if (!ModelUtil.isEmpty(data)) {
            model.data(data);
        }
        // 设置父类
        if (!ModelUtil.isEmpty(parent)) {
            ReflectUtil.setFieldValueDirect(model, "__parent", parent);
        }
        return model;
    }

    @JsonIgnore
    private static MethodInterceptor modelProxyMethodInterceptor = new MethodInterceptor () {
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
            if (meta.relationMap().containsKey(methodName)) {       // 存在关联属性的方法
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

    private boolean isEmpty() {
        return __data == null || __data.isEmpty();
    }

    //---------- TODO::关联方法 ----------//
    /*
     * 关联方法
     * */
    public <D> D relation(String fieldName, Class<?> clazz, RelationType type, String localKey, String foreignKey) {
        // 缓存关联关系
        if (!__meta.relationMap().containsKey(fieldName)) {
            __meta.relationMap().put(fieldName, new Relation(clazz, type, localKey, foreignKey));
        }
        // 返回查询模型
        return newModel(clazz, this);
    }

    public <D extends Model> D relation(Class<?> clazz, RelationType type, String localKey, String foreignKey) {
        String fieldName = Thread.currentThread().getStackTrace()[1].getMethodName();
        return relation(fieldName, clazz, type, localKey, foreignKey);
    }

    public <D extends Model> D relation(String clazzName, RelationType type, String localKey, String foreignKey) {
        try {
            Class<?> clazz = getClass().getClassLoader().loadClass(clazzName);
            String fieldName = Thread.currentThread().getStackTrace()[2].getMethodName();
            return relation(fieldName, clazz, type, localKey, foreignKey);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public <D extends Model> D hasOne(Class<?> clazz, String localKey, String foreignKey) {
        String fieldName = Thread.currentThread().getStackTrace()[2].getMethodName();
        return relation(fieldName, clazz, RelationType.OneToOne, localKey, foreignKey);
    }

    public <D extends Model> D hasMany(Class<?> clazz, String localKey, String foreignKey) {
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
        this.__withs = Arrays.asList(fields.split(","));
        ComUtil.trimList(this.__withs);
        return this;
    }

    //---------- TODO::模型初始化 ----------//
    private static<D> D newModel(Class<?> clazz) {
        try {
            if (isModel(clazz)) {
                D model = (D) clazz.newInstance();
                ModelUtil.setFieldValue(model, "__table", parseModelTableName(clazz));
                return model;
            }
            throw new RuntimeException("该类不能实例化为模型");
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private static<D> D newModel(Class<?> clazz, Model parent) {
        D model = newModel(clazz);
        ModelUtil.setFieldValue(model, "__parent", parent);   // 设置model上的属性要再获取父类
        return model;
    }

    public static boolean isModel(Class<?> clazz) {
        return Model.class.isAssignableFrom(clazz);
    }

    public static boolean isCollection(Class<?> clazz) {
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
            org.quickjava.orm.contain.ModelField fieldInfo = new org.quickjava.orm.contain.ModelField(field);
            String fieldName = fieldInfo.getName();
            // 兼容mybatis-plus字段说明
            TableField tableField = field.getAnnotation(TableField.class);
            // 模型字段说明
            ModelField modelField = field.getAnnotation(ModelField.class);
            if (modelField != null) {
                if (!"".equals(modelField.name())) {
                    fieldInfo.setName(modelField.name());
                }
                fieldInfo.setInsertFill(modelField.insertFill());
                fieldInfo.setUpdateFill(modelField.updateFill());
                fieldInfo.setSoftDelete(modelField.softDelete());
            }

            fieldInfo.setWay(findRelationAno(field));

            // 有关联方法
            if (methodMap.containsKey(fieldName)) {
                try {
                    Method method = methodMap.get(fieldName);
                    method.invoke(model);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
                fieldInfo.setWay(meta.relationMap().get(fieldName));
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
    }

    public static Object findRelationAno(Field field)
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
        return ModelUtil.toUnderlineCase(tableName);
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
     * 加载属性关联getter
     * */
    private static Object LoadingRelationGetter(Class<?> clazz, Object o, Method method, Object ...objects)
    {
        try {
            // 加载
            org.quickjava.orm.contain.ModelField modelField = ModelUtil.getMeta(clazz).fieldMap().get(method.getName());
            Field field = modelField.getField();
            if (Model.class.isAssignableFrom(o.getClass())) {
                Model curr = (Model) o;
                String fieldName = field.getName();
                Class<?> fieldType = field.getType();
                if (curr.data().containsKey(fieldName)) {
                    return curr.data().get(fieldName);
                }

                // 存在递归和toString调用
                StackTraceElement[] stackTraceArr = Thread.currentThread().getStackTrace();
                for (int i = 0; i < stackTraceArr.length; i++) {
                    StackTraceElement ele = stackTraceArr[i];
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
                        curr.data(fieldName, fieldValue);
                        return fieldValue;
                    }
                }
            }
            return null;

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private Class<?> getMClass() {
        return Enhancer.isEnhanced(getClass()) ? getClass().getSuperclass() : getClass();
    }

    private static Class<?> getModelClass(Class<?> clazz) {
        return ModelUtil.getModelClass(clazz);
    }

    private static Class<?> getModelClass(Object obj) {
        return ModelUtil.getModelClass(obj);
    }

    @Override
    public String toString() {
        Map<String, Object> dataMap = new LinkedHashMap<>(data());
        __meta.relationMap().forEach((name, relation) -> dataMap.put(name, ReflectUtil.getFieldValue(this, name)));
        return getMClass().getSimpleName() + dataMap;
    }
}
