package com.imovie.mogic.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.imovie.mogic.R;

import butterknife.BindView;
import butterknife.ButterKnife;



//import butterknife.ButterKnife;
//import ButterKnife.bindView;

public class TitleBar extends LinearLayout {
    
    public LinearLayout llNavLeft;
    public ImageView ivNavLeft;
    public LinearLayout llNavMiddle;
    public TextView tvNavMiddle;
    public RelativeLayout llNavRight; // 建议
    public TextView tvNavRight; // 右布局基本文字
    public ImageView ivNavRight; //  右布局基本图片
    

    public TitleBar(Context context) {
        super(context);
        initLayout();
    }

    public TitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initLayout();
    }

    public TitleBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initLayout();
    }

    private void initLayout() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.base_title_bar, this);
        llNavLeft = (LinearLayout) view.findViewById(R.id.llNavLeft);
        ivNavLeft = (ImageView) view.findViewById(R.id.ivNavLeft);
        llNavMiddle = (LinearLayout) view.findViewById(R.id.llNavMiddle);
        tvNavMiddle = (TextView) view.findViewById(R.id.tvNavMiddle);

        ivNavRight = (ImageView) view.findViewById(R.id.ivNavRight);
        llNavRight = (RelativeLayout) view.findViewById(R.id.llNavRight);
        tvNavRight = (TextView) view.findViewById(R.id.tvNavRight);
        ivNavRight.setVisibility(GONE);
        tvNavRight.setVisibility(GONE);
    }


    /** 设置左布局监听器 */
    public void setLeftListener(OnClickListener leftListener) {
        llNavLeft.setOnClickListener(leftListener);
    }

    /** 设置中间title文案 */
    public void setTitle(String title) {
        tvNavMiddle.setText(title);
    }

    /** 设置中间布局监听器 */
    public void setMiddleListener(OnClickListener leftListener) {
        llNavMiddle.setOnClickListener(leftListener);
    }

    /** 隐藏左边。 默认显示 */
    public void setLeftLayoutGone() {
        llNavLeft.setVisibility(View.INVISIBLE);
    }

    /////////////////// 右布局 //////////////////////////////////////////////////////////////////
    /** 使用右文字 */
    public void enableRightTextView(String rightText, OnClickListener listener) {
        tvNavRight.setText(rightText);
        tvNavRight.setOnClickListener(listener);
        tvNavRight.setVisibility(VISIBLE);
    }
    /** 使用右图 */
    public void enableRightImageView(int resid, OnClickListener listener) {
        ivNavRight.setImageResource(resid);
        ivNavRight.setOnClickListener(listener);
        ivNavRight.setVisibility(VISIBLE);
    }

    public void setRightTextSize(int sp){
        tvNavRight.setTextSize(sp);
    }


    /**
     * 添加右布局.
     * 因右布局变化多，如果基本的文字view和图片view不能满足需求，建议另创建一个布局控件再加进来。
     * */
    public void addViewToRightLayout(View view) {
        llNavRight.addView(view);
    }

}
