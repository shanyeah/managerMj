package com.imovie.mogic.mine;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.imovie.mogic.MyApplication;
import com.imovie.mogic.R;
import com.imovie.mogic.config.AppConfig;
import com.imovie.mogic.home.BaseActivity;
import com.imovie.mogic.home.adater.SelectHallAdapter;
import com.imovie.mogic.home.model.GameHall;
import com.imovie.mogic.home.model.HallModel;
import com.imovie.mogic.home.net.HomeWebHelper;
import com.imovie.mogic.login.LoginActivity;
import com.imovie.mogic.login.RegisterActivity;
import com.imovie.mogic.utills.Utills;
import com.imovie.mogic.web.IModelResultListener;
import com.imovie.mogic.web.model.HttpResultModel;
import com.imovie.mogic.widget.FlexibleFrameLayout;
import com.imovie.mogic.widget.PageGridView;
import com.imovie.mogic.widget.PullToRefreshFrameLayout;
import com.imovie.mogic.widget.TitleBar;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class SetingHallActivity extends BaseActivity {
    private static final String TAG = "SetingHallActivity";
    public static final int MSG_REFRESH = 600;
    public static final int MSG_WXSEND = 601;
    public static final int SELECT_WEL = 11;
    public static final int SELECT_MAIN = 12;

    public static final int SELECT_RESULT = 50;

    private TitleBar titleBar;
    private PullToRefreshFrameLayout pull_content;
    private FlexibleFrameLayout ff_list;
    private PageGridView lvMyMovieList;
    public TextView tvVersion;
    public List<GameHall> listHall = new ArrayList<>();
    public SelectHallAdapter hallAdapter;
    private int selectFrom = 12;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_hall_activity);
        selectFrom = getIntent().getIntExtra("selectFrom",12);
        initView();
        setView();
        initListener();
        getHallList(0,"");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(selectFrom == SELECT_MAIN){
            Intent mIntent = new Intent();
            mIntent.putExtra("result", SELECT_RESULT);
            SetingHallActivity.this.setResult(SELECT_RESULT, mIntent);
        }
        finish();
    }

    private void initView(){

        titleBar = (TitleBar) findViewById(R.id.title_bar);
        titleBar.setTitle("设置我的店铺");

        pull_content = (PullToRefreshFrameLayout) findViewById(R.id.pull_content);
        ff_list = (FlexibleFrameLayout) findViewById(R.id.ff_list);
        lvMyMovieList = (PageGridView) findViewById(R.id.lvMyMovieList);
        tvVersion = (TextView) findViewById(R.id.tvVersion);

        titleBar.setLeftListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void setView(){
        setPullAndFlexListener();
        tvVersion.setText("版本号：" + AppConfig.getVersionName());
        hallAdapter = new SelectHallAdapter(SetingHallActivity.this,listHall);
        lvMyMovieList.setAdapter(hallAdapter);
//        lvMyMovieList.setPageSize(10);
        lvMyMovieList.setFocusable(true);
        hallAdapter.setSelectHallLisener(new SelectHallAdapter.SelectHallLisener() {
            @Override
            public void onSelect(int stgId) {
                Utills.showShortToast("设置成功");
                if(selectFrom == SELECT_WEL){
                    Intent intent = new Intent(SetingHallActivity.this, AutoScrollActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    Intent mIntent = new Intent();
                    mIntent.putExtra("result", SELECT_RESULT);
                    SetingHallActivity.this.setResult(SELECT_RESULT, mIntent);
                    finish();
                }
            }
        });

        lvMyMovieList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                int stgId = hallAdapter.getItem(position).id;
                hallAdapter.setSelectItem(stgId);
                SharedPreferences.Editor editor = MyApplication.getInstance().mPref.edit();
                editor.putInt("organId",stgId);
                editor.commit();
                Utills.showShortToast("设置成功");
                if(selectFrom == SELECT_WEL){
                    Intent intent = new Intent(SetingHallActivity.this, AutoScrollActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    Intent mIntent = new Intent();
                    mIntent.putExtra("result", SELECT_RESULT);
                    SetingHallActivity.this.setResult(SELECT_RESULT, mIntent);
                    finish();
                }
            }
        });

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

                getHallList(0,"");
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

    private void getHallList(int orderType, String cityId){
        HomeWebHelper.getHallList(orderType,cityId,1,20,new IModelResultListener<HallModel>() {
            @Override
            public boolean onGetResultModel(HttpResultModel resultModel) {
                pull_content.endRefresh(true);
                return false;
            }

            @Override
            public void onSuccess(String resultCode, HallModel resultModel, List<HallModel> resultModelList, String resultMsg, String hint) {

                try {
                    Log.e("----hall",""+resultCode);
                    pull_content.endRefresh(true);
                    if(resultModel.list.size()>0){
                    hallAdapter.list = resultModel.list;
                    hallAdapter.notifyDataSetChanged();
                    int organId = MyApplication.getInstance().mPref.getInt("organId",0);
                    hallAdapter.setSelectItem(organId);
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
        private final WeakReference<SetingHallActivity> activity;
        public UIHandler(SetingHallActivity act) {
            super();
            activity = new WeakReference<SetingHallActivity>(act);
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
                    lvMyMovieList.requestFocus();
                    lvMyMovieList.setFocusable(true);
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


