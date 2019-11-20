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

    public void insert(Note data){
        baseMapper.insert(data);
    }

    public Note selectOne(String sNote_ID){
         return baseMapper.selectOne(sNote_ID);
    }
}
