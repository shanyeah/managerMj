package com.imovie.mogic.mine.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.imovie.mogic.R;
import com.imovie.mogic.mine.model.GroupItem;
import com.imovie.mogic.widget.AnimatedExpandableListView;

import java.util.List;


/**
 * Created by zhoulinda on 16/3/15.
 */
public class AnimatedListAdapter extends AnimatedExpandableListView.AnimatedExpandableListAdapter {
    private Context context;
    public List<GroupItem> list;

    public AnimatedListAdapter(Context context, List<GroupItem> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return list.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return list.size();
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return list.get(groupPosition).list.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupHolder holder;
        if (convertView == null) {
            holder = new GroupHolder();
            convertView = View.inflate(context, R.layout.adapter_parent, null);
            holder.item_parent = (TextView) convertView.findViewById(R.id.item_parent);
            holder.tvAttendDesc = (TextView) convertView.findViewById(R.id.tvAttendDesc);
            holder.ivUpAndDown = (ImageView) convertView.findViewById(R.id.ivUpAndDown);
            convertView.setTag(holder);
        } else {
            holder = (GroupHolder) convertView.getTag();
        }
        holder.item_parent.setText(list.get(groupPosition).title);
        holder.tvAttendDesc.setText(list.get(groupPosition).desc);

        //判断isExpanded就可以控制是按下还是关闭，同时更换图片
        if(isExpanded){
            holder.ivUpAndDown.setBackgroundResource(R.drawable.icon_up);
        }else{
            holder.ivUpAndDown.setBackgroundResource(R.drawable.icon_down); }
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public int getRealChildrenCount(int groupPosition) {
        return list.get(groupPosition).list.size();
    }

    @Override
    public View getRealChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildHolder holder;
        if (convertView == null) {
            holder = new ChildHolder();
            convertView = View.inflate(context, R.layout.adapter_child, null);
            holder.item_child = (TextView) convertView.findViewById(R.id.item_child);
            convertView.setTag(holder);
        } else {
            holder = (ChildHolder) convertView.getTag();
        }
        holder.item_child.setText(list.get(groupPosition).list.get(childPosition).note);
        return convertView;
    }

    private static class ChildHolder {
        public TextView item_child;
    }

    private static class GroupHolder {
        public TextView item_parent;
        public TextView tvAttendDesc;
        public ImageView ivUpAndDown;
    }

}
