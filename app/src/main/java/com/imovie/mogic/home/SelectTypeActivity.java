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
import android.util.Log;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.imovie.mogic.MyApplication;
import com.imovie.mogic.R;
import com.imovie.mogic.ScanPay.zxing.activity.CaptureActivity;
import com.imovie.mogic.car.CarPayActivity;
import com.imovie.mogic.car.DetailActivity;
import com.imovie.mogic.car.adapters.CarAdapter;
import com.imovie.mogic.car.bean.FoodBean;
import com.imovie.mogic.car.bean.TypeBean;
import com.imovie.mogic.car.utils.ViewUtils;
import com.imovie.mogic.car.view.AddWidget;
import com.imovie.mogic.car.view.MaxHeightRecyclerView;
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
import com.imovie.mogic.widget.SmartPopupWindow;
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
    public static final String SUB_ACTION = "handleSub";
    public static final String LIST_ACTION = "handleList";
    public static final String CLEARCAR_ACTION = "clearCar";
    public static final String CAR_REFRESH = "carRefresh";
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
        intentFilter.addAction(LIST_ACTION);
        intentFilter.addAction(SUB_ACTION);
        intentFilter.addAction(CAR_REFRESH);
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
    public void onAddClick(View view, FoodBean fb,int type) {
//        dealCar(fb);
        if(type==2) {
            addOneCar(fb ,false);
            ViewUtils.addTvAnim(view, shopCarView.carLoc, getApplicationContext(), rootview);
        }else{
            if(fb.getGoodsPackList().size()>0){
                List<FoodBean> flist = carAdapter.getData();
                long count=0;
                for(int j=0;j<flist.size();j++){
                    if(flist.get(j).getId() == fb.getId()) {
                        count +=1;
                    }
                }
                if(count==0){
                    addOneCar(fb,false);
                    ViewUtils.addTvAnim(view, shopCarView.carLoc, getApplicationContext(), rootview);
                }else if(count==1){
                    addOneCar(fb,true);
                }else{
                    SmartPopupWindow popupWindow = new SmartPopupWindow(SelectTypeActivity.this,view);
                    popupWindow.showPopupWindow();
                }
            }else{
                addOneCar(fb ,false);
                ViewUtils.addTvAnim(view, shopCarView.carLoc, getApplicationContext(), rootview);
            }

//            Utills.showShortToast(""+type);
        }
    }


    @Override
    public void onSubClick(View view,FoodBean fb,int type) {
//        dealCar(fb);

        if (type == 2) {
            subOneCar(fb,false);
        } else {
            if (fb.getGoodsPackList().size() > 0) {
                List<FoodBean> flist = carAdapter.getData();
                long count = 0;
                for (int j = 0; j < flist.size(); j++) {
                    if (flist.get(j).getId() == fb.getId()) {
                        count += 1;
                    }
                }
                if (count == 1) {
                    subOneCar(fb,true);
                } else {
                    SmartPopupWindow popupWindow = new SmartPopupWindow(SelectTypeActivity.this,view);
                    popupWindow.showPopupWindow();
                }
            } else {
                subOneCar(fb,false);
            }
        }
    }

    private void dealCar(FoodBean food) {
        FoodBean foodBean = food.getFoodBean(food);
        HashMap<String, Long> typeSelect = new HashMap<>();//更新左侧类别badge用
        BigDecimal amount = new BigDecimal(0.0);
        int total = 0;
        boolean hasFood = false;
        boolean hasPack = false;
        if (behavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            buyGoodsFragment.getFoodAdapter().notifyDataSetChanged();
        }
        List<FoodBean> flist = carAdapter.getData();
        int p = -1;

        for (int i = 0; i < flist.size(); i++) {
            FoodBean fb = flist.get(i);
            if (fb.getId() == foodBean.getId()) {

                if(foodBean.getGoodsPackList().size()>0){
                    if(foodBean.getGoodsPackList().size()!=fb.getGoodsPackList().size()){
                        hasFood = false;
                    }else{
                        for(int j=0;j<foodBean.getGoodsPackList().size();j++){
                            if(foodBean.getGoodsPackList().get(j).getGoodsId()!=fb.getGoodsPackList().get(j).getGoodsId()){
                                hasPack = true;
                                break;
                            }
                        }
                        if(hasPack){
                            hasFood = false;
//                            foodBean.setSelectCount(1);
                        }else{
                            hasFood = true;
                            if (foodBean.getSelectCount() == 0) {
                                p = i;
                            } else {
                                carAdapter.setData(i, foodBean);
                            }

                        }

                    }

//					    Log.e("----111",foodBean.getGoodsPackList().size()+"=:="+fb.getGoodsPackList().size());
//                        Log.e("----222",""+hasFood);
                }else{
                    hasFood = true;
//                    foodBean.setSelectCount(foodBean.getSelectCount()+1);
                    if (foodBean.getSelectCount() == 0) {
                        p = i;
                    } else {
                        carAdapter.setData(i, foodBean);
//                        total += foodBean.getSelectCount();
//                        amount = amount.add(foodBean.getPrice());
                    }

                }

            }else{

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
//            foodBean.getSelectCount() > 0
//            foodBean.setSelectCount(1);
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

        Intent intent = new Intent(SelectTypeActivity.LIST_ACTION);
//        if (foodBean.getId() == this.foodBean.getId()) {
//            intent.putExtra("position", position);
//        }
        intent.putExtra("foodbean", foodBean);
        sendBroadcast(intent);
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
//        carAdapter.setNewData(new ArrayList<FoodBean>());
        MyApplication.getInstance().getCarListData().clear();
        carAdapter.notifyDataSetChanged();
        buyGoodsFragment.refreshGoodlist();
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
        MaxHeightRecyclerView carRecView = (MaxHeightRecyclerView) findViewById(R.id.car_recyclerview);
//		carRecView.setNestedScrollingEnabled(false);
        carRecView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        ((DefaultItemAnimator) carRecView.getItemAnimator()).setSupportsChangeAnimations(false);
        carAdapter = new CarAdapter(MyApplication.getInstance().getCarListData(), this);
        carAdapter.bindToRecyclerView(carRecView);
        carRecView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                super.onItemChildClick(adapter, view, position);
                Utills.showShortToast("999"+position);
//                if (view.getId() == R.id.car_main) {
//                    Intent intent = new Intent(SelectTypeActivity.this, DetailActivity.class);
//                    intent.putExtra("food", (FoodBean) adapter.getData().get(position));
//                    intent.putExtra("FoodBeanList", (Serializable) carAdapter.getData());
//                    intent.putExtra("position", position);
//                    startActivity(intent);
//                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
//                }

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
            titleBar.enableRightImageView(R.drawable.home_refresh, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    buyGoodsFragment.refresh();
                }
            });
            buyGoodsFragment = new BuyGoodsFragment();
            ft.add(R.id.ll_fragment, buyGoodsFragment, "0");
            ft.show(buyGoodsFragment);
            shopCarView.setVisibility(View.VISIBLE);
//            buyGoodsFragment.getCasheDate();
            Message msg = new Message();
            msg.what = MSG_REFRESH;
            uiHandler.sendMessageDelayed(msg,3000);
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
//                    activity.get().webviewFragment.setWebviewUrl(activity.get().classifyModel.iconUrl);
                    activity.get().dealBeginCar();
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
                    FoodBean fb= (FoodBean) intent.getSerializableExtra("foodbean");
                    refreshCar(fb);
                    break;
                case SUB_ACTION:
                    FoodBean f= (FoodBean) intent.getSerializableExtra("foodbean");
                    subOneCar(f,false);
                    break;

                case CAR_REFRESH:
                    dealBeginCar();
                    break;

                case LIST_ACTION:
                    FoodBean food= (FoodBean) intent.getSerializableExtra("foodbean");
//                    FoodBean fb = foodBean;
                    for (int i = 0; i < buyGoodsFragment.getFoodAdapter().getItemCount(); i++) {
                        FoodBean fbean = buyGoodsFragment.getFoodAdapter().getItem(i);
                        if (fbean.getGoodsId() == food.getGoodsId()) {
                            List<FoodBean> flist = carAdapter.getData();
                            long count=0;
                            for(int j=0;j<flist.size();j++){
                                if(flist.get(j).getGoodsId() == food.getGoodsId()) {
                                    count +=flist.get(j).getSelectCount();
                                }
                            }
                            fbean.setSelectCount(count);
                            food.setSelectCount(count);
                            buyGoodsFragment.getFoodAdapter().setData(i, food);
                            break;
                        }
                    }
                    break;

                case CLEARCAR_ACTION:
                    clearCar();
                    break;
            }
            if (CAR_ACTION.equals(intent.getAction())) {

            }
        }
    };



    private void addOneCar(FoodBean food,boolean bl) {
        FoodBean foodBean = food.getFoodBean(food);
        HashMap<String, Long> typeSelect = new HashMap<>();//更新左侧类别badge用
        BigDecimal amount = new BigDecimal(0.0);
        int total = 0;
        boolean hasFood = false;
        boolean hasPack = false;
        if (behavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            buyGoodsFragment.getFoodAdapter().notifyDataSetChanged();
        }
        List<FoodBean> flist = carAdapter.getData();
        int p = -1;
        for (int i = 0; i < flist.size(); i++) {
            FoodBean fb = flist.get(i);
            if (fb.getId() == foodBean.getId()) {
                if(foodBean.getGoodsPackList().size()>0){
                    if(bl){
                        hasFood = true;
                        hasPack = true;
                        fb.setSelectCount(fb.getSelectCount()+1);
                        carAdapter.setData(i, fb);
                    }else{
                        if(fb.getGoodsPackList().containsAll(foodBean.getGoodsPackList())){
                            hasFood = true;
                            hasPack = true;
                            fb.setSelectCount(fb.getSelectCount()+1);
                            carAdapter.setData(i, fb);
                        }else{
                            if(hasPack){
                                hasFood = true;
                            }else{
                                hasFood = false;
                            }
                        }
                    }
                }else{
                    if(!fb.getTagsName().equals(foodBean.getTagsName())){
                        hasFood = false;
                    }else {
                        hasFood = true;
                        fb.setSelectCount(fb.getSelectCount() + 1);
                        carAdapter.setData(i, fb);
                    }
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
        } else if (!hasFood) {
//            foodBean.getSelectCount() > 0
//            foodBean.setSelectCount(1);
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

        Intent intent = new Intent(SelectTypeActivity.LIST_ACTION);
//        if (foodBean.getId() == this.foodBean.getId()) {
//            intent.putExtra("position", position);
//        }
        intent.putExtra("foodbean", foodBean);
        sendBroadcast(intent);
    }

    private void subOneCar(FoodBean foodBean,boolean bl) {
//        FoodBean foodBean = food.getFoodBean(food);
        HashMap<String, Long> typeSelect = new HashMap<>();//更新左侧类别badge用
        BigDecimal amount = new BigDecimal(0.0);
        int total = 0;
        boolean hasFood = false;
        boolean hasPack = false;
        if (behavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            buyGoodsFragment.getFoodAdapter().notifyDataSetChanged();
        }
        List<FoodBean> flist = carAdapter.getData();
        int p = -1;
        for (int i = 0; i < flist.size(); i++) {
            FoodBean fb = flist.get(i);
            if (fb.getId() == foodBean.getId()) {
                if(foodBean.getGoodsPackList().size()>0){
                    if(bl){
                        hasFood = true;
                        hasPack = true;
                        fb.setSelectCount(fb.getSelectCount()-1);
                        carAdapter.setData(i, fb);
                    }else{
                        if(fb.getGoodsPackList().containsAll(foodBean.getGoodsPackList())){
                            hasFood = true;
                            hasPack = true;
                            fb.setSelectCount(fb.getSelectCount()-1);
                            carAdapter.setData(i, fb);
                        }else{
                            if(hasPack){
                                hasFood = true;
                            }else{
                                hasFood = false;
                            }
                        }
                    }

                }else{
                    if(!fb.getTagsName().equals(foodBean.getTagsName())){
                        hasFood = false;
                    }else {
                        hasFood = true;
                        fb.setSelectCount(fb.getSelectCount() - 1);
                        carAdapter.setData(i, fb);
                    }
                }

            }
            total += fb.getSelectCount();
            if (typeSelect.containsKey(fb.getType())) {
                typeSelect.put(fb.getType(), typeSelect.get(fb.getType()) + fb.getSelectCount());
            } else {
                typeSelect.put(fb.getType(), fb.getSelectCount());
            }
            amount = amount.add(fb.getPrice().multiply(BigDecimal.valueOf(fb.getSelectCount())));
            if (fb.getSelectCount() <= 0) {
                p=i;
            }
        }

        shopCarView.showBadge(total);
        buyGoodsFragment.getTypeAdapter().updateBadge(typeSelect);
        shopCarView.updateAmount(amount);
        amountStr = amount.toString();

        if(p>=0)carAdapter.remove(p);

        Intent intent = new Intent(SelectTypeActivity.LIST_ACTION);
//        if (foodBean.getId() == this.foodBean.getId()) {
//            intent.putExtra("position", position);
//        }
        intent.putExtra("foodbean", foodBean);
        sendBroadcast(intent);

    }

    private void refreshCar(FoodBean food) {
        FoodBean foodBean = food.getFoodBean(food);
        HashMap<String, Long> typeSelect = new HashMap<>();//更新左侧类别badge用
        BigDecimal amount = new BigDecimal(0.0);
        int total = 0;
        if (behavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            buyGoodsFragment.getFoodAdapter().notifyDataSetChanged();
        }
        List<FoodBean> flist = MyApplication.getInstance().getCarListData();
        int p = -1;
        for (int i = 0; i < flist.size(); i++) {
            FoodBean fb = flist.get(i);

            total += fb.getSelectCount();
            if (typeSelect.containsKey(fb.getType())) {
                typeSelect.put(fb.getType(), typeSelect.get(fb.getType()) + fb.getSelectCount());
            } else {
                typeSelect.put(fb.getType(), fb.getSelectCount());
            }
            amount = amount.add(fb.getPrice().multiply(BigDecimal.valueOf(fb.getSelectCount())));

        }

        shopCarView.showBadge(total);
        buyGoodsFragment.getTypeAdapter().updateBadge(typeSelect);
        shopCarView.updateAmount(amount);
        amountStr = amount.toString();
        carAdapter.notifyDataSetChanged();

        Intent intent = new Intent(SelectTypeActivity.LIST_ACTION);
        intent.putExtra("foodbean", foodBean);
        sendBroadcast(intent);
    }

    private void dealBeginCar() {
        try {
            if(MyApplication.getInstance().getCarListData().size()>0) {
    //		FoodBean foodBean = food.getFoodBean(food);
                BigDecimal amount = new BigDecimal(0.0);
                HashMap<String, Long> typeSelect = new HashMap<>();//更新左侧类别badge用
                int total = 0;

                List<FoodBean> flist = carAdapter.getData();
//                for (int i = 0; i < flist.size(); i++) {
//                    FoodBean fb = flist.get(i);
//                    total += fb.getSelectCount();
//                    amount = amount.add(fb.getPrice().multiply(BigDecimal.valueOf(fb.getSelectCount())));
//
//                }
                for (int i = 0; i < flist.size(); i++) {
                    FoodBean fb = flist.get(i);
                    if (fb.getSelectCount() <= 0) {
                        carAdapter.remove(i);
                        continue;
                    }
                    total += fb.getSelectCount();
                    if (typeSelect.containsKey(fb.getType())) {
                        typeSelect.put(fb.getType(), typeSelect.get(fb.getType()) + fb.getSelectCount());
                    } else {
                        typeSelect.put(fb.getType(), fb.getSelectCount());
                    }
                    amount = amount.add(fb.getPrice().multiply(BigDecimal.valueOf(fb.getSelectCount())));

                    Intent intent = new Intent(SelectTypeActivity.LIST_ACTION);
                    intent.putExtra("foodbean", fb);
                    sendBroadcast(intent);

                }
                shopCarView.showBadge(total);
    		    buyGoodsFragment.getTypeAdapter().updateBadge(typeSelect);
                shopCarView.updateAmount(amount);
                amountStr = amount.toString();
                shopCarView.showBadge(total);
                shopCarView.updateAmount(amount);
                amountStr = amount.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

