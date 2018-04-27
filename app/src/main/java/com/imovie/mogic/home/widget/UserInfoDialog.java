package com.imovie.mogic.home.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.imovie.mogic.R;
import com.imovie.mogic.home.adater.UserInfoAdapter;
import com.imovie.mogic.home.model.GoodTagList;
import com.imovie.mogic.home.model.GoodsModel;
import com.imovie.mogic.home.model.SearchModel;
import com.imovie.mogic.home.model.SearchUserModel;
import com.imovie.mogic.home.net.HomeWebHelper;
import com.imovie.mogic.utills.StringHelper;
import com.imovie.mogic.utills.Utills;
import com.imovie.mogic.web.IModelResultListener;
import com.imovie.mogic.web.model.HttpResultModel;

import java.util.ArrayList;
import java.util.List;

public class UserInfoDialog extends Dialog implements View.OnClickListener{

	private TextView tvOk;
	private TextView tvCancel;
	public ListView lvGoodsTagList;
	public UserInfoAdapter adapter;
	public List<SearchUserModel> list = new ArrayList<>();
	private Context context;
	public SearchUserModel userModel;
	public GoodsModel goodsModel;
	public View view;
	public Button btChargeSelect;
	public EditText etUserId;
	public interface onSelectListener {
		void onSelect(SearchUserModel userModel);
	}
	private onSelectListener listener;
	public void setOnSelectListener(onSelectListener listener) {
		this.listener = listener;
	}

	public UserInfoDialog(Context context) {
		super(context,R.style.Theme_TranslucentDlg);
		setContentView(R.layout.home_user_info_dialog);
		this.context = context;
		btChargeSelect = (Button) findViewById(R.id.btChargeSelect);
		etUserId = (EditText) findViewById(R.id.etUserId);
		lvGoodsTagList = (ListView) findViewById(R.id.lvGoodsTagList);
		adapter = new UserInfoAdapter(context,list);
		lvGoodsTagList.setAdapter(adapter);
		lvGoodsTagList.setDivider(null);
//		if(list.size()>0) {
//			adapter.setSelectIndex(0);
//			goodTagList = adapter.getItem(0);
//		}
		tvOk = (TextView) findViewById(R.id.tvOk);
		tvCancel = (TextView) findViewById(R.id.tvCancel);
		tvOk.setOnClickListener(this);
		tvCancel.setOnClickListener(this);
		btChargeSelect.setOnClickListener(this);
		lvGoodsTagList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//				adapter.setSelectIndex(i);
				userModel = adapter.getItem(i);
				listener.onSelect(userModel);
				dismiss();
			}
		});

	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {

		case R.id.tvOk:
//			this.listener.onSelect(goodTagList,this.view);

//			Intent intent = new Intent(context,YaoZhangGuiIntegralHelp.class);
//			context.startActivity(intent);
			dismiss();
			break;
		case R.id.tvCancel:
			dismiss();
			break;
		case R.id.btChargeSelect:
			String user = etUserId.getText().toString();
			if(StringHelper.isEmail(user)) {
				Utills.showShortToast("请输入充值用户");
				return;
			}
			getCheckUserInfo(user);
			break;

		default:
			break;
		}
		
	}

	public void getCheckUserInfo(String input){
		HomeWebHelper.getCheckUserInfo(input,new IModelResultListener<SearchModel>() {
			@Override
			public boolean onGetResultModel(HttpResultModel resultModel) {
				return false;
			}

			@Override
			public void onSuccess(String resultCode, SearchModel resultModel, List<SearchModel> resultModelList, String resultMsg, String hint) {
//                Log.e("----city:",""+resultCode);
				if(resultCode.equals("0")) {
					adapter.list.clear();
					if (resultModel.list.size() > 0) {
						adapter.list.addAll(resultModel.list);
						adapter.notifyDataSetChanged();
					}
				}

			}

			@Override
			public void onFail(String resultCode, String resultMsg, String hint) {

			}

			@Override
			public void onError(String errorMsg) {

			}
		});
	}

	
}
