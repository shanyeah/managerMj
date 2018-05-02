package com.imovie.mogic.home.adater;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.imovie.mogic.R;
import com.imovie.mogic.home.model.GoodTagList;
import com.imovie.mogic.home.model.GoodTypeModel;

import java.util.List;


/**
 * Created by zhouxinshan on 2016/3/31.
 */
public class GoodsTagAdapter extends BaseAdapter {
    private Context context;
    public List<GoodTagList> list;
    public GoodsTagAdapter(Context context, List<GoodTagList> list) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.home_goods_tag_item, null);
//            holder.rlNameState = (RelativeLayout) convertView.findViewById(R.id.rlNameState);
            holder.tvTypeName = (TextView) convertView.findViewById(R.id.tvTypeName);
	  convertView.setTag(holder);
         } else {
            holder = (ViewHolder) convertView.getTag();
         }

        holder.tvTypeName.setText(list.get(position).name);
//        if(list.get(position).isSelect){
////            holder.viewType.setVisibility(View.VISIBLE);
//            holder.rlNameState.setBackgroundResource(R.color.BG5);
//        }else{
////            holder.viewType.setVisibility(View.GONE);
//            holder.rlNameState.setBackgroundResource(R.color.BG3);
//        }
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

    class ViewHolder {
        public RelativeLayout rlNameState;
        public TextView tvTypeName = null;
        public View viewType = null;
    }
}
