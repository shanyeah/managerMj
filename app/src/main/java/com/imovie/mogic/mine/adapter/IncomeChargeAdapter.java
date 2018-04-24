package com.imovie.mogic.mine.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.imovie.mogic.R;
import com.imovie.mogic.config.HTTPConfig;
import com.imovie.mogic.mine.model.ChargeInfo;
import com.imovie.mogic.mine.model.ChargeInfoModel;
import com.imovie.mogic.myRank.adater.OrderRecordAdapter;
import com.imovie.mogic.utills.DateUtil;
import com.imovie.mogic.utills.DecimalUtil;
import com.imovie.mogic.utills.QRCodeUtil;

import java.util.List;


/**
 * Created by zhouxinshan on 2016/3/31.
 */
public class IncomeChargeAdapter extends BaseAdapter {
    private Context context;
    public List<ChargeInfo> list;
    private int fromType;

    public IncomeChargeAdapter(Context context, List<ChargeInfo> list,int fromType) {
        this.context = context;
        this.list = list;
        this.fromType  = fromType;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public ChargeInfo getItem(int position) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.income_charge_item, null);
            holder.tvCreateTime = (TextView) convertView.findViewById(R.id.tvCreateTime);
            holder.tvRewardAmount= (TextView) convertView.findViewById(R.id.tvRewardAmount);
            holder.tvPriceSum= (TextView) convertView.findViewById(R.id.tvPriceSum);
            holder.tvOrderState= (TextView) convertView.findViewById(R.id.tvOrderState);
            holder.tvPriceState= (TextView) convertView.findViewById(R.id.tvPriceState);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }

        if(fromType == 21){
            holder.tvOrderState.setText("充值时间");
            holder.tvPriceState.setText("充值金额:");
            holder.tvPriceSum.setText(Html.fromHtml("<font color='#fd5c02' size=14>"+ DecimalUtil.FormatMoney(list.get(position).payAmount/100) +"</font><font color=\'#7c7c7d\' size=14>元</font>"));

        }else if(fromType == 22){
            holder.tvOrderState.setText("点赞时间");
            holder.tvPriceState.setText("被赞人:");
            holder.tvPriceSum.setText(list.get(position).wxNickName);

        }else if(fromType == 23){
            holder.tvOrderState.setText("点餐时间");
            holder.tvPriceState.setText("点餐金额:");
            holder.tvPriceSum.setText(Html.fromHtml("<font color='#fd5c02' size=14>"+ DecimalUtil.FormatMoney(list.get(position).payAmount/100) +"</font><font color=\'#7c7c7d\' size=14>元</font>"));
        }

        holder.tvCreateTime.setText(list.get(position).createTime);
        holder.tvRewardAmount.setText(Html.fromHtml("<font color='#fd5c02' size=14>"+ DecimalUtil.FormatMoney(list.get(position).rewardAmount/100) +"</font><font color=\'#7c7c7d\' size=14>元</font>"));

        return convertView;
    }

    class ViewHolder {
        public TextView tvCreateTime = null;
        public TextView tvRewardAmount;
        public TextView tvPriceSum;
        public TextView tvOrderState;
        public TextView tvPriceState;
    }
}
