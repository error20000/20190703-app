package com.jian.system.dao;

import android.content.Context;
import android.database.Cursor;

import com.jian.system.db.BaseHelper;
import com.jian.system.db.BaseHelperManager;
import com.jian.system.entity.Aid;
import com.jian.system.entity.EquipAis;
import com.jian.system.entity.Messages;
import com.jian.system.utils.Utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MessagesMapper {

    private final static String TAG = MessagesMapper.class.getSimpleName();
    public static String tableName = "tBase_Message";

    private BaseHelperManager baseHelper;

    public MessagesMapper(Context context){
        baseHelper = BaseHelperManager.getInstance(context);
    }

    public BaseHelperManager getBaseHelper(){
        return baseHelper;
    }

    public List<Messages> selectPageByUser(Map<String, Object> condition,
                                           Date startDate, Date endDate,
                                           String sUser_ID, int start, int rows){
        List<Messages> list = new ArrayList<>();
        List<String> args = new ArrayList<>();
        StringBuffer buffer = new StringBuffer();
        buffer.append(" select * ");
        buffer.append(" from ").append(tableName);
        buffer.append(" where 1=1  ");
        if(condition != null){
            for (String key : condition.keySet()) {
                buffer.append(" and ").append(key).append(" = ? ");
                args.add(String.valueOf(condition.get(key)));
            }
        }
        if(!Utils.isNullOrEmpty(sUser_ID)){
            buffer.append(" and sMsg_ToUserID = ? ");
            args.add(sUser_ID);
        }
        if(!Utils.isNullOrEmpty(startDate)){
            buffer.append(" and dMsg_CreateDate >= ? ");
            args.add(startDate.getTime()+"");
        }
        if(!Utils.isNullOrEmpty(endDate)){
            buffer.append(" and dMsg_CreateDate <= ? ");
            args.add(endDate.getTime()+"");
        }
        buffer.append(" order by dMsg_CreateDate desc  ");
        buffer.append(" limit ").append((start/rows + 1) * rows).append(" , ").append(rows);

        Cursor cursor = baseHelper.getReadableDatabase()
                .rawQuery(buffer.toString(), args.toArray(new String[args.size()]) );

        while (cursor.moveToNext()) {
            Messages obj = new Messages();
            obj.cursorToBean(cursor);
            list.add(obj);
        }
        cursor.close();
        return list;
    }

    public long sizeByUser(Map<String, Object> condition, Date startDate, Date endDate, String sUser_ID){
        List<String> args = new ArrayList<>();
        StringBuffer buffer = new StringBuffer();
        buffer.append(" select count(*) ");
        buffer.append(" from ").append(tableName);
        buffer.append(" where 1=1  ");
        if(condition != null){
            for (String key : condition.keySet()) {
                buffer.append(" and ").append(key).append(" = ? ");
                args.add(String.valueOf(condition.get(key)));
            }
        }
        if(!Utils.isNullOrEmpty(sUser_ID)){
            buffer.append(" and sMsg_ToUserID = ? ");
            args.add(sUser_ID);
        }
        if(!Utils.isNullOrEmpty(startDate)){
            buffer.append(" and dMsg_CreateDate >= ? ");
            args.add(startDate.getTime()+"");
        }
        if(!Utils.isNullOrEmpty(endDate)){
            buffer.append(" and dMsg_CreateDate <= ? ");
            args.add(endDate.getTime()+"");
        }

        Cursor cursor = baseHelper.getReadableDatabase()
                .rawQuery(buffer.toString(), args.toArray(new String[args.size()]) );

        long count = 0;
        if(cursor.moveToNext()) {
            count = cursor.getLong(0);
        }
        cursor.close();
        return count;
    }

    public List<Messages> search(String keywords, String sUser_ID){
        List<Messages> list = new ArrayList<>();
        List<String> args = new ArrayList<>();
        StringBuffer buffer = new StringBuffer();
        buffer.append(" select * ");
        buffer.append(" from ").append(tableName);
        buffer.append(" where  ");
        buffer.append(" ( ");
        buffer.append(" sMsg_Title like '%"+keywords+"%' ");
        buffer.append(" or sMsg_Describe like '%"+keywords+"%' ");
        buffer.append(" )  ");
        if(!Utils.isNullOrEmpty(sUser_ID)){
            buffer.append(" and sMsg_ToUserID = ? ");
            args.add(sUser_ID);
        }

        Cursor cursor = baseHelper.getReadableDatabase()
                .rawQuery(buffer.toString(), args.toArray(new String[args.size()]) );

        while (cursor.moveToNext()) {
            Messages obj = new Messages();
            obj.cursorToBean(cursor);
            list.add(obj);
        }
        cursor.close();
        return list;
    }

    public Map<String, Object> view(String sMsg_ID, String sUser_ID){
        List<String> args = new ArrayList<>();
        StringBuffer buffer = new StringBuffer();
        buffer.append(" select ");
        buffer.append("	a.*, ");
        buffer.append("	b.sAid_Name sMsg_AidName, ");
        buffer.append("	c.sEquip_Name sMsg_EquipName, ");
        buffer.append("	d.sUser_Nick sMsg_UserName, ");
        buffer.append("	e.sUser_Nick sMsg_ToUserName, ");
        buffer.append("	f.sUser_Nick sMsg_FromUserName, ");
        buffer.append("	g.sStoreType_Name sMsg_StoreLv1Name, ");
        buffer.append("	h.sStore_Name sMsg_StoreLv2Name, ");
        buffer.append("	i.sStore_Name sMsg_StoreLv3Name, ");
        buffer.append("	j.sStore_Name sMsg_StoreLv4Name, ");
        buffer.append("	m.sDict_Name sMsg_TypeName, ");
        buffer.append("	n.sDict_Name sMsg_LabelName, ");
        buffer.append("	o.sDict_Name sMsg_StatusName, ");
        buffer.append("	p.sDict_Name sMsg_ReasonName ");
        buffer.append(" from ").append(tableName).append(" a ");
        buffer.append(" 	left join tBase_Aid b ON a.sMsg_AidID = b.sAid_ID ");
        buffer.append(" 	left join tBase_Equip c ON a.sMsg_EquipID = c.sEquip_ID ");
        buffer.append(" 	left join tBase_User d on a.sMsg_UserID = d.sUser_ID ");
        buffer.append(" 	left join tBase_User e on a.sMsg_ToUserID = e.sUser_ID ");
        buffer.append(" 	left join tBase_User f on a.sMsg_FromUserID = f.sUser_ID ");
        buffer.append(" 	left join tBase_StoreType g ON a.sMsg_StoreLv1 = g.sStoreType_ID ");
        buffer.append(" 	left join tBase_Store h ON a.sMsg_StoreLv2 = h.sStore_ID ");
        buffer.append(" 	left join tBase_Store i ON a.sMsg_StoreLv3 = i.sStore_ID ");
        buffer.append(" 	left join tBase_Store j ON a.sMsg_StoreLv4 = j.sStore_ID ");
        buffer.append(" 	left join tBase_Dict m ON a.sMsg_Type = m.sDict_NO and m.sDict_DictTypeNO = 'MsgType' ");
        buffer.append(" 	left join tBase_Dict n ON a.sMsg_Label = n.sDict_NO and n.sDict_DictTypeNO = 'MsgLabel' ");
        buffer.append(" 	left join tBase_Dict o ON a.sMsg_Status = o.sDict_NO and o.sDict_DictTypeNO = 'MsgStatus' ");
        buffer.append(" 	left join tBase_Dict p ON a.sMsg_Reason = p.sDict_NO and p.sDict_DictTypeNO = 'MsgReason' ");
        buffer.append(" where a.sMsg_ID = ? ");
        args.add(sMsg_ID);
        if(!Utils.isNullOrEmpty(sUser_ID)){
            buffer.append(" and a.sMsg_ToUserID = ? ");
            args.add(sUser_ID);
        }

        Cursor cursor = baseHelper.getReadableDatabase()
                .rawQuery(buffer.toString(), args.toArray(new String[args.size()]) );

        Map<String, Object> obj = new HashMap<>();
        if(cursor.moveToNext()) {
            for (int i = 0; i < cursor.getColumnCount(); i++) {
                obj.put(cursor.getColumnName(i), cursor.getString(i));
            }
        }

        cursor.close();
        return obj;
    }


    public static String createTable(){
        StringBuffer buffer = new StringBuffer();
        buffer.append("CREATE TABLE ").append(tableName);
        buffer.append(" ( ");
        buffer.append(" 	 sMsg_ID NVARCHAR(32) NOT NULL, ");
        buffer.append("      sMsg_Type NVARCHAR(64), ");
        buffer.append("      dMsg_CreateDate DATE, ");
        buffer.append("      sMsg_ToUserID NVARCHAR(32), ");
        buffer.append("      sMsg_EquipID NVARCHAR(32), ");
        buffer.append("      sMsg_Describe NVARCHAR(255), ");
        buffer.append("      sMsg_Remarks NVARCHAR(255), ");
        buffer.append("      dMsg_UpdateDate DATE, ");
        buffer.append("      sMsg_UserID NVARCHAR(32), ");
        buffer.append("      sMsg_AidID NVARCHAR(32), ");
        buffer.append("      sMsg_IP NVARCHAR(255), ");
        buffer.append("      sMsg_FromUserID NVARCHAR(32), ");
        buffer.append("      sMsg_Label NVARCHAR(64), ");
        buffer.append("      lMsg_Level NUMBER, ");
        buffer.append("      sMsg_Status NVARCHAR(64), ");
        buffer.append("      sMsg_Title NVARCHAR(32), ");
        buffer.append("      sMsg_StoreLv1 NVARCHAR(32), ");
        buffer.append("      sMsg_StoreLv2 NVARCHAR(32), ");
        buffer.append("      sMsg_StoreLv3 NVARCHAR(32), ");
        buffer.append("      sMsg_StoreLv4 NVARCHAR(32), ");
        buffer.append("      dMsg_StoreNum NUMBER, ");
        buffer.append("      sMsg_Reason NVARCHAR(64), ");
        buffer.append("      sMsg_Source NVARCHAR(64), ");
        buffer.append("  PRIMARY KEY (sMsg_ID)");
        buffer.append(" );");
        return buffer.toString();
    }

    public static String dropTable(){
        return "drop table if exists " +  tableName;
    }
}
