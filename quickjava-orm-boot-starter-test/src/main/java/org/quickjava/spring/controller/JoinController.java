package org.quickjava.spring.controller;

import org.quickjava.orm.wrapper.ModelWrapper;
import org.quickjava.spring.domain.Article;
import org.quickjava.spring.domain.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/join"})
public class JoinController {

    @RequestMapping({"/111"})
    public Object i111() {
        Article row = new ModelWrapper<Article>(Article.class)
                .like(Article::getTitle, "admin")
                .leftJoin(User.class, User::getId, Article::getUserId)
                .find();
        return row;
    }

}
