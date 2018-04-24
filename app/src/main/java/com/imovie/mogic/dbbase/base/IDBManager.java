package com.imovie.mogic.dbbase.base;


import com.imovie.mogic.dbbase.model.CoreFuncReturn;

/**
 * Created by zhouxinshan on 2016/04/06.
 */
public interface IDBManager {

    CoreFuncReturn createDB();

    CoreFuncReturn dropDB();

    CoreFuncReturn exec(BaseDatabase database, String sql);

    CoreFuncReturn exec(BaseDatabase database, String formatSqlString, Object[] args);

    CoreFuncReturn clone(BaseDatabase srcDatabase, BaseDatabase dstDatabase);
}
