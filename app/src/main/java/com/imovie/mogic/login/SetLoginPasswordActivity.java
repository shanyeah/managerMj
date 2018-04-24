package com.imovie.mogic.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.imovie.mogic.MyApplication;
import com.imovie.mogic.R;
import com.imovie.mogic.home.BaseActivity;
import com.imovie.mogic.login.model.TestModel;
import com.imovie.mogic.login.net.LoginWebHelper;
import com.imovie.mogic.utills.StringHelper;
import com.imovie.mogic.web.IModelResultListener;
import com.imovie.mogic.web.model.HttpResultModel;
import com.imovie.mogic.webview.activity.WebViewActivity;
import com.imovie.mogic.widget.ClearButtonEditText;
import com.imovie.mogic.widget.TitleBar;
import com.imovie.mogic.widget.VerifyCoder;

import java.util.List;

public class SetLoginPasswordActivity extends BaseActivity {

    public int result=0;
    public TitleBar navigationbar;
    public ClearButtonEditText etPhone;
    public ClearButtonEditText etPassword;
    public VerifyCoder verifyCoder;
    public Button btnCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_login_password);
        initView();
        initListener();

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

    private void initView() {
        navigationbar = (TitleBar)findViewById(R.id.navigationbar);
        navigationbar.setTitle("修改登录密码");
        etPhone = (ClearButtonEditText) findViewById(R.id.et_phone);
        etPassword = (ClearButtonEditText) findViewById(R.id.et_password);

        verifyCoder = (VerifyCoder) findViewById(R.id.password_verifycoder);
        btnCode = (Button) findViewById(R.id.btn_code);

    }

    private void initListener() {

        navigationbar.setLeftListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }

        });

        etPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                verifyCoder.setCoder(etPhone.getText().toString() + "",1);
            }
        });
        btnCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCodeButton();
            }
        });
    }

    private void setCodeButton() {
        String phone = etPhone.getText().toString();
        String password = etPassword.getText().toString();
        String authCode = verifyCoder.getInputVerifyCode();

        if (StringHelper.isEmpty(phone)) {
            Toast.makeText(SetLoginPasswordActivity.this,"请输入正确的手机号",Toast.LENGTH_SHORT).show();
            return;
        }
        if (StringHelper.isEmpty(password)) {
            Toast.makeText(SetLoginPasswordActivity.this,"新密码不能为空",Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.length() < 6) {
            Toast.makeText(SetLoginPasswordActivity.this,"密码长度过短,请重新设置",Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.length() > 16) {
            Toast.makeText(SetLoginPasswordActivity.this,"密码长度过长,请重新设置",Toast.LENGTH_SHORT).show();
            return;
        }

        if (StringHelper.isEmpty(authCode)) {
            Toast.makeText(SetLoginPasswordActivity.this,"请输入验证码",Toast.LENGTH_SHORT).show();
            return;
        }

        LoginWebHelper.setLoginPassword(phone,password,authCode, new IModelResultListener<TestModel>() {
            @Override
            public boolean onGetResultModel(HttpResultModel resultModel) {
                return false;
            }

            @Override
            public void onSuccess(String resultCode, TestModel resultModel, List<TestModel> resultModelList, String resultMsg, String hint) {
                Log.e("----pass",resultCode);
                TestModel testModel = new TestModel();
                testModel.setModelByJson(resultCode);
                if(testModel.code.equals("0")){
                    Toast.makeText(SetLoginPasswordActivity.this,""+resultMsg,Toast.LENGTH_SHORT).show();
                    result=LoginActivity.REQUEST_FAIL;
                }else{
                    Toast.makeText(SetLoginPasswordActivity.this,"成功更改密码",Toast.LENGTH_SHORT).show();
                    SharedPreferences.Editor editor = MyApplication.getInstance().mPref.edit();
                    editor.putString("phone",etPhone.getText().toString());
                    editor.putString("password",etPassword.getText().toString());
                    editor.commit();
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

}

