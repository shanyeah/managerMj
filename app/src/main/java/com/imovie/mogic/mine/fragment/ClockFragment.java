package com.imovie.mogic.mine.fragment;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.imovie.mogic.MyApplication;
import com.imovie.mogic.R;
import com.imovie.mogic.base.universal_loading.YSBLoadingDialog;
import com.imovie.mogic.calendar.SelectDateDialog;
import com.imovie.mogic.login.model.TestModel;
import com.imovie.mogic.mine.MyAttendActivity;
import com.imovie.mogic.mine.adapter.ClockAdapter;
import com.imovie.mogic.mine.attend.AttendAreaActivity;
import com.imovie.mogic.mine.attend.widget.AttendDialog;
import com.imovie.mogic.mine.model.AttendAreaModel;
import com.imovie.mogic.mine.model.AttendInfo;
import com.imovie.mogic.mine.model.AttendModel;
import com.imovie.mogic.mine.model.ClockInfo;
import com.imovie.mogic.mine.model.ClockModel;
import com.imovie.mogic.mine.net.MineWebHelper;
import com.imovie.mogic.utills.DateUtil;
import com.imovie.mogic.utills.StringHelper;
import com.imovie.mogic.utills.Utills;
import com.imovie.mogic.web.IModelResultListener;
import com.imovie.mogic.web.model.HttpResultModel;
import com.imovie.mogic.widget.FlexibleFrameLayout;
import com.imovie.mogic.widget.NoScrollListView;
import com.imovie.mogic.widget.PullToRefreshFrameLayout;
import com.imovie.mogic.widget.RoundedImageView;
import com.imovie.mogic.widget.TitleBar;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ClockFragment extends Fragment implements SelectDateDialog.OnSelectDateListener{
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private TitleBar titleBar;
    private PullToRefreshFrameLayout pull_content;
    private FlexibleFrameLayout ff_list;
    public NoScrollListView lvClockList;
    public ArrayList<ClockModel> list;
    public ClockAdapter adapter;
    public TextView tvNameOval;
    public TextView tvName;
    public TextView tvClockGroup;
    public TextView tvClock;
    public TextView tvClockTime;
    public TextView tvClockArea;
    public TextView tvOpenArea;
    public TextView tvNoData;
    public RelativeLayout rlClockButton;
    public RelativeLayout rlClockArea;
    public RelativeLayout rlNoAttendData;
    public RoundedImageView profile;
    private DisplayImageOptions mOption;
    public int clockType = 0;
    public ClockInfo clockInfo = new ClockInfo();
    public String selectDate;
    private SelectDateDialog mSelectDateDialog;
    private int mCurrentSelectYear, mCurrentSelectMonth, mCurrentSelectDay;
    private int mPosition = -1;
    public AttendAreaModel areaModel = new AttendAreaModel();

    public ClockFragment() {

    }

    public static ClockFragment newInstance(String param1, String param2) {
        ClockFragment fragment = new ClockFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        selectDate = sdf.format(date); // 当期日期
        mOption = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.person_default_profile)
                .showImageOnFail(R.drawable.person_default_profile)
                .showImageForEmptyUri(R.drawable.person_default_profile)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.clock_fragment, container, false);
        initView(v);
        setView();
        setListener();
        refreshData();
        return v;
    }

    private void initView(View view) {
        titleBar = (TitleBar) view.findViewById(R.id.title_bar);
        titleBar.setTitle("打卡");
        titleBar.setLeftLayoutGone();
        titleBar.enableRightTextView("统计", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), MyAttendActivity.class);
                startActivity(intent);
            }
        });
        pull_content = (PullToRefreshFrameLayout) view.findViewById(R.id.pull_content);
        ff_list = (FlexibleFrameLayout) view.findViewById(R.id.ff_list);
        tvNameOval = (TextView) view.findViewById(R.id.tvNameOval);
        tvName = (TextView) view.findViewById(R.id.tvName);
        tvClockGroup = (TextView) view.findViewById(R.id.tvClockGroup);
        lvClockList = (NoScrollListView) view.findViewById(R.id.lvClockList);
        tvClock = (TextView) view.findViewById(R.id.tvClock);
        rlClockButton = (RelativeLayout) view.findViewById(R.id.rlClockButton);
        rlClockArea = (RelativeLayout) view.findViewById(R.id.rlClockArea);
        tvClockArea = (TextView) view.findViewById(R.id.tvClockArea);
        rlNoAttendData = (RelativeLayout) view.findViewById(R.id.rlNoAttendData);
        tvClockTime = (TextView) view.findViewById(R.id.tvClockTime);
        tvOpenArea = (TextView) view.findViewById(R.id.tvOpenArea);
        tvNoData = (TextView) view.findViewById(R.id.tvNoData);
        profile= (RoundedImageView) view.findViewById(R.id.profile);
    }

    private void setView() {
        setPullAndFlexListener();
        list = new ArrayList<>();
        adapter = new ClockAdapter(getContext(), list);
        lvClockList.setAdapter(adapter);
        tvClockTime.setText(selectDate.replace("-","."));

        ImageLoader.getInstance().displayImage(MyApplication.getInstance().mPref.getString("fackeImageUrl",""),profile,mOption);
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
                refreshData();
            }
        });

    }

    public void refreshData(){
        getAttendInfo(selectDate);
        getAttendArea();
        getAttendRecords(selectDate);
    }
    private void setListener() {
        tvClock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(areaModel.attendArea){
                    if(clockType == 1 && clockInfo!=null && clockInfo.planTime!=null && DateUtil.getCurrentTime()<DateUtil.StringToLongDate(clockInfo.planTime,"yyyy-MM-dd HH:mm:ss")){
                        AttendDialog dialog = new AttendDialog(getContext());
                        dialog.setContent("还没到下班时间，确定要打卡吗？");
                        dialog.setOnSelectListener(new AttendDialog.onSelectListener() {
                            @Override
                            public void onSelect(String type) {
                                getClock(selectDate, clockType, clockInfo.scheduleId, areaModel.type);
                                getAttendRecords(selectDate);
                            }
                        });
                        dialog.show();
                    }else {
                        if(clockInfo!=null && clockInfo.status==999){
                            Utills.showShortToast("还没到打卡时间");
                            return;
                        }
                        getClock(selectDate, clockType, clockInfo.scheduleId, areaModel.type);
                        getAttendRecords(selectDate);
                    }

                }else{
                    Utills.showShortToast("不在考勤范围内，请检查wifi或定位");
                    getAttendArea();
                }

            }
        });

        tvClockTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSelectDateDialog();
            }
        });

        tvOpenArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AttendAreaActivity.class);
                intent.putExtra("areaModel",areaModel);
                startActivity(intent);
            }
        });
    }

    @SuppressLint("WrongConstant")
    private void showSelectDateDialog() {
        if (mSelectDateDialog == null) {
            Calendar calendar = Calendar.getInstance();
            mSelectDateDialog = new SelectDateDialog(getContext(), (SelectDateDialog.OnSelectDateListener) this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), mPosition);
        }
        mSelectDateDialog.show();
    }


    public void getAttendArea(){
        MineWebHelper.getAttendArea(new IModelResultListener<AttendAreaModel>() {
            @Override
            public boolean onGetResultModel(HttpResultModel resultModel) {
                return false;
            }

            @Override
            public void onSuccess(String resultCode, AttendAreaModel resultModel, List<AttendAreaModel> resultModelList, String resultMsg, String hint) {
                if(resultCode.equals("0")) {
                    areaModel = resultModel;
                    if(areaModel.attendArea) {
//                        rlClockArea.setVisibility(View.VISIBLE);
                        if(areaModel.attendWayList.size()>0) {
                            tvClockArea.setText("已进入考勤范围:" + areaModel.attendWayList.get(0).name);
                        }else{
                            tvClockArea.setText("已进入考勤范围内");
                        }
                    }else{
//                        rlClockArea.setVisibility(View.GONE);
                        tvClockArea.setText("不在考勤范围内...");
                    }
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

    public void getClock(String date,int type,int scheduleId,int attendWay){
        YSBLoadingDialog.showLoadingDialog(getContext(), 3000, new YSBLoadingDialog.OnCancelListener() {
            @Override
            public void onTimeout() {
                YSBLoadingDialog.dismissDialog();
            }

            @Override
            public void onCancel() {
                YSBLoadingDialog.dismissDialog();
            }
        });
        MineWebHelper.getClock(date,type,scheduleId,attendWay,new IModelResultListener<TestModel>() {
            @Override
            public boolean onGetResultModel(HttpResultModel resultModel) {
                return false;
            }

            @Override
            public void onSuccess(String resultCode, TestModel resultModel, List<TestModel> resultModelList, String resultMsg, String hint) {
                Utills.showShortToast(resultMsg);
            }

            @Override
            public void onFail(String resultCode, String resultMsg, String hint) {

            }

            @Override
            public void onError(String errorMsg) {

            }
        });
    }

    public void getAttendInfo(String date){
        MineWebHelper.getAttendInfo(date,new IModelResultListener<AttendInfo>() {
            @Override
            public boolean onGetResultModel(HttpResultModel resultModel) {
                return false;
            }

            @Override
            public void onSuccess(String resultCode, AttendInfo resultModel, List<AttendInfo> resultModelList, String resultMsg, String hint) {
                if(resultCode.equals("0")) {
                    tvNameOval.setText(resultModel.name);
                    tvName.setText(resultModel.name);
                    tvClockGroup.setText(resultModel.groupName);
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


    public void getAttendRecords(String date){
        MineWebHelper.getAttendRecords(date,new IModelResultListener<AttendModel>() {
            @Override
            public boolean onGetResultModel(HttpResultModel resultModel) {
                pull_content.endRefresh(true);
                return false;
            }

            @Override
            public void onSuccess(String resultCode, AttendModel resultModel, List<AttendModel> resultModelList, String resultMsg, String hint) {
                pull_content.endRefresh(true);
                adapter.list.clear();
                if(resultCode.equals("0") && resultModel.clockInfo==null){
                    rlClockButton.setVisibility(View.GONE);
                    rlClockArea.setVisibility(View.GONE);
                    rlNoAttendData.setVisibility(View.VISIBLE);
                    tvNoData.setText(resultModel.note);
                }else if(resultCode.equals("0") && resultModel.clockInfo!=null){
                    clockInfo = resultModel.clockInfo;
                    rlClockButton.setVisibility(View.VISIBLE);
                    rlClockArea.setVisibility(View.VISIBLE);
                    rlNoAttendData.setVisibility(View.GONE);
                    if(clockInfo.status == 999){
                        tvClock.setBackground(getResources().getDrawable(R.drawable.oval_bg1_bg1));
                        tvClock.setText("上班打卡");
                    }else if(clockType == 0 && DateUtil.getCurrentTime()>DateUtil.StringToLongDate(clockInfo.planTime,"yyyy-MM-dd HH:mm:ss")){
                        tvClock.setBackground(getResources().getDrawable(R.drawable.oval_bg5_bg5));
                        tvClock.setText("迟到打卡");
                    }else if(clockType == 0){
                        tvClock.setBackground(getResources().getDrawable(R.drawable.oval_bg7_bg7));
                        tvClock.setText("上班打卡");
                    }else{
                        tvClock.setBackground(getResources().getDrawable(R.drawable.oval_bg7_bg7));
                        tvClock.setText("下班打卡");
                    }

                }

//                if(resultCode.equals("0") && resultModel.note==null){
//                    rlNoAttendData.setVisibility(View.GONE);
//                }else if(resultModel.clockInfo==null){
//                    rlNoAttendData.setVisibility(View.VISIBLE);
//                    tvNoData.setText(resultModel.note);
//                }

                if(resultModel.attendRecords.size()>0) {
                    adapter.list=resultModel.attendRecords;
                    rlNoAttendData.setVisibility(View.GONE);
                }
                if(resultModel.note!= null && (resultModel.note.contains("休息") || resultModel.note.contains("未给你排班"))){
                    adapter.notifyDataSetChanged();
                }else{
                    refreshClockList();
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

    public void refreshClockList(){

//        for(int i=0;i<5;i++) {
//            ClockModel clockModel = new ClockModel();
//            clockModel.id = i;
//            list.add(clockModel);
//        }
//        Utills.showShortToast("222"+adapter.list.size());
        if(adapter.list.size() == 0){
            ClockModel clockModel = new ClockModel();
            clockModel.lineShow = true;
            clockModel.clockShow = true;
            String startTimeMin="";
            if(!StringHelper.isEmpty(clockInfo.startTimeMin))startTimeMin = clockInfo.startTimeMin;
            clockModel.clockName = "上班打卡" + startTimeMin;
            adapter.list.add(clockModel);
//            tvClock.setText("上班打卡");
            clockType = 0;
        }else {
            if (adapter.list.size() % 2 == 0) {
                adapter.list.get(0).lineShow = true;
                adapter.list.get(1).viewClock = true;
                adapter.list.get(0).clockName = "上班打卡时间";
                adapter.list.get(1).clockName = "下班打卡时间";

//                ClockModel clockModel = new ClockModel();
//                clockModel.lineShow = false;
//                clockModel.clockShow = true;
//                clockModel.clockName = "上班打卡";
//                adapter.list.add(clockModel);
//                tvClock.setText("上班打卡");
//                clockType = 0;
            }else{
                adapter.list.get(0).lineShow = true;
                adapter.list.get(0).clockName = "上班打卡时间";
                ClockModel clockModel = new ClockModel();
                clockModel.lineShow = false;
                clockModel.clockShow = true;
                String endTimeMin="";
                if(!StringHelper.isEmpty(clockInfo.endTimeMin))endTimeMin = clockInfo.endTimeMin;
                clockModel.clockName = "下班打卡" + endTimeMin;
                adapter.list.add(clockModel);
//                tvClock.setText("下班打卡");
                clockType = 1;
            }
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onSelectDate(final int year, final int month, final int day, long time, int position) {
//        slSchedule.getMonthCalendar().setCurrentItem(position);
//        slSchedule.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                slSchedule.getMonthCalendar().getCurrentMonthView().clickThisMonth(year, month, day);
//            }
//        }, 100);
//        mTime = time;
         selectDate = year + "-" + (month+1) + "-" + day;
        tvClockTime.setText(selectDate.replace("-","."));
        getAttendRecords(selectDate);
    }

}
