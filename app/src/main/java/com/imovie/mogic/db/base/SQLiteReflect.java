package com.imovie.mogic.db.base;

import com.imovie.mogic.db.model.DBModel_VersionControl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * 表注册配置类 *
 * Created by zhouxinshan on 2016/04/06
 */
public class SQLiteReflect {

    // 表名注册
    public static HashMap<Class, String> map_ModelTableName = new HashMap<Class, String>() {
        {
            put(DBModel_VersionControl.class, "VersionControl");
//            put(MovieInfo.class, "MovieInfo");
//            put(FileResource.class, "FileResource");


        }
    };

    // 表主键配置
    public static HashMap<Class, String> map_ModelKeys = new HashMap<Class, String>() {
        {
            put(DBModel_VersionControl.class, "version,tableName");
//            put(MovieInfo.class, "movieId");

        }
    };


    //get table name by model's class
    public static String getTableNameByCls(Class cls) {
        String tableName = null;
        if (map_ModelTableName.containsKey(cls)) {
            tableName = map_ModelTableName.get(cls);
        }
        return tableName;
    }

    //get model class by table name
    public static Class getClsByTableName(String tableName) {
        List<Object> classList = Arrays.asList(map_ModelTableName.keySet().toArray());
        List<Object> tableNameList = Arrays.asList(map_ModelTableName.values().toArray());

        int index = tableNameList.indexOf(tableName);
        if (index >= 0) {
            Class cls = (Class) classList.get(index);
            return cls;
        }
        return null;
    }

    //get keys by model's class
    public static String getKeysStringByCls(Class cls) {
        String keys = null;
        if (map_ModelKeys.containsKey(cls)) {
            keys = map_ModelKeys.get(cls);
        }
        return keys;
    }

}
