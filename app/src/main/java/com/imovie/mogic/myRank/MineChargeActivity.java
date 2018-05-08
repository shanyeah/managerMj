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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.imovie.mogic.R;
import com.imovie.mogic.home.BaseActivity;
import com.imovie.mogic.home.net.HomeWebHelper;
import com.imovie.mogic.login.model.MyDataModel;
import com.imovie.mogic.myRank.fragment.ChargeRecordFragment;
import com.imovie.mogic.myRank.fragment.PraiseNumFragment;
import com.imovie.mogic.myRank.widget.PagerSlidingTabStrip;
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

public class MineChargeActivity extends BaseActivity {

    public static final int MSG_REFRESH = 600;
    public static final int MSG_WXSEND = 601;

    private final List<String> titles = Arrays.asList("引导充值记录","每日引导充值");
    private List<Fragment> mFragments = new ArrayList<>();
    public FragmentPagerAdapter adapter;
    public ChargeRecordFragment chargeFragment;
    public PraiseNumFragment praiseFragment;

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

        getBusinessDetail(0);
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
        titleBar.setTitle("我的引导充值");

        tvNickname = (TextView) findViewById(R.id.tvNickname);
        tvChargeMonth = (TextView) findViewById(R.id.tvChargeMonth);
        tvChargeRewardAmount = (TextView) findViewById(R.id.tvChargeRewardAmount);
        tvChargeRanking = (TextView) findViewById(R.id.tvChargeRanking);
        ivChargeHeader = (ImageView) findViewById(R.id.ivChargeHeader);

        pstTabTitle = (PagerSlidingTabStrip) findViewById(R.id.pst_hall_tabTitle);
        mViewPager = (ViewPager) findViewById(R.id.vpHallPager);


        titleBar.setLeftListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        fixUI();
        praiseFragment = new PraiseNumFragment();
        praiseFragment.refreshData(3);
        chargeFragment = new ChargeRecordFragment();
        mFragments.add(chargeFragment);
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

    public void getBusinessDetail(int type){
        HomeWebHelper.getBusinessDetail(type,new IModelResultListener<MyDataModel>() {
            @Override
            public boolean onGetResultModel(HttpResultModel resultModel) {
//                pull_content.endRefresh(true);
                return false;
            }

            @Override
            public void onSuccess(String resultCode, MyDataModel resultModel, List<MyDataModel> resultModelList, String resultMsg, String hint) {
//                pull_content.endRefresh(true);
                if(resultCode.equals("0")) {
//                    tvNickname.setText(resultModel.nickName);
//                    tvChargeMonth.setText(loginModel.card.cardCategoryName);
//                    tvChargeRewardAmount.setText(loginModel.card.cardCategoryName);
//                    tvChargeRanking.setText(loginModel.card.cardCategoryName);
//                    ImageLoader.getInstance().displayImage(resultModel.fackeImageUrl,ivChargeHeader,mOption);
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


