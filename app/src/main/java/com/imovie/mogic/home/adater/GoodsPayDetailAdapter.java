package com.imovie.mogic.home.adater;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.imovie.mogic.R;
import com.imovie.mogic.car.bean.FoodTagList;
import com.imovie.mogic.home.model.GoodTagList;
import com.imovie.mogic.utills.StringHelper;

import java.util.List;


/**
 * Created by zhouxinshan on 2016/3/31.
 */
public class GoodsPayDetailAdapter extends BaseAdapter {
    private Context context;
    public List<GoodTagList> list;
    public GoodsPayDetailAdapter(Context context, List<GoodTagList> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public GoodTagList getItem(int position) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.home_pay_tag_item, null);
            holder.tvGoodNum = (TextView) convertView.findViewById(R.id.tvGoodNum);
            holder.tvTypeName = (TextView) convertView.findViewById(R.id.tvTypeName);
            holder.car_tag_name = (TextView) convertView.findViewById(R.id.car_tag_name);
	  convertView.setTag(holder);
         } else {
            holder = (ViewHolder) convertView.getTag();
         }

        holder.tvTypeName.setText(list.get(position).goodsName);
        holder.tvGoodNum.setText("x"+list.get(position).quantity);
//        if(list.get(position).isSelect){
////            holder.viewType.setVisibility(View.VISIBLE);
//            holder.rlNameState.setBackgroundResource(R.color.BG5);
//        }else{
////            holder.viewType.setVisibility(View.GONE);
//            holder.rlNameState.setBackgroundResource(R.color.BG3);
//        }
        if(!StringHelper.isEmpty(list.get(position).goodsTags)) {
            holder.car_tag_name.setVisibility(View.VISIBLE);
            holder.car_tag_name.setText("(" + list.get(position).goodsTags + ")");
        }else{
            holder.car_tag_name.setVisibility(View.GONE);
        }
        return convertView;
    }

//    public void setSelectIndex(int position){
//        for(int i=0;i<list.size();i++){
//            if(i==position){
//                list.get(i).isSelect = true;
//            }else{
//                list.get(i).isSelect = false;
//            }
//        }
//        super.notifyDataSetChanged();
//    }

    class ViewHolder {
        public TextView tvGoodNum;
        public TextView tvTypeName = null;
        public TextView car_tag_name;
        public View viewType = null;
    }
}
