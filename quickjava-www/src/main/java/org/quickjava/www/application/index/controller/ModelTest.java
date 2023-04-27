package org.quickjava.www.application.index.controller;

import org.quickjava.framework.module.Controller;
import org.quickjava.framework.orm.example.Test;

public class ModelTest extends Controller {

    public Object index()
    {
        new Test().test();
        return "Hello, indexTest!";
    }

}
