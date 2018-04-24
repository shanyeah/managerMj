package com.imovie.mogic.mine.attend;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.imovie.mogic.R;
import com.imovie.mogic.calendar.SelectDateDialog;
import com.imovie.mogic.login.model.TestModel;
import com.imovie.mogic.mine.attend.model.AttendClock;
import com.imovie.mogic.mine.attend.widget.CustomDatePicker;
import com.imovie.mogic.mine.net.MineWebHelper;
import com.imovie.mogic.utills.StringHelper;
import com.imovie.mogic.utills.Utills;
import com.imovie.mogic.web.IModelResultListener;
import com.imovie.mogic.web.model.HttpResultModel;
import com.imovie.mogic.widget.TitleBar;

import java.util.List;

public class AttendClockActivity extends AppCompatActivity{

    private TitleBar titleBar;
    private TextView tvClockTime;
    private TextView tvSelectTime;
    private RelativeLayout rlClockState;
    private TextView tvClockState;
    private EditText etClockContent;
    private Button btClockCommit;
    public String selectDate;

    private SelectDateDialog mSelectDateDialog;
    private int mPosition = -1;

    private CustomDatePicker customDatePicker;
    public String startTime;
    public String endTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attend_clock_activity);
        initView();
        setView();
        initListener();
        int attendId = getIntent().getIntExtra("attendId",0);
        getRecheckInfo(attendId);
//        initDatePicker();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void initView(){
        titleBar = (TitleBar) findViewById(R.id.title_bar);
        titleBar.setTitle("补卡申请");
        titleBar.setLeftListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvClockTime = (TextView) findViewById(R.id.tvClockTime);
        tvSelectTime = (TextView) findViewById(R.id.tvSelectTime);
        rlClockState = (RelativeLayout) findViewById(R.id.rlClockState);
        tvClockState = (TextView) findViewById(R.id.tvClockState);
        etClockContent = (EditText) findViewById(R.id.etClockContent);
        btClockCommit = (Button) findViewById(R.id.btClockCommit);

    }

    private void setView(){
//        AttendAreaModel areaModel = (AttendAreaModel) getIntent().getSerializableExtra("areaModel");
//        if (areaModel.attendWayList.size()>0){
//            for(int i=0;i<areaModel.attendWayList.size();i++) {
//                if(areaModel.attendWayList.get(i).type==1) {
//                    tvAttendAddress.setText(areaModel.attendWayList.get(i).name);
//                }else{
//                    tvName.setText(areaModel.attendWayList.get(i).name);
//                    tvAddress.setText(areaModel.attendWayList.get(i).address);
//                }
//            }
//        }
    }

    private void initListener() {
        tvSelectTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                showSelectDateDialog();
                if(StringHelper.isEmpty(startTime))return;
                customDatePicker.show(startTime);
            }
        });

        btClockCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(StringHelper.isEmpty(selectDate)){
                    Utills.showShortToast("请先择补卡时间");
                    return;
                }

                String content = etClockContent.getText().toString();
                if(StringHelper.isEmpty(content)){
                    Utills.showShortToast("请输入补卡原因");
                    return;
                }
                int attendId = getIntent().getIntExtra("attendId",0);
                getRecheckClock(selectDate,content,attendId);
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
                selectDate = time + ":00";
                tvSelectTime.setText(time);
            }
        }, startTime, endTime); // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
        customDatePicker.showSpecificTime(true); // 显示时和分
        customDatePicker.setIsLoop(true); // 允许循环滚动
    }

    public void getRecheckClock(String date,String applyReason,int attendRecordId){
        MineWebHelper.getRecheckClock(date,applyReason,attendRecordId,new IModelResultListener<TestModel>() {
            @Override
            public boolean onGetResultModel(HttpResultModel resultModel) {
                return false;
            }

            @Override
            public void onSuccess(String resultCode, TestModel resultModel, List<TestModel> resultModelList, String resultMsg, String hint) {

                if(resultCode.equals("0")) {
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

    public void getRecheckInfo(int attendRecordId){
        MineWebHelper.getRecheckInfo(attendRecordId,new IModelResultListener<AttendClock>() {
            @Override
            public boolean onGetResultModel(HttpResultModel resultModel) {
                return false;
            }

            @Override
            public void onSuccess(String resultCode, AttendClock resultModel, List<AttendClock> resultModelList, String resultMsg, String hint) {

                if(resultCode.equals("0")) {
                    tvClockTime.setText(resultModel.note);
                    if(resultModel.selectDate.size()>0){
                        rlClockState.setVisibility(View.GONE);
//                        startTime = resultModel.startTime.substring(0,resultModel.startTime.length()-3);
//                        endTime = resultModel.endTime.substring(0,resultModel.endTime.length()-3);
                        startTime = resultModel.selectDate.get(0)+ " 00:00";
                        endTime = resultModel.selectDate.get(0)+ " 23:59";
//                        startTime = "2017-01-14 00:00";
//                        endTime = "2017-01-15 23:59";
                        btClockCommit.setVisibility(View.VISIBLE);
                        tvSelectTime.setText(resultModel.startTime.substring(0,resultModel.startTime.length()-3));
                        initDatePicker();
                    }else{
                        btClockCommit.setVisibility(View.GONE);
                        rlClockState.setVisibility(View.VISIBLE);
                        etClockContent.setText(resultModel.applyReason);
                        tvSelectTime.setText(resultModel.extraDate);
                        switch (resultModel.status){
                            case 0:
                                tvClockState.setText("审批中");
                                break;
                            case 1:
                                tvClockState.setText("审核通过");
                                break;
                            case 2:
                                tvClockState.setText("审核不通过");
                                break;
                        }
                    }

//                    Utills.showShortToast(resultMsg+resultModel.selectDate.get(0));
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

