package com.imovie.mogic.dbbase.base;

import android.support.annotation.NonNull;

import com.imovie.mogic.dbbase.model.BaseDBModel;
import com.imovie.mogic.dbbase.model.BaseObject;
import com.imovie.mogic.dbbase.model.CoreFuncReturn;

import java.lang.reflect.Field;


/**
 * Created by zhouxinshan on 2016/04/06.
 * <p/>
 * base table object
 */
public class BaseTable extends BaseObject implements ITable {

    public static class Column extends BaseObject {

        public int columnType;
        public String columnName;

    }

    protected BaseDatabase mDataBase;

    protected String tableName;
    protected String primaryKey;
    protected Class<? extends BaseDBModel> baseDBModelClass;
    protected Column[] columns;

    public BaseTable(@NonNull Class<? extends BaseDBModel> baseDBModelClass) {
        this(baseDBModelClass, null);
    }

    public BaseTable(@NonNull Class<? extends BaseDBModel> baseDBModelClass, String tableName) {
        this(baseDBModelClass, tableName, null);
    }

    public BaseTable(@NonNull Class<? extends BaseDBModel> baseDBModelClass, String tableName, String primaryKey) {

        this.baseDBModelClass = baseDBModelClass;
        this.tableName = tableName;
        this.primaryKey = primaryKey;

        if (tableName == null) {
            this.tableName = baseDBModelClass.getSimpleName();
        }
        columns = getColumns();
    }

    /**
     * get columns from table
     *
     * @return columns
     */
    public Column[] getColumns() {

        if (columns != null)
            return columns;

        Field[] fields = baseDBModelClass.getFields();

        if (fields != null) {

            Column[] columns = new Column[fields.length];
            int index = 0;
            for (Field field : fields) {

                Class cls = field.getType();
                String name = field.getName();

                Column column = new Column();
                column.columnType = ColumnType.getColumnType(cls);
                column.columnName = name;

                columns[index++] = column;
            }
            return columns;
        }
        return null;
    }

    public String getTableName() {
        return tableName;
    }

    public String getPrimaryKey() {
        return primaryKey;
    }

    @Override
    public boolean equals(Object o) {

        if (o == null || !(o instanceof BaseTable))
            return false;

        BaseTable other = (BaseTable) o;

        return tableName.equals(other.tableName);
    }

    @Override
    public CoreFuncReturn insert(BaseDBModel model) {
        return null;
    }

    @Override
    public CoreFuncReturn delete(BaseDBModel model) {
        return null;
    }

    @Override
    public CoreFuncReturn update(BaseDBModel model) {
        return null;
    }

}
