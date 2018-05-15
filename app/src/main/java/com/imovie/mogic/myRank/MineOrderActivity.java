package com.imovie.mogic.myRank;

import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.imovie.mogic.MyApplication;
import com.imovie.mogic.R;
import com.imovie.mogic.home.BaseActivity;
import com.imovie.mogic.home.net.HomeWebHelper;
import com.imovie.mogic.myRank.fragment.OrderRecordFragment;
import com.imovie.mogic.myRank.fragment.PraiseFragment;
import com.imovie.mogic.myRank.fragment.PraiseNumFragment;
import com.imovie.mogic.myRank.model.BusinessDetailMode;
import com.imovie.mogic.myRank.widget.PagerSlidingTabStrip;
import com.imovie.mogic.utills.DecimalUtil;
import com.imovie.mogic.web.IModelResultListener;
import com.imovie.mogic.web.model.HttpResultModel;
import com.imovie.mogic.widget.FlexibleFrameLayout;
import com.imovie.mogic.widget.PullToRefreshFrameLayout;
import com.imovie.mogic.widget.TitleBar;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MineOrderActivity extends BaseActivity {

    public static final int MSG_REFRESH = 600;
    public static final int MSG_WXSEND = 601;

    private final List<String> titles = Arrays.asList("每日收款","收款记录");
    private List<Fragment> mFragments = new ArrayList<>();
    public FragmentPagerAdapter adapter;
    public PraiseNumFragment praiseFragment;
    public OrderRecordFragment recordFragment;

    private TitleBar titleBar;
    private PagerSlidingTabStrip pstTabTitle;
    private ViewPager mViewPager;

    private int stgId;
    private DisplayImageOptions mOption;
    public TextView tvNickname;
    public TextView tvChargeMonth;
    public TextView tvChargeRewardAmount;
    public TextView tvChargeRanking;
    public ImageView ivChargeHeader;
    public RelativeLayout rlChargeRanking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mine_order_list_activity);
        initView();
        mOption = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.person_default_profile)
                .showImageOnFail(R.drawable.person_default_profile)
                .showImageForEmptyUri(R.drawable.person_default_profile)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        getBusinessDetail(2);
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
        titleBar.setTitle("我的点餐");

        tvNickname = (TextView) findViewById(R.id.tvNickname);
        tvChargeMonth = (TextView) findViewById(R.id.tvChargeMonth);
        tvChargeRewardAmount = (TextView) findViewById(R.id.tvChargeRewardAmount);
        tvChargeRanking = (TextView) findViewById(R.id.tvChargeRanking);
        ivChargeHeader = (ImageView) findViewById(R.id.ivChargeHeader);
        rlChargeRanking = (RelativeLayout) findViewById(R.id.rlChargeRanking);

        pstTabTitle = (PagerSlidingTabStrip) findViewById(R.id.pst_hall_tabTitle);
        mViewPager = (ViewPager) findViewById(R.id.vpHallPager);


        titleBar.setLeftListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        fixUI();
        recordFragment = new OrderRecordFragment();
        praiseFragment = new PraiseNumFragment();
        mFragments.add(praiseFragment);
        mFragments.add(recordFragment);
        praiseFragment.refreshData(2);

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

        rlChargeRanking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MineOrderActivity.this, MineRankActivity.class);
                intent.putExtra("stgId",21);
                startActivity(intent);
            }
        });
    }

    private void fixUI() {
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        LinearLayout.LayoutParams tabtitleParams = (LinearLayout.LayoutParams) pstTabTitle.getLayoutParams();
        tabtitleParams.height = 70 * screenWidth/640;
        pstTabTitle.setLayoutParams(tabtitleParams);
        pstTabTitle.setTextSize(16);
    }

    public void getBusinessDetail(int type){
        HomeWebHelper.getBusinessDetail(type,new IModelResultListener<BusinessDetailMode>() {
            @Override
            public boolean onGetResultModel(HttpResultModel resultModel) {
//                pull_content.endRefresh(true);
                return false;
            }

            @Override
            public void onSuccess(String resultCode, BusinessDetailMode resultModel, List<BusinessDetailMode> resultModelList, String resultMsg, String hint) {
//                pull_content.endRefresh(true);
                if(resultCode.equals("0")) {
                    tvNickname.setText(MyApplication.getInstance().mPref.getString("nickName",""));
                    tvChargeMonth.setText(DecimalUtil.FormatMoney(resultModel.goodsAmount)+"元");
                    tvChargeRewardAmount.setText(DecimalUtil.FormatMoney(resultModel.goodsRewardAmount)+"元");
                    tvChargeRanking.setText(resultModel.goodsRanking+"/10");
                    ImageLoader.getInstance().displayImage(MyApplication.getInstance().mPref.getString("fackeImageUrl",""),ivChargeHeader,mOption);
                }

            }

            @Override
            public void onFail(String resultCode, String resultMsg, String hint) {
//                pull_content.endRefresh(true);
            }

            @Override
            public void onError(String errorMsg) {
//                pull_content.endRefresh(true);
            }
        });
    }

}


