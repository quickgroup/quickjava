package org.quickjava.spring.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/index"})
public class IndexController {

    @RequestMapping({"/index"})
    public String index() {
        System.out.println("index.index");
        return "index.index";
    }

}
