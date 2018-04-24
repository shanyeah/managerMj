package com.imovie.mogic.chat.model;


import com.imovie.mogic.dbbase.model.BaseModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${zhouxinshan} on 2016/4/12.
 */

public class ContactModel extends BaseModel {

    public boolean hasNextPage;
    public int pageNum;
    public int pageSize;
    public int pages;
    public List<Contact> list = new ArrayList<>();
    public static class Contact extends BaseModel{
        public int userId;
        public String distance="";
        public String nickName;
        public String faceImageUrl;
        public String shortDesc;
        public int gender;
        public int age;
        public int bossType;
        public String location;

        public String emId;
        public int id;
        public String name;
        public String description;
        public int maxUsers;
        public int memberCount;
    }

}
