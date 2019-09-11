package com.jian.system.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.jian.system.db.BaseHelper;
import com.jian.system.entity.AidMapIcon;
import com.jian.system.entity.AidTypeMapIcon;

import java.util.List;


public class AidTypeMapIconMapper {

    private final static String TAG = AidTypeMapIconMapper.class.getSimpleName();
    public static String tableName = "tBase_AidTypeMapIcon";

    private BaseHelper baseHelper;

    public AidTypeMapIconMapper(Context context){
        baseHelper = BaseHelper.getInstance(context);
    }

    public BaseHelper getBaseHelper(){
        return baseHelper;
    }


    //TODO --------------------------------------------------------------------------------同步数据
    public void deleteAll(){
        baseHelper.getReadableDatabase()
                .delete(tableName, null, null);
    }

    public void insert(List<AidTypeMapIcon> data){
        SQLiteDatabase db = baseHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            for (AidTypeMapIcon node: data) {
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
        buffer.append(" 	 sAidTypeIcon_ID NVARCHAR(32) NOT NULL, ");
        buffer.append("      sAidTypeIcon_Status NVARCHAR(64), ");
        buffer.append("      sAidTypeIcon_StatusIcon NVARCHAR(64), ");
        buffer.append("      sAidTypeIcon_Type NVARCHAR(64), ");
        buffer.append("  PRIMARY KEY (sAidTypeIcon_ID)");
        buffer.append(" );");
        return buffer.toString();
    }

    public static String dropTable(){
        return "drop table " +  tableName;
    }
}
