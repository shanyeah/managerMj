package com.imovie.mogic.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.imovie.mogic.R;
import com.imovie.mogic.widget.interfaces.IEmptyList;
import com.imovie.mogic.widget.interfaces.IPageHint;
import com.imovie.mogic.widget.interfaces.IPageList;


/**
 * 定制ListView.
 * 1.支持上拉分页加载功能。见接口IPageList
 * 2.支持空列表提示页功能。见接口IEmptyList
 * 3.支持页号提示功能。见接口IPageHint
 * 注：getListView()返回ListView
 * Created by zhou on 2015/12/23.
 */
public class YSBPageListView extends RelativeLayout implements IPageList, IEmptyList, IPageHint {
    // view
    protected PageListView lvPageListView;
    protected LinearLayout llListEmpty;
    protected ImageView ivListEmpty;
    protected TextView tvListEmpty;
    protected TextView tvPageTips;
    // data
    protected int _totalPage = -1;
    protected int _pageSize = 10;
    private int lastPage;
    private int lastTotalPage;
    private AbsListView.OnScrollListener onScrollListener;

    /** 获取listView */
    public ListView getListView() {
        return lvPageListView;
    }

    public void setOnScrollListener(AbsListView.OnScrollListener listener) {
        onScrollListener = listener;
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener listener) {
        lvPageListView.setOnItemClickListener(listener);
    }

    public void setAdapter(ListAdapter adapter) {
        lvPageListView.setAdapter(adapter);
    }


    public YSBPageListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initLayout();
    }

    public YSBPageListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initLayout();
    }

    public YSBPageListView(Context context) {
        super(context);
        initLayout();
    }


    private void initLayout() {
        LayoutInflater.from(getContext()).inflate(R.layout.libs_listview_ysb_layout, this);
        initView();
        initListener();
    }

    private void initListener() {

        lvPageListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (null != onScrollListener) {
                    onScrollListener.onScrollStateChanged(view, scrollState);
                }
//                tvPageTips.setVisibility(scrollState == SCROLL_STATE_IDLE ? GONE : VISIBLE);
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (null != onScrollListener) {
                    onScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
                }
                int curPage = (firstVisibleItem + visibleItemCount - 1) / _pageSize + 1;
                if(lastPage == curPage && lastTotalPage == getTotalPage())
                    return;
                tvPageTips.setText(curPage + "/" + getTotalPage());
                lastPage = curPage;
                lastTotalPage = getTotalPage();
            }
        });
    }

    private void initView() {
        lvPageListView = (PageListView) findViewById(R.id.lvPageListView);
        llListEmpty = (LinearLayout)findViewById(R.id.llListEmpty);
        ivListEmpty = (ImageView)findViewById(R.id.ivListEmpty);
        tvListEmpty = (TextView)findViewById(R.id.tvListEmpty);
        tvPageTips = (TextView)findViewById(R.id.tvPageTips);

        llListEmpty.removeAllViews();
        lvPageListView.setEmptyView(llListEmpty);

        tvPageTips.setVisibility(GONE);
    }

    @Override
    public void setOnPageListener(OnPageListener listener) {
        lvPageListView.setOnPageListener(listener);
    }

    @Override
    public void startLoad() {
        lvPageListView.startLoad();
    }

    @Override
    public void finishLoading(boolean hasMoreItems) {
        lvPageListView.finishLoading(hasMoreItems);
    }

    @Override
    public void setHaveMoreData(boolean haveMoreData) {
        lvPageListView.setHaveMoreData(haveMoreData);
    }

    @Override
    public int getPageSize() {
        return _pageSize;
    }

    @Override
    public void setPageSize(int pageSize) {
        _pageSize = pageSize;
    }

    @Override
    public void setTotalPage(int totalPage) {
        _totalPage = totalPage;
    }


    @Override
    public void setEmptyView(View view) {
        llListEmpty.removeAllViews();
        llListEmpty.addView(view);
    }

    @Override
    public TextView setEmptyTips(String emptyTips) {
        llListEmpty.removeAllViews();
        llListEmpty.addView(tvListEmpty);
        tvListEmpty.setText(emptyTips);
        return tvListEmpty;
    }

    @Override
    public ImageView setEmptyImageResid(int imgResid) {
        llListEmpty.removeAllViews();
        llListEmpty.addView(ivListEmpty);
        ivListEmpty.setImageResource(imgResid);
        return ivListEmpty;

    }

    @Override
    public EmptyHolder setEmptyImageResidAndTips(int imgResid, String emptyTips) {
        EmptyHolder emptyHolder = new EmptyHolder();
        llListEmpty.removeAllViews();
        ivListEmpty.setImageResource(imgResid);
        llListEmpty.addView(ivListEmpty);
        emptyHolder.ivEmpty = ivListEmpty;
        tvListEmpty.setText(emptyTips);
        llListEmpty.addView(tvListEmpty);
        emptyHolder.tvEmpty = tvListEmpty;
        return emptyHolder;
    }

    private int getTotalPage() {
        int curCount = lvPageListView.getCount();
        int curTotalPage = curCount/_pageSize;
        if(curCount % _pageSize > 0 ) {
            curTotalPage++;
        }
        return Math.max(_totalPage, curTotalPage);
    }

    public void smoothScrollToPosition(final int position) {
        lvPageListView.smoothScrollToPosition(position);
//        lvPageListView.post(new Runnable() {
//            @Override
//            public void run() {
//                lvPageListView.smoothScrollToPosition(position);
//            }
//        });
    }

    public void setSelectPosition(int index) {
        lvPageListView.setSelection(index);
    }

    public void setTopLoadView(boolean isLoading){
        lvPageListView.setTopLoadView(isLoading);
    }


}
