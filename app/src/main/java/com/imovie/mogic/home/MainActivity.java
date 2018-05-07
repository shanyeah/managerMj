package com.imovie.mogic.home;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.imovie.mogic.MyApplication;
import com.imovie.mogic.R;
import com.imovie.mogic.ScanPay.zxing.activity.CaptureActivity;

import com.imovie.mogic.card.model.InternetBarModel;
import com.imovie.mogic.config.AppConfig;
import com.imovie.mogic.gameHall.model.ReviewModel;
import com.imovie.mogic.home.checkUpdate.net.UpdateHelper;
import com.imovie.mogic.home.fragment.BuyGoodsFragment;
import com.imovie.mogic.home.fragment.ChargeFragment;
import com.imovie.mogic.home.fragment.HomeFragmentOld;
import com.imovie.mogic.home.fragment.MineFragment;
import com.imovie.mogic.home.fragment.PraiseFragment;
import com.imovie.mogic.home.fragment.ReportFragment;
import com.imovie.mogic.home.model.SearchUserModel;
import com.imovie.mogic.home.net.HomeWebHelper;
import com.imovie.mogic.login.LoginActivity;
import com.imovie.mogic.login.model.LoginModel;
import com.imovie.mogic.mine.SetingHallActivity;
import com.imovie.mogic.mine.fragment.ClockFragment;
import com.imovie.mogic.utills.ImageUtil;
import com.imovie.mogic.utills.StringHelper;
import com.imovie.mogic.utills.Utills;
import com.imovie.mogic.utills.baidu.GetLocation;
import com.imovie.mogic.web.IModelResultListener;
import com.imovie.mogic.web.model.HttpResultModel;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainActivity extends BaseActivity {

    public static final String SEND_BROADCAST = "send_broadcast";
    public static final String SEND_DATA = "data";
    public static final int SHOW_NAV_BAR = 81;
    public static final int HIDE_NAV_BAR= 82;
    public static final int PAY_SUCCESS = 83;

    public static final int REQUEST_SUCCESS = 601;
    public static final int PHOTO_REQUEST_CUT = 13;// cut picture

    public final int DELAY_CHANGE_GPS_FIRST = 1;     //第一次上传gps（秒）
    public final int UPDATE_CHANGE_GPS_FREQUENCY = 1800;   //更新上传gps频率（秒），也是打开关闭gps频率，优化省电
    public static final int REFRESH_DATA = 21;

    private FragmentManager fman;
    public HomeFragmentOld homeFragment;
    public ReportFragment reportFragment;
    public ChargeFragment chargeFragment;
    public BuyGoodsFragment buyGoodsFragment;
    public MineFragment mineFragment;
//    public PayMemberFragment payMemberFragment;
    protected PayBroadcast payReceiver;
    private RelativeLayout rlHome;
    private RelativeLayout rlDiscovery;
    private RelativeLayout rlHall;
    private RelativeLayout rlMessage;
    private RelativeLayout rlMine;
    private LinearLayout layout_bottom_nav;

    private ImageView ivHome;
    private TextView tvHome;
    private ImageView ivDiscovery;
    private TextView tvDiscovery;
    private ImageView ivHall;
    private TextView tvHall;

    private ImageView ivMessage;
    private TextView tvMessage;
    private TextView unreadLabel;

    private ImageView ivMine;
    private TextView tvMine;

    private GetLocation getLocation;

    private boolean isShow = false;
    private View inputLayout;
    private EditText commentEt;
    public Button btSend;

    public boolean isConflict = false;
    private boolean isCurrentAccountRemoved = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initGpsAndChangeStore();
        getLocation = new GetLocation(MainActivity.this);
        getLocation.start();

        UpdateHelper.autoCheckUpdate(this); //自动更新版本
        payReceiver = new PayBroadcast();
        IntentFilter filter = new IntentFilter();
        filter.addAction(SEND_BROADCAST);
        this.registerReceiver(payReceiver, filter);



        initView();
        setView();
        initListener();
    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.unregisterReceiver(payReceiver);

    }

    @Override
    protected void onStop() {

        super.onStop();
    }

    @Override
    public void onBackPressed() {
//        if(isShow){
//            layout_bottom_nav.setVisibility(View.VISIBLE);
//            return;
//        }
        super.onBackPressed();
    }

    private void initView() {

        rlHome = (RelativeLayout) findViewById(R.id.layout_home);
        rlDiscovery = (RelativeLayout) findViewById(R.id.layout_discovery);
        rlHall = (RelativeLayout) findViewById(R.id.rl_home_game_hall);
        rlMessage = (RelativeLayout) findViewById(R.id.layout_member);
        rlMine = (RelativeLayout) findViewById(R.id.layout_mine);
        layout_bottom_nav = (LinearLayout) findViewById(R.id.layout_bottom_nav);

        ivHome = (ImageView) findViewById(R.id.btn_home);
        tvHome = (TextView) findViewById(R.id.tv_home);
        ivDiscovery = (ImageView) findViewById(R.id.btn_discovery);
        tvDiscovery = (TextView) findViewById(R.id.tv_discovery);
        ivHall = (ImageView) findViewById(R.id.iv_home_game_hall);
        tvHall = (TextView) findViewById(R.id.tv_hall);

        ivMessage = (ImageView) findViewById(R.id.btn_member);
        tvMessage = (TextView) findViewById(R.id.tv_member);
        unreadLabel = (TextView) findViewById(R.id.unread_msg_number);

        ivMine = (ImageView) findViewById(R.id.btn_mine);
        tvMine = (TextView) findViewById(R.id.tv_mine);

        inputLayout = findViewById(R.id.input_layer);
        commentEt = (EditText) findViewById(R.id.comment_et);
        btSend = (Button)findViewById(R.id.btSend);

    }

    public void setView() {

        fman = getSupportFragmentManager();
        FragmentTransaction ft = fman.beginTransaction();
        homeFragment = new HomeFragmentOld();
//        List<LoginModel.OrganList> organList = (List<LoginModel.OrganList>) getIntent().getSerializableExtra("organList");
        List<LoginModel.OrganList> organList = new ArrayList<>();
        String  jsondata = MyApplication.getInstance().mPref.getString("organList", "null");
        Gson gson = new Gson();
        if (!jsondata.equals("null")) {
            organList = gson.fromJson(jsondata, new TypeToken<List<LoginModel.OrganList>>() {}.getType());
        }

        if(organList!=null && organList.size()>0) {
            for (int i = 0; i < organList.size(); i++) {
                InternetBarModel model = new InternetBarModel();
                LoginModel.OrganList organ = (LoginModel.OrganList)organList.get(i);
                model.id = organ.organId;
                model.name = organ.organName;
                homeFragment.listHall.add(model);
            }
        }
        ft.add(R.id.ll_fragment, homeFragment, "0");
        ft.show(homeFragment);

        reportFragment = new ReportFragment();
        ft.add(R.id.ll_fragment, reportFragment, "1");
        ft.hide(reportFragment);
//
//        chargeFragment = new ChargeFragment();
//        ft.add(R.id.ll_fragment, chargeFragment, "2");
//        ft.hide(chargeFragment);


//            buyGoodsFragment = new BuyGoodsFragment();
//            ft.add(R.id.ll_fragment, buyGoodsFragment, "3");
//            ft.hide(buyGoodsFragment);



        mineFragment = new MineFragment();
        ft.add(R.id.ll_fragment, mineFragment, "4");
        ft.hide(mineFragment);

        ft.commitAllowingStateLoss();

        switchBottomMenu(0);
    }

    private void initListener() {
        rlHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchBottomMenu(0);
            }
        });

        rlDiscovery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchBottomMenu(1);
            }
        });

        rlHall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchBottomMenu(2);
            }
        });

        rlMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    switchBottomMenu(3);

            }
        });

        rlMine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchBottomMenu(4);

            }
        });

        btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Utills.showShortToast("555");
                String content = commentEt.getText().toString();
                if(StringHelper.isEmpty(content)){
                    Utills.showShortToast("回复内容不能为空");
                    return;
                }
                getFriendComments(content,homeFragment.reviewModel.id);
            }
        });
    }


    private void switchBottomMenu(int indexPage) {
        ImageView[] views = {ivHome, ivDiscovery, ivHall, ivMessage,ivMine};
        TextView[] txtViews = {tvHome, tvDiscovery, tvHall, tvMessage,tvMine};
        Integer[] norValue = {R.drawable.home_main_image, R.drawable.home_buy_goods, R.drawable.home_recharge, R.drawable.home_appreciate, R.drawable.home_mine};
        Integer[] presValue = {R.drawable.home_main_image0, R.drawable.home_buy_goods0, R.drawable.home_recharge0, R.drawable.home_appreciate0, R.drawable.home_mine0};
//        this.currentTabIndex = indexPage;
        for (int i = 0; i < 5; i++) {
            if (i == indexPage) {
                views[i].setBackgroundResource(norValue[i]);
                txtViews[i].setTextColor(getResources().getColor((R.color.T6)));
            } else {
                views[i].setBackgroundResource(presValue[i]);
                txtViews[i].setTextColor(getResources().getColor((R.color.T1)));
            }
        }
        supportInvalidateOptionsMenu();
//        mTitleViewHolder.changeTitle(currentTabIndex);
        switchFragment(indexPage);
    }

    private void switchFragment(int position) {
        if (position >= 0 && position < 5) {
            FragmentTransaction ft = fman.beginTransaction();
            for (int i = 0; i < 5; i++) {
                Fragment fragment = fman.findFragmentByTag(String.valueOf(i));
                if (fragment != null) {
                    if (i == position) {
                        ft.show(fragment);
                    } else {
                        ft.hide(fragment);
                    }
                }
            }
            ft.commitAllowingStateLoss();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        Utills.showShortToast(""+requestCode);
        switch (requestCode) {
            case CaptureActivity.MSG_OTHER:
                String code = data.getStringExtra("code");
                if(StringHelper.isEmail(code))return;
                break;


            case SetingHallActivity.SELECT_RESULT:
                homeFragment.refresh();
//                buyGoodsFragment.refresh();
                break;

            default:
                break;

        }
    }


    protected void addFragment() {
//        FragmentTransaction ft = fman.beginTransaction();
//        ft.remove(buyGoodsFragment);
//        ft.commitAllowingStateLoss();
//        payMemberFragment = new PayMemberFragment();
//        FragmentTransaction f = fman.beginTransaction();
//        f.add(R.id.ll_fragment, payMemberFragment, "3");
//        f.hide(payMemberFragment);
//        f.commitAllowingStateLoss();
    }

    /**
     * 评论
     */
    public void getFriendComments(final  String content,int replyId){
        HomeWebHelper.getFriendComments(content,replyId, new IModelResultListener<ReviewModel.Replies>() {
            @Override
            public boolean onGetResultModel(HttpResultModel resultModel) {
                return false;
            }

            @Override
            public void onSuccess(String resultCode, ReviewModel.Replies resultModel, List<ReviewModel.Replies> resultModelList, String resultMsg, String hint) {
//                Log.e("----lan",resultCode);
                if(resultCode.equals("0")){
                    hideInputLayout();
                    Utills.showShortToast("回复成功");
                    homeFragment.adapter.setContext(content,homeFragment.reviewModel.index,resultModel);
                    homeFragment.adapter.notifyDataSetChanged();
                }else {
                    Utills.showShortToast("评论失败");
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


    private void initGpsAndChangeStore() {

		/* 获取当前的经纬度初始化 */
        ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                getLocation.start();
                uiHandler.sendEmptyMessageDelayed( REFRESH_DATA,3000);
            }
        }, DELAY_CHANGE_GPS_FIRST, UPDATE_CHANGE_GPS_FREQUENCY, TimeUnit.SECONDS);

        //8秒后开始记录gps信息
//        uiHandler.sendEmptyMessageDelayed(22, 8000);
    }

    private final MainHandler uiHandler = new MainHandler(this);
    private static class MainHandler extends Handler {
        private final WeakReference<MainActivity> activity;
        public MainHandler(MainActivity act) {
            super();
            activity = new WeakReference<MainActivity>(act);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case REQUEST_SUCCESS:
//                    int status = MyApplication.getInstance().mPref.getInt("status",0);
//                    activity.get().queryWechatOrder(activity.get().wechatIDModel.orderNo);
//                    activity.get().addFragment();
//                    activity.get().switchBottomMenu(3);

                    break;
                case REFRESH_DATA:
//                    activity.get().homeFragment.getHallList();
//                    activity.get().chargeFragment.getHallList(0,"");
                break;
                case SHOW_NAV_BAR:
//                    Utills.showShortToast("SHOW_NAV_BAR");
//                    if(!activity.get().homeFragment.isHidden()){
//                        activity.get().homeFragment.showNavBar();
//                    }else if(!activity.get().discoveryFragment.isHidden()){
//                        activity.get().discoveryFragment.showNavBar();
//                    }
                    break;
                case HIDE_NAV_BAR:
//                    if(!activity.get().homeFragment.isHidden()){
//                        activity.get().homeFragment.hideNavBar();
//                    }else if(!activity.get().discoveryFragment.isHidden()){
//                        activity.get().discoveryFragment.hideNavBar();
//                    }
                    break;

                default:
                    break;
            }
        }
    };

    public class PayBroadcast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                if (intent.getAction().equals(SEND_BROADCAST)) {
                    int data = intent.getIntExtra(SEND_DATA,0);
                    switch (data) {
                        case PAY_SUCCESS:
                            Message msg = new Message();
                            msg.what = REQUEST_SUCCESS;
                            uiHandler.sendMessage(msg);
                            break;
                        case SHOW_NAV_BAR:
                            Message msg1 = new Message();
                            msg1.what = SHOW_NAV_BAR;
                            uiHandler.sendMessage(msg1);
                            break;
                        case HIDE_NAV_BAR:
                            Message msg2 = new Message();
                            msg2.what = HIDE_NAV_BAR;
                            uiHandler.sendMessage(msg2);
                            break;
                    }
//                    Toast.makeText(MainActivity.this,PAY_SUCCESS,Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void hideInputLayout() {
        homeFragment.adapter.setShow(false);
        commentEt.setText("");
        inputLayout.setVisibility(View.GONE);
        closeSoftKeyboard(this);
    }

    private void closeSoftKeyboard(Context context) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null && ((Activity) context).getCurrentFocus() != null) {
            inputMethodManager.hideSoftInputFromWindow(((Activity) context).getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {
//                hideInputLayout();
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    public  boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = { 0, 0 };
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
    }


}
