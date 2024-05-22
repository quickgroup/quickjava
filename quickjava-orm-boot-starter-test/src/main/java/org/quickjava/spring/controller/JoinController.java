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
                .like(Article::getUserId, 1)
                // 关联user表并把user表数据加载到Article.user属性上
//                .leftJoin(User.class, User::getId, Article::getUserId, Article::getUser)
                // 关联user表（别名user2） 并把user表数据加载到Article.user属性上
//                .leftJoin(User.class, "user2", User::getId, Article::getUserId, Article::getUser)
                // 关联user表多个条件上
                .leftJoin(User.class, condition -> {
                    condition.eq(User::getId, Article::getUserId)
                            .eq(User::getStatus, "normal")
                            .neq(User::getStatus, "waitverify");
                }, Article::getUser)
                .find();
        return row;
    }

}
