package com.syw.hongyunhonghe;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.syw.hongyunhonghe.model.ArticleInfo;
import com.syw.hongyunhonghe.model.DataFoundListener;
import com.syw.hongyunhonghe.model.DataModel;
import com.syw.hongyunhonghe.model.User;

import java.util.ArrayList;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.EmailVerifyListener;
import cn.bmob.v3.listener.ResetPasswordListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;


public class UserActivity extends Activity {

    private static final int LOGIN_REQUEST = 0;

    private User user = null;

    //public static final String PICKED_ARTICLE = "com.syw.hongyunhonghe.PICKED_ARTICLE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
    }


    @Override
    protected void onResume() {
        super.onResume();

        // check login status
        user = BmobUser.getCurrentUser(this, User.class);
        if (user == null) {
            // go to login activity
            Intent intent = new Intent(this, LoginActivity.class);
            startActivityForResult(intent, LOGIN_REQUEST);
        } else {
            setUserInfo();

            // set favourites button listener
            Button favButton = (Button)findViewById(R.id.favourite_button);
            favButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // check email verified
                    if (!checkEmailVerified(v.getContext())) {
                        return;
                    }

                    DataModel dm = DataModel.getInstance(getBaseContext());
                    dm.getFavArticleInfoListByUser(user, new DataFoundListener<ArrayList<ArticleInfo>>() {
                        @Override
                        public void onSuccess(ArrayList<ArticleInfo> articleList) {
                            if (articleList.size() == 0) {
                                Toast.makeText(getBaseContext(), "您还没有收藏", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            // set page number to "don't display"
                            for (ArticleInfo articleInfo : articleList) {
                                articleInfo.setNumber(-1);
                            }
                            Intent intent = new Intent(getBaseContext(), ArticleActivity.class);
                            intent.putExtra(MainActivity.ARTICLE_LIST, articleList);
                            startActivity(intent);
                        }

                        @Override
                        public void onFail(ArrayList<ArticleInfo> articleList) {
                            // do nothing
                        }
                    });
                }
            });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == LOGIN_REQUEST) {
            if (resultCode == RESULT_OK) {
                user = BmobUser.getCurrentUser(this, User.class);
                setUserInfo();
            } else {
                finish();
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    // set user info
    private void setUserInfo() {

        // set current user text
        TextView currentUserTextView = (TextView)findViewById(R.id.current_user_text);
        String name = user.getEmail();
        if (user.getRealname() != null) {
            name = user.getRealname();
        }
        currentUserTextView.setText("欢迎您，" + name);

        // set logout button listener
        Button logoutButton = (Button)findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // log out
                BmobUser.logOut(getBaseContext());
                user = BmobUser.getCurrentUser(getBaseContext(), User.class);

                // go to login activity
                Intent intent = new Intent(getBaseContext(), LoginActivity.class);
                startActivityForResult(intent, LOGIN_REQUEST);
            }
        });

        // set verify email button listener
        final Button verifyButton = (Button)findViewById(R.id.verify_email_button);
        if (user.getEmailVerified() != null && user.getEmailVerified()) {
            verifyButton.setVisibility(View.GONE);
        } else {
            verifyButton.setVisibility(View.VISIBLE);
        }
        verifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                BmobUser.requestEmailVerify(v.getContext(), user.getEmail(), new EmailVerifyListener() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(v.getContext(), "发送邮件成功，请到邮箱" + user.getEmail() + "中进行验证。验证后请重新登录。", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(int code, String e) {
                        Toast.makeText(v.getContext(), "请求验证邮件失败:" + e, Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        // set user editor button listener
        Button editButton = (Button)findViewById(R.id.edit_user_button);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // check email verified
                if (!checkEmailVerified(v.getContext())) {
                    return;
                }
                Intent intent = new Intent(getBaseContext(), UserInfoActivity.class);
                startActivity(intent);
            }
        });

        // set reset password button listener
        Button resetButton = (Button)findViewById(R.id.reset_pwd_button);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                // check email verified
                if (!checkEmailVerified(v.getContext())) {
                    return;
                }

                BmobUser.resetPassword(v.getContext(), user.getEmail(), new ResetPasswordListener() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(v.getContext(), "重置密码请求成功，请到您的邮箱" + user.getEmail() + "查看邮件进行密码重置操作", Toast.LENGTH_LONG).show();
                    }
                    @Override
                    public void onFailure(int code, String e) {
                        Toast.makeText(v.getContext(), "重置密码失败:" + e, Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }


    // check email verified
    private boolean checkEmailVerified(Context context) {
        if (user.getEmailVerified() == null || !user.getEmailVerified()) {
            Toast.makeText(context, "请先验证您的邮箱，如果已经验证请重新登录", Toast.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }
    }

}
