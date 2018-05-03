package com.imovie.mogic.home.model;


import com.imovie.mogic.dbbase.model.BaseModel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${zhouxinshan} on 2016/4/12.
 */

public class OrderModel extends BaseModel {
    public int goodsId;
    public String price;
    public String packPrice;
    public String remark;
    public long quantity;
    public BigDecimal payAmount;
    public BigDecimal incomeAmount;
    public String goodsTags;
    public List<GoodTagList> goodsPackList = new ArrayList<>();

}
