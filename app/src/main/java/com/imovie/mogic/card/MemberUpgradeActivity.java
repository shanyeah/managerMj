package com.imovie.mogic.card;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.imovie.mogic.R;
import com.imovie.mogic.base.universal_loading.YSBLoadingDialog;
import com.imovie.mogic.card.net.CardWebHelper;
import com.imovie.mogic.home.BaseActivity;
import com.imovie.mogic.home.MainActivity;
import com.imovie.mogic.home.model.CardModel;
import com.imovie.mogic.home.model.QureyOrderModel;
import com.imovie.mogic.home.model.WechatIDModel;
import com.imovie.mogic.home.net.HomeWebHelper;
import com.imovie.mogic.login.model.LoginModel;
import com.imovie.mogic.login.model.TestModel;
import com.imovie.mogic.utills.DecimalUtil;
import com.imovie.mogic.web.IModelResultListener;
import com.imovie.mogic.web.model.HttpResultModel;
import com.imovie.mogic.widget.TitleBar;
import com.imovie.mogic.wxapi.Constants;
import com.imovie.mogic.wxapi.WXPayEntryActivity;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.lang.ref.WeakReference;
import java.util.List;

import butterknife.BindView; import butterknife.ButterKnife;

public class MemberUpgradeActivity extends BaseActivity {

    public static final int MSG_REFRESH = 600;
    public static final int MSG_CHAEGE = 61;
    
    private LoginModel loginModel = new LoginModel();
    private IWXAPI api;
    protected NotificationBroadcast refreshReceiver;
//    public IWXAPI api;
    public WechatIDModel wechatIDModel = new WechatIDModel();

    TitleBar titleBar;
    TextView tvMemberNumber;
    TextView tvCardCategoryName;
    TextView tvCashBalance;
    TextView nextLevelCategoryName;
    TextView  tvChargePayChat;

    private ProgressBar progressCharge;
    private TextView tvProgress;
    private TextView tvNeedCharge;
    private Button btChargeMoney;
    private TextView tvTotalNeedCharge;
    private TextView tvActiveCharge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.card_member_upgrade);
        initView();
        setView();
        initListener();
        api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);
        getCardDetail();
        refreshReceiver = new NotificationBroadcast();
        IntentFilter filter = new IntentFilter();
        filter.addAction(WXPayEntryActivity.PAY_FINISH_BROADCAST);
        this.registerReceiver(refreshReceiver, filter);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.unregisterReceiver(refreshReceiver);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void initView(){
        titleBar = (TitleBar) findViewById(R.id.title_bar);
        titleBar.setTitle("会员卡升级");
        titleBar.setLeftListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvMemberNumber = (TextView) findViewById(R.id.tv_member_number);
        tvCardCategoryName = (TextView) findViewById(R.id.tvCardCategoryName);
        tvCashBalance = (TextView) findViewById(R.id.tvCashBalance);
        nextLevelCategoryName = (TextView) findViewById(R.id.nextLevelCategoryName);
        tvChargePayChat = (TextView ) findViewById(R.id.tvChargePayChat);

        progressCharge = (ProgressBar) findViewById(R.id.progressCharge);
        tvProgress = (TextView) findViewById(R.id.tvProgress);
        tvNeedCharge = (TextView) findViewById(R.id.tvNeedCharge);
        btChargeMoney = (Button) findViewById(R.id.btChargeMoney);
        tvTotalNeedCharge = (TextView) findViewById(R.id.tvTotalNeedCharge);
        tvActiveCharge = (TextView) findViewById(R.id.tvActiveCharge);

//        LinearLayout.LayoutParams ivParam=(LinearLayout.LayoutParams)cardTop.getLayoutParams();
//        int screenWidth = getResources().getDisplayMetrics().widthPixels;
//        ivParam.width = (screenWidth*576)/746;
//        ivParam.height =( screenWidth*365)/746;
//        cardTop.setLayoutParams(ivParam);
    }

    private void setView(){

//        String title = list.get(0).getNotifiTitle();
//        String content = "》下载完成："+list.get(0).getNotiTime();
//        tvPushMessage.setText(Html.fromHtml("<font color='#f92387' size=18>影片《</font><font color=\'#2962FF\' size=20>"+ title +"</font><font color=\'#f92387\' size=18>"+content+"</font>"));

//        tvPayTotal.setText(DecimalUtil.FormatMoney(cardModel.activeAmount) + getResources().getString(R.string.symbol_RMB));
//        tvPayPhone.setText(MyApplication.getInstance().mPref.getString("phone",""));
//        DisplayImageOptions mOption = new DisplayImageOptions.Builder()
//                .showImageOnLoading(R.drawable.home_card_image)
//                .showImageOnFail(R.drawable.home_card_image)
//                .showImageForEmptyUri(R.drawable.home_card_image)
//                .cacheInMemory(true)
//                .cacheOnDisk(true)
//                .build();
//        try {
//            ImageLoader.getInstance().displayImage(cardModel.imageUrl,cardTop,mOption);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

    }

    private void initListener() {
        tvChargePayChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(Float.parseFloat(loginModel.card.nextLevelCategoryActiveCharge)>0l)
//                    showLoadingView("加载中...", 5000, new YSBLoadingDialog.OnCancelListener() {
//                        @Override
//                        public void onTimeout() {
//                            hideLoadingView();
//                        }
//
//                        @Override
//                        public void onCancel() {
//                            hideLoadingView();
//                        }
//                    });
//                getWechatId("魔杰电竞消费","魔杰电竞消费",loginModel.card.nextLevelCategoryActiveCharge);
            }
        });

        btChargeMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MemberUpgradeActivity.this,MemberChargeActivity.class);
                startActivity(intent);
            }
        });
    }

    public void getCardDetail(){
        CardWebHelper.getCardDetail(new IModelResultListener<TestModel>() {
            @Override
            public boolean onGetResultModel(HttpResultModel resultModel) {
                return false;
            }

            @Override
            public void onSuccess(String resultCode, TestModel resultModel, List<TestModel> resultModelList, String resultMsg, String hint) {
//                Log.e("-----getCardDetail",""+resultCode);
                loginModel.setModelByJson(resultCode);
//                if(loginModel.returnCode.equals("SUCCESS")) {
//                    tvMemberNumber.setText(loginModel.member.idNumber);
//                    tvCardCategoryName.setText(loginModel.card.cardCategoryName);
//                    tvCashBalance.setText(DecimalUtil.FormatMoney(loginModel.card.cashBalance)+getResources().getString(R.string.symbol_RMB));
//                    nextLevelCategoryName.setText("升级为"+loginModel.card.nextLevelCategoryName);
//                    int progress = (int) (loginModel.card.currentPercentage*100);
//                    progressCharge.setMax(100);
//                    progressCharge.setProgress(progress);
//                    tvProgress.setText(progress+"%");
//
//                    tvNeedCharge.setText(Html.fromHtml("<font color='#2f3132' size=18>还缺</font><font color=\'#fd5c02\' size=18>"+ DecimalUtil.FormatMoney(loginModel.card.leftNeedCharge) +"</font><font color=\'#2f3132\' size=18>元</font>"));
//                    tvTotalNeedCharge.setText(Html.fromHtml("<font color='#2f3132' size=20>累计充值</font><font color=\'#fd5c02\' size=20>"+ DecimalUtil.FormatMoney(loginModel.card.nextLevelCategoryTotalNeedCharge) +"</font><font color=\'#2f3132\' size=20>元可升级！</font>"));
//                    tvActiveCharge.setText(Html.fromHtml("<font color='#2f3132' size=20>一次性充</font><font color=\'#fd5c02\' size=20>"+ DecimalUtil.FormatMoney(loginModel.card.nextLevelCategoryActiveCharge) +"</font><font color=\'#2f3132\' size=20>元可升级！</font>"));
//
//
//                }else{
//                    Toast.makeText(MemberUpgradeActivity.this,"网络错误",Toast.LENGTH_SHORT).show();
//                }
            }

            @Override
            public void onFail(String resultCode, String resultMsg, String hint) {
//                lvCard.finishLoading(true);
            }

            @Override
            public void onError(String errorMsg) {
//                lvCard.finishLoading(true);
            }
        });
    }

    public void getWechatId(String body,String detail,String totalFee){
        HomeWebHelper.getWechatId(body,detail,totalFee,new IModelResultListener<TestModel>() {
            @Override
            public boolean onGetResultModel(HttpResultModel resultModel) {
                return false;
            }

            @Override
            public void onSuccess(String resultCode, TestModel resultModel, List<TestModel> resultModelList, String resultMsg, String hint) {
                Log.e("-----",""+resultCode);
                wechatIDModel.setModelByJson(resultCode);
                if(wechatIDModel.returnCode.equals("SUCCESS")) {
//                    Message msg = new Message();
//                    msg.what = MSG_WXSEND;
//                    uiHandler.sendMessage(msg);
                    PayReq req = new PayReq();
                    req.appId = wechatIDModel.appid;
                    req.partnerId = wechatIDModel.partnerid;
                    req.prepayId = wechatIDModel.prepayId;
                    req.nonceStr = wechatIDModel.noncestr;
                    req.timeStamp = wechatIDModel.timestamp;
                    req.packageValue = wechatIDModel.packageStr;
                    req.sign = wechatIDModel.sign;
                    req.extData = "魔杰电竞消费";
                    Toast.makeText(MemberUpgradeActivity.this, "正常调起支付", Toast.LENGTH_SHORT).show();
                    // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
                    api.sendReq(req);
//                    Toast.makeText(CardActivity.this, ""+bl, Toast.LENGTH_SHORT).show();
//                    Message msg = new Message();
//                    msg.what = MSG_REFRESH;
//                    uiHandler.sendMessage(msg);

                }else{
                    Toast.makeText(MemberUpgradeActivity.this,"下单失败",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFail(String resultCode, String resultMsg, String hint) {
//                lvCard.finishLoading(true);
            }

            @Override
            public void onError(String errorMsg) {
//                lvCard.finishLoading(true);
            }
        });
    }

    public void queryWechatOrder(String orderNo){
        HomeWebHelper.queryWechatOrder(orderNo,new IModelResultListener<TestModel>() {
            @Override
            public boolean onGetResultModel(HttpResultModel resultModel) {
                return false;
            }

            @Override
            public void onSuccess(String resultCode, TestModel resultModel, List<TestModel> resultModelList, String resultMsg, String hint) {
//                Log.e("-----",""+resultCode);
                QureyOrderModel qureyOrderModel = new QureyOrderModel();
                qureyOrderModel.setModelByJson(resultCode);
                if(qureyOrderModel.returnCode.equals("SUCCESS")) {
                    if(qureyOrderModel.status.equals("SUCCESS")){
                        Message msg = new Message();
                        msg.what = MSG_CHAEGE;
                        uiHandler.sendMessage(msg);

                    }
//                    Intent intent = new Intent(MainActivity.PAY_SUCCESS);
//                    sendBroadcast(intent);
//                    finish();

                }else{
                    Toast.makeText(MemberUpgradeActivity.this,"查询订单失败",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFail(String resultCode, String resultMsg, String hint) {
//                lvCard.finishLoading(true);
            }

            @Override
            public void onError(String errorMsg) {
//                lvCard.finishLoading(true);
            }
        });
    }

    private final UIHandler uiHandler = new UIHandler(this);
    private static class UIHandler extends Handler {
        private final WeakReference<MemberUpgradeActivity> activity;
        public UIHandler(MemberUpgradeActivity act) {
            super();
            activity = new WeakReference<MemberUpgradeActivity>(act);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_REFRESH:
                    activity.get().queryWechatOrder(activity.get().wechatIDModel.orderNo);
                    break;
                case MSG_CHAEGE:
                    Toast.makeText(activity.get(), "升级成功", Toast.LENGTH_SHORT).show();
                    activity.get().getCardDetail();
                    break;
                default:
                    break;
            }
        }
    };

    public class NotificationBroadcast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                if (intent.getAction().equals(WXPayEntryActivity.PAY_FINISH_BROADCAST)) {
                    Message msg = new Message();
                    msg.what = MSG_REFRESH;
                    uiHandler.sendMessage(msg);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}

