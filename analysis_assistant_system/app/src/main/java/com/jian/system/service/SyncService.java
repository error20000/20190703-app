package com.jian.system.service;

import android.content.Context;

import com.jian.system.dao.SyncMapper;
import com.jian.system.dao.SystemMapper;
import com.jian.system.entity.Sync;
import com.jian.system.entity.System;

import java.util.List;

public class SyncService {

    private SyncMapper baseMapper;

    public SyncService(Context context){
        baseMapper = new SyncMapper(context);
    }

    public SyncMapper getMapper(){
        return baseMapper;
    }

    public List<Sync> selectAll(){
        return baseMapper.selectAll();
    }

    public Sync selectOne(String sSync_TableName){
        return baseMapper.selectOne(sSync_TableName);
    }

    public void insert(Sync data){
        baseMapper.insert(data);
    }

    public void update(Sync data){
        baseMapper.update(data);
    }
}
