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
import com.imovie.mogic.home.model.GoodTagList;
import com.imovie.mogic.home.model.GoodTypeModel;
import com.imovie.mogic.widget.RoundedImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;


/**
 * Created by zhouxinshan on 2016/3/31.
 */
public class GoodsTagAdapter extends BaseAdapter {
    private Context context;
    public List<GoodTagList> list;
    private DisplayImageOptions mOption;
    public GoodsTagAdapter(Context context, List<GoodTagList> list) {
        this.context = context;
        this.list = list;
        mOption = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.food0)
                .showImageOnFail(R.drawable.food0)
                .showImageForEmptyUri(R.drawable.food0)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();

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
            holder.rlGoodBackground = (RelativeLayout) convertView.findViewById(R.id.rlGoodBackground);
            holder.ivGoodImge = (ImageView) convertView.findViewById(R.id.ivGoodImge);
            holder.tvTypeName = (TextView) convertView.findViewById(R.id.tvTypeName);
            holder.tvChangeText = (TextView) convertView.findViewById(R.id.tvChangeText);
	  convertView.setTag(holder);
         } else {
            holder = (ViewHolder) convertView.getTag();
         }

        holder.tvTypeName.setText(list.get(position).name);

        if(list.get(position).selectId==-1){
            holder.rlGoodBackground.setBackground(context.getResources().getDrawable(R.drawable.shape_write_r5_l5));
        }else{
            holder.rlGoodBackground.setBackground(context.getResources().getDrawable(R.drawable.bg_line_l7_r3));
        }

        if(list.get(position).hasChild){
            holder.tvChangeText.setVisibility(View.VISIBLE);
        }else{
            holder.tvChangeText.setVisibility(View.GONE);
        }


        try {
            ImageLoader.getInstance().displayImage(list.get(position).imageUrl,holder.ivGoodImge,mOption);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertView;
    }

    public void setSelectIndex(int selectId){
        for(int i=0;i<list.size();i++){
            if(i == selectId){
                this.list.get(i).selectId = list.get(i).goodsId;
            }else{
                this.list.get(i).selectId = -1;
            }
        }
        super.notifyDataSetChanged();
    }
    public void setSelectItem(int selectId,GoodTagList tag){

        for(int i=0;i<list.size();i++){
            if(i == selectId){
                list.get(i).id = tag.id;
                list.get(i).createTime = tag.createTime;
                list.get(i).isSelect = tag.isSelect;
                list.get(i).goodsId = tag.goodsId;
                list.get(i).name = tag.name;
                list.get(i).packPrice = tag.packPrice;
                list.get(i).price = tag.price;
                list.get(i).quantity = tag.quantity;
                list.get(i).goodsTags = tag.goodsTags;
                list.get(i).imageUrl = tag.imageUrl;
                list.get(i).packGroupId = tag.packGroupId;
            }
        }

        super.notifyDataSetChanged();
    }
    class ViewHolder {
        public RelativeLayout rlGoodBackground;
        public TextView tvTypeName = null;
        public ImageView ivGoodImge;
        public View viewType = null;
        public TextView tvChangeText;
    }
}
