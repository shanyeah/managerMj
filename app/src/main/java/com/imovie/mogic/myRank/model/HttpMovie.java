package com.imovie.mogic.myRank.model;


import com.imovie.mogic.dbbase.model.BaseModel;

/**
 * Created by zhouxinshan on 2016/4/12.
 */
public class HttpMovie<T> extends BaseModel {

    public String returnCode = "";
    public String returnMsg;
    public String hint;
    public T movies;

}
