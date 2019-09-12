package com.jian.system.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jian.system.db.BaseHelper;
import com.jian.system.db.BaseHelperManager;
import com.jian.system.entity.Dict;

import java.util.ArrayList;
import java.util.List;


public class DictMapper {

    private final static String TAG = DictMapper.class.getSimpleName();
    public static String tableName = "tBase_Dict";

    private BaseHelperManager baseHelper;

    public DictMapper(Context context){
        baseHelper = BaseHelperManager.getInstance(context);
    }

    public BaseHelperManager getBaseHelper(){
        return baseHelper;
    }

    public List<Dict> selectList(String sDict_DictTypeNO){

        List<Dict> list = new ArrayList<>();

        StringBuffer buffer = new StringBuffer();
        buffer.append("select * from ");
        buffer.append(tableName);
        buffer.append(" where sDict_DictTypeNO = ? ");

        Cursor cursor = baseHelper.getReadableDatabase()
                .rawQuery(buffer.toString(), new String[]{sDict_DictTypeNO});

        while (cursor.moveToNext()) {
            Dict obj = new Dict();
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

    public void insert(List<Dict> data){
        SQLiteDatabase db = baseHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            for (Dict node: data) {
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
        buffer.append(" 	 sDict_ID NVARCHAR(32) NOT NULL , ");
        buffer.append("      sDict_NO NVARCHAR(64), ");
        buffer.append("      sDict_Name NVARCHAR(255),");
        buffer.append("      dDict_CreateDate DATE,");
        buffer.append("      sDict_UserID NVARCHAR(32),");
        buffer.append("      sDict_DictTypeNO NVARCHAR(64),");
        buffer.append("      dDict_UpdateDate DATE,");
        buffer.append("      sDict_UpdateUserID NVARCHAR(32),");
        buffer.append("      lDict_SysFlag NUMBER,");
        buffer.append("      sDict_Describe NVARCHAR(255),");
        buffer.append("      sDict_Picture NVARCHAR(255),");
        buffer.append("      sDict_Link NVARCHAR(255),");
        buffer.append("      sDict_Color NVARCHAR(255),");
        buffer.append("  PRIMARY KEY (sDict_ID)");
        buffer.append(" );");
        return buffer.toString();
    }

    public static String dropTable(){
        return "drop table if exists " +  tableName;
    }
}
