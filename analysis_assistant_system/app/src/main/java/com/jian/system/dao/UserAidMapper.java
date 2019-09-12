package com.jian.system.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.jian.system.db.BaseHelper;
import com.jian.system.db.BaseHelperManager;
import com.jian.system.entity.AidTypeMapIcon;
import com.jian.system.entity.UserAid;

import java.util.List;


public class UserAidMapper {

    private final static String TAG = UserAidMapper.class.getSimpleName();
    public static String tableName = "tBase_UserAid";

    private BaseHelperManager baseHelper;

    public UserAidMapper(Context context){
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

    public void insert(List<UserAid> data){
        SQLiteDatabase db = baseHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            for (UserAid node: data) {
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
        buffer.append(" 	 sUserAid_ID NVARCHAR(32) NOT NULL, ");
        buffer.append("      sUserAid_UserID NVARCHAR(32), ");
        buffer.append("      sUserAid_AidID NVARCHAR(32), ");
        buffer.append("  PRIMARY KEY (sUserAid_ID)");
        buffer.append(" );");
        return buffer.toString();
    }

    public static String dropTable(){
        return "drop table if exists " +  tableName;
    }
}
