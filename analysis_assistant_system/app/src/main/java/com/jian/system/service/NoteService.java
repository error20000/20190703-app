package com.jian.system.service;

import android.content.Context;

import com.jian.system.dao.NfcMapper;
import com.jian.system.dao.NoteMapper;
import com.jian.system.entity.Nfc;
import com.jian.system.entity.Note;

import java.util.List;

public class NoteService {

    private NoteMapper baseMapper;

    public NoteService(Context context){
        baseMapper = new NoteMapper(context);
    }


    public NoteMapper getMapper(){
        return baseMapper;
    }

    public void deleteAll(){
        baseMapper.deleteAll();
    }

    public long insert(Note data){
        return baseMapper.insert(data);
    }

    public Note selectOne(String sNote_ID){
         return baseMapper.selectOne(sNote_ID);
    }

    public List<Note> selectList(String sNote_UserID){
        return baseMapper.selectList(sNote_UserID);
    }

    public int update(Note data){
        return baseMapper.update(data);
    }

    public int delete(String sNote_ID){
        return baseMapper.delete(sNote_ID);
    }
}
