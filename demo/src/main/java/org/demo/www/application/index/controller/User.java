package org.demo.www.application.index.controller;

import org.demo.www.application.index.model.UserModel;
import org.quickjava.framework.controller.Controller;

public class User extends Controller {

    public void index()
    {
        UserModel.get().where("id", "=", "1").find();
    }


}
