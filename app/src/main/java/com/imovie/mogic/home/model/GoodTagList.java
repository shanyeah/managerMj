package com.imovie.mogic.home.model;


import com.imovie.mogic.dbbase.model.BaseModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${zhouxinshan} on 2016/4/12.
 */

public class GoodTagList extends BaseModel {
    public int id;
    public String createTime;
    public boolean isSelect = false;
    public long goodsId;
    public String name;
    public String goodsName;
    public double packPrice;
    public double price;
    public int quantity;
    public String goodsTags;
    public String imageUrl;
    public int packGroupId;
    public long selectId = -1;

//    childGoodsList":[{"goodsName":"卤肉流心蛋饭","incomeAmount":25.0,"payAmount":25.0,"quantity":1},
}


