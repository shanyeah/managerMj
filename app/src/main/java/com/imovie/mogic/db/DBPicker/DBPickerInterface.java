package com.imovie.mogic.db.DBPicker;


import com.imovie.mogic.db.model.DBModelBase;

import java.util.List;



/**
 * 数据查询接口。
 * Created by zhouxinshan on 2016/04/06.
 * To change this template use File | Settings | File Templates.
 */
interface DBPickerInterface {
    /**
     * 批量查询
     */
    <T extends DBModelBase> List<T> pickModelsWithModelCode(Class<T> modelClass, String condition);

    /**  */
    <T extends DBModelBase> List<T> pickModelsWithModelCode(Class<T> modelClass, String condition, boolean isNeedAlwaysCondition);

    /**
     * 单个查询
     */
    <T extends DBModelBase> T pickModel(Class<T> tClass, String condition);

    /**
     * 查询数量
     */
    <T extends DBModelBase> int pickModelCount(Class<T> modelClass, String condition);

    /**
     * 查询数据是否存在
     */
    boolean isModelExists(DBModelBase model);

    /**  */
    <T extends DBModelBase> List<T> pickModelsWithModel(Class<T> modelClass, String condition);


}
