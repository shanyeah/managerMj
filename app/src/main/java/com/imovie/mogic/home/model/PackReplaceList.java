package com.imovie.mogic.home.model;


import com.imovie.mogic.dbbase.model.BaseModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${zhouxinshan} on 2016/4/12.
 */

public class PackReplaceList extends BaseModel {
    public int packGroupId;
    public String packGroupName;
    public List<GoodTagList> replaceList = new ArrayList<>();

//    public List<Rights> rights = new ArrayList<>();

//    public static class Rights extends BaseModel{
//        public int id;
//        public int categoryId;
//        public String name;
//        public int orderIndex;
//
//    }

}


