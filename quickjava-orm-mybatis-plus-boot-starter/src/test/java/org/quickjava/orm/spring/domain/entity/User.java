package org.quickjava.orm.spring.domain.entity;

import org.quickjava.orm.model.Model;
import org.quickjava.orm.model.annotation.ModelField;
import org.quickjava.orm.model.annotation.OneToMany;
import org.quickjava.orm.model.annotation.OneToOne;

import java.util.List;

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
public class User extends Model {

    private Long id;

    private String username;

    private String nickname;

    private String password;

    private String avatar;

    private String status;

    @ModelField(exist = false)
    @OneToOne(foreignKey = "userId", localKey = "id")
    private Article article;

    @ModelField(exist = false)
    @OneToMany(foreignKey = "userId", localKey = "id")
    private List<Article> articles;

    public Article article() {
        return hasOne(Article.class, "userId", "id");
    }

    public Article articles() {
        return hasOne(Article.class, "userId", "id");
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }
}
