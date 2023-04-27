/*
 * Copyright (c) 2020~2021 http://www.quickjava.org All rights reserved.
 * +-------------------------------------------------------------------
 * Org: QuickJava
 * +-------------------------------------------------------------------
 * Author: Qlo1062
 * +-------------------------------------------------------------------
 * File: IndexValidator.java
 * +-------------------------------------------------------------------
 * Date: 2021/11/18 10:15:18
 * +-------------------------------------------------------------------
 * License: Apache Licence 2.0
 * +-------------------------------------------------------------------
 *
 */

package org.quickjava.www.application.index.validator;

import org.quickjava.framework.module.Validator;

public class IndexValidator extends Validator {

    static {
        // 默认规则
        rules.put("username", "名称", "empty|length:6,20", "不能为空|长度最少6个字，最长20个字");
        rules.put("password", "密码", "empty|length:6,20", "不能为空|长度最少6个字，最长20个字");
        rules.put("email", "邮箱", "empty|email");
        rules.put("tel", "手机号", "empty|tel");
        // 场景配置
        scenes.put("login", "username,password");
        scenes.put("register", "username,password,email");
    }

}
