package com.imovie.mogic.car;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.github.florent37.viewanimator.AnimationListener;
import com.github.florent37.viewanimator.ViewAnimator;
import com.imovie.mogic.MyApplication;
import com.imovie.mogic.R;
import com.imovie.mogic.car.adapters.CarAdapter;
import com.imovie.mogic.car.bean.FoodBean;
import com.imovie.mogic.car.utils.ViewUtils;
import com.imovie.mogic.car.view.AddWidget;
import com.imovie.mogic.car.view.ListContainer;
import com.imovie.mogic.car.view.ShopCarView;
import com.imovie.mogic.home.BaseActivity;
import com.imovie.mogic.home.SelectTypeActivity;
import com.imovie.mogic.home.adater.GoodsTagAdapter;
import com.imovie.mogic.home.model.GoodTagList;
import com.imovie.mogic.mine.AutoScrollActivity;
import com.imovie.mogic.mine.SetingHallActivity;
import com.imovie.mogic.utills.Utills;
import com.imovie.mogic.widget.HorizontalListView;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class DetailActivity extends BaseActivity implements AddWidget.OnAddClick {
	private FoodBean foodBean;
	private AddWidget detail_add;
	public BottomSheetBehavior behavior;
	private ShopCarView shopCarView;
	private CarAdapter carAdapter;
	private CoordinatorLayout detail_main;
//	private DetailHeaderBehavior dhb;
	private View headerView;
	public HorizontalListView lvGoodsTagList;
	private int position = -1;
	private TextView detail_sale;
	private TextView detail_price;
	public GoodsTagAdapter adapter;
	public String amountStr = "0.00";


	protected int getLayoutId() { return R.layout.activity_detail; }

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);
		foodBean = (FoodBean) getIntent().getSerializableExtra("food");
		position = getIntent().getIntExtra("position", -1);
		initViews();
	}

	private void initViews() {
		detail_main = (CoordinatorLayout) findViewById(R.id.detail_main);
		headerView = findViewById(R.id.headerview);
//		CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) headerView.getLayoutParams();
//		dhb = (DetailHeaderBehavior) lp.getBehavior();
		ImageView iv_detail = (ImageView) findViewById(R.id.iv_detail);
//		iv_detail.setImageResource(foodBean.getIcon());
//		TextView toolbar_title = (TextView) findViewById(R.id.toolbar_title);
//		toolbar_title.setText(foodBean.getName());
		TextView detail_name = (TextView) findViewById(R.id.detail_name);
		detail_name.setText(foodBean.getName());
		detail_sale = (TextView) findViewById(R.id.detail_sale);
//		detail_sale.setText(foodBean.getSale() + " 好评率95%");
		detail_price = (TextView) findViewById(R.id.detail_price);
		detail_price.setText(foodBean.getStrPrice(getApplicationContext()));

		lvGoodsTagList = (HorizontalListView) findViewById(R.id.lvGoodsTagList);
		if(foodBean.goodsPackList.size()>0){
			detail_sale.setVisibility(View.VISIBLE);
			lvGoodsTagList.setVisibility(View.VISIBLE);
			adapter = new GoodsTagAdapter(DetailActivity.this,foodBean.goodsPackList);
			lvGoodsTagList.setAdapter(adapter);
		}else{
			detail_sale.setVisibility(View.GONE);
			lvGoodsTagList.setVisibility(View.GONE);
		}


		detail_add = (AddWidget) findViewById(R.id.detail_add);
		detail_add.setData(this, foodBean);
//		detail_add.setState(foodBean.getSelectCount());
//		initRecyclerView();

		lvGoodsTagList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
				GoodTagList tagList = (GoodTagList) adapterView.getItemAtPosition(position);
//				Utills.showShortToast("tagList"+tagList.name);
			}
		});
		initShopCar();
	}

	public void close(View view) {
		finish();
	}

	private void initRecyclerView() {
		RecyclerView recyclerView = (RecyclerView) findViewById(R.id.detail_recyclerView);
		recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
		recyclerView.addItemDecoration(new SpaceItemDecoration());
		((DefaultItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
//		dAdapter = new DetailAdapter(ListContainer.commandList, this);
//		View header = View.inflate(getApplicationContext(), R.layout.item_detail_header, null);
//		dAdapter.addHeaderView(header);
//		TextView footer = new TextView(getApplicationContext());
//		ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewUtils.dip2px(getApplicationContext(), 100));
//		footer.setText("已经没有更多了");
//		footer.setTextSize(12);
//		footer.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.T3));
//		footer.setGravity(Gravity.CENTER_HORIZONTAL);
//		footer.setPadding(0, 30, 0, 0);
//		footer.setLayoutParams(lp);
//		dAdapter.addFooterView(footer);
//		dAdapter.bindToRecyclerView(recyclerView);
	}


	private void initShopCar() {
		shopCarView = (ShopCarView) findViewById(R.id.car_mainfl);
		final View blackView = findViewById(R.id.blackview);
		behavior = BottomSheetBehavior.from(findViewById(R.id.car_container));
		behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
			@Override
			public void onStateChanged(@NonNull View bottomSheet, int newState) {
				shopCarView.sheetScrolling = false;
//				dhb.setDragable(false);
				if (newState == BottomSheetBehavior.STATE_COLLAPSED || newState == BottomSheetBehavior.STATE_HIDDEN) {
					blackView.setVisibility(View.GONE);
//					dhb.setDragable(true);
				}
			}

			@Override
			public void onSlide(@NonNull View bottomSheet, float slideOffset) {
				shopCarView.sheetScrolling = true;
				blackView.setVisibility(View.VISIBLE);
				ViewCompat.setAlpha(blackView, slideOffset);
			}
		});
		blackView.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View view, MotionEvent motionEvent) {
				behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
				return true;
			}
		});
		shopCarView.setBehavior(behavior);
		RecyclerView carRecView = (RecyclerView) findViewById(R.id.car_recyclerview);
		carRecView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

		carRecView.addOnItemTouchListener(new OnItemClickListener() {
			@Override
			public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
				super.onItemChildClick(adapter, view, position);
				Utills.showShortToast("999"+position);
//				if (view.getId() == R.id.car_main) {
//					Intent intent = new Intent(DetailActivity.this, DetailActivity.class);
//					intent.putExtra("food", (FoodBean) adapter.getData().get(position));
//					intent.putExtra("position", position);
//					startActivity(intent);
//					overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
//				}
			}

			@Override
			public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {

			}
		});

		((DefaultItemAnimator) carRecView.getItemAnimator()).setSupportsChangeAnimations(false);
		ArrayList<FoodBean> flist = new ArrayList<>();
		flist.addAll(SelectTypeActivity.carAdapter.getData());
		carAdapter = new CarAdapter(flist, this);
		carAdapter.bindToRecyclerView(carRecView);
		handleCommand(foodBean);
		shopCarView.post(new Runnable() {
			@Override
			public void run() {
				dealCar(foodBean);
			}
		});
	}

	@Override
	public void onAddClick(View view, FoodBean fb) {
		dealCar(fb);
		ViewUtils.addTvAnim(view, shopCarView.carLoc, getApplicationContext(), detail_main);
//		if (!dhb.canDrag) {
//			ViewAnimator.animate(headerView).alpha(1, -4).duration(410).onStop(new AnimationListener.Stop() {
//				@Override
//				public void onStop() {
//					finish();
//				}
//			}).start();
//		}
	}

	@Override
	public void onSubClick(FoodBean fb) {
		dealCar(fb);
	}


	private void dealCar(FoodBean foodBean) {
		BigDecimal amount = new BigDecimal(0.0);
		int total = 0;
		boolean hasFood = false;
		if (behavior.getState() == BottomSheetBehavior.STATE_EXPANDED || foodBean.getId() == this.foodBean.getId() && foodBean.getSelectCount() !=
				this.foodBean.getSelectCount()) {
			this.foodBean = foodBean;
			detail_add.expendAdd(foodBean.getSelectCount());
			handleCommand(foodBean);
		}
		List<FoodBean> flist = carAdapter.getData();
		int p = -1;
		for (int i = 0; i < flist.size(); i++) {
			FoodBean fb = flist.get(i);
			total += fb.getSelectCount();
			amount = amount.add(fb.getPrice().multiply(BigDecimal.valueOf(fb.getSelectCount())));
			if (fb.getId() == foodBean.getId()) {
				hasFood = true;
				if (foodBean.getSelectCount() == 0) {
					p = i;
				} else {
					carAdapter.setData(i, foodBean);
				}
			}
		}
		if (p >= 0) {
			carAdapter.remove(p);
		} else if (!hasFood && foodBean.getSelectCount() > 0) {
			carAdapter.addData(foodBean);
			amount = amount.add(foodBean.getPrice().multiply(BigDecimal.valueOf(foodBean.getSelectCount())));
			total += foodBean.getSelectCount();
		}
		shopCarView.showBadge(total);
		shopCarView.updateAmount(amount);
		amountStr = amount.toString();
		Intent intent = new Intent(SelectTypeActivity.CAR_ACTION);
		if (foodBean.getId() == this.foodBean.getId()) {
			intent.putExtra("position", position);
		}
		intent.putExtra("foodbean", foodBean);
		sendBroadcast(intent);
	}

	private class SpaceItemDecoration extends RecyclerView.ItemDecoration {

		private int space;

		SpaceItemDecoration() {
			this.space = ViewUtils.dip2px(getApplicationContext(), 2);
		}

		@Override
		public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
			outRect.left = space;
			outRect.top = space;
			outRect.right = space;
			if (parent.getChildLayoutPosition(view) % 2 == 0) {
				outRect.left = 0;
			}
		}

	}

	private void handleCommand(FoodBean foodBean) {
//		for (int i = 0; i < dAdapter.getData().size(); i++) {
//			FoodBean fb = dAdapter.getItem(i);
//			if (fb.getId() == foodBean.getId() && fb.getSelectCount() != foodBean.getSelectCount()) {
//				fb.setSelectCount(foodBean.getSelectCount());
//				dAdapter.setData(i, fb);
//				dAdapter.notifyItemChanged(i);
//				break;
//			}
//		}
	}

	public void goAccount(View view) {
		if(carAdapter.getItemCount()<=0){
			Utills.showShortToast("未选购商品");
			return;
		}else{
			Intent intent = new Intent(DetailActivity.this, CarPayActivity.class);
			intent.putExtra("FoodBeanList", (Serializable) carAdapter.getData());
			intent.putExtra("amountStr",amountStr);
			startActivity(intent);
		}
	}




	public void clearCar(View view) {
		ViewUtils.showClearCar(DetailActivity.this, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				List<FoodBean> flist = carAdapter.getData();
				for (int i = 0; i < flist.size(); i++) {
					FoodBean fb = flist.get(i);
					fb.setSelectCount(0);
				}
				carAdapter.setNewData(new ArrayList<FoodBean>());
				shopCarView.showBadge(0);
				shopCarView.updateAmount(new BigDecimal(0.0));
				sendBroadcast(new Intent(SelectTypeActivity.CLEARCAR_ACTION));
			}
		});
	}

}
