package com.imovie.mogic.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RatingBar;
import android.widget.TextView;

import com.imovie.mogic.R;

import java.io.File;

public class RatingBarPopupWindow {

    public File savePictureFile;
    private static final int PHOTO_REQUEST_TAKEPHOTO = 1;// take photo
    private static final int PHOTO_REQUEST_GALLERY = 2;// choose from gallery
    private static final int PHOTO_REQUEST_CUT = 3;// cut picture

    //the popupWindow
    private PopupWindow mPopupWindow;

    private OnPhotoPopupWindowItemClickListener listener;

    private Context context;

    public RatingBarPopupWindow(Context context) {

        this(context, null);

    }

    public RatingBarPopupWindow(Context context, OnPhotoPopupWindowItemClickListener listener) {

        this.context = context;
        this.listener = listener;

    }

    public void showPopupWindow() {

        showPopupWindow(null);

    }

    public void showPopupWindow(OnPhotoPopupWindowItemClickListener listener) {

        if (!(context instanceof Activity))
            return;

        if (listener != null) {
            this.listener = listener;
        }

        View popupView = initPopupView();

        mPopupWindow = new PopupWindow(popupView, LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, true);
        mPopupWindow.setAnimationStyle(R.style.popupwindow_animation_style);
        mPopupWindow.setTouchable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable(context.getResources(), (Bitmap) null));

        mPopupWindow.showAtLocation(((Activity)context).getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
    }



    private View initPopupView() {

        View popupView = LayoutInflater.from(context).inflate(R.layout.popupwindow_rating_bar, null);
        final RatingBar ratingBar = (RatingBar) popupView.findViewById(R.id.ratingBar);
        final TextView tvRatingSorce = (TextView) popupView.findViewById(R.id.tvRatingSorce);
        final Button btn_takePhoto = (Button) popupView.findViewById(R.id.popupwindow_choose_picture_btn_takephoto);
        final Button btn_choose = (Button) popupView.findViewById(R.id.popupwindow_choose_picture_btn_choose);
        final Button btn_cancel = (Button) popupView.findViewById(R.id.popupwindow_choose_picture_btn_cancel);

        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (view.equals(btn_takePhoto)) {
                    if (listener != null) {
                        listener.onTakePhotoClick();
                    }
                    mPopupWindow.dismiss();
                } else if (view.equals(btn_choose)) {
                    if (listener != null) {
                        listener.onChoosePhotoClick();
                    }
                    mPopupWindow.dismiss();
                }
                else if (view.equals(btn_cancel)) {
                    if (listener != null) {
                        listener.onCancelClick();
                    }
                    mPopupWindow.dismiss();
                }

            }
        };

        btn_takePhoto.setOnClickListener(clickListener);
        btn_choose.setOnClickListener(clickListener);
        btn_cancel.setOnClickListener(clickListener);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                tvRatingSorce.setText(rating + "分");
            }
        });

        return popupView;

    }

}
