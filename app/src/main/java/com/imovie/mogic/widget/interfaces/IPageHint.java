package com.imovie.mogic.widget.interfaces;

/**
 * 支持页号提示功能接口
 * */
public interface IPageHint {
    /** 设置每页加载个数 */
    void setPageSize(int pageSize);

    /** 获取每页加载个数 */
    int getPageSize();

    /** 设置总页数 */
    void setTotalPage(int totalPage);

}
