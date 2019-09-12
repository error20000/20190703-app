package com.jian.system.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jian.system.db.BaseHelper;
import com.jian.system.db.BaseHelperManager;
import com.jian.system.entity.AidEquip;
import com.jian.system.entity.EquipViceLamp;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class AidEquipMapper {

    private final static String TAG = AidEquipMapper.class.getSimpleName();
    public static String tableName = "tBase_AidEquip";

    private BaseHelperManager baseHelper;

    public AidEquipMapper(Context context){
        baseHelper = BaseHelperManager.getInstance(context);
    }

    public BaseHelperManager getBaseHelper(){
        return baseHelper;
    }


    //TODO --------------------------------------------------------------------------------同步数据
    public void deleteAll(){
        baseHelper.getReadableDatabase()
                .delete(tableName, null, null);
    }

    public void insert(List<AidEquip> data){
        SQLiteDatabase db = baseHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            for (AidEquip node: data) {
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
        buffer.append(" 	 sAidEquip_ID NVARCHAR(32) NOT NULL, ");
        buffer.append("      sAidEquip_AidID NVARCHAR(32), ");
        buffer.append("      sAidEquip_EquipID NVARCHAR(32), ");
        buffer.append("      dAidEquip_CreateDate DATE, ");
        buffer.append("  PRIMARY KEY (sAidEquip_ID)");
        buffer.append(" );");
        return buffer.toString();
    }

    public static String dropTable(){
        return "drop table if exists " +  tableName;
    }
}
