package com.imovie.mogic.myRank.model;


import com.imovie.mogic.dbbase.model.BaseModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${zhouxinshan} on 2016/4/12.
 */

public class ChargeOrderMode extends BaseModel {
    public String className;
    public String createAdminName;
    public String createTime;
    public double incomeAmount;
    public int optionType;
    public String optionTypeName;
    public String orderNo;
    public double payAmount;
    public long payBillId;
    public long saleBillId;
    public int type;
    public String typeName;
    public int userId;

    public UserWallet userWallet;


    public static class UserWallet extends BaseModel{
        public double afterBalance;
        public double afterCashBalance;
        public double afterPresentBalance;
        public double amount;
        public double cashAmount;
        public String className;
        public String idNumber;
        public String mobile;
        public String userName;
        public double presentAmount;
        public int userId;
    }
}
