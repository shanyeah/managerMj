package com.imovie.mogic.home.model;


import com.imovie.mogic.dbbase.model.BaseModel;

import java.util.ArrayList;
import java.util.List;

import static android.R.id.list;

/**
 * Created by ${zhouxinshan} on 2016/4/12.
 */

public class CardModel extends BaseModel {
    public String stgName;
    public List<Categorys> categorys = new ArrayList<>();
    public static class Categorys extends BaseModel{
        public List<GoodsModel> goods = new ArrayList<>();
        public int id;
        public String name;
    }
}


