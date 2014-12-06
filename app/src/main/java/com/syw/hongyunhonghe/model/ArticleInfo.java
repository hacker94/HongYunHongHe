package com.syw.hongyunhonghe.model;

import android.content.Context;
import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by Hacker_PK on 14/12/5.
 */
public class ArticleInfo implements Serializable {

    private int number;
    private String title;
    private String subtitle;
    private boolean swapTitlePos;
    private String author;
    private ArrayList<String> contentList;

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
        if (s.substring(0, 5).equals("_IMG_")) {
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
        int imgID = Integer.parseInt(contentList.get(i++).split(":")[0].substring(5));

        // find it
        dm.getImgById(imgID, listener);
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

    public ArticleInfo(int number, String title, String subtitle, boolean swapTitlePos, String author, ArrayList<String> contentList) {
        this.number = number;
        this.title = title;
        this.subtitle = subtitle;
        this.swapTitlePos = swapTitlePos;
        this.author = author;
        this.contentList = contentList;
    }
}
