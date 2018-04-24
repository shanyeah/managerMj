package com.imovie.mogic.home.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.imovie.mogic.R;
import com.imovie.mogic.card.model.InternetBarModel;
import com.imovie.mogic.myRank.MinePraiseActivity;
import com.imovie.mogic.myRank.MineRankActivity;
import com.imovie.mogic.home.adater.GameHallAdapter;
import com.imovie.mogic.home.model.GameHall;
import com.imovie.mogic.home.model.MyQrCodeModel;
import com.imovie.mogic.home.net.HomeWebHelper;
import com.imovie.mogic.login.model.MyDataModel;
import com.imovie.mogic.login.model.TestModel;
import com.imovie.mogic.utills.QRCodeUtil;
import com.imovie.mogic.utills.Utills;
import com.imovie.mogic.web.IModelResultListener;
import com.imovie.mogic.web.model.HttpResultModel;
import com.imovie.mogic.widget.FlexibleFrameLayout;
import com.imovie.mogic.widget.PullToRefreshFrameLayout;
import com.imovie.mogic.widget.YSBPageListView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

public class PraiseFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private PullToRefreshFrameLayout pull_content;
    private FlexibleFrameLayout ff_list;

    public RelativeLayout rlChargePraise;
    public RelativeLayout rlChargeState;
    public RelativeLayout rlChargeRanking;
    public TextView tvNickname;
    public TextView tvHallArea;
    public ImageView ivChargeHeader;
    public ImageView ivChargeScan;
    public YSBPageListView lvGameHall;
    public ArrayList<GameHall> listHall;
    public GameHallAdapter hallAdapter;

    private List<InternetBarModel> list = new ArrayList<>();

    private String cityId = "";
    private int orderType = 0;
    private DisplayImageOptions mOption;

    public PraiseFragment() {

    }

    public static PraiseFragment newInstance(String param1, String param2) {
        PraiseFragment fragment = new PraiseFragment();
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

        mOption = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.person_default_profile)
                .showImageOnFail(R.drawable.person_default_profile)
                .showImageForEmptyUri(R.drawable.person_default_profile)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.home_praise_fragment, container, false);
        initView(v);
        setView();
        setListener();
        return v;
    }
    private void initView(View view) {

        rlChargePraise = (RelativeLayout) view.findViewById(R.id.rlChargePraise);
        rlChargeState = (RelativeLayout) view.findViewById(R.id.rlChargeState);
        rlChargeRanking = (RelativeLayout) view.findViewById(R.id.rlChargeRanking);
        tvNickname = (TextView) view.findViewById(R.id.tvNickname);
//        tvHallOrder = (TextView) view.findViewById(R.id.tvHallOrder);
        ivChargeHeader = (ImageView) view.findViewById(R.id.ivChargeHeader);
        ivChargeScan = (ImageView) view.findViewById(R.id.ivChargeScan);
        pull_content = (PullToRefreshFrameLayout) view.findViewById(R.id.pull_content);
        ff_list = (FlexibleFrameLayout) view.findViewById(R.id.ff_list);
        lvGameHall = (YSBPageListView) view.findViewById(R.id.lv_game_hall);
//        mSpinerPopWindow = new SpinerPopWindow<InternetBarModel>(getActivity(), list,itemClickListener);


//        LinearLayout.LayoutParams slideShowViewParam=(LinearLayout.LayoutParams)adSlideBanner.getLayoutParams();
//        slideShowViewParam.width= AppConfig.getScreenWidth();
//        slideShowViewParam.height=(AppConfig.getScreenWidth()*244)/640;
//        adSlideBanner.setLayoutParams(slideShowViewParam);

    }

    private void setView() {
        setPullAndFlexListener();
        listHall = new ArrayList<>();
        hallAdapter = new GameHallAdapter(getContext(),listHall);
        lvGameHall.setAdapter(hallAdapter);
        lvGameHall.setPageSize(10);
//        getHallList(0,"");
        getMyData();
        getMyQrCode();
//        getCityList();
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
//                getHallList(0,"");
                getMyData();
                getMyQrCode();
            }
        });

    }

    private void setListener() {
        rlChargePraise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MinePraiseActivity.class);
                intent.putExtra("stgId",21);
                startActivity(intent);
            }
        });
        rlChargeState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MinePraiseActivity.class);
                intent.putExtra("stgId",21);
                startActivity(intent);
            }
        });

        rlChargeRanking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MineRankActivity.class);
                intent.putExtra("stgId",21);
                startActivity(intent);
            }
        });

    }

    /**
     * 给TextView右边设置图片
     * @param resId
     */
    private void setTextImage(int resId) {
        Drawable drawable = getResources().getDrawable(resId);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(),drawable.getMinimumHeight());// 必须设置图片大小，否则不显示
        tvHallArea.setCompoundDrawables(null, null, drawable, null);
    }

    /**
     * popupwindow显示的ListView的item点击事件
     */
    private AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
//            InternetBarModel internetBarModel = (InternetBarModel)mSpinerPopWindow.list.get(position);
//            tvHallArea.setText(internetBarModel.name);
//            mSpinerPopWindow.dismiss();
//            cityId = "" + internetBarModel.id;
//            if(cityId.equals("0")) cityId="";
//            getHallList(orderType,cityId);
//            Toast.makeText(MovieSelectActivity.this, "点击了:" + internetBarModel.id,Toast.LENGTH_SHORT).show();
        }
    };




    public void getCityList(){
        HomeWebHelper.getCityList(new IModelResultListener<InternetBarModel>() {
            @Override
            public boolean onGetResultModel(HttpResultModel resultModel) {
                return false;
            }

            @Override
            public void onSuccess(String resultCode, InternetBarModel resultModel, List<InternetBarModel> resultModelList, String resultMsg, String hint) {
//                Log.e("----city:",""+resultCode);
                if(resultModelList.size()>0){
//                    mSpinerPopWindow.refreshData(resultModelList);
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

    public void getMyData(){
        HomeWebHelper.getMy(new IModelResultListener<MyDataModel>() {
            @Override
            public boolean onGetResultModel(HttpResultModel resultModel) {
                pull_content.endRefresh(true);
                return false;
            }

            @Override
            public void onSuccess(String resultCode, MyDataModel resultModel, List<MyDataModel> resultModelList, String resultMsg, String hint) {
                pull_content.endRefresh(true);
                if(resultCode.equals("0")) {
                    tvNickname.setText(resultModel.nickName);
//                member.setText(loginModel.card.cardCategoryName);
//                tvTodayIntegration.setText(DecimalUtil.FormatMoney(loginModel.card.cashBalance));
//                tvBalance.setText(DecimalUtil.FormatMoney(loginModel.card.exchangeBalance));
//                tvBean.setText(DecimalUtil.FormatMoney(loginModel.card.presentBalance));
//                tvTicket.setText(DecimalUtil.FormatMoney(loginModel.card.point));
                    ImageLoader.getInstance().displayImage(resultModel.fackeImageUrl,ivChargeHeader,mOption);
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

    public void getMyQrCode(){
        HomeWebHelper.getMyQrCode(new IModelResultListener<TestModel>() {
            @Override
            public boolean onGetResultModel(HttpResultModel resultModel) {
                pull_content.endRefresh(true);
                return false;
            }

            @Override
            public void onSuccess(String resultCode, TestModel resultModel, List<TestModel> resultModelList, String resultMsg, String hint) {
                pull_content.endRefresh(true);
                MyQrCodeModel qrCodeModel = new MyQrCodeModel();
                qrCodeModel.setModelByJson(resultCode);
                if(qrCodeModel.code.equals("0")){
                    try {
//                        tvCodeNumber.setText(""+qrCodeModel.payCode);
                        Bitmap bitmap = QRCodeUtil.createQRImage(qrCodeModel.data,400,400,null);
                        ivChargeScan.setImageBitmap(bitmap);
//                        Bitmap bm = QRCodeUtil.createImage2(qrCodeModel.payCode, 1600, 400);
//                        ivOneQrCode.setImageBitmap(bm);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }else{
                    Utills.showShortToast("获取不到二维码");
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

}
