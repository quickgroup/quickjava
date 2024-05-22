package org.quickjava.spring.controller;

import org.quickjava.orm.wrapper.ModelWrapper;
import org.quickjava.spring.domain.Article;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/model"})
public class ModelController {

    @RequestMapping({"/all"})
    public Object all() {
        Article row = new ModelWrapper<Article>(Article.class)
                .eq(Article::getUserId, 1)
                .neq(Article::getUserId, 1)
                .gt(Article::getUserId, 1)
                .gte(Article::getUserId, 1)
                .lt(Article::getUserId, 1)
                .lte(Article::getUserId, 1)
                .in(Article::getUserId, 1, 2, 3)
                .notIn(Article::getUserId, 1, 2, 3)
                .between(Article::getUserId, 2, 3)
                .notBetween(Article::getUserId, 2, 3)
                .isNull(Article::getUserId)
                .isNotNull(Article::getUserId)
                .like(Article::getUserId, 1)
                .notLike(Article::getUserId, 1)
                .find();
        return row;
    }

}
