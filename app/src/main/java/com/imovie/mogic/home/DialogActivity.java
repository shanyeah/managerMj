package com.imovie.mogic.home;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.imovie.mogic.R;
import com.imovie.mogic.home.model.AuthCodeModel;
import com.imovie.mogic.home.net.HomeWebHelper;
import com.imovie.mogic.login.model.TestModel;
import com.imovie.mogic.utills.Utills;
import com.imovie.mogic.web.IModelResultListener;
import com.imovie.mogic.web.model.HttpResultModel;

import java.util.List;

public class DialogActivity extends Activity {

    public static final int MSG_TOLOGIN = 60;
    public static final int MSG_TOMAIN= 61;

    private TextView tvCancel;
    private TextView tvOk;
    private TextView tvAuthContent;
    private AuthCodeModel authCode = new AuthCodeModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_dialog_activity);
        initView();
        setListeners();
        getScanAuthCode(getIntent().getStringExtra("data"));
    }

    private void initView() {
        tvCancel = (TextView) findViewById(R.id.tvCancel);
        tvOk = (TextView) findViewById(R.id.tvOk);
        tvAuthContent = (TextView) findViewById(R.id.tvAuthContent);
    }

    private void setListeners() {
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getConfirmAuth(authCode.authId);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    public void getScanAuthCode(String code){
        HomeWebHelper.getScanAuthCode(code, new IModelResultListener<AuthCodeModel>() {
            @Override
            public boolean onGetResultModel(HttpResultModel resultModel) {
                return false;
            }

            @Override
            public void onSuccess(String resultCode, AuthCodeModel resultModel, List<AuthCodeModel> resultModelList, String resultMsg, String hint) {

                    if(resultCode.equals("0")) {
                        authCode = resultModel;
                        tvAuthContent.setText(""+resultModel.remark);

//                        refreshData(resultModel.list);authId":11,"remark":"王重文申请[收银签单支付]操作的授权
                    }else{
                        Utills.showShortToast(resultMsg);
                        finish();
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

    public void getConfirmAuth(int authId){
        HomeWebHelper.getConfirmAuth(authId, new IModelResultListener<TestModel>() {
            @Override
            public boolean onGetResultModel(HttpResultModel resultModel) {
                return false;
            }

            @Override
            public void onSuccess(String resultCode, TestModel resultModel, List<TestModel> resultModelList, String resultMsg, String hint) {
                if(resultCode.equals("0")) {
                    Utills.showShortToast(resultMsg);
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


}

