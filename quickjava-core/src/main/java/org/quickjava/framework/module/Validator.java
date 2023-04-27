/*
 * Copyright (c) 2020~2021 http://www.quickjava.org All rights reserved.
 * +-------------------------------------------------------------------
 * Org: QuickJava
 * +-------------------------------------------------------------------
 * Author: Qlo1062
 * +-------------------------------------------------------------------
 * File: Validator.java
 * +-------------------------------------------------------------------
 * Date: 2021/11/18 10:16:18
 * +-------------------------------------------------------------------
 * License: Apache Licence 2.0
 * +-------------------------------------------------------------------
 *
 */

package org.quickjava.framework.module;

import org.quickjava.framework.module.verify.RuleMap;
import org.quickjava.framework.module.verify.SceneMap;
import org.quickjava.framework.module.verify.VerifyResult;

import java.util.Map;

public class Validator {

    /**
     * @langCn 规则容器
     */
    public static RuleMap rules = new RuleMap();

    /**
     * @langCn 场景配置
     */
    public static SceneMap scenes = new SceneMap();

    /**
     * @langCn 全部验证
     * @param data 数据
     * @return 验证结果
     */
    public VerifyResult verify(Map data) {
        return new VerifyResult();
    }

    /**
     * 指定场景验证
     * @param data
     * @param scene
     * @return
     */
    public VerifyResult verify(Map data, String scene) {
        return new VerifyResult();
    }

    /**
     * 指定场景验证，并排除指定字段，默认场景名称default
     * @param data
     * @param scene
     * @param excludeFields
     * @return
     */
    public VerifyResult verify(Map data, String scene, String excludeFields) {
        return new VerifyResult();
    }
}
