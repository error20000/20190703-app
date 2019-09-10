package com.jian.system.dao;

import android.content.Context;
import android.database.Cursor;

import com.jian.system.db.BaseHelper;
import com.jian.system.entity.Equip;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class EquipMapper {

    private final static String TAG = EquipMapper.class.getSimpleName();
    public static String tableName = "tBase_Equip";

    private BaseHelper baseHelper;

    public EquipMapper(Context context){
        baseHelper = BaseHelper.getInstance(context);
    }


    public List<Equip> selectPage(Map<String, Object> condition, int start, int rows){
        List<Equip> list = new ArrayList<>();
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
        buffer.append(" limit ").append((start/rows + 1) * rows).append(" , ").append(rows);

        Cursor cursor = baseHelper.getReadableDatabase()
                .rawQuery(buffer.toString(), args.toArray(new String[args.size()]));

        while (cursor.moveToNext()) {
            Equip obj = new Equip();
            obj.cursorToBean(cursor);
            list.add(obj);
        }
        cursor.close();
        return list;
    }

    public long size(Map<String, Object> condition){
        List<String> args = new ArrayList<>();
        StringBuffer buffer = new StringBuffer();
        buffer.append("select count (*) from ");
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

        long count = cursor.getLong(0);
        cursor.close();
        return count;
    }

    public Equip selectOne(Map<String, Object> condition){
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

        Equip obj = new Equip();
        obj.cursorToBean(cursor);

        cursor.close();
        return obj;
    }

    public List<Equip> selectList(Map<String, Object> condition){
        List<Equip> list = new ArrayList<>();
        List<String> args = new ArrayList<>();
        StringBuffer buffer = new StringBuffer();
        buffer.append("select * from ");
        buffer.append(tableName);
        buffer.append(" where 1 = 1");
        if(condition != null){
            for (String key : condition.keySet()) {
                buffer.append(" and ").append(key).append(" = ? ");
                args.add(String.valueOf(condition.get(key)));
            }
        }

        Cursor cursor = baseHelper.getReadableDatabase()
                .rawQuery(buffer.toString(), args.toArray(new String[args.size()]));

        while (cursor.moveToNext()) {
            Equip obj = new Equip();
            obj.cursorToBean(cursor);
            list.add(obj);
        }
        cursor.close();
        return list;
    }

    public List<Equip> search(String keywords){
        List<Equip> list = new ArrayList<>();
        StringBuffer buffer = new StringBuffer();
        buffer.append("select * from ");
        buffer.append(tableName);
        buffer.append(" where sEquip_NO like '%"+keywords+"%' ");

        Cursor cursor = baseHelper.getReadableDatabase()
                .rawQuery(buffer.toString(), null);

        while (cursor.moveToNext()) {
            Equip obj = new Equip();
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
        buffer.append(" 	 sEquip_ID NVARCHAR(32) NOT NULL, ");
        buffer.append("      sEquip_Name NVARCHAR(64), ");
        buffer.append("      sEquip_StoreLv1 NVARCHAR(32), ");
        buffer.append("      sEquip_StoreLv2 NVARCHAR(32), ");
        buffer.append("      sEquip_StoreLv3 NVARCHAR(32), ");
        buffer.append("      sEquip_StoreLv4 NVARCHAR(32), ");
        buffer.append("      sEquip_Type NVARCHAR(64), ");
        buffer.append("      sEquip_Status NVARCHAR(64), ");
        buffer.append("      sEquip_NfcID NVARCHAR(32), ");
        buffer.append("      sEquip_AidID NVARCHAR(32), ");
        buffer.append("      dEquip_CreateDate DATE, ");
        buffer.append("      sEquip_NO NVARCHAR(64), ");
        buffer.append("      sEquip_Icon NVARCHAR(64), ");
        buffer.append("      sEquip_Manufacturer NVARCHAR(64), ");
        buffer.append("      sEquip_MModel NVARCHAR(255), ");
        buffer.append("      dEquip_ArrivalDate DATE, ");
        buffer.append("      dEquip_UseDate DATE, ");
        buffer.append("      dEquip_StoreDate DATE, ");
        buffer.append("      sEquip_MBrand NVARCHAR(255), ");
        buffer.append("      dEquip_DumpDate DATE, ");
        buffer.append("  PRIMARY KEY (sEquip_ID)");
        buffer.append(" );");
        return buffer.toString();
    }

    public static String dropTable(){
        return "drop table " +  tableName;
    }
}
