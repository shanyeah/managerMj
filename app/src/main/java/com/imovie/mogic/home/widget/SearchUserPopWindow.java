package com.imovie.mogic.home.widget;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.imovie.mogic.R;
import com.imovie.mogic.home.model.SearchUserModel;
import com.imovie.mogic.utills.DecimalUtil;

import java.util.List;

/**
 * Created by $zhou on 2017/3/29 0029.
 */

public class SearchUserPopWindow<T> extends PopupWindow {
    private LayoutInflater inflater;
    private ListView mListView;
    public List<T> list;
    public MyAdapter  mAdapter;
    public Context context;

    public SearchUserPopWindow(Context context, List<T> list, AdapterView.OnItemClickListener clickListener) {
        super(context);
        this.context = context;
        inflater=LayoutInflater.from(context);
        this.list=list;
        init(clickListener);
    }

    private void init(AdapterView.OnItemClickListener clickListener){
        View view = inflater.inflate(R.layout.search_window_layout, null);
        setContentView(view);
        setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setFocusable(true);
        ColorDrawable dw = new ColorDrawable(0x00);
        setBackgroundDrawable(dw);
        mListView = (ListView) view.findViewById(R.id.listview);
        mAdapter=new MyAdapter();
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(clickListener);
    }

    public void refreshData(List<T> list){
        this.list=list;
        mAdapter.notifyDataSetChanged();

    }

    private class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder=null;
            if(convertView==null){
                holder=new ViewHolder();
                convertView=inflater.inflate(R.layout.search_item_layout, null);
                holder.tvName=(TextView) convertView.findViewById(R.id.tv_name);
                holder.tvNumber=(TextView) convertView.findViewById(R.id.tvNumber);
                holder.tvBalance=(TextView) convertView.findViewById(R.id.tvBalance);
                holder.tvCashBalance=(TextView) convertView.findViewById(R.id.tvCashBalance);
                holder.tvPresentBalance=(TextView) convertView.findViewById(R.id.tvPresentBalance);
                convertView.setTag(holder);
            }else{
                holder=(ViewHolder) convertView.getTag();
            }
            SearchUserModel internetBarModel = (SearchUserModel)getItem(position);
            holder.tvName.setText(internetBarModel.name);
            holder.tvNumber.setText("(" + internetBarModel.idNumber + ")");
            holder.tvBalance.setText("余额:" + DecimalUtil.FormatMoney(internetBarModel.balance/100) + context.getResources().getString(R.string.symbol_RMB));
            holder.tvCashBalance.setText("充值:" + DecimalUtil.FormatMoney(internetBarModel.cashBalance/100) + context.getResources().getString(R.string.symbol_RMB));
            holder.tvPresentBalance.setText("赠送:" + DecimalUtil.FormatMoney(internetBarModel.presentBalance/100) + context.getResources().getString(R.string.symbol_RMB));
            return convertView;
        }
    }

    private class ViewHolder{
        private TextView tvName;
        private TextView tvNumber;
        private TextView tvBalance;
        private TextView tvCashBalance;
        private TextView tvPresentBalance;
    }
}
