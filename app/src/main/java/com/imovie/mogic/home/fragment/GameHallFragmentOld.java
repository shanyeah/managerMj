package com.imovie.mogic.home.fragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.imovie.mogic.R;
import com.imovie.mogic.card.model.InternetBarModel;
import com.imovie.mogic.myRank.widget.SpinerPopWindow;
import com.imovie.mogic.home.adater.GameHallAdapter;
import com.imovie.mogic.home.model.GameHall;
import com.imovie.mogic.home.model.HallModel;
import com.imovie.mogic.home.net.HomeWebHelper;
import com.imovie.mogic.web.IModelResultListener;
import com.imovie.mogic.web.model.HttpResultModel;
import com.imovie.mogic.webview.WebViewManager;
import com.imovie.mogic.widget.YSBPageListView;

import java.util.ArrayList;
import java.util.List;

public class GameHallFragmentOld extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    public TextView tvHallOrder;
    public TextView tvHallArea;
    public YSBPageListView lvGameHall;
    public ArrayList<GameHall> listHall;
    public GameHallAdapter hallAdapter;

    private SpinerPopWindow<InternetBarModel> mSpinerPopWindow;
    private List<InternetBarModel> list = new ArrayList<>();

    private String cityId = "";
    private int orderType = 0;

    public GameHallFragmentOld() {

    }

    public static GameHallFragmentOld newInstance(String param1, String param2) {
        GameHallFragmentOld fragment = new GameHallFragmentOld();
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
        View v=inflater.inflate(R.layout.fragment_game_hall, container, false);
        initView(v);
        setView();
        setListener();
        return v;
    }
    private void initView(View view) {
        tvHallArea = (TextView) view.findViewById(R.id.tvHallArea);
        tvHallOrder = (TextView) view.findViewById(R.id.tvHallOrder);
        lvGameHall = (YSBPageListView) view.findViewById(R.id.lv_game_hall);
        mSpinerPopWindow = new SpinerPopWindow<InternetBarModel>(getActivity(), list,itemClickListener);


//        LinearLayout.LayoutParams slideShowViewParam=(LinearLayout.LayoutParams)adSlideBanner.getLayoutParams();
//        slideShowViewParam.width= AppConfig.getScreenWidth();
//        slideShowViewParam.height=(AppConfig.getScreenWidth()*244)/640;
//        adSlideBanner.setLayoutParams(slideShowViewParam);

    }

    private void setView() {
        listHall = new ArrayList<>();
        hallAdapter = new GameHallAdapter(getContext(),listHall);
        lvGameHall.setAdapter(hallAdapter);
        lvGameHall.setPageSize(10);
        getHallList(0,"");
        getCityList();
    }

    private void setListener() {
        lvGameHall.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent intent = new Intent(getContext(), HallActivity.class);
//                intent.putExtra("stgId",hallAdapter.getItem(position).images.get(0).stgId);
//                startActivity(intent);

                try {
//                    String url = "http://mji.imovie.com.cn/mojieAPP/page/ESP/booksteas.html?stgId=8001";
                    String url = "http://mji.imovie.com.cn/mojieAPP/page/ESP/espdetails.html?bartab=false";
                    WebViewManager.enterWebView(getContext(),url,false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        tvHallArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mSpinerPopWindow.setWidth(tvHallArea.getWidth());
                mSpinerPopWindow.showAsDropDown(tvHallArea);
                setTextImage(R.drawable.icon_up);

            }
        });

        mSpinerPopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setTextImage(R.drawable.icon_down);
            }
        });

        tvHallOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderType=1;
                getHallList(orderType,cityId);
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
            InternetBarModel internetBarModel = (InternetBarModel)mSpinerPopWindow.list.get(position);
            tvHallArea.setText(internetBarModel.name);
            mSpinerPopWindow.dismiss();
            cityId = "" + internetBarModel.id;
            if(cityId.equals("0")) cityId="";
            getHallList(orderType,cityId);
//            Toast.makeText(MovieSelectActivity.this, "点击了:" + internetBarModel.id,Toast.LENGTH_SHORT).show();
        }
    };



    public void getHallList(int orderType,String cityId){
        HomeWebHelper.getHallList(orderType,cityId,1,20,new IModelResultListener<HallModel>() {
            @Override
            public boolean onGetResultModel(HttpResultModel resultModel) {
                return false;
            }

            @Override
            public void onSuccess(String resultCode, HallModel resultModel, List<HallModel> resultModelList, String resultMsg, String hint) {
//                Log.e("----hall:",""+resultCode);
                if(resultModelList.size()>0) {
//                    hallAdapter.list = resultModelList;
//                    hallAdapter.notifyDataSetChanged();
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
                    mSpinerPopWindow.refreshData(resultModelList);
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

}
