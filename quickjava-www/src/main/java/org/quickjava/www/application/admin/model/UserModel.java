package org.quickjava.www.application.admin.model;

import org.quickjava.framework.Model;

/**
 * @author Qlo1062-(QloPC-zs)
 * @date 2021/1/20 12:53
 */
public class UserModel extends Model<UserModel> {

    public String name = "user";

    public static UserModel get() {
        return new UserModel();
    }

}
