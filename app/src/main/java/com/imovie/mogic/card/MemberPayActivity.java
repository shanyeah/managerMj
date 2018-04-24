package com.imovie.mogic.card;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.imovie.mogic.R;
import com.imovie.mogic.base.universal_loading.YSBLoadingDialog;
import com.imovie.mogic.card.model.QrCodeModel;
import com.imovie.mogic.card.net.CardWebHelper;
import com.imovie.mogic.home.BaseActivity;
import com.imovie.mogic.login.model.LoginModel;
import com.imovie.mogic.login.model.TestModel;
import com.imovie.mogic.utills.DecimalUtil;
import com.imovie.mogic.utills.QRCodeUtil;
import com.imovie.mogic.web.IModelResultListener;
import com.imovie.mogic.web.model.HttpResultModel;
import com.imovie.mogic.widget.TitleBar;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class MemberPayActivity extends BaseActivity {

    public static final int ACT_REFRESH = 21;
    public static final int ACT_SEND = 22;

    private TitleBar titleBar;
    public ScheduledThreadPoolExecutor scheduledThreadPoolExecutor;
    public AtomicLong mCounter;
    public ImageView ivQrCode;
    public ImageView ivOneQrCode;
    public TextView tvCodeNumber;
    TextView tvCardCategoryName;
    TextView tvCashBalance;
    TextView tv_give_total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.card_member_pay);
        initView();
        setView();
        getCardDetail();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (scheduledThreadPoolExecutor != null)
            scheduledThreadPoolExecutor.shutdown();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void initView(){
        titleBar = (TitleBar) findViewById(R.id.title_bar);
        titleBar.setTitle("刷卡安全支付");
        titleBar.setLeftListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ivQrCode = (ImageView) findViewById(R.id.ivQrCode);
        ivOneQrCode = (ImageView) findViewById(R.id.ivOneQrCode);
        tvCodeNumber = (TextView) findViewById(R.id.tvCodeNumber);
        tvCardCategoryName = (TextView) findViewById(R.id.tvCardCategoryName);
        tvCashBalance = (TextView) findViewById(R.id.tvCashBalance);
        tv_give_total = (TextView) findViewById(R.id.tv_give_total);

//        LinearLayout.LayoutParams ivParam=(LinearLayout.LayoutParams)cardTop.getLayoutParams();
//        int screenWidth = getResources().getDisplayMetrics().widthPixels;
//        ivParam.width = (screenWidth*576)/746;
//        ivParam.height =( screenWidth*365)/746;
//        cardTop.setLayoutParams(ivParam);
    }

    private void setView(){

        scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(1);
        mCounter = new AtomicLong(1);
        mCounter.set(0);
        scheduledThreadPoolExecutor.scheduleAtFixedRate(new Runnable() {//每隔一段时间就执行定时任务,上报状态
            @Override
            public void run() {
                if (mCounter.getAndIncrement() % (120) == 0) {//每2分钟查下  60
                    Message msg = new Message();
                    msg.what = ACT_REFRESH;
                    uiHandler.sendMessage(msg);
                }
            }
        }, 1, 1, TimeUnit.SECONDS);

    }

    public void getCardDetail(){
        showLoadingView("加载中...", 3000, new YSBLoadingDialog.OnCancelListener() {
            @Override
            public void onTimeout() {
                hideLoadingView();
            }

            @Override
            public void onCancel() {
                hideLoadingView();
            }
        });
        CardWebHelper.getCardDetail(new IModelResultListener<TestModel>() {
            @Override
            public boolean onGetResultModel(HttpResultModel resultModel) {
                return false;
            }

            @Override
            public void onSuccess(String resultCode, TestModel resultModel, List<TestModel> resultModelList, String resultMsg, String hint) {
//                Log.e("-----getCardDetail",""+resultCode);
                LoginModel loginModel= new LoginModel();
                loginModel.setModelByJson(resultCode);
//                if(loginModel.returnCode.equals("SUCCESS")) {
//                    tvCardCategoryName.setText(loginModel.card.cardCategoryName);
//                    tvCashBalance.setText(DecimalUtil.FormatMoney(loginModel.card.balance)+getResources().getString(R.string.symbol_RMB));
//                    tv_give_total.setText(DecimalUtil.FormatMoney(loginModel.card.exchangeBalance)+"币");
//
//                }else{
//                    Toast.makeText(MemberPayActivity.this,"网络错误",Toast.LENGTH_SHORT).show();
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


    public void getQRcode(){
        CardWebHelper.getQRcode(new IModelResultListener<TestModel>() {
            @Override
            public boolean onGetResultModel(HttpResultModel resultModel) {
                return false;
            }

            @Override
            public void onSuccess(String resultCode, TestModel resultModel, List<TestModel> resultModelList, String resultMsg, String hint) {
//                Log.e("-----qrcode",""+resultCode);
                QrCodeModel qrCodeModel = new QrCodeModel();
                qrCodeModel.setModelByJson(resultCode);
                if(qrCodeModel.returnCode.equals("SUCCESS")){
                    try {
                        tvCodeNumber.setText(""+qrCodeModel.payCode);
                        Bitmap bitmap = QRCodeUtil.createQRImage(qrCodeModel.payCode,400,400,null);
                        ivQrCode.setImageBitmap(bitmap);
                        Bitmap bm = QRCodeUtil.createImage2(qrCodeModel.payCode, 1600, 400);
                        ivOneQrCode.setImageBitmap(bm);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }else{
                    Toast.makeText(MemberPayActivity.this,"获取不到支付码",Toast.LENGTH_SHORT).show();
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
        private final WeakReference<MemberPayActivity> activity;
        public UIHandler(MemberPayActivity act) {
            super();
            activity = new WeakReference<MemberPayActivity>(act);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ACT_REFRESH:
                    activity.get().getQRcode();
                    break;
                case ACT_SEND:

                    break;
                default:
                    break;
            }
        }
    };

}

