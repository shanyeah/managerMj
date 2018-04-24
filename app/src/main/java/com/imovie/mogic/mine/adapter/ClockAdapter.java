package com.imovie.mogic.mine.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.imovie.mogic.R;
import com.imovie.mogic.mine.attend.AttendClockActivity;
import com.imovie.mogic.mine.model.ClockModel;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.util.List;


/**
 * Created by zhouxinshan on 2016/3/31.
 */
public class ClockAdapter extends BaseAdapter {
    private Context context;
    public List<ClockModel> list;
    private DisplayImageOptions mOption;

    public ClockAdapter(Context context, List<ClockModel> list) {
        this.context = context;
        this.list = list;
//        mOption = new DisplayImageOptions.Builder()
//                .showImageOnLoading(R.drawable.home_card_image)
//                .showImageOnFail(R.drawable.home_card_image)
//                .showImageForEmptyUri(R.drawable.home_card_image)
//                .cacheInMemory(true)
//                .cacheOnDisk(true)
//                .build();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public ClockModel getItem(int position) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.mine_clock_item, null);
            holder.viewOval = (View) convertView.findViewById(R.id.viewOval);
            holder.tvOvalState = (TextView) convertView.findViewById(R.id.tvOvalState);
            holder.viewClock = (View) convertView.findViewById(R.id.viewClock);
            holder.rlClockWork = (RelativeLayout) convertView.findViewById(R.id.rlClockWork);
            holder.tvWorkTime = (TextView) convertView.findViewById(R.id.tvWorkTime);
            holder.tvWorkState = (TextView) convertView.findViewById(R.id.tvWorkState);
            holder.tvPlanTime = (TextView) convertView.findViewById(R.id.tvPlanTime);
            holder.tvWorkClock = (TextView) convertView.findViewById(R.id.tvWorkClock);
            holder.tvClockAgain = (TextView) convertView.findViewById(R.id.tvClockAgain);

	  convertView.setTag(holder);
         } else {
            holder = (ViewHolder) convertView.getTag();
         }

//         if(list.get(position).cashAmount>0){
//             holder.tvMoneyNum.setText("+"+ DecimalUtil.FormatMoney(list.get(position).cashAmount));
//         }else{
//             holder.tvMoneyNum.setText(""+ DecimalUtil.FormatMoney(list.get(position).cashAmount));
//         }
//        holder.tvMoneySum.setText("余额："+ DecimalUtil.FormatMoney(list.get(position).amount)+ context.getResources().getString(R.string.symbol_RMB));
////        holder.tvAccountName.setText(list.get(position).remark);
        if(list.get(position).lineShow){
            holder.viewOval.setVisibility(View.INVISIBLE);
        }else{
            holder.viewOval.setVisibility(View.VISIBLE);
        }

        if(list.get(position).clockShow){
            holder.tvWorkClock.setVisibility(View.VISIBLE);
            holder.rlClockWork.setVisibility(View.GONE);
            holder.viewClock.setVisibility(View.GONE);
            holder.tvOvalState.setBackground(context.getResources().getDrawable(R.drawable.oval_bg7_bg7));
            holder.tvWorkClock.setText(list.get(position).clockName);
        }else{
            holder.tvWorkClock.setVisibility(View.GONE);
            holder.rlClockWork.setVisibility(View.VISIBLE);
            if(list.get(position).viewClock){
                holder.viewClock.setVisibility(View.GONE);
            }else{
                holder.viewClock.setVisibility(View.VISIBLE);
            }
            holder.tvOvalState.setBackground(context.getResources().getDrawable(R.drawable.oval_bg2_bg2));
            holder.tvClockAgain.setTag(list.get(position).id);

            String checkTime = "";
            switch (list.get(position).status){
                case 1:
                    String str = list.get(position).checkTime;
                    checkTime = str.substring(10,str.length()-3);
                    holder.tvWorkState.setText("正常");
                    holder.tvClockAgain.setVisibility(View.GONE);
//                    holder.tvClockAgain.setText("补卡申请");
                    break;
                case 2:
                    String str2 = list.get(position).checkTime;
                    checkTime = str2.substring(10,str2.length()-3);
                    holder.tvWorkState.setText("迟到");
                    holder.tvClockAgain.setVisibility(View.VISIBLE);
                    if(list.get(position).attendExtraRecord != null){
                        switch (list.get(position).attendExtraRecord.status){
                            case 0:
                                holder.tvClockAgain.setText("补卡(审批中)");
                                break;
                            case 1:
                                holder.tvClockAgain.setText("补卡(审核通过)");
                                holder.tvWorkState.setText("正常");
                                break;
                            case 2:
                                holder.tvClockAgain.setText("补卡(审核不通过)");
                                break;
                        }
                    }
                    break;
                case 3:
                    String str3 = list.get(position).checkTime;
                    checkTime = str3.substring(10,str3.length()-3);
                    holder.tvWorkState.setText("早退");
                    holder.tvClockAgain.setVisibility(View.VISIBLE);
                    if(list.get(position).attendExtraRecord != null){
                        switch (list.get(position).attendExtraRecord.status){
                            case 0:
                                holder.tvClockAgain.setText("补卡(审批中)");
                                break;
                            case 1:
                                holder.tvClockAgain.setText("补卡(审核通过)");
                                holder.tvWorkState.setText("正常");
                                break;
                            case 2:
                                holder.tvClockAgain.setText("补卡(审核不通过)");
                                break;
                        }
                    }
                    break;
                case 4:
//                    String str2 = list.get(position).checkTime;
//                    checkTime = str2.substring(10,str2.length()-3);
                    holder.tvWorkState.setText("旷工");
                    holder.tvClockAgain.setVisibility(View.VISIBLE);
                    if(list.get(position).attendExtraRecord != null){
                        switch (list.get(position).attendExtraRecord.status){
                            case 0:
                                holder.tvClockAgain.setText("补卡(审批中)");
                                break;
                            case 1:
                                holder.tvClockAgain.setText("补卡(审核通过)");
                                holder.tvWorkState.setText("正常");
                                break;
                            case 2:
                                holder.tvClockAgain.setText("补卡(审核不通过)");
                                break;
                        }
                    }
                    break;
                case 5:
//                    String str2 = list.get(position).checkTime;
//                    checkTime = str2.substring(10,str2.length()-3);
                    holder.tvWorkState.setText("缺打");
                    holder.tvClockAgain.setVisibility(View.VISIBLE);
                    if(list.get(position).attendExtraRecord != null){
                        switch (list.get(position).attendExtraRecord.status){
                            case 0:
                                holder.tvClockAgain.setText("补卡(审批中)");
                                break;
                            case 1:
                                holder.tvClockAgain.setText("补卡(审核通过)");
                                break;
                            case 2:
                                holder.tvClockAgain.setText("补卡(审核不通过)");
                                break;
                        }
                    }
                    break;

                case 6:
                    holder.tvWorkState.setText("补打");
                    break;
            }
            holder.tvWorkTime.setText(list.get(position).clockName + checkTime);
            String planTime = list.get(position).planTime;
            String clockName = list.get(position).clockName;
            holder.tvPlanTime.setText("("+clockName.substring(0,2)+ "时间:" + planTime.substring(10,planTime.length()-3)+")");
        }

        holder.tvClockAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, AttendClockActivity.class);
                intent.putExtra("attendId",(int)view.getTag());
                context.startActivity(intent);
            }
        });

        return convertView;
    }


    class ViewHolder {
        public View viewOval;
        public TextView tvOvalState = null;
        public View viewClock;
        public RelativeLayout rlClockWork = null;
        public TextView tvWorkTime = null;
        public TextView tvWorkState = null;
        public TextView tvPlanTime = null;
        public TextView tvWorkClock = null;
        public TextView tvClockAgain;

    }
}
