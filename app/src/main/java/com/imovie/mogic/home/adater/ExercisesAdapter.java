package com.imovie.mogic.home.adater;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.imovie.mogic.R;
import com.imovie.mogic.myRank.model.ExercisesMode;
import com.imovie.mogic.utills.DateUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;


/**
 * Created by zhouxinshan on 2016/3/31.
 */
public class ExercisesAdapter extends BaseAdapter {
    private Context context;
    public List<ExercisesMode> list;
    private DisplayImageOptions mOption;

    public ExercisesAdapter(Context context, List<ExercisesMode> list) {
        this.context = context;
        this.list = list;
        mOption = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.home_card_image)
                .showImageOnFail(R.drawable.home_card_image)
                .showImageForEmptyUri(R.drawable.home_card_image)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public ExercisesMode getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = new ViewHolder();
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.discovery_exercises_item, null);
            holder.tvStartTime = (TextView) convertView.findViewById(R.id.tvStartTime);
            holder.ivExercises = (ImageView) convertView.findViewById(R.id.iv_exercises_bg);
            holder.tvEndTime = (TextView) convertView.findViewById(R.id.tvEndTime);
            holder.rightTwo = (TextView) convertView.findViewById(R.id.tv_right_two);
            holder.tvExercisesName = (TextView) convertView.findViewById(R.id.tvExercisesName);
            holder.tvDescription = (TextView) convertView.findViewById(R.id.tvDescription);
            holder.tvHallName = (TextView) convertView.findViewById(R.id.tvHallName);

	  convertView.setTag(holder);
         } else {
            holder = (ViewHolder) convertView.getTag();
         }

        holder.tvExercisesName.setText(list.get(position).introduction);
        String str = list.get(position).schemeDesc.replaceAll("<p>","");
        String str1 = str.replaceAll("</p>","");
        holder.tvDescription.setText(str1);
        holder.tvHallName.setText(list.get(position).stgName);
        holder.tvStartTime.setText(DateUtil.TimeFormat(list.get(position).createTime,"MM/dd")+"发布");
        holder.tvEndTime.setText(DateUtil.TimeFormat(list.get(position).startTime,"MM/dd")+"至"+DateUtil.TimeFormat(list.get(position).startTime,"MM/dd")+"有效");

        RelativeLayout.LayoutParams ivParam=(RelativeLayout.LayoutParams)holder.ivExercises.getLayoutParams();
        int screenWidth = context.getResources().getDisplayMetrics().widthPixels;
        ivParam.width = (screenWidth*220)/746;
        ivParam.height =( screenWidth*220)/746;
        holder.ivExercises.setLayoutParams(ivParam);

        try {
            ImageLoader.getInstance().displayImage(list.get(position).imageUrl,holder.ivExercises,mOption);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertView;
    }

    class ViewHolder {
        public ImageView ivExercises = null;
        public TextView tvStartTime = null;
        public TextView tvEndTime = null;
        public TextView rightTwo = null;
        public TextView tvExercisesName = null;
        public TextView tvDescription = null;
        public TextView tvHallName = null;
    }
}
