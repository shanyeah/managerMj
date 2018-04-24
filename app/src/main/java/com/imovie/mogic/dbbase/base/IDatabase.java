package com.imovie.mogic.dbbase.base;


import com.imovie.mogic.dbbase.exception.DatabaseNotInitException;

/**
 * Created by zhouxinshan on 2016/04/06.
 */
public interface IDatabase {

    boolean open();

    boolean close();

    void exec(String sql) throws DatabaseNotInitException;

    void exec(String sqlFormatString, Object[] args) throws DatabaseNotInitException;
}
