package com.imovie.mogic.mine.model;


import com.imovie.mogic.dbbase.model.BaseModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${zhouxinshan} on 2016/4/12.
 */

public class AttendModel extends BaseModel {
    public List<ClockModel> attendRecords = new ArrayList<>();
    public ClockInfo clockInfo;
    public String note;
    public String title;
}
