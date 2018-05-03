package com.imovie.mogic.home.adater;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.imovie.mogic.R;
import com.imovie.mogic.home.model.ChargeListModel;
import com.imovie.mogic.home.model.ClassifyModel;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;


/**
 * MovieGridView adapter
 * @author zhouxinshan 2016-3-17
 */
public class ChargeAdapter extends BaseAdapter {
	public List<ChargeListModel> list = new ArrayList<>();
	private Context context;
	DisplayImageOptions mOption = null;
	public ChargeAdapter(Context context, List<ChargeListModel> list) {
		this.list = list;
		this.context = context;
		mOption = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.discovery01)
				.showImageOnFail(R.drawable.discovery01)
				.showImageForEmptyUri(R.drawable.discovery01)
				.cacheInMemory(true)
				.cacheOnDisk(true)
				.build();
	}

	public int getCount() {
		// TODO Auto-generated method stub
		if (list == null) {
			return 0;
		}
		return list.size();
	}

	public ChargeListModel getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = new ViewHolder();
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.home_charge_list_item, null);
			holder.rlChargeState = (RelativeLayout) convertView.findViewById(R.id.rlChargeState);
			holder.ivChargeState = (ImageView) convertView.findViewById(R.id.ivChargeState);
			holder.tvChargeAmount = (TextView) convertView.findViewById(R.id.tvChargeAmount);
			holder.tvPresentAmount = (TextView) convertView.findViewById(R.id.tvPresentAmount);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		if(list.get(position).isSelect){
//            holder.viewType.setVisibility(View.VISIBLE);
			holder.ivChargeState.setBackgroundResource(R.drawable.bg_line_l7_r3);
			holder.rlChargeState.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.bg_line_l7_r3));
		}else{
//            holder.viewType.setVisibility(View.GONE);
			holder.rlChargeState.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.shape_write_r5_l5));
			holder.ivChargeState.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.shape_write_r5_l5));
		}

		holder.tvChargeAmount.setText(""+list.get(position).chargeAmount);
		holder.tvPresentAmount.setText("充"+list.get(position).chargeAmount+"充"+list.get(position).presentAmount);
//		holder.ivClassifyImage.setBackgroundResource(list.get(position).imageId);

		int screenWidth = context.getResources().getDisplayMetrics().widthPixels;
		int width = (screenWidth - 125) / 3;
		convertView.setLayoutParams(new GridView.LayoutParams(width, width*3/5));



//		try {
//			ImageLoader.getInstance().displayImage(list.get(position).iconUrl,holder.ivClassifyImage,mOption);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		return convertView;

	}

	public void setSelectIndex(int position){
		for(int i=0;i<this.list.size();i++){
			if(i==position){
				this.list.get(i).isSelect = true;
				this.list.get(i).presentAmount =5000;
			}else{
				this.list.get(i).isSelect = false;
			}
		}
		super.notifyDataSetChanged();
	}

	public void setUnSelectIndex(){
		for(int i=0;i<this.list.size();i++){
			list.get(i).isSelect = false;
		}
		super.notifyDataSetChanged();
	}

	class ViewHolder {
		public RelativeLayout rlChargeState = null;
		public ImageView ivChargeState;
		public TextView tvChargeAmount = null;
		public TextView tvPresentAmount = null;
	}

}
