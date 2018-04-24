package com.imovie.mogic.gameHall.adater;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.imovie.mogic.R;
import com.imovie.mogic.gameHall.model.ReviewModel;
import com.imovie.mogic.home.adater.CommetListAdapter;
import com.imovie.mogic.widget.NoScrollListView;
import com.imovie.mogic.widget.RoundedImageView;
import com.imovie.mogic.widget.YSBCommentListView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;


/**
 * Created by zhouxinshan on 2016/3/31.
 */
public class RatingAdapter extends BaseAdapter {
    private Context context;
    public List<ReviewModel> list;
    private DisplayImageOptions mOption;

    private YSBCommentListView listView;
    private EditText commentEt;//评论输入框
    private View inputLayout;//输入布局
    private boolean isShow = false;//是否在显示
    //位置参数
    private int clickBottom = 0, clickPosition = 0, clickHeight = 0, newH = 0, offset = 0;
    public interface OnClickListening{
        void ClickListening(int i ,ReviewModel comment);
    }
    OnClickListening onClickListening=null;
    public void setOnClickListening(OnClickListening l){
        onClickListening=l;
    }

    public RatingAdapter(Context context, List<ReviewModel> list,final YSBCommentListView listView) {
        this.context = context;
        this.list = list;
        this.listView = listView;
        mOption = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.person_default_profile)
                .showImageOnFail(R.drawable.person_default_profile)
                .showImageForEmptyUri(R.drawable.person_default_profile)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();

        initInputLayout();

        //设置监听器
        listView.setSizeChangeListener(new YSBCommentListView.OnReSizeListener() {
            @Override
            public void onResize(int w, int h, int oldw, final int oldh) {
                if (oldh > h) {
                    //一旦旧的高度大于新的高度就说明键盘此时弹出
                    offset = (oldh - h + inputLayout.getHeight()) - (oldh - clickBottom);
                    newH = h;
                    //滑动列表
                    listView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            listView.scrollListBy(offset);
                        }
                    }, 20);
                }
            }
        });
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public ReviewModel getItem(int position) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.hall_rating_item, null);
            holder.ivPhotoImage = (RoundedImageView) convertView.findViewById(R.id.ivPhotoImage);
            holder.tvName = (TextView) convertView.findViewById(R.id.tvName);
            holder.tvRating = (TextView) convertView.findViewById(R.id.tvRating);
            holder.tvReviewText = (TextView) convertView.findViewById(R.id.tvReviewText);
            holder.tvCreateTime = (TextView) convertView.findViewById(R.id.tvCreateTime);
            holder.tvReply = (TextView) convertView.findViewById(R.id.tvReply);
            holder.lvNoCommentList = (NoScrollListView) convertView.findViewById(R.id.lvNoCommentList);
	  convertView.setTag(holder);
         } else {
            holder = (ViewHolder) convertView.getTag();
         }

        holder.tvReply.setTag(list.get(position).index);
        holder.tvName.setText(list.get(position).name);
        holder.tvRating.setText(list.get(position).rating+"分");
        holder.tvReviewText.setText(list.get(position).reviewText);
        holder.tvCreateTime.setText("在" + list.get(position).createTime + "上机后点评");

        if(list.get(position).replies.size()>0){
            holder.lvNoCommentList.setVisibility(View.VISIBLE);
            List<ReviewModel.Replies> commentsList = list.get(position).replies;
            CommetListAdapter adapter = new CommetListAdapter(context,commentsList);
            holder.lvNoCommentList.setAdapter(adapter);
        }

        if(list.get(position).showComment){
            holder.lvNoCommentList.setVisibility(View.VISIBLE);
        }else{
            holder.lvNoCommentList.setVisibility(View.GONE);
        }
        holder.tvReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int index = (int)v.getTag();
                onClickListening.ClickListening(1,getCommentItem(index));
                showInputLayout();
            }
        });

        try {
            ImageLoader.getInstance().displayImage(list.get(position).photoUrl,holder.ivPhotoImage,mOption);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertView;
    }

    class ViewHolder {
        public RoundedImageView ivPhotoImage = null;
        public TextView tvName = null;
        public TextView tvRating = null;
        public TextView tvReviewText = null;
        public TextView tvCreateTime = null;
        public TextView tvReply;
        public NoScrollListView lvNoCommentList;
    }


    private void showInputLayout() {
        inputLayout.setVisibility(View.VISIBLE);
        openSoftKeyboard(commentEt);
        setShow(true);
    }


    private void initInputLayout() {
        inputLayout = ((Activity) context).findViewById(R.id.input_layer);
        commentEt = (EditText) ((Activity) context).findViewById(R.id.comment_et);
        inputLayout.setVisibility(View.GONE);
    }

    private void openSoftKeyboard(EditText et) {
        et.requestFocus();
        InputMethodManager inputManager = (InputMethodManager) et.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(et, 0);
    }

    public boolean isShow() {
        return isShow;
    }

    public void setShow(boolean show) {
        isShow = show;
    }

    public void setContext(String context,int index,ReviewModel.Replies replies) {
        list.get(index).showComment = true;
//        list.get(index).commentsCount +=1;
        ReviewModel.Replies replies1 = new ReviewModel.Replies();
        replies1.reviewText = replies.reviewText;
        replies1.name = replies.name;
        list.get(index).replies.add(0,replies1);
    }

    public void setIndex(){
        for(int i=0;i<list.size();i++){
            list.get(i).index = i;
        }
    }

    public ReviewModel getCommentItem(int index) {
        return list.get(index);
    }
}
