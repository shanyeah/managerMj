package com.imovie.mogic.db.model;

import android.database.Cursor;

import com.imovie.mogic.db.base.SQLiteReflect;
import com.imovie.mogic.dbbase.model.BaseModel;
import com.imovie.mogic.dbbase.util.LogUtil;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;
import java.util.Map;



/**
 * Created by zhouxinshan on 2016/04/06.
 */
public class DBModelBase extends BaseModel implements Serializable {

    public DBModelBase() {

    }

    public void setModelByCursor(Cursor cursor) {

        try {


            Field[] fields = getFields();

            for (Field field : fields) {

                try {

                    Class<?> type = field.getType();
                    if (type.equals(String.class)) {

                        String val = cursor.getString(cursor.getColumnIndex(field.getName()));
                        field.set(this, val);

                    } else if (type.equals(Date.class)) {

                    /*
                    String val = cursor.getString(cursor.getColumnIndex(field.getName()));
                    if (val == null)
                        continue;
                    val = val.replace("格林尼治标准时间", "CST");
                    Date time = DateUtil.String2Date(val, "EEE MMM dd HH:mm:ss zZ yyyy");
                    */
                        long ms = cursor.getLong(cursor.getColumnIndex(field.getName())) * 1000;
                        if (ms <= 0) {
                            continue;
                        }
                        Date date = new Date(ms);
                        field.set(this, date);

                    } else if (type.equals(int.class) || type.equals(Integer.class)) {

                        int val = cursor.getInt(cursor.getColumnIndex(field.getName()));
                        field.set(this, val);

                    } else if (type.equals(long.class) || type.equals(Long.class)) {

                        long l = cursor.getLong(cursor.getColumnIndex(field.getName()));
                        field.set(this, l);

                    } else if (type.equals(boolean.class) || type.equals(Boolean.class)) {

                        String val = cursor.getString(cursor.getColumnIndex(field.getName()));
                        if (val == null)
                            continue;
                        else {
                            if (val.equals("1")) {
                                val = "true";
                            } else {
                                val = "false";
                            }
                        }
                        boolean b = Boolean.valueOf(val);
                        field.set(this, b);
                    } else if (type.equals(double.class) || type.equals(Double.class)) {

                        String val = cursor.getString(cursor.getColumnIndex(field.getName()));
                        if (val == null)
                            return;
                        double num = Double.valueOf(val);
                        field.set(this, num);
                    } else if (type.equals(float.class) || type.equals(Float.class)) {

                        String val = cursor.getString(cursor.getColumnIndex(field.getName()));
                        if (val == null)
                            return;
                        float num = Float.valueOf(val);
                        field.set(this, num);

                    } else if (type.equals(Object.class) || type.equals(Map.class) || type.equals(List.class) || type.equals(getClass())) {

                        ByteArrayInputStream bis = null;
                        ObjectInputStream ois = null;
                        try {
                            byte[] mapBytes = cursor.getBlob(cursor.getColumnIndex(field.getName()));

                            if (mapBytes == null) {
                                field.set(this, null);
                                continue;
                            }
                            bis = new ByteArrayInputStream(mapBytes);
                            ois = new ObjectInputStream(bis);

                            Object readObj = ois.readObject();

                            field.set(this, readObj);


                        } catch (Exception ex) {
                            logErr(ex);
                        } finally {

                            try {
                                bis.close();
                                ois.close();

                            } catch (Exception e) {

                            }
                        }

                    }

                } catch (Exception ex) {
                    LogUtil.LogErr(getClass(), field.getName(), ex);
                }

            }

        } catch (Exception ex) {
            logErr(ex);
        }

    }


    public String getTableName() {

        return SQLiteReflect.getTableNameByCls(this.getClass());
    }

    public String createSqlTableCreateString() {

        try {

            String tableName = getTableName();
            if (tableName == null)
                return null;

            StringBuilder stringBuilder = new StringBuilder();

            stringBuilder.append("CREATE TABLE IF NOT EXISTS ");
            stringBuilder.append(tableName);
            stringBuilder.append(" ( ");
            stringBuilder.append("DI integer  NOT NULL  PRIMARY KEY AUTOINCREMENT DEFAULT 0");

            Field[] fields = getFields();
            for (Field field : fields) {
                stringBuilder.append(", ");

                String fieldName = field.getName();
                Class cls = field.getType();

                stringBuilder.append(fieldName);
                stringBuilder.append(" ");

                if (cls.equals(int.class) || cls.equals(Integer.class)) {
                    stringBuilder.append("integer");
                } else if (cls.equals(String.class)) {
                    stringBuilder.append("text");
                } else if (cls.equals(Date.class)) {
                    stringBuilder.append("datetime");
                } else if (cls.equals(boolean.class) || cls.equals(Boolean.class)) {
                    stringBuilder.append("boolean");
                } else if (cls.equals(double.class) || cls.equals(Double.class)) {
                    stringBuilder.append("decimal");
                } else if (cls.equals(Map.class) || cls.equals(List.class)) {
                    stringBuilder.append("blob");
                } else if (cls.equals(long.class) || cls.equals(Long.class)) {
                    stringBuilder.append("long");
                }
            }

            stringBuilder.append(" )");
//            Log.e("---sql",stringBuilder.toString());
            return stringBuilder.toString();

        } catch (Exception ex) {
            logErr(ex);
        }

        return null;

    }

    /**
     * 支持接口返回data的顶级为list的情况。
     * 子类需定义属性：public List list;
     *
     * @param srclist
     */
    public void setModelByList(List srclist) {
        Field[] fields = getFields();
        Field listFeild = null;
        for (Field field : fields) {
            if ("list".equals(field.getName())) {
                listFeild = field;
                break;
            }
        }

        try {
            listFeild.set(this, srclist);
        } catch (Exception ex) {
            LogUtil.LogErr(getClass(), listFeild.getName(), ex);
        }
    }

}
