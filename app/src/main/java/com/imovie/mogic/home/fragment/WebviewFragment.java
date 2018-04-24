package com.imovie.mogic.home.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.imovie.mogic.R;
import com.imovie.mogic.utills.Utills;
import com.imovie.mogic.webview.factory.YSBWebViewClient;
import com.imovie.mogic.webview.widget.YSBWebView;
import com.imovie.mogic.widget.TitleBar;

public class WebviewFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    public YSBWebView id_webview;
    public TitleBar titleBar;



    public WebviewFragment() {

    }

    public static WebviewFragment newInstance(String param1, String param2) {
        WebviewFragment fragment = new WebviewFragment();
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
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        initView(view);
        setView();
//        setListener();

        return view;
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void initView(View view) {
        titleBar = (TitleBar) view.findViewById(R.id.title_bar);
        id_webview = (YSBWebView)view.findViewById(R.id.id_webview);

    }

    private void setView(){
//        String url = "http://mji.imovie.com.cn/mojieAPP/activity/lsactive/test.html";
        String url = "";
        id_webview.setData(id_webview, url);
        YSBWebViewClient webViewClient = new YSBWebViewClient();
        webViewClient.setOnGetTitleListener(new YSBWebViewClient.getTitleListener() {
            @Override
            public void onGetTitle(String title) {
//                titleBar.setTitle(title);
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

    //设置打开的网页
    public void setWebviewUrl(String url){
        try {
            Utills.showShortToast("url="+url);
//            id_webview.loadUrl(url);
            id_webview.removeAllViews();
            id_webview.loadUrl(url);
//            id_webview.setData(id_webview, url);
//            id_webview.setWebViewClient(new WebViewClient(){
//                @Override
//                public void onPageFinished(WebView view, String url) {
//                    // TODO Auto-generated method stub
//                    super.onPageFinished(view, url);
//                }
//                @Override
//                public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                    view.loadUrl(url);
//                    return true;
//                }
//            });
//            id_webview.reload();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void showNavBar(){
        titleBar.setVisibility(View.VISIBLE);
    }
    public void hideNavBar(){
        titleBar.setVisibility(View.GONE);
    }


}
