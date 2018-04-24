package com.imovie.mogic.gameHall.model;


import com.imovie.mogic.dbbase.model.BaseModel;
import com.imovie.mogic.home.model.GameHall;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${zhouxinshan} on 2016/4/12.
 */

public class ReviewModel extends BaseModel {

    public int id;
    public int stgId;
    public int memberId;
    public String reviewText;
    public String rating;
    public String createTime;
    public String name;
    public String photoUrl = "";
    public List<Replies> replies = new ArrayList<>();
    public boolean showComment = true;
    public int index;

    public static class Replies extends BaseModel{
        public String createTime;
        public int deleteFlag;
        public int id;
        public int memberId;
        public String name;
        public int parentId;
        public String rating;
        public String reviewText;
        public int stgId;
        public int type;
    }
}
