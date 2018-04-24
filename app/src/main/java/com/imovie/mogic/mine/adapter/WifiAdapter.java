package com.imovie.mogic.mine.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.imovie.mogic.MyApplication;
import com.imovie.mogic.R;
import com.imovie.mogic.mine.attend.AttendClockActivity;
import com.imovie.mogic.mine.attend.model.AttendWay;
import com.imovie.mogic.mine.model.ClockModel;
import com.imovie.mogic.utills.Utills;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.util.List;


/**
 * Created by zhouxinshan on 2016/3/31.
 */
public class WifiAdapter extends BaseAdapter {
    private Context context;
    public List<AttendWay> list;

    public WifiAdapter(Context context, List<AttendWay> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public AttendWay getItem(int position) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.wifi_item, null);
            holder.tvWifiName = (TextView) convertView.findViewById(R.id.tvWifiName);
            holder.tvWifiState = (TextView) convertView.findViewById(R.id.tvWifiState);

	  convertView.setTag(holder);
         } else {
            holder = (ViewHolder) convertView.getTag();
         }

        holder.tvWifiName.setText(list.get(position).name);
        if(MyApplication.getInstance().getWifiName().contains(list.get(position).name)){
            holder.tvWifiState.setText("已连接");
            holder.tvWifiState.setTextColor(context.getResources().getColor(R.color.T7));
        }else{
            holder.tvWifiState.setText("未连接");
            holder.tvWifiState.setTextColor(context.getResources().getColor(R.color.T3));
        }




        return convertView;
    }


    class ViewHolder {
        public TextView tvWifiName;
        public TextView tvWifiState;

    }
}
