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
import com.imovie.mogic.home.MainActivity;
import com.imovie.mogic.home.model.CardModel;
import com.imovie.mogic.home.model.GoodsModel;
import com.imovie.mogic.utills.DecimalUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;


/**
 * Created by zhouxinshan on 2016/3/31.
 */
public class GoodsAdapter extends BaseAdapter {
    private Context context;
    public List<GoodsModel> list;
    private DisplayImageOptions mOption;

    public interface onSelectListener {
        void onSelect(GoodsModel goodsModel,View v);
    }
    private onSelectListener selectListener;
    public void setOnSelectListener(onSelectListener listener) {
        selectListener = listener;
    }

    public GoodsAdapter(Context context, List<GoodsModel> list) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.home_goods_item, null);
            holder.tvGoodName = (TextView) convertView.findViewById(R.id.tvGoodName);
            holder.ivCard = (ImageView) convertView.findViewById(R.id.iv_card_item);
            holder.ivAddGoods = (ImageView) convertView.findViewById(R.id.ivAddGoods);
            holder.tvGoodPrice = (TextView) convertView.findViewById(R.id.tvGoodPrice);
            holder.tvCategoryName = (TextView) convertView.findViewById(R.id.tvCategoryName);
            holder.viewType = (View) convertView.findViewById(R.id.viewType);
	  convertView.setTag(holder);
         } else {
            holder = (ViewHolder) convertView.getTag();
         }

        holder.tvGoodName.setText(list.get(position).name);
        holder.tvGoodPrice.setText(DecimalUtil.FormatMoney(list.get(position).price/100) + context.getResources().getString(R.string.symbol_RMB));
        holder.ivAddGoods.setTag(list.get(position));
        if(list.get(position).isShow) {
            holder.tvCategoryName.setVisibility(View.VISIBLE);
            holder.viewType.setVisibility(View.VISIBLE);
            holder.tvCategoryName.setText(list.get(position).category);
        }else{
            holder.tvCategoryName.setVisibility(View.GONE);
            holder.viewType.setVisibility(View.GONE);
        }

//        RelativeLayout.LayoutParams ivParam=(RelativeLayout.LayoutParams)holder.ivCard.getLayoutParams();
//        int screenWidth = context.getResources().getDisplayMetrics().widthPixels;
//        ivParam.width = (screenWidth*572)/746;
//        ivParam.height =( screenWidth*320)/746;
//        holder.ivCard.setLayoutParams(ivParam);

        holder.ivAddGoods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                int count = list.get(position).getCount();
//                count++;
//                list.get(position).setCount(count);
//                viewholder.et_acount.setVisibility(View.VISIBLE);
//                viewholder.iv_remove.setVisibility(View.VISIBLE);
//                viewholder.et_acount.setText(list.get(position).getCount() + "");
//                catograyAdapter.notifyDataSetChanged();

//                int[] startLocation = new int[2];// 一个整型数组，用来存储按钮的在屏幕的X、Y坐标
//                v.getLocationInWindow(startLocation);// 这是获取购买按钮的在屏幕的X、Y坐标（这也是动画开始的坐标）
//                ImageView ball = new ImageView(context);// buyImg是动画的图片，我的是一个小球（R.drawable.sign）
//                ball.setImageResource(R.mipmap.ic_launcher);// 设置buyImg的图片
//                ((MainActivity)context).setAnim(ball, startLocation);// 开始执行动画
                GoodsModel goodsModel = (GoodsModel) v.getTag();
                selectListener.onSelect(goodsModel,v);
            }
        });

        try {
            ImageLoader.getInstance().displayImage(list.get(position).imageUrl,holder.ivCard,mOption);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertView;
    }

    public class ViewHolder {
        public ImageView ivCard = null;
        public ImageView ivAddGoods;
        public TextView tvGoodName = null;
        public TextView tvGoodPrice = null;
        public TextView tvCategoryName;
        public View viewType;
    }
}
