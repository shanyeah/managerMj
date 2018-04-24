package com.imovie.mogic.ScanPay.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.imovie.mogic.R;
import com.imovie.mogic.login.model.LoginModel;
import com.nostra13.universalimageloader.core.DisplayImageOptions;


public class PayMemberFragment extends Fragment {
	private static final String ARG_PARAM1 = "param1";
	private static final String ARG_PARAM2 = "param2";
	private String mParam1;
	private String mParam2;

	private ImageView cardTop;
	private TextView tvMemberPay;
	private TextView tvChargeMoney;
	private TextView tvMemberUpgrade;
	private TextView tvCardNo;
	private TextView tvCardCategoryName;
	private TextView tv_right_one;
	private TextView tv_right_two;
	private TextView tv_right_three;
	private TextView tv_right_four;
	private LoginModel loginModel;
	private RelativeLayout rlMoneyAccount;
	private RelativeLayout rlGiveAccount;

	private DisplayImageOptions mOption;


	public PayMemberFragment() {

	}

	public static PayMemberFragment newInstance(String param1, String param2) {
		PayMemberFragment fragment = new PayMemberFragment();
		Bundle args = new Bundle();
		args.putString(ARG_PARAM1, param1);
		args.putString(ARG_PARAM2, param2);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			mParam1 = getArguments().getString(ARG_PARAM1);
			mParam2 = getArguments().getString(ARG_PARAM2);
		}
		loginModel = new LoginModel();
		mOption = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.home_card_image)
				.showImageOnFail(R.drawable.home_card_image)
				.showImageForEmptyUri(R.drawable.home_card_image)
				.cacheInMemory(true)
				.cacheOnDisk(true)
				.build();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v=inflater.inflate(R.layout.card_pay_fragment, container, false);

		initView(v);
		setView();
		setListener();
		return v;
	}

	private void initView(View view) {
		cardTop = (ImageView) view.findViewById(R.id.iv_card_image);
		tvMemberPay = (TextView) view.findViewById(R.id.tvMemberPay);
		tvChargeMoney = (TextView) view.findViewById(R.id.tvChargeMoney);
		tvMemberUpgrade = (TextView) view.findViewById(R.id.tvMemberUpgrade);
		tvCardNo = (TextView) view.findViewById(R.id.tvCardNoFragment);
		tvCardCategoryName = (TextView) view.findViewById(R.id.tvCardCategoryName);
		tv_right_one = (TextView) view.findViewById(R.id.tv_right_one);
		tv_right_two = (TextView) view.findViewById(R.id.tv_right_two);
		tv_right_three = (TextView) view.findViewById(R.id.tv_right_three);
		tv_right_four = (TextView) view.findViewById(R.id.tv_right_four);

		rlMoneyAccount = (RelativeLayout) view.findViewById(R.id.rlMoneyAccount);
		rlGiveAccount = (RelativeLayout) view.findViewById(R.id.rlGiveAccount);



		LinearLayout.LayoutParams ivParam=(LinearLayout.LayoutParams)cardTop.getLayoutParams();
		int screenWidth = getResources().getDisplayMetrics().widthPixels;
		ivParam.width = (screenWidth*576)/746;
		ivParam.height =( screenWidth*365)/746;
		cardTop.setLayoutParams(ivParam);

	}

	private void setView() {
//		int memberId = MyApplication.getInstance().mPref.getInt("memberId",0);
//		getMyData(memberId);

		getCardDetail();

	}
	private void setListener() {
		tvMemberPay.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
//				Intent intent = new Intent(getContext(), MineQrCodeActivity.class);
//				startActivity(intent);

			}
		});
		tvChargeMoney.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
//				Intent intent = new Intent(getContext(), PayOrderActivity.class);
//				startActivity(intent);

			}
		});
		tvMemberUpgrade.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
//				Intent intent = new Intent(getContext(), MemberUpgradeActivity.class);
//				startActivity(intent);
			}
		});

		rlMoneyAccount.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
//				Intent intent = new Intent(getContext(), AccountLogActivity.class);
//				intent.putExtra("type",AccountLogActivity.MSG_MONEY);
//				startActivity(intent);
			}
		});

		rlGiveAccount.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
//				Intent intent = new Intent(getContext(), AccountLogActivity.class);
//				intent.putExtra("type",AccountLogActivity.MSG_GIVE);
//				startActivity(intent);
			}
		});

	}

	public void getCardDetail(){
//		PayWebHelper.getCardDetail(new IModelResultListener<TestModel>() {
//			@Override
//			public boolean onGetResultModel(HttpResultModel resultModel) {
//				return false;
//			}
//
//			@Override
//			public void onSuccess(String resultCode, TestModel resultModel, List<TestModel> resultModelList, String resultMsg, String hint) {
////                Log.e("-----getCardDetail",""+resultCode);
//				loginModel.setModelByJson(resultCode);
//				if(loginModel.returnCode.equals("SUCCESS")) {
//					setData();
//				}else{
//					Toast.makeText(getContext(),"网络错误",Toast.LENGTH_SHORT).show();
//				}
//			}
//
//			@Override
//			public void onFail(String resultCode, String resultMsg, String hint) {
////                lvCard.finishLoading(true);
//			}
//
//			@Override
//			public void onError(String errorMsg) {
////                lvCard.finishLoading(true);
//			}
//		});
	}

	private void setData(){
//		tvCardNo.setText(""+loginModel.card.cardNo);
//		tvCardCategoryName.setText(loginModel.card.cardCategoryName);
////					viewHolder.tvCashBalance.setText(DecimalUtil.FormatMoney(loginModel.card.cashBalance)+getResources().getString(R.string.symbol_RMB));
////					viewHolder.nextLevelCategoryName.setText("升级为"+loginModel.card.nextLevelCategoryName);
//		ImageLoader.getInstance().displayImage(loginModel.cardCategory.imageUrl,cardTop,mOption);
//		int size = loginModel.cardCategory.rights.size();
//		if(size>0) {
//			for (int i = 0; i < size; i++) {
//				switch (i) {
//					case 0:
//						tv_right_one.setText(loginModel.cardCategory.rights.get(i).name);
//						tv_right_one.setVisibility(View.VISIBLE);
//						break;
//					case 1:
//						tv_right_two.setText(loginModel.cardCategory.rights.get(i).name);
//						tv_right_two.setVisibility(View.VISIBLE);
//						break;
//					case 2:
//						tv_right_three.setText(loginModel.cardCategory.rights.get(i).name);
//						tv_right_three.setVisibility(View.VISIBLE);
//						break;
//					case 3:
//						tv_right_four.setText(loginModel.cardCategory.rights.get(i).name);
//						tv_right_four.setVisibility(View.VISIBLE);
//						break;
//				}
//			}
//		}

	}

}
