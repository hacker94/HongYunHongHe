package com.syw.hongyunhonghe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.syw.hongyunhonghe.model.ArticleInfo;
import com.syw.hongyunhonghe.model.DataFoundListener;
import com.syw.hongyunhonghe.model.DataModel;
import com.syw.hongyunhonghe.model.MagazineInfo;

import java.util.ArrayList;

import cn.bmob.v3.datatype.BmobFile;


public class MainActivity extends Activity {

    public static final String ARTICLE_LIST = "com.syw.hongyunhonghe.ARTICLE_LIST";

    private LinearLayout magazinesLL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // find views
        magazinesLL = (LinearLayout)findViewById(R.id.magazines_linear_layout);

        // init DataModel
        DataModel.init(this);

        // refresh magazines list
        refreshMagazines();
    }

    // refresh magazine list
    private void refreshMagazines() {
        // clear magazinesLL
        magazinesLL.removeAllViews();

        final DataModel dm = DataModel.getInstance(getBaseContext());
        dm.getMagazines(new DataFoundListener<ArrayList<MagazineInfo>>() {
            @Override
            public void onSuccess(ArrayList<MagazineInfo> magazineList) {
                for (final MagazineInfo magazineInfo : magazineList) {
                    // inflate
                    LayoutInflater layoutInflater = getLayoutInflater();
                    LinearLayout mgzIconButton = (LinearLayout)layoutInflater.inflate(R.layout.mgz_icon_button, magazinesLL, false);

                    // set title
                    TextView magazineTitleTextView = (TextView)mgzIconButton.findViewById(R.id.magazine_title_text_view);
                    magazineTitleTextView.setText(magazineInfo.getTitle());
                    magazineTitleTextView.setOnClickListener(new MagazineOnClickListener(magazineInfo));

                    // set cover
                    final ImageButton coverImageButton = (ImageButton)mgzIconButton.findViewById(R.id.cover_image_button);
                    coverImageButton.setOnClickListener(new MagazineOnClickListener(magazineInfo));

                    // add to magazinesLL
                    magazinesLL.addView(mgzIconButton);

                    // get img
                    magazineInfo.getCover(dm, new DataFoundListener<BmobFile>() {
                        @Override
                        public void onSuccess(BmobFile imgFile) {
                            imgFile.loadImage(getBaseContext(), coverImageButton);
                        }
                    });
                }

            }
        });

    }

    private class MagazineOnClickListener implements View.OnClickListener {

        private MagazineInfo magazineInfo;

        public MagazineOnClickListener(MagazineInfo magazineInfo) {
            this.magazineInfo = magazineInfo;
        }

        @Override
        public void onClick(View v) {
            DataModel dm = DataModel.getInstance(getBaseContext());
            magazineInfo.getArticleList(dm, new DataFoundListener<ArrayList<ArticleInfo>>() {
                @Override
                public void onSuccess(ArrayList<ArticleInfo> articleList) {
                    Intent intent = new Intent(MainActivity.this, ArticleActivity.class);
                    intent.putExtra(ARTICLE_LIST, articleList);  // pass articleList
                    startActivity(intent);
                }
            });


        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_main_refresh) {
            refreshMagazines();
        } else if (id == R.id.action_main_user) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

}
