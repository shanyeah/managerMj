package com.imovie.mogic.home.model;


import com.imovie.mogic.dbbase.model.BaseModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${zhouxinshan} on 2016/4/12.
 */

public class GameHall extends BaseModel {
    public int id = 0;
    public String name;
    public int seatFlag;
    public int activeFlag;
    public int taskFlag;
    public int changeFlag;
    public int movieFlag;
    public String distance;
    public String openTime;
    public float rating;


    public int type;
    public int status;
    public int cityId;

    public String address;
    public String postcode;
    public String tel;
    public String cityName;
    public String stgDesc;
    public boolean selectState = false;

    public List<HallImages> images = new ArrayList<>();

    public static class HallImages extends BaseModel{
        public int id;
        public int stgId;
        public int type;
        public String imageUrl;
        public String imageHash;
        public int orderIndex;
    }
}
