package com.imovie.mogic.login;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.imovie.mogic.MyApplication;
import com.imovie.mogic.R;
import com.imovie.mogic.home.BaseActivity;
import com.imovie.mogic.login.model.TestModel;
import com.imovie.mogic.login.net.LoginWebHelper;
import com.imovie.mogic.utills.StringHelper;
import com.imovie.mogic.utills.WeakHandler;
import com.imovie.mogic.web.IModelResultListener;
import com.imovie.mogic.web.model.HttpResultModel;
import com.imovie.mogic.widget.ClearButtonEditText;
import com.imovie.mogic.widget.VerifyCoder;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RegisterActivity extends BaseActivity {

    public int result=0;
    public ClearButtonEditText etName;
    public ClearButtonEditText etNumber;
    public ClearButtonEditText etPhone;
    public ClearButtonEditText etPassword;
    public VerifyCoder verifyCoder;
    public Button btnCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
        initListener();

    }

    @Override
    public void onBackPressed() {
        Intent mIntent = new Intent();
        mIntent.putExtra("result",LoginActivity.REQUEST_FAIL);
        RegisterActivity.this.setResult(LoginActivity.REQUEST_LOGIN, mIntent);
        super.onBackPressed();
        finish();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    private void initView() {
        etName = (ClearButtonEditText) findViewById(R.id.etName);
        etNumber = (ClearButtonEditText) findViewById(R.id.etNumber);
        etPhone = (ClearButtonEditText) findViewById(R.id.et_phone);
        etPassword = (ClearButtonEditText) findViewById(R.id.et_password);

        verifyCoder = (VerifyCoder) findViewById(R.id.password_verifycoder);
        btnCode = (Button) findViewById(R.id.btn_code);

    }

    private void initListener() {

        etPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                verifyCoder.setCoder(etPhone.getText().toString() + "",0);
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
        String name = etName.getText().toString();
        String number = etNumber.getText().toString();
        String phone = etPhone.getText().toString();
        String password = etPassword.getText().toString();
        String authCode = verifyCoder.getInputVerifyCode();
        if (StringHelper.isEmpty(name)) {
            Toast.makeText(RegisterActivity.this,"名字不能为空",Toast.LENGTH_SHORT).show();
            return;
        }
        if (StringHelper.isEmpty(number)) {
            Toast.makeText(RegisterActivity.this,"身份证号不能为空",Toast.LENGTH_SHORT).show();
            return;
        }

        if (StringHelper.isEmpty(phone)) {
            Toast.makeText(RegisterActivity.this,"请输入正确的手机号",Toast.LENGTH_SHORT).show();
            return;
        }
        if (StringHelper.isEmpty(password)) {
            Toast.makeText(RegisterActivity.this,"密码不能为空",Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.length() < 6) {
            Toast.makeText(RegisterActivity.this,"密码长度过短,请重新设置",Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.length() > 16) {
            Toast.makeText(RegisterActivity.this,"密码长度过长,请重新设置",Toast.LENGTH_SHORT).show();
            return;
        }

        if (StringHelper.isEmpty(authCode)) {
            Toast.makeText(RegisterActivity.this,"请输入验证码",Toast.LENGTH_SHORT).show();
            return;
        }

        LoginWebHelper.getRegister(name,password,0,phone,authCode,number, new IModelResultListener<TestModel>() {
            @Override
            public boolean onGetResultModel(HttpResultModel resultModel) {
                return false;
            }

            @Override
            public void onSuccess(String resultCode, TestModel resultModel, List<TestModel> resultModelList, String resultMsg, String hint) {
//                Toast.makeText(RegisterActivity.this,"注册成功",Toast.LENGTH_SHORT).show();
                TestModel testModel = new TestModel();
                testModel.setModelByJson(resultCode);
                if(testModel.code.equals("0")){
                    Toast.makeText(RegisterActivity.this,""+resultMsg,Toast.LENGTH_SHORT).show();
                    result=LoginActivity.REQUEST_FAIL;
                }else{
                    Toast.makeText(RegisterActivity.this,"注册成功",Toast.LENGTH_SHORT).show();
                    SharedPreferences.Editor editor = MyApplication.getInstance().mPref.edit();
                    editor.putString("phone",etPhone.getText().toString());
                    editor.putString("password",etPassword.getText().toString());
                    editor.putBoolean("isLogin",true);
                    editor.commit();
                    Intent mIntent = new Intent();
                    mIntent.putExtra("result",LoginActivity.REQUEST_SUCCESS);
                    RegisterActivity.this.setResult(LoginActivity.REQUEST_LOGIN, mIntent);
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

