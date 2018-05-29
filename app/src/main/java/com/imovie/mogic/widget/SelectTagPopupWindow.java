package com.imovie.mogic.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RatingBar;
import android.widget.TextView;

import com.imovie.mogic.R;
import com.imovie.mogic.car.DetailActivity;
import com.imovie.mogic.home.adater.CategorysAdapter;
import com.imovie.mogic.home.adater.GoodsTagAdapter;
import com.imovie.mogic.home.model.CategorysModel;
import com.imovie.mogic.home.model.GoodTagList;
import com.imovie.mogic.utills.Utills;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SelectTagPopupWindow {


    //the popupWindow
    private PopupWindow mPopupWindow;
    public List<CategorysModel.Categorys> list = new ArrayList<>();
    public CategorysAdapter categorysAdapter;
    public GoodTagList tag;

    public interface onSelectListener {
        void onSelect(GoodTagList tag);
    }
    private onSelectListener listener;
    public void setOnSelectListener(onSelectListener listener) {
        this.listener = listener;
    }

    private Context context;

//    public SelectTagPopupWindow(Context context,List<CategorysModel.Categorys> list,GoodTagList tag) {
//
//        this(context,list,tag);
////        Utills.showShortToast(this.list.get(0).name);
//
//    }

    public SelectTagPopupWindow(Context context,List<CategorysModel.Categorys> list,GoodTagList tag) {

        this.context = context;
        this.list = list;
        this.tag = tag;

    }

    public void showPopupWindow() {

        showPopup();

    }

    public void showPopup() {

        if (!(context instanceof Activity))
            return;

        View popupView = initPopupView();

        mPopupWindow = new PopupWindow(popupView, LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, true);
        mPopupWindow.setAnimationStyle(R.style.popupwindow_animation_style);
        mPopupWindow.setTouchable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.popup_select_tag));
        mPopupWindow.showAtLocation(((Activity)context).getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);

    }



    private View initPopupView() {

        View popupView = LayoutInflater.from(context).inflate(R.layout.popupwindow_select_tag, null);
        final ImageView ivGoodImage = (ImageView) popupView.findViewById(R.id.ivGoodImage);
        final TextView tvTagName = (TextView) popupView.findViewById(R.id.tvTagName);
        final ImageView ivCancel = (ImageView) popupView.findViewById(R.id.ivCancel);
        final NoScrollListView lvCategorysList = (NoScrollListView) popupView.findViewById(R.id.lvCategorysList);
        final Button btOk = (Button) popupView.findViewById(R.id.btOk);
        DisplayImageOptions mOption = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.food0)
                .showImageOnFail(R.drawable.food0)
                .showImageForEmptyUri(R.drawable.food0)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        try {
            ImageLoader.getInstance().displayImage(tag.imageUrl,ivGoodImage,mOption);
        } catch (Exception e) {
            e.printStackTrace();
        }
        tvTagName.setText(tag.name);
        categorysAdapter = new CategorysAdapter(context,list);
        lvCategorysList.setAdapter(categorysAdapter);

        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (view.equals(btOk)) {
                    if (listener != null) {
                        if(categorysAdapter.list.size()>0) {
                            tag.tagsName = categorysAdapter.getTags()[1];
                        }
                        listener.onSelect(tag);
                    }
                    mPopupWindow.dismiss();
                }
                else if (view.equals(ivCancel)) {
                    mPopupWindow.dismiss();
                }

            }
        };

        ivCancel.setOnClickListener(clickListener);
        btOk.setOnClickListener(clickListener);

        return popupView;
    }

}
