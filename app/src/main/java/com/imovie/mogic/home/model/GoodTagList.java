package com.imovie.mogic.home.model;


import com.imovie.mogic.dbbase.model.BaseModel;

/**
 * Created by ${zhouxinshan} on 2016/4/12.
 */

public class GoodTagList extends BaseModel {
    public int id;
    public int goodsTagCategory;
    public String name;
    public int orderIndex;
    public double price;
    public String createTime;
    public int createAdmin;
    public int updateAdmin;
    public String updateTime;
    public boolean isSelect = false;

}


