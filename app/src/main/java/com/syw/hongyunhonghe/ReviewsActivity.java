package com.syw.hongyunhonghe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.syw.hongyunhonghe.model.ArticleInfo;
import com.syw.hongyunhonghe.model.DataFoundListener;
import com.syw.hongyunhonghe.model.DataModel;
import com.syw.hongyunhonghe.model.ReviewInfo;

import java.util.ArrayList;


public class ReviewsActivity extends Activity {

    private LinearLayout reviewsLL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);

        reviewsLL = (LinearLayout)findViewById(R.id.reviews_linear_layout);
        Intent intent = getIntent();
        ArticleInfo articleInfo = (ArticleInfo)intent.getSerializableExtra(ArticleActivity.PICKED_ARTICLE);

        DataModel.getInstance(this).getReview(articleInfo, new DataFoundListener<ArrayList<ReviewInfo>>() {
            @Override
            public void onSuccess(ArrayList<ReviewInfo> reviewList) {
                if (reviewList.size() == 0) {
                    return;
                }
                reviewsLL.removeAllViews();
                for (ReviewInfo reviewInfo : reviewList) {
                    // inflate
                    LayoutInflater layoutInflater = getLayoutInflater();
                    final LinearLayout rowLayout = (LinearLayout) layoutInflater.inflate(R.layout.reviews_row, reviewsLL, false);

                    // set text
                    TextView usernameTextView = (TextView)rowLayout.findViewById(R.id.review_username_text);
                    TextView timeTextView = (TextView)rowLayout.findViewById(R.id.review_time_text);
                    TextView textTextView = (TextView)rowLayout.findViewById(R.id.review_text);
                    usernameTextView.setText(reviewInfo.getUsername());
                    timeTextView.setText(reviewInfo.getCreateTime());
                    textTextView.setText(reviewInfo.getText());

                    reviewsLL.addView(rowLayout);
                }
            }

            @Override
            public void onFail(ArrayList<ReviewInfo> reviewList) {
                Toast.makeText(getBaseContext(), "获取评论失败", Toast.LENGTH_LONG).show();
            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_reviews, menu);
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
