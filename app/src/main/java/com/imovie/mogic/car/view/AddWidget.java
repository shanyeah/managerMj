package com.imovie.mogic.car.view;


import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.florent37.viewanimator.AnimationListener;
import com.github.florent37.viewanimator.ViewAnimator;
import com.imovie.mogic.R;
import com.imovie.mogic.car.bean.FoodBean;

public class AddWidget extends FrameLayout {

	private View sub;
	private TextView tv_count;
	public TextView tvAddCar;
	public LinearLayout llAddCar;
	private long count;
	private AddButton addbutton;
	private boolean sub_anim, circle_anim;
	private FoodBean foodBean;
	private boolean btAdd = false;
	private int type;

	public interface OnAddClick {

		void onAddClick(View view, FoodBean fb,int type);

		void onSubClick(View view,FoodBean fb,int type);
	}

	private OnAddClick onAddClick;

	public AddWidget(@NonNull Context context) {
		super(context);
	}

	public AddWidget(@NonNull Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);
		inflate(context, R.layout.view_addwidget, this);
		addbutton = findViewById(R.id.addbutton);
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AddWidget);
		for (int i = 0; i < a.getIndexCount(); i++) {
			int attr = a.getIndex(i);
			switch (attr) {
				case R.styleable.AddWidget_circle_anim:
					circle_anim = a.getBoolean(R.styleable.AddWidget_circle_anim, false);
					break;
				case R.styleable.AddWidget_sub_anim:
					sub_anim = a.getBoolean(R.styleable.AddWidget_sub_anim, false);
					break;
			}

		}
		a.recycle();
		sub = findViewById(R.id.iv_sub);
		tv_count = findViewById(R.id.tv_count);
		addbutton.setAnimListner(new AddButton.AnimListner() {
			@Override
			public void onStop() {
				if(!btAdd) {
					if (count == 0) {
						ViewAnimator.animate(sub)
								.translationX(addbutton.getLeft() - sub.getLeft(), 0)
								.rotation(360)
								.alpha(0, 255)
								.duration(300)
								.interpolator(new DecelerateInterpolator())
								.andAnimate(tv_count)
								.translationX(addbutton.getLeft() - tv_count.getLeft(), 0)
								.rotation(360)
								.alpha(0, 255)
								.interpolator(new DecelerateInterpolator())
								.duration(300)
								.start()
						;
					}
				}
//				count++;
				if(!btAdd)tv_count.setText(count + "");
				foodBean.setSelectCount(count);
				if (onAddClick != null) {
					onAddClick.onAddClick(addbutton, foodBean,type);
				}
			}
		});
		sub.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (count == 0) {
					return;
				}
				if (count == 1 && sub_anim) {
					subAnim();
				}
//				count--;
//				tv_count.setText(count == 0 ? "1" : count + "");
//				foodBean.setSelectCount(count);
				tv_count.setText(count + "");
				if (onAddClick != null) {
					onAddClick.onSubClick(v,foodBean,type);
				}
			}
		});

//		tvAddCar = findViewById(R.id.tv_count);
		llAddCar = findViewById(R.id.llAddCar);
	}

	private void subAnim(){
		ViewAnimator.animate(sub)
				.translationX(0, addbutton.getLeft() - sub.getLeft())
				.rotation(-360)
				.alpha(255, 0)
				.duration(300)
				.interpolator(new AccelerateInterpolator())
				.andAnimate(tv_count)
				.onStop(new AnimationListener.Stop() {
					@Override
					public void onStop() {
						if (circle_anim) {
							addbutton.expendAnim();
						}
					}
				})
				.translationX(0, addbutton.getLeft() - tv_count.getLeft())
				.rotation(-360)
				.alpha(255, 0)
				.interpolator(new AccelerateInterpolator())
				.duration(300)
				.start()
		;
	}

	public void setData(OnAddClick onAddClick, FoodBean foodBean,int type) {
		this.foodBean = foodBean;
		this.type = type;
		this.onAddClick = onAddClick;
		count = foodBean.getSelectCount();
        if(!btAdd) {
            if (count == 0) {
                sub.setAlpha(0);
                tv_count.setAlpha(0);
            } else {
                sub.setAlpha(1f);
                tv_count.setAlpha(1f);
                tv_count.setText(count + "");
            }
        }

	}

	public void setState(long count) {
//		addbutton.setState(count > 0);
		addbutton.setState(false);
		btAdd = true;
	}


	public void expendAdd(long count) {
		this.count = count;
		if(!btAdd){
            tv_count.setText(count == 0 ? "1" : count + "");
            if (count == 0) {
                subAnim();
            }
        }
	}
}
