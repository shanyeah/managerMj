package com.imovie.mogic.home.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.imovie.mogic.R;
import com.imovie.mogic.ScanPay.manager.ScanPayManager;
import com.imovie.mogic.card.model.PresentModel;
import com.imovie.mogic.card.net.CardWebHelper;
import com.imovie.mogic.home.model.MyQrCodeModel;
import com.imovie.mogic.home.model.SearchModel;
import com.imovie.mogic.home.model.SearchUserModel;
import com.imovie.mogic.home.net.HomeWebHelper;
import com.imovie.mogic.home.widget.SearchUserPopWindow;
import com.imovie.mogic.login.model.MyDataModel;
import com.imovie.mogic.login.model.TestModel;
import com.imovie.mogic.myRank.MinePraiseActivity;
import com.imovie.mogic.myRank.MineRankActivity;
import com.imovie.mogic.utills.DecimalUtil;
import com.imovie.mogic.utills.StringHelper;
import com.imovie.mogic.utills.Utills;
import com.imovie.mogic.web.IModelResultListener;
import com.imovie.mogic.web.model.HttpResultModel;
import com.imovie.mogic.widget.FlexibleFrameLayout;
import com.imovie.mogic.widget.PullToRefreshFrameLayout;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

public class ChargeFragment extends Fragment {
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
    public TextView tvPresentAmount;
    public ImageView ivChargeHeader;
    public LinearLayout llRightOne;
    public LinearLayout llRightTwo;
    public LinearLayout llRightThree;
    public LinearLayout llRightFour;
    public LinearLayout llUserNumState;
    public EditText etChargeNum;
    public EditText etUserId;
    public TextView tvUserName;
    public Button btChargeCard;
    public Button btChargeSelect;
    public SearchUserModel internetBarModel = new SearchUserModel();
    public String chargeNum = "";

    private SearchUserPopWindow<SearchUserModel> mSpinerPopWindow;
    private List<SearchUserModel> list = new ArrayList<>();

    private int userId = -1;
    private int orderType = 0;
    private DisplayImageOptions mOption;

    public ChargeFragment() {

    }

    public static ChargeFragment newInstance(String param1, String param2) {
        ChargeFragment fragment = new ChargeFragment();
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
        View v=inflater.inflate(R.layout.home_charge_fragment, container, false);
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
        ivChargeHeader = (ImageView) view.findViewById(R.id.ivChargeHeader);
        pull_content = (PullToRefreshFrameLayout) view.findViewById(R.id.pull_content);
        ff_list = (FlexibleFrameLayout) view.findViewById(R.id.ff_list);
        llRightOne = (LinearLayout) view.findViewById(R.id.llRightOne);
        llRightTwo = (LinearLayout) view.findViewById(R.id.llRightTwo);
        llRightThree = (LinearLayout) view.findViewById(R.id.llRightThree);
        llRightFour = (LinearLayout) view.findViewById(R.id.llRightFour);
        llUserNumState = (LinearLayout) view.findViewById(R.id.llUserNumState);
        etUserId = (EditText) view.findViewById(R.id.etUserId);
        tvUserName = (TextView) view.findViewById(R.id.tvUserName);
        etChargeNum = (EditText) view.findViewById(R.id.etChargeNum);
        tvPresentAmount = (TextView) view.findViewById(R.id.tvPresentAmount);
        btChargeCard = (Button) view.findViewById(R.id.btChargeCard);
        btChargeSelect = (Button) view.findViewById(R.id.btChargeSelect);

        mSpinerPopWindow = new SearchUserPopWindow<SearchUserModel>(getActivity(), list,itemClickListener);

//        LinearLayout.LayoutParams slideShowViewParam=(LinearLayout.LayoutParams)adSlideBanner.getLayoutParams();
//        slideShowViewParam.width= AppConfig.getScreenWidth();
//        slideShowViewParam.height=(AppConfig.getScreenWidth()*244)/640;
//        adSlideBanner.setLayoutParams(slideShowViewParam);

    }

    private void setView() {
        setPullAndFlexListener();
//        listHall = new ArrayList<>();
//        hallAdapter = new GameHallAdapter(getContext(),listHall);
//        lvGameHall.setAdapter(hallAdapter);
//        lvGameHall.setPageSize(10);
        getMyData();
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
                getMyData();
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
        etUserId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
//                String user = editable.toString();
//                if(!StringHelper.isEmail(user)) {
//                    getCheckUserInfo(user);
//                }
            }
        });
        btChargeSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = etUserId.getText().toString();
                if(StringHelper.isEmail(user)) {
                    Utills.showShortToast("请输入充值用户");
                   return;
                }
                getCheckUserInfo(user);
            }
        });

        etChargeNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                llRightOne.setBackground(getContext().getResources().getDrawable(R.drawable.shape_write_r5_l5));
                llRightTwo.setBackground(getContext().getResources().getDrawable(R.drawable.shape_write_r5_l5));
                llRightThree.setBackground(getContext().getResources().getDrawable(R.drawable.shape_write_r5_l5));
                llRightFour.setBackground(getContext().getResources().getDrawable(R.drawable.shape_write_r5_l5));
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                chargeNum = editable.toString();
                if(!StringHelper.isEmpty(chargeNum))getPresentList(chargeNum+"00",userId);
            }
        });

        llRightOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etChargeNum.setText("");
                chargeNum = "50";
                llRightOne.setBackground(getContext().getResources().getDrawable(R.drawable.shape_bg5_r5_l5));
                llRightTwo.setBackground(getContext().getResources().getDrawable(R.drawable.shape_write_r5_l5));
                llRightThree.setBackground(getContext().getResources().getDrawable(R.drawable.shape_write_r5_l5));
                llRightFour.setBackground(getContext().getResources().getDrawable(R.drawable.shape_write_r5_l5));
                if(userId != -1)getPresentList(chargeNum+"00",userId);
            }
        });
        llRightTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etChargeNum.setText("");
                chargeNum = "100";
                llRightOne.setBackground(getContext().getResources().getDrawable(R.drawable.shape_write_r5_l5));
                llRightTwo.setBackground(getContext().getResources().getDrawable(R.drawable.shape_bg5_r5_l5));
                llRightThree.setBackground(getContext().getResources().getDrawable(R.drawable.shape_write_r5_l5));
                llRightFour.setBackground(getContext().getResources().getDrawable(R.drawable.shape_write_r5_l5));
                if(userId != -1)getPresentList(chargeNum+"00",userId);
            }
        });
        llRightThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etChargeNum.setText("");
                chargeNum = "200";
                llRightOne.setBackground(getContext().getResources().getDrawable(R.drawable.shape_write_r5_l5));
                llRightTwo.setBackground(getContext().getResources().getDrawable(R.drawable.shape_write_r5_l5));
                llRightThree.setBackground(getContext().getResources().getDrawable(R.drawable.shape_bg5_r5_l5));
                llRightFour.setBackground(getContext().getResources().getDrawable(R.drawable.shape_write_r5_l5));
                if(userId != -1)getPresentList(chargeNum+"00",userId);
            }
        });
        llRightFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etChargeNum.setText("");
                chargeNum = "500";
                llRightOne.setBackground(getContext().getResources().getDrawable(R.drawable.shape_write_r5_l5));
                llRightTwo.setBackground(getContext().getResources().getDrawable(R.drawable.shape_write_r5_l5));
                llRightThree.setBackground(getContext().getResources().getDrawable(R.drawable.shape_write_r5_l5));
                llRightFour.setBackground(getContext().getResources().getDrawable(R.drawable.shape_bg5_r5_l5));
                if(userId != -1)getPresentList(chargeNum+"00",userId);
            }
        });

        btChargeCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userId == -1) {
                    Utills.showShortToast("请选择充值用户");
                    return;
                }
//                String chargeFee = etChargeNum.getText().toString();
                if(StringHelper.isEmail(chargeNum)) {
                    Utills.showShortToast("请选择充值金额");
                    return;
                }
                internetBarModel.chargeNum = chargeNum;
                getPreqrCharge(chargeNum+"00",userId);
            }
        });

    }

//    /**
//     * 给TextView右边设置图片
//     * @param resId
//     */
//    private void setTextImage(int resId) {
//        Drawable drawable = getResources().getDrawable(resId);
//        drawable.setBounds(0, 0, drawable.getMinimumWidth(),drawable.getMinimumHeight());// 必须设置图片大小，否则不显示
//        tvHallArea.setCompoundDrawables(null, null, drawable, null);
//    }

    /**
     * popupwindow显示的ListView的item点击事件
     */
    private AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
            internetBarModel = (SearchUserModel)mSpinerPopWindow.list.get(position);
            tvUserName.setText(internetBarModel.name + "(" + internetBarModel.idNumber + ")");
            userId = internetBarModel.userId;
            mSpinerPopWindow.dismiss();
            if(!StringHelper.isEmpty(chargeNum)) getPresentList(chargeNum+"00",userId);
//            cityId = "" + internetBarModel.userId;
//            if(cityId.equals("0")) cityId="";
//            Toast.makeText(MovieSelectActivity.this, "点击了:" + internetBarModel.id,Toast.LENGTH_SHORT).show();
        }
    };

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

    public void getCheckUserInfo(String input){
        HomeWebHelper.getCheckUserInfo(input,new IModelResultListener<SearchModel>() {
            @Override
            public boolean onGetResultModel(HttpResultModel resultModel) {
                return false;
            }

            @Override
            public void onSuccess(String resultCode, SearchModel resultModel, List<SearchModel> resultModelList, String resultMsg, String hint) {
//                Log.e("----city:",""+resultCode);
                if(resultModelList.size()>0){
//                    closeSoftKeyboard(getContext());
                    closeSoftKeyboard(getActivity());
                    mSpinerPopWindow.setWidth(llUserNumState.getWidth());
                    mSpinerPopWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
                    mSpinerPopWindow.showAsDropDown(llUserNumState);
                    mSpinerPopWindow.refreshData(resultModel.list);

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

    public void getPresentList(String chargeFee,int userId){
        CardWebHelper.getPresentList(chargeFee,userId , new IModelResultListener<PresentModel>() {
            @Override
            public boolean onGetResultModel(HttpResultModel resultModel) {
                return false;
            }

            @Override
            public void onSuccess(String resultCode, PresentModel resultModel, List<PresentModel> resultModelList, String resultMsg, String hint) {
//                Log.e("-----getPresentList",""+resultCode);
//                loginModel.setModelByJson(resultCode);
                if(resultModelList.size()>0) {
                    tvPresentAmount.setText("(充" + DecimalUtil.FormatMoney(resultModelList.get(0).chargeAmount/100) + "元,送"+ DecimalUtil.FormatMoney(resultModelList.get(0).presentAmount/100) + "元)");

//                    for(int i=0;i<resultModelList.size();i++) {
//                        switch (i){
//                            case 0:
//                                tvRightOne.setText(resultModelList.get(0).presentAmount);
//                                break;
//                            case 1:
//                                tvRightTwo.setText(resultModelList.get(0).presentAmount);
//                                break;
//                            case 2:
//                                tvRightThree.setText(resultModelList.get(2).presentAmount);
//                                break;
//                            case 3:
//                                tvRightFour.setText(resultModelList.get(3).presentAmount);
//                                break;
//                            case 4:
//                                tv_right_Five.setText(resultModelList.get(4).presentAmount);
//                                break;
//                            case 5:
//                                tv_right_six.setText(resultModelList.get(5).presentAmount);
//                                break;
//                            case 6:
//                                tv_right_seven.setText(resultModelList.get(6).presentAmount);
//                                break;
//                        }
//                    }


                }
//                else{
//                    Toast.makeText(MemberChargeActivity.this,"网络错误",Toast.LENGTH_SHORT).show();
//                }
            }

            @Override
            public void onFail(String resultCode, String resultMsg, String hint) {
//                lvCard.finishLoading(true);
            }

            @Override
            public void onError(String errorMsg) {
//                lvCard.finishLoading(true);
            }
        });
    }

    public void getPreqrCharge(String chargeFee,int userId1){
        CardWebHelper.getPreqrCharge(chargeFee,userId1 , new IModelResultListener<TestModel>() {
            @Override
            public boolean onGetResultModel(HttpResultModel resultModel) {
                return false;
            }

            @Override
            public void onSuccess(String resultCode, TestModel resultModel, List<TestModel> resultModelList, String resultMsg, String hint) {
//                Log.e("-----getPresentList",""+resultCode);
                MyQrCodeModel qrCodeModel = new MyQrCodeModel();
                qrCodeModel.setModelByJson(resultCode);
                if(qrCodeModel.code.equals("0")){
//                    Utills.showShortToast(""+qrCodeModel.data);
                    try {
                        ScanPayManager.enterCaptureActivity(getContext(),""+qrCodeModel.data,internetBarModel);
                        tvUserName.setText("");
                        userId = -1;
                        etUserId.setText("");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }else{
                    Utills.showShortToast("下单失败");
                }
            }

            @Override
            public void onFail(String resultCode, String resultMsg, String hint) {
//                lvCard.finishLoading(true);
            }

            @Override
            public void onError(String errorMsg) {
//                lvCard.finishLoading(true);
            }
        });
    }


    private void closeSoftKeyboard(Context context) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null && ((Activity) context).getCurrentFocus() != null) {
            inputMethodManager.hideSoftInputFromWindow(((Activity) context).getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

}
