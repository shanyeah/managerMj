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
    public String goodsName;
    public double price;
    public int type;
    public int packType;
    public String unit;
    public String category = "";
    public boolean isShow = false;
    public int sum;
    public int goodsTagCategory;
    public boolean haveTag = false;
    public String tagName;
    public String goodsTags;

    public int quantity;
    public int goodsId;
    public double payAmount;

    public List<GoodTagList> goodsPackList = new ArrayList<>();
    public List<CategorysModel> goodsTagsList = new ArrayList<>();
    public List<GoodTagList> childGoodsList = new ArrayList<>();
    public List<PackReplaceList> packReplaceList = new ArrayList<>();

}


