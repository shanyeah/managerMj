package com.imovie.mogic.home.adater;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.imovie.mogic.R;
import com.imovie.mogic.home.model.ChargeListModel;
import com.imovie.mogic.home.model.GoodsTags;
import com.imovie.mogic.utills.DecimalUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.util.ArrayList;
import java.util.List;


/**
 * MovieGridView adapter
 * @author zhouxinshan 2016-3-17
 */
public class CategoryTagsAdapter extends BaseAdapter {
	public List<GoodsTags> list = new ArrayList<>();
	private Context context;
	DisplayImageOptions mOption = null;
	public CategoryTagsAdapter(Context context, List<GoodsTags> list) {
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

	public GoodsTags getItem(int position) {
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
			convertView = LayoutInflater.from(context).inflate(R.layout.home_category_tag_item, null);
			holder.tvCategoryTagName = (TextView) convertView.findViewById(R.id.tvCategoryTagName);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}


//		holder.rlChargeState.setTag(list.get(position).isSelect);
//		holder.tvChargeAmount.setText(""+ DecimalUtil.FormatMoney(list.get(position).chargeAmount,"#0"));
//		if(list.get(position).presentAmount>0){
//			holder.tvPresentAmount.setVisibility(View.VISIBLE);
//			holder.tvPresentAmount.setText("送"+DecimalUtil.FormatMoney(list.get(position).presentAmount,"#0"));
//		}else{
//			holder.tvPresentAmount.setVisibility(View.GONE);
//		}

//		holder.ivClassifyImage.setBackgroundResource(list.get(position).imageId);
		holder.tvCategoryTagName.setText(list.get(position).name);

//		int screenWidth = context.getResources().getDisplayMetrics().widthPixels;
//		int width = (screenWidth - 125) / 3;
//		convertView.setLayoutParams(new GridView.LayoutParams(width, width*3/5));



//		try {
//			ImageLoader.getInstance().displayImage(list.get(position).iconUrl,holder.ivClassifyImage,mOption);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}


		if(list.get(position).selectId==-1){
			holder.tvCategoryTagName.setBackground(context.getResources().getDrawable(R.drawable.shape_write_r5_l5));
		}else{
			holder.tvCategoryTagName.setBackground(context.getResources().getDrawable(R.drawable.bg_line_l7_r3));
		}
		return convertView;

	}

	public void setSelectIndex(int selectId){
		for(int i=0;i<this.list.size();i++){
			if(this.list.get(i).id == selectId){
				this.list.get(i).selectId = this.list.get(i).id;
				this.list.get(i).selectname = this.list.get(i).name;
//				this.list.get(i).presentAmount = 5000;
			}else{
				this.list.get(i).selectId = -1;
//				this.list.get(i).presentAmount = 1000;
			}
		}
		super.notifyDataSetChanged();
	}

	class ViewHolder {
		public TextView tvCategoryTagName = null;
	}

}
