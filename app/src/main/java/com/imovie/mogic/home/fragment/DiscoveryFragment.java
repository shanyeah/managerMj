package com.imovie.mogic.home.fragment;

import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.imovie.mogic.MyApplication;
import com.imovie.mogic.R;
import com.imovie.mogic.utills.StringHelper;
import com.imovie.mogic.utills.Utills;
import com.imovie.mogic.webview.factory.YSBWebViewClient;
import com.imovie.mogic.webview.widget.YSBWebView;
import com.imovie.mogic.widget.TitleBar;


public class DiscoveryFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    public YSBWebView id_webview;
    public TitleBar titleBar;


    public DiscoveryFragment() {

    }

    public static DiscoveryFragment newInstance(String param1, String param2) {
        DiscoveryFragment fragment = new DiscoveryFragment();
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
        View v=inflater.inflate(R.layout.fragment_discovery, container, false);
        initView(v);
        setView();
        return v;
    }

    private void initView(View view) {
        titleBar = (TitleBar) view.findViewById(R.id.title_bar);
        id_webview = (YSBWebView)view.findViewById(R.id.id_webview);
    }

    private void setView(){
//        String url = "http://mji.imovie.com.cn/mojieAPP/activity/lsactive/test.html";
        String phone = MyApplication.getInstance().mPref.getString("phone","");
        String url = "http://m.jesport.com/sly/apilogin";
        if(!StringHelper.isEmpty(phone)) url=url+"?phone="+phone;
        //设置打开的网页
//        id_webview.setData(id_webview, url);
        Utills.showShortToast(url);
        Log.e("----",url);
        YSBWebViewClient webViewClient = new YSBWebViewClient();
        webViewClient.setOnGetTitleListener(new YSBWebViewClient.getTitleListener() {
            @Override
            public void onGetTitle(String title) {
                titleBar.setTitle(title);
            }
        });

        id_webview.setWebViewClient(webViewClient);


        titleBar.setLeftListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(id_webview.canGoBack()){
                    id_webview.goBack();
                }else{
                    titleBar.setVisibility(View.GONE);
                }
            }
        });

    }
    public void showNavBar(){
        titleBar.setVisibility(View.VISIBLE);
    }
    public void hideNavBar(){
        titleBar.setVisibility(View.GONE);
    }
}
