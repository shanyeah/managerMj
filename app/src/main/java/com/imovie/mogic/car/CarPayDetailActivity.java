package com.imovie.mogic.car;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
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
import com.imovie.mogic.home.adater.GoodsCarDetailAdapter;
import com.imovie.mogic.home.model.GoodsModel;
import com.imovie.mogic.home.model.PayDetailModel;
import com.imovie.mogic.home.model.PayResultModel;
import com.imovie.mogic.home.model.SearchUserModel;
import com.imovie.mogic.home.net.HomeWebHelper;
import com.imovie.mogic.home.widget.UserInfoDialog;
import com.imovie.mogic.login.model.TestModel;
import com.imovie.mogic.utills.DecimalUtil;
import com.imovie.mogic.utills.StringHelper;
import com.imovie.mogic.utills.Utills;
import com.imovie.mogic.web.IModelResultListener;
import com.imovie.mogic.web.model.HttpResultModel;
import com.imovie.mogic.widget.NoScrollListView;
import com.imovie.mogic.widget.TitleBar;

import java.util.ArrayList;
import java.util.List;

public class CarPayDetailActivity extends BaseActivity {

    public static final int MSG_REFRESH = 600;
    public static final int MSG_WXSEND = 601;

    private TitleBar titleBar;
    private LinearLayout llCarPayMember;
    private TextView tvOrderId;
    private TextView tvName;
    private TextView tvNumber;
    private TextView tvBalance;
    private TextView tvPresentBalance;
    private TextView tvPhone;
    private TextView stgName;
    private TextView tvPayInfo;
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
    private TextView etUserId;
    private TextView etPayRemark;
    private String amount;
    public List<GoodsModel> foodBeanList = new ArrayList<>();
    public GoodsCarDetailAdapter carAdapter;
    public int payType = 0;
    public UserInfoDialog dialog;
    public boolean unSelect = true;
    public int userId = 0;
    public PayResultModel payModel = new PayResultModel();
    public PayResultModel payModelMember = new PayResultModel();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.car_pay_detail_activity);
        initView();
        setView();
        try {
            queryGoodsBillDetail(getIntent().getStringExtra("saleBillId"));
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
//        amount = getIntent().getStringExtra("amountStr");
//        foodBeanList = (List<FoodBean>) getIntent().getSerializableExtra("FoodBeanList");
//        Utills.showShortToast(""+foodBeanList.size());
        titleBar = (TitleBar) findViewById(R.id.title_bar);
        titleBar.setTitle("订单详情");
        titleBar.setLeftListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        llCarPayMember = (LinearLayout) findViewById(R.id.llCarPayMember);
        tvOrderId=(TextView) findViewById(R.id.tvOrderId);
        tvName=(TextView) findViewById(R.id.tv_name);
        tvNumber=(TextView) findViewById(R.id.tvNumber);
        tvBalance = (TextView) findViewById(R.id.tvBalance);
        tvPresentBalance = (TextView) findViewById(R.id.tvPresentBalance);
        tvPhone=(TextView) findViewById(R.id.tvPhone);
        tvPayInfo=(TextView) findViewById(R.id.tvPayInfo);
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
        etUserId = (TextView) findViewById(R.id.etUserId);
        etPayRemark = (TextView) findViewById(R.id.etPayRemark);
    }

    private void setView(){
//        stgName.setText(MyApplication.getInstance().mPref.getString("organName",""));
//        tv_amount.setText("总计: ¥"+amount);
        carAdapter = new GoodsCarDetailAdapter(CarPayDetailActivity.this,foodBeanList);
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
//                if(payType==1){
//                    ivScanPay.setBackground(getResources().getDrawable(R.drawable.box_select));
//                    ivMemberPay.setBackground(getResources().getDrawable(R.drawable.box_unselect));
//                    payType = 0;
//                    llCarPayMember.setVisibility(View.GONE);
//                    unSelect = true;
//                    if(payModel!=null) {
//                        tv_sum_amount.setText("小计：¥" + payModel.payAmount);
//                        tvIncomeAmount.setText("应付金额：¥" + payModel.incomeAmount);
//                        tvDiscountAmount.setText("减免金额：¥" + payModel.discountAmount);
//                        tv_amount.setText("总计: ¥" + payModel.incomeAmount);
//                    }
//                }
            }
        });
        rlMemberPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if(payType==0){
//                    ivScanPay.setBackground(getResources().getDrawable(R.drawable.box_unselect));
//                    ivMemberPay.setBackground(getResources().getDrawable(R.drawable.box_select));
//                    payType = 1;
//                    if(payModelMember!=null) {
//                        tv_sum_amount.setText("小计：¥" + payModelMember.payAmount);
//                        tvIncomeAmount.setText("应付金额：¥" + payModelMember.incomeAmount);
//                        tvDiscountAmount.setText("减免金额：¥" + payModelMember.discountAmount);
//                        tv_amount.setText("总计: ¥" + payModelMember.incomeAmount);
//                    }
//                }else{
//                    tv_sum_amount.setText("小计：¥" + payModel.payAmount);
//                    tvIncomeAmount.setText("应付金额：¥" + payModel.incomeAmount);
//                    tvDiscountAmount.setText("减免金额：¥" + payModel.discountAmount);
//                    tv_amount.setText("总计: ¥" + payModel.incomeAmount);
//                }
//                Intent intent = new Intent(CarPayDetailActivity.this,SearchMemberActivity.class);
//                startActivityForResult(intent,SearchMemberActivity.SELECT_RESULT);

//                    dialog.show();
            }
        });
        llCarPayMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(CarPayDetailActivity.this,SearchMemberActivity.class);
//                startActivityForResult(intent,SearchMemberActivity.SELECT_RESULT);
            }
        });
        car_limit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(payType==0){
                    ScanPayManager.enterCaptureActivity(CarPayDetailActivity.this,(BaseActivity)CarPayDetailActivity.this);
                }else if(payType==1){
//                    String seakNo = etUserId.getText().toString();
//                    String remark = etPayRemark.getText().toString();
//                    payGoodsOrder(payModel.saleBillId,userId,seakNo,2, 0,payModel.incomeAmount,remark);
                    payOrder("",2);
                }
            }
        });
    }
    public void payOrder(String sn,int payType){
        String seakNo = etUserId.getText().toString();
        String remark = etPayRemark.getText().toString();
        payGoodsOrder(payModel.saleBillId,userId,seakNo,payType, 0,payModel.incomeAmount,remark,sn);
    }


    public void setUserData(SearchUserModel userModel){
        unSelect = false;
        userId = userModel.userId;
        llCarPayMember.setVisibility(View.VISIBLE);
        tvName.setText(""+userModel.name);
        tvNumber.setText("证件号：" + userModel.idNumber);
        tvPhone.setText("" + userModel.mobile);
        tvBalance.setText(Html.fromHtml("<font color='#565a5c' size=14>余额:</font><font color=\'#fd5c02\' size=14>"+ DecimalUtil.FormatMoney(userModel.balance) +"</font><font color=\'#565a5c\' size=14>"+getResources().getString(R.string.symbol_RMB)+"</font>"));
        tvPresentBalance.setText(Html.fromHtml("<font color='#565a5c' size=14>赠送:</font><font color=\'#fd5c02\' size=14>"+ DecimalUtil.FormatMoney(userModel.presentBalance) +"</font><font color=\'#565a5c\' size=14>"+getResources().getString(R.string.symbol_RMB)+"</font>"));
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

    public void queryGoodsBillDetail(String saleBillId){
        YSBLoadingDialog.showLoadingDialog(CarPayDetailActivity.this, 2000, new YSBLoadingDialog.OnCancelListener() {
            @Override
            public void onTimeout() {
                YSBLoadingDialog.dismissDialog();
            }

            @Override
            public void onCancel() {
                YSBLoadingDialog.dismissDialog();
            }
        });
        HomeWebHelper.queryGoodsBillDetail(saleBillId,new IModelResultListener<PayDetailModel>() {
            @Override
            public boolean onGetResultModel(HttpResultModel resultModel) {
                return false;
            }

            @Override
            public void onSuccess(String resultCode, PayDetailModel resultModel, List<PayDetailModel> resultModelList, String resultMsg, String hint) {
                if(resultCode.equals("0")) {
//                    payModel = resultModel;
                    tvOrderId.setText(resultModel.orderNo);
                    stgName.setText(resultModel.organName);
                    tv_sum_amount.setText("小计：¥" + DecimalUtil.FormatMoney(resultModel.payAmount));
                    tvIncomeAmount.setText("应付金额：¥" + DecimalUtil.FormatMoney(resultModel.incomeAmount));
                    tvDiscountAmount.setText("减免金额：¥" + DecimalUtil.FormatMoney(resultModel.discountAmount));
                    tv_amount.setText("总计: ¥"+DecimalUtil.FormatMoney(resultModel.incomeAmount));
                    tvPayInfo.setText(resultModel.payInfo);
                    etUserId.setText(resultModel.seatNo);
                    etPayRemark.setText(resultModel.remark);
                    if(resultModel.goodsList.size()>0) {
                        carAdapter.list.clear();
                        carAdapter.list.addAll(resultModel.goodsList);
                        carAdapter.notifyDataSetChanged();
                    }

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




    public void payGoodsOrder(long saleBillId, long userId,String seatNo,int payType, long payCategoryId,double incomeAmount,String remark,String tn){
        HomeWebHelper.payGoodsOrder(saleBillId, userId,seatNo,payType, payCategoryId,incomeAmount,remark,tn,new IModelResultListener<TestModel>() {
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
                    payOrder(code,1);
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


