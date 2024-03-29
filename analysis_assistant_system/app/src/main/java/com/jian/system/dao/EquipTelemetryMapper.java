package com.jian.system.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jian.system.db.BaseHelper;
import com.jian.system.db.BaseHelperManager;
import com.jian.system.entity.EquipSpareLamp;
import com.jian.system.entity.EquipTelemetry;
import com.jian.system.entity.EquipViceLamp;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class EquipTelemetryMapper {

    private final static String TAG = EquipTelemetryMapper.class.getSimpleName();
    public static String tableName = "tEquip_Telemetry";

    private BaseHelperManager baseHelper;

    public EquipTelemetryMapper(Context context){
        baseHelper = BaseHelperManager.getInstance(context);
    }

    public BaseHelperManager getBaseHelper(){
        return baseHelper;
    }

    public EquipTelemetry selectOne(Map<String, Object> condition){

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
        buffer.append(" limit 1 ");

        Cursor cursor = baseHelper.getReadableDatabase()
                .rawQuery(buffer.toString(), args.toArray(new String[args.size()]));

        EquipTelemetry obj = new EquipTelemetry();
        if (cursor.moveToNext()) {
            obj.cursorToBean(cursor);
        }

        cursor.close();
        return obj;
    }


    //TODO --------------------------------------------------------------------------------同步数据
    public void deleteAll(){
        baseHelper.getReadableDatabase()
                .delete(tableName, null, null);
    }

    public void insert(List<EquipTelemetry> data){
        SQLiteDatabase db = baseHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            for (EquipTelemetry node: data) {
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
        buffer.append(" 	 sEquip_ID NVARCHAR(32) NOT NULL, ");
        buffer.append("      sTelemetry_Mode NVARCHAR(64), ");
        buffer.append("      lTelemetry_Watt NUMBER, ");
        buffer.append("      sTelemetry_NO NVARCHAR(255), ");
        buffer.append("      lTelemetry_Volt NUMBER, ");
        buffer.append("      sTelemetry_SIM NVARCHAR(255), ");
        buffer.append("  PRIMARY KEY (sEquip_ID)");
        buffer.append(" );");
        return buffer.toString();
    }

    public static String dropTable(){
        return "drop table if exists " +  tableName;
    }
}
