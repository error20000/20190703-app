package com.jian.system.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jian.system.db.BaseHelper;
import com.jian.system.db.BaseHelperManager;
import com.jian.system.entity.Dict;
import com.jian.system.entity.Store;
import com.jian.system.entity.Sync;
import com.jian.system.entity.System;

import java.util.ArrayList;
import java.util.List;


public class SyncMapper {

    private final static String TAG = SyncMapper.class.getSimpleName();
    public static String tableName = "tBase_Sync";

    private BaseHelperManager baseHelper;

    public SyncMapper(Context context){
        baseHelper = BaseHelperManager.getInstance(context);
    }

    public BaseHelperManager getBaseHelper(){
        return baseHelper;
    }

    public List<Sync> selectAll(){
        List<Sync> list = new ArrayList<>();
        StringBuffer buffer = new StringBuffer();
        buffer.append("select * from ");
        buffer.append(tableName);

        Cursor cursor = baseHelper.getReadableDatabase()
                .rawQuery(buffer.toString(), null);

        while (cursor.moveToNext()) {
            Sync obj = new Sync();
            obj.cursorToBean(cursor);
            list.add(obj);
        }
        cursor.close();
        return list;
    }

    public Sync selectOne(String sSync_TableName){
        StringBuffer buffer = new StringBuffer();
        buffer.append(" select * from ");
        buffer.append(tableName);
        buffer.append(" where sSync_TableName = ? ");

        Cursor cursor = baseHelper.getReadableDatabase()
                .rawQuery(buffer.toString(), new String[]{sSync_TableName});

        Sync obj = null;
        if(cursor.moveToNext()) {
            obj = new Sync();
            obj.cursorToBean(cursor);
        }

        cursor.close();
        return obj;
    }

    //TODO --------------------------------------------------------------------------------同步数据
    public void deleteAll(){
        baseHelper.getReadableDatabase()
                .delete(tableName, null, null);
    }

    public void insert(Sync data){
        baseHelper.getWritableDatabase()
                .insert(tableName, null, data.beanToValues());
    }

    public void update(Sync data){
        baseHelper.getWritableDatabase()
                .update(tableName, data.beanToValues(), "sSync_TableName = ?", new String[]{data.getsSync_TableName()});
    }

    //TODO --------------------------------------------------------------------------------------创建表
    public static String createTable(){
        StringBuffer buffer = new StringBuffer();
        buffer.append("CREATE TABLE ").append(tableName);
        buffer.append(" ( ");
        buffer.append(" 	 sSync_TableName NVARCHAR(32) NOT NULL, ");
        buffer.append("      dSync_UpdateDate DATE,");
        buffer.append("  PRIMARY KEY (sSync_TableName)");
        buffer.append(" );");
        return buffer.toString();
    }

    public static String dropTable(){
        return "drop table if exists " +  tableName;
    }
}
