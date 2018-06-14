package com.imovie.mogic.mine.attend;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.imovie.mogic.MyApplication;
import com.imovie.mogic.R;
import com.imovie.mogic.calendar.OnCalendarClickListener;
import com.imovie.mogic.calendar.month.MonthCalendarView;
import com.imovie.mogic.calendar.month.MonthView;
import com.imovie.mogic.calendar.week.DateTimeInterpreter;
import com.imovie.mogic.calendar.week.WeekDayView;
import com.imovie.mogic.calendar.week.WeekHeaderView;
import com.imovie.mogic.calendar.week.WeekViewEvent;
import com.imovie.mogic.card.model.InternetBarModel;
import com.imovie.mogic.mine.adapter.ClockAdapter;
import com.imovie.mogic.mine.attend.model.AttendDatesModel;
import com.imovie.mogic.mine.attend.model.MonthDayModel;
import com.imovie.mogic.mine.model.AttendModel;
import com.imovie.mogic.mine.model.ClockModel;
import com.imovie.mogic.mine.net.MineWebHelper;
import com.imovie.mogic.myRank.widget.SpinerPopWindow;
import com.imovie.mogic.utills.ACache;
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
import java.util.Locale;

public class AttendDayViewActivity extends AppCompatActivity implements WeekDayView.MonthChangeListener,
        WeekDayView.EventClickListener, WeekDayView.EventLongPressListener,WeekDayView.EmptyViewClickListener,WeekDayView.EmptyViewLongPressListener,WeekDayView.ScrollListener{

    public static final int MSG_REFRESH = 600;
    public static final int MSG_WXSEND = 601;

    private TitleBar titleBar;
    public RoundedImageView profile;
    private DisplayImageOptions mOption;

    private LinearLayout ll_ad;

    private TextView tvClockTime;
    private TextView tvName;
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
    public WeekHeaderView mcvWeekCalendar;
    private WeekDayView mWeekView;
    public List<WeekViewEvent> mNewEvent = new ArrayList<WeekViewEvent>();

    private SpinerPopWindow<InternetBarModel> mSpinerPopWindow;
    public List<InternetBarModel> listHall = new ArrayList<>();
    public int idBar = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attend_day_view_activity);
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
        titleBar.setTitle("考勤日历");
        ll_ad = (LinearLayout) findViewById(R.id.ll_ad);
        tvClockTime = (TextView) findViewById(R.id.tvClockTime);

        titleBar.setLeftListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Calendar calendar = Calendar.getInstance();
        tvClockTime.setText(calendar.get(Calendar.YEAR)+"年"+(calendar.get(Calendar.MONTH)+1)+"月"+ calendar.get(Calendar.DAY_OF_MONTH)+"日");


        InternetBarModel barModel = new InternetBarModel();
        barModel.id = 2;
        barModel.name = "月视图";
        listHall.add(barModel);
        InternetBarModel barModel2 = new InternetBarModel();
        barModel2.id = 1;
        barModel2.name = "";
        listHall.add(barModel2);

        mSpinerPopWindow = new SpinerPopWindow<InternetBarModel>(AttendDayViewActivity.this, listHall,itemClickListener);

        mWeekView = (WeekDayView) findViewById(R.id.weekdayview);
        mcvWeekCalendar = (WeekHeaderView) findViewById(R.id.mcvWeekCalendar);

    }

    @SuppressLint("WrongConstant")
    private void setView(){
        list = new ArrayList<>();
//        adapter = new ClockAdapter(this, list);
//        lvClockList.setAdapter(adapter);

//        String[] s = date.split("-");
//        initDate(Integer.parseInt(s[0]), Integer.parseInt(s[1])-1, Integer.parseInt(s[2]), mPosition);
        tvClockTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSpinerPopWindow.setWidth(titleBar.getWidth());
                mSpinerPopWindow.showAsDropDown(tvClockTime);
                setTextImage(R.drawable.icon_up);
            }
        });

        mSpinerPopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setTextImage(R.drawable.icon_down);
            }
        });



        mWeekView.setMonthChangeListener(this);
        mWeekView.setEventLongPressListener(this);
        mWeekView.setOnEventClickListener(this);
        mWeekView.setEmptyViewClickListener(this);
        mWeekView.setScrollListener(this);

        mcvWeekCalendar.setDateSelectedChangeListener(new WeekHeaderView.DateSelectedChangeListener() {
            @Override
            public void onDateSelectedChange(Calendar oldSelectedDay, Calendar newSelectedDay) {
                mWeekView.goToDate(newSelectedDay);
                tvClockTime.setText(newSelectedDay.get(Calendar.YEAR)+"年"+(newSelectedDay.get(Calendar.MONTH)+1)+"月"+ newSelectedDay.get(Calendar.DAY_OF_MONTH)+"日");
            }
        });
        mcvWeekCalendar.setScrollListener(new WeekHeaderView.ScrollListener() {
            @Override
            public void onFirstVisibleDayChanged(Calendar newFirstVisibleDay, Calendar oldFirstVisibleDay) {
//                mWeekView.goToDate(mcvWeekCalendar.getSelectedDay());
                Calendar newSelectedDay =  mcvWeekCalendar.getSelectedDay();
                mWeekView.goToDate(newSelectedDay);
                tvClockTime.setText(newSelectedDay.get(Calendar.YEAR)+"年"+(newSelectedDay.get(Calendar.MONTH)+1)+"月"+ newSelectedDay.get(Calendar.DAY_OF_MONTH)+"日");
            }
        });
        setupDateTimeInterpreter(false);


//        Button btBackDay  = (Button) findViewById(R.id.btBackDay);
//        Button btNextDay  = (Button) findViewById(R.id.btNextDay);
//        cl = Calendar.getInstance();
//        day = cl.get(Calendar.DATE);
//        btBackDay.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
//                System.out.println("---当前日期："+sf.format(cl.getTime()));
//                cl.add(Calendar.DAY_OF_MONTH, -1);
//                System.out.println("---增加一天后日期 : "+sf.format(cl.getTime()));
////                mcvWeekCalendar.setCurrentWeekView(2018,6,10);
//                mcvWeekCalendar.setSelectedDay(cl);
//
//            }
//        });
//
//        btNextDay.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
//                System.out.println("---当前日期："+sf.format(cl.getTime()));
//                cl.add(Calendar.DAY_OF_MONTH, 1);
//                System.out.println("---增加一天后日期 : "+sf.format(cl.getTime()));
////                mcvWeekCalendar.setCurrentWeekView(2018,6,4);
//                mcvWeekCalendar.setSelectedDay(cl);
//            }
//        });

    }

    private void initDate(int year, int month, int day, int position) {
//        setCurrentSelectDate(year, month, day);
//        if (position != -1) {
//            mcvCalendar.setCurrentItem(position, false);
//            mcvCalendar.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//                @Override
//                public void onGlobalLayout() {
//                    MonthView monthView = mcvCalendar.getCurrentMonthView();
//                    monthView.setSelectYearMonth(mCurrentSelectYear, mCurrentSelectMonth, mCurrentSelectDay);
////                    if(monthView.mSelMonth == 11) {
////
////                        List<Integer> days = new ArrayList<>();
////                        days.add(2);
////                        days.add(3);
////                        days.add(10);
////                        monthView.addTaskHints(days);
////                    }
//                    monthView.invalidate();
//                }
//            });
//        }

    }



    private void setupDateTimeInterpreter(final boolean shortDate) {
        final String[] weekLabels={"日","一","二","三","四","五","六"};
        mWeekView.setDateTimeInterpreter(new DateTimeInterpreter() {
            @Override
            public String interpretDate(Calendar date) {
                SimpleDateFormat weekdayNameFormat = new SimpleDateFormat("EEE", Locale.getDefault());
                String weekday = weekdayNameFormat.format(date.getTime());
                SimpleDateFormat format = new SimpleDateFormat("d", Locale.getDefault());
                return format.format(date.getTime());
            }

            @Override
            public String interpretTime(int hour) {
                return String.format("%02d:00", hour);

            }

            @Override
            public String interpretWeek(int date) {
                if(date>7||date<1){
                    return null;
                }
                return weekLabels[date-1];
            }
        });
    }

    @Override
    public List<WeekViewEvent> onMonthChange(int newYear, int newMonth) {

        // Populate the week view with some events.
        List<WeekViewEvent> events = new ArrayList<WeekViewEvent>();

        Calendar startTime = Calendar.getInstance();
        startTime.set(Calendar.HOUR_OF_DAY, 3);
        startTime.set(Calendar.MINUTE, 0);
        startTime.set(Calendar.MONTH, newMonth - 1);
        startTime.set(Calendar.YEAR, newYear);
        Calendar endTime = (Calendar) startTime.clone();
        endTime.add(Calendar.HOUR, 1);
        endTime.set(Calendar.MONTH, newMonth - 1);
        WeekViewEvent event = new WeekViewEvent(1, "This is a Event!!", startTime, endTime);
        event.setColor(getResources().getColor(R.color.T5));
        events.add(event);

        startTime = Calendar.getInstance();
        startTime.set(Calendar.HOUR_OF_DAY, 3);
        startTime.set(Calendar.MINUTE, 30);
        startTime.set(Calendar.MONTH, newMonth - 1);
        startTime.set(Calendar.YEAR, newYear);
        endTime = (Calendar) startTime.clone();
        endTime.set(Calendar.HOUR_OF_DAY, 4);
        endTime.set(Calendar.MINUTE, 30);
        endTime.set(Calendar.MONTH, newMonth - 1);
        event = new WeekViewEvent(10, getEventTitle(startTime), startTime, endTime);
        event.setColor(getResources().getColor(R.color.T5));
        events.add(event);

        startTime = Calendar.getInstance();
        startTime.set(Calendar.HOUR_OF_DAY, 4);
        startTime.set(Calendar.MINUTE, 20);
        startTime.set(Calendar.MONTH, newMonth - 1);
        startTime.set(Calendar.YEAR, newYear);
        endTime = (Calendar) startTime.clone();
        endTime.set(Calendar.HOUR_OF_DAY, 5);
        endTime.set(Calendar.MINUTE, 0);
        event = new WeekViewEvent(10, getEventTitle(startTime), startTime, endTime);
        event.setColor(getResources().getColor(R.color.event_color_03));
        events.add(event);

        startTime = Calendar.getInstance();
        startTime.set(Calendar.HOUR_OF_DAY, 5);
        startTime.set(Calendar.MINUTE, 30);
        startTime.set(Calendar.MONTH, newMonth - 1);
        startTime.set(Calendar.YEAR, newYear);
        endTime = (Calendar) startTime.clone();
        endTime.add(Calendar.HOUR_OF_DAY, 2);
        endTime.set(Calendar.MONTH, newMonth - 1);
        event = new WeekViewEvent(2, getEventTitle(startTime), startTime, endTime);
        event.setColor(getResources().getColor(R.color.event_color_02));
        events.add(event);
        startTime = Calendar.getInstance();
        startTime.set(Calendar.HOUR_OF_DAY, 5);
        startTime.set(Calendar.MINUTE, 30);
        startTime.set(Calendar.MONTH, newMonth - 1);
        startTime.set(Calendar.YEAR, newYear);
        endTime = (Calendar) startTime.clone();
        endTime.add(Calendar.HOUR_OF_DAY, 2);
        endTime.set(Calendar.MONTH, newMonth - 1);
        event = new WeekViewEvent(2, "dddd", startTime, endTime);
        event.setColor(getResources().getColor(R.color.event_color_01));
        events.add(event);
        startTime = Calendar.getInstance();
        startTime.set(Calendar.HOUR_OF_DAY, 8);
        startTime.set(Calendar.MINUTE, 30);
        startTime.set(Calendar.MONTH, newMonth - 1);
        startTime.set(Calendar.YEAR, newYear);
        endTime = (Calendar) startTime.clone();
        endTime.add(Calendar.HOUR_OF_DAY, 2);
        endTime.set(Calendar.MONTH, newMonth - 1);
        event = new WeekViewEvent(6, "一起去海边玩我人有的和主民有淡膛埒肚腩二硫键分解符", startTime, endTime);
        event.setColor(getResources().getColor(R.color.event_color_01));
        events.add(event);
        startTime = Calendar.getInstance();
        startTime.set(Calendar.HOUR_OF_DAY, 5);
        startTime.set(Calendar.MINUTE, 0);
        startTime.set(Calendar.MONTH, newMonth - 1);
        startTime.set(Calendar.YEAR, newYear);
        startTime.add(Calendar.DATE, 1);
        endTime = (Calendar) startTime.clone();
        endTime.add(Calendar.HOUR_OF_DAY, 3);
        endTime.set(Calendar.MONTH, newMonth - 1);
        event = new WeekViewEvent(3, getEventTitle(startTime), startTime, endTime);
        event.setColor(getResources().getColor(R.color.event_color_03));
        events.add(event);

        startTime = Calendar.getInstance();
        startTime.set(Calendar.DAY_OF_MONTH, 15);
        startTime.set(Calendar.HOUR_OF_DAY, 3);
        startTime.set(Calendar.MINUTE, 0);
        startTime.set(Calendar.MONTH, newMonth - 1);
        startTime.set(Calendar.YEAR, newYear);
        endTime = (Calendar) startTime.clone();
        endTime.add(Calendar.HOUR_OF_DAY, 3);
        event = new WeekViewEvent(4, getEventTitle(startTime), startTime, endTime);
        event.setColor(getResources().getColor(R.color.event_color_04));
        events.add(event);

        startTime = Calendar.getInstance();
        startTime.set(Calendar.DAY_OF_MONTH, 1);
        startTime.set(Calendar.HOUR_OF_DAY, 3);
        startTime.set(Calendar.MINUTE, 0);
        startTime.set(Calendar.MONTH, newMonth - 1);
        startTime.set(Calendar.YEAR, newYear);
        endTime = (Calendar) startTime.clone();
        endTime.add(Calendar.HOUR_OF_DAY, 3);
        event = new WeekViewEvent(5, getEventTitle(startTime), startTime, endTime);
        event.setColor(getResources().getColor(R.color.event_color_01));
        events.add(event);

        startTime = Calendar.getInstance();
        startTime.set(Calendar.DAY_OF_MONTH, startTime.getActualMaximum(Calendar.DAY_OF_MONTH));
        startTime.set(Calendar.HOUR_OF_DAY, 15);
        startTime.set(Calendar.MINUTE, 0);
        startTime.set(Calendar.MONTH, newMonth - 1);
        startTime.set(Calendar.YEAR, newYear);
        endTime = (Calendar) startTime.clone();
        endTime.add(Calendar.HOUR_OF_DAY, 3);
        event = new WeekViewEvent(5, getEventTitle(startTime), startTime, endTime);
        event.setColor(getResources().getColor(R.color.event_color_02));
        events.add(event);
        events.addAll(mNewEvent);
        return events;
    }

    private String getEventTitle(Calendar time) {
        return String.format("Event of %02d:%02d %s/%d", time.get(Calendar.HOUR_OF_DAY), time.get(Calendar.MINUTE), time.get(Calendar.MONTH) + 1, time.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public void onEventClick(WeekViewEvent event, RectF eventRect) {
//        Toast.makeText(MainActivity.this, "Clicked " + event.getName(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onEventLongPress(WeekViewEvent event, RectF eventRect) {
//        Toast.makeText(MainActivity.this, "Long pressed event: " + event.getName(), Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onEmptyViewClicked(Calendar time) {
//         SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Intent intent = new Intent(AttendDayViewActivity.this,AttendDayRecordActivity.class);
//        intent.putExtra("time",sf.format(cl.getTime()));
        intent.putExtra("time",time.getTimeInMillis());
        startActivity(intent);
//        Toast.makeText(MainActivity.this, "Empty View clicked " + time.get(Calendar.YEAR) + "/" + time.get(Calendar.MONTH) + "/" + time.get(Calendar.DAY_OF_MONTH), Toast.LENGTH_LONG).show();


    }

    @Override
    public void onEmptyViewLongPress(Calendar time) {
//        Toast.makeText(MainActivity.this, "Empty View long  clicked " + time.get(Calendar.YEAR) + "/" + time.get(Calendar.MONTH) + "/" + time.get(Calendar.DAY_OF_MONTH), Toast.LENGTH_LONG).show();

    }

    @Override
    public void onFirstVisibleDayChanged(Calendar newFirstVisibleDay, Calendar oldFirstVisibleDay) {

    }

    @Override
    public void onSelectedDaeChange(Calendar selectedDate) {
        mcvWeekCalendar.setSelectedDay(selectedDate);
        tvClockTime.setText(selectedDate.get(Calendar.YEAR)+"年"+(selectedDate.get(Calendar.MONTH)+1)+"月"+ selectedDate.get(Calendar.DAY_OF_MONTH)+"日");

    }


    /**
     * 给TextView右边设置图片
     * @param resId
     */
    private void setTextImage(int resId) {
        Drawable drawable = getResources().getDrawable(resId);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(),drawable.getMinimumHeight());// 必须设置图片大小，否则不显示
        tvClockTime.setCompoundDrawables(null, null, drawable, null);
    }

    /**
     * popupwindow显示的ListView的item点击事件
     */
    private AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
            InternetBarModel internetBarModel = (InternetBarModel)mSpinerPopWindow.list.get(position);
            if(idBar==1 && internetBarModel.id==2) {
//                tvClockTime.setText(internetBarModel.name);
//                organId = internetBarModel.id;
                Intent intent = new Intent(AttendDayViewActivity.this,AttendCalendarActivity.class);
                intent.putExtra("date","2018-06-06");
                startActivity(intent);
            }
            mSpinerPopWindow.dismiss();
        }
    };

//    @SuppressLint("WrongConstant")
//    private void setCurrentSelectDate(int year, int month, int day) {
//        mCurrentSelectYear = year;
//        mCurrentSelectMonth = month;
//        mCurrentSelectDay = day;
////        Calendar calendar = Calendar.getInstance();
////        if (year == calendar.get(Calendar.YEAR)) {
//////            tvDate.setText(mMonthText[month]);
////        } else {
//////            tvDate.setText(String.format("%s%s", String.format(getContext().getString(R.string.calendar_year), year),
//////                    mMonthText[month]));
////        }
//
//        tvClockTime.setText(year+"-" + (month+1)+"-"+day);
//        getAttendRecords(year+"-" + (month+1)+"-"+day);
//    }





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
//                    tvClockGroup.setText(resultModel.title);
//                    adapter.list.clear();
//                    if (resultModel.attendRecords.size() > 0) {
//                        adapter.list = resultModel.attendRecords;
//                        rlNoData.setVisibility(View.GONE);
//                    }else{
//                        rlNoData.setVisibility(View.VISIBLE);
//                        tvNoData.setText(resultModel.note);
//                    }
//                    refreshClockList();
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
        private final WeakReference<AttendDayViewActivity> activity;
        public UIHandler(AttendDayViewActivity act) {
            super();
            activity = new WeakReference<AttendDayViewActivity>(act);
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


