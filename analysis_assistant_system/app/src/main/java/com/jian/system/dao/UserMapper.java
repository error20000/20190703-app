package com.jian.system.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jian.system.db.BaseHelper;
import com.jian.system.entity.User;

import java.util.ArrayList;
import java.util.List;


public class UserMapper {

    private final static String TAG = UserMapper.class.getSimpleName();
    public static String tableName = "tBase_User";

    private BaseHelper baseHelper;

    public UserMapper(Context context){
        baseHelper = BaseHelper.getInstance(context);
    }

    public BaseHelper getBaseHelper(){
        return baseHelper;
    }

    public void insert(User user){
        baseHelper.getWritableDatabase()
                .insert(tableName, null, user.beanToValues());
    }

    public void delete(){
        baseHelper.getWritableDatabase()
                .delete(tableName, null, null);
    }

    public List<User> selectAll(){

        List<User> list = new ArrayList<>();

        StringBuffer buffer = new StringBuffer();
        buffer.append("select * from ");
        buffer.append(tableName);

        Cursor cursor = baseHelper.getReadableDatabase()
                .rawQuery(buffer.toString(), null);

        while (cursor.moveToNext()) {
            User obj = new User();
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

    public void insert(List<User> data){
        SQLiteDatabase db = baseHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            for (User node: data) {
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
        buffer.append(" 	 sUser_ID NVARCHAR(32) NOT NULL,");
        buffer.append("      sUser_UserName NVARCHAR(32),");
        buffer.append("      sUser_PassWord NVARCHAR(32),");
        buffer.append("      sUser_Nick NVARCHAR(10),");
        buffer.append("      lUser_StatusFlag NUMBER,");
        buffer.append("      sUser_GroupID NVARCHAR(32),");
        buffer.append("      sUser_QQ NVARCHAR(20),");
        buffer.append("      sUser_Email NVARCHAR(255),");
        buffer.append("      sUser_Phone NVARCHAR(20),");
        buffer.append("      sUser_ThirdID NVARCHAR(32),");
        buffer.append("      dUser_CreateDate DATE,");
        buffer.append("      sUser_UserID NVARCHAR(32),");
        buffer.append("  PRIMARY KEY( sUser_ID )");
        buffer.append(" );");
        return buffer.toString();
    }

    public static String dropTable(){
        return "drop table " +  tableName;
    }
}
