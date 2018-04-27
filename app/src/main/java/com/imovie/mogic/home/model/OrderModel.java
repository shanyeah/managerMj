package com.imovie.mogic.home.model;


import com.imovie.mogic.dbbase.model.BaseModel;

import java.util.List;

/**
 * Created by ${zhouxinshan} on 2016/4/12.
 */

public class OrderModel extends BaseModel {
    public int goodsId;
    public String price;
    public String remark;
    public long quantity;
    public double payAmount;
    public double incomeAmount;
    public String goodsTags;
    public List<GoodTagList> goodsPackList = null;

}
