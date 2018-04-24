package com.imovie.mogic.myRank.adater;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.imovie.mogic.R;
import com.imovie.mogic.myRank.model.PraiseMode;
import com.imovie.mogic.utills.DecimalUtil;
import com.imovie.mogic.widget.RoundedImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;


/**
 * Created by zhouxinshan on 2016/3/31.
 */
public class PraiseAdapter extends BaseAdapter {
    private Context context;
    public List<PraiseMode> list;
    private DisplayImageOptions mOption;

    public PraiseAdapter(Context context, List<PraiseMode> list) {
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
    public PraiseMode getItem(int position) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.praise_rank_item, null);
            holder.ivPhotoImage = (RoundedImageView) convertView.findViewById(R.id.ivPhotoImage);
            holder.tvName = (TextView) convertView.findViewById(R.id.tvName);
            holder.tvPraiseRanking = (TextView) convertView.findViewById(R.id.tvPraiseRanking);
            holder.tvMemberId = (TextView) convertView.findViewById(R.id.tvMemberId);
            holder.tvRewardAmount = (TextView) convertView.findViewById(R.id.tvRewardAmount);
            holder.tvPraiseCount = (TextView) convertView.findViewById(R.id.tvPraiseCount);
	  convertView.setTag(holder);
         } else {
            holder = (ViewHolder) convertView.getTag();
         }

        holder.tvName.setText(list.get(position).nickName);
        holder.tvPraiseRanking.setText("" + list.get(position).ranking);
        holder.tvMemberId.setText("ID:" + list.get(position).adminId);
        holder.tvRewardAmount.setText(Html.fromHtml("<font color='#565a5c' size=14>奖励金额:</font><font color='#fd5c02' size=14>"+ DecimalUtil.FormatMoney(list.get(position).rewardAmount/100) +"</font><font color=\'#565a5c\' size=14>元</font>"));
        holder.tvPraiseCount.setText("点赞次数:"+list.get(position).count);
//        holder.tvCreateTime.setText("在"+DateUtil.TimeFormat(list.get(position).createTime,"yyyy/MM/dd")+"日上机后点评");
//        RelativeLayout.LayoutParams ivParam=(RelativeLayout.LayoutParams)holder.ivExercises.getLayoutParams();
//        int screenWidth = context.getResources().getDisplayMetrics().widthPixels;
//        ivParam.width = (screenWidth*220)/746;
//        ivParam.height =( screenWidth*220)/746;
//        holder.ivExercises.setLayoutParams(ivParam);

        try {
            ImageLoader.getInstance().displayImage(list.get(position).fackeImageUrl,holder.ivPhotoImage,mOption);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertView;
    }

    class ViewHolder {
        public RoundedImageView ivPhotoImage = null;
        public TextView tvName = null;
        public TextView tvPraiseRanking = null;
        public TextView tvMemberId = null;
        public TextView tvRewardAmount = null;
        public TextView tvPraiseCount;
    }
}
