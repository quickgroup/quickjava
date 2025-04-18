package org.quickjava.orm.spring.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import org.quickjava.orm.model.Model;
import org.quickjava.orm.model.annotation.ModelField;
import org.quickjava.orm.model.annotation.ModelId;
import org.quickjava.orm.model.annotation.ModelName;
import org.quickjava.orm.model.enums.ModelFieldFill;

import java.util.Date;
import java.util.List;

/*
 * Copyright (c) 2020~2023 http://www.quickjava.org All rights reserved.
 * +-------------------------------------------------------------------
 * Organization: QuickJava
 * +-------------------------------------------------------------------
 * Author: Qlo1062
 * +-------------------------------------------------------------------
 * File: Article
 * +-------------------------------------------------------------------
 * Date: 2023-3-8 16:26
 * +-------------------------------------------------------------------
 * License: Apache Licence 2.0
 * +-------------------------------------------------------------------
 */
@ModelName
public class Article extends Model {

    @TableId
    @ModelId
    private Long id;

    private Long userId;

    private String title;

    private String content;

    private Long categoryId;

    // 一对一关联
    private User user;

    private User user2;

    // 一对多关联
    @ModelField(exist = false)
    private List<User> users;

    // 一对多：评论回复表
    @ModelField(exist = false)
    private List<Comment> comments;

    // 一对多：评论回复表
    @ModelField(exist = false)
    private List<ArticleTag> tags;

    @ModelField(insertFill = ModelFieldFill.DATETIME)
    private Date createTime;

    @ModelField(updateFill = ModelFieldFill.DATETIME)
    private Date updateTime;

    @ModelField(softDelete = true)
    private Date deleteTime;

    public User user() {
        return hasOne(User.class, "userId", "id");
    }

    public User user2() {
        return hasOne(User.class, "userId", "id");
    }

    public User users() {
        return hasMany(User.class, "userId", "id");
    }

    public Comment comments() {
        return hasMany(Comment.class, "id", "articleId");
    }

    public ArticleTag tags() {
        return hasMany(ArticleTag.class, "id", "articleId");
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public User getUser2() {
        return user2;
    }

    public void setUser2(User user2) {
        this.user2 = user2;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public List<ArticleTag> getTags() {
        return tags;
    }

    public void setTags(List<ArticleTag> tags) {
        this.tags = tags;
    }
}
