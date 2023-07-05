//package org.quickjava.orm;
//
//import com.baomidou.mybatisplus.annotation.TableField;
//import com.fasterxml.jackson.annotation.JsonIgnore;
//import org.quickjava.common.utils.DatetimeUtil;
//import org.quickjava.common.utils.ReflectUtil;
//import org.quickjava.orm.callback.WhereCallback;
//import org.quickjava.orm.contain.DataMap;
//import org.quickjava.orm.contain.ModelFieldO;
//import org.quickjava.orm.contain.ModelMeta;
//import org.quickjava.orm.contain.Pagination;
//import org.quickjava.orm.enums.ModelFieldFill;
//import org.quickjava.orm.enums.Operator;
//import org.quickjava.orm.utils.ModelUtil;
//import org.quickjava.orm.utils.ORMHelper;
//import org.quickjava.orm.utils.QuerySetHelper;
//import org.quickjava.orm.utils.QuickORMException;
//
//import java.io.Serializable;
//import java.util.*;
//
///*
// * Copyright (c) 2020~2023 http://www.quickjava.org All rights reserved.
// * +-------------------------------------------------------------------
// * Organization: QuickJava
// * +-------------------------------------------------------------------
// * Author: Qlo1062
// * +-------------------------------------------------------------------
// * File: ModelInterface
// * +-------------------------------------------------------------------
// * Date: 2023/6/17 15:35
// * +-------------------------------------------------------------------
// * License: Apache Licence 2.0
// * +-------------------------------------------------------------------
// */
//public abstract class AModel implements IModel {
//
//    /**
//     * 模型元信息
//     * */
//    @JsonIgnore
//    @TableField(exist = false)
//    protected ModelMeta __meta;
//
//    /**
//     * 预载入属性
//     * */
//    @JsonIgnore
//    @TableField(exist = false)
//    protected List<String> __withs;
//
//    /**
//     * 数据
//     */
//    @JsonIgnore
//    @TableField(exist = false)
//    protected final DataMap __data = new DataMap();
//
//    /**
//     * 修改的字段
//     * */
//    @JsonIgnore
//    @TableField(exist = false)
//    protected List<ModelFieldO> __modified;
//
//    /**
//     * 查询器
//     * */
//    @JsonIgnore
//    @TableField(exist = false)
//    protected QuerySet __querySet;
//
//    /**
//     * 关联的父模型对象
//     * */
//    @JsonIgnore
//    @TableField(exist = false)
//    protected Model __parent;
//
//    /**
//     * 是素模型
//     * */
//    @JsonIgnore
//    @TableField(exist = false)
//    protected boolean __vegetarian = true;
//
//
//    //TODO:---------- 类方法 ----------
//    // 强制修改对象是否是素模型
//    public AModel vegetarian(boolean vegetarian) {
//        this.__vegetarian = vegetarian;
//        return this;
//    }
//
//    //TODO:---------- 查询方法 ----------
//    public abstract AModel where(String field, Operator opr, Object val);
//
//    public abstract AModel where(String field, String opr, Object val);
//
//    public abstract AModel where(String field, Object val);
//
//    public abstract AModel where(String field, Operator operator);
//
//    public abstract AModel where(Map<String, Object> query);
//
//    public abstract AModel where(String sql);
//
//    public abstract AModel where(WhereCallback callback);
//
//    public abstract AModel whereOr(WhereCallback callback);
//
//    public abstract AModel eq(String field, Object val);
//
//    public abstract AModel neq(String field, Object val);
//
//    public abstract AModel gt(String field, Object val);
//
//    public abstract AModel gte(String field, Object val);
//
//    public abstract AModel lt(String field, Object val);
//
//    public abstract AModel lte(String field, Object val);
//
//    public abstract AModel in(String field, Object ...args);
//
//    public abstract AModel notIn(String field, Object ...args);
//
//    public abstract AModel isNull(String field);
//
//    public abstract AModel isNotNull(String field);
//
//    public abstract AModel between(String field, Object val1, Object val2);
//
//    //---------- TODO::操作方法：增删改查 ----------
//    public abstract <D extends IModel> D insert();
//
//    /**
//     * 使用数据集新增
//     * @param data 数据集
//     * @return 模型对象
//     */
//    public abstract <D extends IModel> D insert(DataMap data);
//
//    /**
//     * 删除
//     * @return 1
//     */
//    public abstract int delete();
//
//    /**
//     * 更新
//     * @return 模型对象
//     */
//    public abstract <D extends IModel> D update();
//
//    public abstract <D extends IModel> D update(DataMap data);
//
//    public abstract <D extends IModel> D updateById();
//
//    /**
//     * 保存数据
//     * - 自动判断主键是否为null，为null执行新增，否则进行更新
//     * @return 模型对象
//     */
//    public abstract <D extends IModel> D save();
//
//    public abstract <D extends IModel> D save(DataMap data);
//
//    /**
//     * 查询一条数据
//     * @return 模型对象
//     * @param <D> 模型类
//     */
//    public abstract <D extends IModel> D find();
//
//    public abstract <D extends IModel> D find(Serializable id);
//
//    public abstract <D extends IModel> List<D> select();
//
//    public abstract <D> Pagination<D> pagination(Integer page, Integer pageSize);
//
//    public abstract <D> Pagination<D> pagination();
//
//    //---------- TODO::操作方法：排序、聚合等 ----------
//    /**
//     * 排序
//     * @param field 字段
//     * @param asc 排序方式：ASC、DESC
//     * @return 模型对象
//     */
//    public abstract AModel order(String field, String asc);
//
//    public abstract AModel order(String field, boolean asc);
//
//    public abstract AModel order(String fields);
//
//    public abstract AModel order(List<String> fields);
//
//    public abstract AModel order(String[] fields);
//
//    public abstract AModel limit(Integer index, Integer count);
//
//    public abstract AModel limit(Integer count);
//
//    /**
//     * 分页
//     * @param page 页数
//     * @return 模型对象
//     */
//    public abstract AModel page(Integer page);
//
//    /**
//     * 分页
//     * @param page 页数
//     * @param size 页大小
//     * @return 模型对象
//     */
//    public abstract AModel page(Integer page, Integer size);
//
//    public abstract AModel group(String fields);
//
//    public abstract AModel having(String fields);
//
//    public abstract AModel union(String sql);
//
//    public abstract AModel union(String[] sqlArr);
//
//    public abstract AModel distinct(boolean distinct);
//
//    public abstract AModel lock(boolean lock);
//
//    // 编译sql，当前只支持查询
//    public abstract AModel fetchSql(boolean fetch);
//
//
//
//    //TODO::---------- 数据方法 START ----------
//    /**
//     * 实体通过map装载数据
//     * @param data 数据集
//     * @return 模型对象
//     */
//    public abstract AModel data(DataMap data);
//
//    /**
//     * 获取data中的数据
//     * @param field 属性名
//     * @return 属性值
//     */
//    public abstract Object data(String field);
//
//    /**
//     * 装载数据
//     * @param name 属性名
//     * @param val 属性值
//     * @return 模型对象
//     */
//    public abstract AModel data(String name, Object val);
//
//    /**
//     * 获取data全部数据
//     * @return 模型数据
//     */
//    public abstract DataMap data();
//
//    public abstract String pk();
//
//    public abstract Object pkVal();
//    //TODO::---------- 数据方法 END ----------
//
//
//
//    //TODO::---------- 静态操作方法 START ----------
//    /**
//     * 通过 Map 创建对象
//     * @param data 数据集
//     * @return 模型对象
//     */
//    public abstract AModel create(Map<String, Object> data);
//
//    /**
//     * 通过 DataMap 创建对象
//     * @param data 数据集
//     * @return 模型对象
//     */
//    public abstract AModel create(DataMap data);
//
//    public abstract AModel create(Model model);
//
//    /**
//     * 批量创建
//     * @param dataList 数据列表
//     * @return 对象数量
//     */
//    public abstract Integer bulkCreate(List<DataMap> dataList);
//    //TODO::---------- 静态操作方法 END ----------
//}
