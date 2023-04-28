package org.quickjava.framework.orm.example;

import com.baomidou.mybatisplus.annotation.TableField;
import org.quickjava.framework.orm.Model;
import org.quickjava.framework.orm.annotation.ModelName;

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
public class ArticleTag extends Model {

    private Long id;

    private Long articleId;

    private String value;

    @TableField(exist = false)
    private Article article;

    public Article article() {
        return hasOne(Article.class, "articleId", "id");
    }
}
