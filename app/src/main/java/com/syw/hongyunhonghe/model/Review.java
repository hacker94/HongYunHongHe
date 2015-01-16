package com.syw.hongyunhonghe.model;

import cn.bmob.v3.BmobObject;

/**
 * Created by Hacker_PK on 15/1/17.
 */
public class Review extends BmobObject {
    private User user;
    private Article article;
    private String text;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
