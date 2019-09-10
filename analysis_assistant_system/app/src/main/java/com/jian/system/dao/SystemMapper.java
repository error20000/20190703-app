package com.jian.system.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jian.system.db.BaseHelper;
import com.jian.system.entity.Dict;
import com.jian.system.entity.Sync;
import com.jian.system.entity.System;

import java.util.ArrayList;
import java.util.List;


public class SystemMapper {

    private final static String TAG = SystemMapper.class.getSimpleName();
    public static String tableName = "tBase_System";

    private BaseHelper baseHelper;

    public SystemMapper(Context context){
        baseHelper = BaseHelper.getInstance(context);
    }


    public System selectOne(){
        StringBuffer buffer = new StringBuffer();
        buffer.append("select * from ");
        buffer.append(tableName);
        buffer.append(" limit 1 ");

        Cursor cursor = baseHelper.getReadableDatabase()
                .rawQuery(buffer.toString(), null);

        System obj = new System();
        obj.cursorToBean(cursor);

        cursor.close();
        return obj;
    }

    public List<System> selectAll(){
        List<System> list = new ArrayList<>();
        StringBuffer buffer = new StringBuffer();
        buffer.append("select * from ");
        buffer.append(tableName);

        Cursor cursor = baseHelper.getReadableDatabase()
                .rawQuery(buffer.toString(), null);

        while (cursor.moveToNext()) {
            System obj = new System();
            obj.cursorToBean(cursor);
            list.add(obj);
        }
        cursor.close();
        return list;
    }

    //TODO --------------------------------------------------------------------------------同步数据
    public void deleteAll(){
        baseHelper.getReadableDatabase()
                .delete(tableName, null, null);
    }

    public void insert(List<System> data){
        SQLiteDatabase db = baseHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            for (System system: data) {
                db.insert(tableName, null, system.beanToValues());
            }
        }catch (Exception e){

        }finally {
            db.endTransaction(); // 处理完成
            db.close();
        }
        baseHelper.close();
    }

    public static String createTable(){
        StringBuffer buffer = new StringBuffer();
        buffer.append("CREATE TABLE ").append(tableName);
        buffer.append(" ( ");
        buffer.append(" 	 sSys_ID NVARCHAR(32) NOT NULL, ");
        buffer.append("      sSys_MapService NVARCHAR(255),");
        buffer.append("      lSys_MapLat NUMBER,");
        buffer.append("      lSys_MapLng NUMBER,");
        buffer.append("      lSys_MapLevel NUMBER,");
        buffer.append("      lSys_MapIconWidth NUMBER,");
        buffer.append("      lSys_MapIconHeight NUMBER,");
        buffer.append("  PRIMARY KEY (sSys_ID)");
        buffer.append(" );");
        return buffer.toString();
    }

    public static String dropTable(){
        return "drop table " +  tableName;
    }
}
