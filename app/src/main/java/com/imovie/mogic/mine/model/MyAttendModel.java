package com.imovie.mogic.mine.model;

import com.imovie.mogic.dbbase.model.BaseModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhoulinda on 16/3/15.
 */
public class MyAttendModel extends BaseModel{
    public AttendInfo info;
    public List<GroupItem> list = new ArrayList<>();
}
