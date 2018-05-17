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
		void onSelect(String str);
	}
	private onSelectListener listener;
	public void setOnSelectListener(onSelectListener listener) {
		this.listener = listener;
	}

	public UserInfoDialog(Context context) {
		super(context,R.style.Theme_TranslucentDlg);
		setContentView(R.layout.home_user_info_dialog);
		this.context = context;
		etUserId = (EditText) findViewById(R.id.etUserId);

		tvOk = (TextView) findViewById(R.id.tvOk);
		tvCancel = (TextView) findViewById(R.id.tvCancel);
		tvOk.setOnClickListener(this);
		tvCancel.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {

		case R.id.tvOk:
			String password = etUserId.getText().toString();
			if(StringHelper.isEmpty(password)){
				Utills.showShortToast("请输入支付密码");
				return;
			}
			listener.onSelect(password);
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
