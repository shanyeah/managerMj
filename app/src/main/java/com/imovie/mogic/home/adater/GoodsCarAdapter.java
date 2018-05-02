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
import com.imovie.mogic.car.bean.FoodBean;
import com.imovie.mogic.home.model.CardModel;
import com.imovie.mogic.home.model.GoodTypeModel;
import com.imovie.mogic.widget.NoScrollListView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;


/**
 * Created by zhouxinshan on 2016/3/31.
 */
public class GoodsCarAdapter extends BaseAdapter {
    private Context context;
    public List<FoodBean> list;
    public GoodsCarAdapter(Context context, List<FoodBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public FoodBean getItem(int position) {
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
            holder.tvTypeName = (TextView) convertView.findViewById(R.id.tvTypeName);
            holder.tvGoodNum = (TextView) convertView.findViewById(R.id.tvGoodNum);
            holder.tvGoodMoney = (TextView) convertView.findViewById(R.id.tvGoodMoney);
            holder.lvCarTagList = (NoScrollListView) convertView.findViewById(R.id.lvCarTagList);
	  convertView.setTag(holder);
         } else {
            holder = (ViewHolder) convertView.getTag();
         }

        holder.tvTypeName.setText(list.get(position).getName());
        holder.tvGoodMoney.setText(context.getResources().getString(R.string.symbol_of_RMB) + list.get(position).getPrice());
        holder.tvGoodNum.setText("x"+list.get(position).getSelectCount());
        if(list.get(position).goodsPackList.size()>0){
            GoodsPayTagAdapter adapter = new GoodsPayTagAdapter(context,list.get(position).goodsPackList);
            holder.lvCarTagList.setAdapter(adapter);
        }
//        if(list.get(position).isSelect){
//            holder.viewType.setVisibility(View.VISIBLE);
//            holder.rlNameState.setBackgroundResource(R.color.BG15);
//        }else{
//            holder.viewType.setVisibility(View.GONE);
//            holder.rlNameState.setBackgroundResource(R.color.BG3);
//        }
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

//    public void setSelectCategory(String category){
//        for(int i=0;i<list.size();i++){
//            if(list.get(i).name.equals(category)){
//                list.get(i).isSelect = true;
//            }else{
//                list.get(i).isSelect = false;
//            }
//        }
//        super.notifyDataSetChanged();
//    }

    class ViewHolder {
        public TextView tvTypeName = null;
        public TextView tvGoodMoney = null;
        public TextView tvGoodNum = null;
        public NoScrollListView lvCarTagList;
    }
}
