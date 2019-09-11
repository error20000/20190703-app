package com.jian.system.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jian.system.db.BaseHelper;
import com.jian.system.entity.Aid;
import com.jian.system.entity.Dict;
import com.jian.system.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AidMapper {

    private final static String TAG = AidMapper.class.getSimpleName();
    public static String tableName = "tBase_Aid";

    private BaseHelper baseHelper;

    public AidMapper(Context context){
        baseHelper = BaseHelper.getInstance(context);
    }

    public BaseHelper getBaseHelper(){
        return baseHelper;
    }

    public List<Aid> selectPageByUser(Map<String, Object> condition, String sUser_ID,  int start, int rows){
        List<Aid> list = new ArrayList<>();
        List<String> args = new ArrayList<>();
        StringBuffer buffer = new StringBuffer();
        buffer.append("select a.* from ");
        buffer.append(tableName).append(" a ");
        buffer.append(" left join tBase_UserAid b on a.sAid_ID = b.sUserAid_AidID ");
        buffer.append(" where 1=1  ");
        if(condition != null){
            for (String key : condition.keySet()) {
                buffer.append(" and a.").append(key).append(" = ? ");
                args.add(String.valueOf(condition.get(key)));
            }
        }
        if(!Utils.isNullOrEmpty(sUser_ID)){
            buffer.append(" and b.sUserAid_UserID = ? ");
            args.add(sUser_ID);
        }
        buffer.append(" limit ").append((start/rows + 1) * rows).append(" , ").append(rows);

        Cursor cursor = baseHelper.getReadableDatabase()
                .rawQuery(buffer.toString(), args.toArray(new String[args.size()]) );

        while (cursor.moveToNext()) {
            Aid obj = new Aid();
            obj.cursorToBean(cursor);
            list.add(obj);
        }
        cursor.close();
        return list;
    }

    public long sizeByUser(Map<String, Object> condition, String sUser_ID){
        List<Aid> list = new ArrayList<>();
        List<String> args = new ArrayList<>();
        StringBuffer buffer = new StringBuffer();
        buffer.append("select count(distinct a.sAid_ID) from ");
        buffer.append(tableName).append(" a ");
        buffer.append(" left join tBase_UserAid b on a.sAid_ID = b.sUserAid_AidID ");
        buffer.append(" where 1=1  ");
        if(condition != null){
            for (String key : condition.keySet()) {
                buffer.append(" and a.").append(key).append(" = ? ");
                args.add(String.valueOf(condition.get(key)));
            }
        }
        if(!Utils.isNullOrEmpty(sUser_ID)){
            buffer.append(" and b.sUserAid_UserID = ? ");
            args.add(sUser_ID);
        }

        Cursor cursor = baseHelper.getReadableDatabase()
                .rawQuery(buffer.toString(), args.toArray(new String[args.size()]) );

        long count = cursor.getLong(0);
        cursor.close();
        return count;
    }

    public List<Map<String, Object>> aidAll(){
        List<Map<String, Object>> list = new ArrayList<>();
        StringBuffer buffer = new StringBuffer();
        buffer.append("select sAid_ID, sAid_Name, sAid_NO from ");
        buffer.append(tableName);

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

    public List<Aid> search(String keywords, String sUser_ID){
        List<Aid> list = new ArrayList<>();
        List<String> args = new ArrayList<>();
        StringBuffer buffer = new StringBuffer();
        buffer.append("select a.* from ");
        buffer.append(tableName).append(" a ");
        buffer.append(" left join tBase_UserAid b on a.sAid_ID = b.sUserAid_AidID ");
        buffer.append(" where a.sAid_NO like '%"+keywords+"%'  ");
        if(!Utils.isNullOrEmpty(sUser_ID)){
            buffer.append(" and b.sUserAid_UserID = ? ");
            args.add(sUser_ID);
        }

        Cursor cursor = baseHelper.getReadableDatabase()
                .rawQuery(buffer.toString(), args.toArray(new String[args.size()]) );

        while (cursor.moveToNext()) {
            Aid obj = new Aid();
            obj.cursorToBean(cursor);
            list.add(obj);
        }
        cursor.close();
        return list;
    }

    public List<Map<String, Object>> equip(String sAid_ID){
        List<Map<String, Object>> list = new ArrayList<>();
        StringBuffer buffer = new StringBuffer();
        buffer.append("select ");
        buffer.append(" a.*, b.*, ");
        buffer.append(" b.sEquip_Name sAidEquip_EquipName,  ");
        buffer.append(" b.sEquip_NO sAidEquip_EquipNO,  ");
        buffer.append(" b.sEquip_Type sAidEquip_EquipType,  ");
        buffer.append(" d.sDict_Name sAidEquip_EquipTypeName  ");
        buffer.append(" from tBase_AidEquip a ");
        buffer.append(" left join tBase_Equip b on a.sAidEquip_EquipID = b.sEquip_ID ");
        buffer.append(" left join tBase_Dict d on b.sEquip_Type = d.sDict_NO  and d.sDict_DictTypeNO = 'EquipType' ");
        buffer.append(" left join tBase_Dict e on b.sEquip_Icon = e.sDict_NO and e.sDict_DictTypeNO = 'EquipIcon' ");
        buffer.append(" where a.sAidEquip_AidID = ? ");
        
        Cursor cursor = baseHelper.getReadableDatabase()
                .rawQuery(buffer.toString(), new String[]{sAid_ID});

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

    public List<Map<String, Object>> aidMap(){
        List<Map<String, Object>> list = new ArrayList<>();
        StringBuffer buffer = new StringBuffer();
        buffer.append("select ");
        buffer.append(" a.*, ");
        buffer.append(" d.sDict_Picture sAid_StatusIcon, e.sDict_Picture sAid_TypeIcon,  ");
        buffer.append(" f.sDict_Name sAid_StationName,  ");
        buffer.append(" g.sDict_Picture sAid_IconUrl ");
        buffer.append(" h.sDict_Name sAid_TypeName,  ");
        buffer.append(" from ").append(tableName).append(" a ");
        buffer.append(" left join tBase_AidMapIcon b on a.sAid_ID = b.sAidIcon_AidID and a.sAid_Status = b.sAidIcon_Status ");
        buffer.append(" left join tBase_AidTypeMapIcon c on a.sAid_Type = c.sAidTypeIcon_Type and a.sAid_Status = c.sAidTypeIcon_Status ");
        buffer.append(" left join tBase_Dict d on b.sAidIcon_StatusIcon = d.sDict_NO  and d.sDict_DictTypeNO = 'MapIcon' ");
        buffer.append(" left join tBase_Dict e on c.sAidTypeIcon_StatusIcon = e.sDict_NO and e.sDict_DictTypeNO = 'MapIcon' ");
        buffer.append(" left join tBase_Dict f on a.sAid_Station = f.sDict_NO and f.sDict_DictTypeNO = 'AidStation' ");
        buffer.append(" left join tBase_Dict g on a.sAid_Icon = g.sDict_NO and g.sDict_DictTypeNO = 'AidIcon' ");
        buffer.append(" left join tBase_Dict h on a.sAid_Type = h.sDict_NO and h.sDict_DictTypeNO = 'AidType' ");
        
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


    //TODO --------------------------------------------------------------------------------同步数据
    public void deleteAll(){
        baseHelper.getReadableDatabase()
                .delete(tableName, null, null);
    }

    public void insert(List<Aid> data){
        SQLiteDatabase db = baseHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            for (Aid node: data) {
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
        buffer.append(" 	 sAid_ID NVARCHAR(32) NOT NULL , ");
        buffer.append("      sAid_Name NVARCHAR(64), ");
        buffer.append("      sAid_NO NVARCHAR(64),");
        buffer.append("      lAid_Lat NUMBER,");
        buffer.append("      lAid_Lng NUMBER,");
        buffer.append("      sAid_Station NVARCHAR(64),");
        buffer.append("      sAid_Type NVARCHAR(64),");
        buffer.append("      sAid_Icon NVARCHAR(255),");
        buffer.append("      dAid_BuildDate DATE,");
        buffer.append("      dAid_DelDate DATE,");
        buffer.append("      sAid_Lighting NVARCHAR(64),");
        buffer.append("      sAid_Mark NVARCHAR(64),");
        buffer.append("      sAid_NfcID NVARCHAR(32),");
        buffer.append("      dAid_CreateDate DATE,");
        buffer.append("      sAid_Status NVARCHAR(64),");
        buffer.append("      lAid_LatDu NUMBER,");
        buffer.append("      lAid_LatFen NUMBER,");
        buffer.append("      lAid_LatMiao NUMBER,");
        buffer.append("      lAid_LngDu NUMBER,");
        buffer.append("      lAid_LngFen NUMBER,");
        buffer.append("      lAid_LngMiao NUMBER,");
        buffer.append("  PRIMARY KEY (sAid_ID)");
        buffer.append(" );");
        return buffer.toString();
    }

    public static String dropTable(){
        return "drop table " +  tableName;
    }
}
