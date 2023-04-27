package org.quickjava.framework.orm.example;

import org.quickjava.framework.orm.Model;
import org.quickjava.framework.orm.annotation.ModelName;

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

    private Long id;

    private Long userId;

    private String title;

    private String content;

    private String contentType;

    private Long categoryId;

    // 一对一关联
    private User user;

    private User user2;

    // 一对多关联
    private List<User> users;

    public User user() {
        return hasOne(User.class, "userId", "id");
    }

    public User user2() {
        return hasOne(User.class, "userId", "id");
    }

    public User users() {
        return hasMany(User.class, "userId", "id");
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

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
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
}
