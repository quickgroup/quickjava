/*
 * Copyright (c) 2021.
 * More Info to http://www.quickjava.org
 */

package org.quickjava.www.application.common.model;

import org.quickjava.orm.model.Model;
import org.quickjava.orm.model.annotation.ModelName;

/**
 * @author Qlo1062-(QloPC-zs)
 * #date 2021/1/20 12:53
 */
@ModelName("qj_user")
public class UserModel extends Model {

    /**
     * 表字段
     */
    public Integer id;

    public String username;

    public String password;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
