package com.imovie.mogic.home;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.imovie.mogic.R;
import com.imovie.mogic.ScanPay.zxing.activity.CaptureActivity;
import com.imovie.mogic.car.CarPayActivity;
import com.imovie.mogic.car.DetailActivity;
import com.imovie.mogic.car.adapters.CarAdapter;
import com.imovie.mogic.car.bean.FoodBean;
import com.imovie.mogic.car.bean.TypeBean;
import com.imovie.mogic.car.utils.ViewUtils;
import com.imovie.mogic.car.view.AddWidget;
import com.imovie.mogic.car.view.ShopCarView;
import com.imovie.mogic.home.fragment.BuyGoodsFragment;
import com.imovie.mogic.home.fragment.ChargeFragment;
import com.imovie.mogic.home.fragment.PraiseFragment;
import com.imovie.mogic.home.fragment.WebviewFragment;
import com.imovie.mogic.home.model.ClassifyModel;
import com.imovie.mogic.home.model.SearchUserModel;
import com.imovie.mogic.login.LoginActivity;
import com.imovie.mogic.mine.MyAttendActivity;
import com.imovie.mogic.mine.fragment.ClockFragment;
import com.imovie.mogic.myRank.MinePraiseActivity;
import com.imovie.mogic.utills.StringHelper;
import com.imovie.mogic.utills.Utills;
import com.imovie.mogic.widget.TitleBar;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SelectTypeActivity extends BaseActivity implements AddWidget.OnAddClick{
    public static final int MSG_REFRESH = 600;
    public static final int MSG_WXSEND = 601;
    public static final String CAR_ACTION = "handleCar";
    public static final String CLEARCAR_ACTION = "clearCar";
    private TitleBar titleBar;
    private FragmentManager fman;
    public ChargeFragment chargeFragment;
    public BuyGoodsFragment buyGoodsFragment;
    public ClockFragment clockFragment;
    public WebviewFragment webviewFragment;
    public ClassifyModel classifyModel;
    private ShopCarView shopCarView;
    public static CarAdapter carAdapter;
    private CoordinatorLayout rootview;
    public BottomSheetBehavior behavior;
    public String amountStr = "0.00";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_select_type_activity);
        initView();
        setView();
        initListener();
        IntentFilter intentFilter = new IntentFilter(CAR_ACTION);
        intentFilter.addAction(CLEARCAR_ACTION);
        registerReceiver(broadcastReceiver, intentFilter);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onAddClick(View view, FoodBean fb) {
        dealCar(fb);
        ViewUtils.addTvAnim(view, shopCarView.carLoc, getApplicationContext(), rootview);
    }


    @Override
    public void onSubClick(FoodBean fb) {
        dealCar(fb);
    }

    private void dealCar(FoodBean foodBean) {
        HashMap<String, Long> typeSelect = new HashMap<>();//更新左侧类别badge用
        BigDecimal amount = new BigDecimal(0.0);
        int total = 0;
        boolean hasFood = false;
        if (behavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            buyGoodsFragment.getFoodAdapter().notifyDataSetChanged();
        }
        List<FoodBean> flist = carAdapter.getData();
        int p = -1;
        for (int i = 0; i < flist.size(); i++) {
            FoodBean fb = flist.get(i);
            if (fb.getId() == foodBean.getId()) {
                fb = foodBean;
                hasFood = true;
                if (foodBean.getSelectCount() == 0) {
                    p = i;
                } else {
                    carAdapter.setData(i, foodBean);
                }
            }
            total += fb.getSelectCount();
            if (typeSelect.containsKey(fb.getType())) {
                typeSelect.put(fb.getType(), typeSelect.get(fb.getType()) + fb.getSelectCount());
            } else {
                typeSelect.put(fb.getType(), fb.getSelectCount());
            }
            amount = amount.add(fb.getPrice().multiply(BigDecimal.valueOf(fb.getSelectCount())));
        }
        if (p >= 0) {
            carAdapter.remove(p);
        } else if (!hasFood && foodBean.getSelectCount() > 0) {
            carAdapter.addData(foodBean);
            if (typeSelect.containsKey(foodBean.getType())) {
                typeSelect.put(foodBean.getType(), typeSelect.get(foodBean.getType()) + foodBean.getSelectCount());
            } else {
                typeSelect.put(foodBean.getType(), foodBean.getSelectCount());
            }
            amount = amount.add(foodBean.getPrice().multiply(BigDecimal.valueOf(foodBean.getSelectCount())));
            total += foodBean.getSelectCount();
        }
        shopCarView.showBadge(total);
        buyGoodsFragment.getTypeAdapter().updateBadge(typeSelect);
        shopCarView.updateAmount(amount);
        amountStr = amount.toString();
    }

    public void goAccount(View view) {
        if(carAdapter.getItemCount()<=0){
            Utills.showShortToast("未选购商品");
            return;
        }else{
            Intent intent = new Intent(SelectTypeActivity.this, CarPayActivity.class);
            intent.putExtra("FoodBeanList", (Serializable) carAdapter.getData());
            intent.putExtra("fromType", 1);
            intent.putExtra("amountStr",amountStr);
            startActivity(intent);
        }
    }

    public void clearCar(View view) {
        ViewUtils.showClearCar(SelectTypeActivity.this, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                clearCar();
            }
        });
    }

    private void clearCar() {
        List<FoodBean> flist = carAdapter.getData();
        for (int i = 0; i < flist.size(); i++) {
            FoodBean fb = flist.get(i);
            fb.setSelectCount(0);
        }
        carAdapter.setNewData(new ArrayList<FoodBean>());
        buyGoodsFragment.getFoodAdapter().notifyDataSetChanged();
        shopCarView.showBadge(0);
        buyGoodsFragment.getTypeAdapter().updateBadge(new HashMap<String, Long>());
        shopCarView.updateAmount(new BigDecimal(0.0));
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

        rootview = (CoordinatorLayout) findViewById(R.id.rootview);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initShopCar();
//        rlLoginPassword = (RelativeLayout) findViewById(R.id.rlLoginPassword);
//        btnExitLogin = (Button) findViewById(R.id.btnExitLogin);

    }

    private void initShopCar() {
        behavior = BottomSheetBehavior.from(findViewById(R.id.car_container));
        shopCarView = (ShopCarView) findViewById(R.id.car_mainfl);
        View blackView = findViewById(R.id.blackview);
        shopCarView.setBehavior(behavior, blackView);
        RecyclerView carRecView = (RecyclerView) findViewById(R.id.car_recyclerview);
//		carRecView.setNestedScrollingEnabled(false);
        carRecView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        ((DefaultItemAnimator) carRecView.getItemAnimator()).setSupportsChangeAnimations(false);
        carAdapter = new CarAdapter(new ArrayList<FoodBean>(), this);
        carAdapter.bindToRecyclerView(carRecView);
        carRecView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                super.onItemChildClick(adapter, view, position);
                Utills.showShortToast("999"+position);
                if (view.getId() == R.id.car_main) {
                    Intent intent = new Intent(SelectTypeActivity.this, DetailActivity.class);
                    intent.putExtra("food", (FoodBean) adapter.getData().get(position));
                    intent.putExtra("position", position);
                    startActivity(intent);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }
            }

            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {

            }
        });


    }

    private void setView(){
        fman = getSupportFragmentManager();
        FragmentTransaction ft = fman.beginTransaction();
        if(classifyModel.code.equals("GOODS_ORDER")){
            buyGoodsFragment = new BuyGoodsFragment();
            ft.add(R.id.ll_fragment, buyGoodsFragment, "0");
            ft.show(buyGoodsFragment);
            shopCarView.setVisibility(View.VISIBLE);
            buyGoodsFragment.refresh();
        }else if(classifyModel.code.equals("USER_RECHARGE")){
            shopCarView.setVisibility(View.GONE);
            chargeFragment = new ChargeFragment();
            ft.add(R.id.ll_fragment, chargeFragment, "0");
            ft.show(chargeFragment);
        }else if(classifyModel.code.equals("ATTEND_CARD")){
            shopCarView.setVisibility(View.GONE);
            clockFragment = new ClockFragment();
            ft.add(R.id.ll_fragment, clockFragment, "0");
            ft.show(clockFragment);
            titleBar.enableRightTextView("统计", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), MyAttendActivity.class);
                    startActivity(intent);
                }
            });
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
                    activity.get().webviewFragment.setWebviewUrl(activity.get().classifyModel.iconUrl);
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

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case CAR_ACTION:
                    FoodBean foodBean = (FoodBean) intent.getSerializableExtra("foodbean");
                    FoodBean fb = foodBean;
                    int p = intent.getIntExtra("position", -1);
                    if (p >= 0 && p < buyGoodsFragment.getFoodAdapter().getItemCount()) {
                        fb = buyGoodsFragment.getFoodAdapter().getItem(p);
                        fb.setSelectCount(foodBean.getSelectCount());
                        buyGoodsFragment.getFoodAdapter().setData(p, fb);
                    } else {
                        for (int i = 0; i < buyGoodsFragment.getFoodAdapter().getItemCount(); i++) {
                            fb = buyGoodsFragment.getFoodAdapter().getItem(i);
                            if (fb.getId() == foodBean.getId()) {
                                fb.setSelectCount(foodBean.getSelectCount());
                                buyGoodsFragment.getFoodAdapter().setData(i, fb);
                                break;
                            }
                        }
                    }
                    dealCar(fb);
                    break;
                case CLEARCAR_ACTION:
                    clearCar();
                    break;
            }
            if (CAR_ACTION.equals(intent.getAction())) {

            }
        }
    };

}

