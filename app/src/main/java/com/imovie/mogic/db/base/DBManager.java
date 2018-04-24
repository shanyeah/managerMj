package com.imovie.mogic.db.base;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.imovie.mogic.config.AppConfig;
import com.imovie.mogic.db.DBPicker.DBPicker;
import com.imovie.mogic.db.DBSaver.DBSaver;
import com.imovie.mogic.db.model.DBModelBase;
import com.imovie.mogic.db.model.DBModel_VersionControl;
import com.imovie.mogic.dbbase.model.BaseObject;
import com.imovie.mogic.dbbase.model.CoreFuncReturn;
import com.imovie.mogic.dbbase.util.LogUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by zhouxinshan on 2016/04/06.
 * To change this template use File | Settings | File Templates.
 */
public class DBManager extends BaseObject implements DBManagermentInterface {

    public SQLiteDatabase db = null;
    public String path;

    public DBManager() {
        this(AppConfig.dbPath);
    }

    public DBManager(String path) {
        this.path = path;
        if (DBExists()) {
            openDB();
            closeDB();
        }
    }

    private boolean DBExists() {
        boolean isOK = false;

        File dbFile = new File(path);

        isOK = dbFile.exists();

        return isOK;
    }

    public boolean checkDB(int newestVersion) {
        boolean isOk = false;
        try {

            File dbFile = new File(path);
            if (!dbFile.exists()) {

                if (!dbFile.isDirectory()) {
                    File parentFile = dbFile.getParentFile();
                    if (!parentFile.exists()) {
                        parentFile.mkdirs();
                    }
                    boolean b = dbFile.createNewFile();
                    if (!b) {
                        return b;
                    }
                    db = SQLiteDatabase.openOrCreateDatabase(path, null);
                }

                if (db.isOpen()) {
                    closeDB();
                }

            }

            //check table update
            int currentDBVersion = getDbVersion();
            if (currentDBVersion < newestVersion) {
                updateTables(newestVersion);
                logMsg("update database");
            }

        } catch (Exception e) {
            isOk = false;
            logErr(e);
        }

        return isOk;
    }

    private boolean updateTables(int newestVersion) {
        boolean isOk = false;

        try {

            int oldVersion = getDbVersion();

            //check old version
            if (oldVersion == 0) {
                openDB();
                for (int i = 0; i < SQLiteScript.getTBCreateScriptList().size(); i++) {

                    String tbCreateStr = SQLiteScript.getTBCreateScriptList().get(i);

                    db.execSQL(tbCreateStr);

                    isOk = true;

                }
                closeDB();
            } else {
                //old version is not equals current version

                //get version remember model list
                List<DBModel_VersionControl> oldVersionModelList = new DBPicker().pickModelsWithModelCode(DBModel_VersionControl.class, "version = " + oldVersion);

                //if not get, must be have mistake, create all tables
                if (oldVersionModelList.size() == 0) {

                    openDB();
                    for (int i = 0; i < SQLiteScript.getTBCreateScriptList().size(); i++) {

                        String tbCreateStr = SQLiteScript.getTBCreateScriptList().get(i);

                        db.execSQL(tbCreateStr);

                        isOk = true;

                    }
                    closeDB();

                } else {

                    //check the update

                    Map<Class, String> map = SQLiteReflect.map_ModelTableName;

                    List modelClasses = Arrays.asList(map.keySet().toArray());
//                    List tableNames = Arrays.asList(map.values().toArray());

                    //get old version database models class's list and its properties
                    List<Class> oldVersionModelClassList = new ArrayList<Class>();
                    Map<Class, String[]> oldVersionModelPropertyListMap = new HashMap<Class, String[]>();
                    for (int i = 0; i < oldVersionModelList.size(); i++) {
                        DBModel_VersionControl model_versionControl =
                                (DBModel_VersionControl) oldVersionModelList.get(i);

                        String tableName = model_versionControl.tableName;
                        Class cls = SQLiteReflect.getClsByTableName(tableName);
                        if (cls != null) {
                            oldVersionModelClassList.add(cls);

                            String fields = model_versionControl.fields;
                            String[] properties = fields.split(",");
                            oldVersionModelPropertyListMap.put(cls, properties);
                        }

                    }

                    //compare between old and new
                    for (int i = 0; i < modelClasses.size(); i++) {

                        try {

                            Class cls = (Class) modelClasses.get(i);
                            DBModelBase model = (DBModelBase) cls.newInstance();

                            //it is not a new table
                            if (oldVersionModelClassList.contains(cls)) {

                                String[] propertyNames = model.getPropertyNames();
                                String[] oldPropertyNames = oldVersionModelPropertyListMap.get(cls);
                                List<String> newPropertyList = new ArrayList<>(Arrays.asList(propertyNames));
                                List<String> oldPropertyList = new ArrayList<>(Arrays.asList(oldPropertyNames));
                                checkAndUpdateTableStructure(cls, model, oldPropertyList, newPropertyList);
                            } else {
                                //it is a new table
                                String sqlCreateString = model.createSqlTableCreateString();
                                openDB();
                                db.execSQL(sqlCreateString);
                                addIndex(db);
                                //openIndex
                                closeDB();
                            }

                        } catch (Exception e) {
                            logErr(e);
                        }

                    }

                }
            }

            setVersionControlTable(newestVersion);

        } catch (Exception ex) {
            logErr(ex);
        } finally {
            closeDB();
        }

        return isOk;
    }

    /**
     * 检查并更新表结构
     */
    private void checkAndUpdateTableStructure(Class cls, DBModelBase model, List<String> oldPropertyList, List<String> newPropertyList) {
        String tableName = SQLiteReflect.getTableNameByCls(cls);
        if (isTableStructureModify(tableName, oldPropertyList, newPropertyList)) {
            // 删除临时表sql
            String sqlDeleteTableString = "DROP TABLE IF EXISTS " + tableName + "_temp";
            // 旧表重命名为临时表sql
            String sqlRenameString = "ALTER TABLE " + tableName + " RENAME TO " + tableName + "_temp";
            // 创建新表sql
            String sqlNewTableCreateString = model.createSqlTableCreateString();
            // 将临时表数据迁移会新表sql。 删除的字段数据会丢失
            Set<String> commonPropertyNames = new HashSet<>(); // 新旧表相同字段集
            for (String oldName : oldPropertyList) {
                if (newPropertyList.contains(oldName)) {
                    commonPropertyNames.add(oldName);
                }
            }
            StringBuilder copyStringBuilder = new StringBuilder();
            copyStringBuilder.append("INSERT INTO ");
            copyStringBuilder.append(tableName);
            copyStringBuilder.append("( DI");
            for (String propertyName : commonPropertyNames) {
                copyStringBuilder.append(", ");
                copyStringBuilder.append(propertyName);
            }
            copyStringBuilder.append(") ");
            copyStringBuilder.append("SELECT ");
            copyStringBuilder.append(" DI");
            for (String propertyName : commonPropertyNames) {
                copyStringBuilder.append(", ");
                copyStringBuilder.append(propertyName);
            }
            copyStringBuilder.append(" FROM ");
            copyStringBuilder.append(tableName + "_temp");
            String sqlDataCopyString = copyStringBuilder.toString();

            try {
                // 执行sql.
                // 1.删除临时表（防止存在导致2步出错）
                // 2.重命名为临时表
                // 3.创建新表
                // 4.迁移旧数据
                // 5.删除临时表
                openDB();
                db.execSQL(sqlDeleteTableString);
                db.execSQL(sqlRenameString);
                db.execSQL(sqlNewTableCreateString);
                db.execSQL(sqlDataCopyString);
                db.execSQL(sqlDeleteTableString);
                addIndex(db);
            } catch (Exception e) {
                logErr(e);
            } finally {
                closeDB();
            }
        }
    }


    public static void addIndex(SQLiteDatabase db) {
        Log.d("test", "add Index For DataBase");
        try {
            db.execSQL("CREATE UNIQUE INDEX index_hash on MovieInfo(hash) ");
        } catch (Exception e) {
        }
        try {
            db.execSQL("CREATE UNIQUE INDEX index_movid on MovieInfo(movieId) ");
        } catch (Exception e) {
        }
        try {
            db.execSQL("CREATE UNIQUE INDEX index_hash2 on TotalMovieInfo(hash) ");
        } catch (Exception e) {
        }
        try {
            db.execSQL("CREATE UNIQUE INDEX index_movieId3 on TotalMoviePy(movieId) ");
        } catch (Exception e) {
        }
        try {
            db.execSQL("CREATE UNIQUE INDEX index_hash3 on FileDistReport(hash) ");
        } catch (Exception e) {
        }
        try {
            db.execSQL("CREATE UNIQUE INDEX index_hash3 on UploadData(hash) ");
        } catch (Exception e) {
        }
        try {
            db.execSQL("CREATE UNIQUE INDEX index_hash4 on ReadyMovie(hash) ");
        } catch (Exception e) {
        }

        try {
            db.execSQL("CREATE UNIQUE INDEX index_hash5 on NASSharefile(hash,filePathHash) ");
        } catch (Exception e) {
        }


        }


    /**
     * 判断表结构是否有更改。
     * 只删不增字段视为无改动。
     */
    private boolean isTableStructureModify(String tableName, List<String> oldPropertyList, List<String> newPropertyList) {
        // 对比数量
        if (newPropertyList.size() != oldPropertyList.size()) {
            LogUtil.LogMsg(this.getClass(), tableName + "表结构字段数量有差异。" + newPropertyList.size() + "|" + oldPropertyList.size());
            return true;
        }

        // 查看是否有新字段
        for (String newPropertyName : newPropertyList) {
            if (!oldPropertyList.contains(newPropertyName)) {
                LogUtil.LogMsg(this.getClass(), tableName + "表结构字段至少有一个字段更新。" + newPropertyName);
                return true;
            }
        }

        LogUtil.LogMsg(this.getClass(), tableName + "表结构字段没有字段更新。");
        return false;
    }

    private void setVersionControlTable(int newestVersion) {

        try {

            Map<Class, String> map = SQLiteReflect.map_ModelTableName;

            List modelClasses = Arrays.asList(map.keySet().toArray());
            List tableNames = Arrays.asList(map.values().toArray());

            for (int i = 0; i < modelClasses.size(); i++) {

                try {

                    Class cls = (Class) modelClasses.get(i);
                    DBModelBase model = (DBModelBase) cls.newInstance();

                    StringBuilder stringBuilder = new StringBuilder();
                    String[] propertyNames = model.getPropertyNames();
                    for (String propertyName : propertyNames) {
                        stringBuilder.append(propertyName);
                        stringBuilder.append(",");
                    }

                    String fields = stringBuilder.substring(0, stringBuilder.length() - 1);

                    DBModel_VersionControl versionControl = new DBModel_VersionControl();
                    versionControl.version = newestVersion;
                    versionControl.tableName = (String) tableNames.get(i);
                    versionControl.fields = fields;

                    DBSaver saver = new DBSaver();
                    saver.insertModel(versionControl);

                } catch (Exception e) {
                    logErr(e);
                }

            }
            setDbVersion(newestVersion);

        } catch (Exception ex) {
            logErr(ex);
        }

    }

    public boolean openDB() {

        db = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READWRITE);
        if (db != null) {
            if (db.isOpen()) {
//                logMsg("数据库已打开");
                return true;
            }
        }
        return false;

    }

    public boolean closeDB() {

        boolean isOk = false;

        try {
            if (!db.isOpen()) {
//                logMsg("数据库已关闭");
                return true;
            }
            db.close();
            isOk = true;
//            logMsg("数据库已关闭");
        } catch (Exception e) {
            logErr(e);
        }

        return isOk;
    }

    /**
     * execSQL function, remember openDB before using this function
     * warning : after finished use,please close your cursor first then closeDB
     *
     * @param sql sqlFormatString
     * @return a CoreFuncReturn with tag cursor if you select or the result
     */
    @Override
    public CoreFuncReturn execSQL(String sql) {

        CoreFuncReturn result = new CoreFuncReturn();

        try {

            // 1.判断查询语句的类型
            while (true) {
                if (sql.substring(0, 1).equals(" ")) {
                    sql = sql.substring(1);
                    continue;
                }
                break;
            }
            int index = sql.indexOf(" ");
            boolean temp = sql.substring(0, index).toLowerCase().contains("select");
            if (temp) {
                //判断为select语句
                Cursor resultCursor = db.rawQuery(sql, null);
                result.setValues(true, "数据查询完毕！", resultCursor);

            } else {
                //判断为其他查询语句
                db.execSQL(sql);
                result.setValues(true, "操作完成！", null);

            }


        } catch (Exception ex) {
            logErr(ex);
            return result;

        }
        return result;
    }

    /**
     * execSQL function with parameters, remember openDB before using this function
     * warning : after finished use,please close your cursor first then closeDB
     *
     * @param sqlFormatString sqlFormatString
     * @param args            parameters
     * @return a CoreFuncReturn with tag cursor if you select or the result
     */
    @Override
    public CoreFuncReturn execSQL(String sqlFormatString, Object[] args) {

        CoreFuncReturn result = new CoreFuncReturn();

        try {
            // 1.判断查询语句的类型
            while (true) {
                if (sqlFormatString.substring(0, 1).equals(" ")) {
                    sqlFormatString = sqlFormatString.substring(1);
                    continue;
                }
                break;
            }
            int index = sqlFormatString.indexOf(" ");
            boolean temp = sqlFormatString.substring(0, index).toLowerCase().contains("select");
            if (temp) {
                //判断为select语句
                String[] tempStrArr = (String[]) args;
                Cursor resultCursor = db.rawQuery(sqlFormatString, tempStrArr);
                result.setValues(true, "数据查询完毕！", resultCursor);
            } else {
                //判断为其他查询语句
                db.execSQL(sqlFormatString, args);
                result.setValues(true, "操作完成！", null);
            }

        } catch (Exception ex) {
            logErr(ex);
            return result;

        }
        return result;

    }

    public boolean setDbVersion(int version) {
        try {
            openDB();
            db.setVersion(version);
            closeDB();
            return true;
        } catch (Exception e) {
            logErr(e);
        }
        return false;

    }

    public int getDbVersion() {
        int ver = -1;
        try {
            if (!db.isOpen()) {
                openDB();
            }
            ver = db.getVersion();
            closeDB();
        } catch (Exception e) {
            logErr(e);
        }
        return ver;
    }
}
