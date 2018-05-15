package com.imovie.mogic.home.model;


import com.imovie.mogic.dbbase.model.BaseModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${zhouxinshan} on 2016/4/12.
 */

public class PayDetailModel extends BaseModel {
    public double discount;
    public double discountAmount;
    public List<GoodsModel> goodsList = new ArrayList<>();
    public double incomeAmount;
    public String orderNo;
    public int organId;
    public String organName;
    public double payAmount;
    public String payInfo;
    public String remark;
    public long saleBillId;
    public String seatNo;

}


