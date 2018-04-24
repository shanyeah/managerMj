package com.imovie.mogic.mine;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.imovie.mogic.R;
import com.imovie.mogic.home.BaseActivity;
import com.imovie.mogic.mine.adapter.IncomeChargeAdapter;
import com.imovie.mogic.mine.model.ChargeInfoModel;
import com.imovie.mogic.widget.FlexibleFrameLayout;
import com.imovie.mogic.widget.PullToRefreshFrameLayout;
import com.imovie.mogic.widget.TitleBar;
import com.imovie.mogic.widget.YSBPageListView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class MyMovieActivity extends BaseActivity {

    public static final int MSG_REFRESH = 600;
    public static final int MSG_WXSEND = 601;

    private TitleBar titleBar;
    private PullToRefreshFrameLayout pull_content;
    private FlexibleFrameLayout ff_list;
    private YSBPageListView lvMyMovieList;
    public List<ChargeInfoModel> list;
    public IncomeChargeAdapter mymovieAdapter;
    public TextView tvNoMyMovie;
    private boolean isHaveAD = false;
    private LinearLayout ll_ad;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.income_charge_activity);
        initView();
        setView();
        initListener();
        getMyMovie();

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
        titleBar.setTitle("我的电影");

        pull_content = (PullToRefreshFrameLayout) findViewById(R.id.pull_content);
        ff_list = (FlexibleFrameLayout) findViewById(R.id.ff_list);
        ll_ad = (LinearLayout) findViewById(R.id.ll_ad);
        lvMyMovieList = (YSBPageListView) findViewById(R.id.lvMyMovieList);

        titleBar.setLeftListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void setView(){
//        list = new ArrayList<>();
//        mymovieAdapter = new IncomeChargeAdapter(MyMovieActivity.this,list);
//        lvMyMovieList.setAdapter(mymovieAdapter);
        setPullAndFlexListener();
    }



    private void setPullAndFlexListener(){
//        ff_list.setFlexView(ll_ad);
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
        pull_content.setOnPullToRefreshListener(new PullToRefreshFrameLayout.OnPullToRefreshListener() {
            @Override
            public void onRefresh() {
//				llFilter.reset();
//                reFresh();

                getMyMovie();
            }
        });

        lvMyMovieList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
//                Utills.showShortToast(""+scrollState);
//                if (scrollState == SCROLL_STATE_IDLE) {

                    //列表处于最上方
                    if (view.getChildAt(0).getTop() == 0) {
//                        ff_list.setFlexible(true);
                        pull_content.setPullEnable(true);

                    } else {
//                        ff_list.setFlexible(false);
                        pull_content.setPullEnable(false);
                    }
//                }

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

    }

    private void initListener() {
//        ivHallTop.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                getWechatId("魔杰电竞"+cardModel.name+"购买","魔杰电竞"+cardModel.name+"购买",DecimalUtil.FormatMoney(cardModel.activeAmount));
//            }
//        });

    }

    private void getMyMovie(){
//        MineWebHelper.getChargeInfo(new IModelResultListener<ChargeInfoModel>() {
//            @Override
//            public boolean onGetResultModel(HttpResultModel resultModel) {
//                return false;
//            }
//
//            @Override
//            public void onSuccess(String resultCode, ChargeInfoModel resultModel, List<ChargeInfoModel> resultModelList, String resultMsg, String hint) {
//
//                try {
////                    Log.e("----movie",""+resultCode);
//                    pull_content.endRefresh(true);
//                    if(resultModelList.size()>0){
//                        mymovieAdapter.list = resultModelList;
//                        mymovieAdapter.notifyDataSetChanged();
//                    }else{
//                    }
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFail(String resultCode, String resultMsg, String hint) {
//            }
//
//            @Override
//            public void onError(String errorMsg) {
//            }
//        });
    }



    private final UIHandler uiHandler = new UIHandler(this);
    private static class UIHandler extends Handler {
        private final WeakReference<MyMovieActivity> activity;
        public UIHandler(MyMovieActivity act) {
            super();
            activity = new WeakReference<MyMovieActivity>(act);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_REFRESH:
//                    activity.get().queryWechatOrder(activity.get().wechatIDModel.orderNo);
                    break;
                case MSG_WXSEND:
//                    PayReq req = new PayReq();
//                    req.appId = activity.get().wechatIDModel.appid;
//                    req.partnerId = activity.get().wechatIDModel.partnerid;
//                    req.prepayId = activity.get().wechatIDModel.prepayId;
//                    req.nonceStr = activity.get().wechatIDModel.noncestr;
//                    req.timeStamp = activity.get().wechatIDModel.timestamp;
//                    req.packageValue = activity.get().wechatIDModel.packageStr;
//                    req.sign = activity.get().wechatIDModel.sign;
//                    req.extData = "魔杰电竞"+activity.get().cardModel.name+"消费";
//                    Toast.makeText(activity.get(), "正常调起支付", Toast.LENGTH_SHORT).show();
//                    // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
//                    activity.get().api.sendReq(req);
                    break;
                default:
                    break;
            }
        }
    };

}


