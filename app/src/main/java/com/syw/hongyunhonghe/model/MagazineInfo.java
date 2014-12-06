package com.syw.hongyunhonghe.model;

import android.provider.ContactsContract;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by Hacker_PK on 14/12/3.
 */
public class MagazineInfo {

    private String title;
    private int coverID;
    private Magazine magazine;

    public MagazineInfo(String title, int coverID, Magazine magazine) {
        this.title = title;
        this.coverID = coverID;
        this.magazine = magazine;
    }

    public String getTitle() {
        return title;
    }

    public void getCover(DataModel dm, final DataFoundListener listener) {
        dm.getImgById(coverID, listener);

    }

    public void getArticleList(DataModel dm, final DataFoundListener listener) {
        dm.getArticleInfoListByMagazine(magazine, listener);
    }

}
