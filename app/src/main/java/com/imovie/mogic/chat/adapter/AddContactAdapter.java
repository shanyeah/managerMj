package com.imovie.mogic.chat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.imovie.mogic.widget.RoundedImageView;
import com.imovie.mogic.R;
import com.imovie.mogic.chat.model.ContactModel;
import com.imovie.mogic.utills.StringHelper;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;


/**
 * Created by zhouxinshan on 2016/3/31.
 */
public class AddContactAdapter extends BaseAdapter {
    private Context context;
    public List<ContactModel.Contact> list;
    private DisplayImageOptions mOption;
    public int clickType = 0;//附近的人-0   附近的群-1  附近的约会-2

    public AddContactAdapter(Context context, List<ContactModel.Contact> list) {
        this.context = context;
        this.list = list;
        mOption = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.person_default_profile)
                .showImageOnFail(R.drawable.person_default_profile)
                .showImageForEmptyUri(R.drawable.person_default_profile)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public ContactModel.Contact getItem(int position) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.chat_contact_item, null);
            holder.ivUserImage = (RoundedImageView) convertView.findViewById(R.id.ivUserImage);
            holder.tvNickName = (TextView) convertView.findViewById(R.id.tvNickName);
            holder.tvDescription = (TextView) convertView.findViewById(R.id.tvDescription);
            holder.tvDistance = (TextView) convertView.findViewById(R.id.tvDistance);
	  convertView.setTag(holder);
         } else {
            holder = (ViewHolder) convertView.getTag();
         }

        if(clickType==0){
            holder.tvNickName.setText(list.get(position).nickName);
            holder.tvDescription.setText(list.get(position).shortDesc);
        }else if(clickType==1){
            holder.tvNickName.setText(list.get(position).name);
            holder.tvDescription.setText(list.get(position).description);
        }

        if(!StringHelper.isEmpty(list.get(position).distance)) {
            holder.tvDistance.setText(list.get(position).distance);
        }
        try {
            ImageLoader.getInstance().displayImage(list.get(position).faceImageUrl,holder.ivUserImage,mOption);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertView;
    }

    class ViewHolder {
        public RoundedImageView ivUserImage = null;
        public TextView tvNickName = null;
        public TextView tvDescription;
        public TextView tvDistance;
    }
}
