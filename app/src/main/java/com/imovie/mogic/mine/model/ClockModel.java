package com.imovie.mogic.mine.model;


import com.imovie.mogic.dbbase.model.BaseModel;
import com.imovie.mogic.mine.attend.model.AttendRecord;

/**
 * Created by ${zhouxinshan} on 2016/4/12.
 */

public class ClockModel extends BaseModel {
    public int id;
    public boolean lineShow = false;
    public boolean clockShow = false;
    public boolean viewClock = false;

    public String clockName;

    public int adminId;
    public int attendScheduleId;
    public int checkWay;
    public String createTime;

    public int organId;
    public String planTime;
    public int status;
    public int type;
    public String workDate;
    public String checkTime;
    public AttendRecord attendExtraRecord;
}



