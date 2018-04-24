package com.imovie.mogic.mine;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import com.imovie.mogic.R;
import com.imovie.mogic.home.BaseActivity;
import com.imovie.mogic.home.checkUpdate.net.UpdateHelper;
import com.imovie.mogic.mine.adapter.AutoScrollAdapter;
import com.imovie.mogic.mine.model.AutoModel;
import com.imovie.mogic.mine.net.MineWebHelper;
import com.imovie.mogic.web.IModelResultListener;
import com.imovie.mogic.web.model.HttpResultModel;
import com.imovie.mogic.widget.AutoScrollListView;
import com.imovie.mogic.widget.FlexibleFrameLayout;
import com.imovie.mogic.widget.PullToRefreshFrameLayout;
import com.imovie.mogic.widget.TitleBar;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class AutoScrollActivity extends BaseActivity {
    private static final String TAG = "AutoScrollActivity";
    public static final int MSG_REFRESH = 600;
    public static final int MSG_WXSEND = 601;

    public ScheduledThreadPoolExecutor scheduledThreadPoolExecutor;
    public AtomicLong mCounter;
    private TitleBar titleBar;
    private PullToRefreshFrameLayout pull_content;
    private FlexibleFrameLayout ff_list;
    private AutoScrollListView lvMyMovieList;
    public List<AutoModel> list = new ArrayList<>();
    public AutoScrollAdapter adapter;
    public int i=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auto_scroll_activity);
        initView();
        setView();
        UpdateHelper.autoCheckUpdate(this); //自动更新版本
        initListener();
        getMyMovie();
        setAtomicLongTime();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (scheduledThreadPoolExecutor != null) scheduledThreadPoolExecutor.shutdown();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void initView(){

        titleBar = (TitleBar) findViewById(R.id.title_bar);
        titleBar.setTitle("魔杰订餐");
        titleBar.enableRightTextView("设置", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AutoScrollActivity.this, SetingHallActivity.class);
                intent.putExtra("selectFrom",SetingHallActivity.SELECT_MAIN);
                startActivity(intent);
            }
        });
        pull_content = (PullToRefreshFrameLayout) findViewById(R.id.pull_content);
        ff_list = (FlexibleFrameLayout) findViewById(R.id.ff_list);
        lvMyMovieList = (AutoScrollListView) findViewById(R.id.lvMyMovieList);



        titleBar.setLeftListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void setView(){
        setPullAndFlexListener();
        initData();
        adapter = new AutoScrollAdapter(AutoScrollActivity.this,list);
        lvMyMovieList.setAdapter(adapter);

//        viewHolder.tvPayTotal.setText(DecimalUtil.FormatMoney(cardModel.activeAmount) + getResources().getString(R.string.symbol_RMB));
//        viewHolder.tvPayPhone.setText(MyApplication.getInstance().mPref.getString("phone",""));
//        DisplayImageOptions mOption = new DisplayImageOptions.Builder()
//                .showImageOnLoading(R.drawable.home_card_image)
//                .showImageOnFail(R.drawable.home_card_image)
//                .showImageForEmptyUri(R.drawable.home_card_image)
//                .cacheInMemory(true)
//                .cacheOnDisk(true)
//                .build();
//        try {
//            ImageLoader.getInstance().displayImage(cardModel.imageUrl,ivHallTop,mOption);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

    }



    private void setPullAndFlexListener(){
//        ff_list.setFlexView(ll_ad);
//        ff_list.setFlexible(true);
        pull_content.setPullEnable(false);
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
//				llFilter.reset();
//                reFresh();

//                getChargeInfo();
            }
        });

    }

    private void initListener() {
//        ivHallTop.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                getWechatId("魔杰电竞"+cardModel.name+"购买","魔杰电竞"+cardModel.name+"购买",DecimalUtil.FormatMoney(cardModel.activeAmount));
//            }
//        });

    }

    private void getMyMovie(){
        MineWebHelper.getOrderList(new IModelResultListener<AutoModel>() {
            @Override
            public boolean onGetResultModel(HttpResultModel resultModel) {
                return false;
            }

            @Override
            public void onSuccess(String resultCode, AutoModel resultModel, List<AutoModel> resultModelList, String resultMsg, String hint) {

                try {
//                    Log.e("----hall",""+resultCode);
                    pull_content.endRefresh(true);
                    if(resultModelList.size()>0){
                        if(adapter.list.size()>0 && adapter.list.get(0).id != resultModelList.get(0).id) {

                            lvMyMovieList.stopAutoScroll();
                            adapter.list.clear();
                            adapter.notifyDataSetChanged();
                            adapter.list = resultModelList;
                            adapter.setOrderId();
                            adapter.notifyDataSetChanged();
                            lvMyMovieList.startAutoScroll();
                        }else{
//                            lvMyMovieList.stopAutoScroll();
//                            adapter.list.clear();
//                            adapter.notifyDataSetChanged();
                            adapter.list = resultModelList;
//                            if(i>=2) adapter.list.get(10).seatName = "W000";
                            adapter.setOrderId();
                            adapter.notifyDataSetChanged();
                            lvMyMovieList.startAutoScroll();
//                            i++;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
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



    private final UIHandler uiHandler = new UIHandler(this);
    private static class UIHandler extends Handler {
        private final WeakReference<AutoScrollActivity> activity;
        public UIHandler(AutoScrollActivity act) {
            super();
            activity = new WeakReference<AutoScrollActivity>(act);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_REFRESH:
                    activity.get().getMyMovie();
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

    private void initData() {
//        AutoModel beanOne = new AutoModel();
//        beanOne.seatName = "H1";
//        beanOne.detail = "面包一个";
//        beanOne.imageUrl = "http://icon.xinliji.me//avatar_0_63.png";
//        list.add(beanOne);
//
//        AutoModel beanTwo = new AutoModel();
//        beanTwo.seatName = "H2";
//        beanTwo.detail = "面包二个";
//        beanTwo.imageUrl = "http://icon.xinliji.me//avatar_0_63.png";
//        list.add(beanTwo);
//
//        AutoModel beanThree = new AutoModel();
//        beanThree.seatName = "H3";
//        beanThree.detail = "面包三个";
//        beanThree.imageUrl = "http://icon.xinliji.me//avatar_0_63.png";
//        list.add(beanThree);
    }

    private void setAtomicLongTime(){
                scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(1);
                mCounter = new AtomicLong(1);
                mCounter.set(0);
                scheduledThreadPoolExecutor.scheduleAtFixedRate(new Runnable() {//每隔一段时间就执行定时任务,上报状态
                    @Override
                    public void run() {
                        if (mCounter.getAndIncrement() % 20 == 0) {//每20秒查下
                            Message msg = new Message();
                            msg.what = MSG_REFRESH;
                            uiHandler.sendMessage(msg);
                        }
                    }
                }, 1, 1, TimeUnit.SECONDS);

    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        Log.d(TAG, "---key :" + event.getKeyCode());
//        if (notificationMessage.getDisablePlayer() == 1) {
//            return true;
//        } else
        if (event.getAction() == KeyEvent.ACTION_DOWN && this.hasWindowFocus()) {
            if (event.getKeyCode() == KeyEvent.KEYCODE_PAGE_UP) {
//                if (!movieListFragment.gvMovieList.hasFocus())
//                    movieListFragment.gvMovieList.setFocusable(true);
                KeyEvent e = new KeyEvent(0, 19);
                dispatchKeyEvent(e);
                return true;
            } else if (event.getKeyCode() == KeyEvent.KEYCODE_PAGE_DOWN) {
//                if (!movieListFragment.gvMovieList.hasFocus())
//                    movieListFragment.gvMovieList.setFocusable(true);
                KeyEvent e = new KeyEvent(0, 20);
                dispatchKeyEvent(e);
                return true;
            } else if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_DOWN) {
//                if (movieListFragment.gvMovieList.hasFocus()) {
//                    int i = movieListFragment.gvMovieList.getSelectedItemPosition();
//                    movieListFragment.pageDown(i);
//                }
            } else if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_UP) {
//                if (movieListFragment.gvMovieList.hasFocus()) {
//                    int i = movieListFragment.gvMovieList.getSelectedItemPosition();
//                    movieListFragment.pageUp(i);
//                }
            }
        }
        return super.dispatchKeyEvent(event);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.d(TAG, "---keyDown :" + keyCode);
//        if (notificationMessage.getDisablePlayer() == 1) {
//            return true;
//        } else
        if ((keyCode >= KeyEvent.KEYCODE_0) && (keyCode <= KeyEvent.KEYCODE_9)) {
//            enterSettings(keyCode, 1);
        }

        switch (keyCode) {
            case KeyEvent.KEYCODE_MENU:
                Intent intent = new Intent(AutoScrollActivity.this, SetingHallActivity.class);
                intent.putExtra("selectFrom",SetingHallActivity.SELECT_MAIN);
                startActivity(intent);
//                    if ((MyApplication.getInstance().connect.getUiMode() == 0) && MyApplication.getInstance().connect.getOrganId() == 600001) {
//                        ad.setVisibility(View.GONE);
//                    }
//                    Intent intent = new Intent(this, MenuKeyActivity.class);
//                    startActivity(intent);
                return true;
            //if (!MyApplication.getInstance().isServerMode()) return  true;
//                break;
            case KeyEvent.KEYCODE_BACK:
                finish();
//                    HtMainActivity.this.hideAutoSearch();
//                    movieListFragment.gvMovieList.setSelected(true);
//                    movieListFragment.gvMovieList.requestFocus();
//                    movieListFragment.gvMovieList.setFocusable(true);

//                    if ((MyApplication.getInstance().connect.getUiMode() == 0) && MyApplication.getInstance().connect.getOrganId() == 600001) {
//                        ad.setVisibility(View.GONE);
//                    }
                return true;

        }

        return super.onKeyDown(keyCode, event);
    }

}


