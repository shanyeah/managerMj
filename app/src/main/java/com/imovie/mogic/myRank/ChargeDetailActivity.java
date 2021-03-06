package com.imovie.mogic.myRank;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.imovie.mogic.R;
import com.imovie.mogic.ScanPay.net.PayWebHelper;
import com.imovie.mogic.base.universal_loading.YSBLoadingDialog;
import com.imovie.mogic.car.CarPayDetailActivity;
import com.imovie.mogic.home.BaseActivity;
import com.imovie.mogic.home.model.CardModel;
import com.imovie.mogic.home.model.ChargeSuccessModel;
import com.imovie.mogic.home.model.PayDetailModel;
import com.imovie.mogic.home.model.SearchUserModel;
import com.imovie.mogic.home.net.HomeWebHelper;
import com.imovie.mogic.login.model.LoginModel;
import com.imovie.mogic.myRank.model.ChargeOrderMode;
import com.imovie.mogic.utills.DecimalUtil;
import com.imovie.mogic.utills.Utills;
import com.imovie.mogic.web.IModelResultListener;
import com.imovie.mogic.web.model.HttpResultModel;
import com.imovie.mogic.widget.FlexibleFrameLayout;
import com.imovie.mogic.widget.PullToRefreshFrameLayout;
import com.imovie.mogic.widget.TitleBar;

import java.lang.ref.WeakReference;
import java.util.List;

public class ChargeDetailActivity extends BaseActivity {

    public static final int MSG_REFRESH = 600;
    public static final int MSG_WXSEND = 601;
    
    private CardModel cardModel;
    private LoginModel loginModel = new LoginModel();

    private TitleBar titleBar;
    private PullToRefreshFrameLayout pull_content;
    private FlexibleFrameLayout ff_list;
    private TextView tvName;
    private TextView tvMemberMobile;
    private TextView tv_member_number;
    private TextView tvCashBalance;
    private TextView tvChargeCount;
    private TextView tvPrecentNum;
    private TextView tv_order_number;
    private TextView  tvPayType;
    private TextView  tvPayTime;
    private TextView  tvPayState;

    public String data = "";
    public ChargeSuccessModel userModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.charge_detail_activity);
        initView();
        setView();
        initListener();
        try {
            queryRechangeDetail(getIntent().getStringExtra("saleBillId"));
        } catch (Exception e) {
            e.printStackTrace();
        }

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
        titleBar.setTitle("充值订单详情");
        titleBar.setLeftListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        pull_content = (PullToRefreshFrameLayout) findViewById(R.id.pull_content);
        ff_list = (FlexibleFrameLayout) findViewById(R.id.ff_list);
        tvName = (TextView) findViewById(R.id.tvName);
        tvMemberMobile = (TextView) findViewById(R.id.tvMemberMobile);
        tv_member_number = (TextView) findViewById(R.id.tv_member_number);
        tvCashBalance = (TextView) findViewById(R.id.tvCashBalance);
        tvChargeCount = (TextView) findViewById(R.id.tvChargeCount);
        tvPrecentNum = (TextView) findViewById(R.id.tvPrecentNum);
        tv_order_number = (TextView) findViewById(R.id.tv_order_number);
        tvPayType = (TextView) findViewById(R.id.tvPayType);
        tvPayTime = (TextView) findViewById(R.id.tvPayTime);
        tvPayState = (TextView) findViewById(R.id.tvPayState);
//        LinearLayout.LayoutParams ivParam=(LinearLayout.LayoutParams)cardTop.getLayoutParams();
//        int screenWidth = getResources().getDisplayMetrics().widthPixels;
//        ivParam.width = (screenWidth*576)/746;
//        ivParam.height =( screenWidth*365)/746;
//        cardTop.setLayoutParams(ivParam);
    }

    private void setView(){
        setPullAndFlexListener();

    }

    private void setPullAndFlexListener(){
//		ff_list.setFlexView(ll_ad);
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
                queryRechangeDetail(getIntent().getStringExtra("saleBillId"));
            }
        });


    }

    private void initListener() {
//        btChargeScan.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                ScanPayManager.enterCaptureActivity(ChargeSuccessActivity.this,data,userModel);
////                getPreqrCharge(userModel.chargeNum +"00",userModel.userId);
//            }
//        });
//
//        btChargeReset.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                getScanReset();
//            }
//        });

    }

    public void queryRechangeDetail(String saleBillId){
//        YSBLoadingDialog.showLoadingDialog(ChargeDetailActivity.this, 2000, new YSBLoadingDialog.OnCancelListener() {
//            @Override
//            public void onTimeout() {
//                YSBLoadingDialog.dismissDialog();
//            }
//
//            @Override
//            public void onCancel() {
//                YSBLoadingDialog.dismissDialog();
//            }
//        });
        HomeWebHelper.queryRechangeDetail(saleBillId,new IModelResultListener<ChargeOrderMode>() {
            @Override
            public boolean onGetResultModel(HttpResultModel resultModel) {
                pull_content.endRefresh(true);
                return false;
            }

            @Override
            public void onSuccess(String resultCode, ChargeOrderMode resultModel, List<ChargeOrderMode> resultModelList, String resultMsg, String hint) {
                pull_content.endRefresh(true);
                if(resultCode.equals("0")) {
//                    tvMemberNumber.setText(resultModel.idNumber);
//                    tvCardCategoryName.setText(userModel.name);
//                    tvCashBalance.setText(Html.fromHtml("<font color=\'#fd5c02\' size=16>"+ DecimalUtil.FormatMoney(userModel.afterBalance) + "</font><font color=\'#565a5c\' size=16>"+getResources().getString(R.string.symbol_RMB)+"</font>"));
//                    tvGiveSum.setText(Html.fromHtml("<font color=\'#fd5c02\' size=16>"+ DecimalUtil.FormatMoney(userModel.afterCashBalance) + "</font><font color=\'#565a5c\' size=16>"+getResources().getString(R.string.symbol_RMB)+"</font>"));
//                    tvChargeNum.setText(Html.fromHtml("<font color=\'#fd5c02\' size=16>"+ DecimalUtil.FormatMoney(userModel.afterPresentBalance) + "</font><font color=\'#565a5c\' size=16>"+getResources().getString(R.string.symbol_RMB)+"</font>"));
                    tvName.setText(resultModel.userWallet.userName);
                    tvMemberMobile.setText(resultModel.userWallet.mobile);
                    tv_member_number.setText(resultModel.userWallet.idNumber);
                    tvCashBalance.setText(Html.fromHtml("<font color=\'#fd5c02\' size=16>"+ DecimalUtil.FormatMoney(resultModel.userWallet.afterBalance) + "</font><font color=\'#565a5c\' size=16>"+getResources().getString(R.string.symbol_RMB)+"</font>"));
                    tvChargeCount.setText(Html.fromHtml("<font color=\'#fd5c02\' size=16>"+ DecimalUtil.FormatMoney(resultModel.userWallet.cashAmount) + "</font><font color=\'#565a5c\' size=16>"+getResources().getString(R.string.symbol_RMB)+"</font>"));
                    tvPrecentNum.setText(Html.fromHtml("<font color=\'#fd5c02\' size=16>"+ DecimalUtil.FormatMoney(resultModel.userWallet.presentAmount) + "</font><font color=\'#565a5c\' size=16>"+getResources().getString(R.string.symbol_RMB)+"</font>"));
                    tv_order_number.setText(resultModel.orderNo);
                    tvPayType.setText(resultModel.payInfo);
                    tvPayTime.setText(resultModel.createTime);
                    if(resultModel.type==0) {
                        tvPayState.setText("成功");
                    }else{
                        tvPayState.setText("退款");
                    }

                }else{
                    Utills.showShortToast(resultMsg);
                }
            }

            @Override
            public void onFail(String resultCode, String resultMsg, String hint) {
                pull_content.endRefresh(true);
            }

            @Override
            public void onError(String errorMsg) {
                pull_content.endRefresh(true);
            }
        });
    }




    private final UIHandler uiHandler = new UIHandler(this);
    private static class UIHandler extends Handler {
        private final WeakReference<ChargeDetailActivity> activity;
        public UIHandler(ChargeDetailActivity act) {
            super();
            activity = new WeakReference<ChargeDetailActivity>(act);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_REFRESH:
//                    activity.get().queryWechatOrder(activity.get().payOrderModel.orderNo);
                    break;
                case MSG_WXSEND:

                    break;
                default:
                    break;
            }
        }
    };

}

