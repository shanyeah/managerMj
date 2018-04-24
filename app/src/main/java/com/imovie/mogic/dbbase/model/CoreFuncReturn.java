package com.imovie.mogic.dbbase.model;

import java.io.Serializable;

/**
 * Created by zhouxinshan on 2016/04/06.
 * <p/>
 * This is the basic return object for the core functionality such as web, database
 */
public class CoreFuncReturn extends BaseObject implements Serializable {

    public boolean isOK;
    public String msg;
    public Object tag;

    public CoreFuncReturn() {
        this(false);
    }

    public CoreFuncReturn(boolean isOK) {
        this(isOK, null);
    }

    public CoreFuncReturn(boolean isOK, String msg) {
        this(isOK, msg, null);
    }

    public CoreFuncReturn(boolean isOK, String msg, Object tag) {

        this.isOK = isOK;
        this.msg = msg;
        this.tag = tag;
    }

    public void setValues(boolean isok, String msg, Object tag) {
        this.isOK = isok;
        this.msg = msg;
        this.tag = tag;
    }
}