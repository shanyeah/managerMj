package com.imovie.mogic.db.DBPicker;


import com.imovie.mogic.db.base.DBManager;
import com.imovie.mogic.dbbase.model.BaseObject;
import com.imovie.mogic.dbbase.model.CoreFuncReturn;

/**
 * Created by zhouxinshan on 2016/04/06.
 */
public abstract class DBPickerBase extends BaseObject implements DBPickerInterface {

    public DBManager managerment;

    public DBPickerBase() {
        managerment = new DBManager();
    }

    public CoreFuncReturn execSQL(String queryString) {

        try {


            CoreFuncReturn FR = managerment.execSQL(queryString);
            return FR;

        } catch (Exception ex) {
            //logErr(ex);
            return new CoreFuncReturn();
        }


    }

    public CoreFuncReturn execSQL(String queryString, String[] args) {
        try {

            CoreFuncReturn FR = managerment.execSQL(queryString, args);
            return FR;

        } catch (Exception ex) {
            // logErr(ex);
            return new CoreFuncReturn();
        }
    }

    public void begin() {

        if (managerment.db == null || !managerment.db.isOpen()) {
            managerment.openDB();
        }
    }

    public void finish() {

        if (managerment.db != null && managerment.db.isOpen()) {
            managerment.closeDB();
        }
    }
}
