package com.syw.hongyunhonghe;

import java.util.ArrayList;
import java.util.Locale;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v13.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.syw.hongyunhonghe.model.ArticleInfo;
import com.syw.hongyunhonghe.model.DataFoundListener;
import com.syw.hongyunhonghe.model.DataModel;

import cn.bmob.v3.datatype.BmobFile;


public class ArticleActivity extends Activity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v13.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    private static final int PICK_ARTICLE_REQUEST = 0;

    public static final String PICKED_ARTICLE = "com.syw.hongyunhonghe.PICKED_ARTICLE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

        // get articles
        ArrayList<ArticleInfo> articleList = (ArrayList<ArticleInfo>)getIntent().getSerializableExtra(MainActivity.ARTICLE_LIST);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager(), articleList);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_article, menu);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_ARTICLE_REQUEST) {
            if (resultCode == RESULT_OK) {
                mViewPager.setCurrentItem(data.getIntExtra(PICKED_ARTICLE, 0));
            }
        }
    }






    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        private ArrayList<ArticleInfo> arrayList;

        public SectionsPagerAdapter(FragmentManager fm, ArrayList<ArticleInfo> arrayList) {
            super(fm);
            this.arrayList = arrayList;
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return ArticleFragment.newInstance(position, arrayList.get(position), arrayList);
        }

        @Override
        public int getCount() {
            return arrayList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return arrayList.get(position).getTitle();
        }
    }







    /**
     * A placeholder fragment containing a simple view.
     */
    public static class ArticleFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        private ArticleInfo articleInfo;
        private ArrayList<ArticleInfo> articleList;

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static ArticleFragment newInstance(int sectionNumber, ArticleInfo articleInfo, ArrayList<ArticleInfo> articleList) {
            ArticleFragment fragment = new ArticleFragment();
            fragment.articleInfo = articleInfo;
            fragment.articleList = articleList;

            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public ArticleFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_article, container, false);

            // get content LinearLayout
            LinearLayout contentLL = (LinearLayout)rootView.findViewById(R.id.article_content_layout);

            // add content
            articleInfo.reset();
            while (articleInfo.hasNextContent()) {
                if (articleInfo.isNextImg()) {
                    // create a image view
                    final ImageView imageView = new ImageView(getActivity());
                    imageView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    contentLL.addView(imageView);

                    // get img file
                    DataModel dm = DataModel.getInstance(getActivity());
                    articleInfo.getNextImg(dm, new DataFoundListener<BmobFile>() {
                        @Override
                        public void onSuccess(BmobFile imgFile) {
                            imgFile.loadImage(getActivity(), imageView);
                        }
                    });

                } else {
                    // get part of article
                    String part = articleInfo.getNextPart();

                    // create a text view
                    TextView textView = new TextView(getActivity());
                    textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    textView.setText(part);

                    // add to contentLL
                    contentLL.addView(textView);
                }
            }

            // TODO: add comment list


            // set article list button
            ImageButton navButton = (ImageButton)rootView.findViewById(R.id.nav_to_article_list_button);
            navButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ArticleListActivity.class);
                    intent.putExtra(MainActivity.ARTICLE_LIST, articleList);
                    startActivityForResult(intent, PICK_ARTICLE_REQUEST);
                }
            });

            return rootView;
        }

    }

}