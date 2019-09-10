package com.jian.system.dao;

import android.content.Context;
import android.database.Cursor;

import com.jian.system.db.BaseHelper;
import com.jian.system.entity.EquipLamp;
import com.jian.system.entity.EquipRadar;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class EquipRadarMapper {

    private final static String TAG = EquipRadarMapper.class.getSimpleName();
    public static String tableName = "tEquip_Radar";

    private BaseHelper baseHelper;

    public EquipRadarMapper(Context context){
        baseHelper = BaseHelper.getInstance(context);
    }



    public EquipRadar selectOne(Map<String, Object> condition){

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

        EquipRadar obj = new EquipRadar();
        obj.cursorToBean(cursor);

        cursor.close();
        return obj;
    }


    public static String createTable(){
        StringBuffer buffer = new StringBuffer();
        buffer.append("CREATE TABLE ").append(tableName);
        buffer.append(" ( ");
        buffer.append(" 	 sEquip_ID NVARCHAR(32) NOT NULL, ");
        buffer.append("      sRadar_NO NVARCHAR(64), ");
        buffer.append("      sRadar_Band NVARCHAR(64), ");
        buffer.append("  PRIMARY KEY (sEquip_ID)");
        buffer.append(" );");
        return buffer.toString();
    }

    public static String dropTable(){
        return "drop table " +  tableName;
    }
}
