package com.imovie.mogic.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.imovie.mogic.MyApplication;
import com.imovie.mogic.R;
import com.imovie.mogic.base.universal_loading.YSBLoadingDialog;
import com.imovie.mogic.chat.db.DemoHelper;
import com.imovie.mogic.home.BaseActivity;
import com.imovie.mogic.home.MainActivity;
import com.imovie.mogic.login.model.LoginModel;
import com.imovie.mogic.login.net.LoginWebHelper;
import com.imovie.mogic.utills.StringHelper;
import com.imovie.mogic.web.IModelResultListener;
import com.imovie.mogic.web.model.HttpResultModel;
import com.imovie.mogic.widget.ClearButtonEditText;

import java.util.List;

public class LoginActivity extends BaseActivity {
    public static final int REQUEST_SUCCESS = 1;
    public static final int REQUEST_FAIL = 2;
    public static final int REQUEST_LOGIN = 300;
    private Button btnLogin;
    private TextView tvRegister;
    private TextView tvForget;
    private ClearButtonEditText etUserName;
    private ClearButtonEditText etPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        initListener();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void getLogin(){
        String phone = etUserName.getText().toString();
        String password = etPassword.getText().toString();
        if (StringHelper.isEmpty(phone)) {
            Toast.makeText(this,"请输入正确的帐号",Toast.LENGTH_SHORT).show();
            return;
        }
        if (StringHelper.isEmpty(password)) {
            Toast.makeText(this,"密码不能为空",Toast.LENGTH_SHORT).show();
            return;
        }

        YSBLoadingDialog.showLoadingDialog(this, 3000, new YSBLoadingDialog.OnCancelListener() {
            @Override
            public void onTimeout() {
                YSBLoadingDialog.dismissDialog();
            }

            @Override
            public void onCancel() {
                YSBLoadingDialog.dismissDialog();
            }
        });

        LoginWebHelper.getLogin(password,phone,new IModelResultListener<LoginModel>() {
            @Override
            public boolean onGetResultModel(HttpResultModel resultModel) {
                return false;
            }

            @Override
            public void onSuccess(String resultCode, LoginModel resultModel, List<LoginModel> resultModelList, String resultMsg, String hint) {
//                LoginModel loginModel = new LoginModel();
//                loginModel.setModelByJson(resultCode);
                if(resultCode.equals("0")){
                    Toast.makeText(LoginActivity.this,"登录成功",Toast.LENGTH_SHORT).show();
                    SharedPreferences.Editor editor = MyApplication.getInstance().mPref.edit();
                    editor.putString("phone",etUserName.getText().toString());
                    editor.putString("password",etPassword.getText().toString());
                    editor.putBoolean("isLogin",true);
                    editor.putInt("adminId",resultModel.adminId);
                    editor.putInt("organId",resultModel.organId);
                    editor.putString("token",resultModel.token);
                    editor.putString("nickName",resultModel.nickName);
                    editor.putString("fackeImageUrl",resultModel.fackeImageUrl);
                    editor.commit();
                    loginIM(etUserName.getText().toString(),etPassword.getText().toString(),"");

                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(LoginActivity.this,""+resultMsg,Toast.LENGTH_SHORT).show();
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


    private void initView() {
        String phone = MyApplication.getInstance().mPref.getString("phone","");
        if(!StringHelper.isEmpty(phone))etUserName.setText(phone);

        btnLogin = (Button) findViewById(R.id.btn_login);
        tvRegister = (TextView) findViewById(R.id.tv_register);
        tvForget = (TextView) findViewById(R.id.tv_forget);
        etUserName = (ClearButtonEditText) findViewById(R.id.et_username);
        etPassword = (ClearButtonEditText) findViewById(R.id.et_password);

    }

    private void initListener() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLogin();
            }
        });
        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivityForResult(intent,REQUEST_LOGIN);

            }
        });
        tvForget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,SetLoginPasswordActivity.class);
                startActivity(intent);
            }
        });
    }

    public void loginIM(String currentUsername,String currentPassword,String faceImageUrl) {
        Log.e("----sch333",""+currentUsername+":----" + currentPassword);
//        if (!EaseCommonUtils.isNetWorkConnected(this)) {
//            Toast.makeText(this, R.string.network_isnot_available, Toast.LENGTH_SHORT).show();
//            return;
//        }
//        String currentUsername = usernameEditText.getText().toString().trim();
//        String currentPassword = passwordEditText.getText().toString().trim();
//
//        if (TextUtils.isEmpty(currentUsername)) {
//            Toast.makeText(this, R.string.User_name_cannot_be_empty, Toast.LENGTH_SHORT).show();
//            return;
//        }
//        if (TextUtils.isEmpty(currentPassword)) {
//            Toast.makeText(this, R.string.Password_cannot_be_empty, Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        progressShow = true;
//        final ProgressDialog pd = new ProgressDialog(LoginActivity.this);
//        pd.setCanceledOnTouchOutside(false);
//        pd.setOnCancelListener(new DialogInterface.OnCancelListener() {
//
//            @Override
//            public void onCancel(DialogInterface dialog) {
//                Log.d(TAG, "EMClient.getInstance().onCancel");
//                progressShow = false;
//            }
//        });
//        pd.setMessage(getString(R.string.Is_landing));
//        pd.show();
//
//        // After logout，the DemoDB may still be accessed due to async callback, so the DemoDB will be re-opened again.
//        // close it before login to make sure DemoDB not overlap
//        DemoDBManager.getInstance().closeDB();

        // reset current user name before login
        DemoHelper.getInstance().setCurrentUserName(currentUsername);
//        DemoHelper.getInstance().getUserProfileManager().setCurrentUserAvatar(""+faceImageUrl);

        final long start = System.currentTimeMillis();
        // call login method
//        Log.d(TAG, "EMClient.getInstance().login");
        EMClient.getInstance().login(currentUsername, currentPassword, new EMCallBack() {

            @Override
            public void onSuccess() {
//                Log.d(TAG, "login: onSuccess");


                // ** manually load all local groups and conversation
                EMClient.getInstance().groupManager().loadAllGroups();
                EMClient.getInstance().chatManager().loadAllConversations();

                // update current user's display name for APNs
//                boolean updatenick = EMClient.getInstance().pushManager().updatePushNickname(
//                        DemoApplication.currentUserNick.trim());
//                if (!updatenick) {
//                    Log.e("LoginActivity", "update current user nick fail");
//                }
//
//                if (!LoginActivity.this.isFinishing() && pd.isShowing()) {
//                    pd.dismiss();
//                }
                // get user's info (this should be get from App's server or 3rd party service)
                DemoHelper.getInstance().getUserProfileManager().asyncGetCurrentUserInfo();

//                Intent intent = new Intent(LoginActivity.this,
//                        MainActivity.class);
//                startActivity(intent);
//
//                finish();
            }

            @Override
            public void onProgress(int progress, String status) {
//                Log.d(TAG, "login: onProgress");
            }

            @Override
            public void onError(final int code, final String message) {
//                Log.d(TAG, "login: onError: " + code);
//                if (!progressShow) {
//                    return;
//                }
//                runOnUiThread(new Runnable() {
//                    public void run() {
//                        pd.dismiss();
//                        Toast.makeText(getApplicationContext(), getString(R.string.Login_failed) + message,
//                                Toast.LENGTH_SHORT).show();
//                    }
//                });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        int result = data.getIntExtra("result",0);
        switch (requestCode) {
            case REQUEST_LOGIN:
                if(result==LoginActivity.REQUEST_SUCCESS) {
                    Intent mIntent = new Intent();
                    mIntent.putExtra("result", LoginActivity.REQUEST_SUCCESS);
                    LoginActivity.this.setResult(LoginActivity.REQUEST_LOGIN, mIntent);
                    finish();
                }

                break;

        }
    }
}

