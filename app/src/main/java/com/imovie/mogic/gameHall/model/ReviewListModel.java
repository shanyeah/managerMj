package com.imovie.mogic.gameHall.model;


import com.imovie.mogic.dbbase.model.BaseModel;
import com.imovie.mogic.home.model.GameHall;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${zhouxinshan} on 2016/4/12.
 */

public class ReviewListModel extends BaseModel {
    public int endRow;
    public List<ReviewModel> list = new ArrayList<>();
}


