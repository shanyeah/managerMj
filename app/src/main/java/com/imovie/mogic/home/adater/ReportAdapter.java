package com.imovie.mogic.home.adater;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.imovie.mogic.R;
import com.imovie.mogic.home.model.ClassifyModel;
import com.imovie.mogic.home.model.ReportModel;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;


/**
 * MovieGridView adapter
 * @author zhouxinshan 2016-3-17
 */
public class ReportAdapter extends BaseAdapter {
	public List<ReportModel> list = null;
	private Context context;
	DisplayImageOptions mOption = null;
	public ReportAdapter(Context context, List<ReportModel> list) {
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

	public ReportModel getItem(int position) {
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
			convertView = LayoutInflater.from(context).inflate(R.layout.home_classify_item, null);
			holder.ivClassifyImage = (ImageView) convertView.findViewById(R.id.ivClassifyImage);
			holder.tvClassifyName = (TextView) convertView.findViewById(R.id.tvClassifyName);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.tvClassifyName.setText(list.get(position).name);
//		holder.ivClassifyImage.setBackgroundResource(list.get(position).imageId);

		int screenWidth = context.getResources().getDisplayMetrics().widthPixels;
		int width = (screenWidth - 125) / 3;
		convertView.setLayoutParams(new GridView.LayoutParams(width, width));

		try {
			ImageLoader.getInstance().displayImage(list.get(position).imgUrl,holder.ivClassifyImage,mOption);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return convertView;

	}

	class ViewHolder {
		public ImageView ivClassifyImage = null;
		public TextView tvClassifyName = null;
	}

}
