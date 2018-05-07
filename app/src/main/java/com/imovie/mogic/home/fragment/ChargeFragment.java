package com.imovie.mogic.home.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
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
import com.imovie.mogic.ScanPay.zxing.activity.CaptureActivity;
import com.imovie.mogic.car.CarPayActivity;
import com.imovie.mogic.card.model.PresentModel;
import com.imovie.mogic.card.net.CardWebHelper;
import com.imovie.mogic.home.BaseActivity;
import com.imovie.mogic.home.SearchMemberActivity;
import com.imovie.mogic.home.SelectTypeActivity;
import com.imovie.mogic.home.adater.ChargeAdapter;
import com.imovie.mogic.home.adater.ClassifyAdapter;
import com.imovie.mogic.home.model.ChargeListModel;
import com.imovie.mogic.home.model.ChargeSuccessModel;
import com.imovie.mogic.home.model.ClassifyModel;
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
import com.imovie.mogic.widget.NoScrollGridView;
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

    private LinearLayout llCarPayMember;
    private TextView tvName;
    private TextView tvNumber;
    private TextView tvBalance;
    private TextView tvPresentBalance;
    private TextView tvPhone;
    private RelativeLayout rlNoUserData;
    private RelativeLayout rlUserData;

    private TextView tvPresentAmount;

//    public LinearLayout llRightOne;
//    public LinearLayout llRightTwo;
//    public LinearLayout llRightThree;
//    public LinearLayout llRightFour;
//    public LinearLayout llUserNumState;
    public EditText etChargeNum;
    public Button btChargeCard;
    public Button btChargeSelect;
    public SearchUserModel internetBarModel = new SearchUserModel();
    public String chargeNum;
    public boolean unSelect = true;
    public NoScrollGridView gvChargeList;
    public List<ChargeListModel> listCharge = new ArrayList<>();
    public ChargeAdapter chargeAdapter;


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
        llCarPayMember = (LinearLayout) view.findViewById(R.id.llCarPayMember);
        tvName=(TextView) view.findViewById(R.id.tv_name);
        tvNumber=(TextView) view.findViewById(R.id.tvNumber);
        tvBalance = (TextView) view.findViewById(R.id.tvBalance);
        tvPresentBalance = (TextView) view.findViewById(R.id.tvPresentBalance);
        tvPhone=(TextView) view.findViewById(R.id.tvPhone);
        rlNoUserData = (RelativeLayout) view.findViewById(R.id.rlNoUserData);
        rlUserData = (RelativeLayout) view.findViewById(R.id.rlUserData);

        pull_content = (PullToRefreshFrameLayout) view.findViewById(R.id.pull_content);
        ff_list = (FlexibleFrameLayout) view.findViewById(R.id.ff_list);
        gvChargeList = (NoScrollGridView) view.findViewById(R.id.gvChargeList);


        etChargeNum = (EditText) view.findViewById(R.id.etChargeNum);

        btChargeCard = (Button) view.findViewById(R.id.btChargeCard);
        btChargeSelect = (Button) view.findViewById(R.id.btChargeSelect);
        tvPresentAmount = (TextView) view.findViewById(R.id.tvPresentAmount);


    }

    private void setView() {
        setPullAndFlexListener();
        for(int i=0;i<6;i++){
            ChargeListModel model = new ChargeListModel();
            switch (i){
                case 0:
                    model.chargeAmount = 50;
                    model.presentAmount = 5;
                    model.present = 5;
                    break;
                case 1:
                    model.chargeAmount = 100;
                    model.presentAmount = 20;
                    model.present = 20;
                    break;
                case 2:
                    model.chargeAmount = 200;
                    model.presentAmount = 50;
                    model.present = 50;
                    break;
                case 3:
                    model.chargeAmount = 500;
                    model.presentAmount = 100;
                    model.present = 100;
                    break;
                case 4:
                    model.chargeAmount = 1000;
                    model.presentAmount = 200;
                    model.present = 200;
                    break;
                case 5:
                    model.chargeAmount = 2000;
                    model.presentAmount = 500;
                    model.present = 500;
                    break;
            }

            listCharge.add(model);
        }
        chargeAdapter = new ChargeAdapter(getActivity(),listCharge);
        gvChargeList.setAdapter(chargeAdapter);
//        listHall = new ArrayList<>();
//        hallAdapter = new GameHallAdapter(getContext(),listHall);
//        lvGameHall.setAdapter(hallAdapter);
//        lvGameHall.setPageSize(10);
//        getMyData();
//        getCityList();
        getPresentList("",-1);
    }

    public void setUserData(SearchUserModel userModel){
        unSelect = false;
        userId = userModel.userId;
        rlUserData.setVisibility(View.VISIBLE);
        rlNoUserData.setVisibility(View.GONE);
        tvName.setText(""+userModel.name);
        tvNumber.setText("证件号：" + userModel.idNumber);
        tvPhone.setText("" + userModel.mobile);
        tvBalance.setText(Html.fromHtml("<font color='#565a5c' size=14>余额:</font><font color=\'#fd5c02\' size=14>"+ DecimalUtil.FormatMoney(userModel.balance) +"</font><font color=\'#565a5c\' size=14>"+getResources().getString(R.string.symbol_RMB)+"</font>"));
        tvPresentBalance.setText(Html.fromHtml("<font color='#565a5c' size=14>赠送:</font><font color=\'#fd5c02\' size=14>"+ DecimalUtil.FormatMoney(userModel.presentBalance) +"</font><font color=\'#565a5c\' size=14>"+getResources().getString(R.string.symbol_RMB)+"</font>"));

    }

    public void setNoUserData(){
        if(unSelect){
            rlUserData.setVisibility(View.GONE);
            rlNoUserData.setVisibility(View.VISIBLE);
        }

    }

    private void setPullAndFlexListener(){
//        ff_list.setFlexView(ll_ad);
//        ff_list.setFlexible(true);
        pull_content.setPullEnable(false);

        ff_list.setOnFlexChangeListener(new FlexibleFrameLayout.OnFlexChangeListener() {
            @Override
            public void onFlexChange(int flexHeight, int currentFlexHeight, boolean isOnTop) {
                if (isOnTop) {
                    pull_content.setPullEnable(false);
                } else {
                    pull_content.setPullEnable(false);
                }
            }

        });
        pull_content.setOnPullToRefreshListener(new PullToRefreshFrameLayout.OnPullToRefreshListener() {
            @Override
            public void onRefresh() {
//                getMyData();
            }
        });

    }

    private void setListener() {
        llCarPayMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),SearchMemberActivity.class);
                startActivityForResult(intent,SearchMemberActivity.SELECT_RESULT);
            }
        });

//        etChargeNum.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(unSelect) {
//                    Intent intent = new Intent(getActivity(), SearchMemberActivity.class);
//                    startActivityForResult(intent, SearchMemberActivity.SELECT_RESULT);
//                    return;
//                }
//            }
//        });
        etChargeNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                llRightOne.setBackground(getContext().getResources().getDrawable(R.drawable.shape_write_r5_l5));
//                llRightTwo.setBackground(getContext().getResources().getDrawable(R.drawable.shape_write_r5_l5));
//                llRightThree.setBackground(getContext().getResources().getDrawable(R.drawable.shape_write_r5_l5));
//                llRightFour.setBackground(getContext().getResources().getDrawable(R.drawable.shape_write_r5_l5));
                if(unSelect) {
                    Intent intent = new Intent(getActivity(), SearchMemberActivity.class);
                    startActivityForResult(intent, SearchMemberActivity.SELECT_RESULT);
                    return;
                }

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                chargeAdapter.setSelectIndex(-1);
                chargeNum = editable.toString();
                if(!StringHelper.isEmpty(chargeNum)){
                    getChargePresent(chargeNum+"",userId);
                }else{
                    tvPresentAmount.setVisibility(View.GONE);
                }
            }
        });

        gvChargeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    if(unSelect) {
                        Intent intent = new Intent(getActivity(), SearchMemberActivity.class);
                        startActivityForResult(intent, SearchMemberActivity.SELECT_RESULT);
                        return;
                    }
//                    Utills.showShortToast(""+position);
                    if(!StringHelper.isEmpty(etChargeNum.getText().toString()))etChargeNum.setText("");
                    chargeAdapter.setSelectIndex(position);
//                    for(int i=0;i<listCharge.size();i++){
//                        if(i == position){
//                            listCharge.get(i).isSelect = true;
//                            listCharge.get(i).presentAmount =5000;
//                        }else{
//                            listCharge.get(i).isSelect = false;
//                        }
//                    }
//                    chargeAdapter.list.clear();
//                    chargeAdapter.list=listCharge;
//                    chargeAdapter.notifyDataSetChanged();

                    chargeNum = ""+chargeAdapter.getItem(position).chargeAmount;

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

//        llRightOne.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(unSelect) {
//                    Intent intent = new Intent(getActivity(), SearchMemberActivity.class);
//                    startActivityForResult(intent, SearchMemberActivity.SELECT_RESULT);
//                    return;
//                }
//
//                etChargeNum.setText("");
//                chargeNum = "50";
//                llRightOne.setBackground(getContext().getResources().getDrawable(R.drawable.bg_line_l7_r3));
//                llRightTwo.setBackground(getContext().getResources().getDrawable(R.drawable.shape_write_r5_l5));
//                llRightThree.setBackground(getContext().getResources().getDrawable(R.drawable.shape_write_r5_l5));
//                llRightFour.setBackground(getContext().getResources().getDrawable(R.drawable.shape_write_r5_l5));
//                if(userId != -1)getPresentList(chargeNum+"00",userId);
//            }
//        });
//        llRightTwo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(unSelect) {
//                    Intent intent = new Intent(getActivity(), SearchMemberActivity.class);
//                    startActivityForResult(intent, SearchMemberActivity.SELECT_RESULT);
//                    return;
//                }
//                etChargeNum.setText("");
//                chargeNum = "100";
//                llRightOne.setBackground(getContext().getResources().getDrawable(R.drawable.shape_write_r5_l5));
//                llRightTwo.setBackground(getContext().getResources().getDrawable(R.drawable.bg_line_l7_r3));
//                llRightThree.setBackground(getContext().getResources().getDrawable(R.drawable.shape_write_r5_l5));
//                llRightFour.setBackground(getContext().getResources().getDrawable(R.drawable.shape_write_r5_l5));
//                if(userId != -1)getPresentList(chargeNum+"00",userId);
//            }
//        });
//        llRightThree.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(unSelect) {
//                    Intent intent = new Intent(getActivity(), SearchMemberActivity.class);
//                    startActivityForResult(intent, SearchMemberActivity.SELECT_RESULT);
//                    return;
//                }
//                etChargeNum.setText("");
//                chargeNum = "200";
//                llRightOne.setBackground(getContext().getResources().getDrawable(R.drawable.shape_write_r5_l5));
//                llRightTwo.setBackground(getContext().getResources().getDrawable(R.drawable.shape_write_r5_l5));
//                llRightThree.setBackground(getContext().getResources().getDrawable(R.drawable.bg_line_l7_r3));
//                llRightFour.setBackground(getContext().getResources().getDrawable(R.drawable.shape_write_r5_l5));
//                if(userId != -1)getPresentList(chargeNum+"00",userId);
//            }
//        });
//        llRightFour.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(unSelect) {
//                    Intent intent = new Intent(getActivity(), SearchMemberActivity.class);
//                    startActivityForResult(intent, SearchMemberActivity.SELECT_RESULT);
//                    return;
//                }
//                etChargeNum.setText("");
//                chargeNum = "500";
//                llRightOne.setBackground(getContext().getResources().getDrawable(R.drawable.shape_write_r5_l5));
//                llRightTwo.setBackground(getContext().getResources().getDrawable(R.drawable.shape_write_r5_l5));
//                llRightThree.setBackground(getContext().getResources().getDrawable(R.drawable.shape_write_r5_l5));
//                llRightFour.setBackground(getContext().getResources().getDrawable(R.drawable.bg_line_l7_r3));
//                if(userId != -1)getPresentList(chargeNum+"00",userId);
//            }
//        });

        btChargeCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(unSelect) {
                    Intent intent = new Intent(getActivity(), SearchMemberActivity.class);
                    startActivityForResult(intent, SearchMemberActivity.SELECT_RESULT);
                    return;
                }
//                if(userId == -1) {
//                    Utills.showShortToast("请选择充值用户");
//                    return;
//                }
//                String chargeFee = etChargeNum.getText().toString();
                if(StringHelper.isEmpty(chargeNum)) {
                    Utills.showShortToast("请选择充值金额");
                    return;
                }
//                internetBarModel.chargeNum = chargeNum;
//                ScanPayManager.enterCaptureActivity(getActivity(),(BaseActivity)getActivity());
//                getPreqrCharge(chargeNum,userId,1,0,0,"","sn");
                if (Build.VERSION.SDK_INT >= 23){
                    int checkCallPhonePermission = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA);
                    if(checkCallPhonePermission != PackageManager.PERMISSION_GRANTED){
                        ActivityCompat.requestPermissions((Activity) getActivity(),new String[]{Manifest.permission.CAMERA},222);
                        return;
                    }
                }
                Intent intent = new Intent(getActivity(),CaptureActivity.class);
                intent.putExtra("data","");
                intent.putExtra(CaptureActivity.MSG_FROM,CaptureActivity.MSG_OTHER);
               startActivityForResult(intent,CaptureActivity.MSG_OTHER);
            }
        });

    }



    public void getPresentList(String chargeFee,int userId){
        CardWebHelper.getPresentList("",userId , new IModelResultListener<ChargeListModel>() {
            @Override
            public boolean onGetResultModel(HttpResultModel resultModel) {
                return false;
            }

            @Override
            public void onSuccess(String resultCode, ChargeListModel resultModel, List<ChargeListModel> resultModelList, String resultMsg, String hint) {
//                Log.e("-----getPresentList",""+resultCode);
//                loginModel.setModelByJson(resultCode);
                if(resultCode.equals("0")) {
                    if (resultModelList.size() > 0) {
                        listCharge.clear();
                        listCharge.addAll(resultModelList);
//                        chargeAdapter.list.addAll(resultModelList);
                        chargeAdapter.notifyDataSetChanged();
                    }
                }else{

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

    public void getChargePresent(String chargeFee,int userId){
        CardWebHelper.getPresentList(chargeFee,userId , new IModelResultListener<ChargeListModel>() {
            @Override
            public boolean onGetResultModel(HttpResultModel resultModel) {
                return false;
            }

            @Override
            public void onSuccess(String resultCode, ChargeListModel resultModel, List<ChargeListModel> resultModelList, String resultMsg, String hint) {
                if(resultCode.equals("0")) {
                    if (resultModelList.size() > 0 && resultModelList.get(0).presentAmount>0) {
                        tvPresentAmount.setVisibility(View.VISIBLE);
                        tvPresentAmount.setText("(送"+ DecimalUtil.FormatMoney(resultModelList.get(0).presentAmount,"#0")+")");
                    }
                }else{
                    tvPresentAmount.setVisibility(View.GONE);
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

    public void getPreqrCharge(String amount,int userId,int payType, long payCategoryId,int type,String remark,String tn){
        CardWebHelper.getPreqrCharge(amount,userId,payType, payCategoryId,type,remark,tn , new IModelResultListener<ChargeSuccessModel>() {
            @Override
            public boolean onGetResultModel(HttpResultModel resultModel) {
                return false;
            }

            @Override
            public void onSuccess(String resultCode, ChargeSuccessModel resultModel, List<ChargeSuccessModel> resultModelList, String resultMsg, String hint) {
                if(resultCode.equals("0")) {
//                    Utills.showShortToast(resultMsg);
                    ScanPayManager.enterChargeSuccessActivity(getActivity(), "", resultModel);
                }else if(resultCode.equals("90016")){

                }else{
                    Utills.showShortToast(resultMsg);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        Utills.showShortToast(""+requestCode);
        switch (requestCode) {
            case CaptureActivity.MSG_OTHER:
                String code = data.getStringExtra("code");
                if(StringHelper.isEmpty(code)){
                    Utills.showShortToast("扫码出错");
                    return;
                }else{
                    getPreqrCharge(chargeNum,userId,1,0,0,"",code);
//                    Utills.showShortToast("扫码" + code);
                }
            break;


            case SearchMemberActivity.SELECT_RESULT:
                SearchUserModel userModel = (SearchUserModel) data.getSerializableExtra("userModel");
                if(userModel.name!=null){
                    setUserData(userModel);
                    getPresentList(chargeNum+"",userModel.userId);
                }else{
                    setNoUserData();
                }
                break;
            default:
                break;

        }
    }



}
