package com.imovie.mogic.car;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.imovie.mogic.MyApplication;
import com.imovie.mogic.R;
import com.imovie.mogic.car.bean.FoodBean;
import com.imovie.mogic.home.BaseActivity;
import com.imovie.mogic.home.adater.GoodsCarAdapter;
import com.imovie.mogic.home.model.SearchUserModel;
import com.imovie.mogic.home.widget.UserInfoDialog;
import com.imovie.mogic.myRank.fragment.OrderRecordFragment;
import com.imovie.mogic.myRank.fragment.PraiseNumFragment;
import com.imovie.mogic.myRank.widget.PagerSlidingTabStrip;
import com.imovie.mogic.utills.Utills;
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
    private TextView stgName;
    private NoScrollListView lvCarList;
    private TextView tv_amount;
    private RelativeLayout rlScanPay;
    private RelativeLayout rlMemberPay;
    private ImageView ivScanPay;
    private ImageView ivMemberPay;
    private String amount;
    public List<FoodBean> foodBeanList = new ArrayList<>();
    public GoodsCarAdapter carAdapter;
    public int payType = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.car_pay_activity);
        initView();
        setView();
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
        foodBeanList = (List<FoodBean>) getIntent().getSerializableExtra("FoodBeanList");
//        Utills.showShortToast(""+foodBeanList.size());
        titleBar = (TitleBar) findViewById(R.id.title_bar);
        titleBar.setTitle("确认订单");
        titleBar.setLeftListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        stgName = (TextView) findViewById(R.id.stgName);
        tv_amount = (TextView) findViewById(R.id.tv_amount);
        lvCarList = (NoScrollListView) findViewById(R.id.lvCarList);
        rlScanPay = (RelativeLayout) findViewById(R.id.rlScanPay);
        rlMemberPay = (RelativeLayout) findViewById(R.id.rlMemberPay);
        ivScanPay = (ImageView) findViewById(R.id.ivScanPay);
        ivMemberPay = (ImageView) findViewById(R.id.ivMemberPay);

    }

    private void setView(){
        stgName.setText(MyApplication.getInstance().mPref.getString("organName",""));
        tv_amount.setText("总计 ¥"+amount);
        carAdapter = new GoodsCarAdapter(CarPayActivity.this,foodBeanList);
        lvCarList.setAdapter(carAdapter);
        rlScanPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(payType==1){
                    ivScanPay.setBackground(getResources().getDrawable(R.drawable.box_select));
                    ivMemberPay.setBackground(getResources().getDrawable(R.drawable.box_unselect));
                    payType = 0;
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
                }

                UserInfoDialog dialog = new UserInfoDialog(CarPayActivity.this);
                    dialog.setOnSelectListener(new UserInfoDialog.onSelectListener() {
                        @Override
                        public void onSelect(SearchUserModel userModel) {
//                            payGoodsOrder(goodsOrderId , clerkOrderId , userModel.qrCode);
//                            saveGoodsOrder(userModel.qrCode,TotalPrice, text, list);
                        }
                    });
                    dialog.show();
            }
        });
    }


}


