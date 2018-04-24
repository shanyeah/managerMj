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
import com.imovie.mogic.home.model.CardModel;
import com.imovie.mogic.home.model.GoodTypeModel;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;


/**
 * Created by zhouxinshan on 2016/3/31.
 */
public class GoodsTypeAdapter extends BaseAdapter {
    private Context context;
    public List<GoodTypeModel> list;
    public GoodsTypeAdapter(Context context, List<GoodTypeModel> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public GoodTypeModel getItem(int position) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.home_goods_type_item, null);
            holder.rlNameState = (RelativeLayout) convertView.findViewById(R.id.rlNameState);
            holder.tvTypeName = (TextView) convertView.findViewById(R.id.tvTypeName);
            holder.viewType = (View) convertView.findViewById(R.id.viewType);
	  convertView.setTag(holder);
         } else {
            holder = (ViewHolder) convertView.getTag();
         }

        holder.tvTypeName.setText(list.get(position).name);
        if(list.get(position).isSelect){
            holder.viewType.setVisibility(View.VISIBLE);
            holder.rlNameState.setBackgroundResource(R.color.BG15);
        }else{
            holder.viewType.setVisibility(View.GONE);
            holder.rlNameState.setBackgroundResource(R.color.BG3);
        }
        return convertView;
    }

    public void setSelectIndex(int position){
        for(int i=0;i<list.size();i++){
            if(i==position){
                list.get(i).isSelect = true;
            }else{
                list.get(i).isSelect = false;
            }
        }
        super.notifyDataSetChanged();
    }

    public void setSelectCategory(String category){
        for(int i=0;i<list.size();i++){
            if(list.get(i).name.equals(category)){
                list.get(i).isSelect = true;
            }else{
                list.get(i).isSelect = false;
            }
        }
        super.notifyDataSetChanged();
    }

    class ViewHolder {
        public RelativeLayout rlNameState;
        public TextView tvTypeName = null;
        public View viewType = null;
    }
}
