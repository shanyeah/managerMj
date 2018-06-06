package com.imovie.mogic.car.adapters;


import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.imovie.mogic.R;
import com.imovie.mogic.car.bean.FoodBean;
import com.imovie.mogic.car.view.AddWidget;
import com.imovie.mogic.home.adater.GoodsPayTagAdapter;
import com.imovie.mogic.utills.StringHelper;
import com.imovie.mogic.widget.NoScrollListView;

import java.math.BigDecimal;
import java.util.List;

public class CarAdapter extends BaseQuickAdapter<FoodBean, BaseViewHolder> {
	private AddWidget.OnAddClick onAddClick;
//	public List<FoodBean> list;

	public CarAdapter(@Nullable List<FoodBean> data, AddWidget.OnAddClick onAddClick) {
		super(R.layout.item_car, data);
//		this.list = data;
		this.onAddClick = onAddClick;
	}

	@Override
	protected void convert(BaseViewHolder helper, FoodBean item) {
		helper.setText(R.id.car_name, item.getName())
				.setText(R.id.car_price, item.getStrPrice(mContext, item.getPrice().multiply(BigDecimal.valueOf(item.getSelectCount()))))
		;
		AddWidget addWidget = helper.getView(R.id.car_addwidget);

//		addWidget.setData(this, helper.getAdapterPosition(), onAddClick);
		addWidget.setData(onAddClick,item,2);
		TextView car_tag_name = helper.getView(R.id.car_tag_name);
		if(!StringHelper.isEmpty(item.getTagsName())) {
			car_tag_name.setVisibility(View.VISIBLE);
			car_tag_name.setText("(" + item.getTagsName() + ")");
		}else{
			car_tag_name.setVisibility(View.GONE);
		}

		NoScrollListView lvCarTagList = (NoScrollListView) helper.getView(R.id.lvCarTagList);
		if(item.getGoodsPackList().size()>0){
			lvCarTagList.setVisibility(View.VISIBLE);
			GoodsPayTagAdapter adapter = new GoodsPayTagAdapter(mContext,item.getGoodsPackList());
			lvCarTagList.setAdapter(adapter);
		}else{
			lvCarTagList.setVisibility(View.GONE);
		}
	}
}
