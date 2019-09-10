package com.jian.system.dao;

import android.content.Context;
import android.database.Cursor;

import com.jian.system.db.BaseHelper;
import com.jian.system.entity.Equip;
import com.jian.system.entity.StoreType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class StoreTypeMapper {

    private final static String TAG = StoreTypeMapper.class.getSimpleName();
    public static String tableName = "tBase_StoreType";

    private BaseHelper baseHelper;

    public StoreTypeMapper(Context context){
        baseHelper = BaseHelper.getInstance(context);
    }


    public List<StoreType> selectAll(){

        List<StoreType> list = new ArrayList<>();
        StringBuffer buffer = new StringBuffer();
        buffer.append("select * from ");
        buffer.append(tableName);

        Cursor cursor = baseHelper.getReadableDatabase()
                .rawQuery(buffer.toString(), null);

        while (cursor.moveToNext()) {
            StoreType obj = new StoreType();
            obj.cursorToBean(cursor);
            list.add(obj);
        }
        cursor.close();
        return list;
    }

    public List<Map<String, Object>> storeMap(){
        List<Map<String, Object>> list = new ArrayList<>();
        StringBuffer buffer = new StringBuffer();
        buffer.append(" select ");
        buffer.append("     a.*, b.sDict_Picture sStoreType_MapIconPic ");
        buffer.append(" from ").append(tableName).append(" a  ");
        buffer.append(" `left join tBase_Dict b on a.sStoreType_MapIcon = b.sDict_NO and b.sDict_DictTypeNO = 'StoreMapIcon'  ");

        Cursor cursor = baseHelper.getReadableDatabase()
                .rawQuery(buffer.toString(), null);

        while (cursor.moveToNext()) {
            Map<String, Object> obj = new HashMap<>();;
            for (int i = 0; i < cursor.getColumnCount(); i++) {
                obj.put(cursor.getColumnName(i), cursor.getString(i));
            }
            list.add(obj);
        }
        cursor.close();
        return list;
    }


    public static String createTable(){
        StringBuffer buffer = new StringBuffer();
        buffer.append("CREATE TABLE ").append(tableName);
        buffer.append(" ( ");
        buffer.append(" 	 sStoreType_ID NVARCHAR(32) NOT NULL, ");
        buffer.append("      sStoreType_Name NVARCHAR(64), ");
        buffer.append("      sStoreType_Address NVARCHAR(255), ");
        buffer.append("      lStoreType_Lat NUMBER, ");
        buffer.append("      lStoreType_Lng NUMBER, ");
        buffer.append("      sStoreType_Station NVARCHAR(64), ");
        buffer.append("      lStoreType_Limit NUMBER, ");
        buffer.append("      sStoreType_MapIcon NVARCHAR(64), ");
        buffer.append("      lStoreType_LatDu NUMBER, ");
        buffer.append("      lStoreType_LatFen NUMBER, ");
        buffer.append("      lStoreType_LatMiao NUMBER, ");
        buffer.append("      lStoreType_LngDu NUMBER, ");
        buffer.append("      lStoreType_LngFen NUMBER, ");
        buffer.append("      lStoreType_LngMiao NUMBER, ");
        buffer.append("  PRIMARY KEY (sStoreType_ID)");
        buffer.append(" );");
        return buffer.toString();
    }

    public static String dropTable(){
        return "drop table " +  tableName;
    }
}
