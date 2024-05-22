package org.quickjava.spring.loader;/*
 * Copyright (c) 2020~2024 http://www.quickjava.org All rights reserved.
 * +-------------------------------------------------------------------
 * Organization: QuickJava
 * +-------------------------------------------------------------------
 * Author: Qlo1062
 * +-------------------------------------------------------------------
 * File: MybatisPlusConfigure
 * +-------------------------------------------------------------------
 * Date: 2024/5/21 16:45
 * +-------------------------------------------------------------------
 * License: Apache Licence 2.0
 * +-------------------------------------------------------------------
 */

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import org.quickjava.orm.ORMContext;
import org.quickjava.orm.model.ModelFieldHelper;
import org.quickjava.orm.model.callback.ModelFieldStrut;
import org.quickjava.orm.model.contain.ModelIdType;
import org.quickjava.orm.model.contain.ModelFieldMeta;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

public class MyBatisPlusConfigure {

    protected static TableId getTableId(ModelFieldMeta fieldMeta) {
        return fieldMeta.getField().getAnnotation(TableId.class);
    }
    protected static TableField getTableField(ModelFieldMeta fieldMeta) {
        return fieldMeta.getField().getAnnotation(TableField.class);
    }

    protected static void init() {
        ORMContext.setModelFieldStrut(new ModelFieldStrut() {

            @Override
            public boolean isTableId(ModelFieldMeta fieldMeta) {
                return getTableId(fieldMeta) != null;
            }

            @Override
            public ModelIdType tableIdType(ModelFieldMeta fieldMeta) {
                TableId tableId = getTableId(fieldMeta);
                if (tableId.type() == IdType.NONE) {
                    return ModelIdType.NONE;
                } else if (tableId.type() == IdType.AUTO) {
                    return ModelIdType.AUTO;
                } else if (tableId.type() == IdType.INPUT) {
                    return ModelIdType.INPUT;
                }
                return ModelIdType.NONE;
            }

            @Override
            public String tableIdName(ModelFieldMeta fieldMeta) {
                TableId tableId = getTableId(fieldMeta);
                if (tableId != null) {
                    return tableId.value();
                }
                return null;
            }

            @Override
            public Serializable tableIdFillValue(ModelFieldMeta fieldMeta) {
                TableId tableId = getTableId(fieldMeta);
                if (tableId.type() == IdType.NONE) {
                    return ModelIdType.NONE;
                } else if (tableId.type() == IdType.AUTO) {
                    return ModelIdType.AUTO;
                } else if (tableId.type() == IdType.INPUT) {
                    return ModelIdType.INPUT;
                } else if (tableId.type() == IdType.ASSIGN_ID) {
                    return IdWorker.getId();
                } else if (tableId.type() == IdType.ID_WORKER) {
                    return IdWorker.getId();
                } else if (tableId.type() == IdType.ID_WORKER_STR) {
                    return IdWorker.getIdStr();
                }
                return null;
            }

            @Override
            public boolean isTableField(ModelFieldMeta fieldMeta) {
                TableId tableId = fieldMeta.getField().getAnnotation(TableId.class);
                TableField tableField = fieldMeta.getField().getAnnotation(TableField.class);
                return tableId != null || (tableField != null && tableField.exist());
            }

            @Override
            public boolean tableFieldExist(ModelFieldMeta fieldMeta) {
                return isTableField(fieldMeta);
            }

            @Override
            public String getTableFieldName(ModelFieldMeta fieldMeta) {
                TableId tableId = fieldMeta.getField().getAnnotation(TableId.class);
                if (tableId != null) {
                    return tableId.value();
                }
                TableField tableField = fieldMeta.getField().getAnnotation(TableField.class);
                if (tableField != null) {
                    return tableField.value();
                }
                return null;
            }

            @Override
            public boolean hasInsertFull(ModelFieldMeta fieldMeta) {
                TableField tableField = fieldMeta.getField().getAnnotation(TableField.class);
                if (tableField != null) {
                    return tableField.fill() == FieldFill.INSERT || tableField.fill() == FieldFill.INSERT_UPDATE;
                }
                return false;
            }

            @Override
            public Object getInsertFullValue(ModelFieldMeta fieldMeta) {
                TableField tableField = fieldMeta.getField().getAnnotation(TableField.class);
                if (tableField != null) {
                    // 回调填充方法
                    MetaObjectHandler metaObjectHandler = SpringLoader.getBean(MetaObjectHandler.class);
                    if (metaObjectHandler != null) {    // 对象去接收值
//                        metaObjectHandler.insertFill();
                    }
                    // mp是根据字段类型进行对应数据填充
                    return ModelFieldHelper.fillValue(fieldMeta);
                }
                return null;
            }

            @Override
            public boolean hasUpdateFull(ModelFieldMeta fieldMeta) {
                TableField tableField = fieldMeta.getField().getAnnotation(TableField.class);
                if (tableField != null) {
                    return tableField.fill() == FieldFill.UPDATE || tableField.fill() == FieldFill.INSERT_UPDATE;
                }
                return false;
            }

            @Override
            public Object getUpdateFullValue(ModelFieldMeta fieldMeta) {
                TableField tableField = fieldMeta.getField().getAnnotation(TableField.class);
                if (tableField != null) {
                    // 回调填充方法
                    MetaObjectHandler metaObjectHandler = SpringLoader.getBean(MetaObjectHandler.class);
                    if (metaObjectHandler != null) {    // 对象去接收值
//                        metaObjectHandler.updateFill();
                    }
                    // mp是根据字段类型进行对应数据填充
                    return ModelFieldHelper.fillValue(fieldMeta);
                }
                return null;
            }

            @Override
            public boolean isTableLogic(ModelFieldMeta fieldMeta) {
                TableLogic tableLogic = fieldMeta.getField().getAnnotation(TableLogic.class);
                return tableLogic != null;
            }
        });
    }

}
