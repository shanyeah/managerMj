package com.imovie.mogic.dbbase.base;


import com.imovie.mogic.dbbase.model.BaseModel;
import com.imovie.mogic.dbbase.model.BaseObject;

import java.util.Date;
import java.util.List;
import java.util.Map;



/**
 * Created by zhouxinshan on 2016/04/06.
 */
public class ColumnType extends BaseObject {

    public final static int TYPE_UNKNOW = -1;
    public final static int TYPE_INTEGER = 0;
    public final static int TYPE_LONG = 1;
    public final static int TYPE_TEXT = 2;
    public final static int TYPE_BOOLEAN = 3;
    public final static int TYPE_DOUBLE = 4;
    public final static int TYPE_FLOAT = 5;
    public final static int TYPE_DATETIME = 6;
    public final static int TYPE_DECIMAL = 7;
    public final static int TYPE_BLOB = 8;

    public static int getColumnType(Class cls) {

        int type = TYPE_UNKNOW;
        if (cls.equals(String.class)) {
            type = TYPE_TEXT;
        } else if (cls.equals(int.class) || cls.equals(Integer.class)) {
            type = TYPE_INTEGER;
        } else if (cls.equals(double.class) || cls.equals(Double.class)) {
            type = TYPE_DOUBLE;
        } else if (cls.equals(Date.class)) {
            type = TYPE_DATETIME;
        } else if (List.class.isAssignableFrom(cls) || Map.class.isAssignableFrom(cls)) {
            type = TYPE_BLOB;
        } else if (BaseModel.class.isAssignableFrom(cls)) {
            type = TYPE_BLOB;
        } else if (cls.equals(boolean.class) || cls.equals(Boolean.class)) {
            type = TYPE_BOOLEAN;
        } else if (cls.equals(long.class) || cls.equals(Long.class)) {
            type = TYPE_LONG;
        } else if (cls.equals(float.class) || cls.equals(Float.class)) {
            type = TYPE_FLOAT;
        }
        return type;
    }

    public static String getSqlTypeString(int type) {

        String typeString;

        switch (type) {

            case TYPE_TEXT: {
                typeString = "text";
                break;
            }
            case TYPE_INTEGER: {
                typeString = "integer";
                break;
            }
            case TYPE_DOUBLE: {
                typeString = "decimal";
                break;
            }
            case TYPE_DATETIME: {
                typeString = "datetime";
                break;
            }
            case TYPE_BLOB: {
                typeString = "blob";
                break;
            }
            case TYPE_BOOLEAN: {
                typeString = "boolean";
                break;
            }
            case TYPE_LONG: {
                typeString = "long";
                break;
            }
            case TYPE_FLOAT: {
                typeString = "float";
                break;
            }
            default: {
                typeString = "text";
                break;
            }
        }

        return typeString;
    }

}
