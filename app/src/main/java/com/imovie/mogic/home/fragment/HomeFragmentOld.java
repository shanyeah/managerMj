package com.imovie.mogic.home.fragment;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RatingBar;
import android.widget.TextView;

import com.imovie.mogic.MyApplication;
import com.imovie.mogic.R;
import com.imovie.mogic.ScanPay.manager.ScanPayManager;
import com.imovie.mogic.car.bean.FoodBean;
import com.imovie.mogic.card.model.InternetBarModel;
import com.imovie.mogic.gameHall.adater.RatingAdapter;
import com.imovie.mogic.gameHall.model.ReviewListModel;
import com.imovie.mogic.gameHall.model.ReviewModel;
import com.imovie.mogic.gameHall.net.HallWebHelper;
import com.imovie.mogic.home.MainActivity;
import com.imovie.mogic.home.SelectTypeActivity;
import com.imovie.mogic.home.adater.ClassifyAdapter;
import com.imovie.mogic.home.adbanner.AdSlideBanner;
import com.imovie.mogic.home.model.ClassifyModel;
import com.imovie.mogic.home.model.DBModel_SlideBanner;
import com.imovie.mogic.home.model.GameHall;
import com.imovie.mogic.home.model.HallModel;
import com.imovie.mogic.home.model.HomeModel;
import com.imovie.mogic.home.net.HomeWebHelper;
import com.imovie.mogic.login.LoginActivity;
import com.imovie.mogic.login.model.LoginModel;
import com.imovie.mogic.login.model.TestModel;
import com.imovie.mogic.mine.MapActivity;
import com.imovie.mogic.myRank.widget.SpinerPopWindow;
import com.imovie.mogic.utills.ACache;
import com.imovie.mogic.utills.DecimalUtil;
import com.imovie.mogic.utills.StringHelper;
import com.imovie.mogic.utills.Utills;
import com.imovie.mogic.web.IModelResultListener;
import com.imovie.mogic.web.model.HttpResultModel;
import com.imovie.mogic.webview.WebViewManager;
import com.imovie.mogic.widget.FlexibleFrameLayout;
import com.imovie.mogic.widget.NoScrollGridView;
import com.imovie.mogic.widget.PullToRefreshFrameLayout;
import com.imovie.mogic.widget.TitleBar;
import com.imovie.mogic.widget.YSBCommentListView;
import com.imovie.mogic.widget.interfaces.IPageList;

import java.util.ArrayList;
import java.util.List;

public class HomeFragmentOld extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private AdSlideBanner adSlideBanner;
    private List<DBModel_SlideBanner> dbModel_slideBanners = new ArrayList<>();
    private TextView tvHallAddress;
    private TextView tvHallTelNum;
    private TextView tvCommentNum;
    private TitleBar titleBar;
    private TextView totalPayAmount;
    private TextView totalIncomeAmount;
    private TextView goodsSaleBillCount;
    private TextView goodsIncomeAmount;
    private TextView rechargeSaleBillCount;
    private TextView rechargeIncomeAmount;
    private TextView menuName;

    private TextView tvHomeHall;
    private ImageView ivHallPhone;
    private ImageView ivHomeScan;
    private RatingBar ratingBar;
    private TextView ratingBarScore;
    private TextView tvNoData;
    private SpinerPopWindow<InternetBarModel> mSpinerPopWindow;
    public List<InternetBarModel> listHall = new ArrayList<>();

    public List<ClassifyModel> listClassify = new ArrayList<>();
    public ClassifyAdapter classifyAdapter;
    public NoScrollGridView gvClassification;

    private PullToRefreshFrameLayout pull_content;
    private FlexibleFrameLayout ff_list;
    private LinearLayout ll_ad;
    private LinearLayout llAuthCodeList;
    public YSBCommentListView lvRatingList;
    public RatingAdapter adapter;
    public List<ReviewModel> list = new ArrayList<>();
    public int organId;
    public String phoneNum;
    public ReviewModel reviewModel;
    public String address = "";
    public int selectPop = 0;
    public List<LoginModel.OrganList> organList = new ArrayList<>();

    public HomeFragmentOld() {

    }

    public static HomeFragmentOld newInstance(String param1, String param2) {
        HomeFragmentOld fragment = new HomeFragmentOld();
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
        View view = inflater.inflate(R.layout.fragment_home_old, container, false);

        initView(view);
        setView();
        setListener();
//        getHallDetail();
        organId = MyApplication.getInstance().mPref.getInt("organId",0);
        getHomeDetail(organId);
        getReviewList(organId);
        return view;
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        adSlideBanner.stopPlay();
    }

    private void initView(View view) {
//        http://127.0.0.1:81/mojieAPP/page/ESP/espdetails.html?tabname=home&bartab=false
        titleBar = (TitleBar) view.findViewById(R.id.title_bar);
//        titleBar.setTitle("首页");
        titleBar.setLeftLayoutGone();
        pull_content = (PullToRefreshFrameLayout) view.findViewById(R.id.pull_content);
        ff_list = (FlexibleFrameLayout) view.findViewById(R.id.ff_list);
        ll_ad = (LinearLayout) view.findViewById(R.id.ll_ad);
        llAuthCodeList = (LinearLayout) view.findViewById(R.id.llAuthCodeList);
        adSlideBanner = (AdSlideBanner)view.findViewById(R.id.home_ad);
        lvRatingList = (YSBCommentListView) view.findViewById(R.id.lvRatingList);

        totalPayAmount = (TextView)view.findViewById(R.id.totalPayAmount);
        totalIncomeAmount = (TextView)view.findViewById(R.id.totalIncomeAmount);
        goodsSaleBillCount = (TextView)view.findViewById(R.id.goodsSaleBillCount);
        goodsIncomeAmount = (TextView)view.findViewById(R.id.goodsIncomeAmount);
        rechargeSaleBillCount = (TextView)view.findViewById(R.id.rechargeSaleBillCount);
        rechargeIncomeAmount = (TextView)view.findViewById(R.id.rechargeIncomeAmount);
        menuName = (TextView)view.findViewById(R.id.menuName);

        tvCommentNum = (TextView)view.findViewById(R.id.tvCommentNum);
        ivHallPhone = (ImageView) view.findViewById(R.id.ivHallPhone);
        ivHomeScan = (ImageView) view.findViewById(R.id.ivHomeScan);
        tvHomeHall = (TextView)view.findViewById(R.id.tvHomeHall);
        ratingBar = (RatingBar) view.findViewById(R.id.ratingBar);
        ratingBarScore = (TextView)view.findViewById(R.id.ratingBarScore);
        tvNoData = (TextView)view.findViewById(R.id.tvNoData);
        gvClassification = (NoScrollGridView) view.findViewById(R.id.gvClassification);

        LinearLayout.LayoutParams slideShowViewParam=(LinearLayout.LayoutParams)adSlideBanner.getLayoutParams();
        int screenWidth = getContext().getResources().getDisplayMetrics().widthPixels;
        slideShowViewParam.width= screenWidth;
        slideShowViewParam.height=(screenWidth*280)/640;
        adSlideBanner.setLayoutParams(slideShowViewParam);

    }

    private void setView(){
        setPullAndFlexListener();
//        Integer[] norValue = {R.drawable.discovery01, R.drawable.discovery02, R.drawable.discovery03, R.drawable.discovery04};
//        String[] name = { "点餐", "充值", "点赞","其它"};
//        for(int i=0;i<4;i++){
//            ClassifyModel classifyModel = new ClassifyModel();
//            classifyModel.name = name[i];
//            classifyModel.id = i+1;
//            classifyModel.imageId = norValue[i];
//            listClassify.add(classifyModel);
//        }

        classifyAdapter = new ClassifyAdapter(getContext(),listClassify);
        gvClassification.setAdapter(classifyAdapter);

        tvHomeHall.setText(MyApplication.getInstance().mPref.getString("organName",""));

        mSpinerPopWindow = new SpinerPopWindow<InternetBarModel>(getActivity(), listHall,itemClickListener);

        adapter = new RatingAdapter(getContext(),list,lvRatingList);
        lvRatingList.setAdapter(adapter);
        lvRatingList.setHaveMoreData(false);
//        lvRatingList.setEmptyTips("暂无评论");

        adapter.setOnClickListening(new RatingAdapter.OnClickListening() {
            @Override
            public void ClickListening(int i, ReviewModel comment) {
                reviewModel = comment;
            }
        });


//        if(organId==0){
//            getHallList();
//        }else{
//            selectPop = 1;
//            getReviewList(organId);
//            getHallList();
//        }
//        getAuthCodeList();
//        getSchemeList();
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
                MainActivity mainActivity = (MainActivity) getContext();
                mainActivity .hideInputLayout();
            }

        });

        pull_content.setOnPullToRefreshListener(new PullToRefreshFrameLayout.OnPullToRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });

        lvRatingList.setOnScrollListener(new AbsListView.OnScrollListener() {
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

                MainActivity mainActivity = (MainActivity) getContext();
                mainActivity .hideInputLayout();

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

        lvRatingList.setOnPageListener(new IPageList.OnPageListener() {
            @Override
            public void onLoadMoreItems() {
//                getPlaceCommentList();
//                getReviewList(organId);
                lvRatingList.finishLoading(true);
            }
        });

    }

    private void setListener(){

        ivHallPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!StringHelper.isEmpty(phoneNum)) {
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNum));
                    startActivity(intent);
                }
            }
        });

        /* 广告页点击事件 */
        adSlideBanner.setOnPageClickListener(new AdSlideBanner.OnPageClickListener() {
            @Override
            public void OnPageClick(View view, DBModel_SlideBanner dbModel_slideBanner) {
                WebViewManager.enterWebView(getActivity(),dbModel_slideBanner.hrefUrl,false);
            }
        });

//        tvHallAddress.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), MapActivity.class);
//                intent.putExtra("address",address);
//                startActivity(intent);
//            }
//        });

        ivHomeScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScanPayManager.enterCaptureActivity(getContext(),1);
            }
        });

        tvHomeHall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSpinerPopWindow.setWidth(titleBar.getWidth());
                mSpinerPopWindow.showAsDropDown(tvHomeHall);
                setTextImage(R.drawable.icon_up);
            }
        });

        mSpinerPopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setTextImage(R.drawable.icon_down);
            }
        });

        gvClassification.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                if(classifyAdapter.getItem(position).id==4){
//                    WebViewManager.enterWebView(getContext(),classifyAdapter.getItem(position).url,false);
//                }else {
                if(classifyAdapter.getItem(position).code.equals("ATTEND_CARD")){
                    Utills.showShortToast("暂未开放");
                    return;
                }
                try {
                    Intent intent = new Intent(getContext(), SelectTypeActivity.class);
                    intent.putExtra("classifyModel", classifyAdapter.getItem(position));
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
//                }
            }
        });

    }

    public void refresh(){
//        organId = MyApplication.getInstance().mPref.getInt("organId",0);
        getHomeDetail(organId);
        getReviewList(organId);
    }

    private void getHomeDetail(int organId){
        HallWebHelper.getHomeDetail(organId, new IModelResultListener<HomeModel>() {
            @Override
            public boolean onGetResultModel(HttpResultModel resultModel) {
                pull_content.endRefresh(true);
                Utills.showShortToast("网络异常");
                return false;
            }

            @Override
            public void onSuccess(String resultCode, HomeModel resultModel, List<HomeModel> resultModelList, String resultMsg, String hint) {

                try {
//                    Log.e("----hall",""+resultCode);
                    pull_content.endRefresh(true);
                    if(resultCode.equals("0")){
                        totalPayAmount.setText(DecimalUtil.FormatMoney(resultModel.dayReport.totalPayAmount));
                        totalIncomeAmount.setText(DecimalUtil.FormatMoney(resultModel.dayReport.totalIncomeAmount));
                        goodsSaleBillCount.setText(""+resultModel.dayReport.goodsSaleBillCount);
                        goodsIncomeAmount.setText(DecimalUtil.FormatMoney(resultModel.dayReport.goodsIncomeAmount));
                        rechargeSaleBillCount.setText(""+resultModel.dayReport.rechargeSaleBillCount);
                        rechargeIncomeAmount.setText(DecimalUtil.FormatMoney(resultModel.dayReport.rechargeIncomeAmount));
                        if(resultModel.menuList.size()>0){
                            classifyAdapter.list.clear();
                            llAuthCodeList.setVisibility(View.VISIBLE);
                            for(int i=0;i<resultModel.menuList.size();i++){
                                if(resultModel.menuList.get(i).name.contains("日常应用")){
                                    menuName.setText(resultModel.menuList.get(i).name);
                                    if(resultModel.menuList.get(i).operationList.size()>0) {
                                        classifyAdapter.list.addAll(resultModel.menuList.get(i).operationList);
                                        classifyAdapter.notifyDataSetChanged();
                                    }
                                }
                            }
                        }
                    }else if(resultCode.equals("90006") || resultCode.equals("90024")|| resultCode.equals("90027")){
                        SharedPreferences.Editor editor = MyApplication.getInstance().mPref.edit();
                        editor.putString("phone","");
                        editor.putString("password","");
                        editor.putBoolean("isLogin",false);
                        editor.putInt("memberId",0);
                        editor.putInt("status",0);
                        editor.putInt("cardNo",0);
                        editor.commit();
                        Intent intent = new Intent(getActivity(),LoginActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                    }else{
                        Utills.showShortToast(resultMsg);
                        llAuthCodeList.setVisibility(View.GONE);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(String resultCode, String resultMsg, String hint) {
                pull_content.endRefresh(true);
                Utills.showShortToast("网络异常");
            }

            @Override
            public void onError(String errorMsg) {
                pull_content.endRefresh(true);
                Utills.showShortToast("网络异常");
            }
        });
    }

    public void getReviewList(int stgId){
        HallWebHelper.getReviewList(stgId, new IModelResultListener<ReviewListModel>() {
            @Override
            public boolean onGetResultModel(HttpResultModel resultModel) {
                pull_content.endRefresh(true);
                lvRatingList.finishLoading(true);
                return false;
            }

            @Override
            public void onSuccess(String resultCode, ReviewListModel resultModel, List<ReviewListModel> resultModelList, String resultMsg, String hint) {

                try {
                    pull_content.endRefresh(true);
                    lvRatingList.finishLoading(true);
                    if(resultModel.list.size()>0){
                        tvCommentNum.setText("(" + resultModel.list.size() +")");
                        adapter.list = resultModel.list;
                        adapter.setIndex();
                        adapter.notifyDataSetChanged();
                        tvNoData.setVisibility(View.GONE);
                    }else{
                        adapter.list.clear();
                        adapter.notifyDataSetChanged();
                        tvNoData.setVisibility(View.VISIBLE);
                        tvCommentNum.setText("(0)");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(String resultCode, String resultMsg, String hint) {
                pull_content.endRefresh(true);
                lvRatingList.finishLoading(true);
            }

            @Override
            public void onError(String errorMsg) {
                pull_content.endRefresh(true);
                lvRatingList.finishLoading(true);
            }
        });
    }

    public void setAdData(List<GameHall.HallImages> images){
        dbModel_slideBanners.clear();
        for(int i=0;i<images.size();i++){
            DBModel_SlideBanner slideBanner = new DBModel_SlideBanner();
            slideBanner.id = images.get(i).id;
            slideBanner.stgId = images.get(i).stgId;
            slideBanner.imageUrl = images.get(i).imageUrl;
            dbModel_slideBanners.add(slideBanner);
        }
        adSlideBanner.setData(dbModel_slideBanners);
        adSlideBanner.setVisibility(View.GONE);
        adSlideBanner.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.advertising_enter));

    }

    /**
     * 给TextView右边设置图片
     * @param resId
     */
    private void setTextImage(int resId) {
        Drawable drawable = getResources().getDrawable(resId);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(),drawable.getMinimumHeight());// 必须设置图片大小，否则不显示
        tvHomeHall.setCompoundDrawables(null, null, drawable, null);
    }

    /**
     * popupwindow显示的ListView的item点击事件
     */
    private AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
            InternetBarModel internetBarModel = (InternetBarModel)mSpinerPopWindow.list.get(position);
            if(organId != internetBarModel.id) {
                tvHomeHall.setText(internetBarModel.name);
                organId = internetBarModel.id;
                SharedPreferences.Editor editor = MyApplication.getInstance().mPref.edit();
                editor.putInt("organId", organId);
                editor.commit();
                refresh();
                ACache mCache = ACache.get(getContext());
                mCache.clear();
                MyApplication.getInstance().getCarListData().clear();
            }
            mSpinerPopWindow.dismiss();
        }
    };


}
