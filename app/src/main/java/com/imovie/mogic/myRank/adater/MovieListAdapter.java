package com.imovie.mogic.myRank.adater;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.imovie.mogic.R;
import com.imovie.mogic.myRank.model.MovieModel;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;



/**
 * MovieGridView adapter
 * @author zhouxinshan 2016-3-17
 */
public class MovieListAdapter extends BaseAdapter {
	public List<MovieModel> list = null;
	private int isSelect = -1;
	private int isClick = -1;
	private Context context;
	DisplayImageOptions options = null;

	public MovieListAdapter(Context context, List<MovieModel> list) {
		this.list = list;
		this.context = context;
		options = new DisplayImageOptions.Builder()
				.resetViewBeforeLoading()
				.cacheInMemory()
				.cacheOnDisc()
				.bitmapConfig(Bitmap.Config.RGB_565).build();
	}

	public int getCount() {
		// TODO Auto-generated method stub
		if (list == null) {
			return 0;
		}
		return list.size();
	}

	public MovieModel getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

		public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public void setNotifyDataChange(int id) {
		isSelect = id;
		super.notifyDataSetChanged();
	}

	public void setNotifyItemSelected(int id) {
		isClick = id;
		super.notifyDataSetChanged();
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = new ViewHolder();
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.discovery_movie_item, null);
			holder.rlMovieItemLayout = (RelativeLayout) convertView.findViewById(R.id.rlMovieItemLayout);
			holder.ivMovieImage = (ImageView) convertView.findViewById(R.id.ivMovieImage);
			holder.tvMovieName = (TextView) convertView.findViewById(R.id.tvMovieName);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		String name = list.get(position).name;
		holder.tvMovieName.setText(name);
		holder.tvMovieName.setTag(getItemId(position) + "");

//		RelativeLayout.LayoutParams imageParams = (RelativeLayout.LayoutParams) holder.ivMovieImage.getLayoutParams();
		int screenWidth = context.getResources().getDisplayMetrics().widthPixels;
		int width = (screenWidth - 125) / 3;
//		imageParams.width = width;
//		imageParams.height = width*3/2;
//		holder.ivMovieImage.setLayoutParams(imageParams);

		convertView.setLayoutParams(new GridView.LayoutParams(width, width*7/5));
//        int p = Integer.parseInt((String) holder.tvMovieName.getTag());
//		if (isClick == p) {
//			holder.rlMovieItemLayout.setBackgroundResource(R.drawable.bg_focus);
//		} else {
//			if (isSelect == p) {
//				holder.rlMovieItemLayout.setBackgroundResource(R.drawable.interative_normal);
//			} else {
//				holder.rlMovieItemLayout.setBackgroundResource(R.drawable.interative_normal);
//			}
//		}

		try {
			ImageLoader.getInstance().displayImage(list.get(position).previewPoster,holder.ivMovieImage,options);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return convertView;

	}

	class ViewHolder {
		public RelativeLayout rlMovieItemLayout = null;
		public ImageView ivMovieImage = null;
		public TextView tvMovieName = null;
	}

}
