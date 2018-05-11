package com.imovie.mogic.home.adater;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.imovie.mogic.R;
import com.imovie.mogic.home.model.CategorysModel;
import com.imovie.mogic.home.model.GoodTagList;
import com.imovie.mogic.utills.Utills;
import com.imovie.mogic.widget.HorizontalListView;
import com.imovie.mogic.widget.NoScrollGridView;

import java.util.List;


/**
 * Created by zhouxinshan on 2016/3/31.
 */
public class CategorysAdapter extends BaseAdapter {
    private Context context;
    public List<CategorysModel.Categorys> list;
    public CategorysAdapter(Context context, List<CategorysModel.Categorys> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public CategorysModel.Categorys getItem(int position) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.home_categorys_item, null);
            holder.lvCategoryTagsList = (NoScrollGridView) convertView.findViewById(R.id.lvCategoryTagsList);
            holder.tvTypeName = (TextView) convertView.findViewById(R.id.tvTypeName);
	  convertView.setTag(holder);
         } else {
            holder = (ViewHolder) convertView.getTag();
         }

        holder.tvTypeName.setText(list.get(position).name);
        CategoryTagsAdapter tagsAdapter = new CategoryTagsAdapter(context,list.get(position).goodsTags);
        holder.lvCategoryTagsList.setAdapter(tagsAdapter);
        holder.lvCategoryTagsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                CategoryTagsAdapter tagsAdapter = (CategoryTagsAdapter)adapterView.getAdapter();
                tagsAdapter.setSelectIndex(tagsAdapter.getItem(i).id);
            }
        });

//        if(list.get(position).isSelect){
////            holder.viewType.setVisibility(View.VISIBLE);
//            holder.rlNameState.setBackgroundResource(R.color.BG5);
//        }else{
////            holder.viewType.setVisibility(View.GONE);
//            holder.rlNameState.setBackgroundResource(R.color.BG3);
//        }
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
        public NoScrollGridView lvCategoryTagsList;
        public TextView tvTypeName = null;
        public View viewType = null;
    }
}
