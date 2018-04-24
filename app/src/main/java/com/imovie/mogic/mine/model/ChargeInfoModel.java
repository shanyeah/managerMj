package com.imovie.mogic.mine.model;


import com.imovie.mogic.dbbase.model.BaseModel;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${zhouxinshan} on 2016/4/12.
 */

public class ChargeInfoModel extends BaseModel {
    public PageDate pageData;
    public static class PageDate extends BaseModel{
        public List<ChargeInfo> list = new ArrayList<>();
    }

}


