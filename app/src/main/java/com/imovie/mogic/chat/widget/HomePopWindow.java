package com.imovie.mogic.chat.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.imovie.mogic.R;
import com.imovie.mogic.ScanPay.model.InternetBarModel;

import java.util.List;

/**
 * Created by $zhou on 2017/3/29 0029.
 */

public class HomePopWindow<T> extends PopupWindow {
    private LayoutInflater inflater;
    private ListView mListView;
    public List<T> list;
    public MyAdapter  mAdapter;
    private Context context;

    public HomePopWindow(Context context, List<T> list, AdapterView.OnItemClickListener clickListener) {
        super(context);
        inflater=LayoutInflater.from(context);
        this.list=list;
        this.context = context;
        init(clickListener);
    }

    private void init(AdapterView.OnItemClickListener clickListener){
        View view = inflater.inflate(R.layout.home_popup_layout, null);
        setContentView(view);
        setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setFocusable(true);
//        ColorDrawable dw = new ColorDrawable(0x00);
//        setBackgroundDrawable(dw);
        setBackgroundDrawable(context.getResources().getDrawable(R.drawable.home_bg_more));
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
                convertView=inflater.inflate(R.layout.home_popup_item, null);
                holder.tvName=(TextView) convertView.findViewById(R.id.tv_name);
                holder.ivName=(ImageView) convertView.findViewById(R.id.ivName);
                convertView.setTag(holder);
            }else{
                holder=(ViewHolder) convertView.getTag();
            }
            InternetBarModel internetBarModel = (InternetBarModel)getItem(position);
            holder.tvName.setText(internetBarModel.name);
            if(position==0){
                holder.ivName.setBackground(context.getResources().getDrawable(R.drawable.home_more_scan));
            }else if(position==1){
                holder.ivName.setBackground(context.getResources().getDrawable(R.drawable.home_more_add));
            }else if(position==2){
                holder.ivName.setBackground(context.getResources().getDrawable(R.drawable.home_more_message));
            }
            return convertView;
        }
    }

    private class ViewHolder{
        private TextView tvName;
        private ImageView ivName;
    }
}
