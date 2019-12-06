package com.jian.system.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jian.system.db.BaseHelper;
import com.jian.system.db.BaseHelperManager;
import com.jian.system.entity.Equip;
import com.jian.system.entity.EquipAis;
import com.jian.system.entity.EquipLog;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class EquipLogMapper {

    private final static String TAG = EquipLogMapper.class.getSimpleName();
    public static String tableName = "tBase_EquipLog";

    private BaseHelperManager baseHelper;

    public EquipLogMapper(Context context){
        baseHelper = BaseHelperManager.getInstance(context);
    }

    public BaseHelperManager getBaseHelper(){
        return baseHelper;
    }

    public List<EquipLog> selectList(Map<String, Object> condition){
        List<EquipLog> list = new ArrayList<>();
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
            EquipLog obj = new EquipLog();
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

    public void insert(List<EquipLog> data){
        SQLiteDatabase db = baseHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            for (EquipLog node: data) {
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
        buffer.append(" 	 sELog_ID NVARCHAR(32) NOT NULL, ");
        buffer.append("      sELog_Type NVARCHAR(64), ");
        buffer.append("      dELog_CreateDate DATE, ");
        buffer.append("      sELog_UserID NVARCHAR(32), ");
        buffer.append("      sELog_EquipID NVARCHAR(32), ");
        buffer.append("      sELog_Describe NVARCHAR(255), ");
        buffer.append("      sELog_Remarks NVARCHAR(255), ");
        buffer.append("      sELog_IP NVARCHAR(255), ");
        buffer.append("      sELog_StoreLv1 NVARCHAR(32), ");
        buffer.append("      sELog_StoreLv2 NVARCHAR(32), ");
        buffer.append("      sELog_StoreLv3 NVARCHAR(32), ");
        buffer.append("      sELog_StoreLv4 NVARCHAR(32), ");
        buffer.append("      sELog_AidID NVARCHAR(32), ");
        buffer.append("  PRIMARY KEY (sELog_ID)");
        buffer.append(" );");
        return buffer.toString();
    }

    public static String dropTable(){
        return "drop table if exists " +  tableName;
    }
}
