package com.imovie.mogic.home.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.imovie.mogic.R;
import com.imovie.mogic.card.model.InternetBarModel;
import com.imovie.mogic.home.SelectTypeActivity;
import com.imovie.mogic.home.adater.ClassifyAdapter;
import com.imovie.mogic.home.adater.GameHallAdapter;
import com.imovie.mogic.home.adater.ReportAdapter;
import com.imovie.mogic.home.model.ClassifyModel;
import com.imovie.mogic.home.model.GameHall;
import com.imovie.mogic.home.model.MyQrCodeModel;
import com.imovie.mogic.home.model.ReportListModel;
import com.imovie.mogic.home.model.ReportModel;
import com.imovie.mogic.home.net.HomeWebHelper;
import com.imovie.mogic.login.model.MyDataModel;
import com.imovie.mogic.login.model.TestModel;
import com.imovie.mogic.myRank.MinePraiseActivity;
import com.imovie.mogic.myRank.MineRankActivity;
import com.imovie.mogic.utills.QRCodeUtil;
import com.imovie.mogic.utills.Utills;
import com.imovie.mogic.web.IModelResultListener;
import com.imovie.mogic.web.model.HttpResultModel;
import com.imovie.mogic.webview.WebViewManager;
import com.imovie.mogic.widget.FlexibleFrameLayout;
import com.imovie.mogic.widget.PageGridView;
import com.imovie.mogic.widget.PullToRefreshFrameLayout;
import com.imovie.mogic.widget.TitleBar;
import com.imovie.mogic.widget.YSBPageListView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

public class ReportFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private TitleBar titleBar;
    private PullToRefreshFrameLayout pull_content;
    private FlexibleFrameLayout ff_list;
    private LinearLayout ll_ad;

    public PageGridView lv_report_list;
    public List<ReportModel> listClassify = new ArrayList<>();
    public ReportAdapter reportAdapter;


    public ReportFragment() {

    }

    public static ReportFragment newInstance(String param1, String param2) {
        ReportFragment fragment = new ReportFragment();
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
        View v=inflater.inflate(R.layout.home_report_fragment, container, false);
        initView(v);
        setView();
        setListener();
        return v;
    }
    private void initView(View view) {

        titleBar = (TitleBar) view.findViewById(R.id.title_bar);
        titleBar.setTitle("报表");
        titleBar.setLeftLayoutGone();


        pull_content = (PullToRefreshFrameLayout) view.findViewById(R.id.pull_content);
        ff_list = (FlexibleFrameLayout) view.findViewById(R.id.ff_list);
        ll_ad = (LinearLayout) view.findViewById(R.id.ll_ad);
        lv_report_list = (PageGridView) view.findViewById(R.id.lv_report_list);

    }

    private void setView() {
        setPullAndFlexListener();

        for(int i=0;i<6;i++){
            ReportModel model = new ReportModel();
            model.name = "报表1"+i;
            listClassify.add(model);
        }
        reportAdapter = new ReportAdapter(getContext(),listClassify);
        lv_report_list.setAdapter(reportAdapter);
        queryReportList();
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
                queryReportList();
            }
        });
        lv_report_list.setOnScrollListener(new AbsListView.OnScrollListener() {
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

    private void setListener() {
        lv_report_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                if(classifyAdapter.getItem(position).id==4){
//                    WebViewManager.enterWebView(getContext(),classifyAdapter.getItem(position).url,false);
//                }else {
                try {
//                    Intent intent = new Intent(getContext(), SelectTypeActivity.class);
//                    intent.putExtra("classifyModel", classifyAdapter.getItem(position));
//                    startActivity(intent);
//                    WebViewManager.enterWebView(getActivity(),reportAdapter.getItem(position).imgUrl,false);
                    WebViewManager.enterTitleWebView(getActivity(),reportAdapter.getItem(position).imgUrl,reportAdapter.getItem(position).name,false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
//                }
            }
        });

    }



    public void queryReportList(){
        HomeWebHelper.getReportList(new IModelResultListener<ReportListModel>() {
            @Override
            public boolean onGetResultModel(HttpResultModel resultModel) {
                pull_content.endRefresh(true);
                return false;
            }

            @Override
            public void onSuccess(String resultCode, ReportListModel resultModel, List<ReportListModel> resultModelList, String resultMsg, String hint) {
                pull_content.endRefresh(true);
                if(resultCode.equals("0")){
                    try {
                        if(resultModel.reportList.size()>0){
                            reportAdapter.list.clear();
                            reportAdapter.list.addAll(resultModel.reportList);
                            reportAdapter.notifyDataSetChanged();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }else{
//                    Utills.showShortToast("获取不到二维码");
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
