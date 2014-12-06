package com.syw.hongyunhonghe.model;

import android.content.Context;
import android.util.Log;

import com.syw.hongyunhonghe.MainActivity;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by Hacker_PK on 14/12/3.
 */
public final class DataModel {

    private static final String APPLICATION_KEY = "a75833b218d559df577562e86416ea93";

    // one and only DataModel
    private static DataModel dataModel = null;

    private Context context;

    // get the one and only DataModel
    public static DataModel getInstance(Context context) {
        if (dataModel == null) {
            dataModel = new DataModel();
        }
        dataModel.context = context;
        return dataModel;
    }

    // must invoke this first of all
    public static void init(MainActivity mainActivity) {
        Bmob.initialize(mainActivity, APPLICATION_KEY);
    }

    // get magazines list
    public void getMagazines(final DataFoundListener listener) {
        // find magazines on bmob server
        BmobQuery<Magazine> query = new BmobQuery<Magazine>();
        // order by number
        query.order("-number");

        // find
        query.findObjects(context, new FindListener<Magazine>() {
            @Override
            public void onSuccess(List<Magazine> magazines) {
                // turn magazine to magazineInfo
                ArrayList<MagazineInfo> magazineList = new ArrayList<MagazineInfo>();
                for (Magazine magazine : magazines) {
                    MagazineInfo magazineInfo = new MagazineInfo(magazine.getTitle(), magazine.getCover(), magazine);
                    magazineList.add(magazineInfo);
                }
                listener.onSuccess(magazineList);
            }

            @Override
            public void onError(int i, String s) {
                Log.d("杂志列表获取失败", s);
            }
        });
    }

    public void getImgById(int coverID, final DataFoundListener listener) {
        BmobQuery<Img> query = new BmobQuery<Img>();
        query.addWhereEqualTo("imgID", coverID);
        query.findObjects(context, new FindListener<Img>() {
            @Override
            public void onSuccess(List<Img> object) {
                listener.onSuccess(object.get(0).getImgFile());
            }
            @Override
            public void onError(int code, String msg) {
                Log.d("图片查询失败", msg);
            }
        });
    }

    public void getArticleInfoListByMagazine(Magazine magazine, final DataFoundListener listener) {
        BmobQuery<Article> query = new BmobQuery<Article>();
        query.addWhereRelatedTo("articles", new BmobPointer(magazine));
        query.order("number");
        query.findObjects(context, new FindListener<Article>() {
            @Override
            public void onSuccess(List<Article> articles) {
                ArrayList<ArticleInfo> articleList = new ArrayList<ArticleInfo>();
                for (Article article : articles) {
                    ArticleInfo articleInfo = new ArticleInfo(article.getNumber(), article.getTitle(), article.getSubtitle(), article.isSwapTitlePos(), article.getAuthor(), article.getContentList());
                    articleList.add(articleInfo);
                }
                listener.onSuccess(articleList);
            }
            @Override
            public void onError(int code, String msg) {
                Log.d("图片查询失败", msg);
            }
        });
    }
}
