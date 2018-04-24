package com.imovie.mogic.mine.attend.model;


import com.imovie.mogic.dbbase.model.BaseModel;
import com.imovie.mogic.mine.model.ClockInfo;
import com.imovie.mogic.mine.model.ClockModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${zhouxinshan} on 2016/4/12.
 */

public class AttendDatesModel extends BaseModel {
    public List<AttendDates> attendDates = new ArrayList<>();
    public String date;
    public String groupName;
    public String name;
    public String week;
}
