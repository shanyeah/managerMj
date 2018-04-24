package com.imovie.mogic.card;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.imovie.mogic.R;
import com.imovie.mogic.card.net.CardWebHelper;
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
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.lang.ref.WeakReference;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChargeSuccessActivity extends Activity{

    public static final int MSG_REFRESH = 600;
    public static final int MSG_WXSEND = 601;
    
    private CardModel cardModel;
    private LoginModel loginModel = new LoginModel();

    private TitleBar titleBar;
    private TextView tvMemberNumber;
    private TextView tvCardCategoryName;
    private TextView tvCashBalance;
    private TextView tvGiveMoneySum;
    private TextView tvGiveSum;
    private TextView tvGiveTotal;

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
        titleBar.setTitle("充值成功");
        titleBar.setLeftListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tvMemberNumber = (TextView) findViewById(R.id.tv_member_number);
        tvCardCategoryName = (TextView) findViewById(R.id.tvCardCategoryName);
        tvCashBalance = (TextView) findViewById(R.id.tvCashBalance);
        tvGiveMoneySum = (TextView) findViewById(R.id.tvGiveMoneySum);
        tvGiveSum = (TextView) findViewById(R.id.tvGiveSum);
        tvGiveTotal = (TextView) findViewById(R.id.tv_give_total);
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

        String orderNo = getIntent().getStringExtra("orderNo");
        getCardDetail();
        getChargeDetail(orderNo);


    }

    private void initListener() {

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
//                    tvCashBalance.setText(DecimalUtil.FormatMoney(loginModel.card.cashBalance)+getResources().getString(R.string.symbol_RMB));
////                    nextLevelCategoryName.setText("升级为"+loginModel.card.nextLevelCategoryName);
//
//                }else{
//                    Toast.makeText(ChargeSuccessActivity.this,"网络错误",Toast.LENGTH_SHORT).show();
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

    public void getChargeDetail(String orderNo){
        CardWebHelper.getChargeDetail(orderNo,new IModelResultListener<TestModel>() {
            @Override
            public boolean onGetResultModel(HttpResultModel resultModel) {
                return false;
            }

            @Override
            public void onSuccess(String resultCode, TestModel resultModel, List<TestModel> resultModelList, String resultMsg, String hint) {
                Log.e("-----getCardDetail",""+resultCode);
//                loginModel.setModelByJson(resultCode);
//                if(loginModel.returnCode.equals("SUCCESS")) {
//                    tvMemberNumber.setText(loginModel.member.idNumber);
//                    tvCardCategoryName.setText(loginModel.card.cardCategoryName);
//                    tvCashBalance.setText(DecimalUtil.FormatMoney(loginModel.card.cashBalance)+getResources().getString(R.string.symbol_RMB));
////                    nextLevelCategoryName.setText("升级为"+loginModel.card.nextLevelCategoryName);
//
//                }else{
//                    Toast.makeText(ChargeSuccessActivity.this,"网络错误",Toast.LENGTH_SHORT).show();
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
//                    activity.get().queryWechatOrder(activity.get().wechatIDModel.orderNo);
                    break;
                case MSG_WXSEND:

                    break;
                default:
                    break;
            }
        }
    };



    class ViewHolder{

        @BindView(R.id.title_bar)
        TitleBar titleBar;

        @BindView(R.id.tv_member_number)
        TextView tvMemberNumber;

        @BindView(R.id.tvCardCategoryName)
        TextView tvCardCategoryName;

        @BindView(R.id.tvCashBalance)
        TextView tvCashBalance;

        @BindView(R.id.tvGiveMoneySum)
        TextView tvGiveMoneySum;

        @BindView(R.id.tvGiveSum)
        TextView tvGiveSum;
        @BindView(R.id.tv_give_total)
        TextView tvGiveTotal;


        ViewHolder(Activity act) {
            ButterKnife.bind(this, act);
        }
    }
}

