package org.quickjava.orm.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIgnoreType;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.quickjava.common.utils.ComUtil;
import org.quickjava.common.utils.DatetimeUtil;
import org.quickjava.common.utils.ReflectUtil;
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
import org.quickjava.orm.enums.CompareEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
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
 * */
@JsonIgnoreType
@JsonIgnoreProperties(value = {"reservoir", "logger"}, ignoreUnknown = true)
public class Model implements IModel {

    @JsonIgnore
    @TableField(exist = false)
    private final ModelReservoir reservoir = new ModelReservoir(this);

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
            if (reservoir.querySet == null) {
                reservoir.querySet = QuerySet.table(parseModelTableName(getClass()));
                QueryReservoir queryReservoir = ReflectUtil.getFieldValue(reservoir.querySet, "reservoir");
                queryReservoir.setCallback(WhereOptCallback.class, ModelUtil.whereOptCallback, this);
                queryReservoir.setCallback(OrderByOptCallback.class, ModelUtil.orderByOptCallback, this);
            }
        }
        return reservoir.querySet;
    }

    private QueryReservoir querySetReservoir() {
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
        query.forEach((name, val) -> queryRet.put(ModelUtil.fieldToUnderlineCase(name), val));
        // 调用querySet加载条件
        QuerySetHelper.loadQuery(query(), queryRet);
        return this;
    }

    public Model where(String sql) {
        query().where(sql, Operator.RAW, null);
        return this;
    }

    public Model where(WhereClosure callback)
    {
        query().where(callback);
        return this;
    }

    public Model whereOr(WhereClosure callback)
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
        reservoir.meta.fieldMap().forEach((name, field) -> {
            if (!reservoir.data.containsKey(name)) {
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
        if (querySetReservoir().isFetchSql())
            return toD(new ModelSql(querySetReservoir().getSql()));
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
        for (ModelFieldMeta field : reservoir.meta.fieldMap().values()) {
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
         reservoir.meta.fieldMap().forEach((name, field) -> {
            if (!reservoir.data.containsKey(name)) {
                if (field.isUpdateFill()) {
                    data(name, ModelUtil.fill(field.getModelField().updateFill(), field.getModelField().updateFillTarget()));
                }
            }
        });
        // 执行
        query().update(this.sqlData());
        // 编译sql
        if (querySetReservoir().isFetchSql())
            return toD(new ModelSql(querySetReservoir().getSql()));
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
//        reservoir.data.remove(pk);  // 不去更新主键
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
        List<Map<String, Object>> dataList = query().limit(0L, 1L).select();
        // 编译sql
        if (querySetReservoir().fetchSql) {
            return toD(new ModelSql(query().buildSql()));
        }
        // 结果为空
        if (ModelUtil.isEmpty(dataList)) {
            return null;
        }
        // 装载数据
        List<IModel> models = ModelUtil.resultTranshipment(this, getClass(), dataList);
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
        if (querySetReservoir().fetchSql) {
            return toD(new ModelListSql(querySetReservoir().sql));
        }
        // 装载
        List<IModel> models = ModelUtil.resultTranshipment(this, getClass(), dataList);
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
        return toD(find());
    }

    //TODO::---------- 分页方法 ----------
    public <D> Pagination<D> pagination(Long page, Long pageSize) {
        // 查询前处理：预载入
        queryBefore();
        // 执行查询
        Pagination<Map<String, Object>> pagination = query().pagination(page, pageSize);
        // 数据组装
        Pagination<IModel> pagination1 = new Pagination<>(pagination);
        pagination1.rows = ModelUtil.resultTranshipment(this, getMClass(), pagination.rows);
        return toD(pagination1);
    }

    public <D> Pagination<D> pagination() {
        return pagination(1L, 20L);
    }

    //TODO::---------- 操作方法：排序、聚合等 START ----------
    /**
     * 排序
     * @param field 字段
     * @param type 排序方式
     * @return 模型对象
     */
    public Model order(String field, OrderByType type) {
        query().order(ModelUtil.fieldToUnderlineCase(field), type);
        return this;
    }

    public Model order(String field, boolean desc) {
        query().order(field, desc);
        return this;
    }

    public Model order(String fields)
    {
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
     * @param page 页数
     * @return 模型对象
     */
    public Model page(Long page) {
        query().page(page);
        return this;
    }

    /**
     * 分页
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
        ModelFieldMeta field =  reservoir.meta.fieldMap().get(name);
        // 非本表属性或关联属性不设置
        if (field == null || field.getRelationWay() != null) {
            return this;
        }

        reservoir.data.put(name, val);
        ReflectUtil.setFieldValue(this, name, val);

        // 被修改的字段
        reservoir.getModified().add(field);
        return this;
    }

    /**
     * 获取data全部数据
     * @return 模型数据
     */
    public DataMap data() {
        this.loadingVegetarianModel();
        return reservoir.data;
    }

    public String pk() {
        return  reservoir.meta.getPkName();
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
        if (reservoir.modified != null) {
            reservoir.modified.forEach(field -> {
                if (field.getRelationWay() != null) {
                    return;
                }
                Object v = data.get(field.getName());
                ret.put(ModelUtil.fieldToUnderlineCase(field.getName()), ModelUtil.valueToSqlValue(v));
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
        for (ModelFieldMeta field : reservoir.meta.fieldMap().values()) {
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
        System.out.println("relationMap=" + relationMap);
        if (relationMap.isEmpty()) {
            return;
        }

        // T1::字段声明
        List<String> fields = new LinkedList<>();
        // 本表字段声明
        ModelUtil.loadModelAccurateFields(query(), reservoir.meta, reservoir.meta.table());

        // 关联表
        relationMap.forEach((relationName, relation) -> {
            ModelMeta meta = ModelUtil.getMeta(relation.getClazz());
            // 关联表字段
            ModelUtil.loadModelAccurateFields(query(), meta, relationName);
            // 关联方式声明
            String conditionSql = ModelUtil.joinConditionSql(
                    relationName, relation.foreignKey(),
                    CompareEnum.EQ,
                    reservoir.meta.table(), relation.localKey()
            );
            query().join(meta.table() + " " + relationName, conditionSql, JoinType.LEFT);
        });
        // 装填字段
        query().field(fields);
    }

    /**
     * 获取预载入的属性名对应模型类
     * @param types 关联类型
     * @return 关联属性集合
     */
    private Map<String, Relation> getWithRelation(RelationType[] types) {
        Map<String, Relation> relationMap = new LinkedHashMap<>();
        if (reservoir.withs != null) {
            reservoir.withs.forEach(name -> {
                Relation relation =  reservoir.meta.relationMap().get(name);
                if (relation != null && SqlUtil.inArray(types, relation.getType())) {
                    relationMap.put(name,  reservoir.meta.relationMap().get(name));
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

    public static<D extends IModel> D newModel(Class<?> clazz, Map<String, Object> data, IModel parent)
    {
        // 创建代理类信息
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(getModelClass(clazz));
        enhancer.setCallback(modelProxyMethodInterceptor);
        D model = toD(enhancer.create());
        ModelReservoir reservoir = ReflectUtil.getFieldValue(model, "reservoir");
        //
//        ModelUtil.setFieldValue(model, "__table", parseModelTableName(clazz));
        // 加载数据
        if (!ModelUtil.isEmpty(data)) {
            ((Model) model).data((DataMap) data);
        }
        // 设置父类
        if (!ModelUtil.isEmpty(parent)) {
            reservoir.parent = parent;
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
        if (ModelUtil.isVegetarianModel(this) && reservoir.vegetarian) {
            // 收集字段数据
             reservoir.meta.fieldMap().forEach((name, field) -> {
                Object val = ReflectUtil.getFieldValue(this, field.getField());
                data(field.getName(), val);
            });
        }
    }

    //---------- TODO::关联方法 ----------//
    /*
     * 关联方法
     * */
    public <D extends IModel> D relation(String fieldName, Class<?> clazz, RelationType type, String localKey, String foreignKey) {
        // 缓存关联关系
        if (! reservoir.meta.relationMap().containsKey(fieldName)) {
            reservoir.meta.relationMap().put(fieldName, new Relation(clazz, type, localKey, foreignKey));
        }
        // 返回查询模型
        return toD(newModel(clazz, this));
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
        List<String> withs = Arrays.asList(fields.split(","));
        ComUtil.trimList(withs);
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
    private static void initModel(Model model, Class<?> clazz)
    {
        if (!Model.class.isAssignableFrom(clazz)) {
            return;
        }
        if (Enhancer.isEnhanced(clazz)) {
            clazz = clazz.getSuperclass();
        }

        // 已加载模型元组信息
        if (ModelUtil.metaExist(clazz)) {
            model.reservoir.meta = ModelUtil.getMeta(clazz);
            return;
        }

        // 初始化模型信息
        ModelMeta meta = model.reservoir.meta = new ModelMeta();
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
            ModelFieldMeta fieldMeta = new ModelFieldMeta(field);
            // 隐藏字段
            if (!fieldMeta.isExist()) {
                continue;
            }

            // 关联属性的关联注解
            fieldMeta.setRelationWay(findRelationAno(field));

            // 有关联方法
            Method method = methodMap.get(fieldMeta.getName());
            if (method != null && Model.class.isAssignableFrom(method.getReturnType())) {
                try {
                    method.invoke(model);
                    fieldMeta.setRelationWay(meta.relationMap().get(fieldMeta.getName()));
                } catch (IllegalAccessException | InvocationTargetException ignore) {
                }
            }

            meta.fieldMap().put(field.getName(), fieldMeta);
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
            tableName = ModelUtil.getModelAlias(clazz);
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
            ModelFieldMeta modelField = ModelUtil.getMeta(clazz).fieldMap().get(method.getName());
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
                else if (isModel(fieldType) && OneToOne.class.isAssignableFrom(modelField.getRelationWay().getClass())) {
                    OneToOne one = (OneToOne) modelField.getRelationWay();
                    Model relationModel = newModel(fieldType, curr);
                    // 设置全部关联属性为空，避免多重查询
                    Object localValue = ReflectUtil.getFieldValue(curr.getMClass(), curr, one.localKey());
                    Object fieldValue = relationModel.where(one.foreignKey(), localValue).find();  // 查询结果
                    curr.data(fieldName, fieldValue);
                    return fieldValue;
                } else if (isCollection(fieldType) && OneToMany.class.isAssignableFrom(modelField.getRelationWay().getClass())) {
                    OneToMany many = (OneToMany) modelField.getRelationWay();
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
            logger.error("getter数据查询异常：{}", e.getMessage(), e);
            throw new RuntimeException("getter数据查询异常：" + e);
        }
    }

    private Class<?> getMClass() {
        return Enhancer.isEnhanced(getClass()) ? getClass().getSuperclass() : getClass();
    }

    private static Class<?> getModelClass(Class<?> clazz) {
        return ModelUtil.getModelClass(clazz);
    }

    private static TableColumn fieldAlias(String table, String field) {
        return ModelUtil.fieldAlias(table, field);
    }

    private static String toUnderlineCase(String name) {
        return ModelUtil.toUnderlineCase(name);
    }

    @Override
    public String toString() {
        Map<String, Object> dataMap = new LinkedHashMap<>(data());
         reservoir.meta.relationMap().forEach((name, relation) -> dataMap.put(name, ReflectUtil.getFieldValue(this, name)));
        return getMClass().getSimpleName() + dataMap;
    }
}
