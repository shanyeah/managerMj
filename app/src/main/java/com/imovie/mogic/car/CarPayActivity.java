package com.imovie.mogic.car;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.imovie.mogic.MyApplication;
import com.imovie.mogic.R;
import com.imovie.mogic.ScanPay.manager.ScanPayManager;
import com.imovie.mogic.ScanPay.zxing.activity.CaptureActivity;
import com.imovie.mogic.base.universal_loading.YSBLoadingDialog;
import com.imovie.mogic.car.bean.FoodBean;
import com.imovie.mogic.home.BaseActivity;
import com.imovie.mogic.home.SearchMemberActivity;
import com.imovie.mogic.home.SelectTypeActivity;
import com.imovie.mogic.home.adater.GoodsCarAdapter;
import com.imovie.mogic.home.model.GoodsModel;
import com.imovie.mogic.home.model.PayResultModel;
import com.imovie.mogic.home.model.SearchUserModel;
import com.imovie.mogic.home.net.HomeWebHelper;
import com.imovie.mogic.home.widget.UserInfoDialog;
import com.imovie.mogic.login.model.TestModel;
import com.imovie.mogic.mine.SetingHallActivity;
import com.imovie.mogic.myRank.fragment.OrderRecordFragment;
import com.imovie.mogic.myRank.fragment.PraiseNumFragment;
import com.imovie.mogic.myRank.widget.PagerSlidingTabStrip;
import com.imovie.mogic.utills.DecimalUtil;
import com.imovie.mogic.utills.StringHelper;
import com.imovie.mogic.utills.Utills;
import com.imovie.mogic.web.IModelResultListener;
import com.imovie.mogic.web.model.HttpResultModel;
import com.imovie.mogic.widget.NoScrollListView;
import com.imovie.mogic.widget.TitleBar;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CarPayActivity extends BaseActivity {

    public static final int MSG_REFRESH = 600;
    public static final int MSG_WXSEND = 601;

    private TitleBar titleBar;
    private LinearLayout llCarPayMember;
    private TextView tvName;
    private TextView tvNumber;
    private TextView tvBalance;
    private TextView tvPresentBalance;
    private TextView tvPhone;
    private TextView stgName;
    private NoScrollListView lvCarList;
    private TextView tv_amount;
    private RelativeLayout rlScanPay;
    private RelativeLayout rlMemberPay;
    private ImageView ivScanPay;
    private ImageView ivMemberPay;
    private TextView car_limit;
    private TextView tv_sum_amount;
    private TextView tvIncomeAmount;
    private TextView tvDiscountAmount;
    private EditText etUserId;
    private EditText etPayRemark;
    private String amount;
    public List<FoodBean> foodBeanList = new ArrayList<>();
    public GoodsCarAdapter carAdapter;
    public int payType = 0;
    public UserInfoDialog dialog;
    public boolean unSelect = true;
    public int userId = 0;
    public int needPayPass = 0;
    public PayResultModel payModel = new PayResultModel();
    public PayResultModel payModelMember = new PayResultModel();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.car_pay_activity);
        initView();
        setView();
        try {
            for(int i=0;i<foodBeanList.size();i++){
                FoodBean foodBean = foodBeanList.get(i);
                foodBeanList.get(i).setQuantity(foodBean.getSelectCount());
                foodBeanList.get(i).setPayAmount(foodBean.getPrice().multiply(BigDecimal.valueOf(foodBean.getSelectCount())));
                foodBeanList.get(i).setIncomeAmount(foodBean.getPrice().multiply(BigDecimal.valueOf(foodBean.getSelectCount())));
            }
            saveGoodsOrder(userId,"","","",foodBeanList);
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
        amount = getIntent().getStringExtra("amountStr");
//        foodBeanList = (List<FoodBean>) getIntent().getSerializableExtra("FoodBeanList");
        foodBeanList = MyApplication.getInstance().getCarListData();
//        Utills.showShortToast(""+foodBeanList.size());
        titleBar = (TitleBar) findViewById(R.id.title_bar);
        titleBar.setTitle("确认订单");
        titleBar.setLeftListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        llCarPayMember = (LinearLayout) findViewById(R.id.llCarPayMember);
        tvName=(TextView) findViewById(R.id.tv_name);
        tvNumber=(TextView) findViewById(R.id.tvNumber);
        tvBalance = (TextView) findViewById(R.id.tvBalance);
        tvPresentBalance = (TextView) findViewById(R.id.tvPresentBalance);
        tvPhone=(TextView) findViewById(R.id.tvPhone);
        stgName = (TextView) findViewById(R.id.stgName);
        tv_amount = (TextView) findViewById(R.id.tv_amount);
        lvCarList = (NoScrollListView) findViewById(R.id.lvCarList);
        rlScanPay = (RelativeLayout) findViewById(R.id.rlScanPay);
        rlMemberPay = (RelativeLayout) findViewById(R.id.rlMemberPay);
        ivScanPay = (ImageView) findViewById(R.id.ivScanPay);
        ivMemberPay = (ImageView) findViewById(R.id.ivMemberPay);
        car_limit = (TextView) findViewById(R.id.car_limit);
        tv_sum_amount = (TextView) findViewById(R.id.tv_sum_amount);
        tvIncomeAmount = (TextView) findViewById(R.id.tvIncomeAmount);
        tvDiscountAmount = (TextView) findViewById(R.id.tvDiscountAmount);
        etUserId = (EditText) findViewById(R.id.etUserId);
        etPayRemark = (EditText) findViewById(R.id.etPayRemark);
    }

    private void setView(){
        stgName.setText(MyApplication.getInstance().mPref.getString("organName",""));
        tv_amount.setText("总计: ¥"+amount);
        carAdapter = new GoodsCarAdapter(CarPayActivity.this,foodBeanList);
        lvCarList.setAdapter(carAdapter);
//        dialog = new UserInfoDialog(CarPayActivity.this);
//        dialog.setOnSelectListener(new UserInfoDialog.onSelectListener() {
//            @Override
//            public void onSelect(SearchUserModel userModel) {
//                unSelect = false;
//                userId = userModel.userId;
//                llCarPayMember.setVisibility(View.VISIBLE);
//                tvName.setText("会员："+userModel.name);
//                tvNumber.setText("证件号：" + userModel.idNumber);
//                tvPhone.setText("手机号：" + userModel.mobile);
//                tvBalance.setText(Html.fromHtml("<font color='#565a5c' size=14>余额:</font><font color=\'#fd5c02\' size=14>"+ DecimalUtil.FormatMoney(userModel.balance) +"</font><font color=\'#565a5c\' size=14>"+getResources().getString(R.string.symbol_RMB)+"</font>"));
//                tvPresentBalance.setText(Html.fromHtml("<font color='#565a5c' size=14>赠送:</font><font color=\'#fd5c02\' size=14>"+ DecimalUtil.FormatMoney(userModel.presentBalance) +"</font><font color=\'#565a5c\' size=14>"+getResources().getString(R.string.symbol_RMB)+"</font>"));
//            }
//        });
//        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
//            @Override
//            public void onDismiss(DialogInterface dialogInterface) {
//                if(unSelect){
//                    ivScanPay.setBackground(getResources().getDrawable(R.drawable.box_select));
//                    ivMemberPay.setBackground(getResources().getDrawable(R.drawable.box_unselect));
//                    payType = 0;
//                    return;
//                }
//            }
//        });
        rlScanPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(payType==1){
                    ivScanPay.setBackground(getResources().getDrawable(R.drawable.box_select));
                    ivMemberPay.setBackground(getResources().getDrawable(R.drawable.box_unselect));
                    payType = 0;
                    llCarPayMember.setVisibility(View.GONE);
                    unSelect = true;
                    if(payModel!=null) {
                        tv_sum_amount.setText("小计：¥" + payModel.payAmount);
                        tvIncomeAmount.setText("应付金额：¥" + payModel.incomeAmount);
                        tvDiscountAmount.setText("减免金额：¥" + payModel.discountAmount);
                        tv_amount.setText("总计: ¥" + payModel.incomeAmount);
                    }
                }
            }
        });
        rlMemberPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(payType==0){
                    ivScanPay.setBackground(getResources().getDrawable(R.drawable.box_unselect));
                    ivMemberPay.setBackground(getResources().getDrawable(R.drawable.box_select));
                    payType = 1;
                    if(payModelMember!=null) {
                        tv_sum_amount.setText("小计：¥" + payModelMember.payAmount);
                        tvIncomeAmount.setText("应付金额：¥" + payModelMember.incomeAmount);
                        tvDiscountAmount.setText("减免金额：¥" + payModelMember.discountAmount);
                        tv_amount.setText("总计: ¥" + payModelMember.incomeAmount);
                    }
                }else{
                    tv_sum_amount.setText("小计：¥" + payModel.payAmount);
                    tvIncomeAmount.setText("应付金额：¥" + payModel.incomeAmount);
                    tvDiscountAmount.setText("减免金额：¥" + payModel.discountAmount);
                    tv_amount.setText("总计: ¥" + payModel.incomeAmount);
                }
                Intent intent = new Intent(CarPayActivity.this,SearchMemberActivity.class);
                startActivityForResult(intent,SearchMemberActivity.SELECT_RESULT);

//                    dialog.show();
            }
        });
        llCarPayMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CarPayActivity.this,SearchMemberActivity.class);
                startActivityForResult(intent,SearchMemberActivity.SELECT_RESULT);
            }
        });
        car_limit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(payType==0){
                    ScanPayManager.enterCaptureActivity(CarPayActivity.this,(BaseActivity)CarPayActivity.this);
                }else if(payType==1){
//                    String seakNo = etUserId.getText().toString();
//                    String remark = etPayRemark.getText().toString();
//                    payGoodsOrder(payModel.saleBillId,userId,seakNo,2, 0,payModel.incomeAmount,remark);
                    if(needPayPass == 1){
                        dialog = new UserInfoDialog(CarPayActivity.this);
                        dialog.setOnSelectListener(new UserInfoDialog.onSelectListener() {
                            @Override
                            public void onSelect(String str) {
                                payOrder("",2,payModelMember.incomeAmount,str);

                            }
                        });
                        dialog.show();
                    }else{
                        payOrder("",2,payModelMember.incomeAmount,"");
                    }
                }
            }
        });
    }
    public void payOrder(String sn,int payType,double incomeAmount,String payPassword){
        String seakNo = etUserId.getText().toString();
        String remark = etPayRemark.getText().toString();
        payGoodsOrder(payModel.saleBillId,userId,seakNo,payType, 0,incomeAmount,remark,sn,payPassword);
    }


    public void setUserData(SearchUserModel userModel){
        unSelect = false;
        userId = userModel.userId;
        needPayPass = userModel.needPayPass;
        llCarPayMember.setVisibility(View.VISIBLE);
        tvName.setText(""+userModel.name);
        tvNumber.setText("证件号：" + userModel.idNumber);
        tvPhone.setText("" + userModel.mobile);
        tvBalance.setText(Html.fromHtml("<font color='#565a5c' size=14>余额:</font><font color=\'#fd5c02\' size=14>"+ DecimalUtil.FormatMoney(userModel.balance) +"</font><font color=\'#565a5c\' size=14>"+getResources().getString(R.string.symbol_RMB)+"</font>"));
        tvPresentBalance.setText(Html.fromHtml("<font color='#565a5c' size=14>现金:</font><font color=\'#fd5c02\' size=14>"+ DecimalUtil.FormatMoney(userModel.cashBalance) +"</font><font color=\'#565a5c\' size=14>"+getResources().getString(R.string.symbol_RMB)+"</font>"));
        try {
            billMemberPrice(userId,payModel.saleBillId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setNoUserData() {
         if(unSelect){
            ivScanPay.setBackground(getResources().getDrawable(R.drawable.box_select));
            ivMemberPay.setBackground(getResources().getDrawable(R.drawable.box_unselect));
            payType = 0;
             tv_sum_amount.setText("小计：¥" + payModel.payAmount);
             tvIncomeAmount.setText("应付金额：¥" + payModel.incomeAmount);
             tvDiscountAmount.setText("减免金额：¥" + payModel.discountAmount);
             tv_amount.setText("总计: ¥" + payModel.incomeAmount);
         }
    }

    public void saveGoodsOrder(int userId,String qrCode,String remark, String seatNo, List<FoodBean> goodsList){
        YSBLoadingDialog.showLoadingDialog(CarPayActivity.this, 3000, new YSBLoadingDialog.OnCancelListener() {
            @Override
            public void onTimeout() {
                YSBLoadingDialog.dismissDialog();
            }

            @Override
            public void onCancel() {
                YSBLoadingDialog.dismissDialog();
            }
        });
        HomeWebHelper.saveGoodsOrder(userId,qrCode,remark, seatNo, goodsList,new IModelResultListener<PayResultModel>() {
            @Override
            public boolean onGetResultModel(HttpResultModel resultModel) {
                return false;
            }

            @Override
            public void onSuccess(String resultCode, PayResultModel resultModel, List<PayResultModel> resultModelList, String resultMsg, String hint) {
                if(resultCode.equals("0")) {
                    payModel = resultModel;
                    tv_sum_amount.setText("小计：¥" + payModel.payAmount);
                    tvIncomeAmount.setText("应付金额：¥" + payModel.incomeAmount);
                    tvDiscountAmount.setText("减免金额：¥" + payModel.discountAmount);
                    tv_amount.setText("总计: ¥"+resultModel.incomeAmount);
//                    if(payType == 2){
//                        ScanPayManager.enterCaptureActivity(CarPayActivity.this,resultModel);
//                    }else{
//                        payGoodsOrder(resultModel.saleBillId , resultModel.saleBillId,"");
//                    }

                }else{
                    Utills.showShortToast(resultMsg);
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

    public void billMemberPrice(int userId,long saleBillId){

        HomeWebHelper.billMemberPrice(userId,saleBillId,new IModelResultListener<PayResultModel>() {
            @Override
            public boolean onGetResultModel(HttpResultModel resultModel) {
                return false;
            }

            @Override
            public void onSuccess(String resultCode, PayResultModel resultModel, List<PayResultModel> resultModelList, String resultMsg, String hint) {
                if(resultCode.equals("0")) {
                    payModelMember = resultModel;
                    tv_sum_amount.setText("小计：¥" + resultModel.payAmount);
                    tvIncomeAmount.setText("应付金额：¥" + resultModel.incomeAmount);
                    tvDiscountAmount.setText("减免金额：¥" + resultModel.discountAmount);
                    tv_amount.setText("总计: ¥"+resultModel.incomeAmount);
//                    if(payType == 2){
//                        ScanPayManager.enterCaptureActivity(CarPayActivity.this,resultModel);
//                    }else{
//                        payGoodsOrder(resultModel.saleBillId , resultModel.saleBillId,"");
//                    }

                }else{
                    Utills.showShortToast(resultMsg);
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




    public void payGoodsOrder(long saleBillId, long userId,String seatNo,int payType, long payCategoryId,double incomeAmount,String remark,String tn,String payPassword){
        YSBLoadingDialog.showLoadingDialog(CarPayActivity.this, 2000, new YSBLoadingDialog.OnCancelListener() {
            @Override
            public void onTimeout() {
                YSBLoadingDialog.dismissDialog();
            }

            @Override
            public void onCancel() {
                YSBLoadingDialog.dismissDialog();
            }
        });
        HomeWebHelper.payGoodsOrder(saleBillId, userId,seatNo,payType, payCategoryId,incomeAmount,remark,tn,payPassword,new IModelResultListener<TestModel>() {
            @Override
            public boolean onGetResultModel(HttpResultModel resultModel) {
                return false;
            }

            @Override
            public void onSuccess(String resultCode, TestModel resultModel, List<TestModel> resultModelList, String resultMsg, String hint) {

                if(resultCode.equals("0")) {
                    Utills.showShortToast(resultMsg);
                    sendBroadcast(new Intent(SelectTypeActivity.CLEARCAR_ACTION));
                    finish();
                }else{
                    Utills.showShortToast(resultMsg);
                }
            }

            @Override
            public void onFail(String resultCode, String resultMsg, String hint) {

            }

            @Override
            public void onError(String errorMsg) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        Utills.showShortToast(""+requestCode);
        switch (requestCode) {
            case CaptureActivity.MSG_OTHER:
                String code = data.getStringExtra("code");
                if(StringHelper.isEmpty(code)){
                    Utills.showShortToast("扫码出错");
                    return;
                }else{
                    payOrder(code,1,payModel.incomeAmount,"");
//                    Utills.showShortToast("扫码" + code);
                }
                break;


            case SearchMemberActivity.SELECT_RESULT:
                SearchUserModel userModel = (SearchUserModel) data.getSerializableExtra("userModel");
                if(userModel.name!=null){
                    setUserData(userModel);
                }else{
                    setNoUserData();
                }
                break;
            default:
                break;

        }
    }

}


