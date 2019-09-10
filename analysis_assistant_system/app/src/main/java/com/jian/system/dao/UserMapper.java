package com.jian.system.dao;

import android.content.Context;
import android.database.Cursor;

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
