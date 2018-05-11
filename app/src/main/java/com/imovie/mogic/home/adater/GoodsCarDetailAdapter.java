package com.imovie.mogic.home.adater;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.imovie.mogic.R;
import com.imovie.mogic.car.bean.FoodBean;
import com.imovie.mogic.car.bean.FoodTagList;
import com.imovie.mogic.home.model.GoodTagList;
import com.imovie.mogic.home.model.GoodsModel;
import com.imovie.mogic.utills.DecimalUtil;
import com.imovie.mogic.widget.NoScrollListView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by zhouxinshan on 2016/3/31.
 */
public class GoodsCarDetailAdapter extends BaseAdapter {
    private Context context;
    public List<GoodsModel> list;
    public GoodsCarDetailAdapter(Context context, List<GoodsModel> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public GoodsModel getItem(int position) {
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

        holder.tvTypeName.setText(list.get(position).goodsName);
        holder.tvGoodMoney.setText(context.getResources().getString(R.string.symbol_of_RMB) + DecimalUtil.FormatMoney(list.get(position).payAmount));
        holder.tvGoodNum.setText("x"+list.get(position).quantity);
        if(list.get(position).childGoodsList.size()>0){
            GoodsPayDetailAdapter adapter = new GoodsPayDetailAdapter(context,list.get(position).childGoodsList);
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

    public List<FoodTagList> getGoodsPackList(List<GoodTagList> packList) {
        List<FoodTagList> list = new ArrayList<>();
        for (GoodTagList tag : packList) {
            FoodTagList foodTag = new FoodTagList();
            foodTag.setId(tag.id);
            foodTag.setGoodsId(tag.goodsId);
            foodTag.setName(tag.name);
            foodTag.setPackPrice(tag.packPrice);
            foodTag.setPrice(tag.price);
            foodTag.setQuantity(tag.quantity);
            foodTag.setGoodsTags(tag.goodsTags);
            foodTag.setImageUrl(tag.imageUrl);
            foodTag.setPackGroupId(tag.packGroupId);
            foodTag.setSelectId(tag.selectId);
            list.add(foodTag);
        }
        return list;
    }
}
