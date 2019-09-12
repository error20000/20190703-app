package com.jian.system.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.jian.system.db.BaseHelper;
import com.jian.system.db.BaseHelperManager;
import com.jian.system.entity.AidEquip;
import com.jian.system.entity.AidMapIcon;

import java.util.List;


public class AidMapIconMapper {

    private final static String TAG = AidMapIconMapper.class.getSimpleName();
    public static String tableName = "tBase_AidMapIcon";

    private BaseHelperManager baseHelper;

    public AidMapIconMapper(Context context){
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

    public void insert(List<AidMapIcon> data){
        SQLiteDatabase db = baseHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            for (AidMapIcon node: data) {
                db.insert(tableName, null, node.beanToValues());
            }
            db.setTransactionSuccessful();
        }catch (Exception e){

        }finally {
            db.endTransaction(); // 处理完成
        }
        baseHelper.close();
    }

    //TODO --------------------------------------------------------------------------------------创建表

    public static String createTable(){
        StringBuffer buffer = new StringBuffer();
        buffer.append("CREATE TABLE ").append(tableName);
        buffer.append(" ( ");
        buffer.append(" 	 sAidIcon_ID NVARCHAR(32) NOT NULL, ");
        buffer.append("      sAidIcon_Status NVARCHAR(64), ");
        buffer.append("      sAidIcon_StatusIcon NVARCHAR(64), ");
        buffer.append("      sAidIcon_AidID NVARCHAR(32), ");
        buffer.append("  PRIMARY KEY (sAidIcon_ID)");
        buffer.append(" );");
        return buffer.toString();
    }

    public static String dropTable(){
        return "drop table if exists " +  tableName;
    }
}
