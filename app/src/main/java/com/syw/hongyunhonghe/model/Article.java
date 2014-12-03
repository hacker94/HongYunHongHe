package com.syw.hongyunhonghe.model;

import java.util.ArrayList;

import cn.bmob.v3.BmobObject;

/**
 * Created by Hacker_PK on 14/12/3.
 */
public class Article extends BmobObject {
    private String title;
    private String subtitle;
    private boolean swapTitlePos;
    private String author;
    private ArrayList<String> contentList;
    private Magazine magazine;

    public boolean isSwapTitlePos() {
        return swapTitlePos;
    }

    public void setSwapTitlePos(boolean swapTitlePos) {
        this.swapTitlePos = swapTitlePos;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public ArrayList<String> getContentList() {
        return contentList;
    }

    public void setContentList(ArrayList<String> contentList) {
        this.contentList = contentList;
    }

    public Magazine getMagazine() {
        return magazine;
    }

    public void setMagazine(Magazine magazine) {
        this.magazine = magazine;
    }
}
