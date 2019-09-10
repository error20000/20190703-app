package com.jian.system.dao;

import android.content.Context;
import android.database.Cursor;

import com.jian.system.db.BaseHelper;
import com.jian.system.entity.Equip;
import com.jian.system.entity.Store;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class StoreMapper {

    private final static String TAG = StoreMapper.class.getSimpleName();
    public static String tableName = "tBase_Store";

    private BaseHelper baseHelper;

    public StoreMapper(Context context){
        baseHelper = BaseHelper.getInstance(context);
    }

    public List<Store> selectAll(){

        List<Store> list = new ArrayList<>();
        StringBuffer buffer = new StringBuffer();
        buffer.append("select * from ");
        buffer.append(tableName);

        Cursor cursor = baseHelper.getReadableDatabase()
                .rawQuery(buffer.toString(), null);

        while (cursor.moveToNext()) {
            Store obj = new Store();
            obj.cursorToBean(cursor);
            list.add(obj);
        }
        cursor.close();
        return list;
    }

    public List<Store> selectList(Map<String, Object> condition){

        List<Store> list = new ArrayList<>();
        List<String> args = new ArrayList<>();
        StringBuffer buffer = new StringBuffer();
        buffer.append("select * from ");
        buffer.append(tableName);
        buffer.append(" where 1 = 1");
        if(condition != null){
            for (String key : condition.keySet()) {
                buffer.append(" and ").append(key).append(" = ?");
                args.add(String.valueOf(condition.get(key)));
            }
        }

        Cursor cursor = baseHelper.getReadableDatabase()
                .rawQuery(buffer.toString(), args.toArray(new String[args.size()]));

        while (cursor.moveToNext()) {
            Store obj = new Store();
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
        buffer.append(" 	 sStore_ID NVARCHAR(32) NOT NULL, ");
        buffer.append("      sStore_Name NVARCHAR(64), ");
        buffer.append("      sStore_Level1 NVARCHAR(32), ");
        buffer.append("      sStore_Level2 NVARCHAR(32), ");
        buffer.append("      sStore_Level3 NVARCHAR(32), ");
        buffer.append("      sStore_Parent NVARCHAR(32), ");
        buffer.append("      lStore_Limit NUMBER, ");
        buffer.append("  PRIMARY KEY (sStore_ID)");
        buffer.append(" );");
        return buffer.toString();
    }

    public static String dropTable(){
        return "drop table " +  tableName;
    }
}
