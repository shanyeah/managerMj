package com.imovie.mogic.dbbase.exception;


import com.imovie.mogic.dbbase.base.BaseDatabase;

/**
 * Created by zhouxinshan on 2016/04/06.
 */
public class DatabaseNotInitException extends BaseException {

    public DatabaseNotInitException() {
        super();
    }

    public DatabaseNotInitException(BaseDatabase database) {

        super(database.getDatabaseName() + "is not init!");
    }
}
