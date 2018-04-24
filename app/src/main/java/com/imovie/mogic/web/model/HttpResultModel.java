package com.imovie.mogic.web.model;


import com.imovie.mogic.dbbase.model.BaseModel;

/**
 * Created by zhouxinshan on 2016/4/12.
 */
public class HttpResultModel<T> extends BaseModel {
    public String code = "";
    public String message;
    public String hint;
    public T data;
}
