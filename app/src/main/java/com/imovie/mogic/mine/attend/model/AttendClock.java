package com.imovie.mogic.mine.attend.model;


import com.imovie.mogic.dbbase.model.BaseModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${zhouxinshan} on 2016/4/12.
 */

public class AttendClock extends BaseModel {
    public String endTime;
    public String note;
    public String startTime;
    public List<String> selectDate = new ArrayList<>();

    public String applyReason;
    public String extraDate;
    public int status;

}


