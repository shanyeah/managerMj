package com.imovie.mogic.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.imovie.mogic.R;


/** 加载进度view  */
public class FooterLoadingView extends LinearLayout {
    private ProgressBar pbLoading;
    private TextView tvFooterViewTips;

    /** */
    public void setProgressBarVisibility(int visibility) {
        if(pbLoading.getVisibility() != visibility) {
            pbLoading.setVisibility(visibility);
        }
    }
    /** */
    public void setTips(String tips) {
        tvFooterViewTips.setText(tips);
    }
    /** */
    public void setTipsVisibility(int visibility) {
        if(tvFooterViewTips.getVisibility() != visibility) {
            tvFooterViewTips.setVisibility(visibility);
        }
    }

    public FooterLoadingView(Context context) {
        super(context);
        init();
    }

    public FooterLoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.libs_listview_loadmore_view, this);
        pbLoading = (ProgressBar)findViewById(R.id.pbLoading);
        tvFooterViewTips = (TextView)findViewById(R.id.tvFooterViewTips);
        tvFooterViewTips.setVisibility(GONE);
    }



}
