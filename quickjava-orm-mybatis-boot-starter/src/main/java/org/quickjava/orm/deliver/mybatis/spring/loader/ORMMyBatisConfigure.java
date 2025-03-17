package org.quickjava.orm.deliver.mybatis.spring.loader;/*
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

import cn.hutool.core.util.ObjectUtil;
import org.quickjava.orm.ORMContext;
import org.quickjava.orm.model.callback.ModelStrut;
import org.quickjava.orm.model.contain.ModelIdType;
import org.quickjava.orm.model.contain.ModelFieldMeta;
import org.quickjava.orm.model.contain.ModelMeta;

import java.io.Serializable;

public class ORMMyBatisConfigure {

    protected static void loadConfig(ORMMyBatisLoader loader) {
        if (ObjectUtil.isEmpty(loader.getOrmProps().getTypeAliasesPackage())) {
            String typeAliasesPackage = loader.getEnvironment().getProperty("mybatis.type-aliases-package");
            if (ObjectUtil.isEmpty(typeAliasesPackage)) {
                typeAliasesPackage = loader.getEnvironment().getProperty("mybatis-plus.type-aliases-package");
            }
            if (ObjectUtil.isEmpty(typeAliasesPackage)) {
                typeAliasesPackage = loader.getEnvironment().getProperty("spring.component-scan.base-package");
            }
            loader.getOrmProps().setTypeAliasesPackage(typeAliasesPackage);
        }
    }

    protected static void init(ORMMyBatisLoader loader) {
        // mp配置加载
        loadConfig(loader);
        // 插槽处理
        ORMContext.setModelStrut(new ModelStrut() {
            @Override
            public String getTableName(ModelMeta modelMeta) {
                return "";
            }

            @Override
            public boolean isTableId(ModelFieldMeta fieldMeta) {
                return false;
            }

            @Override
            public ModelIdType tableIdType(ModelFieldMeta fieldMeta) {
                return null;
            }

            @Override
            public String tableIdName(ModelFieldMeta fieldMeta) {
                return "";
            }

            @Override
            public Serializable tableIdFillValue(ModelFieldMeta fieldMeta) {
                return null;
            }

            @Override
            public Boolean isTableField(ModelFieldMeta fieldMeta) {
                return null;
            }

            @Override
            public Boolean tableFieldExist(ModelFieldMeta fieldMeta) {
                return null;
            }

            @Override
            public String getTableFieldName(ModelFieldMeta fieldMeta) {
                return "";
            }

            @Override
            public Boolean hasInsertFull(ModelFieldMeta fieldMeta) {
                return null;
            }

            @Override
            public Object getInsertFullValue(ModelFieldMeta fieldMeta) {
                return null;
            }

            @Override
            public Boolean hasUpdateFull(ModelFieldMeta fieldMeta) {
                return null;
            }

            @Override
            public Object getUpdateFullValue(ModelFieldMeta fieldMeta) {
                return null;
            }

            @Override
            public Boolean isTableLogic(ModelFieldMeta fieldMeta) {
                return null;
            }
        });
    }

}
