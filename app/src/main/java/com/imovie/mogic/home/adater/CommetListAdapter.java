package com.imovie.mogic.home.adater;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.imovie.mogic.R;
import com.imovie.mogic.gameHall.model.ReviewModel;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.util.List;


/**
 * Created by zhouxinshan on 2016/3/31.
 */
public class CommetListAdapter extends BaseAdapter {
    private Context context;
    public List<ReviewModel.Replies> list;
    private DisplayImageOptions mOption;

    public CommetListAdapter(Context context, List<ReviewModel.Replies> list) {
        this.context = context;
        this.list = list;
//        mOption = new DisplayImageOptions.Builder()
//                .showImageOnLoading(R.drawable.home_enjoy_image)
//                .showImageOnFail(R.drawable.home_enjoy_image)
//                .showImageForEmptyUri(R.drawable.home_enjoy_image)
//                .cacheInMemory(true)
//                .cacheOnDisk(true)
//                .build();

    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public ReviewModel.Replies getItem(int position) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.comment_list_item, null);
            holder.tvCommentContent = (TextView) convertView.findViewById(R.id.tvCommentContent);
	  convertView.setTag(holder);
         } else {
            holder = (ViewHolder) convertView.getTag();
         }
        holder.tvCommentContent.setText(list.get(position).name+"："+list.get(position).reviewText);
        return convertView;
    }

    class ViewHolder {
        public TextView tvCommentContent = null;
    }
}
