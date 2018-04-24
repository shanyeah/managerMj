package com.imovie.mogic.myRank.model;


import com.imovie.mogic.dbbase.model.BaseModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${zhouxinshan} on 2016/4/12.
 */

public class OrderRecord extends BaseModel {

    public List<Record> list = new ArrayList<>();

    public static class Record extends BaseModel{
        public int id;
        public int billId;
        public String createTime;
        public double payAmount;
        public String remark;
        public double rewardAmount;
        public int status;
        public int type;
        public RecordDetail detail;
    }

    public static class RecordDetail extends BaseModel{
        public double cashAmount;
        public String createTime;
        public double discount;
        public String idNumber;
        public String name;
        public String payFinishTime;
        public double payAmount;
        public double price;
    }

}
