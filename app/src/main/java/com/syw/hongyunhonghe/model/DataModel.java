package com.syw.hongyunhonghe.model;

import com.syw.hongyunhonghe.MainActivity;

import cn.bmob.v3.Bmob;

/**
 * Created by Hacker_PK on 14/12/3.
 */
public final class DataModel {

    private static final String APPLICATION_KEY = "a75833b218d559df577562e86416ea93";

    // one and only DataModel
    private static DataModel dataModel = null;

    // get the one and only DataModel
    public static DataModel getInstance() {
        if (dataModel == null) {
            dataModel = new DataModel();
        }
        return dataModel;
    }

    // must invoke this first of all
    public static void init(MainActivity mainActivity) {
        Bmob.initialize(mainActivity, APPLICATION_KEY);
    }

}
