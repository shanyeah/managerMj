package com.imovie.mogic.car;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.imovie.mogic.car.view.ShopCarView;
import com.imovie.mogic.home.BaseActivity;
import com.imovie.mogic.home.SelectTypeActivity;
import com.imovie.mogic.home.adater.CategorysAdapter;
import com.imovie.mogic.home.adater.GoodsTagAdapter;
import com.imovie.mogic.home.adater.GoodsTagChildAdapter;
import com.imovie.mogic.home.model.CategorysModel;
import com.imovie.mogic.home.model.GoodTagList;
import com.imovie.mogic.home.model.GoodsModel;
import com.imovie.mogic.home.model.PackReplaceList;
import com.imovie.mogic.home.model.PayResultModel;
import com.imovie.mogic.home.net.HomeWebHelper;
import com.imovie.mogic.utills.DecimalUtil;
import com.imovie.mogic.utills.StringHelper;
import com.imovie.mogic.utills.Utills;
import com.imovie.mogic.web.IModelResultListener;
import com.imovie.mogic.web.model.HttpResultModel;
import com.imovie.mogic.widget.FlexibleFrameLayout;
import com.imovie.mogic.widget.HorizontalListView;
import com.imovie.mogic.widget.NoScrollListView;
import com.imovie.mogic.widget.PullToRefreshFrameLayout;
import com.imovie.mogic.widget.SelectTagPopupWindow;
import com.imovie.mogic.widget.TitleBar;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class DetailActivity extends BaseActivity implements AddWidget.OnAddClick {
	private TitleBar titleBar;
	private FoodBean foodBean;
	private AddWidget detail_add;
	public BottomSheetBehavior behavior;
	private ShopCarView shopCarView;
	private CarAdapter carAdapter;
	private CoordinatorLayout detail_main;
	private PullToRefreshFrameLayout pull_content;
	private FlexibleFrameLayout ff_list;
	private LinearLayout ll_ad;
//	private DetailHeaderBehavior dhb;
	private View headerView;
	public HorizontalListView lvGoodsTagList;
	public HorizontalListView goodsPackList;
	public GoodsTagChildAdapter tagAdapter;
	public List<GoodTagList> packsList = new ArrayList<>();
	public List<GoodTagList> packList = new ArrayList<>();
	public List<PackReplaceList> packReplaceLists = new ArrayList<>();
	public LinearLayout llPackListState;
	private int position = -1;
	private int selectIndex = -1;
	private TextView detail_sale;
	private TextView detail_price;
    private TextView tvAddOneCar;
	private ImageView iv_detail;
	public GoodsTagAdapter adapter;
	public String amountStr = "0.00";
	public NoScrollListView lvCategorysList;
	public CategorysAdapter categorysAdapter;
	public List<CategorysModel.Categorys> categorys = new ArrayList<>();
	public List<CategorysModel> goodsTagsList = new ArrayList<>();



	protected int getLayoutId() { return R.layout.activity_detail; }

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);
		foodBean = (FoodBean) getIntent().getSerializableExtra("food");
		position = getIntent().getIntExtra("position", -1);
		initViews();
		setViews();
		queryGoodsDetail(foodBean.getId());
	}

	@Override
	protected void onResume() {
		super.onResume();
		shopCarView.post(new Runnable() {
			@Override
			public void run() {
				dealBeginCar(foodBean);
			}
		});
	}

	private void initViews() {
		detail_main = (CoordinatorLayout) findViewById(R.id.detail_main);
		titleBar = (TitleBar) findViewById(R.id.title_bar);
		titleBar.setTitle("订单详情");
		titleBar.setLeftListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
//		pull_content = (PullToRefreshFrameLayout) findViewById(R.id.pull_content);
//		ff_list = (FlexibleFrameLayout) findViewById(R.id.ff_list);
//		ll_ad = (LinearLayout) findViewById(R.id.ll_ad);
		headerView = findViewById(R.id.headerview);
//		CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) headerView.getLayoutParams();
//		dhb = (DetailHeaderBehavior) lp.getBehavior();
		iv_detail = (ImageView) findViewById(R.id.iv_detail);
//		iv_detail.setImageResource(foodBean.getIcon());
		tvAddOneCar = (TextView) findViewById(R.id.tvAddOneCar);
//		toolbar_title.setText(foodBean.getName());
		TextView detail_name = (TextView) findViewById(R.id.detail_name);
		detail_name.setText(foodBean.getName());
		detail_sale = (TextView) findViewById(R.id.detail_sale);
//		detail_sale.setText(foodBean.getSale() + " 好评率95%");
		detail_price = (TextView) findViewById(R.id.detail_price);
		detail_price.setText(foodBean.getStrPrice(getApplicationContext()));
		lvCategorysList = (NoScrollListView) findViewById(R.id.lvCategorysList);
		llPackListState = (LinearLayout) findViewById(R.id.llPackListState);
		lvGoodsTagList = (HorizontalListView) findViewById(R.id.lvGoodsTagList);
		goodsPackList = (HorizontalListView) findViewById(R.id.goodsPackList);
		adapter = new GoodsTagAdapter(DetailActivity.this,packsList);
        adapter.setOnSelectListener(new GoodsTagAdapter.onSelectListener() {
            @Override
            public void onSelect(GoodTagList tag) {
				List<CategorysModel.Categorys> categorys = new ArrayList<>();
				if(goodsTagsList.size()>0){
					for(int k=0;k<goodsTagsList.size();k++) {
						if(tag.goodsId == goodsTagsList.get(k).goodsId){
							categorys.addAll(goodsTagsList.get(k).categorys);
						}
					}

				}
				SelectTagPopupWindow popupWindow = new SelectTagPopupWindow(DetailActivity.this,categorys,tag);
				popupWindow.showPopupWindow();
				popupWindow.setOnSelectListener(new SelectTagPopupWindow.onSelectListener() {
					@Override
					public void onSelect(GoodTagList tag) {
//						Utills.showShortToast(""+tag.tagsName);
						for(int i=0;i<adapter.list.size();i++){
							if(adapter.list.get(i).goodsId == tag.goodsId && adapter.list.get(i).selectId == tag.goodsId){
								adapter.list.get(i).tagsName = tag.tagsName;
								break;
							}
						}
						foodBean.getGoodsPackList().clear();
						foodBean.setGoodsPackList(adapter.list);
					}
				});
            }

        });
		lvGoodsTagList.setAdapter(adapter);
		if(foodBean.getGoodsPackList().size()>0){
			detail_sale.setVisibility(View.VISIBLE);
			lvGoodsTagList.setVisibility(View.VISIBLE);
		}else{
			detail_sale.setVisibility(View.GONE);
			lvGoodsTagList.setVisibility(View.GONE);
		}


		detail_add = (AddWidget) findViewById(R.id.detail_add);
//		detail_add.setAddButton(true);
		detail_add.setState(foodBean.getSelectCount());
		detail_add.setData((AddWidget.OnAddClick) this, foodBean);


//		initRecyclerView();


		initShopCar();
	}
	private void setViews() {
//		setPullAndFlexListener();

		categorysAdapter = new CategorysAdapter(DetailActivity.this,categorys);
		lvCategorysList.setAdapter(categorysAdapter);
		tagAdapter = new GoodsTagChildAdapter(DetailActivity.this,packList);
		goodsPackList.setAdapter(tagAdapter);

		lvGoodsTagList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
//				GoodTagList tagList = (GoodTagList) adapterView.getItemAtPosition(position);
				selectIndex = position;
				GoodsTagAdapter goodsTagAdapter = (GoodsTagAdapter) adapterView.getAdapter();
				goodsTagAdapter.setSelectIndex(position);
				int packGroupId = goodsTagAdapter.getItem(position).packGroupId;
				if(packReplaceLists.size()>0) {
					for (int i = 0; i < packReplaceLists.size(); i++) {
						if(packGroupId == packReplaceLists.get(i).packGroupId){
							goodsPackList.setVisibility(View.VISIBLE);
//					llPackListState.setVisibility(View.VISIBLE);
							tagAdapter.list.clear();
							tagAdapter.list.addAll(packReplaceLists.get(i).replaceList);
							tagAdapter.setIndex();
							tagAdapter.notifyDataSetChanged();
						}else{
							goodsPackList.setVisibility(View.GONE);
//					llPackListState.setVisibility(View.GONE);
						}

					}
				}

//				Utills.showShortToast("tagList"+tagList.name);
			}
		});

		goodsPackList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
//				GoodTagList tagList = (GoodTagList) adapterView.getItemAtPosition(position);
				GoodsTagChildAdapter childAdapter = (GoodsTagChildAdapter) adapterView.getAdapter();
				childAdapter.setSelectIndex(childAdapter.getItem(position).id);
				adapter.setSelectItem(selectIndex,childAdapter.getItem(position));
				if(foodBean.getGoodsPackList().size()>0) {
					foodBean.getGoodsPackList().clear();
					foodBean.setGoodsPackList(adapter.list);
					double totalPrices = 0;
					for(int i=0;i<adapter.list.size();i++){
						totalPrices += adapter.list.get(i).packPrice;
					}
					foodBean.setPrice(new BigDecimal(Double.toString(totalPrices)));
					detail_price.setText("¥"+DecimalUtil.FormatMoney(totalPrices));

				}

//				if(goodsTagAdapter.getItem(position).packList.size()>0){
//					goodsPackList.setVisibility(View.VISIBLE);
////					llPackListState.setVisibility(View.VISIBLE);
//					tagAdapter.list.clear();
//					tagAdapter.list.addAll(goodsTagAdapter.getItem(position).packList);
//					tagAdapter.notifyDataSetChanged();
//				}else{
//					goodsPackList.setVisibility(View.GONE);
////					llPackListState.setVisibility(View.GONE);
//				}


//				Utills.showShortToast("tagList"+tagList.name);
			}
		});

        tvAddOneCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
				try {
					if(categorysAdapter.list.size()>0) {
						//			Utills.showShortToast(""+categorysAdapter.getTags()[1]);
						foodBean.setGoodsTags(categorysAdapter.getTags()[0]);
						foodBean.setTagsName(categorysAdapter.getTags()[1]);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
                addOneCar(foodBean);
                ViewUtils.addTvAnim(view, shopCarView.carLoc, getApplicationContext(), detail_main);
            }
        });
	}

	private void setPullAndFlexListener(){
//		ff_list.setFlexView(ll_ad);
//		ff_list.setFlexible(true);
//
//		ff_list.setOnFlexChangeListener(new FlexibleFrameLayout.OnFlexChangeListener() {
//			@Override
//			public void onFlexChange(int flexHeight, int currentFlexHeight, boolean isOnTop) {
//				if (isOnTop) {
//					pull_content.setPullEnable(true);
//				} else {
//					pull_content.setPullEnable(false);
//				}
//			}
//
//		});
		pull_content.setOnPullToRefreshListener(new PullToRefreshFrameLayout.OnPullToRefreshListener() {
			@Override
			public void onRefresh() {
				queryGoodsDetail(foodBean.getId());
			}
		});


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
		carRecView.setLayoutManager(new LinearLayoutManager(DetailActivity.this));

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
		List<FoodBean> flist = new ArrayList<>();
//		List<FoodBean> list = SelectTypeActivity.carAdapter.getData();
//		for(int i= 0;i<list.size();i++){
//			FoodBean food = new FoodBean();
//			food.getFoodBean(list.get(i));
//			flist.add(food.getFoodBean(list.get(i)));
//		}
//		flist.addAll(SelectTypeActivity.carAdapter.getData());
		carAdapter = new CarAdapter(MyApplication.getInstance().getCarListData(), this);
		carAdapter.bindToRecyclerView(carRecView);
		handleCommand(foodBean);
		shopCarView.post(new Runnable() {
			@Override
			public void run() {
//				dealCar(foodBean);
//                addOneCar(foodBean);
				dealBeginCar(foodBean);
			}
		});
	}

	@Override
	public void onAddClick(View view, FoodBean fb) {
		try {
			if(categorysAdapter.list.size()>0) {
    //			Utills.showShortToast(""+categorysAdapter.getTags()[1]);
                fb.setGoodsTags(categorysAdapter.getTags()[1]);
                fb.setTagsName(categorysAdapter.getTags()[1]);
            }
		} catch (Exception e) {
			e.printStackTrace();
		}
//		Utills.showShortToast(""+adapter.list.size());
//		dealCar(fb);
		addOneCar(fb);
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
//		dealCar(fb);
		subOneCar(fb);
	}


	private void dealCar(FoodBean food) {
		FoodBean foodBean = food.getFoodBean(food);
		BigDecimal amount = new BigDecimal(0.0);
		int total = 0;
		boolean hasFood = false;
		boolean hasPack = false;
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
			if (fb.getId() == foodBean.getId()) {

				if(foodBean.getGoodsPackList().size()>0){
					if(foodBean.getGoodsPackList().size()!=fb.getGoodsPackList().size()){
						hasFood = false;
					}else{
						for(int j=0;j<foodBean.getGoodsPackList().size();j++){
							if(foodBean.getGoodsPackList().get(j).getGoodsId()!=fb.getGoodsPackList().get(j).getGoodsId()){
								hasPack = true;
								break;
							}
						}
						if(hasPack){
							hasFood = false;
//                            foodBean.setSelectCount(1);
						}else{
							hasFood = true;
							if (foodBean.getSelectCount() == 0) {
								p = i;
							} else {
								carAdapter.setData(i, foodBean);
							}
//                            if (typeSelect.containsKey(fb.getType())) {
//                                typeSelect.put(fb.getType(), typeSelect.get(fb.getType()) + fb.getSelectCount());
//                            } else {
//                                typeSelect.put(fb.getType(), fb.getSelectCount());
//                            }
						}

					}

//					    Log.e("----111",foodBean.getGoodsPackList().size()+"=:="+fb.getGoodsPackList().size());
//                        Log.e("----222",""+hasFood);
				}else{
					hasFood = true;
//                    foodBean.setSelectCount(foodBean.getSelectCount()+1);
					if (foodBean.getSelectCount() == 0) {
						p = i;
					} else {
						carAdapter.setData(i, foodBean);
						total += foodBean.getSelectCount();
						amount = amount.add(foodBean.getPrice());
					}
//                    if (typeSelect.containsKey(foodBean.getType())) {
//                        typeSelect.put(foodBean.getType(), typeSelect.get(foodBean.getType()) + foodBean.getSelectCount());
//                    } else {
//                        typeSelect.put(foodBean.getType(), foodBean.getSelectCount());
//                    }
				}
				total += foodBean.getSelectCount();
				amount = amount.add(foodBean.getPrice().multiply(BigDecimal.valueOf(foodBean.getSelectCount())));
			}else{
//				total += fb.getSelectCount();
//				amount = amount.add(fb.getPrice().multiply(BigDecimal.valueOf(fb.getSelectCount())));
			}


		}
		if (p >= 0) {
			carAdapter.remove(p);
		} else if (!hasFood) {
//            foodBean.getSelectCount() > 0
			foodBean.setSelectCount(1);
			carAdapter.addData(foodBean);
//            if (typeSelect.containsKey(foodBean.getType())) {
//                typeSelect.put(foodBean.getType(), typeSelect.get(foodBean.getType()) + foodBean.getSelectCount());
//            } else {
//                typeSelect.put(foodBean.getType(), foodBean.getSelectCount());
//            }
			amount = amount.add(foodBean.getPrice().multiply(BigDecimal.valueOf(foodBean.getSelectCount())));
			total += foodBean.getSelectCount();
		}
		shopCarView.showBadge(total);
		shopCarView.updateAmount(amount);
		amountStr = amount.toString();
		Intent intent = new Intent(SelectTypeActivity.CAR_ACTION);
//		if (foodBean.getId() == this.foodBean.getId()) {
//			intent.putExtra("position", position);
//		}
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
//		for (int i = 0; i < carAdapter.getData().size(); i++) {
//			FoodBean fb = carAdapter.getItem(i);
////			if(fb.getId() == foodBean.getId() && fb.goodsPackList.size()>0 ){
////				for(int k=0;k<foodBean.goodsPackList.size();k++){
////
////				}
////
////			}
//			if (fb.getId() == foodBean.getId() && fb.getSelectCount() != foodBean.getSelectCount()) {
////				if(fb.goodsPackList.size()>0){
////
////				}
//				fb.setSelectCount(foodBean.getSelectCount());
//				carAdapter.setData(i, fb);
//				carAdapter.notifyItemChanged(i);
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
				MyApplication.getInstance().getCarListData().clear();
				carAdapter.notifyDataSetChanged();
//				carAdapter.setNewData(new ArrayList<FoodBean>());
				shopCarView.showBadge(0);
				shopCarView.updateAmount(new BigDecimal(0.0));
				sendBroadcast(new Intent(SelectTypeActivity.CLEARCAR_ACTION));
			}
		});
	}

	public void queryGoodsDetail(long saleBillId){

		HomeWebHelper.queryGoodsDetail(saleBillId,new IModelResultListener<GoodsModel>() {
			@Override
			public boolean onGetResultModel(HttpResultModel resultModel) {
//				pull_content.endRefresh(true);
				return false;
			}

			@Override
			public void onSuccess(String resultCode, GoodsModel resultModel, List<GoodsModel> resultModelList, String resultMsg, String hint) {
//				pull_content.endRefresh(true);
				if(resultCode.equals("0")) {
					if(resultModel.goodsPackList.size()>0){
						detail_sale.setVisibility(View.VISIBLE);
						lvGoodsTagList.setVisibility(View.VISIBLE);
						llPackListState.setVisibility(View.VISIBLE);

						if(resultModel.packReplaceList.size()>0) {
							packReplaceLists.addAll(resultModel.packReplaceList);
							for (int i = 0; i < resultModel.goodsPackList.size(); i++) {
								for (int j = 0; j < packReplaceLists.size(); j++) {
									if(resultModel.goodsPackList.get(i).packGroupId == packReplaceLists.get(j).packGroupId){
										resultModel.goodsPackList.get(i).hasChild = true;
										break;
									}
								}
							}
						}

						if(resultModel.goodsTagsList.size()>0){
							goodsTagsList.clear();
							goodsTagsList.addAll(resultModel.goodsTagsList);

							for (int i = 0; i < resultModel.goodsPackList.size(); i++) {
								for (int j = 0; j < resultModel.goodsTagsList.size(); j++) {
									if(resultModel.goodsPackList.get(i).goodsId == resultModel.goodsTagsList.get(j).goodsId){
										resultModel.goodsPackList.get(i).hasTag = true;
										break;
									}
								}
							}
						}

						adapter.list.clear();
						adapter.list.addAll(resultModel.goodsPackList);
						adapter.notifyDataSetChanged();
					}else{
						detail_sale.setVisibility(View.GONE);
						lvGoodsTagList.setVisibility(View.GONE);
						llPackListState.setVisibility(View.GONE);
						if(resultModel.goodsTagsList.size()>0){
							lvCategorysList.setVisibility(View.VISIBLE);
							categorysAdapter.list.clear();
							for(int k=0;k<resultModel.goodsTagsList.size();k++) {
								if(resultModel.goodsId == resultModel.goodsTagsList.get(k).goodsId){
									categorysAdapter.list.addAll(resultModel.goodsTagsList.get(k).categorys);
								}
							}
							categorysAdapter.notifyDataSetChanged();
						}else{
							lvCategorysList.setVisibility(View.GONE);
						}
					}


					DisplayImageOptions mOption = new DisplayImageOptions.Builder()
							.showImageOnLoading(R.drawable.food0)
							.showImageOnFail(R.drawable.food0)
							.showImageForEmptyUri(R.drawable.food0)
							.cacheInMemory(true)
							.cacheOnDisk(true)
							.build();
					ImageLoader.getInstance().displayImage(resultModel.imageUrl,iv_detail,mOption);

				}else{
					Utills.showShortToast(resultMsg);
				}
			}

			@Override
			public void onFail(String resultCode, String resultMsg, String hint) {
//                lvCard.finishLoading(true);
//				pull_content.endRefresh(true);
			}

			@Override
			public void onError(String errorMsg) {
//                lvCard.finishLoading(true);
//				pull_content.endRefresh(true);
			}
		});
	}

	private void addOneCar(FoodBean food) {
		FoodBean foodBean = food.getFoodBean(food);
		HashMap<String, Long> typeSelect = new HashMap<>();//更新左侧类别badge用
		BigDecimal amount = new BigDecimal(0.0);
		int total = 0;
		boolean hasFood = false;
		boolean hasPack = false;
        if (behavior.getState() == BottomSheetBehavior.STATE_EXPANDED || foodBean.getId() == this.foodBean.getId() && foodBean.getSelectCount() !=
            this.foodBean.getSelectCount()) {
//            this.foodBean = foodBean;
            detail_add.expendAdd(foodBean.getSelectCount());
            handleCommand(foodBean);
        }
		List<FoodBean> flist = carAdapter.getData();
		int p = -1;
		for (int i = 0; i < flist.size(); i++) {
			FoodBean fb = flist.get(i);
			if (fb.getId() == foodBean.getId()) {
				if(foodBean.getGoodsPackList().size()>0){
					if(fb.getGoodsPackList().containsAll(foodBean.getGoodsPackList())){
						hasFood = true;
						hasPack = true;
						fb.setSelectCount(fb.getSelectCount()+1);
						carAdapter.setData(i, fb);
					}else{
						if(hasPack){
							hasFood = true;
						}else{
							hasFood = false;
						}
					}
				}else{
					if(!fb.getTagsName().equals(foodBean.getTagsName())){
						hasFood = false;
					}else {
						hasFood = true;
						fb.setSelectCount(fb.getSelectCount() + 1);
						carAdapter.setData(i, fb);
					}
				}

			}
			total += fb.getSelectCount();
			if (typeSelect.containsKey(fb.getType())) {
				typeSelect.put(fb.getType(), typeSelect.get(fb.getType()) + fb.getSelectCount());
			} else {
				typeSelect.put(fb.getType(), fb.getSelectCount());
			}
			amount = amount.add(fb.getPrice().multiply(BigDecimal.valueOf(fb.getSelectCount())));


		}
		if (p >= 0) {
			carAdapter.remove(p);
		} else if (!hasFood) {
//            foodBean.getSelectCount() > 0
//            foodBean.setSelectCount(1);
			carAdapter.addData(foodBean);
			if (typeSelect.containsKey(foodBean.getType())) {
				typeSelect.put(foodBean.getType(), typeSelect.get(foodBean.getType()) + foodBean.getSelectCount());
			} else {
				typeSelect.put(foodBean.getType(), foodBean.getSelectCount());
			}
			amount = amount.add(foodBean.getPrice().multiply(BigDecimal.valueOf(foodBean.getSelectCount())));
			total += foodBean.getSelectCount();
		}

		shopCarView.showBadge(total);
//		buyGoodsFragment.getTypeAdapter().updateBadge(typeSelect);
		shopCarView.updateAmount(amount);
		amountStr = amount.toString();

		Intent intent = new Intent(SelectTypeActivity.CAR_ACTION);
//        if (foodBean.getId() == this.foodBean.getId()) {
//            intent.putExtra("position", position);
//        }
		intent.putExtra("foodbean", foodBean);
		sendBroadcast(intent);
	}

	private void subOneCar(FoodBean foodBean) {
//        FoodBean foodBean = food.getFoodBean(food);
//		HashMap<String, Long> typeSelect = new HashMap<>();//更新左侧类别badge用
		BigDecimal amount = new BigDecimal(0.0);
		int total = 0;
		boolean hasFood = false;
		boolean hasPack = false;
		if (behavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
//			buyGoodsFragment.getFoodAdapter().notifyDataSetChanged();
		}

		List<FoodBean> flist = carAdapter.getData();
		int p = -1;
		for (int i = 0; i < flist.size(); i++) {
			FoodBean fb = flist.get(i);
			if (fb.getId() == foodBean.getId()) {
				if(foodBean.getGoodsPackList().size()>0){
					if(fb.getGoodsPackList().containsAll(foodBean.getGoodsPackList())){
						hasFood = true;
						hasPack = true;
						fb.setSelectCount(fb.getSelectCount()-1);
						carAdapter.setData(i, fb);
					}else{
						if(hasPack){
							hasFood = true;
						}else{
							hasFood = false;
						}
					}
				}else{
					if(!fb.getTagsName().equals(foodBean.getTagsName())){
						hasFood = false;
					}else {
						hasFood = true;
						fb.setSelectCount(fb.getSelectCount() - 1);
						carAdapter.setData(i, fb);
					}
				}

			}
			total += fb.getSelectCount();
//			if (typeSelect.containsKey(fb.getType())) {
//				typeSelect.put(fb.getType(), typeSelect.get(fb.getType()) + fb.getSelectCount());
//			} else {
//				typeSelect.put(fb.getType(), fb.getSelectCount());
//			}
			amount = amount.add(fb.getPrice().multiply(BigDecimal.valueOf(fb.getSelectCount())));
			if (fb.getSelectCount() <= 0) {
				p=i;
			}
		}

		shopCarView.showBadge(total);
//		buyGoodsFragment.getTypeAdapter().updateBadge(typeSelect);
		shopCarView.updateAmount(amount);
		amountStr = amount.toString();
		if(p>=0)carAdapter.remove(p);

		Intent intent = new Intent(SelectTypeActivity.CAR_ACTION);
//        if (foodBean.getId() == this.foodBean.getId()) {
//            intent.putExtra("position", position);
//        }
		intent.putExtra("foodbean", foodBean);
		sendBroadcast(intent);
	}

	private void dealBeginCar(FoodBean food) {
//		FoodBean foodBean = food.getFoodBean(food);
		BigDecimal amount = new BigDecimal(0.0);
		int total = 0;
		boolean hasFood = false;
		boolean hasPack = false;
		if (behavior.getState() == BottomSheetBehavior.STATE_EXPANDED || foodBean.getId() == this.foodBean.getId() && foodBean.getSelectCount() !=
				this.foodBean.getSelectCount()) {
			this.foodBean = foodBean;
			detail_add.expendAdd(foodBean.getSelectCount());
			handleCommand(foodBean);
		}
		List<FoodBean> flist = carAdapter.getData();
		for (int i = 0; i < flist.size(); i++) {
			FoodBean fb = flist.get(i);
			total += fb.getSelectCount();
			amount = amount.add(fb.getPrice().multiply(BigDecimal.valueOf(fb.getSelectCount())));
			if(fb.getSelectCount()<=0){
				carAdapter.remove(i);
			}

		}

		shopCarView.showBadge(total);
//		buyGoodsFragment.getTypeAdapter().updateBadge(typeSelect);
		shopCarView.updateAmount(amount);
		amountStr = amount.toString();
		shopCarView.showBadge(total);
		shopCarView.updateAmount(amount);
		amountStr = amount.toString();
	}

}
