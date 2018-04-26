package com.imovie.mogic.home.model;


import com.imovie.mogic.dbbase.model.BaseModel;

/**
 * Created by ${zhouxinshan} on 2016/4/12.
 */

public class GoodTagList extends BaseModel {
    public int id;
    public int goodsTagCategory;

    public int orderIndex;

    public String createTime;
    public int createAdmin;
    public int updateAdmin;
    public String updateTime;
    public boolean isSelect = false;

    public int goodsId;
    public String name;
    public double packPrice;
    public double price;
    public int quantity;

}


