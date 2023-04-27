package org.quickjava.www.application.index.controller;

import org.quickjava.www.application.index.model.UserModel;
import org.quickjava.framework.module.Controller;
import org.quickjava.www.application.index.model.bean.User;
import org.quickjava.www.application.index.service.IndexService;

public class UserController extends Controller {

    public void index()
    {
        (new UserModel()).where("id", "=", "1").find();
    }

    public Object register(User user)
    {
        if (request.isPost()) {
            new IndexService().register(user);
        }
        return render();
    }

    public Object login()
    {
        return render();
    }

}
