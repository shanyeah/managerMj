package com.imovie.mogic.myRank.model;


import com.imovie.mogic.dbbase.model.BaseModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${zhouxinshan} on 2016/4/12.
 */

public class MovieCondition extends BaseModel {
    public String returnCode;
    public String returnMsg;
    public List<MovieCat> movieCat = new ArrayList<>();
    public List<MovieCat> movieTimes = new ArrayList<>();
    public List<MovieCat> movieArea = new ArrayList<>();

    public static class MovieCat extends BaseModel{
        public String id;
        public String name;

    }

}
