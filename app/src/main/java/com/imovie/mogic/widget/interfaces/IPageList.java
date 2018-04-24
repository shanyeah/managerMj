package com.imovie.mogic.widget.interfaces;

/**
 * 支持分页的接口
 * */
public interface IPageList {
    /** 设置分页监听器  */
    void setOnPageListener(OnPageListener listener) ;

    /** 手动加载数据。 一般适用于首次加载 */
    void startLoad();

    /**
     * 结束数据加载。
     * @param hasMoreItems 是否还有更多数据
     */
    void finishLoading(boolean hasMoreItems);

    /** 设置剩余数据状态 */
    void setHaveMoreData(boolean haveMoreData);

    interface OnPageListener {
        void onLoadMoreItems();
    }
}
