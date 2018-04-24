package com.imovie.mogic.home;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.imovie.mogic.R;
import com.imovie.mogic.home.fragment.BuyGoodsFragment;
import com.imovie.mogic.home.fragment.ChargeFragment;
import com.imovie.mogic.home.fragment.PraiseFragment;
import com.imovie.mogic.home.fragment.WebviewFragment;
import com.imovie.mogic.home.model.ClassifyModel;
import com.imovie.mogic.login.LoginActivity;
import com.imovie.mogic.widget.TitleBar;

import java.lang.ref.WeakReference;

public class SelectTypeActivity extends BaseActivity{
    public static final int MSG_REFRESH = 600;
    public static final int MSG_WXSEND = 601;
    private TitleBar titleBar;
    private FragmentManager fman;
    public PraiseFragment praiseFragment;
    public ChargeFragment chargeFragment;
    public BuyGoodsFragment buyGoodsFragment;
    public WebviewFragment webviewFragment;
    public ClassifyModel classifyModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_select_type_activity);
        initView();
        setView();
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
        classifyModel = (ClassifyModel) getIntent().getSerializableExtra("classifyModel");
        titleBar = (TitleBar) findViewById(R.id.title_bar);
        titleBar.setTitle(classifyModel.name);
        titleBar.setLeftListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
//        rlLoginPassword = (RelativeLayout) findViewById(R.id.rlLoginPassword);
//        btnExitLogin = (Button) findViewById(R.id.btnExitLogin);

    }

    private void setView(){
        fman = getSupportFragmentManager();
        FragmentTransaction ft = fman.beginTransaction();
        switch (classifyModel.id){
            case 1:
                buyGoodsFragment = new BuyGoodsFragment();
                ft.add(R.id.ll_fragment, buyGoodsFragment, "0");
                ft.show(buyGoodsFragment);
                break;
            case 2:
                chargeFragment = new ChargeFragment();
                ft.add(R.id.ll_fragment, chargeFragment, "0");
                ft.show(chargeFragment);
                break;
            case 3:
                praiseFragment = new PraiseFragment();
                ft.add(R.id.ll_fragment, praiseFragment, "0");
                ft.show(praiseFragment);
                break;
            case 4:
//                webviewFragment = new WebviewFragment();
//                ft.add(R.id.ll_fragment, webviewFragment, "0");
//                ft.show(webviewFragment);
//                Message message = new Message();
//                message.what = MSG_REFRESH;
//                uiHandler.handleMessage(message);

                break;
        }

        ft.commitAllowingStateLoss();

    }

    private void initListener() {


    }


    private final UIHandler uiHandler = new UIHandler(this);
    private static class UIHandler extends Handler {
        private final WeakReference<SelectTypeActivity> activity;
        public UIHandler(SelectTypeActivity act) {
            super();
            activity = new WeakReference<SelectTypeActivity>(act);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_REFRESH:
                    activity.get().webviewFragment.setWebviewUrl(activity.get().classifyModel.url);
                    break;
                case MSG_WXSEND:
//                    Intent intent1 = new Intent(activity.get(), SetingHallActivity.class);
//                    Intent intent1 = new Intent(activity.get(), LoginActivity.class);
////                    intent1.putExtra("selectFrom",SetingHallActivity.SELECT_WEL);
//                    activity.get().startActivity(intent1);
//                    activity.get().finish();
                    break;
                default:
                    break;
            }
        }
    };


}

