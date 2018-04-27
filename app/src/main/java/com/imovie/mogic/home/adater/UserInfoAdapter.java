package com.imovie.mogic.home.adater;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.imovie.mogic.R;
import com.imovie.mogic.home.model.GoodTagList;
import com.imovie.mogic.home.model.SearchUserModel;
import com.imovie.mogic.home.widget.SearchUserPopWindow;
import com.imovie.mogic.utills.DecimalUtil;

import java.util.List;


/**
 * Created by zhouxinshan on 2016/3/31.
 */
public class UserInfoAdapter extends BaseAdapter {
    private Context context;
    public List<SearchUserModel> list;
    public UserInfoAdapter(Context context, List<SearchUserModel> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public SearchUserModel getItem(int position) {
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
            convertView=LayoutInflater.from(context).inflate(R.layout.search_item_layout, null);
            holder.tvName=(TextView) convertView.findViewById(R.id.tv_name);
            holder.tvNumber=(TextView) convertView.findViewById(R.id.tvNumber);
            holder.tvBalance=(TextView) convertView.findViewById(R.id.tvBalance);
            holder.tvCashBalance=(TextView) convertView.findViewById(R.id.tvCashBalance);
            holder.tvPresentBalance=(TextView) convertView.findViewById(R.id.tvPresentBalance);
            convertView.setTag(holder);
         } else {
            holder = (ViewHolder) convertView.getTag();
         }

        SearchUserModel internetBarModel = (SearchUserModel)getItem(position);
        holder.tvName.setText("会员："+internetBarModel.name);
        holder.tvNumber.setText("证件号：" + internetBarModel.idNumber);
        holder.tvBalance.setText(Html.fromHtml("<font color='#565a5c' size=14>余额:</font><font color=\'#fd5c02\' size=14>"+ DecimalUtil.FormatMoney(internetBarModel.balance) +"</font><font color=\'#565a5c\' size=14>"+context.getResources().getString(R.string.symbol_RMB)+"</font>"));
        holder.tvCashBalance.setText(Html.fromHtml("<font color='#565a5c' size=14>充值:</font><font color=\'#fd5c02\' size=14>"+ DecimalUtil.FormatMoney(internetBarModel.cashBalance) +"</font><font color=\'#565a5c\' size=14>"+context.getResources().getString(R.string.symbol_RMB)+"</font>"));
        holder.tvPresentBalance.setText(Html.fromHtml("<font color='#565a5c' size=14>赠送:</font><font color=\'#fd5c02\' size=14>"+ DecimalUtil.FormatMoney(internetBarModel.presentBalance) +"</font><font color=\'#565a5c\' size=14>"+context.getResources().getString(R.string.symbol_RMB)+"</font>"));
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

    class ViewHolder {
        private TextView tvName;
        private TextView tvNumber;
        private TextView tvBalance;
        private TextView tvCashBalance;
        private TextView tvPresentBalance;
    }
}
