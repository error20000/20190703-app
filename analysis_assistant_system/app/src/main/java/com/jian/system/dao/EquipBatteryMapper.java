package com.jian.system.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jian.system.db.BaseHelper;
import com.jian.system.entity.EquipAis;
import com.jian.system.entity.EquipBattery;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class EquipBatteryMapper {

    private final static String TAG = EquipBatteryMapper.class.getSimpleName();
    public static String tableName = "tEquip_Battery";

    private BaseHelper baseHelper;

    public EquipBatteryMapper(Context context){
        baseHelper = BaseHelper.getInstance(context);
    }

    public BaseHelper getBaseHelper(){
        return baseHelper;
    }

    public EquipBattery selectOne(Map<String, Object> condition){

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

        EquipBattery obj = new EquipBattery();
        obj.cursorToBean(cursor);

        cursor.close();
        return obj;
    }

    //TODO --------------------------------------------------------------------------------同步数据
    public void deleteAll(){
        baseHelper.getReadableDatabase()
                .delete(tableName, null, null);
    }

    public void insert(List<EquipBattery> data){
        SQLiteDatabase db = baseHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            for (EquipBattery node: data) {
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
        buffer.append(" 	 sEquip_ID NVARCHAR(32) NOT NULL, ");
        buffer.append("      sBattery_NO NVARCHAR(255), ");
        buffer.append("      sBattery_Type NVARCHAR(64), ");
        buffer.append("      lBattery_Volt NUMBER, ");
        buffer.append("      lBattery_Watt NUMBER, ");
        buffer.append("      sBattery_Connect NVARCHAR(255), ");
        buffer.append("  PRIMARY KEY (sEquip_ID)");
        buffer.append(" );");
        return buffer.toString();
    }

    public static String dropTable(){
        return "drop table " +  tableName;
    }
}
