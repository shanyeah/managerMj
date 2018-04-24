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
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.imovie.mogic.MyApplication;
import com.imovie.mogic.R;
import com.imovie.mogic.base.universal_loading.YSBLoadingDialog;
import com.imovie.mogic.home.BaseActivity;
import com.imovie.mogic.home.MainActivity;
import com.imovie.mogic.home.model.CardModel;
import com.imovie.mogic.home.model.QureyOrderModel;
import com.imovie.mogic.home.model.WechatIDModel;
import com.imovie.mogic.home.net.HomeWebHelper;
import com.imovie.mogic.login.LoginActivity;
import com.imovie.mogic.login.model.TestModel;
import com.imovie.mogic.utills.DecimalUtil;
import com.imovie.mogic.utills.Utills;
import com.imovie.mogic.web.IModelResultListener;
import com.imovie.mogic.web.model.HttpResultModel;
import com.imovie.mogic.wxapi.Constants;
import com.imovie.mogic.wxapi.WXPayEntryActivity;

import com.imovie.mogic.widget.TitleBar;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.lang.ref.WeakReference;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class CardActivity extends BaseActivity {

    public static final int MSG_REFRESH = 600;
    public static final int MSG_WXSEND = 601;
    
    private CardModel cardModel;
//    public IWXAPI api;
    public WechatIDModel wechatIDModel = new WechatIDModel();
    private IWXAPI api;
    protected NotificationBroadcast refreshReceiver;

    private TitleBar titleBar;
    private ImageView cardTop;
    private TextView tvPayPhone;
    private TextView tvPayTotal;
    private RelativeLayout rlChat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.card_activity);
        initView();
        setView();
        initListener();

        refreshReceiver = new NotificationBroadcast();
        IntentFilter filter = new IntentFilter();
        filter.addAction(WXPayEntryActivity.PAY_FINISH_BROADCAST);
        CardActivity.this.registerReceiver(refreshReceiver, filter);

        api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);

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
        cardModel = (CardModel) getIntent().getExtras().getSerializable("card");
        titleBar = (TitleBar) findViewById(R.id.title_bar);
//        titleBar.setTitle(cardModel.name);
        titleBar.setLeftListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        cardTop = (ImageView) findViewById(R.id.iv_card_image);
        tvPayPhone = (TextView) findViewById(R.id.tv_pay_phone);
        tvPayTotal = (TextView) findViewById(R.id.tv_pay_total);
        rlChat = (RelativeLayout) findViewById(R.id.rl_pay_chat);

        LinearLayout.LayoutParams ivParam=(LinearLayout.LayoutParams)cardTop.getLayoutParams();
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        ivParam.width = (screenWidth*576)/746;
        ivParam.height =( screenWidth*365)/746;
        cardTop.setLayoutParams(ivParam);
    }

    private void setView(){
//        tvPayTotal.setText(DecimalUtil.FormatMoney(cardModel.activeAmount) + getResources().getString(R.string.symbol_RMB));
        tvPayPhone.setText(MyApplication.getInstance().mPref.getString("phone",""));
        DisplayImageOptions mOption = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.home_card_image)
                .showImageOnFail(R.drawable.home_card_image)
                .showImageForEmptyUri(R.drawable.home_card_image)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        try {
//            ImageLoader.getInstance().displayImage(cardModel.imageUrl,cardTop,mOption);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void initListener() {
        rlChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoadingView("加载中...", 6000, new YSBLoadingDialog.OnCancelListener() {
                    @Override
                    public void onTimeout() {
                        hideLoadingView();
                    }

                    @Override
                    public void onCancel() {
                        hideLoadingView();
                    }
                });
//                getWechatId("魔杰电竞"+cardModel.name+"购买","魔杰电竞"+cardModel.name+"购买",DecimalUtil.FormatMoney(cardModel.activeAmount));
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
//                Log.e("-----",""+resultCode);
                wechatIDModel.setModelByJson(resultCode);
                if(wechatIDModel.returnCode.equals("SUCCESS")) {
                    Message msg = new Message();
                    msg.what = MSG_WXSEND;
                    uiHandler.sendMessage(msg);
                    PayReq req = new PayReq();
                    req.appId = wechatIDModel.appid;
                    req.partnerId = wechatIDModel.partnerid;
                    req.prepayId = wechatIDModel.prepayId;
                    req.nonceStr = wechatIDModel.noncestr;
                    req.timeStamp = wechatIDModel.timestamp;
                    req.packageValue = wechatIDModel.packageStr;
                    req.sign = wechatIDModel.sign;
//                    req.extData = "魔杰电竞"+cardModel.name+"购买";
                    Toast.makeText(CardActivity.this, "正常调起支付", Toast.LENGTH_SHORT).show();
                    // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
                     api.sendReq(req);
//                    Toast.makeText(CardActivity.this, ""+bl, Toast.LENGTH_SHORT).show();
//                    Message msg = new Message();
//                    msg.what = MSG_REFRESH;
//                    uiHandler.sendMessage(msg);

                }else{
                    Toast.makeText(CardActivity.this,"下单失败",Toast.LENGTH_SHORT).show();
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
                        Intent intent = new Intent(MainActivity.SEND_BROADCAST);
                        intent.putExtra(MainActivity.SEND_DATA,MainActivity.PAY_SUCCESS);
                        sendBroadcast(intent);
                        finish();
                    }

                }else{
                    Toast.makeText(CardActivity.this,"查询订单失败",Toast.LENGTH_SHORT).show();
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
        private final WeakReference<CardActivity> activity;
        public UIHandler(CardActivity act) {
            super();
            activity = new WeakReference<CardActivity>(act);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_REFRESH:
                    activity.get().queryWechatOrder(activity.get().wechatIDModel.orderNo);
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

