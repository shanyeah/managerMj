package com.imovie.mogic.web;


import com.imovie.mogic.web.model.HttpResultModel;

import java.util.List;

/**
 * Created by ${zhouxinshan} on 2016/4/12.
 */
public interface IModelResultListener<T> {

    /**
     * 获得http返回模型
     *
     * @param resultModel 返回模型
     * @return true则后面可以继续解析，false则不会执行后面的方法
     */
    boolean onGetResultModel(HttpResultModel resultModel);

    /**
     * success返回码情况
     *
     * @param resultCode      返回码
     * @param resultMsg       返回消息
     * @param resultModel     返回的模型（若请求到的是List则为空）
     * @param resultModelList //返回模型列表（仅当返回是列表的时候才有值）
     * @param hint            //返回提示
     */
    void onSuccess(String resultCode, T resultModel, List<T> resultModelList, String resultMsg, String hint);

    /**
     * 非success返回码情况
     *
     * @param resultCode 返回码
     * @param resultMsg  返回消息
     * @param hint       返回提示
     */
    void onFail(String resultCode, String resultMsg, String hint);

    /**
     * 异常情况。如代码异常，网络异常
     *
     * @param errorMsg 异常消息
     */
    void onError(String errorMsg);
}
