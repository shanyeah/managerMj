package com.imovie.mogic.mine.attend;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.imovie.mogic.MyApplication;
import com.imovie.mogic.R;
import com.imovie.mogic.calendar.OnCalendarClickListener;
import com.imovie.mogic.calendar.month.MonthCalendarView;
import com.imovie.mogic.calendar.month.MonthView;
import com.imovie.mogic.mine.adapter.ClockAdapter;
import com.imovie.mogic.mine.attend.model.AttendDatesModel;
import com.imovie.mogic.mine.attend.model.MonthDayModel;
import com.imovie.mogic.mine.model.AttendModel;
import com.imovie.mogic.mine.model.ClockModel;
import com.imovie.mogic.mine.net.MineWebHelper;
import com.imovie.mogic.utills.DateUtil;
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

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AttendCalendarActivity extends AppCompatActivity implements OnCalendarClickListener {

    public static final int MSG_REFRESH = 600;
    public static final int MSG_WXSEND = 601;

    private TitleBar titleBar;
    public RoundedImageView profile;
    private DisplayImageOptions mOption;
    private PullToRefreshFrameLayout pull_content;
    private FlexibleFrameLayout ff_list;
    private LinearLayout ll_ad;
    private RelativeLayout rlNoData;
    private TextView tvNoData;

    private TextView tvClockTime;
    private TextView tvName;
    private TextView tvClockGroup;
    private MonthCalendarView mcvCalendar;
    private String[] mMonthText;
    private int mCurrentSelectYear, mCurrentSelectMonth, mCurrentSelectDay;
    private int mPosition = -1;
    public String date;

    public NoScrollListView lvClockList;
    public ArrayList<ClockModel> list;
    public ClockAdapter adapter;
//    final List<Integer> days = new ArrayList<>();
    final List<MonthDayModel> days = new ArrayList<>();

    public Calendar cl;
    public int day;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attend_calendar_activity);
        initView();
        setView();
        getMyCalendar(date);
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
        date = getIntent().getStringExtra("date");
        titleBar = (TitleBar) findViewById(R.id.title_bar);
        titleBar.setTitle("考勤月历");
        profile= (RoundedImageView)findViewById(R.id.profile);
        pull_content = (PullToRefreshFrameLayout) findViewById(R.id.pull_content);
        ff_list = (FlexibleFrameLayout) findViewById(R.id.ff_list);
        ll_ad = (LinearLayout) findViewById(R.id.ll_ad);
        tvClockTime = (TextView) findViewById(R.id.tvClockTime);
        lvClockList = (NoScrollListView) findViewById(R.id.lvClockList);
        tvName = (TextView) findViewById(R.id.tvName);
        tvNoData = (TextView) findViewById(R.id.tvNoData);
        tvClockGroup = (TextView) findViewById(R.id.tvClockGroup);
        rlNoData = (RelativeLayout) findViewById(R.id.rlNoData);

        titleBar.setLeftListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mcvCalendar = (MonthCalendarView) findViewById(R.id.mcvCalendar);
        mcvCalendar.setOnCalendarClickListener(this);
        tvClockTime.setText(date);

    }

    @SuppressLint("WrongConstant")
    private void setView(){
        setPullAndFlexListener();

        list = new ArrayList<>();
        adapter = new ClockAdapter(this, list);
        lvClockList.setAdapter(adapter);

        String[] s = date.split("-");
        initDate(Integer.parseInt(s[0]), Integer.parseInt(s[1])-1, Integer.parseInt(s[2]), mPosition);

        mOption = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.person_default_profile)
                .showImageOnFail(R.drawable.person_default_profile)
                .showImageForEmptyUri(R.drawable.person_default_profile)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();

        ImageLoader.getInstance().displayImage(MyApplication.getInstance().mPref.getString("fackeImageUrl",""),profile,mOption);

        Button btBackDay  = (Button) findViewById(R.id.btBackDay);
        Button btNextDay  = (Button) findViewById(R.id.btNextDay);
        cl = Calendar.getInstance();
//        day = cl.get(Calendar.DATE);
        btBackDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
                System.out.println("---当前日期："+sf.format(cl.getTime()));
                cl.add(Calendar.DAY_OF_MONTH, -1);
                System.out.println("---增加一天后日期 : "+sf.format(cl.getTime()));

            }
        });

        btNextDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
                System.out.println("---当前日期："+sf.format(cl.getTime()));
                cl.add(Calendar.DAY_OF_MONTH, 1);
                System.out.println("---增加一天后日期 : "+sf.format(cl.getTime()));
            }
        });

    }

    private void initDate(int year, int month, int day, int position) {
        setCurrentSelectDate(year, month, day);
        if (position != -1) {
//            mcvCalendar.setCurrentItem(position, false);
            mcvCalendar.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    MonthView monthView = mcvCalendar.getCurrentMonthView();
                    monthView.setSelectYearMonth(mCurrentSelectYear, mCurrentSelectMonth, mCurrentSelectDay);
//                    if(monthView.mSelMonth == 11) {
//
//                        List<Integer> days = new ArrayList<>();
//                        days.add(2);
//                        days.add(3);
//                        days.add(10);
//                        monthView.addTaskHints(days);
//                    }
                    monthView.invalidate();
                }
            });
        }

    }

    @Override
    public void onClickDate(int year, int month, int day) {
        setCurrentSelectDate(year, month, day);
    }

    @Override
    public void onPageChange(int year, int month, int day) {
//        Utills.showShortToast(""+month);
        getMyCalendar(year + "-" + (month+1) + "-" + day);
    }

    @SuppressLint("WrongConstant")
    private void setCurrentSelectDate(int year, int month, int day) {
        mCurrentSelectYear = year;
        mCurrentSelectMonth = month;
        mCurrentSelectDay = day;
//        Calendar calendar = Calendar.getInstance();
//        if (year == calendar.get(Calendar.YEAR)) {
////            tvDate.setText(mMonthText[month]);
//        } else {
////            tvDate.setText(String.format("%s%s", String.format(getContext().getString(R.string.calendar_year), year),
////                    mMonthText[month]));
//        }

        tvClockTime.setText(year+"-" + (month+1)+"-"+day);
        getAttendRecords(year+"-" + (month+1)+"-"+day);
    }


    private void setPullAndFlexListener(){
//        ff_list.setFlexView(ll_ad);
        ff_list.setFlexible(false);
        pull_content.setPullEnable(false);

        ff_list.setOnFlexChangeListener(new FlexibleFrameLayout.OnFlexChangeListener() {
            @Override
            public void onFlexChange(int flexHeight, int currentFlexHeight, boolean isOnTop) {
                if (isOnTop) {
                    pull_content.setPullEnable(false);
                } else {
                    pull_content.setPullEnable(false);
                }
            }

        });
        pull_content.setOnPullToRefreshListener(new PullToRefreshFrameLayout.OnPullToRefreshListener() {
            @Override
            public void onRefresh() {
//				llFilter.reset();
//                reFresh();

//                getHallDetail();
//                praiseFragment.getReviewList(stgId);
            }
        });


    }


    public void getMyCalendar(String date){
        MineWebHelper.getMyCalendar(date,new IModelResultListener<AttendDatesModel>() {
            @Override
            public boolean onGetResultModel(HttpResultModel resultModel) {
                return false;
            }

            @Override
            public void onSuccess(String resultCode, AttendDatesModel resultModel, List<AttendDatesModel> resultModelList, String resultMsg, String hint) {
                if(resultCode.equals("0")) {
                    tvName.setText(resultModel.name);
                    if(resultModel.attendDates.size()>0){
                        days.clear();
//                        final List<Integer> days = new ArrayList<>();
                        int year = 2017;
                        int month = 12;
                        for(int i=0;i<resultModel.attendDates.size();i++){
                            MonthDayModel monthDay = new MonthDayModel();
                            Date date = DateUtil.String2Date(resultModel.attendDates.get(i).scheduleDate,"yyyy-MM-dd HH:mm:ss");
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTime(date);
                            int day = calendar.get(Calendar.DAY_OF_MONTH);
                            year = calendar.get(Calendar.YEAR);
                            month = calendar.get(Calendar.MONTH);
                            monthDay.year = year;
                            monthDay.month = month;
                            monthDay.day = day;
                            monthDay.name = resultModel.attendDates.get(i).attendClassName;
                            days.add(monthDay);
                        }
                        final MonthView monthView = mcvCalendar.getCurrentMonthView();
                        monthView.setSelectYearMonth(mCurrentSelectYear, mCurrentSelectMonth, mCurrentSelectDay);
                        if(monthView.mSelYear==year && monthView.mSelMonth == month) {
                        mcvCalendar.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                            @Override
                            public void onGlobalLayout() {

                                monthView.addTaskHintsDay(days);
                                monthView.invalidate();
                            }
                        });
                    }

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


    public void getAttendRecords(String date){
        MineWebHelper.getAttendRecords(date,new IModelResultListener<AttendModel>() {
            @Override
            public boolean onGetResultModel(HttpResultModel resultModel) {
                return false;
            }

            @Override
            public void onSuccess(String resultCode, AttendModel resultModel, List<AttendModel> resultModelList, String resultMsg, String hint) {
                if(resultCode.equals("0")) {
                    tvClockGroup.setText(resultModel.title);
                    adapter.list.clear();
                    if (resultModel.attendRecords.size() > 0) {
                        adapter.list = resultModel.attendRecords;
                        rlNoData.setVisibility(View.GONE);
                    }else{
                        rlNoData.setVisibility(View.VISIBLE);
                        tvNoData.setText(resultModel.note);
                    }
                    refreshClockList();
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

    public void refreshClockList(){

//        for(int i=0;i<5;i++) {
//            ClockModel clockModel = new ClockModel();
//            clockModel.id = i;
//            list.add(clockModel);
//        }
//        Utills.showShortToast("222"+adapter.list.size());
        if(adapter.list.size() == 0){
//            ClockModel clockModel = new ClockModel();
//            clockModel.lineShow = true;
//            clockModel.clockShow = true;
//            clockModel.clockName = "上班打卡";
//            adapter.list.add(clockModel);

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
//                ClockModel clockModel = new ClockModel();
//                clockModel.lineShow = false;
//                clockModel.clockShow = true;
//                clockModel.clockName = "下班打卡";
//                adapter.list.add(clockModel);
            }
        }
        adapter.notifyDataSetChanged();
    }



    private final UIHandler uiHandler = new UIHandler(this);
    private static class UIHandler extends Handler {
        private final WeakReference<AttendCalendarActivity> activity;
        public UIHandler(AttendCalendarActivity act) {
            super();
            activity = new WeakReference<AttendCalendarActivity>(act);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_REFRESH:
//                    activity.get().queryWechatOrder(activity.get().wechatIDModel.orderNo);
                    break;
                case MSG_WXSEND:
//                    PayReq req = new PayReq();
//                    req.appId = activity.get().wechatIDModel.appid;
//                    req.partnerId = activity.get().wechatIDModel.partnerid;
//                    req.prepayId = activity.get().wechatIDModel.prepayId;
//                    req.nonceStr = activity.get().wechatIDModel.noncestr;
//                    req.timeStamp = activity.get().wechatIDModel.timestamp;
//                    req.packageValue = activity.get().wechatIDModel.packageStr;
//                    req.sign = activity.get().wechatIDModel.sign;
//                    req.extData = "魔杰电竞"+activity.get().cardModel.name+"消费";
//                    Toast.makeText(activity.get(), "正常调起支付", Toast.LENGTH_SHORT).show();
//                    // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
//                    activity.get().api.sendReq(req);
                    break;
                default:
                    break;
            }
        }
    };

}


