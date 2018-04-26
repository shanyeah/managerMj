package com.imovie.mogic.login.model;


import com.imovie.mogic.dbbase.model.BaseModel;
import com.imovie.mogic.home.model.CardModel;
import com.imovie.mogic.home.model.GameHall;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by ${zhouxinshan} on 2016/4/12.
 */

public class LoginModel extends BaseModel {
    public String token;
    public int adminId;
    public String username;
    public int organId;
    public String name;
    public String fackeImageUrl;
    public List<OrganList> organList = new ArrayList<>();


    public static class OrganList extends BaseModel{
        public int organId;
        public String organName;
    }
}



