package com.imovie.mogic.myRank.model;


import com.imovie.mogic.dbbase.model.BaseModel;

/**
 * Created by ${zhouxinshan} on 2016/4/12.
 */

public class ExercisesMode extends BaseModel {
    public int id;
    public String name;
    public String imageUrl;
    public String logoUrl;
    public int stgId;
    public int status;
    public long startTime;
    public long endTime;

    public long createTime;
    public long updateTime;

    public String introduction;
    public String schemeDesc;
    public String stgName;


}
