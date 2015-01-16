package com.syw.hongyunhonghe.model;

import android.content.Context;
import android.util.Log;

import com.syw.hongyunhonghe.MainActivity;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

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
                final ArrayList<ArticleInfo> articleList = new ArrayList<ArticleInfo>();
                for (Article article : articles) {
                    ArticleInfo articleInfo = new ArticleInfo(article);
                    articleList.add(articleInfo);
                }
                listener.onSuccess(articleList);
            }

            @Override
            public void onError(int code, String msg) {
                Log.d("文章列表查询失败", msg);
            }
        });
    }

    public void isFavArticle(Article article, final DataFoundListener<Boolean> listener) {
        User user = BmobUser.getCurrentUser(context, User.class);
        if (user == null) {
            listener.onFail(false);
        }

        BmobQuery<Favour> query = new BmobQuery<Favour>();
        query.addWhereEqualTo("user", user);
        query.addWhereEqualTo("article", article);
        query.findObjects(context, new FindListener<Favour>() {
            @Override
            public void onSuccess(List<Favour> list) {
                if (list.size() > 0) {
                    listener.onSuccess(true);
                } else {
                    listener.onFail(false);
                }
            }

            @Override
            public void onError(int i, String s) {
                Log.d("自动判断是否是收藏文章出错", s);
                listener.onFail(false);
            }
        });
    }

    public void getFavArticleInfoListByUser(final DataFoundListener listener) {
        User user = BmobUser.getCurrentUser(context, User.class);

        BmobQuery<Favour> query = new BmobQuery<Favour>();
        query.addWhereEqualTo("user", user);
        query.include("article");
        query.setLimit(1000);
        query.findObjects(context, new FindListener<Favour>() {

            @Override
            public void onSuccess(List<Favour> favList) {
                ArrayList<ArticleInfo> articleList = new ArrayList<ArticleInfo>();
                for (Favour favour : favList) {
                    Article article = favour.getArticle();
                    ArticleInfo articleInfo = new ArticleInfo(article);
                    articleList.add(articleInfo);
                }
                listener.onSuccess(articleList);
            }

            @Override
            public void onError(int code, String msg) {
                Log.d("查询用户关联的收藏表失败", msg);
                listener.onFail(null);
            }
        });
    }


    public void addFavArticle(ArticleInfo articleInfo, final DataFoundListener listener) {
        User user = BmobUser.getCurrentUser(context, User.class);
        if (user == null) {
            listener.onFail("请先登录");
        } else {
            Favour favour = new Favour();
            favour.setUser(user);
            favour.setArticle(articleInfo.getArticle());

            favour.save(context, new SaveListener() {
                @Override
                public void onSuccess() {
                    listener.onSuccess("收藏成功");
                }

                @Override
                public void onFailure(int i, String msg) {
                    Log.d("添加收藏失败", msg);
                    listener.onFail("添加收藏失败");
                }
            });
        }
    }

    public void removeFavArticle(ArticleInfo articleInfo, final DataFoundListener<String> listener) {
        User user = BmobUser.getCurrentUser(context, User.class);
        if (user == null) {
            listener.onFail("请先登录");
        } else {
            BmobQuery<Favour> query = new BmobQuery<Favour>();
            query.addWhereEqualTo("user", user);
            query.addWhereEqualTo("article", articleInfo.getArticle());
            query.findObjects(context, new FindListener<Favour>() {
                @Override
                public void onSuccess(List<Favour> list) {
                    for (Favour favour : list) {
                        favour.delete(context, new DeleteListener() {
                            @Override
                            public void onSuccess() {
                                listener.onSuccess("取消收藏成功");
                            }

                            @Override
                            public void onFailure(int i, String s) {
                                Log.d("删除收藏出错", s);
                                listener.onFail("取消收藏失败");
                            }
                        });
                    }
                }

                @Override
                public void onError(int i, String s) {
                    Log.d("查找要删除的收藏出错", s);
                    listener.onFail("取消收藏失败");
                }
            });
        }
    }


    public void addReview(ArticleInfo articleInfo, String text, final DataFoundListener<String> listener) {
        User user = BmobUser.getCurrentUser(context, User.class);
        if (user == null) {
            listener.onFail("请先登录");
        } else {
            Review review = new Review();
            review.setUser(user);
            review.setArticle(articleInfo.getArticle());
            review.setText(text);

            review.save(context, new SaveListener() {
                @Override
                public void onSuccess() {
                    listener.onSuccess("评论成功");
                }

                @Override
                public void onFailure(int i, String msg) {
                    Log.d("添加评论失败", msg);
                    listener.onFail("评论失败");
                }
            });
        }
    }


    public void getReview(ArticleInfo articleInfo, final DataFoundListener<ArrayList<ReviewInfo>> listener) {
        BmobQuery<Review> query = new BmobQuery<Review>();
        query.addWhereEqualTo("article", articleInfo.getArticle());
        query.include("user");
        query.setLimit(1000);
        query.findObjects(context, new FindListener<Review>() {

            @Override
            public void onSuccess(List<Review> reviews) {
                ArrayList<ReviewInfo> reviewList = new ArrayList<>();
                for (Review review : reviews) {
                    ReviewInfo reviewInfo = new ReviewInfo(review.getUser().getUsername(), review.getText(), review.getCreatedAt());
                    reviewList.add(reviewInfo);
                }
                listener.onSuccess(reviewList);
            }

            @Override
            public void onError(int code, String msg) {
                Log.d("获取评论列表失败", msg);
                listener.onFail(null);
            }
        });
    }
}
