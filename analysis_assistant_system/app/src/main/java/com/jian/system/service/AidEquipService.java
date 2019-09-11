package com.jian.system.service;

import android.content.Context;

import com.jian.system.dao.AidEquipMapper;
import com.jian.system.entity.AidEquip;

import java.util.List;

public class AidEquipService {

    private AidEquipMapper baseMapper;

    public AidEquipService(Context context){
        baseMapper = new AidEquipMapper(context);
    }

    public AidEquipMapper getMapper(){
        return baseMapper;
    }

    public void deleteAll(){
        baseMapper.deleteAll();
    }

    public void insert(List<AidEquip> data){
        baseMapper.insert(data);
    }
}
