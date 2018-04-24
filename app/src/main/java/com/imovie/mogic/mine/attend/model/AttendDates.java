package com.imovie.mogic.mine.attend.model;


import com.imovie.mogic.dbbase.model.BaseModel;
import com.imovie.mogic.mine.model.ClockInfo;
import com.imovie.mogic.mine.model.ClockModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${zhouxinshan} on 2016/4/12.
 */

public class AttendDates extends BaseModel {
    public int adminId;
    public int attendClassId;
    public String attendClassName;
    public int attendGroupId;
    public int createAdmin;
    public String createTime;
    public int editStatus;
    public String endTime;
    public int id;
    public int recordCount;
    public String scheduleDate;
    public String startTime;
    public String updateTime;

}
