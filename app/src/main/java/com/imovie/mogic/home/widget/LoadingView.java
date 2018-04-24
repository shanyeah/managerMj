package com.imovie.mogic.home.widget;

import java.lang.ref.SoftReference;
import java.util.HashMap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.imovie.mogic.R;
import com.imovie.mogic.utills.ImageUtil;

public class LoadingView extends BaseView {
	public static final int FINISH = 0;
	public static final int LOADING = 1;
	private HashMap<String, SoftReference<Bitmap>> caches;
	private ImageView iv;
	private RelativeLayout layout;
	private RelativeLayout layout_loading;
	private Animation rotate;
	private TextView tv;

	public LoadingView(Context context) {
		super(context);
	}

	public LoadingView(Context context, AttributeSet attributeSet) {
		super(context, attributeSet);
	}

	public void onCreate(Context context) {
		super.onCreate(context);
		setContentView(R.layout.view_loading);
		this.caches = new HashMap<String, SoftReference<Bitmap>>();
		layout_loading = (RelativeLayout) findViewById(R.id.layout_loading);
		layout = (RelativeLayout) findViewById(R.id.layout);
		iv = (ImageView) findViewById(R.id.iv);
		this.layout_loading.setBackgroundDrawable(new BitmapDrawable(ImageUtil
				.setImage(context, this.caches, R.drawable.bg_loading_normal)));
		this.iv.setImageBitmap(ImageUtil.setImage(context, this.caches,
				R.drawable.ic_loading_normal));
		this.tv = ((TextView)findViewById(R.id.tv));
	    
		this.rotate = AnimationUtils.loadAnimation(context,
				R.anim.rotate_loading);
		this.rotate.setInterpolator(new LinearInterpolator());
		this.layout.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
			}
		});
	}

	public void setText(String text) {
		this.tv.setText(text);
	}
	 
	public void showState(int status, String text) {
		switch (status) {
		case LOADING:
			iv.startAnimation(rotate);
			setVisibility(VISIBLE);
			break;
		case FINISH:
			iv.clearAnimation();
			setVisibility(GONE);
			caches.clear();
			break;
		}
	}

}
