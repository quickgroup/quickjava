package org.demo.www.index.controller;

import org.demo.www.index.model.UserModel;
import org.quickjava.framework.controller.Controller;

public class User extends Controller {

    public void index()
    {
        UserModel.get().where("id", "=", "1").find();
    }
}
