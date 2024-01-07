package org.quickjava.orm.utils;

import org.quickjava.common.utils.ComUtil;
import org.quickjava.common.utils.ReflectUtil;
import org.quickjava.orm.IModel;
import org.quickjava.orm.Model;
import org.quickjava.orm.ModelReservoir;
import org.quickjava.orm.contain.Relation;
import org.quickjava.orm.enums.Operator;
import org.quickjava.orm.enums.RelationType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/*
 * Copyright (c) 2020~2023 http://www.quickjava.org All rights reserved.
 * +-------------------------------------------------------------------
 * Organization: QuickJava
 * +-------------------------------------------------------------------
 * Author: Qlo1062
 * +-------------------------------------------------------------------
 * File: HelperController
 * +-------------------------------------------------------------------
 * Date: 2023-3-14 11:52
 * +-------------------------------------------------------------------
 * License: Apache Licence 2.0
 * +-------------------------------------------------------------------
 */
public abstract class ORMHelper extends ComUtil {

    private static final Logger logger = LoggerFactory.getLogger(ORMHelper.class);

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
     * @param iModel
     * @param data
     * @param alias
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
