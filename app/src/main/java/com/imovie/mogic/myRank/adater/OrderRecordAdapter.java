package com.imovie.mogic.myRank.adater;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.imovie.mogic.R;
import com.imovie.mogic.myRank.model.OrderRecord;
import com.imovie.mogic.utills.DecimalUtil;

import java.util.List;


/**
 * Created by zhouxinshan on 2016/3/31.
 */
public class OrderRecordAdapter extends BaseAdapter {
    private Context context;
    public List<OrderRecord.Record> list;

    public OrderRecordAdapter(Context context, List<OrderRecord.Record> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public OrderRecord.Record getItem(int position) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.my_order_record_item, null);
            holder.tvNickName = (TextView) convertView.findViewById(R.id.tvNickName);
            holder.tvOrderId = (TextView) convertView.findViewById(R.id.tvOrderId);
            holder.tvCreateTime = (TextView) convertView.findViewById(R.id.tvCreateTime);
            holder.tvMemberId = (TextView) convertView.findViewById(R.id.tvMemberId);
            holder.tvPayStatus = (TextView) convertView.findViewById(R.id.tvPayStatus);
            holder.tvRewardAmount= (TextView) convertView.findViewById(R.id.tvRewardAmount);
            holder.tvPriceSum= (TextView) convertView.findViewById(R.id.tvPriceSum);
	        convertView.setTag(holder);
         } else {
            holder = (ViewHolder) convertView.getTag();
         }

        holder.tvNickName.setText(list.get(position).detail.name);
        holder.tvOrderId.setText("" + list.get(position).billId);
        holder.tvCreateTime.setText(list.get(position).createTime);
        holder.tvRewardAmount.setText(Html.fromHtml("<font color='#fd5c02' size=14>"+ DecimalUtil.FormatMoney(list.get(position).rewardAmount/100) +"</font><font color=\'#7c7c7d\' size=14>元</font>"));
        holder.tvPriceSum.setText(Html.fromHtml("<font color='#fd5c02' size=14>"+ DecimalUtil.FormatMoney(list.get(position).payAmount/100) +"</font><font color=\'#7c7c7d\' size=14>元</font>"));
        holder.tvMemberId.setText("(" + list.get(position).detail.idNumber + ")");

        switch (list.get(position).status){
            case 0:
                holder.tvPayStatus.setText("待支付");
                break;
            case 1:
                holder.tvPayStatus.setText("待支付");
                break;
            case 2:
                holder.tvPayStatus.setText("已支付");
                break;
            case 3:
                holder.tvPayStatus.setText("订单取消");
                break;
            default:
                break;

        }




//        holder.tvCreateTime.setText("在"+DateUtil.TimeFormat(list.get(position).createTime,"yyyy/MM/dd")+"日上机后点评");
//        RelativeLayout.LayoutParams ivParam=(RelativeLayout.LayoutParams)holder.ivExercises.getLayoutParams();
//        int screenWidth = context.getResources().getDisplayMetrics().widthPixels;
//        ivParam.width = (screenWidth*220)/746;
//        ivParam.height =( screenWidth*220)/746;
//        holder.ivExercises.setLayoutParams(ivParam);

//        try {
//            ImageLoader.getInstance().displayImage(list.get(position).fackeImageUrl,holder.ivPhotoImage,mOption);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        return convertView;
    }

    class ViewHolder {
        public TextView tvOrderId = null;
        public TextView tvCreateTime = null;
        public TextView tvNickName = null;
        public TextView tvMemberId;
        public TextView tvPayStatus;
        public TextView tvRewardAmount;
        public TextView tvPriceSum;
    }
}
