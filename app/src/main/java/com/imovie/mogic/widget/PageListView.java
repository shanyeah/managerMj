package com.imovie.mogic.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.HeaderViewListAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.imovie.mogic.R;
import com.imovie.mogic.widget.interfaces.IPageList;

/**
 * 支持分页的ListView.
 * 外部使用参考接口IPageList说明
 * 另：可使用setEmptyView(...) 设置无数据时的提示view
 */
public class PageListView extends ListView implements IPageList {
    private final static String TAG = PageListView.class.getSimpleName();

    private OnPageListener _pageListener;
    private FooterLoadingView footerView = new FooterLoadingView(getContext()); //
    private View emptyView; // 无数据提示view
    private OnScrollListener onScrollListener;

    // 状态
    private boolean _isLoading = false; // 是否加载数据中
    private boolean _isTopLoading = true; // 是否顶部加载数据中
    private boolean _hasMoreItems = true; // 是否还有更多数据

    public PageListView(Context context) {
        super(context);
        init();
    }

    public PageListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PageListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    @Override
    public void setOnPageListener(OnPageListener listener) {
        _pageListener = listener;
    }

    @Override
    public void startLoad() {
        if(getAdapter().isEmpty()) {
            setHasMoreItems(true);
        }
        loadMore(_isTopLoading);
    }

    @Override
    public void finishLoading(boolean hasMoreItems) {
        super.setEmptyView(emptyView);
        setHasMoreItems(hasMoreItems);
        setIsLoading(false);
    }

    /** 设置剩余数据状态 */
    public void setHaveMoreData(boolean haveMoreData) {
        setHasMoreItems(haveMoreData);
    }

    @Override
    public void setOnScrollListener(OnScrollListener listener) {
        onScrollListener = listener;
    }

    public FooterLoadingView getFooterView(){
        return footerView;
    }

    /*************************************************************************************************************************/
    private void loadMore(boolean _isLoading) {
        if(!isTimeToLoadMore()) {
            return;
        }

        if(null != _pageListener) {
            // 不显示空提示view
            emptyView = super.getEmptyView();
            if(null != emptyView) {
                emptyView.setVisibility(GONE);
            }
            super.setEmptyView(null);

            setIsLoading(_isLoading);
            _pageListener.onLoadMoreItems();
        } else {
            Log.e(TAG, "page listener is null!!!");
        }
    }

    // 设置加载状态
    private void setIsLoading(boolean isLoading) {
        _isTopLoading = isLoading;
        footerView.setVisibility(_isTopLoading ? VISIBLE : GONE);
    }

    // 设置剩余数据状态
    private void setHasMoreItems(boolean hasMoreItems) {
        _hasMoreItems = hasMoreItems;

        if (!_hasMoreItems) {
            removeFooterView(footerView);
        } else if (findViewById(R.id.loading_view) == null) {
            addFooterView(footerView);
            try{
                ListAdapter adapter = ((HeaderViewListAdapter) getAdapter()).getWrappedAdapter();
                setAdapter(adapter);
            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }

    private void init() {
      //  footerView = new FooterLoadingView(getContext());
        addFooterView(footerView);
        footerView.setVisibility(GONE);
        setDivider(null);

        super.setOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (onScrollListener != null) {
                    onScrollListener.onScrollStateChanged(view, scrollState);
                }
                // 滚动加载
                if (OnScrollListener.SCROLL_STATE_IDLE == scrollState) {
                    loadMore(true);
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (onScrollListener != null) {
                    onScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
                }
            }
        });
    }

    //
    private boolean isTimeToLoadMore(){
        if(_isLoading || !_hasMoreItems) {
            return false;
        }

        int restCount = getCount() - getLastVisiblePosition(); // 剩余未显示项
        // 剩余未显示项不到5个
        if(restCount<5) {
            return true;
        }
        return false;
    }

    public void setTopLoadView(boolean _isTopLoading){
        this._isTopLoading = _isTopLoading;
    }

}
