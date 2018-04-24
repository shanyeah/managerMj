package com.imovie.mogic.home.model;


import com.imovie.mogic.dbbase.model.BaseModel;

import java.util.List;

/**
 * Created by ${zhouxinshan} on 2016/4/12.
 */

public class OrderModel extends BaseModel {
    public int goodsId;
    public String price;
    public String name;
    public int goodsTagCategory;
    public List<BomModel> goodsBomList = null;
}
