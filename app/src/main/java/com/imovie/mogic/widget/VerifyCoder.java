package com.imovie.mogic.widget;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import android.widget.*;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import com.imovie.mogic.R;
import com.imovie.mogic.config.HTTPConfig;
import com.imovie.mogic.dbbase.model.CoreFuncReturn;
import com.imovie.mogic.login.model.TestModel;
import com.imovie.mogic.login.net.LoginWebHelper;
import com.imovie.mogic.utills.StringHelper;
import com.imovie.mogic.web.BaseWebHelper;
import com.imovie.mogic.web.IModelResultListener;
import com.imovie.mogic.web.IResultListener;
import com.imovie.mogic.web.model.HttpResultModel;

/**
 * Created by zhou on 2015/2/2.
 */
public class VerifyCoder extends LinearLayout {
    private Context context;
    private String Tel;
    private int type;

    //widgets
    private ClearButtonEditText et_input;
    private EditText et_password;
    private Button iv_code;
    private TextView tv_hintMessage;
    private TextView tv_verify_code_state;
    private AsyncTask<Integer, Integer, Integer> mAsyncTask;
    public VerifyCoder(Context context) {
        super(context);
        this.context = context;
        initViews();
        setViews();
    }
    public VerifyCoder(Context context, AttributeSet attrs){
        super(context, attrs);
        this.context = context;
        initViews();
        setViews();
    }


    public void setInputLoseFocus(EditText password){
                this.et_password = password;
    }
    public void setCoder(String tel, int type){
        this.Tel = tel;
        this.type = type;
//        initViews();
//        setViews();

    }

    private void initViews(){
        LinearLayout parent = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.verifycoder, this);
        LinearLayout code_layout = (LinearLayout) parent.findViewById(R.id.security_get_code_layout);
        et_input = (ClearButtonEditText) parent.findViewById(R.id.security_verification_code);
        iv_code = (Button) parent.findViewById(R.id.security_get_code_iv);
        tv_hintMessage = (TextView) parent.findViewById(R.id.hintMessage);
        tv_verify_code_state = (TextView) parent.findViewById(R.id.tv_verify_code_state);

//        LayoutParams code_layoutParams = (LayoutParams) code_layout.getLayoutParams();
//        int screenWidth = getResources().getDisplayMetrics().widthPixels;
//        code_layoutParams.height = screenWidth *90/640;
//        code_layout.setLayoutParams(code_layoutParams);
    }
    private void setViews(){
        iv_code.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                String mobile = Tel;
//                boolean isValid = AddressInfoHelper.checkValidation(AddressInfoHelper.MOBILE_VALID, phoneNumberStr + "");

//                if (et_password != null) {
//                    et_password.clearFocus();
//                }
                if (StringHelper.isEmpty(mobile)) {
                    Toast.makeText(context, "请输入正确手机号", Toast.LENGTH_SHORT).show();
                } else {
                    try {
//                        if (!phoneNumberStr.isEmpty()) {
                            time();

                            LoginWebHelper.getRegAuth(mobile,type, new IModelResultListener<TestModel>() {
                                @Override
                                public boolean onGetResultModel(HttpResultModel resultModel) {
                                    return false;
                                }

                                @Override
                                public void onSuccess(String resultCode, TestModel resultModel, List<TestModel> resultModelList, String resultMsg, String hint) {

//                                    Toast.makeText(context, "" + resultMsg, Toast.LENGTH_SHORT).show();
                                    et_input.requestFocus();
//                                    if ("code".equals("40001")) {
//                                                iv_code.setEnabled(true);
//                                                iv_code.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_code_normal));
//                                                iv_code.setText("获取验证码");
//                                                tv_hintMessage.setVisibility(View.GONE);
//                                                mAsyncTask.cancel(true);
//
//                                            }else{
//                                                iv_code.setEnabled(true);
//                                                iv_code.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_code_normal));
//                                                iv_code.setText("获取验证码");
//                                                tv_hintMessage.setVisibility(View.GONE);
//                                                mAsyncTask.cancel(true);
//                                            }

                                }

                                @Override
                                public void onFail(String resultCode, String resultMsg, String hint) {

                                }

                                @Override
                                public void onError(String errorMsg) {

                                }
                            });

                    } catch (Exception e) {
                        e.getStackTrace();
                        Toast.makeText(context, "网络异常", Toast.LENGTH_SHORT).show();
                    }
                }


            }
        });
    }

    public void setCodeStateShow(boolean bl){
        if(bl){
            tv_verify_code_state.setVisibility(VISIBLE);
        }else{
            tv_verify_code_state.setVisibility(GONE);
        }


    }
    private void time(){
        mAsyncTask =  new AsyncTask<Integer, Integer, Integer>() {
            Timer timer;
            int seconds;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                iv_code.setEnabled(false);
                iv_code.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_code_enable));
                iv_code.setText("已获取验证码");
                tv_hintMessage.setVisibility(View.VISIBLE);
                timer = new Timer();
                seconds = 30;
            }
            @Override
            protected Integer doInBackground(Integer... integers) {
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        seconds--;
                        publishProgress(seconds);
                        if(seconds==0) {
                            timer.cancel();
                        }
                    }
                }, 0, 1000);
                return null;
            }
            @Override
            protected void onProgressUpdate(Integer... values) {
                super.onProgressUpdate(values);
                tv_hintMessage.setText("如果您没有收到"+values[0]+"秒后再点击重发");
                if(values[0]==0){
                    iv_code.setEnabled(true);
                    iv_code.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_code_normal));
                    iv_code.setText("获取验证码");
                    tv_hintMessage.setVisibility(View.GONE);
                }
            }
            @Override
            protected void onPostExecute(Integer integer) {
                super.onPostExecute(integer);
            }
        }.execute(0,0,0);
    }
    public String getInputVerifyCode(){
        return et_input.getText().toString();
    }

}
