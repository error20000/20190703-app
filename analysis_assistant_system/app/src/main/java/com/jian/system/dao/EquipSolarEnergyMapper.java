package com.jian.system.dao;

import android.content.Context;
import android.database.Cursor;

import com.jian.system.db.BaseHelper;
import com.jian.system.entity.EquipRadar;
import com.jian.system.entity.EquipSolarEnergy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class EquipSolarEnergyMapper {

    private final static String TAG = EquipSolarEnergyMapper.class.getSimpleName();
    public static String tableName = "tEquip_SolarEnergy";

    private BaseHelper baseHelper;

    public EquipSolarEnergyMapper(Context context){
        baseHelper = BaseHelper.getInstance(context);
    }



    public EquipSolarEnergy selectOne(Map<String, Object> condition){

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

        EquipSolarEnergy obj = new EquipSolarEnergy();
        obj.cursorToBean(cursor);

        cursor.close();
        return obj;
    }


    public static String createTable(){
        StringBuffer buffer = new StringBuffer();
        buffer.append("CREATE TABLE ").append(tableName);
        buffer.append(" ( ");
        buffer.append(" 	 sEquip_ID NVARCHAR(32) NOT NULL, ");
        buffer.append("      sSolar_NO NVARCHAR(255), ");
        buffer.append("      sSolar_Type NVARCHAR(64), ");
        buffer.append("      lSolar_Volt NUMBER, ");
        buffer.append("      lSolar_Watt NUMBER, ");
        buffer.append("      sSolar_Connect NVARCHAR(255), ");
        buffer.append("  PRIMARY KEY (sEquip_ID)");
        buffer.append(" );");
        return buffer.toString();
    }

    public static String dropTable(){
        return "drop table " +  tableName;
    }
}
