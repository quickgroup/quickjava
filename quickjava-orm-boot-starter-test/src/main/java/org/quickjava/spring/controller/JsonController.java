package org.quickjava.spring.controller;

import org.quickjava.spring.domain.Article;
import org.quickjava.spring.domain.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// json序列化测试
@RestController
@RequestMapping({"/json"})
public class JsonController {

    @RequestMapping({"/all"})
    public String all() {
        return "index.index";
    }

    @RequestMapping({"/spring"})
    public Object spring() {
        User user = new User();
        user.setNickname("test");
        user.setUsername("test");
        user.setPassword("test");
        Article article = new Article();
        article.setTitle("title asdasd");
        article.setContent("content asdasd");
        article.setUser(user);
        article.setUser2(user);
        user.setArticle(article);
        return user;
    }

    @RequestMapping({"/jackson"})
    public String jackson() {
        return "index.index";
    }

    @RequestMapping({"/fastjson"})
    public String fastjson() {
        return "index.index";
    }

}
