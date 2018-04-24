package com.imovie.mogic.myRank.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.imovie.mogic.R;
import com.imovie.mogic.myRank.MovieDetailActivity;
import com.imovie.mogic.myRank.adater.MovieListAdapter;
import com.imovie.mogic.myRank.model.MovieCondition;
import com.imovie.mogic.myRank.model.MovieModel;
import com.imovie.mogic.myRank.net.RankWebHelper;
import com.imovie.mogic.login.model.TestModel;
import com.imovie.mogic.web.IModelResultListener;
import com.imovie.mogic.web.model.HttpResultModel;
import com.imovie.mogic.widget.PageGridView;
import com.imovie.mogic.widget.interfaces.IPageList;

import java.util.ArrayList;
import java.util.List;


public class MovieFragment extends Fragment {

	//分类
	RadioGroup mCatalogGroup;
	List<MovieCondition.MovieCat> moviecatList = new ArrayList<>();
//	Button btTest;

	RadioGroup mAreaGroup;
	List<MovieCondition.MovieCat> mMovieAreaList = new ArrayList<>();
	//年份
	RadioGroup mYearGroup;
	int groupTextSize = 20;
	List<MovieCondition.MovieCat> mMovieYearList = new ArrayList<>();

	public String mMovieCatalogSelection="0";
	public String mMovieAreaSelection="0";
	public String mMovieYearSelection="0";
	public int pageNo=0;

	public PageGridView gvMovieList;
	public MovieListAdapter movieAdapter;
	public List<MovieModel> movieList;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v=inflater.inflate(R.layout.discovery_fm_movie, container, false);
		initView(v);
		setView();
		setListener();
		return v;
	}
	private void initView(View view) {
		gvMovieList = (PageGridView) view.findViewById(R.id.gvMovieList);
		//分类
		mCatalogGroup = (RadioGroup) view.findViewById(R.id.catalog_radio_group);
		//地区
		mAreaGroup = (RadioGroup) view.findViewById(R.id.area_radio_group);
		//年份
		mYearGroup = (RadioGroup) view.findViewById(R.id.year_radio_group);



//        LinearLayout.LayoutParams slideShowViewParam=(LinearLayout.LayoutParams)adSlideBanner.getLayoutParams();
//        slideShowViewParam.width= AppConfig.getScreenWidth();
//        slideShowViewParam.height=(AppConfig.getScreenWidth()*244)/640;
//        adSlideBanner.setLayoutParams(slideShowViewParam);

	}

	private void setView() {
		movieList = new ArrayList<>();
		movieAdapter = new MovieListAdapter(getContext(),movieList);
		gvMovieList.setAdapter(movieAdapter);
		getMovieCondition(mMovieCatalogSelection,mMovieYearSelection,mMovieAreaSelection);
	}

	private void setListener() {
		gvMovieList.setOnPageListener(new IPageList.OnPageListener() {
			@Override
			public void onLoadMoreItems() {
				pageNo ++;
				getMovieData(pageNo,"",mMovieCatalogSelection,mMovieYearSelection,mMovieAreaSelection);
//				Utills.showShortToast(""+pageNo);

			}
		});


		gvMovieList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				MovieModel movieModel = movieAdapter.getItem(position);
				Intent intent = new Intent(getContext(), MovieDetailActivity.class);
				intent.putExtra("movieModel",movieModel);
				startActivity(intent);

			}
		});


		gvMovieList.setOnScrollListener(new AbsListView.OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
//				if (scrollState == SCROLL_STATE_IDLE) {
//
//					//列表处于最上方
//					if (isHaveAD && view.getChildAt(0).getTop() == 0) {
//						viewHolder.flexible_layout.setFlexible(true);
//
//					} else {
//						viewHolder.flexible_layout.setFlexible(false);
//
//					}
//				}

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

			}
		});

		gvMovieList.startLoad();
	}

	private void getMovieData(final int pageNo, String movieName, String movieCat, String movieTimes, String movieArea){
		if(movieCat.equals("0"))movieCat="";
		if(movieTimes.equals("0"))movieTimes="";
		if(movieArea.equals("0"))movieArea="";
		RankWebHelper.getMovieList(0,pageNo,20,"",movieCat,movieTimes,movieArea, new IModelResultListener<MovieModel>() {
			@Override
			public boolean onGetResultModel(HttpResultModel resultModel) {
				gvMovieList.finishLoading(true);
				return false;
			}

			@Override
			public void onSuccess(String resultCode, MovieModel resultModel, List<MovieModel> resultModelList, String resultMsg, String hint) {
				if(pageNo==1)movieAdapter.list.clear();
				movieAdapter.list.addAll(resultModelList);
				movieAdapter.notifyDataSetChanged();
				gvMovieList.finishLoading(resultModelList.size()==20);

			}

			@Override
			public void onFail(String resultCode, String resultMsg, String hint) {
				gvMovieList.finishLoading(true);
			}

			@Override
			public void onError(String errorMsg) {
				gvMovieList.finishLoading(true);
			}
		});
	}

	private void getMovieCondition(String movieCat,String movieTimes, String movieArea){
		if(movieCat.equals("0"))movieCat="";
		if(movieTimes.equals("0"))movieTimes="";
		if(movieArea.equals("0"))movieArea="";
		RankWebHelper.getMovieCondition("",movieCat,movieTimes,movieArea, new IModelResultListener<TestModel>() {
			@Override
			public boolean onGetResultModel(HttpResultModel resultModel) {
				return false;
			}

			@Override
			public void onSuccess(String resultCode, TestModel resultModel, List<TestModel> resultModelList, String resultMsg, String hint) {
//				Log.e("---e",resultCode);
				MovieCondition movieCondition = new MovieCondition();
				movieCondition.setModelByJson(resultCode);
				setConditionData(movieCondition);
			}

			@Override
			public void onFail(String resultCode, String resultMsg, String hint) {

			}

			@Override
			public void onError(String errorMsg) {

			}
		});
	}

	public void setConditionData(MovieCondition movieCondition){
		moviecatList.clear();
		mMovieAreaList.clear();
		mMovieYearList.clear();
		MovieCondition.MovieCat movieCat = new MovieCondition.MovieCat();
		movieCat.id = "0";
		movieCat.name="全部";
		moviecatList.add(movieCat);
		mMovieAreaList.add(movieCat);
		mMovieYearList.add(movieCat);

		moviecatList.addAll(movieCondition.movieCat);
		mMovieAreaList.addAll(movieCondition.movieArea);
		mMovieYearList.addAll(movieCondition.movieTimes);
		if(moviecatList.size()>0){
			mCatalogGroup.removeAllViews();
			for(int i=0;i<moviecatList.size();i++){
				RadioButton b = (RadioButton)LayoutInflater.from(getActivity()).inflate(R.layout.radio,mCatalogGroup,false);

				b.setTextSize(groupTextSize);
				b.setText(moviecatList.get(i).name);
				b.setTag(moviecatList.get(i));
//				Log.e("-----",""+moviecatList.get(i).id);
				if(mMovieCatalogSelection.equals(moviecatList.get(i).id)) {
//					b.setSelected(true);
					b.performClick();
				}
				b.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						MovieCondition.MovieCat m = (MovieCondition.MovieCat) v.getTag();
						mMovieCatalogSelection = m.id;
						pageNo=1;
						getMovieCondition(mMovieCatalogSelection,mMovieYearSelection,mMovieAreaSelection);
						getMovieData(pageNo,"",mMovieCatalogSelection,mMovieYearSelection,mMovieAreaSelection);
					}
				});
				mCatalogGroup.addView(b);
			}
		}
		if(mMovieAreaList.size()>0){
			mAreaGroup.removeAllViews();
			for(int i=0;i<mMovieAreaList.size();i++){
				RadioButton b = (RadioButton)LayoutInflater.from(getActivity()).inflate(R.layout.radio,mAreaGroup,false);
				b.setTextSize(groupTextSize);
				b.setText(mMovieAreaList.get(i).name);
				b.setTag(mMovieAreaList.get(i));
				if(mMovieAreaSelection.equals(mMovieAreaList.get(i).id)) {
//					b.setSelected(true);
					b.performClick();
				}
				b.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						MovieCondition.MovieCat m = (MovieCondition.MovieCat) v.getTag();
						mMovieAreaSelection = m.id;
						pageNo=1;
						getMovieCondition(mMovieCatalogSelection,mMovieYearSelection,mMovieAreaSelection);
						getMovieData(pageNo,"",mMovieCatalogSelection,mMovieYearSelection,mMovieAreaSelection);
					}
				});
				mAreaGroup.addView(b);
			}
		}

		if(mMovieYearList.size()>0){
			mYearGroup.removeAllViews();
			for(int i=0;i<mMovieYearList.size();i++){
				RadioButton b = (RadioButton)LayoutInflater.from(getActivity()).inflate(R.layout.radio,mYearGroup,false);
				b.setTextSize(groupTextSize);
				b.setText(mMovieYearList.get(i).name);
				b.setTag(mMovieYearList.get(i));
				if(mMovieYearSelection.equals(mMovieYearList.get(i).id)) {
//					b.setSelected(true);
					b.performClick();
				}
				b.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						MovieCondition.MovieCat m = (MovieCondition.MovieCat) v.getTag();
						mMovieYearSelection = m.id;
						pageNo=1;
						getMovieCondition(mMovieCatalogSelection,mMovieYearSelection,mMovieAreaSelection);
						getMovieData(pageNo,"",mMovieCatalogSelection,mMovieYearSelection,mMovieAreaSelection);
					}
				});
				mYearGroup.addView(b);
			}
		}
	}

}
