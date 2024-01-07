package org.quickjava.orm.model;

import net.sf.cglib.proxy.Enhancer;
import org.quickjava.common.utils.DatetimeUtil;
import org.quickjava.common.utils.ReflectUtil;
import org.quickjava.orm.model.contain.ModelMeta;
import org.quickjava.orm.model.contain.Relation;
import org.quickjava.orm.model.enums.ModelFieldFill;
import org.quickjava.orm.model.enums.RelationType;
import org.quickjava.orm.query.build.Where;
import org.quickjava.orm.query.callback.OrderByOptCallback;
import org.quickjava.orm.query.callback.WhereOptCallback;
import org.quickjava.orm.query.enums.Operator;
import org.quickjava.orm.utils.SqlUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/*
 * Copyright (c) 2020~2023 http://www.quickjava.org All rights reserved.
 * +-------------------------------------------------------------------
 * Organization: QuickJava
 * +-------------------------------------------------------------------
 * Author: Qlo1062
 * +-------------------------------------------------------------------
 * File: ModelUtil
 * +-------------------------------------------------------------------
 * Date: 2023-3-10 17:15
 * +-------------------------------------------------------------------
 * License: Apache Licence 2.0
 * +-------------------------------------------------------------------
 */
public class ModelUtil extends SqlUtil {

    private static final Logger logger = LoggerFactory.getLogger(ModelUtil.class);

    public static final Map<Class<?>, ModelMeta> modelCache = new LinkedHashMap<>();

    public static ModelMeta getMeta(Class<?> clazz) {
        return modelCache.get(clazz);
    }

    public static boolean metaExist(Class<?> clazz) {
        return modelCache.containsKey(clazz);
    }

    public static void setMeta(Class<?> clazz, ModelMeta meta) {
        modelCache.put(clazz, meta);
    }

    public static boolean isProxyModel(Class<?> clazz) {
        return Enhancer.isEnhanced(clazz);
    }

    public static boolean isProxyModel(Object obj) {
        return Enhancer.isEnhanced(obj.getClass());
    }

    public static boolean isVegetarianModel(Class<?> clazz) {
        return !isProxyModel(clazz);
    }

    public static boolean isVegetarianModel(Object obj) {
        return !isProxyModel(obj.getClass());
    }

    public static Class<? extends Model> getModelClass(Class<?> clazz) {
        if (!Enhancer.isEnhanced(clazz)) {
            return Model.class.isAssignableFrom(clazz) ? (Class<? extends Model>) clazz : null;
        }
        return getModelClass(clazz.getSuperclass());
    }

    public static Class<?> getModelClass(Object obj) {
        if (obj instanceof Class) {
            return getModelClass((Class<?>) obj);
        }
        return getModelClass(obj.getClass());
    }

    public static String getModelAlias(Class<?> clazz) {
        String name = clazz.getSimpleName();
        if (name.endsWith("Model")) {
            name = name.substring(0, name.lastIndexOf("Model"));
        }
        return name;
    }

    public static void setFieldValue(Object o, String field, Object value) {
        setFieldValue(o.getClass(), o, field, value);
    }

    private static void setFieldValue(Class<?> clazz, Object o, String field, Object value) {
        try {
            setFieldValue(o, clazz.getDeclaredField(field), value);
        } catch (NoSuchFieldException e) {
            if (clazz.getSuperclass() != null) {    // 向上找父类的属性
                setFieldValue(clazz.getSuperclass(), o, field, value);
            }
        }
    }

    public static void setFieldValue(Object o, Field field, Object value)
    {
        try {
            field.setAccessible(true);
            // 实现对关联属性的数据赋值
            if (Model.class.isAssignableFrom(field.getType())) {
                if (value instanceof Map) {
                    Model child = ReflectUtil.invoke(Model.class, "newProxyModel", (Map<String, Object>) value, (Model) o);
                    field.set(o, child);
                }
            } else {
                ReflectUtil.setFieldValue(o, field, value);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 直接拷贝属性
     * @param src 来源对象
     * @param dst 目标对象
     */
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
                    String fieldName = ModelUtil.toCamelCase(field.getName());
                    if (data.containsKey(fieldName)) {
                        field.setAccessible(true);
                        field.set(dst, data.get(fieldName));
                        continue;
                    }
                    fieldName = ModelUtil.toUnderlineCase(field.getName());
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

    /**
     * Java程序数据转sql兼容数据，如：true=1、false=0
     * @param val 值
     * @return 转换后的值
     */
    public static Object valueToSqlValue(Object val)
    {
        if (val instanceof Boolean) {
            return Boolean.TRUE.equals(val) ? 1 : 0;
        }
        return val;
    }

    /**
     * 对象比较
     * @param obj1 对象1
     * @param obj2 对象2
     * @return 比较结果
     */
    public static boolean objectEquals(Object obj1, Object obj2)
    {
        if (obj1 == null && obj2 == null) {
            return true;
        } else if (obj1 != null) {
            return obj1.equals(obj2);
        } else if (obj2 != null) {
            return obj2.equals(obj1);
        } else {
            return false;
        }
    }

    /**
     * 获取方法引用传递的方法名称
     * @param getter getter
     * @return 对应属性名称
     */
    public static String getSupplierConvFieldName(Supplier<?> getter)
    {
        Method method = getter.getClass().getDeclaredMethods()[0];
        String fieldName = method.getName();
        if (fieldName.startsWith("get") || fieldName.startsWith("set")) {
            fieldName = fieldName.substring(2);
        }
        return toUnderlineCase(fieldName);
    }

    // 填充数据
    public static Object fill(ModelFieldFill fill, String staticMethodStr) {
        if (fill == ModelFieldFill.DATETIME) {
            return new Date();
        } else if (fill == ModelFieldFill.DATE) {
            return DatetimeUtil.format(new Date(), DatetimeUtil.FORMAT_DATE);
        } else if (fill == ModelFieldFill.TIME) {
            return DatetimeUtil.format(new Date(), DatetimeUtil.FORMAT_TIME);
        } else if (fill == ModelFieldFill.TIMESTAMP) {
            return System.currentTimeMillis() / 1000;
        } else if (fill == ModelFieldFill.MILL_TIMESTAMP) {
            return System.currentTimeMillis();
        } else if (fill == ModelFieldFill.STATIC_METHOD) {
            return ReflectUtil.invoke(staticMethodStr);
        }
        return null;
    }

    public static String joinConditionSql(String left, String leftField, String conditionType,
                                          String right, String rightField) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(left).append(".").append(toUnderlineCase(leftField));
        stringBuilder.append(" ").append(Where.OpMap.get(conditionType)).append(" ");
        stringBuilder.append(right).append(".").append(toUnderlineCase(rightField));
        return stringBuilder.toString();
    }

    public static String joinConditionSql(String left, String leftField, String conditionType,
                                          String rightValue) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(left).append(".").append(toUnderlineCase(leftField));
        stringBuilder.append(" ").append(Where.OpMap.get(conditionType)).append(" ");
        stringBuilder.append(rightValue);
        return stringBuilder.toString();
    }

    // 回调处理方法
    public static WhereOptCallback whereOptCallback = (where, querySet, userData) -> {
        // 子查询条件不处理
        if (where.getChildren() != null && !where.getChildren().isEmpty()) {
            return;
        }
        where.setColumn(fieldToUnderlineCase(where.getColumn()));
    };

    public static OrderByOptCallback orderByOptCallback = (orderBy, userData) -> {
        orderBy.setField(fieldToUnderlineCase(orderBy.getField()));
    };

    /**
     * 字段转下划线格式
     * @param field 属性名
     * @return 结果
     */
    public static String fieldToUnderlineCase(String field) {
        if (field.contains(".")) {
            String[] arr = field.split("\\.");
            return arr[0] + "." + toUnderlineCase(arr[1]);
        } else {
            return toUnderlineCase(field);
        }
    }

    public static ModelReservoir getModelReservoir(IModel iModel) {
        return ReflectUtil.getFieldValue(iModel, "reservoir");
    }

    /**
     * 查询后处理预载入
     * - 组装一对一数据
     * - 一对多的关联在主数据返回后再统一查询组装
     * */
    public static <D extends IModel> List<D> resultTranshipment(Model queryModel, Class<?> clazz, List<Map<String, Object>> dataList) {
        List<IModel> models = new LinkedList<>();
        ModelReservoir reservoir = ReflectUtil.getFieldValue(queryModel, "reservoir");
        // 无预载入
        if (SqlUtil.isEmpty(reservoir.withs)) {
            dataList.forEach(data -> {
                D main = Model.newModel(clazz);
                ((Model) main).data(data);
                models.add(main);
            });
        } else {
            Map<String, Relation> relationMap = getWithRelation(queryModel, new RelationType[]{RelationType.OneToOne, RelationType.OneToMany});
            // 主表数据、一对一数据
            dataList.forEach(data -> {
                // 主表
                D main = Model.newModel(clazz);
                // 主表数据
                resultTranshipmentWith(main, data, null);
                // 一对一关联表数据
                reservoir.withs.forEach(relationName -> {
                    Relation relation = relationMap.get(relationName);
                    Model relationModel = Model.newModel(relation.getClazz(), null, main);
                    resultTranshipmentWith(relationModel, data, relationName);
                    ReflectUtil.setFieldValue(main, relationName, relationModel);
                });
                models.add(main);
            });
            // 一对多
            resultTranshipmentMany(reservoir.withs, relationMap, models);
        }
        return (List<D>) models;
    }

    /**
     * 将data数据通过alias找到并装载到模型上
     */
    public static void resultTranshipmentWith(IModel iModel, Map<String, Object> data, String alias) {
        Model model = ((Model) iModel);
        ModelReservoir reservoir = ReflectUtil.getFieldValue(model, "reservoir");
        reservoir.meta.fieldMap().forEach((name, field) -> {
            if (field.isRelation() || Model.class.isAssignableFrom(field.getClazz())) {
                return;
            }
            String tableName = alias == null ? reservoir.meta.table() : alias;
            String fieldName = tableName + "__" + toUnderlineCase(name);
            model.data(name, data.get(fieldName));
        });
    }

    private static Map<String, Relation> getWithRelation(Model model, RelationType[] types) {
        Map<String, Relation> relationMap = new LinkedHashMap<>();
        ModelReservoir reservoir = ReflectUtil.getFieldValue(model, "reservoir");
        reservoir.meta.relationMap().forEach((name, relation) -> {
            if (SqlUtil.inArray(types, relation.getType())) {
                relationMap.put(name, relation);
            }
        });
        return relationMap;
    }

    /**
     * 查询后模型处理
     * @param models 数据集
     * */
    private static void resultTranshipmentMany(List<String> withs, Map<String, Relation> relationMap, List<IModel> models)
    {
        // 超过500警告
        if (models.size() > 500) {
            logger.warn("QuickJava-ORM：The current query has too much data and may cause the service to crash. models.size=" + models.size());
        }
        // 数据关联条件：关联属性名=关联id
        Map<String, List<Object>> conditionMap = new LinkedHashMap<>();

        // 一对多查询条件准备
        withs.forEach(relationName -> {
            if (!conditionMap.containsKey(relationName)) {
                conditionMap.put(relationName, new LinkedList<>());
            }
            Relation relation = relationMap.get(relationName);
            models.forEach(model -> {
                Object localKeyValue = ReflectUtil.getFieldValue(model, relation.localKey());
                if (!conditionMap.get(relationName).contains(localKeyValue)) {  // 避免相同关联数据重复查询
                    conditionMap.get(relationName).add(localKeyValue);
                }
            });
        });
        // 一对多查询
        conditionMap.forEach((relationName, localKeyValues) -> {
            if (conditionMap.get(relationName).isEmpty()) {
                return;
            }
            Relation relation = relationMap.get(relationName);
            Model relationQueryModel = Model.newModel(relation.getClazz());
            List<Model> rows = relationQueryModel.where(relation.foreignKey(), Operator.IN, localKeyValues).select();
            // 装填数据
            models.forEach(model -> {
                Object localKeyVal = ReflectUtil.getFieldValue(model, relation.localKey());
                List<Model> set = rows.stream().filter(row -> localKeyVal.equals(ReflectUtil.getFieldValue(row, relation.foreignKey())))
                        .collect(Collectors.toList());
                ReflectUtil.setFieldValue(model, relationName, set);
            });
        });
    }

}
