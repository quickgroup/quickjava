/*
 * Copyright (c) 2021.
 * More Info to http://www.quickjava.org
 */

package org.quickjava.www.application.common.model;

import org.quickjava.framework.Model;

/**
 * @author Qlo1062-(QloPC-zs)
 * @date 2021/1/20 12:53
 */
public class UserModel extends Model<UserModel> {

    /**
     * 模型参数
     */
    public String name = "user";

    /**
     * 表字段
     */
    public Integer id;

    public String username;

    public String password;

}
