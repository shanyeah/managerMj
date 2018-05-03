package com.imovie.mogic.home.model;


import com.imovie.mogic.dbbase.model.BaseModel;

/**
 * Created by ${zhouxinshan} on 2016/4/12.
 */

public class GoodTagList extends BaseModel {
    public int id;
    public String createTime;
    public boolean isSelect = false;
    public int goodsId;
    public String name;
    public double packPrice;
    public double price;
    public int quantity;
    public String goodsTags;

}


