package org.quickjava.orm.model;

import org.quickjava.orm.model.enums.RelationType;

/*
 * Copyright (c) 2020~2023 http://www.quickjava.org All rights reserved.
 * +-------------------------------------------------------------------
 * Organization: QuickJava
 * +-------------------------------------------------------------------
 * Author: Qlo1062
 * +-------------------------------------------------------------------
 * File: ModelInterface
 * +-------------------------------------------------------------------
 * Date: 2023/6/17 15:35
 * +-------------------------------------------------------------------
 * License: Apache Licence 2.0
 * +-------------------------------------------------------------------
 */
public interface IModel<Children> {

    Children relation(String fieldName, Class<?> clazz, RelationType type, String localKey, String foreignKey);

}
