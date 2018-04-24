package com.imovie.mogic.db.base;


import com.imovie.mogic.db.model.DBModelBase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



/**
 * Created by zhouxinshan on 2014/12/19.
 */
public class SQLiteScript {

    private List<String> tbScriptList;

    public static List<String> getTBCreateScriptList() {
        SQLiteScript sqLiteScript = new SQLiteScript();
        return sqLiteScript.tbScriptList;
    }

    SQLiteScript() {

        tbScriptList = new ArrayList<String>();

        List modelClassList = Arrays.asList(SQLiteReflect.map_ModelTableName.keySet().toArray());

        for (int i = 0; i < modelClassList.size(); i++) {

            try {

                Class modelClass = (Class) modelClassList.get(i);

                DBModelBase model = (DBModelBase) modelClass.newInstance();

                String sqlTbCreateString = model.createSqlTableCreateString();

                tbScriptList.add(sqlTbCreateString);

            } catch (Exception ex) {

            }


        }

    }
}
