package com.jian.system.service;

import android.content.Context;

import com.jian.system.dao.EquipSpareLampMapper;
import com.jian.system.entity.EquipSpareLamp;

import java.util.Map;

public class EquipSpareLampService {

    private EquipSpareLampMapper baseMapper;

    public EquipSpareLampService(Context context){
        baseMapper = new EquipSpareLampMapper(context);
    }


    public EquipSpareLamp selectOne(Map<String, Object> condition){
        return baseMapper.selectOne(condition);
    }
}
