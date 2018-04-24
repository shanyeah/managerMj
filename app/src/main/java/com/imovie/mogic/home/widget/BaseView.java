package com.imovie.mogic.home.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

public class BaseView extends RelativeLayout {
	private LayoutParams params = new LayoutParams(
			LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

	public BaseView(Context context) {
		super(context);
		onCreate(context);
	}

	public BaseView(Context context, AttributeSet attributeSet) {
		super(context, attributeSet);
		onCreate(context);
	}

	public void onCreate(Context context) {
	}

	public void setContentView(int layoutId) {
		View view = LayoutInflater.from(getContext()).inflate(layoutId,
				null);
		view.setLayoutParams(this.params);
		addView(view);
	}
}
