package com.imovie.mogic.dbbase.model;


import com.imovie.mogic.dbbase.util.LogUtil;

/**
 * Created by zhouxinshan on 2016/04/06.
 */
public class BaseObject {
    protected void logMsg(String msg) {
        LogUtil.LogMsg(this.getClass(), msg);
//       Log.i(this.getClass().getSimpleName(), msg);
    }

    protected void logErr(Exception ex) {
        LogUtil.LogErr(this.getClass(), ex);
//        Log.e(this.getClass().getSimpleName(), ex.getMessage());
    }
}
