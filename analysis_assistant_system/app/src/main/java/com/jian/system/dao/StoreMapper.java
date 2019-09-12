package com.jian.system.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jian.system.db.BaseHelper;
import com.jian.system.db.BaseHelperManager;
import com.jian.system.entity.Equip;
import com.jian.system.entity.Store;
import com.jian.system.entity.StoreType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class StoreMapper {

    private final static String TAG = StoreMapper.class.getSimpleName();
    public static String tableName = "tBase_Store";

    private BaseHelperManager baseHelper;

    public StoreMapper(Context context){
        baseHelper = BaseHelperManager.getInstance(context);
    }

    public BaseHelperManager getBaseHelper(){
        return baseHelper;
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

    //TODO --------------------------------------------------------------------------------同步数据
    public void deleteAll(){
        baseHelper.getReadableDatabase()
                .delete(tableName, null, null);
    }

    public void insert(List<Store> data){
        SQLiteDatabase db = baseHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            for (Store node: data) {
                db.insert(tableName, null, node.beanToValues());
            }
            db.setTransactionSuccessful();
        }catch (Exception e){

        }finally {
            db.endTransaction(); // 处理完成
            db.close();
        }
        baseHelper.close();
    }

    //TODO --------------------------------------------------------------------------------------创建表
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
        return "drop table if exists " +  tableName;
    }
}
