package com.jian.system.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jian.system.db.BaseHelperManager;
import com.jian.system.entity.Equip;
import com.jian.system.entity.Note;
import com.jian.system.utils.Utils;

import java.util.ArrayList;
import java.util.List;


public class NoteMapper {

    private final static String TAG = NoteMapper.class.getSimpleName();
    public static String tableName = "tBase_Note";

    private BaseHelperManager baseHelper;

    public NoteMapper(Context context){
        baseHelper = BaseHelperManager.getInstance(context);
    }

    public BaseHelperManager getBaseHelper(){
        return baseHelper;
    }

    //TODO --------------------------------------------------------------------------------操作

    public List<Note> selectAll(){

        List<Note> list = new ArrayList<>();

        StringBuffer buffer = new StringBuffer();
        buffer.append("select * from ");
        buffer.append(tableName);

        Cursor cursor = baseHelper.getReadableDatabase()
                .rawQuery(buffer.toString(), null);

        while (cursor.moveToNext()) {
            Note obj = new Note();
            obj.cursorToBean(cursor);
            list.add(obj);
        }
        cursor.close();
        return list;
    }

    public Note selectOne(String sNote_ID){

        StringBuffer buffer = new StringBuffer();
        buffer.append("select * from ");
        buffer.append(tableName);
        buffer.append(" where sNote_ID  = ? ");

        Cursor cursor = baseHelper.getReadableDatabase()
                .rawQuery(buffer.toString(), new String[]{ sNote_ID });

        Note obj = new Note();
        if (cursor.moveToNext()) {
            obj.cursorToBean(cursor);
        }

        cursor.close();
        return obj;
    }

    public List<Note> selectList(String sNote_UserID){

        List<Note> list = new ArrayList<>();

        if(Utils.isNullOrEmpty(sNote_UserID)){
            return list;
        }

        StringBuffer buffer = new StringBuffer();
        buffer.append("select * from ");
        buffer.append(tableName);
        buffer.append(" where sNote_UserID  = ? ");

        Cursor cursor = baseHelper.getReadableDatabase()
                .rawQuery(buffer.toString(), new String[]{ sNote_UserID });

        while (cursor.moveToNext()) {
            Note obj = new Note();
            obj.cursorToBean(cursor);
            list.add(obj);
        }
        cursor.close();
        return list;
    }

    public long insert(Note note){
        SQLiteDatabase db = baseHelper.getWritableDatabase();
        return db.insert(tableName,null, note.beanToValues());
    }

    public int update(Note note){
        SQLiteDatabase db = baseHelper.getWritableDatabase();
        return db.update(tableName,note.beanToValues(), "sNote_ID  = ? ", new String[]{ note.getsNote_ID() });
    }

    public int delete(String sNote_ID){
        return baseHelper.getWritableDatabase()
                .delete(tableName,"sNote_ID  = ? ", new String[]{ sNote_ID });
    }

    //TODO --------------------------------------------------------------------------------同步数据
    public int deleteAll(){
        return baseHelper.getReadableDatabase()
                .delete(tableName, null, null);
    }

    public void insert(List<Note> data){
        SQLiteDatabase db = baseHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            for (Note node: data) {
                db.insert(tableName, null, node.beanToValues());
            }
            db.setTransactionSuccessful();
        }catch (Exception e){
            e.printStackTrace();
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
        buffer.append(" 	 sNote_ID NVARCHAR(32) NOT NULL, ");
        buffer.append("      sNote_Content NVARCHAR(2000), ");
        buffer.append("      sNote_UserID NVARCHAR(32), ");
        buffer.append("      dNote_CreateDate DATE, ");
        buffer.append("      dNote_UpdateDate DATE, ");
        buffer.append("  PRIMARY KEY (sNote_ID)");
        buffer.append(" );");
        return buffer.toString();
    }

    public static String dropTable(){
        return "drop table if exists " +  tableName;
    }
}
