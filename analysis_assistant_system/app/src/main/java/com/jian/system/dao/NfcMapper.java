package com.jian.system.dao;

import android.content.Context;
import android.database.Cursor;

import com.jian.system.db.BaseHelper;
import com.jian.system.entity.Dict;
import com.jian.system.entity.Nfc;
import com.jian.system.entity.System;

import java.util.ArrayList;
import java.util.List;


public class NfcMapper {

    private final static String TAG = NfcMapper.class.getSimpleName();
    public static String tableName = "tBase_Nfc";

    private BaseHelper baseHelper;

    public NfcMapper(Context context){
        baseHelper = BaseHelper.getInstance(context);
    }


    public List<Nfc> selectAll(){

        List<Nfc> list = new ArrayList<>();

        StringBuffer buffer = new StringBuffer();
        buffer.append("select * from ");
        buffer.append(tableName);

        Cursor cursor = baseHelper.getReadableDatabase()
                .rawQuery(buffer.toString(), null);

        while (cursor.moveToNext()) {
            Nfc obj = new Nfc();
            obj.cursorToBean(cursor);
            list.add(obj);
        }
        cursor.close();
        return list;
    }

    public List<Nfc> unbind(){
        List<Nfc> list = new ArrayList<>();

        StringBuffer buffer = new StringBuffer();
        buffer.append("select * from ");
        buffer.append(tableName);
        buffer.append(" where lNfc_StatusFlag = 0 ");

        Cursor cursor = baseHelper.getReadableDatabase()
                .rawQuery(buffer.toString(), null);

        while (cursor.moveToNext()) {
            Nfc obj = new Nfc();
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
        buffer.append(" 	 sNfc_ID NVARCHAR(32) NOT NULL, ");
        buffer.append("      sNfc_Name NVARCHAR(64), ");
        buffer.append("      sNfc_NO NVARCHAR(64), ");
        buffer.append("      lNfc_StatusFlag NUMBER, ");
        buffer.append("      dNfc_CreateDate DATE, ");
        buffer.append("  PRIMARY KEY (sNfc_ID)");
        buffer.append(" );");
        return buffer.toString();
    }

    public static String dropTable(){
        return "drop table " +  tableName;
    }
}
