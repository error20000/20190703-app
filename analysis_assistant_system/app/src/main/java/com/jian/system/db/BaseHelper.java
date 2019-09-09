package com.jian.system.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseHelper extends SQLiteOpenHelper{

    private static Map<String, BaseHelper> dbMaps = new HashMap<String, BaseHelper>();
    private List<String> createTableList;
    private String dbName;

    private BaseHelper(Context context, String dbName, int dbVersion, String createTableSql) {
        super(context, dbName, null, dbVersion);
        this.dbName = dbName;
        createTableList = new ArrayList<String>();
        createTableList.add(createTableSql);
    }

    public static BaseHelper getInstance(Context context, String dbName, int dbVersion, String createTableSql) {
        BaseHelper baseHelper = dbMaps.get(dbName);
        if (baseHelper == null) {
            baseHelper = new BaseHelper(context, dbName, dbVersion, createTableSql);
        }
        dbMaps.put(dbName, baseHelper);
        return baseHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
