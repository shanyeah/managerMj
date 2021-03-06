package com.imovie.mogic.myRank.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;

import com.imovie.mogic.R;
import com.imovie.mogic.myRank.adater.PraiseAdapter;
import com.imovie.mogic.myRank.adater.PraiseNumAdapter;
import com.imovie.mogic.myRank.model.PraiseMode;
import com.imovie.mogic.myRank.net.RankWebHelper;
import com.imovie.mogic.web.IModelResultListener;
import com.imovie.mogic.web.model.HttpResultModel;
import com.imovie.mogic.widget.FlexibleFrameLayout;
import com.imovie.mogic.widget.PullToRefreshFrameLayout;
import com.imovie.mogic.widget.YSBPageListView;

import java.util.ArrayList;
import java.util.List;

public class PraiseNumFragment extends Fragment {

	private PullToRefreshFrameLayout pull_content;
	private FlexibleFrameLayout ff_list;
	private LinearLayout ll_ad;
	public YSBPageListView lvRatingList;
	public PraiseNumAdapter adapter;
	public List<PraiseMode> list;
	public int typeNum = 1;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v=inflater.inflate(R.layout.my_rank_praise_fragment, container, false);
		initView(v);
		setView();
		return v;
	}

	private void initView(View v) {
		pull_content = (PullToRefreshFrameLayout) v.findViewById(R.id.pull_content);
		ff_list = (FlexibleFrameLayout) v.findViewById(R.id.ff_list);
		ll_ad = (LinearLayout) v.findViewById(R.id.ll_ad);
		lvRatingList = (YSBPageListView) v.findViewById(R.id.lvRatingList);

	}

	private void setView() {
		setPullAndFlexListener();
		list = new ArrayList<>();
		adapter = new PraiseNumAdapter(getContext(),list);
		lvRatingList.setAdapter(adapter);
//		lvRatingList.setEmptyTips("暂无数据");
		getPraiseList(typeNum);

//		btHallRating.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				RatingBarPopupWindow ratingBarPopupWindow = new RatingBarPopupWindow(getContext());
//				ratingBarPopupWindow.showPopupWindow(new OnPhotoPopupWindowItemClickListener() {
//					@Override
//					public void onTakePhotoClick() {
//
//					}
//
//					@Override
//					public void onChoosePhotoClick() {
//
//					}
//
//					@Override
//					public void onCancelClick() {
//
//					}
//				});
//
//			}
//		});
//
	}

	private void setPullAndFlexListener(){
		ff_list.setFlexView(ll_ad);
		ff_list.setFlexible(true);

		ff_list.setOnFlexChangeListener(new FlexibleFrameLayout.OnFlexChangeListener() {
			@Override
			public void onFlexChange(int flexHeight, int currentFlexHeight, boolean isOnTop) {
				if (isOnTop) {
					pull_content.setPullEnable(true);
				} else {
					pull_content.setPullEnable(false);
				}
			}

		});
		pull_content.setOnPullToRefreshListener(new PullToRefreshFrameLayout.OnPullToRefreshListener() {
			@Override
			public void onRefresh() {
				getPraiseList(typeNum);
			}
		});

		lvRatingList.setOnScrollListener(new AbsListView.OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				if (scrollState == SCROLL_STATE_IDLE) {

					//列表处于最上方
					if (true && view.getChildAt(0).getTop() == 0) {
						ff_list.setFlexible(true);
					} else {
						ff_list.setFlexible(false);
					}
				}

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

			}
		});


	}
	public void refreshData(int type){
		typeNum = type;
		getPraiseList(typeNum);
	}

	public void getPraiseList(int type){
		RankWebHelper.getPraiseNum(type,new IModelResultListener<PraiseMode>() {
			@Override
			public boolean onGetResultModel(HttpResultModel resultModel) {
				pull_content.endRefresh(true);
				lvRatingList.finishLoading(true);
				return false;
			}

			@Override
			public void onSuccess(String resultCode, PraiseMode resultModel, List<PraiseMode> resultModelList, String resultMsg, String hint) {

				try {
					pull_content.endRefresh(true);
					lvRatingList.finishLoading(true);
					if(resultModelList.size()>0){
						adapter.list = resultModelList;
						adapter.typeNum = typeNum;
						adapter.notifyDataSetChanged();
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFail(String resultCode, String resultMsg, String hint) {
				pull_content.endRefresh(true);
				lvRatingList.finishLoading(true);
			}

			@Override
			public void onError(String errorMsg) {
//				pull_content.endRefresh(true);
//				lvRatingList.finishLoading(true);
			}
		});
	}
}
