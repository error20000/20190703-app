package com.jian.system.service;

import android.content.Context;

import com.jian.system.dao.AidEquipMapper;
import com.jian.system.dao.AidMapIconMapper;
import com.jian.system.entity.AidEquip;
import com.jian.system.entity.AidMapIcon;

import java.util.List;

public class AidMapIconService {

    private AidMapIconMapper baseMapper;

    public AidMapIconService(Context context){
        baseMapper = new AidMapIconMapper(context);
    }

    public AidMapIconMapper getMapper(){
        return baseMapper;
    }

    public void deleteAll(){
        baseMapper.deleteAll();
    }

    public void insert(List<AidMapIcon> data){
        baseMapper.insert(data);
    }
}
