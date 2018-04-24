package com.imovie.mogic.myRank.widget;

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
import com.imovie.mogic.card.model.InternetBarModel;

import java.util.List;

/**
 * Created by $zhou on 2017/3/29 0029.
 */

public class SpinerPopWindow<T> extends PopupWindow {
    private LayoutInflater inflater;
    private ListView mListView;
    public List<T> list;
    public MyAdapter  mAdapter;

    public SpinerPopWindow(Context context, List<T> list, AdapterView.OnItemClickListener clickListener) {
        super(context);
        inflater=LayoutInflater.from(context);
        this.list=list;
        init(clickListener);
    }

    private void init(AdapterView.OnItemClickListener clickListener){
        View view = inflater.inflate(R.layout.spiner_window_layou, null);
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
                convertView=inflater.inflate(R.layout.spiner_item_layou, null);
                holder.tvName=(TextView) convertView.findViewById(R.id.tv_name);
                convertView.setTag(holder);
            }else{
                holder=(ViewHolder) convertView.getTag();
            }
            InternetBarModel internetBarModel = (InternetBarModel)getItem(position);
            holder.tvName.setText(internetBarModel.name);
            return convertView;
        }
    }

    private class ViewHolder{
        private TextView tvName;
    }
}
