package com.imovie.mogic.home.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.imovie.mogic.R;
import com.imovie.mogic.home.adater.GoodsAdapter;
import com.imovie.mogic.home.adater.GoodsTagAdapter;
import com.imovie.mogic.home.model.GoodTagList;
import com.imovie.mogic.home.model.GoodsModel;
import com.imovie.mogic.widget.YSBPageListView;

import java.util.List;

public class GoodTagDialog extends Dialog implements View.OnClickListener{

	private TextView tvOk;
	private TextView tvCancel;
	public ListView lvGoodsTagList;
	public GoodsTagAdapter adapter;
	public List<GoodTagList> list;
	private Context context;
	public GoodTagList goodTagList;
	public GoodsModel goodsModel;
	public View view;
	public interface onSelectListener {
		void onSelect(GoodTagList goodTagList, View v);
	}
	private onSelectListener listener;
	public void setOnSelectListener(onSelectListener listener) {
		this.listener = listener;
	}

	public GoodTagDialog(Context context,List<GoodTagList> list,View v) {
		super(context,R.style.Theme_TranslucentDlg);
		setContentView(R.layout.home_good_tag_dialog);
		this.context = context;
		this.view = v;
		lvGoodsTagList = (ListView) findViewById(R.id.lvGoodsTagList);
//		adapter = new GoodsTagAdapter(context,list);
//		lvGoodsTagList.setAdapter(adapter);
//		lvGoodsTagList.setDivider(null);
		if(list.size()>0) {
			adapter.setSelectIndex(0);
			goodTagList = adapter.getItem(0);
		}
		tvOk = (TextView) findViewById(R.id.tvOk);
		tvCancel = (TextView) findViewById(R.id.tvCancel);
		tvOk.setOnClickListener(this);
		tvCancel.setOnClickListener(this);
		lvGoodsTagList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
				adapter.setSelectIndex(i);
				goodTagList = adapter.getItem(i);
			}
		});
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {

		case R.id.tvOk:
			this.listener.onSelect(goodTagList,this.view);

//			Intent intent = new Intent(context,YaoZhangGuiIntegralHelp.class);
//			context.startActivity(intent);
			dismiss();
			break;
		case R.id.tvCancel:
			dismiss();
			break;

		default:
			break;
		}
		
	}

	
}
