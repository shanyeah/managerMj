package com.imovie.mogic.dbbase.configeration;



import com.imovie.mogic.dbbase.base.BaseTable;
import com.imovie.mogic.dbbase.model.BaseDBModel;
import com.imovie.mogic.dbbase.model.BaseObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by zhouxinshan on 2016/04/06.
 */
public final class DatabaseConfig extends BaseObject {

    private List<BaseTable> tables;
    private int version;

    private DatabaseConfig() {
        tables = new ArrayList<BaseTable>();
        version = -1;
    }

    public List<BaseTable> getTables() {
        return tables;
    }

    public int getVersion() {
        return version;
    }

    /**
     * table config builder
     */
    public final static class Builder {

        private static Builder instance;
        private DatabaseConfig databaseConfig;

        private Builder() {
            databaseConfig = new DatabaseConfig();
        }

        public static DatabaseConfig build() {
            DatabaseConfig databaseConfig = instance.databaseConfig;
            instance = null;
            return databaseConfig;
        }

        public static Builder addTable(Class<? extends BaseDBModel> dbModelClass) {

            return addTable(dbModelClass, null);
        }

        public static Builder addTable(Class<? extends BaseDBModel> dbModelClass, String tableName) {
            return addTable(dbModelClass, tableName, null);
        }

        public static Builder addTable(Class<? extends BaseDBModel> dbModelClass, String tableName, String primaryKey) {

            BaseTable table = new BaseTable(dbModelClass, tableName, primaryKey);
            return addTable(table);
        }

        public static Builder addTable(BaseTable table) {

            if (instance == null) {
                instance = new Builder();
            }

            instance.databaseConfig.tables.add(table);

            return instance;
        }

        public static Builder addTables(List<BaseTable> tables) {

            if (instance == null) {
                instance = new Builder();
            }
            instance.databaseConfig.tables.addAll(tables);

            return instance;
        }

        public static Builder addTables(BaseTable[] tables) {

            if (instance == null) {
                instance = new Builder();
            }
            instance.databaseConfig.tables.addAll(Arrays.asList(tables));

            return instance;
        }

        public static Builder version(int version) {

            if (instance == null) {
                instance = new Builder();
            }
            instance.databaseConfig.version = version;

            return instance;
        }
    }
}
