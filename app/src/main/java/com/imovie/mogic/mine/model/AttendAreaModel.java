package com.imovie.mogic.mine.model;


import com.imovie.mogic.dbbase.model.BaseModel;
import com.imovie.mogic.mine.attend.model.AttendDates;
import com.imovie.mogic.mine.attend.model.AttendWay;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${zhouxinshan} on 2016/4/12.
 */

public class AttendAreaModel extends BaseModel {
    public int type = 1;
    public boolean attendArea = false;
    public int distanceRange = 60;
    public List<AttendWay> attendWayList = new ArrayList<>();
}


