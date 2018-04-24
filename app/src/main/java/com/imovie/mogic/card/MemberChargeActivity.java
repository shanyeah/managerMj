package com.imovie.mogic.card;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.imovie.mogic.MyApplication;
import com.imovie.mogic.R;
import com.imovie.mogic.base.universal_loading.YSBLoadingDialog;
import com.imovie.mogic.card.model.PresentModel;
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
import com.imovie.mogic.utills.StringHelper;
import com.imovie.mogic.utills.Utills;
import com.imovie.mogic.web.IModelResultListener;
import com.imovie.mogic.web.model.HttpResultModel;
import com.imovie.mogic.widget.TitleBar;
import com.imovie.mogic.wxapi.Constants;
import com.imovie.mogic.wxapi.WXPayEntryActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.lang.ref.WeakReference;
import java.util.List;

import butterknife.BindView; import butterknife.ButterKnife;

public class MemberChargeActivity extends BaseActivity {

    public static final int MSG_REFRESH = 600;
    public static final int MSG_WXSEND = 601;
    
    private CardModel cardModel;
    public IWXAPI api;
    public WechatIDModel wechatIDModel = new WechatIDModel();
    private LoginModel loginModel = new LoginModel();
    protected NotificationBroadcast refreshReceiver;

    private TitleBar titleBar;
    RelativeLayout rlChargePayChat;
    TextView tvMemberNumber;
    TextView tvCardCategoryName;
    TextView tvCashBalance;
    TextView tv_give_total;

    TextView tvRightOne;
    TextView tvRightThree;
    TextView tvRightTwo;
    TextView tvRightFour;
    TextView tv_right_Five;
    TextView tv_right_six;
    TextView tv_right_seven;
    TextView tvSendNum;
    LinearLayout llRightOne;
    LinearLayout llRightTwo;
    LinearLayout llRightThree;
    LinearLayout llRightFour;
    LinearLayout llRightFive;
    LinearLayout llRightSix;
    LinearLayout llRightSeven;
    EditText etChargeNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.card_member_charge);
        api = WXAPIFactory.createWXAPI(this, null);
        api.registerApp(Constants.APP_ID);
        initView();
        setView();
        initListener();

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
        titleBar.setTitle("会员卡充值");
        titleBar.setLeftListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        rlChargePayChat = (RelativeLayout) findViewById(R.id.rlChargePayChat);
        tvMemberNumber = (TextView) findViewById(R.id.tv_member_number);
        tvCardCategoryName = (TextView) findViewById(R.id.tvCardCategoryName);
        tvCashBalance = (TextView) findViewById(R.id.tvCashBalance);
        tv_give_total = (TextView) findViewById(R.id.tv_give_total);

        tvRightOne = (TextView) findViewById(R.id.tvRightOne);
        tvRightThree = (TextView) findViewById(R.id.tvRightThree);
        tvRightTwo = (TextView) findViewById(R.id.tvRightTwo);
        tvRightFour = (TextView) findViewById(R.id.tvRightFour);

        tv_right_Five = (TextView) findViewById(R.id.tv_right_Five);
        tv_right_six = (TextView) findViewById(R.id.tv_right_six);
        tv_right_seven = (TextView) findViewById(R.id.tv_right_seven);
        tvSendNum = (TextView) findViewById(R.id.tvSendNum);

        llRightOne = (LinearLayout) findViewById(R.id.llRightOne);
        llRightOne = (LinearLayout) findViewById(R.id.llRightOne);
        llRightTwo = (LinearLayout) findViewById(R.id.llRightTwo);
        llRightThree = (LinearLayout) findViewById(R.id.llRightThree);
        llRightFour = (LinearLayout) findViewById(R.id.llRightFour);
        llRightFive = (LinearLayout) findViewById(R.id.llRightFive);
        llRightSix = (LinearLayout) findViewById(R.id.llRightSix);
        llRightSeven = (LinearLayout) findViewById(R.id.llRightSeven);
        etChargeNum = (EditText) findViewById(R.id.etChargeNum);

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

        getCardDetail();
        getPresentList();

    }

    private void initListener() {
        rlChargePayChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String num = etChargeNum.getText().toString();

                if(StringHelper.isEmpty(num)){
                    Utills.showShortToast("充值金额不能为空");
                    return;
                }
                showLoadingView("加载中...", 5000, new YSBLoadingDialog.OnCancelListener() {
                    @Override
                    public void onTimeout() {
                        hideLoadingView();
                    }

                    @Override
                    public void onCancel() {
                        hideLoadingView();
                    }
                });

                getWechatId("魔杰电竞充值","魔杰电竞充值",num);
            }
        });
        llRightOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etChargeNum.setText("50");
                tvSendNum.setText(""+tvRightOne.getText().toString());
            }
        });
        llRightTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etChargeNum.setText("100");
                tvSendNum.setText(""+tvRightTwo.getText().toString());
            }
        });
        llRightThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etChargeNum.setText("200");
                tvSendNum.setText(""+tvRightThree.getText().toString());
            }
        });
        llRightFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etChargeNum.setText("300");
                tvSendNum.setText(""+tvRightFour.getText().toString());
            }
        });
        llRightFive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etChargeNum.setText("500");
                tvSendNum.setText(""+tv_right_Five.getText().toString());
//                tv_right_Five.getText().toString();
            }
        });
        llRightSix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etChargeNum.setText("1000");
                tvSendNum.setText(""+tv_right_six.getText().toString());
            }
        });
        llRightSeven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etChargeNum.setText("2000");
                tvSendNum.setText(""+tv_right_seven.getText().toString());
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
                Log.e("-----getCardDetail",""+resultCode);
                loginModel.setModelByJson(resultCode);
//                if(loginModel.returnCode.equals("SUCCESS")) {
//                    tvMemberNumber.setText(loginModel.member.idNumber);
//                    tvCardCategoryName.setText(loginModel.card.cardCategoryName);
//                    tvCashBalance.setText(DecimalUtil.FormatMoney(loginModel.card.balance)+getResources().getString(R.string.symbol_RMB));
//                    tv_give_total.setText(DecimalUtil.FormatMoney(loginModel.card.exchangeBalance)+"币");
//
//                }else{
//                    Toast.makeText(MemberChargeActivity.this,"网络错误",Toast.LENGTH_SHORT).show();
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

    public void getPresentList(){
//        CardWebHelper.getPresentList(new IModelResultListener<PresentModel>() {
//            @Override
//            public boolean onGetResultModel(HttpResultModel resultModel) {
//                return false;
//            }
//
//            @Override
//            public void onSuccess(String resultCode, PresentModel resultModel, List<PresentModel> resultModelList, String resultMsg, String hint) {
//                Log.e("-----getPresentList",""+resultCode);
////                loginModel.setModelByJson(resultCode);
//                if(resultModelList.size()>0) {
//                    for(int i=0;i<resultModelList.size();i++) {
//                        switch (i){
//                            case 0:
//                                tvRightOne.setText(resultModelList.get(0).presentAmount);
//                                break;
//                            case 1:
//                                tvRightTwo.setText(resultModelList.get(0).presentAmount);
//                                break;
//                            case 2:
//                                tvRightThree.setText(resultModelList.get(2).presentAmount);
//                                break;
//                            case 3:
//                                tvRightFour.setText(resultModelList.get(3).presentAmount);
//                                break;
//                            case 4:
//                                tv_right_Five.setText(resultModelList.get(4).presentAmount);
//                                break;
//                            case 5:
//                                tv_right_six.setText(resultModelList.get(5).presentAmount);
//                                break;
//                            case 6:
//                                tv_right_seven.setText(resultModelList.get(6).presentAmount);
//                                break;
//                        }
//                    }
//
//
//                }
////                else{
////                    Toast.makeText(MemberChargeActivity.this,"网络错误",Toast.LENGTH_SHORT).show();
////                }
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
                    PayReq req = new PayReq();
                    req.appId = wechatIDModel.appid;
                    req.partnerId = wechatIDModel.partnerid;
                    req.prepayId = wechatIDModel.prepayId;
                    req.nonceStr = wechatIDModel.noncestr;
                    req.timeStamp = wechatIDModel.timestamp;
                    req.packageValue = wechatIDModel.packageStr;
                    req.sign = wechatIDModel.sign;
                    req.extData = "魔杰电竞消费";
                    Toast.makeText(MemberChargeActivity.this, "正常调起支付", Toast.LENGTH_SHORT).show();
                    // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
                    api.sendReq(req);

                }else{
                    Toast.makeText(MemberChargeActivity.this,"下单失败",Toast.LENGTH_SHORT).show();
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
                        Intent intent = new Intent(MemberChargeActivity.this,ChargeSuccessActivity.class);
                        intent.putExtra("orderNo",wechatIDModel.orderNo);
                        startActivity(intent);

                    }

//                    Intent intent = new Intent(MemberChargeActivity.this,ChargeSuccessActivity.class);
//                    intent.putExtra("orderNo",wechatIDModel.orderNo);
//                    startActivity(intent);


                }else{
                    Toast.makeText(MemberChargeActivity.this,"查询订单失败",Toast.LENGTH_SHORT).show();
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
        private final WeakReference<MemberChargeActivity> activity;
        public UIHandler(MemberChargeActivity act) {
            super();
            activity = new WeakReference<MemberChargeActivity>(act);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_REFRESH:
                    activity.get().queryWechatOrder(activity.get().wechatIDModel.orderNo);
                    break;
                case MSG_WXSEND:

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

