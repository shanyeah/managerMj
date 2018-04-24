package com.imovie.mogic.mine;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.imovie.mogic.MyApplication;
import com.imovie.mogic.R;
import com.imovie.mogic.home.BaseActivity;
import com.imovie.mogic.login.LoginActivity;
import com.imovie.mogic.login.model.MyDataModel;
import com.imovie.mogic.login.model.TestModel;
import com.imovie.mogic.mine.model.IncomeModel;
import com.imovie.mogic.mine.net.MineWebHelper;
import com.imovie.mogic.utills.DecimalUtil;
import com.imovie.mogic.web.IModelResultListener;
import com.imovie.mogic.web.model.HttpResultModel;
import com.imovie.mogic.widget.FlexibleFrameLayout;
import com.imovie.mogic.widget.HoopChartView;
import com.imovie.mogic.widget.PullToRefreshFrameLayout;
import com.imovie.mogic.widget.TitleBar;

import java.util.ArrayList;
import java.util.List;

public class MyIncomeActivity extends BaseActivity {

    private TitleBar titleBar;
    private PullToRefreshFrameLayout pull_content;
    private FlexibleFrameLayout ff_list;
    private HoopChartView hoopChartView;
    private RelativeLayout rlMineCharge;
    private RelativeLayout rlMinePraise;
    private RelativeLayout rlMineOrder;
    private TextView tvChargeMoney;
    private TextView tvPraiseMoney;
    private TextView tvOrderMoney;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mine_income_activity);
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
        titleBar.setTitle("收入汇总 ");
        titleBar.setLeftListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        pull_content = (PullToRefreshFrameLayout)findViewById(R.id.pull_content);
        ff_list = (FlexibleFrameLayout)findViewById(R.id.ff_list);
        hoopChartView = (HoopChartView) findViewById(R.id.hoopChartView);
        rlMineCharge = (RelativeLayout)findViewById(R.id.rlMineCharge);
        rlMinePraise = (RelativeLayout)findViewById(R.id.rlMinePraise);
        rlMineOrder = (RelativeLayout)findViewById(R.id.rlMineOrder);
        tvChargeMoney = (TextView) findViewById(R.id.tvChargeMoney);
        tvPraiseMoney = (TextView) findViewById(R.id.tvPraiseMoney);
        tvOrderMoney = (TextView) findViewById(R.id.tvOrderMoney);

    }

    private void setView(){
        setPullAndFlexListener();
        getIncomeSummary();
    }

    private void setPullAndFlexListener(){
//        ff_list.setFlexView(ll_ad);
//        ff_list.setFlexible(true);

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
                getIncomeSummary();
            }
        });

    }

    private void initListener() {

        rlMineCharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyIncomeActivity.this,IncomeChargeActivity.class);
                intent.putExtra("fromType",IncomeChargeActivity.MSG_CHARGE);
                startActivity(intent);
            }
        });

        rlMinePraise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyIncomeActivity.this,IncomeChargeActivity.class);
                intent.putExtra("fromType",IncomeChargeActivity.MSG_PRAISE);
                startActivity(intent);
            }
        });
        rlMineOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyIncomeActivity.this,IncomeChargeActivity.class);
                intent.putExtra("fromType",IncomeChargeActivity.MSG_ORDER);
                startActivity(intent);
            }
        });

    }



    public void getIncomeSummary(){
        MineWebHelper.getIncomeSummary(new IModelResultListener<IncomeModel>() {
            @Override
            public boolean onGetResultModel(HttpResultModel resultModel) {
                pull_content.endRefresh(true);
                return false;
            }

            @Override
            public void onSuccess(String resultCode, IncomeModel resultModel, List<IncomeModel> resultModelList, String resultMsg, String hint) {
                pull_content.endRefresh(true);
                if(resultCode.equals("0")){
                    tvChargeMoney.setText(Html.fromHtml("<font color='#fd5c02' size=22>"+ DecimalUtil.FormatMoney(resultModel.chargeAmount/100) +"</font><font color=\'#565a5c\' size=22>元</font>"));
                    tvPraiseMoney.setText(Html.fromHtml("<font color='#fd5c02' size=22>"+ DecimalUtil.FormatMoney(resultModel.userLikeAmount/100) +"</font><font color=\'#565a5c\' size=22>元</font>"));
                    tvOrderMoney.setText(Html.fromHtml("<font color='#fd5c02' size=22>"+ DecimalUtil.FormatMoney(resultModel.goodsAmount/100) +"</font><font color=\'#565a5c\' size=22>元</font>"));

                    List<HoopChartView.HoopChart> datas = new ArrayList<>();
                    datas.add(new HoopChartView.HoopChart("充值收入" + resultModel.chargeRate + "%", resultModel.chargeRate/100, Color.parseColor("#0FF001")));
                    datas.add(new HoopChartView.HoopChart("点赞收入" + resultModel.userLikeRate + "%", resultModel.userLikeRate/100, Color.parseColor("#FFF001")));
                    datas.add(new HoopChartView.HoopChart("点餐收入" + resultModel.goodsRate + "%", resultModel.goodsRate/100, Color.parseColor("#FF0101")));
                    hoopChartView.refresh(datas);
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


}

