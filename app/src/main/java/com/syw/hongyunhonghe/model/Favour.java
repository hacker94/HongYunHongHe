package com.syw.hongyunhonghe.model;

import cn.bmob.v3.BmobObject;

/**
 * Created by Hacker_PK on 15/1/17.
 */
public class Favour extends BmobObject {
    private User user;
    private Article article;

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
}
