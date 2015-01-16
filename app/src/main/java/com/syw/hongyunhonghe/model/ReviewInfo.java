package com.syw.hongyunhonghe.model;

/**
 * Created by Hacker_PK on 15/1/17.
 */
public class ReviewInfo {
    private String username;
    private String text;
    private String createTime;

    public String getUsername() {
        return username;
    }

    public String getText() {
        return text;
    }

    public String getCreateTime() {
        return createTime;
    }

    public ReviewInfo(String username, String text, String createTime) {
        this.username = username;
        this.text = text;
        this.createTime = createTime;
    }
}
