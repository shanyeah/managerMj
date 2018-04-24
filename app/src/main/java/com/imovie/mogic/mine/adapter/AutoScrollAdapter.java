package com.imovie.mogic.mine.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.baidu.platform.comapi.map.D;
import com.imovie.mogic.R;
import com.imovie.mogic.mine.model.AutoModel;
import com.imovie.mogic.utills.DateUtil;
import com.imovie.mogic.utills.DecimalUtil;
import com.imovie.mogic.utills.Utills;
import com.imovie.mogic.widget.AutoScrollListView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AutoScrollAdapter extends BaseAdapter implements AutoScrollListView.AutoScroll {

	public List<AutoModel> list = new ArrayList<>();
	private Context mContext;
	private DisplayImageOptions mOption;

	public AutoScrollAdapter(Context context,List<AutoModel> list) {
		this.list = list;
		this.mContext = context;
		mOption = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.home_card_image)
				.showImageOnFail(R.drawable.home_card_image)
				.showImageForEmptyUri(R.drawable.home_card_image)
				.cacheInMemory(true)
				.cacheOnDisk(true)
				.build();
	}

	//用于设置随机颜色
	private Random random = new Random();
	//布局管理器
	private LayoutInflater mLayoutInflater;
	@Override
	public int getCount() {
		return list.size();  //返回的最多的数据
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder viewHolder;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.auto_list_item, null);
			viewHolder = new ViewHolder();
			viewHolder.tvOrderId = (TextView) convertView.findViewById(R.id.tvOrderId);
			viewHolder.tvOrderNo = (TextView) convertView.findViewById(R.id.tvOrderNo);
//			viewHolder.ivImage = (ImageView) convertView.findViewById(R.id.iv_content);
			viewHolder.tvOrderTime = (TextView) convertView.findViewById(R.id.tvOrderTime);
			viewHolder.tvPaySum = (TextView) convertView.findViewById(R.id.tvPaySum);
			viewHolder.tvPayType = (TextView) convertView.findViewById(R.id.tvPayType);
			viewHolder.tvSend = (TextView) convertView.findViewById(R.id.tvSend);
			viewHolder.tvGoodDetail = (TextView) convertView.findViewById(R.id.tvGoodDetail);
			convertView.setTag(viewHolder);
		}else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		if(DateUtil.getCurrentTime()-DateUtil.StringToLongDate(list.get(position).createTime,"yyyy-MM-dd HH:mm:ss")<300000){
			convertView.setBackgroundColor(Color.argb(100, random.nextInt(255), random.nextInt(255), random.nextInt(255)));
		}else{
//			convertView.setBackgroundColor(Color.argb(100, random.nextInt(255), random.nextInt(255), random.nextInt(255)));
			convertView.setBackgroundColor(Color.argb(100, 255, 255, 255));
		}
		viewHolder.tvOrderId.setText(""+list.get(position).orderId);
		viewHolder.tvOrderNo.setText(list.get(position).seatName);
		viewHolder.tvOrderTime.setText(list.get(position).createTime);
		viewHolder.tvPaySum.setText(Html.fromHtml("<font color='#fd5c02' size=22>"+ DecimalUtil.FormatMoney(list.get(position).payAmount/100) +"</font><font color=\'#565a5c\' size=22>元</font>"));
		viewHolder.tvPayType.setText(list.get(position).payTypeName);
		viewHolder.tvSend.setText(list.get(position).statusName);
		viewHolder.tvGoodDetail.setText(list.get(position).detail);
		//		Glide.with(mContext).load(marqueeBean.getImgurl()).transform(new GlideCircleTransform(mContext)).into(viewHolder.ivImage);
//		try {
//			ImageLoader.getInstance().displayImage(list.get(position).imageUrl,viewHolder.ivImage,mOption);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		return convertView;
	}

	//获取到当前滚动视图的高度
	@Override
	public int getListItemHeight(Context context) {
		//在这里我们要获取到我们的布局的高度  才能实现一些
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, context.getResources().getDisplayMetrics());
	}


	@Override
	public int getVisiableCount() {
//		if(list.size()>=3){
//			return 3;
//		}else{
//			return list.size();  //显示滚动的item 的个数
//		}
		return list.size();
//		return 3;
	}

	public void setOrderId(){
		for(int i=0;i<list.size();i++){
			list.get(i).orderId = i+1;
		}
	}

	class ViewHolder{
//		public ImageView ivImage;
		public TextView tvOrderId;
		public TextView tvOrderNo;
		public TextView tvOrderTime;
		public TextView tvPaySum;
		public TextView tvPayType;
		public TextView tvSend;
		public TextView tvGoodDetail;
	}
	
}
