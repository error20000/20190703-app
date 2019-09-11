package com.jian.system.service;

import android.content.Context;

import com.jian.system.dao.AidMapIconMapper;
import com.jian.system.dao.AidTypeMapIconMapper;
import com.jian.system.entity.AidMapIcon;
import com.jian.system.entity.AidTypeMapIcon;

import java.util.List;

public class AidTypeMapIconService {

    private AidTypeMapIconMapper baseMapper;

    public AidTypeMapIconService(Context context){
        baseMapper = new AidTypeMapIconMapper(context);
    }

    public AidTypeMapIconMapper getMapper(){
        return baseMapper;
    }

    public void deleteAll(){
        baseMapper.deleteAll();
    }

    public void insert(List<AidTypeMapIcon> data){
        baseMapper.insert(data);
    }
}
