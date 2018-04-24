package com.imovie.mogic.mine.model;

import com.imovie.mogic.dbbase.model.BaseModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhoulinda on 16/3/15.
 */
public class GroupItem extends BaseModel{
    public String title;
    public String count;
    public String desc;
    public List<GroupChildItem> list = new ArrayList<>();
}
