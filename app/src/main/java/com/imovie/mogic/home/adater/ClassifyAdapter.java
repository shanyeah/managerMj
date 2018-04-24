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
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.util.List;


/**
 * MovieGridView adapter
 * @author zhouxinshan 2016-3-17
 */
public class ClassifyAdapter extends BaseAdapter {
	public List<ClassifyModel> list = null;
	private Context context;
	DisplayImageOptions mOption = null;
	public ClassifyAdapter(Context context, List<ClassifyModel> list) {
		this.list = list;
		this.context = context;
		mOption = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.person_default_profile)
				.showImageOnFail(R.drawable.person_default_profile)
				.showImageForEmptyUri(R.drawable.person_default_profile)
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

	public ClassifyModel getItem(int position) {
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
		holder.ivClassifyImage.setBackgroundResource(list.get(position).imageId);

		int screenWidth = context.getResources().getDisplayMetrics().widthPixels;
		int width = (screenWidth - 125) / 5;
		convertView.setLayoutParams(new GridView.LayoutParams(width, width*7/5));

//		try {
//			ImageLoader.getInstance().displayImage(list.get(position).imageUrl,holder.ivClassifyImage,mOption);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		return convertView;

	}

	class ViewHolder {
		public ImageView ivClassifyImage = null;
		public TextView tvClassifyName = null;
	}

}
