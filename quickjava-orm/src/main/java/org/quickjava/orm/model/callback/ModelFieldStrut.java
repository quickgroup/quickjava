package org.quickjava.orm.model.callback;

import org.quickjava.orm.model.contain.ModelIdType;
import org.quickjava.orm.model.contain.ModelFieldMeta;

import java.io.Serializable;

/**
 * 模型属性闭包处理
 */
public interface ModelFieldStrut {

    boolean isTableId(ModelFieldMeta fieldMeta);

    ModelIdType tableIdType(ModelFieldMeta fieldMeta);

    Serializable tableIdValue(ModelFieldMeta fieldMeta);

    boolean isTableField(ModelFieldMeta fieldMeta);

    boolean tableFieldExist(ModelFieldMeta fieldMeta);

    String getTableFieldName(ModelFieldMeta fieldMeta);

    boolean hasInsertFull(ModelFieldMeta fieldMeta);

    Object getInsertFullValue(ModelFieldMeta fieldMeta);

    boolean hasUpdateFull(ModelFieldMeta fieldMeta);

    Object getUpdateFullValue(ModelFieldMeta fieldMeta);

    boolean isTableLogic(ModelFieldMeta fieldMeta);

}
