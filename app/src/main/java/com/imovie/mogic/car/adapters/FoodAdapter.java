package com.imovie.mogic.car.adapters;


import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.imovie.mogic.R;
import com.imovie.mogic.car.bean.FoodBean;
import com.imovie.mogic.car.bean.TypeBean;
import com.imovie.mogic.car.view.AddWidget;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

public class FoodAdapter extends BaseQuickAdapter<FoodBean, BaseViewHolder> {
	public static final int FIRST_STICKY_VIEW = 1;
	public static final int HAS_STICKY_VIEW = 2;
	public static final int NONE_STICKY_VIEW = 3;
	private List<FoodBean> flist;
	private AddWidget.OnAddClick onAddClick;

	public FoodAdapter(@Nullable List<FoodBean> data, AddWidget.OnAddClick onAddClick) {
		super(R.layout.item_food, data);
		flist = data;
		this.onAddClick = onAddClick;
	}

	public void updateAdapter(List<FoodBean> list) {
		this.flist.clear();
		this.flist.addAll(list);
		notifyDataSetChanged();
	}

	@Override
	protected void convert(BaseViewHolder helper, FoodBean item) {
		helper.setText(R.id.tv_name, item.getName())
				.setText(R.id.tv_price,item.getStrPrice(mContext))
				.setImageResource(R.id.iv_food, item.getIcon()).addOnClickListener(R.id.addwidget)
				.addOnClickListener(R.id.food_main)
		;
		AddWidget addWidget = helper.getView(R.id.addwidget);
		TextView tv_summary = helper.getView(R.id.tv_summary);
//		addWidget.setData(this, helper.getAdapterPosition(), onAddClick);
		addWidget.setState(2);
		addWidget.setData( onAddClick,item);

		if(item.getGoodsPackList().size()>0){
			tv_summary.setVisibility(View.VISIBLE);
		}else{
			tv_summary.setVisibility(View.GONE);
		}
		ImageView iv_food = helper.getView(R.id.iv_food);
		try {
            DisplayImageOptions mOption = new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.drawable.food0)
                    .showImageOnFail(R.drawable.food0)
                    .showImageForEmptyUri(R.drawable.food0)
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .build();
			ImageLoader.getInstance().displayImage(item.getImageUrl(),iv_food,mOption);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (helper.getAdapterPosition() == 0) {
			helper.setVisible(R.id.stick_header, true)
					.setText(R.id.tv_header, item.getType())
					.setTag(R.id.food_main, FIRST_STICKY_VIEW);
		} else {
			if (!TextUtils.equals(item.getType(), flist.get(helper.getAdapterPosition() - 1).getType())) {
				helper.setVisible(R.id.stick_header, true)
						.setText(R.id.tv_header, item.getType())
						.setTag(R.id.food_main, HAS_STICKY_VIEW);
			} else {
				helper.setVisible(R.id.stick_header, false)
						.setTag(R.id.food_main, NONE_STICKY_VIEW);
			}
		}
		helper.getConvertView().setContentDescription(item.getType());
	}

}
