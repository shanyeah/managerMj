package com.imovie.mogic.mine.model;


import com.imovie.mogic.dbbase.model.BaseModel;

/**
 * Created by ${zhouxinshan} on 2016/4/12.
 */

public class AutoModel extends BaseModel {
    public int id;
    public int orderId;
    public String imageUrl;
    public String createTime;
    public String detail;
    public double payAmount = 0;
    public String payTypeName;
    public String seatName;
    public String statusName;
}


