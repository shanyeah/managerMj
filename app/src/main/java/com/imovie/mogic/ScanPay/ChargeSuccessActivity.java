package com.imovie.mogic.ScanPay;

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
import com.imovie.mogic.ScanPay.manager.ScanPayManager;
import com.imovie.mogic.ScanPay.net.PayWebHelper;
import com.imovie.mogic.card.net.CardWebHelper;
import com.imovie.mogic.home.BaseActivity;
import com.imovie.mogic.home.model.CardModel;
import com.imovie.mogic.home.model.ChargeSuccessModel;
import com.imovie.mogic.home.model.MyQrCodeModel;
import com.imovie.mogic.home.model.SearchUserModel;
import com.imovie.mogic.login.model.LoginModel;
import com.imovie.mogic.login.model.TestModel;
import com.imovie.mogic.utills.DecimalUtil;
import com.imovie.mogic.utills.Utills;
import com.imovie.mogic.web.IModelResultListener;
import com.imovie.mogic.web.model.HttpResultModel;
import com.imovie.mogic.widget.TitleBar;

import java.lang.ref.WeakReference;
import java.util.List;

public class ChargeSuccessActivity extends BaseActivity {

    public static final int MSG_REFRESH = 600;
    public static final int MSG_WXSEND = 601;
    
    private CardModel cardModel;
    private LoginModel loginModel = new LoginModel();

    private TitleBar titleBar;
    private ImageView ivChargeState;
    private TextView tvChargeState;
    private TextView tvMemberNumber;
    private TextView tvCardCategoryName;
    private TextView tvCashBalance;
    private TextView tvGiveMoneySum;
    private TextView tvChargeNum;
    private TextView tvGiveSum;
    private RelativeLayout rlChargeNumState;
    private Button btChargeReset;
    private Button btChargeScan;

    public String data = "";
    public ChargeSuccessModel userModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.card_charge_success);
        initView();
        setView();
        initListener();



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
        titleBar.setTitle("充值结果");
        titleBar.setLeftListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tvMemberNumber = (TextView) findViewById(R.id.tv_member_number);
        tvCardCategoryName = (TextView) findViewById(R.id.tvCardCategoryName);
        tvCashBalance = (TextView) findViewById(R.id.tvCashBalance);
        tvChargeNum = (TextView) findViewById(R.id.tvChargeNum);
        tvGiveSum = (TextView) findViewById(R.id.tvGiveSum);
        tvGiveMoneySum = (TextView) findViewById(R.id.tvGiveMoneySum);
        ivChargeState = (ImageView) findViewById(R.id.ivChargeState);
        tvChargeState = (TextView) findViewById(R.id.tvChargeState);
        rlChargeNumState = (RelativeLayout) findViewById(R.id.rlChargeNumState);
        btChargeScan = (Button) findViewById(R.id.btChargeScan);
        btChargeReset = (Button) findViewById(R.id.btChargeReset);
//        LinearLayout.LayoutParams ivParam=(LinearLayout.LayoutParams)cardTop.getLayoutParams();
//        int screenWidth = getResources().getDisplayMetrics().widthPixels;
//        ivParam.width = (screenWidth*576)/746;
//        ivParam.height =( screenWidth*365)/746;
//        cardTop.setLayoutParams(ivParam);
    }

    private void setView(){
//        tvPushMessage.setText(Html.fromHtml("<font color='#f92387' size=18>影片《</font><font color=\'#2962FF\' size=20>"+ title +"</font><font color=\'#f92387\' size=18>"+content+"</font>"));
        userModel = (ChargeSuccessModel) getIntent().getSerializableExtra("userModel");
        data = getIntent().getStringExtra("data");
        if(userModel.status == 1){
            ivChargeState.setBackgroundResource(R.drawable.card_charge_success);
            tvChargeState.setText("充值成功");
//            rlChargeNumState.setVisibility(View.GONE);
//            btChargeScan.setVisibility(View.GONE);
//            btChargeReset.setVisibility(View.GONE);
        }else {
//            ivChargeState.setBackgroundResource(R.drawable.card_charge_fail);
//            tvChargeState.setText("充值失败");
//            tvChargeNum.setText(DecimalUtil.FormatMoney(userModel.afterBalance) + getResources().getString(R.string.symbol_RMB));
//            tvChargeNum.setText(Html.fromHtml("<font color=\'#fd5c02\' size=16>"+ userModel.chargeNum + ".00</font><font color=\'#565a5c\' size=16>"+getResources().getString(R.string.symbol_RMB)+"</font>"));
//
//            rlChargeNumState.setVisibility(View.VISIBLE);
//            btChargeScan.setVisibility(View.VISIBLE);
//            btChargeReset.setVisibility(View.VISIBLE);
        }
        tvMemberNumber.setText(userModel.idNumber);
        tvCardCategoryName.setText(userModel.name);
        tvCashBalance.setText(Html.fromHtml("<font color=\'#fd5c02\' size=16>"+ DecimalUtil.FormatMoney(userModel.afterBalance) + "</font><font color=\'#565a5c\' size=16>"+getResources().getString(R.string.symbol_RMB)+"</font>"));
        tvGiveSum.setText(Html.fromHtml("<font color=\'#fd5c02\' size=16>"+ DecimalUtil.FormatMoney(userModel.afterCashBalance) + "</font><font color=\'#565a5c\' size=16>"+getResources().getString(R.string.symbol_RMB)+"</font>"));
        tvChargeNum.setText(Html.fromHtml("<font color=\'#fd5c02\' size=16>"+ DecimalUtil.FormatMoney(userModel.afterPresentBalance) + "</font><font color=\'#565a5c\' size=16>"+getResources().getString(R.string.symbol_RMB)+"</font>"));


//        String orderNo = getIntent().getStringExtra("orderNo");
//        getCardDetail();
//        getChargeDetail(orderNo);

    }

    private void initListener() {
        btChargeScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                ScanPayManager.enterCaptureActivity(ChargeSuccessActivity.this,data,userModel);
//                getPreqrCharge(userModel.chargeNum +"00",userModel.userId);
            }
        });

        btChargeReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                getScanReset();
            }
        });

    }

    public void getPreqrCharge(String chargeFee,int userId){
//        CardWebHelper.getPreqrCharge(chargeFee,userId , new IModelResultListener<TestModel>() {
//            @Override
//            public boolean onGetResultModel(HttpResultModel resultModel) {
//                return false;
//            }
//
//            @Override
//            public void onSuccess(String resultCode, TestModel resultModel, List<TestModel> resultModelList, String resultMsg, String hint) {
////                Log.e("-----getPresentList",""+resultCode);
//                MyQrCodeModel qrCodeModel = new MyQrCodeModel();
//                qrCodeModel.setModelByJson(resultCode);
//                if(qrCodeModel.code.equals("0")){
////                    Utills.showShortToast(""+qrCodeModel.data);
//                    try {
//                        ScanPayManager.enterCaptureActivity(ChargeSuccessActivity.this,""+qrCodeModel.data,userModel);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//
//                }else{
//                    Utills.showShortToast("下单失败");
//                }
//            }
//
//            @Override
//            public void onFail(String resultCode, String resultMsg, String hint) {
////                lvCard.finishLoading(true);
//            }
//
//            @Override
//            public void onError(String errorMsg) {
////                lvCard.finishLoading(true);
//            }
//        });
    }

    private void getScanReset() {
        PayWebHelper.getScanReset(data, new IModelResultListener<SearchUserModel>() {
            @Override
            public boolean onGetResultModel(HttpResultModel resultModel) {
                return false;
            }

            @Override
            public void onSuccess(String resultCode, SearchUserModel resultModel, List<SearchUserModel> resultModelList, String resultMsg, String hint) {
//                Log.e("------scan",resultCode);

//                if(resultModel.status == 1) {
                    Utills.showShortToast("充值失败");
//                    ScanPayManager.enterChargeSuccessActivity(CaptureActivity.this,data,resultModel);
//                }else{
//                    ScanPayManager.enterChargeSuccessActivity(CaptureActivity.this,data,(SearchUserModel) getIntent().getSerializableExtra("userModel"));
//                }
//                finish();
            }

            @Override
            public void onFail(String resultCode, String resultMsg, String hint) {
//                Utills.showShortToast("支付失败");
//                ScanPayManager.enterChargeSuccessActivity(CaptureActivity.this,data,(SearchUserModel) getIntent().getSerializableExtra("userModel"));
//                finish();
            }

            @Override
            public void onError(String errorMsg) {
//                Utills.showShortToast("支付失败");
//                ScanPayManager.enterChargeSuccessActivity(CaptureActivity.this,data,(SearchUserModel) getIntent().getSerializableExtra("userModel"));
//                finish();
            }
        });
    }




    private final UIHandler uiHandler = new UIHandler(this);
    private static class UIHandler extends Handler {
        private final WeakReference<ChargeSuccessActivity> activity;
        public UIHandler(ChargeSuccessActivity act) {
            super();
            activity = new WeakReference<ChargeSuccessActivity>(act);
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

