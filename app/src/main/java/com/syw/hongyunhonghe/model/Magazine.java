package com.syw.hongyunhonghe.model;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobRelation;

/**
 * Created by Hacker_PK on 14/12/3.
 */
public class Magazine extends BmobObject {

    private String title;
    private int cover;
    private BmobRelation articles;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BmobRelation getArticles() {
        return articles;
    }

    public void setArticles(BmobRelation articles) {
        this.articles = articles;
    }

    public int getCover() {
        return cover;
    }

    public void setCover(int cover) {
        this.cover = cover;
    }
}
