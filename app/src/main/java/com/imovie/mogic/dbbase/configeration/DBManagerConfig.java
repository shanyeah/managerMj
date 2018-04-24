package com.imovie.mogic.dbbase.configeration;

import android.content.Context;


import com.imovie.mogic.dbbase.base.BaseDatabase;
import com.imovie.mogic.dbbase.model.BaseObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhouxinshan on 2016/04/06.
 */
public final class DBManagerConfig extends BaseObject {

    private List<BaseDatabase> databases;
    private BaseDatabase defaultDatabase;

    private DBManagerConfig() {
        databases = new ArrayList<BaseDatabase>();
    }

    public final static class Builder {

        private static Builder instance;
        DBManagerConfig config;

        private Builder() {
            config = new DBManagerConfig();
        }

        public static DBManagerConfig build() {

            DBManagerConfig config = instance.config;
            instance = null;
            return config;
        }

        public static Builder defaultDatabase(Context context, String name) {

            BaseDatabase database = new BaseDatabase(context, name);
            return defaultDatabase(database);
        }

        public static Builder defaultDatabase(String path, String name) {

            BaseDatabase baseDatabase = new BaseDatabase(path, name);
            return defaultDatabase(baseDatabase);
        }

        public static Builder defaultDatabase(BaseDatabase database) {

            if (instance == null) {
                instance = new Builder();
            }

            instance.config.defaultDatabase = database;
            if (!instance.config.databases.contains(database)) {
                instance.config.databases.add(database);
            }
            return instance;
        }

        public static Builder addDatabase(Context context, String name) {

            BaseDatabase database = new BaseDatabase(context, name);
            return defaultDatabase(database);
        }

        public static Builder addDatabase(String path, String name) {

            BaseDatabase baseDatabase = new BaseDatabase(path, name);
            return defaultDatabase(baseDatabase);
        }

        public static Builder addDatabase(BaseDatabase database) {

            if (instance == null) {
                instance = new Builder();
            }

            if (!instance.config.databases.contains(database)) {
                instance.config.databases.add(database);
            }
            return instance;
        }
    }
}
