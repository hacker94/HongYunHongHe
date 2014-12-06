package com.syw.hongyunhonghe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.syw.hongyunhonghe.model.ArticleInfo;

import java.util.ArrayList;


public class ArticleListActivity extends Activity {

    private LinearLayout articlesLL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_list);

        // find views
        articlesLL = (LinearLayout)findViewById(R.id.articles_linear_layout);

        // get article list from intent
        Intent intent = getIntent();
        ArrayList<ArticleInfo> articleList = (ArrayList<ArticleInfo>)intent.getSerializableExtra(MainActivity.ARTICLE_LIST);

        // generate article rows and add to articlesLL
        for (ArticleInfo articleInfo : articleList) {
            // inflate
            LayoutInflater layoutInflater = getLayoutInflater();
            final LinearLayout rowLayout = (LinearLayout)layoutInflater.inflate(R.layout.article_list_row, articlesLL, false);

            // set tag
            rowLayout.setTag(articleInfo.getNumber());

            // set text
            TextView titleTextView = (TextView)rowLayout.findViewById(R.id.article_title_text_view);
            TextView numberTextView = (TextView)rowLayout.findViewById(R.id.article_number_text_view);
            titleTextView.setText(articleInfo.getTitle());
            numberTextView.setText('(' + Integer.toString(articleInfo.getNumber()) + ')');

            // set onClick
            rowLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra(ArticleActivity.PICKED_ARTICLE, (Integer)rowLayout.getTag());
                    setResult(RESULT_OK, returnIntent);
                    finish();
                }
            });
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_article_list, menu);
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
}
