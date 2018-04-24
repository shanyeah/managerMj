package com.imovie.mogic.db.DBPicker;

import android.database.Cursor;
import android.util.Log;

import com.imovie.mogic.db.base.DBManager;
import com.imovie.mogic.db.base.SQLiteReflect;
import com.imovie.mogic.db.model.DBModelBase;
import com.imovie.mogic.dbbase.model.CoreFuncReturn;
import com.imovie.mogic.dbbase.util.LogUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by zhouxinshan on 2016/04/06
 */
public class DBPicker extends DBPickerBase {


    public <T extends DBModelBase> List<T> pickModelsWithModelCode(Class<T> modelClass) {

        return pickModelsWithModelCode(modelClass, null);

    }

    @Override
    public <T extends DBModelBase> List<T> pickModelsWithModelCode(Class<T> modelClass, String condition) {

        return pickModelsWithModelCode(modelClass, condition, false);

    }

    @Override
    public <T extends DBModelBase> List<T> pickModelsWithModelCode(Class<T> modelClass, String condition, boolean isNeedAlwaysCondition) {

        List<T> list = new ArrayList<T>();

        try {

            //获取表名
            String tableName = SQLiteReflect.getTableNameByCls(modelClass);
            //获取类
            Class cls = modelClass;

            //开始查询
            begin();

            if (isNeedAlwaysCondition) {

            } else {

                if (condition == null) {
                    condition = "";
                } else if (condition.substring(0, 5).equals("ORDER")) {
                } else {
                    condition = "WHERE " + condition;
                }

            }

            String queryString = "SELECT DISTINCT " + tableName + ".* FROM " + tableName + " " + condition;

            CoreFuncReturn FR = execSQL(queryString);

            if (FR.isOK) {
                Cursor cursor = (Cursor) FR.tag;

                while (cursor.moveToNext()) {

                    T model = (T) cls.newInstance();

                    model.setModelByCursor(cursor);

                    list.add(model);

                }

                cursor.close();
            }

        } catch (Exception ex) {
            Log.e(LogUtil.class.getSimpleName(), ex.getMessage());
        } finally {
            finish();
        }

        return list;

    }

    @Override
    public <T extends DBModelBase> int pickModelCount(Class<T> modelClass, String condition) {

        try {

            //获取表名
            String tableName = SQLiteReflect.getTableNameByCls(modelClass);

            StringBuilder sqlStringBuilder = new StringBuilder();

            sqlStringBuilder.append("SELECT COUNT(*) AS numOfCount FROM ");
            sqlStringBuilder.append(tableName);
            if (condition != null && !condition.isEmpty()) {

                sqlStringBuilder.append(" WHERE ");
                sqlStringBuilder.append(condition);
            }

            String sqlString = sqlStringBuilder.toString();

            begin();
            CoreFuncReturn coreFuncReturn = execSQL(sqlString);

            if (coreFuncReturn.isOK) {

                Cursor cursor = (Cursor) coreFuncReturn.tag;

                if (cursor != null) {
                    if (cursor.getCount() > 0) {
                        cursor.moveToFirst();
                        int count = cursor.getInt(cursor.getColumnIndex("numOfCount"));

                        cursor.close();
                        return count;
                    }
                    cursor.close();
                    return 0;

                }

            }


        } catch (Exception ex) {
            Log.e(LogUtil.class.getSimpleName(), ex.getMessage());
        } finally {
            finish();
        }

        return 0;
    }

    @Override
    public <T extends DBModelBase> T pickModel(Class<T> tClass, String condition) {

        List<T> modelBaseList = pickModelsWithModelCode(tClass, condition);

        if (modelBaseList.size() > 0) {

            T model = modelBaseList.get(0);
            return model;
        }


        return null;
    }

    @Override
    public boolean isModelExists(DBModelBase model) {

        try {
            String condition = "";
            //获取主键
            String keyStr = SQLiteReflect.getKeysStringByCls(model.getClass());

            List<String> keys = new ArrayList<String>();
            List<Object> keyValues = new ArrayList<Object>();

            int j = keyStr.indexOf(",");
            while (j > 0) {

                keys.add(keyStr.substring(0, j));
                keyStr = keyStr.substring(j + 1);
                j = keyStr.indexOf(",");
            }
            keys.add(keyStr);

            for (int i = 0; i < keys.size(); i++) {

                Object keyValue = model.getValueByName(keys.get(i));

                if (keyValue == null) {
                    keyValue = "-1";
                }

                keyValues.add(keyValue);

            }

            for (int i = 0; i < keys.size(); i++) {

                if (i > 0) {
                    condition += " AND ";
                }

                Class cls = keyValues.get(i).getClass();
                if (cls.equals(String.class)) {
                    condition += (keys.get(i) + " like '" + keyValues.get(i) + "'");
                } else {
                    condition += (keys.get(i) + " = " + keyValues.get(i));
                }

            }

            int i = pickModelCount(model.getClass(), condition);

            return i > 0;


        } catch (Exception ex) {
            Log.e(LogUtil.class.getSimpleName(), ex.getMessage());
        }
        return false;
    }

    @Override
    public <T extends DBModelBase> List<T> pickModelsWithModel(Class<T> modelClass, String condition) {

        List<T> list = new ArrayList<T>();
        try {

        Class cls = modelClass;
            try {
                DBManager dbManager = new DBManager();
                dbManager.openDB();
                if(dbManager.db.isOpen()){
                    Cursor cursor = dbManager.db.rawQuery(condition, null);                //得到一组游标的结果
                    while(cursor.moveToNext()){//遍历游标
                        T model = (T) cls.newInstance();
                        model.setModelByCursor(cursor);
                        list.add(model);
                    }
                    cursor.close();
                    dbManager.closeDB();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception ex) {
            Log.e(LogUtil.class.getSimpleName(), ex.getMessage());
        } finally {
            finish();
        }

        return list;
    }

}
