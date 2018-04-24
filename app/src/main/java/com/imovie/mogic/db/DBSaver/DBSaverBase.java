package com.imovie.mogic.db.DBSaver;

import com.imovie.mogic.config.AppConfig;
import com.imovie.mogic.db.base.DBManager;
import com.imovie.mogic.dbbase.model.BaseObject;
import com.imovie.mogic.dbbase.model.CoreFuncReturn;

/**
 * Created by zhouxinshan on 2016/04/06.
 * To change this template use File | Settings | File Templates.
 */
public abstract class DBSaverBase extends BaseObject implements DBSaverInterface {

    public DBManager manager;

    public DBSaverBase() {
        super();
        manager = new DBManager(AppConfig.dbPath);
    }


    @Override
    public CoreFuncReturn execSQL(String SQLString) {
        manager.openDB();
        CoreFuncReturn FR = manager.execSQL(SQLString);
        manager.closeDB();
        return FR;
    }


    public void openDataBase() {
        manager.openDB();
    }

    public void closeDataBase() {
        manager.closeDB();
    }
}
