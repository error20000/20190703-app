package com.jian.system.dao;

import android.content.Context;
import android.database.Cursor;

import com.jian.system.db.BaseHelper;
import com.jian.system.entity.Store;
import com.jian.system.entity.System;

import java.util.ArrayList;
import java.util.List;


public class SyncMapper {

    private final static String TAG = SyncMapper.class.getSimpleName();
    public static String tableName = "tBase_Sync";

    private BaseHelper baseHelper;

    public SyncMapper(Context context){
        baseHelper = BaseHelper.getInstance(context);
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
