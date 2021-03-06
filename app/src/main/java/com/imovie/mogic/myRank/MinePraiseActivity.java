package com.imovie.mogic.myRank;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.imovie.mogic.R;
import com.imovie.mogic.home.BaseActivity;
import com.imovie.mogic.myRank.fragment.PraiseFragment;
import com.imovie.mogic.myRank.fragment.PraiseNumFragment;
import com.imovie.mogic.myRank.fragment.PraiseRecordFragment;
import com.imovie.mogic.myRank.widget.PagerSlidingTabStrip;
import com.imovie.mogic.widget.FlexibleFrameLayout;
import com.imovie.mogic.widget.PullToRefreshFrameLayout;
import com.imovie.mogic.widget.TitleBar;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MinePraiseActivity extends BaseActivity {

    public static final int MSG_REFRESH = 600;
    public static final int MSG_WXSEND = 601;

    private final List<String> titles = Arrays.asList("我的点赞","引导点赞");
    private List<Fragment> mFragments = new ArrayList<>();
    public FragmentPagerAdapter adapter;
    PraiseRecordFragment praiseRecordFragment;
    public PraiseNumFragment praiseFragment;

    private TitleBar titleBar;
    private PagerSlidingTabStrip pstTabTitle;
    private ViewPager mViewPager;

    private int stgId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mine_rank_activity);
        initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void initView(){
        stgId = getIntent().getIntExtra("stgId",0);
        titleBar = (TitleBar) findViewById(R.id.title_bar);
        titleBar.setTitle("我的点赞");

        pstTabTitle = (PagerSlidingTabStrip) findViewById(R.id.pst_hall_tabTitle);
        mViewPager = (ViewPager) findViewById(R.id.vpHallPager);


        titleBar.setLeftListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        fixUI();
        praiseRecordFragment = new PraiseRecordFragment();
        praiseFragment = new PraiseNumFragment();
        praiseFragment.refreshData(1);
        mFragments.add(praiseRecordFragment);
        mFragments.add(praiseFragment);

//        mFragments.add(new ChangeFragment());
//        mFragments.add(new MissionFragment());
        adapter = new FragmentPagerAdapter(getSupportFragmentManager()) {

            @Override
            public Fragment getItem(int i) {
                return mFragments.get(i);
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return titles.get(position);
            }

            @Override
            public int getCount() {
                return mFragments.size();
            }

            @Override
            public int getItemPosition(Object object) {
                return PagerAdapter.POSITION_NONE;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                return super.instantiateItem(container, position);
            }
        };
        mViewPager.setAdapter(adapter);
        mViewPager.setOffscreenPageLimit(2);
        pstTabTitle.setViewPager(mViewPager);
        mViewPager.setCurrentItem(0);
    }


    private void fixUI() {
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        LinearLayout.LayoutParams tabtitleParams = (LinearLayout.LayoutParams) pstTabTitle.getLayoutParams();
        tabtitleParams.height = 70 * screenWidth/640;
        pstTabTitle.setLayoutParams(tabtitleParams);
        pstTabTitle.setTextSize(16);
    }


}


