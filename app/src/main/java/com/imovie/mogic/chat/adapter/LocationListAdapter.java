package com.imovie.mogic.chat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mapapi.search.core.PoiInfo;
import com.imovie.mogic.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.util.List;


/**
 * Created by zhouxinshan on 2016/3/31.
 */
public class LocationListAdapter extends BaseAdapter {
    private Context context;
    public List<PoiInfo> list;
    private DisplayImageOptions mOption;
    public int checkPos = -1;

    public LocationListAdapter(Context context, List<PoiInfo> list) {
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
    public PoiInfo getItem(int position) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.chat_location_list_item, null);
            holder.title = (TextView) convertView.findViewById(R.id.title);
            holder.address = (TextView) convertView.findViewById(R.id.address);
            holder.selected = (ImageView) convertView.findViewById(R.id.selected);


	  convertView.setTag(holder);
         } else {
            holder = (ViewHolder) convertView.getTag();
         }

        holder.title.setText(list.get(position).name);
        holder.address.setText(list.get(position).address);
        if(checkPos==position){
            holder.selected.setVisibility(View.VISIBLE);
        } else {
            holder.selected.setVisibility(View.GONE);
        }

        return convertView;
    }


    class ViewHolder {
        public TextView title = null;
        public TextView address = null;
        public ImageView selected = null;
    }
}
