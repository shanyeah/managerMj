package com.imovie.mogic.dbbase.model;

import android.database.Cursor;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by zhouxinshan on 2016/04/06.
 */
public class BaseDBModel extends BaseModel {

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
                    Date date = DateUtil.String2Date(val, "EEE MMM dd HH:mm:ss zZ yyyy");
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

                }

            }

        } catch (Exception ex) {
            logErr(ex);
        }

    }


}
