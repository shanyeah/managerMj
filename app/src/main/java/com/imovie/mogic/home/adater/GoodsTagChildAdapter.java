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
import com.imovie.mogic.widget.RoundedImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;


/**
 * Created by zhouxinshan on 2016/3/31.
 */
public class GoodsTagChildAdapter extends BaseAdapter {
    private Context context;
    public List<GoodTagList> list;
    private DisplayImageOptions mOption;
    public GoodsTagChildAdapter(Context context, List<GoodTagList> list) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.home_goods_tag_child_item, null);
            holder.rlGoodBackground = (RelativeLayout) convertView.findViewById(R.id.rlGoodBackground);
            holder.ivGoodImge = (RoundedImageView) convertView.findViewById(R.id.ivGoodImge);
            holder.tvTypeName = (TextView) convertView.findViewById(R.id.tvTypeName);
	  convertView.setTag(holder);
         } else {
            holder = (ViewHolder) convertView.getTag();
         }

        holder.tvTypeName.setText(list.get(position).name);

        if(list.get(position).selectId==-1){
            holder.rlGoodBackground.setBackground(context.getResources().getDrawable(R.drawable.home_good_unselect));
        }else{
            holder.rlGoodBackground.setBackground(context.getResources().getDrawable(R.drawable.home_good_select));
        }
        try {
            ImageLoader.getInstance().displayImage(list.get(position).imageUrl,holder.ivGoodImge,mOption);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertView;
    }

    public void setSelectIndex(long selectId){
        for(int i=0;i<list.size();i++){
            if(list.get(i).id == selectId){
                this.list.get(i).selectId = selectId;
            }else{
                this.list.get(i).selectId = -1;
            }
        }
        super.notifyDataSetChanged();
    }

    public void setIndex(){
        for(int i=0;i<list.size();i++){
            list.get(i).id = i;
        }
    }

    class ViewHolder {
        public RelativeLayout rlGoodBackground;
        public RoundedImageView ivGoodImge;
        public TextView tvTypeName = null;
        public View viewType = null;
    }
}
