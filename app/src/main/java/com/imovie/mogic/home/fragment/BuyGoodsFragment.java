package com.imovie.mogic.home.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.imovie.mogic.R;
import com.imovie.mogic.base.universal_loading.YSBLoadingDialog;
import com.imovie.mogic.car.CarPayActivity;
import com.imovie.mogic.car.adapters.CarAdapter;
import com.imovie.mogic.car.adapters.FoodAdapter;
import com.imovie.mogic.car.adapters.TypeAdapter;
import com.imovie.mogic.car.bean.FoodBean;
import com.imovie.mogic.car.bean.TypeBean;
import com.imovie.mogic.car.view.ListContainer;
import com.imovie.mogic.home.SelectTypeActivity;
import com.imovie.mogic.home.model.CardModel;
import com.imovie.mogic.home.model.GoodsModel;
import com.imovie.mogic.home.net.HomeWebHelper;
import com.imovie.mogic.utills.ACache;
import com.imovie.mogic.utills.AliJsonUtil;
import com.imovie.mogic.utills.DecimalUtil;
import com.imovie.mogic.utills.StringHelper;
import com.imovie.mogic.web.IModelResultListener;
import com.imovie.mogic.web.model.HttpResultModel;
import com.imovie.mogic.widget.ClearButtonEditText;

import org.json.JSONArray;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class BuyGoodsFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static CarAdapter carAdapter;
    private String mParam1;
    private String mParam2;

    public LinearLayout rlSearchTitle;
    public ClearButtonEditText etHomeSearch;
    public ArrayList<GoodsModel> listSearch = new ArrayList<>();
    private ListContainer listContainer;
    public TextView tvCount;

    public List<TypeBean> typeList = new ArrayList<>();
    public List<FoodBean> foodBeanList = new ArrayList<>();

    private ACache mCache;

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

        mCache = ACache.get(getContext());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_goods, container, false);
        initView(v);
        setView();
        getCasheDate();
        return v;
    }

    private void initView(View view) {

        rlSearchTitle = (LinearLayout) view.findViewById(R.id.rlSearchTitle);
        listContainer = (ListContainer) view.findViewById(R.id.listcontainer);
        listContainer.setAddClick((SelectTypeActivity) getActivity());
        etHomeSearch = (ClearButtonEditText) view.findViewById(R.id.etHomeSearch);

    }

    public FoodAdapter getFoodAdapter() {
        return listContainer.foodAdapter;
    }

    public TypeAdapter getTypeAdapter() {
        return listContainer.typeAdapter;
    }

    private void setView() {

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
                if (!StringHelper.isEmpty(chargeFee)) {
                    listSearch.clear();
//                    for(int i = 0;i<listAllCard.size();i++){
//                        if(listAllCard.get(i).name.contains(chargeFee)){
//                            listSearch.add(listAllCard.get(i));
//                        }
//                    }
//                    if(listSearch.size()>0) {
//                        goodsAdapter.list = listSearch;
//                        goodsAdapter.notifyDataSetChanged();
//                    }
                }
            }
        });

    }


    public void refresh() {
        getAllGoodList();
    }

    public void refreshGoodlist() {
        for (int i = 0; i < foodBeanList.size(); i++) {
            foodBeanList.get(i).setSelectCount(0);
        }
        listContainer.refreshTypeAdater(typeList, foodBeanList);
    }

    public void getCasheDate() {
        try {
            if(mCache.getAsJSONArray("typeList")!=null){
                JSONArray testJsonArray = mCache.getAsJSONArray("typeList");
                JSONArray jsonArray = mCache.getAsJSONArray("foodBeanList");
                typeList = AliJsonUtil.parseList(testJsonArray.toString(), TypeBean.class);
                foodBeanList = AliJsonUtil.parseList(jsonArray.toString(), FoodBean.class);
                listContainer.refreshTypeAdater(typeList,foodBeanList);
            }else{
                getAllGoodList();
                Log.e("----1111", "===" + foodBeanList.size());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getAllGoodList(){
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
        HomeWebHelper.getAllGoodList(new IModelResultListener<CardModel>() {
            @Override
            public boolean onGetResultModel(HttpResultModel resultModel) {

//                lvCard.finishLoading(true);
                return false;
            }

            @Override
            public void onSuccess(String resultCode, CardModel resultModel, List<CardModel> resultModelList, String resultMsg, String hint) {

//                lvCard.finishLoading(true);
                if(resultCode.equals("0")) {
                    if(resultModel.categories.size()>0){
                        for(int i=0;i<resultModel.categories.size();i++){
                            TypeBean typeBean = new TypeBean();
                            typeBean.setName(resultModel.categories.get(i).categoryName);
                            typeBean.setCategoryId(resultModel.categories.get(i).categoryId);
                            typeList.add(typeBean);
                            List<GoodsModel> goodsList = resultModel.categories.get(i).goodsList;
                            for(int j=0;j<goodsList.size();j++){
                                FoodBean foodBean = new FoodBean();
                                GoodsModel goodsModel = goodsList.get(j);
//                                foodBean.setGoodsId(goodsModel.goodsId);
                                foodBean.setId(goodsModel.goodsId);
                                foodBean.setGoodsId(goodsModel.goodsId);
                                foodBean.setImageUrl(goodsModel.imageUrl);
                                foodBean.setName(goodsModel.name);
                                foodBean.setTagsName("");
                                BigDecimal price = new BigDecimal(DecimalUtil.FormatMoney(goodsModel.price));
                                foodBean.setPrice(price);
                                foodBean.setType(resultModel.categories.get(i).categoryName);
                                if(goodsModel.goodsPackList.size()>0){
                                    foodBean.setGoodsPackList(goodsModel.goodsPackList);
                                }
                                foodBeanList.add(foodBean);
                            }
                        }
                        listContainer.refreshTypeAdater(typeList,foodBeanList);

                        mCache.put("typeList", AliJsonUtil.toJSONString(typeList),600);
                        mCache.put("foodBeanList", AliJsonUtil.toJSONString(foodBeanList),600);

                    }
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




}
