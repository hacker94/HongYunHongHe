package com.syw.hongyunhonghe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.syw.hongyunhonghe.model.ArticleInfo;
import com.syw.hongyunhonghe.model.DataFoundListener;
import com.syw.hongyunhonghe.model.DataModel;
import com.syw.hongyunhonghe.model.User;

import java.util.ArrayList;

import cn.bmob.v3.BmobUser;


public class UserActivity extends Activity {

    private static final int LOGIN_REQUEST = 0;

    private User user = null;

    //public static final String PICKED_ARTICLE = "com.syw.hongyunhonghe.PICKED_ARTICLE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

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
                    DataModel dm = DataModel.getInstance(getBaseContext());
                    dm.getFavArticleInfoListByUser(new DataFoundListener<ArrayList<ArticleInfo>>() {
                        @Override
                        public void onSuccess(ArrayList<ArticleInfo> articleList) {
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
        currentUserTextView.setText(user.getEmail());

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

        // set user editor button listener
        Button editButton = (Button)findViewById(R.id.edit_user_button);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // TODO: go to set user info activity
            }
        });
    }
}
