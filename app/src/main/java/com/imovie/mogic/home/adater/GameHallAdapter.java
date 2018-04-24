package com.imovie.mogic.home.adater;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.imovie.mogic.MyApplication;
import com.imovie.mogic.R;
import com.imovie.mogic.home.model.GameHall;
import com.imovie.mogic.utills.Utills;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;


/**
 * Created by zhouxinshan on 2016/3/31.
 */
public class GameHallAdapter extends BaseAdapter {
    private Context context;
    public List<GameHall> list;
    private DisplayImageOptions mOption;

    public GameHallAdapter(Context context, List<GameHall> list) {
        this.context = context;
        this.list = list;
        mOption = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.home_ad_default)
                .showImageOnFail(R.drawable.home_ad_default)
                .showImageForEmptyUri(R.drawable.home_ad_default)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public GameHall getItem(int position) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.home_game_hall_item, null);
            holder.ivHallItem = (ImageView) convertView.findViewById(R.id.iv_hall_item);
            holder.name = (TextView) convertView.findViewById(R.id.tv_item_hall_name);
            holder.sorce = (TextView) convertView.findViewById(R.id.tv_item_sorce);
            holder.tvItemTime = (TextView) convertView.findViewById(R.id.tvItemTime);
            holder.tvItemSeat = (TextView) convertView.findViewById(R.id.tvItemSeat);
            holder.tvItemExercise = (TextView) convertView.findViewById(R.id.tvItemExercise);
            holder.tvItemMission = (TextView) convertView.findViewById(R.id.tvItemMission);
            holder.tvItemChange = (TextView) convertView.findViewById(R.id.tvItemChange);
            holder.tvItemTicket = (TextView) convertView.findViewById(R.id.tvItemTicket);
            holder.tvItemDistance = (TextView) convertView.findViewById(R.id.tvItemDistance);
            holder.ivSelectBox = (ImageView) convertView.findViewById(R.id.ivSelectBox);
            convertView.setTag(holder);
    } else {
        holder = (ViewHolder) convertView.getTag();
    }

        holder.sorce.setText(getItem(position).rating+"分");
        holder.name.setText(getItem(position).name);
        holder.tvItemDistance.setText(getItem(position).distance+"km");
        holder.tvItemTime.setText(getItem(position).openTime);
        holder.ivSelectBox.setTag(getItem(position).id);
        if(getItem(position).seatFlag==0){
            holder.tvItemSeat.setVisibility(View.GONE);
        }else{
            holder.tvItemSeat.setVisibility(View.VISIBLE);
        }

        if(getItem(position).activeFlag==0){
            holder.tvItemExercise.setVisibility(View.GONE);
        }else{
            holder.tvItemExercise.setVisibility(View.VISIBLE);
        }

        if(getItem(position).taskFlag==0){
            holder.tvItemMission.setVisibility(View.GONE);
        }else{
            holder.tvItemMission.setVisibility(View.VISIBLE);
        }

        if(getItem(position).changeFlag==0){
            holder.tvItemChange.setVisibility(View.GONE);
        }else{
            holder.tvItemChange.setVisibility(View.VISIBLE);
        }
        if(getItem(position).movieFlag==0){
            holder.tvItemTicket.setVisibility(View.GONE);
        }else{
            holder.tvItemTicket.setVisibility(View.VISIBLE);
        }

        if(getItem(position).images.size()>0){
            ImageLoader.getInstance().displayImage(list.get(position).images.get(0).imageUrl,holder.ivHallItem,mOption);
        }

        RelativeLayout.LayoutParams ivParam=(RelativeLayout.LayoutParams)holder.ivHallItem.getLayoutParams();
        int screenWidth = context.getResources().getDisplayMetrics().widthPixels;
        ivParam.height=(screenWidth*280)/640;
        holder.ivHallItem.setLayoutParams(ivParam);
        return convertView;
    }

    class ViewHolder {
        public ImageView ivHallItem = null;
        public ImageView ivSelectBox;
        public TextView name = null;
        public TextView sorce = null;
        public TextView tvItemTime = null;
        public TextView tvItemSeat = null;
        public TextView tvItemExercise = null;
        public TextView tvItemMission = null;
        public TextView tvItemChange = null;
        public TextView tvItemTicket = null;
        public TextView tvItemDistance;
    }
}
