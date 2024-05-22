package org.quickjava.orm.model.callback;

import org.quickjava.orm.model.contain.ModelIdType;
import org.quickjava.orm.model.contain.ModelFieldMeta;
import org.quickjava.orm.model.contain.ModelMeta;

import java.io.Serializable;

/**
 * 模型属性闭包处理
 */
public interface ModelStrut {
    // 模型-表相关
    String getTableName(ModelMeta modelMeta);

    // 主键相关
    boolean isTableId(ModelFieldMeta fieldMeta);

    ModelIdType tableIdType(ModelFieldMeta fieldMeta);

    String tableIdName(ModelFieldMeta fieldMeta);

    /**
     * 主键id值（自实现）
     */
    Serializable tableIdFillValue(ModelFieldMeta fieldMeta);

    /**
     * 返回null表示无法判断
     */
    Boolean isTableField(ModelFieldMeta fieldMeta);

    /**
     * 返回null表示无法判断
     */
    Boolean tableFieldExist(ModelFieldMeta fieldMeta);

    String getTableFieldName(ModelFieldMeta fieldMeta);

    /**
     * 返回null表示无法判断
     */
    Boolean hasInsertFull(ModelFieldMeta fieldMeta);

    Object getInsertFullValue(ModelFieldMeta fieldMeta);

    /**
     * 返回null表示无法判断
     */
    Boolean hasUpdateFull(ModelFieldMeta fieldMeta);

    Object getUpdateFullValue(ModelFieldMeta fieldMeta);

    /**
     * 返回null表示无法判断
     */
    Boolean isTableLogic(ModelFieldMeta fieldMeta);

}
