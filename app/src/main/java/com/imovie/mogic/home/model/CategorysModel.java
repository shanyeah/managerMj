package com.imovie.mogic.home.model;


import com.imovie.mogic.dbbase.model.BaseModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${zhouxinshan} on 2016/4/12.
 */

public class CategorysModel extends BaseModel {
    public long goodsId;
    public List<Categorys> categorys = new ArrayList<>();
    public static class Categorys extends BaseModel{
        public List<GoodsTags> goodsTags = new ArrayList<>();
        public long goodsId;
        public String name;
        public int id;
    }
}


