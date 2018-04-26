package com.imovie.mogic.home.model;


import com.imovie.mogic.dbbase.model.BaseModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${zhouxinshan} on 2016/4/12.
 */

public class GoodsModel extends BaseModel {
    public int id;
    public String code;
    public String imageUrl;
    public String name;
    public double price;
    public int type;
    public String unit;
    public String category = "";
    public boolean isShow = false;
    public int sum;
    public int goodsTagCategory;
    public boolean haveTag = false;
    public String tagName;

    public int existTags;
    public int goodsId;


    public List<GoodTagList> goodsPackList = null;

}


