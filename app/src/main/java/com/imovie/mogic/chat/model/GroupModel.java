package com.imovie.mogic.chat.model;


import com.imovie.mogic.dbbase.model.BaseModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${zhouxinshan} on 2016/4/12.
 */

public class GroupModel extends BaseModel {

    public int id;
    public String emId;
    public String name;
    public String imageUrl;
    public String address;
    public double latitude;
    public double longitude;
    public int maxUsers;
    public int memberCount;
    public int emPublic;
    public String createTime;
    public int deleteFlag;
    public String description;
    public int roleType;
    public int type;
    public List<Labels> labels = new ArrayList<>();
//    public List<UserAccess> members = new ArrayList<>();
    public static class Labels extends BaseModel{
        public int labelId;
        public int id;
        public String labelName;
    }

}
