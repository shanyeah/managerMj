package com.imovie.mogic.home.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.imovie.mogic.R;
import com.imovie.mogic.ScanPay.manager.ScanPayManager;
import com.imovie.mogic.base.universal_loading.YSBLoadingDialog;
import com.imovie.mogic.car.adapters.CarAdapter;
import com.imovie.mogic.home.BaseActivity;
import com.imovie.mogic.home.adater.GoodsAdapter;
import com.imovie.mogic.home.adater.GoodsTypeAdapter;
import com.imovie.mogic.home.model.CardModel;
import com.imovie.mogic.home.model.GoodTagList;
import com.imovie.mogic.home.model.GoodTypeModel;
import com.imovie.mogic.home.model.GoodsModel;
import com.imovie.mogic.home.model.PayResultModel;
import com.imovie.mogic.home.model.SearchUserModel;
import com.imovie.mogic.home.net.HomeWebHelper;
import com.imovie.mogic.home.widget.CartPopWindow;
import com.imovie.mogic.home.widget.GoodTagDialog;
import com.imovie.mogic.home.widget.UserInfoDialog;
import com.imovie.mogic.login.model.TestModel;
import com.imovie.mogic.utills.DecimalUtil;
import com.imovie.mogic.utills.StringHelper;
import com.imovie.mogic.utills.Utills;
import com.imovie.mogic.web.IModelResultListener;
import com.imovie.mogic.web.model.HttpResultModel;
import com.imovie.mogic.widget.ClearButtonEditText;
import com.imovie.mogic.widget.FlexibleFrameLayout;
import com.imovie.mogic.widget.PullToRefreshFrameLayout;
import com.imovie.mogic.widget.YSBPageListView;
import com.imovie.mogic.widget.interfaces.IPageList;

import java.util.ArrayList;
import java.util.List;

public class BuyGoodsFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static CarAdapter carAdapter;
    private String mParam1;
    private String mParam2;
    private PullToRefreshFrameLayout pull_content;
    private FlexibleFrameLayout ff_list;
    private LinearLayout ll_ad;
    public LinearLayout rlSearchTitle;
    public ClearButtonEditText etHomeSearch;
    public RelativeLayout rlShopCart;
    public YSBPageListView lvGoodsTypeList;
    public GoodsTypeAdapter typeAdapter;
    public ArrayList<GoodTypeModel> listType;

    public YSBPageListView lvCard;
    public ArrayList<GoodsModel> listCard;
    public GoodsAdapter goodsAdapter;
    public ArrayList<GoodsModel> listAllCard = new ArrayList<>();

    public ArrayList<GoodsModel> listSearch = new ArrayList<>();

    public CartPopWindow<GoodsModel> cartPopWindow;
    private List<GoodsModel> list = new ArrayList<>();

    private ViewGroup anim_mask_layout;//动画层
    public ImageView ivShopCart;
    private int buyNum = 0;//购买数量
    private TextView tvSumPrice;//购物车上的数量标签
    public TextView tvCount;
    public double TotalPrice=0;
    public int payType = 1;
    public GoodsModel goodModel;

    public BuyGoodsFragment() {

    }

    public static BuyGoodsFragment newInstance(String param1, String param2) {
        BuyGoodsFragment fragment = new BuyGoodsFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_goods, container, false);
        initView(v);
        setView();
        return v;
    }

    private void initView(View view) {
        pull_content = (PullToRefreshFrameLayout) view.findViewById(R.id.pull_content);
        ff_list = (FlexibleFrameLayout) view.findViewById(R.id.ff_list);
        ll_ad = (LinearLayout) view.findViewById(R.id.ll_ad);
        rlSearchTitle = (LinearLayout) view.findViewById(R.id.rlSearchTitle);
        rlShopCart = (RelativeLayout) view.findViewById(R.id.rlShopCart);
        lvGoodsTypeList = (YSBPageListView) view.findViewById(R.id.lvGoodsTypeList);
        lvCard = (YSBPageListView) view.findViewById(R.id.lv_card_list);
        ivShopCart = (ImageView) view.findViewById(R.id.ivShopCart);
        tvCount = (TextView) view.findViewById(R.id.tvCount);
        tvSumPrice = (TextView) view.findViewById(R.id.tvSumPrice);
        etHomeSearch = (ClearButtonEditText) view.findViewById(R.id.etHomeSearch);
        cartPopWindow = new CartPopWindow<GoodsModel>(getActivity(), list,itemClickListener);
        cartPopWindow.setAnimationStyle(R.style.popupwindow_anim_top_style);
        cartPopWindow.setOnSelectListener(new CartPopWindow.onSelectListener() {
            @Override
            public void onSelect(int type,String text,int id) {
                if(type == 1) {
                    if(payType == 2){
                        saveGoodsOrder("",TotalPrice, text, list);
                    }else{
                        String input = cartPopWindow.getMemberNum();
                        if(StringHelper.isEmpty(input)){
                            Utills.showShortToast("请输入付款帐号");
                            return;
                        }
                        getCheckUserInfo(text,input);
                    }

                }else if(type == 2){
                    payType = 1;
                }else if(type == 3){
                    payType = 2;
                }else if(type == 4){
                    addOneCartToList(text,id);
                }else if(type == 5){
                    reduceCartList(text,id);
                }else if(type == 6){
                    ScanPayManager.enterCaptureActivity(getContext(),(BaseActivity)getActivity());
                }else if(type == 7){
                    clearCart();
                }
            }
        });
    }

    private void setView() {
        setPullAndFlexListener();
        listType = new ArrayList<>();
        typeAdapter = new GoodsTypeAdapter(getContext(),listType);
        lvGoodsTypeList.setAdapter(typeAdapter);
        typeAdapter.setSelectIndex(0);

        listCard = new ArrayList<>();
        goodsAdapter = new GoodsAdapter(getContext(),listCard);
        goodsAdapter.setOnSelectListener(new GoodsAdapter.onSelectListener() {
            @Override
            public void onSelect(final GoodsModel goodsModel, View v) {
                if(goodsModel.goodsTagList!=null && goodsModel.goodsTagList.size()>0) {
                    GoodTagDialog dialog = new GoodTagDialog(getContext(), goodsModel.goodsTagList,v);
                    dialog.setOnSelectListener(new GoodTagDialog.onSelectListener() {
                        @Override
                        public void onSelect(GoodTagList goodTagList, View v) {
                            TotalPrice += goodsModel.price;
                            tvSumPrice.setText(DecimalUtil.FormatMoney(TotalPrice / 100) + getContext().getResources().getString(R.string.symbol_RMB));
                            int[] startLocation = new int[2];// 一个整型数组，用来存储按钮的在屏幕的X、Y坐标
                            v.getLocationInWindow(startLocation);// 这是获取购买按钮的在屏幕的X、Y坐标（这也是动画开始的坐标）
                            ImageView ball = new ImageView(getContext());// buyImg是动画的图片，我的是一个小球（R.drawable.sign）
                            ball.setImageResource(R.drawable.home_dot);// 设置buyImg的图片
                            setAnim(ball, startLocation);// 开始执行动画
                            GoodsModel goods = new GoodsModel();
                            goods.id = goodsModel.id;
                            goods.name = goodsModel.name;
                            goods.price = goodsModel.price;
                            goods.imageUrl = goodsModel.imageUrl;
                            goods.haveTag = true;
//                            goods.goodsTagCategory = goodTagList.goodsTagCategory;
                            goods.goodsTagCategory = goodTagList.id;
                            goods.tagName = goodTagList.name;
                            AddCartList(goods);
                        }
                    });
                    dialog.show();
                }else {
                    TotalPrice += goodsModel.price;
                    tvSumPrice.setText(DecimalUtil.FormatMoney(TotalPrice / 100) + getContext().getResources().getString(R.string.symbol_RMB));
                    int[] startLocation = new int[2];// 一个整型数组，用来存储按钮的在屏幕的X、Y坐标
                    v.getLocationInWindow(startLocation);// 这是获取购买按钮的在屏幕的X、Y坐标（这也是动画开始的坐标）
                    ImageView ball = new ImageView(getContext());// buyImg是动画的图片，我的是一个小球（R.drawable.sign）
                    ball.setImageResource(R.drawable.home_dot);// 设置buyImg的图片
                    setAnim(ball, startLocation);// 开始执行动画
                    AddCartList(goodsModel);
                }
            }
        });
        lvCard.setAdapter(goodsAdapter);

        lvCard.setOnPageListener(new IPageList.OnPageListener() {
            @Override
            public void onLoadMoreItems() {
                getAllGoodList();
            }
        });

        lvCard.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        lvCard.startLoad();

        lvGoodsTypeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                typeAdapter.setSelectIndex(position);
                int index = typeAdapter.getItem(position).index;
                lvCard.setSelectPosition(index);
                lvCard.smoothScrollToPosition(position);

            }
        });

        rlShopCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(list.size()>0) {
                    cartPopWindow.setWidth(rlSearchTitle.getWidth());
                    cartPopWindow.showAsDropDown(rlSearchTitle);
                    cartPopWindow.refreshData(list);
                }
            }
        });

        etHomeSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String chargeFee = editable.toString();
                if(!StringHelper.isEmpty(chargeFee)){
                    listSearch.clear();
                    for(int i = 0;i<listAllCard.size();i++){
                        if(listAllCard.get(i).name.contains(chargeFee)){
                            listSearch.add(listAllCard.get(i));
                        }
                    }
                    if(listSearch.size()>0) {
                        goodsAdapter.list = listSearch;
                        goodsAdapter.notifyDataSetChanged();
                    }
                }
            }
        });

    }

    private void setPullAndFlexListener(){
        ff_list.setFlexView(ll_ad);
        ff_list.setFlexible(true);

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
                getAllGoodList();
            }
        });

        lvCard.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == SCROLL_STATE_IDLE) {

                    //列表处于最上方
                    if (true && view.getChildAt(0).getTop() == 0) {
                        ff_list.setFlexible(true);
                    } else {
                        ff_list.setFlexible(false);
                    }
                }

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//                typeAdapter.setSelectCategory(goodsAdapter.list.get(firstVisibleItem).category);
            }
        });

        lvGoodsTypeList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == SCROLL_STATE_IDLE) {
                    //列表处于最上方
                    if (true && view.getChildAt(0).getTop() == 0) {
                        ff_list.setFlexible(true);
                    } else {
                        ff_list.setFlexible(false);
                    }
                }

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

    }

    /**
     * popupwindow显示的ListView的item点击事件
     */
    private AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
//            internetBarModel = (SearchUserModel)mSpinerPopWindow.list.get(position);
//            tvUserName.setText(internetBarModel.name + "(" + internetBarModel.idNumber + ")");
//            userId = internetBarModel.userId;
//            mSpinerPopWindow.dismiss();

        }
    };

    public void reduceCartList(String text,int id){
        for(int i=0;i<list.size();i++){
            if(list.get(i).id == Integer.parseInt(text)){
                if(list.get(i).haveTag){
                    if(list.get(i).goodsTagCategory == id){
                        reduceCart(i);
                        break;
                    }
                }else{
                    reduceCart(i);
                    break;
                }

            }
        }
    }

    public void reduceCart(int i){
        if(list.get(i).sum>=1) {
            list.get(i).sum -= 1;
            TotalPrice -= list.get(i).price;
            buyNum--;//让购买数量加1
            tvCount.setText(""+buyNum);
            if(buyNum==0){
                tvCount.setVisibility(View.GONE);
            }
            tvSumPrice.setText(DecimalUtil.FormatMoney(TotalPrice / 100) + getContext().getResources().getString(R.string.symbol_RMB));
            if(list.get(i).sum ==0)list.remove(list.get(i));
            cartPopWindow.refreshData(list);
        }
    }

    public void addOneCartToList(String text,int id){
        for(int i=0;i<list.size();i++){
            if(list.get(i).id == Integer.parseInt(text)){
                if(list.get(i).haveTag){
                    if(list.get(i).goodsTagCategory == id){
                        addCart(i);
                        break;
                    }
                }else{
                    addCart(i);
                    break;
                }
            }
        }
    }
    public void addCart(int i){
        list.get(i).sum += 1;
        TotalPrice += list.get(i).price;
        buyNum++;//让购买数量加1
        tvCount.setText(buyNum + "");
        tvSumPrice.setText(DecimalUtil.FormatMoney(TotalPrice/100) + getContext().getResources().getString(R.string.symbol_RMB));
        cartPopWindow.refreshData(list);
    }

    public void clearCart(){
        buyNum=0;
        TotalPrice = 0;
        tvCount.setVisibility(View.GONE);
        tvSumPrice.setText(DecimalUtil.FormatMoney(TotalPrice / 100) + getContext().getResources().getString(R.string.symbol_RMB));
        list.clear();
        cartPopWindow.refreshData(list);
    }

    public void AddCartList(GoodsModel goodsModel){
        if(list.size()==0){
            goodsModel.sum = 1;
            list.add(goodsModel);
        }else{
            boolean isHave = false;
            if(goodsModel.haveTag){
                for(int i=0;i<list.size();i++){
                    if(list.get(i).id == goodsModel.id && list.get(i).goodsTagCategory == goodsModel.goodsTagCategory){
                        list.get(i).sum += 1;
                        isHave = true;
                    }
                }
            }else{
                for(int i=0;i<list.size();i++){
                    if(list.get(i).id == goodsModel.id){
                        list.get(i).sum += 1;
                        isHave = true;
                    }
                }
            }

            if(!isHave){
                goodsModel.sum = 1;
                list.add(goodsModel);
            }
        }
    }

    public void refresh(){
        getAllGoodList();
    }

    public void getAllGoodList(){
        HomeWebHelper.getAllGoodList(new IModelResultListener<CardModel>() {
            @Override
            public boolean onGetResultModel(HttpResultModel resultModel) {
                pull_content.endRefresh(true);
                lvCard.finishLoading(true);
                return false;
            }

            @Override
            public void onSuccess(String resultCode, CardModel resultModel, List<CardModel> resultModelList, String resultMsg, String hint) {
                pull_content.endRefresh(true);
                lvCard.finishLoading(true);
                if(resultCode.equals("0")) {
                    goodsAdapter.list.clear();
                    listAllCard.clear();
                    listType.clear();
                    int index = 0;
                    for(int i=0;i<resultModel.categorys.size();i++){
                        GoodTypeModel typeModel = new GoodTypeModel();
                        typeModel.id = resultModel.categorys.get(i).id;
                        typeModel.name = resultModel.categorys.get(i).name;
                        typeModel.index = index;
                        listType.add(typeModel);
                        List<GoodsModel> goods = resultModel.categorys.get(i).goods;
                        goods.get(0).category = typeModel.name;
                        goods.get(0).isShow = true;
                        goodsAdapter.list.addAll(goods);
                        listAllCard.addAll(goods);
                        index += goods.size();
                    }
                    typeAdapter.list = listType;
                    typeAdapter.notifyDataSetChanged();
                    typeAdapter.setSelectIndex(0);

//                    goodsAdapter.list = resultModel.categorys.get(0).goods;
                    goodsAdapter.notifyDataSetChanged();
                    lvCard.setHaveMoreData(false);
                    lvCard.setTotalPage(1);
                }
            }

            @Override
            public void onFail(String resultCode, String resultMsg, String hint) {
                pull_content.endRefresh(true);
                lvCard.finishLoading(true);
            }

            @Override
            public void onError(String errorMsg) {
                pull_content.endRefresh(true);
                lvCard.finishLoading(true);
            }
        });
    }

    public void getCheckUserInfo(final String text,String input){
        HomeWebHelper.getCheckUserInfo(input,new IModelResultListener<SearchUserModel>() {
            @Override
            public boolean onGetResultModel(HttpResultModel resultModel) {
                return false;
            }

            @Override
            public void onSuccess(String resultCode, SearchUserModel resultModel, List<SearchUserModel> resultModelList, String resultMsg, String hint) {
//                Log.e("----city:",""+resultCode);
                if(resultModelList.size()>0){
                    UserInfoDialog dialog = new UserInfoDialog(getContext(),resultModelList);
                    dialog.setOnSelectListener(new UserInfoDialog.onSelectListener() {
                        @Override
                        public void onSelect(SearchUserModel userModel) {
//                            payGoodsOrder(goodsOrderId , clerkOrderId , userModel.qrCode);
                            saveGoodsOrder(userModel.qrCode,TotalPrice, text, list);
                        }
                    });
                    dialog.show();


//                    payGoodsOrder(goodsOrderId , clerkOrderId , resultModelList.get(0).qrCode);
//                    Utills.showShortToast(resultModelList.get(0).qrCode);
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


    public void saveGoodsOrder(String qrCode,double totalFee, String seatName, List<GoodsModel> goodsList){
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
        HomeWebHelper.saveGoodsOrder(qrCode,totalFee, seatName, goodsList,new IModelResultListener<PayResultModel>() {
            @Override
            public boolean onGetResultModel(HttpResultModel resultModel) {
                return false;
            }

            @Override
            public void onSuccess(String resultCode, PayResultModel resultModel, List<PayResultModel> resultModelList, String resultMsg, String hint) {
                if(resultCode.equals("0")) {
                    if(payType == 2){
                        ScanPayManager.enterCaptureActivity(getContext(),resultModel);
                    }else{
                        payGoodsOrder(resultModel.goodsOrderId , resultModel.clerkOrderId,"");
                    }

                }else{
                    Utills.showShortToast(resultMsg);
                }
            }

            @Override
            public void onFail(String resultCode, String resultMsg, String hint) {
                lvCard.finishLoading(true);
            }

            @Override
            public void onError(String errorMsg) {
                lvCard.finishLoading(true);
            }
        });
    }

    public void payGoodsOrder(int goodsOrderId, int clerkOrderId,String code){
        HomeWebHelper.payGoodsOrder(goodsOrderId , clerkOrderId , code ,new IModelResultListener<TestModel>() {
            @Override
            public boolean onGetResultModel(HttpResultModel resultModel) {
                return false;
            }

            @Override
            public void onSuccess(String resultCode, TestModel resultModel, List<TestModel> resultModelList, String resultMsg, String hint) {
//                CardModel t = resultModel;
//                List<CardModel> l = resultModelList;
//                String s = resultCode;
                if(resultCode.equals("0")) {
                    Utills.showShortToast(resultMsg);
                    clearCart();
                }else{
                    Utills.showShortToast(resultMsg);
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


    /**
     * @Description: 创建动画层
     * @param
     * @return void
     * @throws
     */
    private ViewGroup createAnimLayout() {
        ViewGroup rootView = (ViewGroup) getActivity().getWindow().getDecorView();
        LinearLayout animLayout = new LinearLayout(getActivity());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        animLayout.setLayoutParams(lp);
        animLayout.setId(Integer.MAX_VALUE-1);
        animLayout.setBackgroundResource(android.R.color.transparent);
        rootView.addView(animLayout);
        return animLayout;
    }

    private View addViewToAnimLayout(final ViewGroup parent, final View view,
                                     int[] location) {
        int x = location[0];
        int y = location[1];
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.leftMargin = x;
        lp.topMargin = y;
        view.setLayoutParams(lp);
        return view;
    }

    public void setAnim(final View v, int[] startLocation) {
        anim_mask_layout = null;
        anim_mask_layout = createAnimLayout();
        anim_mask_layout.addView(v);//把动画小球添加到动画层
        final View view = addViewToAnimLayout(anim_mask_layout, v,
                startLocation);
        int[] endLocation = new int[2];// 存储动画结束位置的X、Y坐标
        ivShopCart.getLocationInWindow(endLocation);// shopCart是那个购物车
//        Utills.showShortToast("x="+startLocation[0]+"y="+startLocation[1]);
//        Utills.showShortToast("x1="+endLocation[0]+"y1="+endLocation[1]);

        // 计算位移
        int endX = endLocation[0] - startLocation[0];// 动画位移的X坐标
        int endY = endLocation[1] - startLocation[1]+3;// 动画位移的y坐标
        TranslateAnimation translateAnimationX = new TranslateAnimation(0,
                endX, 0, 0);
        translateAnimationX.setInterpolator(new LinearInterpolator());
        translateAnimationX.setRepeatCount(0);// 动画重复执行的次数
        translateAnimationX.setFillAfter(true);

        TranslateAnimation translateAnimationY = new TranslateAnimation(0, 0,
                0, endY);
        translateAnimationY.setInterpolator(new AccelerateInterpolator());
        translateAnimationY.setRepeatCount(0);// 动画重复执行的次数
        translateAnimationX.setFillAfter(true);

        AnimationSet set = new AnimationSet(false);
        set.setFillAfter(false);
        set.addAnimation(translateAnimationY);
        set.addAnimation(translateAnimationX);
        set.setDuration(500);// 动画的执行时间
        view.startAnimation(set);
        // 动画监听事件
        set.setAnimationListener(new Animation.AnimationListener() {
            // 动画的开始
            @Override
            public void onAnimationStart(Animation animation) {
                v.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // TODO Auto-generated method stub
            }

            // 动画的结束
            @Override
            public void onAnimationEnd(Animation animation) {
                v.setVisibility(View.GONE);
                buyNum++;//让购买数量加1
                tvCount.setText(buyNum + "");
                tvCount.setVisibility(View.VISIBLE);
//                buyNumView.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
//                buyNumView.show();
            }
        });
    }

}
