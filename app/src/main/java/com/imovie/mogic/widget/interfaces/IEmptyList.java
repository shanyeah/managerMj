package com.imovie.mogic.widget.interfaces;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 支持空列表提示页功能接口。
 * 接口的方法是互斥的，即只有最后一次setEmpty*(...)生效。
 * */
public interface IEmptyList {

    /** 设置空列表提示view */
    void setEmptyView(View view);

    /**
     * 设置空列表提示语
     * @param emptyTips
     * @return 对应的TextView
     */
    TextView setEmptyTips(String emptyTips);

    /**
     * 设置空列表提示图片
     * @param imgResid 资源图片id。R.drawable.*
     * @return 对应的ImageView
     */
    ImageView setEmptyImageResid(int imgResid);

    /**
     * 设置空列表提示图片和提示语。 提示图片会在提示语上方
     * @param imgResid
     * @param emptyTips
     * @return
     */
    EmptyHolder setEmptyImageResidAndTips(int imgResid, String emptyTips);


    class EmptyHolder{
        public ImageView ivEmpty;
        public TextView tvEmpty;
    }

}
