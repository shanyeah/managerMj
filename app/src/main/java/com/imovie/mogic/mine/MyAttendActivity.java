package com.imovie.mogic.mine;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.imovie.mogic.MyApplication;
import com.imovie.mogic.R;
import com.imovie.mogic.home.BaseActivity;
import com.imovie.mogic.mine.adapter.AnimatedListAdapter;
import com.imovie.mogic.mine.attend.AttendMonthActivity;
import com.imovie.mogic.mine.attend.widget.CustomDatePicker;
import com.imovie.mogic.mine.model.GroupItem;
import com.imovie.mogic.mine.model.MyAttendModel;
import com.imovie.mogic.mine.net.MineWebHelper;
import com.imovie.mogic.utills.DateUtil;
import com.imovie.mogic.web.IModelResultListener;
import com.imovie.mogic.web.model.HttpResultModel;
import com.imovie.mogic.widget.AnimatedExpandableListView;
import com.imovie.mogic.widget.RoundedImageView;
import com.imovie.mogic.widget.TitleBar;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MyAttendActivity extends BaseActivity {

    private TitleBar titleBar;
    public RoundedImageView profile;
    private DisplayImageOptions mOption;
    private List<GroupItem> parentList = new ArrayList<>();
    public AnimatedExpandableListView mListView;
    public AnimatedListAdapter adapter;
    public TextView tvName;
    public TextView tvClockGroup;
    public TextView tvClockTime;
    public TextView tvAttendMonth;
    public String month;
    public String date;

    private CustomDatePicker customDatePicker;
    public String startTime ="2010-01-01 00:00";
    public String endTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_attend_activity);
        mOption = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.person_default_profile)
                .showImageOnFail(R.drawable.person_default_profile)
                .showImageForEmptyUri(R.drawable.person_default_profile)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();

        initView();
        setView();
        initListener();
        getMyAttend(month);
        initDatePicker();
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
        titleBar.setTitle("统计");
        titleBar.setLeftListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

//        profile= (RoundedImageView)findViewById(R.id.profile);
        mListView = (AnimatedExpandableListView) findViewById(R.id.expandableListView);
        View headerView = LayoutInflater.from(MyAttendActivity.this).inflate(R.layout.header_attend_list,null);
        tvName = (TextView) headerView.findViewById(R.id.tvName);
        tvClockGroup = (TextView) headerView.findViewById(R.id.tvClockGroup);
        tvClockTime = (TextView) headerView.findViewById(R.id.tvClockTime);
        tvAttendMonth = (TextView) headerView.findViewById(R.id.tvAttendMonth);
        profile= (RoundedImageView)headerView.findViewById(R.id.profile);

        mListView.addHeaderView(headerView);

    }
    @SuppressLint("WrongConstant")
    private void setView(){
        Calendar calendar = Calendar.getInstance();
        month = ""+calendar.get(Calendar.YEAR) + (calendar.get(Calendar.MONTH)+1);
        date = DateUtil.Date2String(calendar.getTime(),"yyyy-MM-dd");
        tvClockTime.setText(calendar.get(Calendar.YEAR) +"-"+ (calendar.get(Calendar.MONTH)+1));
        adapter = new AnimatedListAdapter(this,parentList);
        mListView.setAdapter(adapter);
        mListView.setGroupIndicator(null);


        //默认第一组打开
//        mListView.expandGroupWithAnimation(0);

        //点击分组打开或关闭时添加动画
        mListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                if (mListView.isGroupExpanded(groupPosition)){

                    mListView.collapseGroupWithAnimation(groupPosition);
                }else{
                    mListView.expandGroupWithAnimation(groupPosition);
                }
                int count = mListView.getExpandableListAdapter().getGroupCount();
                for(int j = 0; j < count; j++){
                    if(j != groupPosition){
                        mListView.collapseGroup(j);
                    }
                }
                return true;
            }
        });
        mListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
                date = adapter.list.get(i).list.get(i1).date;
                Intent intent = new Intent(MyAttendActivity.this,AttendMonthActivity.class);
                intent.putExtra("date",date);
                startActivity(intent);
                return false;
            }
        });
        ImageLoader.getInstance().displayImage(MyApplication.getInstance().mPref.getString("fackeImageUrl",""),profile,mOption);
    }

    private void initListener() {

        tvAttendMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyAttendActivity.this,AttendMonthActivity.class);
                intent.putExtra("date",date);
                startActivity(intent);
            }
        });

        tvClockTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDatePicker.show(endTime);
            }
        });

    }



    public void getMyAttend(String month){
        MineWebHelper.getMyAttend(month,new IModelResultListener<MyAttendModel>() {
            @Override
            public boolean onGetResultModel(HttpResultModel resultModel) {
                return false;
            }

            @Override
            public void onSuccess(String resultCode, MyAttendModel resultModel, List<MyAttendModel> resultModelList, String resultMsg, String hint) {

                if(resultCode.equals("0")) {
                    tvName.setText(resultModel.info.name);
                    tvClockGroup.setText(resultModel.info.groupName);
                    if(resultModel.list.size()>0) {
                        adapter.list = resultModel.list;
                        adapter.notifyDataSetChanged();
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

    private void initDatePicker() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        String now = sdf.format(new Date());
        endTime = now;
        customDatePicker = new CustomDatePicker(this, new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) { // 回调接口，获得选中的时间
//                currentTime.setText(time);
//                selectDate = time + ":00";
                String str = time.substring(0,7);
                tvClockTime.setText(str);
                month = str.replace("-","");
                getMyAttend(month);
            }
        }, startTime, now); // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
        customDatePicker.showSpecificTime(false); // 显示时和分
        customDatePicker.setIsLoop(true); // 允许循环滚动
    }


}

