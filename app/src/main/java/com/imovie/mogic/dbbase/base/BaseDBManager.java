package com.imovie.mogic.dbbase.base;


import com.imovie.mogic.dbbase.configeration.DatabaseConfig;
import com.imovie.mogic.dbbase.model.BaseObject;
import com.imovie.mogic.dbbase.model.CoreFuncReturn;

/**
 * Created by zhouxinshan on 2016/04/06.
 */
public class BaseDBManager extends BaseObject implements IDBManager {

    protected BaseDatabase defaultDatabase;

    public void init(DatabaseConfig config, BaseDatabase.OnDatabaseUpdateListener onDatabaseUpdateListener) {

        if (config == null)
            return;


    }

    @Override
    public CoreFuncReturn createDB() {
        return null;
    }

    @Override
    public CoreFuncReturn dropDB() {
        return null;
    }

    @Override
    public CoreFuncReturn exec(BaseDatabase database, String sql) {
        return null;
    }

    @Override
    public CoreFuncReturn exec(BaseDatabase database, String formatSqlString, Object[] args) {
        return null;
    }

    @Override
    public CoreFuncReturn clone(BaseDatabase srcDatabase, BaseDatabase dstDatabase) {
        return null;
    }

}
