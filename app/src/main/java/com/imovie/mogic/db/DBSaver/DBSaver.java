package com.imovie.mogic.db.DBSaver;


import com.imovie.mogic.db.base.SQLiteReflect;
import com.imovie.mogic.db.model.DBModelBase;
import com.imovie.mogic.dbbase.base.DB_OPERATORTYPE;
import com.imovie.mogic.dbbase.model.CoreFuncReturn;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;



/**
 * Created by zhouxinshan on 2016/04/06
 */
public class DBSaver extends DBSaverBase {

    public DBSaver() {
        super();
    }

    private String getSQL(DB_OPERATORTYPE type, DBModelBase model) {
        String sqlString = null;

        try {
            String tableName = SQLiteReflect.getTableNameByCls(model.getClass());

            //如果获取表名失败
            if (tableName == null) {
                return null;
            }

            switch (type) {
                case OP_INSERT: {
                    //插入
                    StringBuilder buf = new StringBuilder();
                    buf.append("INSERT INTO ");
                    buf.append(tableName);
                    buf.append("(");

                    String[] propertyNames = model.getPropertyNames();
                    int length = propertyNames.length;
                    for (int i = 0; i < length; i++) {
                        if (i != 0) {
                            buf.append(",");
                        }
                        buf.append(propertyNames[i]);
                    }
                    buf.append(") ");
                    buf.append("VALUES (");

                    for (int i = 0; i < length; i++) {
                        if (i != 0) {
                            buf.append(",");
                        }
                        buf.append("?");
                    }
                    buf.append(")");

                    sqlString = buf.toString();

                    break;
                }
                case OP_UPDATE: {
                    //更新
                    StringBuilder buf = new StringBuilder();
                    buf.append("UPDATE ");
                    buf.append(tableName + " ");
                    buf.append("SET ");

                    String[] propertyNames = model.getPropertyNames();
                    int length = propertyNames.length;
                    for (int i = 0; i < length; i++) {
                        if (i != 0) {
                            buf.append(", ");
                        }
                        buf.append(propertyNames[i] + "=" + "?");
                    }

                    buf.append(" WHERE ");

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
                            buf.append(" AND ");
                        }

                        Class cls = keyValues.get(i).getClass();
                        if (cls.equals(String.class)) {
                            buf.append(keys.get(i) + " like '" + keyValues.get(i) + "'");
                        } else {
                            buf.append(keys.get(i) + " == " + keyValues.get(i));
                        }

                    }

                    sqlString = buf.toString();

                    break;
                }
                case OP_DELETE: {
                    //删除
                    StringBuilder buf = new StringBuilder();
                    buf.append("DELETE FROM ");
                    buf.append(tableName + " ");
                    buf.append("WHERE ");

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
                            buf.append(" AND ");
                        }

                        Class cls = keyValues.get(i).getClass();
                        if (cls.equals(String.class)) {
                            buf.append(keys.get(i) + " like '" + keyValues.get(i) + "'");
                        } else {
                            buf.append(keys.get(i) + " = " + keyValues.get(i));
                        }

                    }

                    sqlString = buf.toString();

                    break;
                }
                case OP_TRUNCATE:
                    sqlString = getTruncateSql(model);
                    break;
                default: {
                    return null;
                }
            }

        } catch (Exception ex) {
            logErr(ex);
            return null;
        }
        logMsg(sqlString);
        return sqlString;
    }

    private String getTruncateSql(DBModelBase model) {
        StringBuilder buf = new StringBuilder();
        buf.append("DELETE FROM ");
        String tableName = SQLiteReflect.getTableNameByCls(model.getClass());
        buf.append(tableName);
        return buf.toString();
    }

    private CoreFuncReturn execInsert(DBModelBase model) {

        CoreFuncReturn result = new CoreFuncReturn();

        try {
            //todo here
            String sql = getSQL(DB_OPERATORTYPE.OP_INSERT, model);
            Object[] values = model.getValues();
            //将日期格式的数据以long的类型存储在数据库
            for (int i = 0; i < values.length; i++) {
                Object obj = values[i];
                if (obj == null)
                    continue;
                Class<?> cls = obj.getClass();
                if (cls.equals(Date.class)) {
                    long date = ((Date) obj).getTime();
                    values[i] = date / 1000;
                } else if (obj instanceof Map || obj instanceof List || obj instanceof DBModelBase) {

                    ByteArrayOutputStream bos = null;
                    ObjectOutputStream oos = null;

                    try {

                        bos = new ByteArrayOutputStream();
                        oos = new ObjectOutputStream(bos);

                        oos.writeObject(obj);

                        byte[] mapBytes = bos.toByteArray();
                        values[i] = mapBytes;

                    } catch (Exception ex) {
                        logErr(ex);
                    } finally {
                        try {
                            oos.close();
                        } catch (Exception ex) {
                        }
                        try {
                            bos.close();
                        } catch (Exception ex) {
                        }
                    }
                }
            }
            result = manager.execSQL(sql, values);


        } catch (Exception ex) {
            logErr(ex);
            result.setValues(false, ex.getMessage(), null);
            return result;
        }

        return result;

    }

    //普通存储数据
    @Override
    public CoreFuncReturn insertModel(DBModelBase model) {

        //the return of the func
        CoreFuncReturn result = new CoreFuncReturn();

        try {
            manager.openDB();

            result = execInsert(model);

        } catch (Exception ex) {
            logErr(ex);
        } finally {
            manager.closeDB();
        }
        return result;

    }


    //普通存储数据
    public CoreFuncReturn insertData(DBModelBase model) {
        //the return of the func
        CoreFuncReturn result = new CoreFuncReturn();
        try {
            result = execInsert(model);
        } catch (Exception ex) {
            logErr(ex);
        } finally {
        }
        return result;
    }




    //使用事务的方式处理数据
    @Override
    public CoreFuncReturn insertModesWithTransaction(DBModelBase[] models) {

        //the return of the func
        CoreFuncReturn result = new CoreFuncReturn();

        try {
            //todo here
            int length = models.length;
            int currentLength = 0;
            int oldLength = 0;
            while (currentLength < length) {
                oldLength = currentLength;
                //事务每次最多执行 500 条
                currentLength += 500;
                if (currentLength > length) {
                    currentLength = length;
                }

                try {

                    manager.openDB();
                    //开启事务的处理
                    manager.db.beginTransaction();

                    //插入语句
                    for (int i = oldLength; i < currentLength; i++) {
                        result = execInsert(models[i]);
                    }
                    manager.db.setTransactionSuccessful();


                } catch (Exception e) {
                    logErr(e);
                } finally {
                    manager.db.endTransaction();
                    manager.closeDB();
                }

            }


        } catch (Exception ex) {
            logErr(ex);
            result.setValues(false, ex.getMessage(), null);
            return result;
        }

        return result;
    }

    @Override
    public CoreFuncReturn updateModel(DBModelBase model) {

        CoreFuncReturn result = new CoreFuncReturn();

        try {
            //todo here
            manager.openDB();

            String sqlString = getSQL(DB_OPERATORTYPE.OP_UPDATE, model);
            Object[] values = model.getValues();

            String val = "";
            //将日期格式的数据以long的类型存储在数据库
            for (int i = 0; i < values.length; i++) {
                Object obj = values[i];
                if (obj == null)
                    continue;
                Class<?> cls = obj.getClass();
                if (cls.equals(Date.class)) {
                    long date = ((Date) obj).getTime();
                    values[i] = date / 1000;
                } else if (obj instanceof Map || obj instanceof List) {

                    ByteArrayOutputStream bos = null;
                    ObjectOutputStream oos = null;

                    try {

                        bos = new ByteArrayOutputStream();
                        oos = new ObjectOutputStream(bos);

                        oos.writeObject(obj);

                        byte[] mapBytes = bos.toByteArray();
                        values[i] = mapBytes;

                    } catch (Exception ex) {
                        logErr(ex);
                    } finally {
                        try {
                            oos.close();
                        } catch (Exception ex) {
                        }
                        try {
                            bos.close();
                        } catch (Exception ex) {
                        }
                    }
                }

                val += (values[i] + "\t\t");

            }

//            logMsg("当前执行的sql语句是：" + sqlString + "\n值是：" + val);
            result = manager.execSQL(sqlString, values);

            manager.closeDB();

        } catch (Exception ex) {
            logErr(ex);
            result.setValues(false, ex.getMessage(), null);
            return result;
        }

        return result;

    }


    public CoreFuncReturn updateData(DBModelBase model) {
        CoreFuncReturn result = new CoreFuncReturn();
        try {
            String sqlString = getSQL(DB_OPERATORTYPE.OP_UPDATE, model);
            Object[] values = model.getValues();
            String val = "";
            //将日期格式的数据以long的类型存储在数据库
            for (int i = 0; i < values.length; i++) {
                Object obj = values[i];
                if (obj == null) continue;
                Class<?> cls = obj.getClass();
                if (cls.equals(Date.class)) {
                    long date = ((Date) obj).getTime();
                    values[i] = date / 1000;
                } else if (obj instanceof Map || obj instanceof List) {
                    ByteArrayOutputStream bos = null;
                    ObjectOutputStream oos = null;
                    try {
                        bos = new ByteArrayOutputStream();
                        oos = new ObjectOutputStream(bos);
                        oos.writeObject(obj);
                        byte[] mapBytes = bos.toByteArray();
                        values[i] = mapBytes;
                    } catch (Exception ex) {
                        logErr(ex);
                    } finally {
                        try {
                            oos.close();
                        } catch (Exception ex) {
                        }
                        try {
                            bos.close();
                        } catch (Exception ex) {
                        }
                    }
                }
                val += (values[i] + "\t\t");
            }
//            logMsg("当前执行的sql语句是：" + sqlString + "\n值是：" + val);
            result = manager.execSQL(sqlString, values);
        } catch (Exception ex) {
            logErr(ex);
            result.setValues(false, ex.getMessage(), null);
            return result;
        }
        return result;
    }


    public CoreFuncReturn deleteData(DBModelBase model) {
        CoreFuncReturn result = new CoreFuncReturn();
        try {
            String sqlString = getSQL(DB_OPERATORTYPE.OP_DELETE, model);
            result = manager.execSQL(sqlString);
        } catch (Exception ex) {
            logErr(ex);
            result.setValues(false, ex.getMessage(), null);
        }
        return result;
    }


    @Override
    public CoreFuncReturn deleteModel(DBModelBase model) {

        CoreFuncReturn result = new CoreFuncReturn();

        try {
            //todo here
            manager.openDB();

            String sqlString = getSQL(DB_OPERATORTYPE.OP_DELETE, model);
            result = manager.execSQL(sqlString);

            manager.closeDB();

        } catch (Exception ex) {
            logErr(ex);
            result.setValues(false, ex.getMessage(), null);
        }

        return result;

    }

    @Override
    public CoreFuncReturn deleteAllModel(DBModelBase model) {
        CoreFuncReturn result = new CoreFuncReturn();
        try {
            manager.openDB();

            String sqlString = getSQL(DB_OPERATORTYPE.OP_TRUNCATE, model);
            Object[] values = new ArrayList<Object>().toArray(); //model.getValues();
            result = manager.execSQL(sqlString, values);

            manager.closeDB();
        } catch (Exception ex) {
            logErr(ex);
            result.setValues(false, ex.getMessage(), null);
        }

        return result;
    }

}
