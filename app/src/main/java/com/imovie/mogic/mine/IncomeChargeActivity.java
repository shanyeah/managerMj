package com.imovie.mogic.mine;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.LinearLayout;

import com.imovie.mogic.R;
import com.imovie.mogic.home.BaseActivity;
import com.imovie.mogic.mine.adapter.IncomeChargeAdapter;
import com.imovie.mogic.mine.model.ChargeInfo;
import com.imovie.mogic.mine.model.ChargeInfoModel;
import com.imovie.mogic.mine.net.MineWebHelper;
import com.imovie.mogic.utills.DateUtil;
import com.imovie.mogic.web.IModelResultListener;
import com.imovie.mogic.web.model.HttpResultModel;
import com.imovie.mogic.widget.FlexibleFrameLayout;
import com.imovie.mogic.widget.PullToRefreshFrameLayout;
import com.imovie.mogic.widget.TitleBar;
import com.imovie.mogic.widget.YSBPageListView;
import com.imovie.mogic.widget.interfaces.IPageList;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class IncomeChargeActivity extends BaseActivity {

    public static final int MSG_CHARGE = 21;  //充值收入
    public static final int MSG_PRAISE = 22;  //点赞收入
    public static final int MSG_ORDER = 23;   //点餐收入

    private TitleBar titleBar;
    private PullToRefreshFrameLayout pull_content;
    private FlexibleFrameLayout ff_list;
    private LinearLayout ll_ad;
    private YSBPageListView lvChargeList;
    public List<ChargeInfo> list;
    public IncomeChargeAdapter adapter;
    public int fromType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.income_charge_activity);
        initView();
        setView();
//        initListener();
        refresh();

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

        titleBar = (TitleBar) findViewById(R.id.title_bar);
        fromType = getIntent().getIntExtra("fromType",21);
        if(fromType == MSG_CHARGE){
           titleBar.setTitle("充值收入");
        }else if(fromType == MSG_PRAISE){
            titleBar.setTitle("点赞收入");
        }else if(fromType == MSG_ORDER){
            titleBar.setTitle("点餐收入");
        }

        pull_content = (PullToRefreshFrameLayout) findViewById(R.id.pull_content);
        ff_list = (FlexibleFrameLayout) findViewById(R.id.ff_list);
        ll_ad = (LinearLayout) findViewById(R.id.ll_ad);
        lvChargeList = (YSBPageListView) findViewById(R.id.lvChargeList);

        titleBar.setLeftListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void setView(){
        setPullAndFlexListener();
        list = new ArrayList<>();
        adapter = new IncomeChargeAdapter(this,list,fromType);
        lvChargeList.setAdapter(adapter);
        lvChargeList.setEmptyTips("暂无数据");
    }



    private void setPullAndFlexListener(){
        ff_list.setFlexView(ll_ad);
        ff_list.setFlexible(true);
        ff_list.setOnFlexChangeListener(new FlexibleFrameLayout.OnFlexChangeListener() {
            @Override
            public void onFlexChange(int flexHeight, int currentFlexHeight, boolean isOnTop) {
                if (isOnTop) {
                    pull_content.setPullEnable(true);
                } else {
                    pull_content.setPullEnable(false);
                }
            }

        });

        lvChargeList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == SCROLL_STATE_IDLE) {

                    //列表处于最上方
                    if (true && view.getChildAt(0).getTop() == 0) {
                        ff_list.setFlexible(true);
                    } else {
                        ff_list.setFlexible(false);
                    }
                }

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
        pull_content.setOnPullToRefreshListener(new PullToRefreshFrameLayout.OnPullToRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
        lvChargeList.setOnPageListener(new IPageList.OnPageListener() {
            @Override
            public void onLoadMoreItems() {
                getChargeInfo("2017-01-01",DateUtil.TimeFormat(DateUtil.getCurrentTime(),"yyyy-MM-dd"));
            }
        });

    }

    private void initListener() {

    }

    public void refresh(){
        adapter.list.clear();
        adapter.notifyDataSetChanged();
        lvChargeList.setHaveMoreData(true);
        lvChargeList.startLoad();
    }

    private void getChargeInfo(String startTime,String endTime){
        int page = adapter.list.size()/lvChargeList.getPageSize() + 1;
        MineWebHelper.getChargeInfo(page,lvChargeList.getPageSize(),startTime,endTime,fromType,new IModelResultListener<ChargeInfoModel>() {
            @Override
            public boolean onGetResultModel(HttpResultModel resultModel) {
                pull_content.endRefresh(true);
                lvChargeList.finishLoading(true);
                return false;
            }

            @Override
            public void onSuccess(String resultCode, ChargeInfoModel resultModel, List<ChargeInfoModel> resultModelList, String resultMsg, String hint) {

                    pull_content.endRefresh(true);
                    if(resultCode.equals("0")){
                        try {
                            if(resultModel.pageData.list.size()>0){
                                adapter.list.addAll(resultModel.pageData.list);
                                adapter.notifyDataSetChanged();
                            }

                            lvChargeList.finishLoading(lvChargeList.getPageSize() == resultModel.pageData.list.size());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

            }

            @Override
            public void onFail(String resultCode, String resultMsg, String hint) {
                pull_content.endRefresh(true);
                lvChargeList.finishLoading(true);
            }

            @Override
            public void onError(String errorMsg) {
                pull_content.endRefresh(true);
                lvChargeList.finishLoading(true);
            }
        });
    }


}


