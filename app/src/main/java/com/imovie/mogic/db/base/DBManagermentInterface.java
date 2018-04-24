package com.imovie.mogic.db.base;


import com.imovie.mogic.dbbase.model.CoreFuncReturn;

/**
 * Created by zhouxinshan on 2016/04/06
 */
public interface DBManagermentInterface {

    CoreFuncReturn execSQL(String sql);

    CoreFuncReturn execSQL(String sqlFormatString, Object[] args);

}
