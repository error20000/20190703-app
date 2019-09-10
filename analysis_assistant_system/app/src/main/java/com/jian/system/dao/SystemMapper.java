package com.jian.system.dao;

import android.content.Context;
import android.database.Cursor;

import com.jian.system.db.BaseHelper;
import com.jian.system.entity.Dict;
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
