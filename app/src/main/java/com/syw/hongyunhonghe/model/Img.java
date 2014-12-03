package com.syw.hongyunhonghe.model;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by Hacker_PK on 14/12/3.
 */
public class Img extends BmobObject {
    private int imgID;
    private BmobFile imgFile;

    public int getImgID() {
        return imgID;
    }

    public void setImgID(int imgID) {
        this.imgID = imgID;
    }

    public BmobFile getImgFile() {
        return imgFile;
    }

    public void setImgFile(BmobFile imgFile) {
        this.imgFile = imgFile;
    }
}
