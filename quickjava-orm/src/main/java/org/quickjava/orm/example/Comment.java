package org.quickjava.orm.example;

import com.baomidou.mybatisplus.annotation.TableField;
import org.quickjava.orm.Model;
import org.quickjava.orm.annotation.ModelName;

/*
 * Copyright (c) 2020~2023 http://www.quickjava.org All rights reserved.
 * +-------------------------------------------------------------------
 * Organization: QuickJava
 * +-------------------------------------------------------------------
 * Author: Qlo1062
 * +-------------------------------------------------------------------
 * File: User
 * +-------------------------------------------------------------------
 * Date: 2023-3-8 16:26
 * +-------------------------------------------------------------------
 * License: Apache Licence 2.0
 * +-------------------------------------------------------------------
 */
@ModelName
public class Comment extends Model {

    private Long id;

    private Long articleId;

    private Long userId;

    private String content;

    @TableField(exist = false)
    private User user;

    @TableField(exist = false)
    private Article article;

    public User user() {
        return hasOne(User.class, "userId", "id");
    }

    public Article article() {
        return hasOne(Article.class, "articleId", "id");
    }
}
