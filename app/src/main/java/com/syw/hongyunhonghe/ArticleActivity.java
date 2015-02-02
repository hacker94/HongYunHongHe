package com.syw.hongyunhonghe;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.syw.hongyunhonghe.model.ArticleInfo;
import com.syw.hongyunhonghe.model.DataFoundListener;
import com.syw.hongyunhonghe.model.DataModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import cn.bmob.v3.datatype.BmobFile;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;


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

    public final DataModel dm = DataModel.getInstance(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

        // get articles
        ArrayList<ArticleInfo> articleList = (ArrayList<ArticleInfo>)getIntent().getSerializableExtra(MainActivity.ARTICLE_LIST);

        mViewPager = (ViewPager) findViewById(R.id.pager);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager(), articleList);

        // Set up the ViewPager with the sections adapter.
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // if page is selected
        Intent intent = getIntent();
        if (intent != null) {
            mViewPager.setCurrentItem(intent.getIntExtra(PICKED_ARTICLE, 0));
        }

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
        private static final String ARG_ARTICLE_INFO = "article_info";
        private static final String ARG_ARTICLE_LIST = "article_list";

        private ArticleInfo articleInfo;
        private ArrayList<ArticleInfo> articleList;

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static ArticleFragment newInstance(int sectionNumber, ArticleInfo articleInfo, ArrayList<ArticleInfo> articleList) {

            ArticleFragment fragment = new ArticleFragment();

            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            args.putSerializable(ARG_ARTICLE_INFO, articleInfo);
            args.putSerializable(ARG_ARTICLE_LIST, articleList);
            fragment.setArguments(args);
            return fragment;
        }

        public ArticleFragment() {
        }



        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            final View rootView = inflater.inflate(R.layout.fragment_article, container, false);

            // get articles from bundle
            articleInfo = (ArticleInfo)this.getArguments().getSerializable(ARG_ARTICLE_INFO);
            articleList = (ArrayList<ArticleInfo>)this.getArguments().getSerializable(ARG_ARTICLE_LIST);

            // get content LinearLayout
            final LinearLayout contentLL = (LinearLayout)rootView.findViewById(R.id.article_content_layout);

            // add title, subtitle, author text
            TextView titleTextView = new TextView(getActivity());
            titleTextView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            titleTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.article_title_text_size));
            titleTextView.setTextColor(getResources().getColor(R.color.red));
            titleTextView.setText(articleInfo.getTitle());

            TextView subtitleTextView = new TextView(getActivity());
            subtitleTextView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            subtitleTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.article_subtitle_text_size));
            subtitleTextView.setText(articleInfo.getSubtitle());

            TextView authorTextView = new TextView(getActivity());
            authorTextView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            authorTextView.setGravity(Gravity.RIGHT);
            authorTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.article_author_text_size));
            authorTextView.setTextColor(getResources().getColor(R.color.gray));
            authorTextView.setText(articleInfo.getAuthor());

            if (articleInfo.isSwapTitlePos()) {
                contentLL.addView(subtitleTextView);
                contentLL.addView(titleTextView);
            } else {
                contentLL.addView(titleTextView);
                contentLL.addView(subtitleTextView);
            }
            contentLL.addView(authorTextView);

            // add a line
            View ruler = new View(getActivity());
            ruler.setBackgroundColor(getResources().getColor(R.color.article_title_content_line));
            contentLL.addView(ruler, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 3));

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
                            try {
                                imgFile.loadImage(getActivity(), imageView);
                            } catch (NullPointerException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFail(BmobFile object) {

                        }
                    });

                } else {
                    // get part of article
                    String part = articleInfo.getNextPart();

                    // create a text view
                    TextView textView = new TextView(getActivity());
                    textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.article_text_size));
                    textView.setLineSpacing(0, (float)1.4); // height = height * 1.4 + 0
                    int padding = getResources().getDimensionPixelSize(R.dimen.article_text_padding);
                    textView.setPadding(0, padding, 0, padding);
                    textView.setText(part);

                    // add to contentLL
                    contentLL.addView(textView);
                }
            }


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


            //set add review EditText
            final EditText editText = (EditText)rootView.findViewById(R.id.review_edit_text);
            editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int keyCode, KeyEvent event) {
                    if (keyCode == EditorInfo.IME_ACTION_SEARCH ||
                            keyCode == EditorInfo.IME_ACTION_DONE ||
                            event.getAction() == KeyEvent.ACTION_DOWN &&
                            event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                        if (!editText.getText().toString().equals("")) {
                            DataModel dm = DataModel.getInstance(getActivity());
                            dm.addReview(articleInfo, editText.getText().toString(), new DataFoundListener<String>() {
                                @Override
                                public void onSuccess(String msg) {
                                    Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                                    editText.setText("");
                                }

                                @Override
                                public void onFail(String msg) {
                                    Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                        return true;
                    }
                    return false;
                }
            });


            // set reviews button
            ImageButton reviewsButton = (ImageButton)rootView.findViewById(R.id.article_reviews_button);
            reviewsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ReviewsActivity.class);
                    intent.putExtra(PICKED_ARTICLE, articleInfo);
                    startActivity(intent);
                }
            });


            // set favourite button
            final ImageButton favButton = (ImageButton)rootView.findViewById(R.id.article_favourite_button);

            DataModel dm = DataModel.getInstance(getActivity());
            dm.isFavArticle(articleInfo.getArticle(), new DataFoundListener<Boolean>() {
                @Override
                public void onSuccess(Boolean object) {
                    favButton.setImageResource(R.drawable.ic_action_favorite_red);
                }

                @Override
                public void onFail(Boolean object) {
                    favButton.setImageResource(R.drawable.ic_action_favorite);
                }
            });

            favButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final DataModel dm = DataModel.getInstance(getActivity());
                    dm.isFavArticle(articleInfo.getArticle(), new DataFoundListener<Boolean>() {
                        @Override
                        public void onSuccess(Boolean object) {
                            dm.removeFavArticle(articleInfo, new DataFoundListener<String>() {
                                @Override
                                public void onSuccess(String msg) {
                                    Toast.makeText(getActivity(), "取消收藏成功", Toast.LENGTH_SHORT).show();
                                    favButton.setImageResource(R.drawable.ic_action_favorite);
                                }

                                @Override
                                public void onFail(String msg) {
                                    Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                        @Override
                        public void onFail(Boolean object) {
                            dm.addFavArticle(articleInfo, new DataFoundListener<String>() {
                                @Override
                                public void onSuccess(String msg) {
                                    Toast.makeText(getActivity(), "收藏成功", Toast.LENGTH_SHORT).show();
                                    favButton.setImageResource(R.drawable.ic_action_favorite_red);
                                }

                                @Override
                                public void onFail(String msg) {
                                    Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
                                }
                            });

                        }
                    });

                }
            });


            // set share button
            ImageButton shareButton = (ImageButton)rootView.findViewById(R.id.article_share_button);
            shareButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OnekeyShare oks = new OnekeyShare();
                    // 关闭sso授权
                    oks.disableSSOWhenAuthorize();
                    // 分享时Notification的图标和文字
                    oks.setNotification(R.drawable.app_icon, getString(R.string.app_name));
                    // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
                    oks.setTitle("今日红云红河");
                    // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
                    oks.setTitleUrl("http://www.hyhhgroup.com/htmlnew/index.php");
                    // text是分享文本，所有平台都需要这个字段
                    String text = articleInfo.getTitle();
                    if (articleInfo.isSwapTitlePos()) {
                        text = articleInfo.getSubtitle() + " " + text;
                    } else {
                        text += " " + articleInfo.getSubtitle();
                    }
                    oks.setText(text);
                    // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
                    Bitmap screenShot = loadBitmapFromView(contentLL);
                    try {
                        File file = File.createTempFile("screen_shot", ".jpg", getActivity().getCacheDir());
                        FileOutputStream out = new FileOutputStream(file);
                        screenShot.compress(Bitmap.CompressFormat.JPEG, 50, out);
                        out.close();
                        oks.setImagePath(file.getAbsolutePath());//确保SDcard下面存在此张图片
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    // site是分享此内容的网站名称，仅在QQ空间使用
                    oks.setSite(getString(R.string.app_name));
                    // siteUrl是分享此内容的网站地址，仅在QQ空间使用
                    oks.setSiteUrl("http://www.hyhhgroup.com/htmlnew/index.php");
                    // 启动分享GUI
                    oks.show(getActivity());
                }
            });

            return rootView;
        }

        // load bmp from view
        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        public Bitmap loadBitmapFromView(View v) {
            Bitmap b = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas c = new Canvas(b);

            Drawable old = v.getBackground(); // save original background
            // set background color to while and draw the view
            v.setBackgroundColor(getResources().getColor(R.color.white));
            v.draw(c);
            // restore original background
            int sdk = android.os.Build.VERSION.SDK_INT;
            if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                v.setBackgroundDrawable(old);
            } else {
                v.setBackground(old);
            }
            return b;
        }


        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);

            if (requestCode == PICK_ARTICLE_REQUEST) {
                if (resultCode == RESULT_OK) {
                    ViewPager mViewPager = (ViewPager)getActivity().findViewById(R.id.pager);
                    mViewPager.setCurrentItem(data.getIntExtra(PICKED_ARTICLE, 0));
                }
            }
        }

    }

}
