package com.imovie.mogic.dbbase.base;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import com.imovie.mogic.db.base.DBManager;
import com.imovie.mogic.dbbase.configeration.DatabaseConfig;
import com.imovie.mogic.dbbase.exception.DatabaseNotInitException;
import com.imovie.mogic.dbbase.model.BaseObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;



/**
 * Created by zhouxinshan on 2016/04/06.
 */
public class BaseDatabase extends BaseObject implements IDatabase {

    public interface OnDatabaseUpdateListener {

        void onDatabaseUpdate(BaseDatabase database, int oldVersion, int newVersion);
    }

    OnDatabaseUpdateListener onDatabaseUpdateListener;

    protected SQLiteDatabase database;
    protected String path;
    protected String name;
    protected String fullPath;
    protected int oldVersion;
    protected int version;
    protected List<BaseTable> tables;


    public BaseDatabase(@NonNull Context context, @NonNull String name) {

        String path = context.getDir("database", Context.MODE_PRIVATE).getPath() + File.separator;

        this.path = path;
        this.name = name;
        fullPath = path + name + ".db";
    }

    public BaseDatabase(@NonNull String path, @NonNull String name) {

        this.path = path;
        this.name = name;
        fullPath = path + name + ".db";
    }

    public void init(DatabaseConfig config, OnDatabaseUpdateListener onDatabaseUpdateListener) {

        boolean isOk = createDatabase();
        if (isOk) {
            tables = config.getTables();
            createTables();
            oldVersion = database.getVersion();
            version = oldVersion;
            setVersion(config.getVersion());
            this.onDatabaseUpdateListener = onDatabaseUpdateListener;
            checkVersion();
        }

    }

    public void setVersion(int version) {

        if (version < 0)
            return;
        if (version <= oldVersion)
            return;

        open();
        database.setVersion(version);
        this.version = version;
        close();
    }

    public int getVersion() {
        return version;
    }

    public void checkVersion() {

        if (oldVersion == version)
            return;

        if (onDatabaseUpdateListener != null) {
            onDatabaseUpdateListener.onDatabaseUpdate(this, oldVersion, version);
        }
    }

    /**
     * if not exist database file, create it
     *
     * @return success
     */
    protected boolean createDatabase() {

        try {

            File dbFile = new File(fullPath);

            boolean isOK = dbFile.exists();
            if (!isOK) {

                if (!dbFile.exists()) {

                    if (!dbFile.isDirectory()) {
                        File parentFile = dbFile.getParentFile();
                        if (!parentFile.exists()) {
                            parentFile.mkdirs();
                        }
                        boolean b = dbFile.createNewFile();
                        if (!b) {
                            return false;
                        }
                        database = SQLiteDatabase.openOrCreateDatabase(path, null);
                    }

                    if (database.isOpen()) {
                        close();
                    }
                    return true;
                }
            } else {
                open();
                close();
            }

        } catch (Exception e) {
            return false;
        }
        return true;

    }

    /**
     * create tables
     */
    protected void createTables() {

        if (tables == null)
            return;

        List<String> createTableStringList = new ArrayList<String>();
        for (BaseTable table : tables) {

            try {

                String tableName = table.getTableName();
                if (tableName == null)
                    continue;

                StringBuilder stringBuilder = new StringBuilder();

                stringBuilder.append("CREATE TABLE IF NOT EXISTS ");
                stringBuilder.append(tableName);
                stringBuilder.append(" ( ");
                stringBuilder.append("DI integer  NOT NULL  PRIMARY KEY AUTOINCREMENT DEFAULT 0");

                BaseTable.Column[] columns = table.getColumns();
                for (BaseTable.Column column : columns) {
                    stringBuilder.append(", ");

                    String fieldName = column.columnName;
                    String sqlTypeString = ColumnType.getSqlTypeString(column.columnType);

                    stringBuilder.append(fieldName);
                    stringBuilder.append(" ");

                    stringBuilder.append(sqlTypeString);
                }
                stringBuilder.append(" )");
                createTableStringList.add(stringBuilder.toString());

            } catch (Exception ex) {
                logErr(ex);
            }
        }

        open();
        for (String s : createTableStringList) {
            database.execSQL(s);
        }
        DBManager.addIndex(database);

        close();
    }

    public String getDatabaseName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {

        if (o == null || !(o instanceof BaseDatabase))
            return false;

        BaseDatabase other = (BaseDatabase) o;
        if (path == null || other.path == null ||
                name == null || other.name == null)
            return false;

        return path.equals(other.path) && name.equals(other.name);
    }

    @Override
    public boolean open() {

        if (database == null)
            return false;

        database = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READWRITE);
        return database.isOpen();
    }

    @Override
    public boolean close() {
        boolean isOk = false;

        if (database == null)
            return false;

        try {
            if (!database.isOpen()) {
                return true;
            }
            database.close();
            isOk = true;
        } catch (Exception e) {
            logErr(e);
        }

        return isOk;
    }

    @Override
    public void exec(String sql) throws DatabaseNotInitException {

        if (database == null)
            throw new DatabaseNotInitException(this);
        database.execSQL(sql);
    }

    @Override
    public void exec(String sqlFormatString, Object[] args) throws DatabaseNotInitException {

        if (database == null)
            throw new DatabaseNotInitException(this);
        database.execSQL(sqlFormatString, args);
    }
}
