package com.imovie.mogic.mine.attend;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.imovie.mogic.MyApplication;
import com.imovie.mogic.R;
import com.imovie.mogic.base.universal_loading.YSBLoadingDialog;
import com.imovie.mogic.calendar.week.WeekViewEvent;
import com.imovie.mogic.config.AppConfig;
import com.imovie.mogic.config.HTTPConfig;
import com.imovie.mogic.home.BaseActivity;
import com.imovie.mogic.home.net.HomeWebHelper;
import com.imovie.mogic.login.model.MyDataModel;
import com.imovie.mogic.mine.attend.widget.CustomDatePicker;
import com.imovie.mogic.utills.DateUtil;
import com.imovie.mogic.utills.StringHelper;
import com.imovie.mogic.utills.Utills;
import com.imovie.mogic.web.IModelResultListener;
import com.imovie.mogic.web.common.BASE64Encoder;
import com.imovie.mogic.web.model.HttpResultModel;
import com.imovie.mogic.widget.FlexibleFrameLayout;
import com.imovie.mogic.widget.OnPhotoPopupWindowItemClickListener;
import com.imovie.mogic.widget.PullToRefreshFrameLayout;
import com.imovie.mogic.widget.RoundedImageView;
import com.imovie.mogic.widget.TakePhotoPopupWindow;
import com.imovie.mogic.widget.TitleBar;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.imovie.mogic.home.MainActivity.PHOTO_REQUEST_CUT;

public class AttendDayRecordActivity extends BaseActivity {

    private TitleBar titleBar;

    private EditText etTitleName;
    private TextView tvStartTime;
    private TextView tvEndTime;
    private Button btCommitImage;
    private CustomDatePicker customDatePicker;
    public String startTime;
    public String endTime;
    private int clickType = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attend_day_record_activity);
        initView();
        setView();
        if(getIntent().getIntExtra("selectTime",0)==2){
            long time = getIntent().getLongExtra("time", 0);
            startTime = DateUtil.TimeFormat(time, "yyyy-MM-dd HH:mm");
            endTime = DateUtil.TimeFormat(time + 3600000, "yyyy-MM-dd HH:mm");
        }else{
            WeekViewEvent event = (WeekViewEvent)getIntent().getSerializableExtra("event");
            long stime = event.getStartTime().getTimeInMillis();
            long etime = event.getEndTime().getTimeInMillis();
            startTime = DateUtil.TimeFormat(stime, "yyyy-MM-dd HH:mm");
            endTime = DateUtil.TimeFormat(etime, "yyyy-MM-dd HH:mm");
            etTitleName.setText(event.getName());
        }
        initDatePicker();
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

        etTitleName = (EditText) findViewById(R.id.etTitleName);
        tvStartTime = (TextView) findViewById(R.id.tvStartTime);
        tvEndTime = (TextView) findViewById(R.id.tvEndTime);
        btCommitImage = (Button) findViewById(R.id.btCommitImage);


        titleBar.setLeftListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        titleBar.enableRightTextView("提交", new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    private void setView(){
        titleBar.setTitle("添加事件");
//        setPullAndFlexListener();
//        getMyData();
    }

    private void initListener() {

        btCommitImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                String nickName = etNickName.getText().toString();
//                if (StringHelper.isEmpty(nickName)) {
//                    Utills.showShortToast("请填写昵称");
//                    return;
//                }
////                upLoadByAsyncHttpClient();
//                int adminId = MyApplication.getInstance().mPref.getInt("adminId", 0);
//                String fileStr = AppConfig.appPath + adminId + "_headerImage.jpg";
////                String file = ImageUtil.compressAndBase64Bitmap(ImageUtil.getBitmap(AppConfig.appPath + adminId + "_headerImage.jpg"));
//                String file = "data:image/png;base64,"+getImageStr(fileStr);
//                String shorDesc = etShortDesc.getText().toString();
//                updateMyData(nickName,shorDesc,file);
            }

        });

        tvStartTime.setText(startTime);
        tvEndTime.setText(endTime);
        tvStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickType = 0;
                customDatePicker.show(startTime);
            }
        });

        tvEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickType = 1;
                customDatePicker.show(endTime);
            }
        });
    }

    private void initDatePicker() {
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
//        String now = sdf.format(new Date());
//        String now = "2017-01-03 00:00";

        customDatePicker = new CustomDatePicker(this, new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) { // 回调接口，获得选中的时间
//                currentTime.setText(time);
                if(clickType==0){
                    startTime = time + ":00";
                    tvStartTime.setText(time);
                }else{
                    endTime = time + ":00";
                    tvEndTime.setText(time);
                }

            }
        }, "2000-01-03 00:00", "2030-01-03 00:00"); // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
        customDatePicker.showSpecificTime(true); // 显示时和分
        customDatePicker.setIsLoop(true); // 允许循环滚动
    }




//    public void getMyData(){
//        HomeWebHelper.getMy(new IModelResultListener<MyDataModel>() {
//            @Override
//            public boolean onGetResultModel(HttpResultModel resultModel) {
//                pull_content.endRefresh(true);
//                return false;
//            }
//
//            @Override
//            public void onSuccess(String resultCode, MyDataModel resultModel, List<MyDataModel> resultModelList, String resultMsg, String hint) {
//                pull_content.endRefresh(true);
//                if(resultCode.equals("0")) {
//                    SharedPreferences.Editor editor = MyApplication.getInstance().mPref.edit();
//                    editor.putInt("adminId",resultModel.adminId);
//                    editor.putInt("organId",resultModel.organId);
//                    editor.putString("nickName",resultModel.nickName);
//                    editor.putString("fackeImageUrl",resultModel.fackeImageUrl);
//                    editor.commit();
//                    etNickName.setText(resultModel.nickName);
//                    if(!StringHelper.isEmpty(resultModel.shortDesc)) etShortDesc.setText(resultModel.shortDesc);
//
//                }
//
//            }
//
//            @Override
//            public void onFail(String resultCode, String resultMsg, String hint) {
//                pull_content.endRefresh(true);
//            }
//
//            @Override
//            public void onError(String errorMsg) {
//                pull_content.endRefresh(true);
//            }
//        });
//    }

//    public void updateMyData(String nickName,String shorDesc,String fileStr){
//        YSBLoadingDialog.showLoadingDialog(this, 6000, new YSBLoadingDialog.OnCancelListener() {
//            @Override
//            public void onTimeout() {
//                YSBLoadingDialog.dismissDialog();
//            }
//
//            @Override
//            public void onCancel() {
//                YSBLoadingDialog.dismissDialog();
//            }
//        });
//        HomeWebHelper.updateMyInfo(nickName,shorDesc,fileStr,new IModelResultListener<MyDataModel>() {
//            @Override
//            public boolean onGetResultModel(HttpResultModel resultModel) {
//                pull_content.endRefresh(true);
//                return false;
//            }
//
//            @Override
//            public void onSuccess(String resultCode, MyDataModel resultModel, List<MyDataModel> resultModelList, String resultMsg, String hint) {
//                pull_content.endRefresh(true);
//                YSBLoadingDialog.dismissDialog();
//                Utills.showShortToast(resultMsg);
//                if(resultCode.equals("0")) {
//                    SharedPreferences.Editor editor = MyApplication.getInstance().mPref.edit();
//                    editor.putInt("adminId",resultModel.adminId);
//                    editor.putInt("organId",resultModel.organId);
//                    editor.putString("nickName",resultModel.nickName);
//                    editor.putString("fackeImageUrl",resultModel.fackeImageUrl);
//                    editor.commit();
//                }
//
//            }
//
//            @Override
//            public void onFail(String resultCode, String resultMsg, String hint) {
//                pull_content.endRefresh(true);
//            }
//
//            @Override
//            public void onError(String errorMsg) {
//                pull_content.endRefresh(true);
//            }
//        });
//    }



}

