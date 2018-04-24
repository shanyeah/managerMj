package com.imovie.mogic.db.base;


import com.imovie.mogic.dbbase.model.BaseObject;
import com.imovie.mogic.dbbase.model.CoreFuncReturn;

/**
 * Created by zhouxinshan on 2016/04/06
 */
public class DBManagerBase extends BaseObject implements DBManagermentInterface {


    @Override
    public CoreFuncReturn execSQL(String sql) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public CoreFuncReturn execSQL(String sqlFormatString, Object[] args) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
