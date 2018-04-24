package com.imovie.mogic.home.widget;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.imovie.mogic.R;
import com.imovie.mogic.home.model.GoodsModel;
import com.imovie.mogic.utills.DecimalUtil;
import com.imovie.mogic.utills.StringHelper;
import com.imovie.mogic.utills.Utills;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by $zhou on 2017/3/29 0029.
 */

public class CartPopWindow<T> extends PopupWindow {
    private LayoutInflater inflater;
    private ListView mListView;
    public TextView tvBalancePay;
    public TextView tvWechatPay;
    public TextView tvMemberInfo;
    public EditText etSeatNum;
    public EditText etMemberNum;
    public ImageView ivSeatScan;
    public ImageView ivCartClear;
    public Button btCartPay;
    public List<T> list;
    public MyAdapter  mAdapter;
    public Context context;
    private DisplayImageOptions mOption;
    public int payType = 1;

    public interface onSelectListener {
        void onSelect(int type,String text,int id);
    }
    private onSelectListener listener;
    public void setOnSelectListener(onSelectListener listener) {
        this.listener = listener;
    }


    public CartPopWindow(Context context, List<T> list, AdapterView.OnItemClickListener clickListener) {
        super(context);
        this.context = context;
        inflater=LayoutInflater.from(context);
        this.list=list;
        init(clickListener);
        mOption = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.home_card_image)
                .showImageOnFail(R.drawable.home_card_image)
                .showImageForEmptyUri(R.drawable.home_card_image)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
    }

    private void init(AdapterView.OnItemClickListener clickListener){
        View view = inflater.inflate(R.layout.cart_window_layout, null);
        setContentView(view);
        setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setFocusable(true);
        ColorDrawable dw = new ColorDrawable(0x00);
        setBackgroundDrawable(dw);
        mListView = (ListView) view.findViewById(R.id.listview);
        tvBalancePay = (TextView) view.findViewById(R.id.tvBalancePay);
        tvWechatPay = (TextView) view.findViewById(R.id.tvWechatPay);
        tvMemberInfo = (TextView) view.findViewById(R.id.tvMemberInfo);
        etSeatNum = (EditText) view.findViewById(R.id.etSeatNum);
        etMemberNum = (EditText) view.findViewById(R.id.etMemberNum);
        ivSeatScan = (ImageView) view.findViewById(R.id.ivSeatScan);
        ivCartClear = (ImageView) view.findViewById(R.id.ivCartClear);
        btCartPay = (Button) view.findViewById(R.id.btCartPay);
        mAdapter=new MyAdapter();
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(clickListener);

        tvBalancePay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvBalancePay.setBackgroundColor(context.getResources().getColor(R.color.BG4));
                tvWechatPay.setBackgroundColor(context.getResources().getColor(R.color.BG2));
                tvMemberInfo.setText("付款帐号:");
                listener.onSelect(2,"222",-1);
                payType = 1;
            }
        });

        tvWechatPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvBalancePay.setBackgroundColor(context.getResources().getColor(R.color.BG2));
                tvWechatPay.setBackgroundColor(context.getResources().getColor(R.color.BG4));
                tvMemberInfo.setText("会员信息:");
                listener.onSelect(3,"222",-1);
                payType = 2;
            }
        });

        btCartPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String seatNum = etSeatNum.getText().toString();
                if(StringHelper.isEmpty(seatNum)){
                    Utills.showShortToast("请输入送餐桌号");
                    return;
                }
                listener.onSelect(1,seatNum,-1);
            }
        });

        ivSeatScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onSelect(6,"222",-1);
            }
        });

        ivCartClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onSelect(7,"222",-1);
            }
        });
    }

    public void refreshData(List<T> list){

            RelativeLayout.LayoutParams ivParam = (RelativeLayout.LayoutParams) mListView.getLayoutParams();
            int screenHeight = context.getResources().getDisplayMetrics().heightPixels;
//        ivParam.width = screenWidth*5/6;
            if(list.size()>5) {
                ivParam.height = screenHeight / 2;
            }else{
                ivParam.height = -2;
            }
            mListView.setLayoutParams(ivParam);
        this.list=list;
        mAdapter.notifyDataSetChanged();

    }

    public void refreshSeatText(String str){
        etSeatNum.setText(str);
    }
    public String getMemberNum(){
        return etMemberNum.getText().toString();
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
                convertView=inflater.inflate(R.layout.cart_goods_item, null);
                holder.tvGoodName = (TextView) convertView.findViewById(R.id.tvGoodName);
                holder.ivCard = (ImageView) convertView.findViewById(R.id.iv_card_item);
                holder.ivAddGoods = (ImageView) convertView.findViewById(R.id.ivAddGoods);
                holder.ivReduceGoods = (ImageView) convertView.findViewById(R.id.ivReduceGoods);
                holder.tvGoodPrice = (TextView) convertView.findViewById(R.id.tvGoodPrice);
                holder.tvCategoryName = (TextView) convertView.findViewById(R.id.tvCategoryName);
                holder.tvTagName = (TextView) convertView.findViewById(R.id.tvTagName);
                holder.etAddGoods = (EditText) convertView.findViewById(R.id.etAddGoods);
                holder.viewType = (View) convertView.findViewById(R.id.viewType);
                convertView.setTag(holder);
            }else{
                holder=(ViewHolder) convertView.getTag();
            }
            GoodsModel goodsModel = (GoodsModel)getItem(position);
            holder.tvGoodName.setText(goodsModel.name);
            holder.tvGoodPrice.setText(DecimalUtil.FormatMoney(goodsModel.price * goodsModel.sum/100) + context.getResources().getString(R.string.symbol_RMB));
            holder.etAddGoods.setText(""+goodsModel.sum);
            holder.ivAddGoods.setTag(goodsModel);
            holder.ivReduceGoods.setTag(goodsModel);
            if(goodsModel.haveTag){
                holder.tvTagName.setVisibility(View.VISIBLE);
                holder.tvTagName.setText(goodsModel.tagName);
            }else{
                holder.tvTagName.setVisibility(View.GONE);
            }

            holder.ivAddGoods.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    GoodsModel goods = (GoodsModel)view.getTag();
                    listener.onSelect(4,""+goods.id,goods.goodsTagCategory);
                }
            });

            holder.ivReduceGoods.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    GoodsModel goods = (GoodsModel)view.getTag();
                    listener.onSelect(5,""+goods.id,goods.goodsTagCategory);
                }
            });

            try {
                ImageLoader.getInstance().displayImage(goodsModel.imageUrl,holder.ivCard,mOption);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return convertView;
        }
    }

    private class ViewHolder{
        public ImageView ivCard = null;
        public ImageView ivAddGoods;
        public ImageView ivReduceGoods;
        public TextView tvGoodName = null;
        public TextView tvGoodPrice = null;
        public TextView tvCategoryName;
        public TextView tvTagName;
        public EditText etAddGoods;
        public View viewType;
    }
}
