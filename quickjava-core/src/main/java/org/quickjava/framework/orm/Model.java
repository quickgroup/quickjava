package org.quickjava.framework.orm;

import cn.hutool.core.util.ArrayUtil;
import com.baomidou.mybatisplus.annotation.TableName;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.quickjava.framework.orm.annotation.ModelName;
import org.quickjava.framework.orm.annotation.OneToMany;
import org.quickjava.framework.orm.annotation.OneToOne;
import org.quickjava.framework.orm.contain.*;
import org.quickjava.framework.orm.enums.RelationType;
import org.quickjava.framework.orm.utils.Helper;
import org.quickjava.framework.orm.utils.ModelUtil;
import org.quickjava.framework.orm.utils.QuerySetHelper;
import org.quickjava.framework.orm.utils.SqlUtil;

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
public class Model extends Helper {

    /**
     * 模型元信息
     * */
    private ModelMeta __meta;

    /**
     * 关联的父模型对象
     * */
    private Model __parent;

    /**
     * 预载入属性
     * */
    private List<String> __withs;

    /**
     * 数据
     * */
    private DataMap __data = new DataMap();

    /**
     * 修改的字段
     * */
    private List<String> __modifiedFields;

    public Model() {
        initModel(this, getClass());
    }

    private QuerySet __querySet = null;

    public synchronized QuerySet query() {
        synchronized (Model.class) {
            if (__querySet == null) {
                __querySet = QuerySet.table(getModelTableName(getClass()));
            }
        }
        return __querySet;
    }

    public Model where(String field, String opr, Object val) {
        query().where(whereFieldName(field), opr, val);
        return this;
    }

    public Model where(String field, Object val) {
        where(field, "EQ", val);
        return this;
    }

    public Model where(Map<String, Object> query) {
        QuerySetHelper.loadQuery(query(), query);
        return this;
    }

    public Model eq(String field, Object val) {
        return where(field, val);
    }

    public Model neq(String field, Object val) {
        return where(field, "NEQ", val);
    }

    public Model gt(String field, Object val) {
        return where(field, "GT", val);
    }

    public Model gte(String field, Object val) {
        return where(field, "GTE", val);
    }

    public Model lt(String field, Object val) {
        return where(field, "LT", val);
    }

    public Model lte(String field, Object val) {
        return where(field, "LTE", val);
    }

    /**
     * 补全字段名称
     * - 预载入补全表名
     * */
    private String whereFieldName(String field) {
        field = ModelUtil.fieldLineName(field);
        if (__withs != null) {
            if (!field.contains(".")) {
                field = __meta.table() + "." + field;
            }
        }
        return field;
    }

    /**
     * 判断当前类是素模型（不是代理模型
     * - 不是：在 insert、update 时需要收集字段数据
     * - 收集字段数据
     * */
    private void loadingVegetarianModel()
    {
        if (ModelUtil.isVegetarianModel(this)) {
            // 收集字段数据
            __meta.getFieldMap().forEach((name, field) -> {
                data(field.getName(), ModelUtil.getFieldValue(this, field.getField()));
            });
        }
    }

    /**
     * 新增
     * */
    public Model insert()
    {
        Long pkVal = query().insert(this.sqlData());
        data(pk(), pkVal);
        return ModelUtil.isProxyModel(this) ? this : newProxyModel(getMClass(), data());
    }

    /**
     * 使用对象新增
     * */
    public Model insert(DataMap data) {
        data(data);
        return insert();
    }

    /**
     * 更新
     * */
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
        return ModelUtil.fieldName(query().getColumnPk());
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
     * */
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
     * */
    public Model order(String field, String asc) {
        query().order(ModelUtil.fieldName(field), asc);
        return this;
    }

    public Model order(String field, boolean asc) {
        order(ModelUtil.fieldName(field), asc ? "ASC" : "DESC");
        return this;
    }

    public Model order(String fields)
    {
        if (fields.contains(",")) {
            return order(Arrays.asList(fields.split(",")));
        }
        String[] arr = fields.split(" ");
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
     * */
    public Model page(Integer page) {
        query().page(page);
        return this;
    }

    public Model page(Integer page, Integer size) {
        query().page(page, size);
        return this;
    }

    //---------- TODO::数据查询方法 ----------//
    /**
     * 查询一条
     * */
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
        if (__withs == null || __withs.size() == 0) {
            return;
        }

        // 本表字段声明
        List<String> fields = new LinkedList<>();
        __meta.getFieldMap().forEach((name, field) -> {
            if (field.getWay() != null || Model.class.isAssignableFrom(field.getClazz())) {
                return;
            }
            name = ModelUtil.fieldLineName(name);
            fields.add(__meta.table() + "." + name + " AS " + __meta.table() + "__" + name);
        });

        // 关联表字段声明
        getWithRelation(new RelationType[]{RelationType.OneToOne}).forEach((relationName, relation) -> {
            ModelMeta meta = ModelUtil.getMeta(relation.getClazz());
            meta.getFieldMap().forEach((name, field) -> {
                if (field.getWay() != null || Model.class.isAssignableFrom(field.getClazz())) {
                    return;
                }
                name = ModelUtil.fieldLineName(name);
                fields.add(relationName + "." + name + " AS " + relationName + "__" + name);
            });
            // join
            query().join(meta.table() + " " + relationName,
                    String.format("%s.%s = %s.%s", relationName, ModelUtil.fieldLineName(relation.foreignKey()),
                    __meta.table(), ModelUtil.fieldLineName(relation.localKey())), "LEFT");
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
            // 装载关联属性
            Map<String, Relation> relationMap = getWithRelation(new RelationType[]{RelationType.OneToOne});
            if (relationMap.size() > 0) {
                // 主表数据
                D model = newProxyModel(clazz);
                resultTranshipmentWith(model, data);
                // 关联表数据
                relationMap.forEach((relationName, relation) -> {
                    Model relationModel = newProxyModel(relation.getClazz());
                    resultTranshipmentWith(relationModel, data);
                    SqlUtil.setFieldValue(model, relationName, relationModel);
                });
                models.add(model);
            } else {
                models.add(newProxyModel(getClass(), data));
            }
        });
        return models;
    }

    private void resultTranshipmentWith(Model model, Map<String, Object> set) {
        model.__meta.getFieldMap().forEach((name, field) -> {
            if (field.getWay() != null || Model.class.isAssignableFrom(field.getClazz())) {
                return;
            }
            String dataName = model.__meta.table() + "__" + ModelUtil.fieldLineName(name);
            model.data(name, set.get(dataName));
        });
    }

    /**
     * 查询后模型处理
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
                models.forEach(model -> conditionMap.get(fieldName).add(SqlUtil.getFieldValue(model, relation.localKey())));
            });
            // 查询
            System.out.println("conditionMap=" + conditionMap);
            relationMap.forEach((fieldName, relation) -> {
                if (conditionMap.get(fieldName).size() == 0) {
                    return;
                }
                Model queryModel = newModel(relation.getClazz());
                List<Model> rows = queryModel.where(relation.foreignKey(), "IN", conditionMap.get(fieldName)).select();
                System.out.println("rows=" + rows);
                // 数据装填
                models.forEach(model -> {
                    Object modelKeyVal = SqlUtil.getFieldValue(model, relation.localKey());
                    List<Model> set = rows.stream().filter(row -> modelKeyVal.equals(SqlUtil.getFieldValue(row, relation.foreignKey())))
                            .collect(Collectors.toList());
                    SqlUtil.setFieldValue(model, fieldName, set);
                });
            });
        }
    }

    /**
     * 获取预载入的属性名对应模型类
     * */
    private Map<String, Relation> getWithRelation(RelationType[] types) {
        Map<String, Relation> relationMap = new LinkedHashMap<>();
        if (__withs != null) {
            __withs.forEach(name -> {
                Relation relation = __meta.getRelationMap().get(name);
                if (relation != null && ArrayUtil.contains(types, relation.getType())) {
                    relationMap.put(name, __meta.getRelationMap().get(name));
                }
            });
        }
        return relationMap;
    }

    //---------- TODO::分页方法 ----------//
    public Pagination<?> pagination(Integer page, Integer pageSize) {
        return query().pagination(getMClass(), page, pageSize);
    }

    public Pagination<?> pagination() {
        return query().pagination(getMClass(), 1, 20);
    }

    // 提取某字段为list
    public <D> D selectFieldList(String field) {
        String fieldLine = ModelUtil.fieldLineName(field);
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
    // 实体通过map装载数据
    public Model data(Map<String, Object> data) {
        data.forEach(this::data);
        return this;
    }

    /**
    * 获取data中的数据
    * */
    public Object data(String field)
    {
        this.loadingVegetarianModel();
        return __data.get(field);
    }

    /**
     * 通过map装载实体数据
     * */
    public Model data(String name, Object val) {
        name = ModelUtil.fieldName(name);
        ModelField field = __meta.getFieldMap().get(name);
        if (field != null && field.getWay() == null) {
            __data.put(name, val);      // 只保存本类字段数据，关联数据不缓存
        }
        ModelUtil.setFieldValue(this, name, val);
        return this;
    }

    /**
     * 获取data全部数据
     * */
    public DataMap data() {
        this.loadingVegetarianModel();
        return __data;
    }

    public static Map<String, Object> dataFieldConvLine(Map<String, Object> data, Class<?> clazz) {
        Map<String, ModelField> fieldMap = ModelUtil.getMeta(clazz).getFieldMap();
        Map<String, Object> ret = new LinkedHashMap<>();
        data.forEach((k, v) -> {
            if (fieldMap.containsKey(ModelUtil.fieldName(k))) {
                ret.put(ModelUtil.fieldLineName(k), v);
            }
        });
        return ret;
    }

    /**
     * 执行sql的数据
     * */
    public DataMap sqlData()
    {
        this.loadingVegetarianModel();
        DataMap data = DataMap.one();
        this.data().forEach((k, v) -> {
            if (v instanceof Boolean) {
                data.put(ModelUtil.fieldLineName(k), Boolean.TRUE.equals(v) ? 1 : 0);
            } else if (v instanceof Model) {
                return; // 关联模型数据不能一起写入
            } else {
                data.put(ModelUtil.fieldLineName(k), v);
            }
        });
        return data;
    }

    //---------- TODO::静态操作方法 ----------//
    /**
     * 通过 Map 创建对象
     * */
    public Model create(Map<String, Object> data)
    {
        Model model = newProxyModel(getMClass(), data);
        model.insert();
        return model;
    }

    /**
     * 通过 DataMap 创建对象
     * */
    public Model create(DataMap data) {
        return create((Map<String, Object>) data);
    }

    public Model create(Model model) {
        return model.insert();
    }

    /**
     * 批量创建
     * */
    public Integer bulkCreate(List<DataMap> dataList)
    {
        List<DataMap> dataList2 = new LinkedList<>();
        dataList.forEach(map -> {
            DataMap data = new DataMap();
            map.forEach((k, v) -> data.put(ModelUtil.fieldLineName(k), v));
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
            ModelUtil.setFieldValue(model, "__parent", parent);
        }
        return model;
    }

    public static MethodInterceptor modelProxyMethodInterceptor = new MethodInterceptor () {
        @Override
        public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
            /*
             * 1. 缓存全部需要代理的关联属性
             * 2. 判断当前方法是否是关联属性的查询方法、获取方法
             * 3. 如果是就对返回数据处理
             * */
            Class<?> clazz = getModelClass(o.getClass());
            if (ModelUtil.getMeta(clazz).getRelationMap().containsKey(method.getName())) {       // 存在关联属性的方法
                return LoadingRelationGetter(clazz, o, method, objects);
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
        if (!__meta.getRelationMap().containsKey(fieldName)) {
            __meta.getRelationMap().put(fieldName, new Relation(clazz, type, localKey, foreignKey));
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
     * */
    public Model with(String fields) {
        this.__withs = Arrays.asList(fields.split(","));
        return this;
    }

    //---------- TODO::模型初始化 ----------//
    private static<D> D newModel(Class<?> clazz) {
        try {
            if (isModel(clazz)) {
                D model = (D) clazz.newInstance();
                ModelUtil.setFieldValue(model, "__table", getModelTableName(clazz));
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
        meta.setTable(getModelTableName(clazz));
        meta.setClazz(clazz);
        meta.setFieldMap(new LinkedHashMap<>());
        ModelUtil.setMeta(clazz, meta);

        // 全部方法
        Map<String, Method> methodMap = new LinkedHashMap<>();
        for (Method method : clazz.getDeclaredMethods()) {
            methodMap.put(method.getName(), method);
        }
        // 全部属性
        for (Field field : clazz.getDeclaredFields()) {
            // 排除静态字段
            if (Modifier.isStatic(field.getModifiers())) {
                continue;
            }
            ModelField fieldInfo = new ModelField(field);
            String fieldName = fieldInfo.getName();
            //
            org.quickjava.framework.orm.annotation.ModelField modelField = field.getAnnotation(org.quickjava.framework.orm.annotation.ModelField.class);
            if (modelField != null) {
                if (!modelField.exist()) {
                    continue;
                }
                if (!"".equals(modelField.name())) {
                    fieldInfo.setName(modelField.name());
                }
                fieldInfo.setFill(modelField.fill());
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
                fieldInfo.setWay(meta.getRelationMap().get(fieldName));
            }

            meta.getFieldMap().put(field.getName(), fieldInfo);
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

    public static String getModelTableName(Class<?> clazz)
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
        return ModelUtil.fieldLineName(tableName);
    }

    public static Object LoadingRelationGetter(Class<?> clazz, Object o, Method method, Object ...objects)
    {
        try {
            // 加载
            ModelField modelField = ModelUtil.getMeta(clazz).getFieldMap().get(method.getName());
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
                        Object localValue = ModelUtil.getFieldValue(curr.getMClass(), curr, relation.localKey());
                        Object fieldValue = relationModel.where(relation.foreignKey(), localValue).find();  // 查询结果
                        curr.data(fieldName, fieldValue);
                        return fieldValue;
                    } else if (relation.getType() == RelationType.OneToMany) {
                        if (field.getGenericType() instanceof ParameterizedType) {
                            ParameterizedType pt = (ParameterizedType) field.getGenericType();
                            Class<?> genericClazz = (Class<?>) pt.getActualTypeArguments()[0];
                            Model relationModel = newModel(genericClazz, curr);
                            Object localValue = ModelUtil.getFieldValue(curr.getMClass(), curr, relation.localKey());
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
                    Object localValue = ModelUtil.getFieldValue(curr.getMClass(), curr, one.localKey());
                    Object fieldValue = relationModel.where(one.foreignKey(), localValue).find();  // 查询结果
                    curr.data(fieldName, fieldValue);
                    return fieldValue;
                } else if (isCollection(fieldType) && OneToMany.class.isAssignableFrom(modelField.getWay().getClass())) {
                    OneToMany many = (OneToMany) modelField.getWay();
                    if (field.getGenericType() instanceof ParameterizedType) {
                        ParameterizedType pt = (ParameterizedType) field.getGenericType();
                        Class<?> genericClazz = (Class<?>) pt.getActualTypeArguments()[0];
                        Model relationModel = newModel(genericClazz, curr);
                        Object localValue = ModelUtil.getFieldValue(curr.getMClass(), curr, many.localKey());
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

    // 直接拷贝属性
    public static void copyProperties(Object src, Object dst)
    {
        try {
            Class<?> srcClass = getModelClass(src);
            Class<?> dstClass = getModelClass(dst);
            if (Map.class.isAssignableFrom(dstClass)) {
                throw new RuntimeException("暂不支持 dst 为Map的拷贝");
            }
            // map=>实体
            if (Map.class.isAssignableFrom(srcClass)) {
                Map<String, Object> data = (Map<String, Object>) src;
                for (Field field : dstClass.getDeclaredFields()) {
                    if (data.containsKey(field.getName())) {
                        field.setAccessible(true);
                        field.set(dst, data.get(field.getName()));
                        continue;
                    }
                    String fieldName = ModelUtil.fieldName(field.getName());
                    if (data.containsKey(fieldName)) {
                        field.setAccessible(true);
                        field.set(dst, data.get(fieldName));
                        continue;
                    }
                    fieldName = ModelUtil.fieldLineName(field.getName());
                    if (data.containsKey(fieldName)) {
                        field.setAccessible(true);
                        field.set(dst, data.get(fieldName));
                        continue;
                    }
                }
                return;
            }
            // 实体=>实体
            Map<String, Object> srcMap = new LinkedHashMap<>();
            for (Field field : srcClass.getDeclaredFields()) {
                field.setAccessible(true);
                srcMap.put(field.getName(), field.get(src));
            }
            for (Field field : dstClass.getDeclaredFields()) {
                if (srcMap.containsKey(field.getName())) {
                    field.setAccessible(true);
                    field.set(dst, srcMap.get(field.getName()));
                }
            }
        } catch (IllegalAccessException ignored) {
        }
    }

    public Class<?> getMClass() {
        return Enhancer.isEnhanced(getClass()) ? getClass().getSuperclass() : getClass();
    }

    public static Class<?> getModelClass(Class<?> clazz) {
        return ModelUtil.getModelClass(clazz);
    }

    public static Class<?> getModelClass(Object obj) {
        return ModelUtil.getModelClass(obj);
    }

    @Override
    public String toString() {
        return getMClass().getSimpleName() + data().toString();
    }
}
