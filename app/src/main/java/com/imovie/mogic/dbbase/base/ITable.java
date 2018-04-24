package com.imovie.mogic.dbbase.base;


import com.imovie.mogic.dbbase.model.BaseDBModel;
import com.imovie.mogic.dbbase.model.CoreFuncReturn;

/**
 * Created by zhouxinshan on 2016/04/06.
 */
public interface ITable {

    CoreFuncReturn insert(BaseDBModel model);

    CoreFuncReturn delete(BaseDBModel model);

    CoreFuncReturn update(BaseDBModel model);
}
