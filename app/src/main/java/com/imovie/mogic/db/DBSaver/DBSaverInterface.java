package com.imovie.mogic.db.DBSaver;


import com.imovie.mogic.db.model.DBModelBase;
import com.imovie.mogic.dbbase.model.CoreFuncReturn;

/**
 * 数据更改接口。
 * Created by zhouxinshan on 2016/04/06.
 * To change this template use File | Settings | File Templates.
 */
interface DBSaverInterface {

    CoreFuncReturn execSQL(String SQLString);

    CoreFuncReturn insertModel(DBModelBase model);

    CoreFuncReturn insertModesWithTransaction(DBModelBase[] models);

    CoreFuncReturn updateModel(DBModelBase model);

    CoreFuncReturn deleteModel(DBModelBase model);

    CoreFuncReturn deleteAllModel(DBModelBase model);

}
