package com.imovie.mogic.home.model;


import com.imovie.mogic.dbbase.model.BaseModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${zhouxinshan} on 2016/4/12.
 */

public class HomeModel extends BaseModel {
    public DayReport dayReport = new DayReport();
    public List<MenuList> menuList = new ArrayList<>();
    public static class DayReport extends BaseModel{
        public double goodsIncomeAmount;
        public int goodsRefundBillCount;
        public double rechargeIncomeAmount;
        public int goodsSaleBillCount;
        public int rechargeRefundBillCount;
        public int rechargeSaleBillCount;
        public double totalIncomeAmount;
        public double totalPayAmount;
    }

    public static class MenuList extends BaseModel{
        public String name;
        public List<ClassifyModel> operationList = new ArrayList<>();
    }
}


