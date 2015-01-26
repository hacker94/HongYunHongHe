package com.syw.hongyunhonghe.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Hacker_PK on 14/12/5.
 */
public class ArticleInfo implements Serializable {

    private Article article;
    private int number;
    private String title;
    private String subtitle;
    private boolean swapTitlePos;
    private String author;
    private ArrayList<String> contentList;
    private boolean isFav;

    private int i;

    public void reset() {
        i = 0;
    }

    public boolean hasNextContent() {
        if (i < contentList.size()) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isNextImg() {
        String s = contentList.get(i);
        if (s.length() > 5 && s.substring(0, 5).equals("_IMG_")) {
            return true;
        } else {
            return false;
        }
    }

    public String getNextPart() {
        return contentList.get(i++);
    }

    public void getNextImg(DataModel dm, DataFoundListener listener) {
        // parse the img id
        int imgID = Integer.parseInt(contentList.get(i++).split(":")[1]);

        // find it
        dm.getImgById(imgID, listener);
    }

    public Article getArticle() {
        return article;
    }

    public boolean isFav() {
        return isFav;
    }

    public void setFav(boolean isFav) {
        this.isFav = isFav;
    }

    public ArrayList<String> getContentList() {
        return contentList;
    }

    public void setContentList(ArrayList<String> contentList) {
        this.contentList = contentList;
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

    public boolean isSwapTitlePos() {
        return swapTitlePos;
    }

    public void setSwapTitlePos(boolean swapTitlePos) {
        this.swapTitlePos = swapTitlePos;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public ArticleInfo(Article article) {
        this.article = article;
        this.number = article.getNumber();
        this.title = article.getTitle();
        this.subtitle = article.getSubtitle();
        this.swapTitlePos = article.isSwapTitlePos();
        this.author = article.getAuthor();
        this.contentList = article.getContentList();
    }
}
