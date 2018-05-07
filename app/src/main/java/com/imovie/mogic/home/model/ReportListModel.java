package com.imovie.mogic.home.model;


import com.imovie.mogic.dbbase.model.BaseModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${zhouxinshan} on 2016/4/12.
 */

public class ReportListModel extends BaseModel {
    public List<ReportModel> reportList = new ArrayList<>();
    public String weather;
}


