package com.imovie.mogic.mine;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.imovie.mogic.MyApplication;
import com.imovie.mogic.R;
import com.imovie.mogic.home.BaseActivity;
import com.imovie.mogic.login.model.TestModel;
import com.imovie.mogic.mine.net.MineWebHelper;
import com.imovie.mogic.utills.StringHelper;
import com.imovie.mogic.utills.Utills;
import com.imovie.mogic.web.IModelResultListener;
import com.imovie.mogic.web.model.HttpResultModel;
import com.imovie.mogic.widget.ClearButtonEditText;
import com.imovie.mogic.widget.TitleBar;

import java.util.List;

public class ResetPasswordActivity extends BaseActivity {

    private TitleBar titleBar;
    private ClearButtonEditText etNewPassword;
    private ClearButtonEditText etConfirmPassword;
    private ClearButtonEditText etOldPassword;
    private Button btnSetPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mine_reset_password);
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
        titleBar.setTitle("修改密码");
        titleBar.setLeftListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        etNewPassword = (ClearButtonEditText) findViewById(R.id.etNewPassword);
        etConfirmPassword = (ClearButtonEditText) findViewById(R.id.etConfirmPassword);
        etOldPassword = (ClearButtonEditText) findViewById(R.id.etOldPassword);
        btnSetPassword = (Button) findViewById(R.id.btnSetPassword);

    }

    private void setView(){


    }

    private void initListener() {
        btnSetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newPassword = etNewPassword.getText().toString();
                String confirmPassword = etConfirmPassword.getText().toString();
                String oldPassword = etOldPassword.getText().toString();

                if(StringHelper.isEmpty(newPassword)){
                    Utills.showShortToast("密码不能为空");
                    return;
                }
                if(StringHelper.isEmpty(confirmPassword)){
                    Utills.showShortToast("确认密码不能为空");
                    return;
                }
                if(StringHelper.isEmpty(oldPassword)){
                    Utills.showShortToast("旧密码不能为空");
                    return;
                }
                if(!newPassword.equals(confirmPassword)){
                    Utills.showShortToast("新密码不一致");
                    return;
                }
//                String password = MyApplication.getInstance().mPref.getString("passwordCard","");
//                if(StringHelper.isEmpty(password)) oldPassword = newPassword;
                setPassword(newPassword,oldPassword);
            }
        });
    }



    public void setPassword(String newPassword,String oldPassword){
        MineWebHelper.setPassword(newPassword,oldPassword,new IModelResultListener<TestModel>() {
            @Override
            public boolean onGetResultModel(HttpResultModel resultModel) {
                return false;
            }

            @Override
            public void onSuccess(String resultCode, TestModel resultModel, List<TestModel> resultModelList, String resultMsg, String hint) {
//                Log.e("----pass",resultCode);
//                Log.e("----msg",resultMsg);
                if(resultCode.equals("0")){
                    Utills.showShortToast("设置成功");
                    finish();
                }else {
                    if (!StringHelper.isEmpty(resultMsg)) {
                        Utills.showShortToast(resultMsg);
                    }
                }

            }

            @Override
            public void onFail(String resultCode, String resultMsg, String hint) {
                Utills.showShortToast("网络或服务器出错");
            }

            @Override
            public void onError(String errorMsg) {
                Utills.showShortToast("网络或服务器出错");
            }
        });
    }


}

