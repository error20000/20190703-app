package com.jian.system.service;

import android.content.Context;

import com.jian.system.dao.EquipViceLampMapper;
import com.jian.system.entity.EquipViceLamp;

import java.util.Map;

public class EquipViceLampService {

    private EquipViceLampMapper baseMapper;

    public EquipViceLampService(Context context){
        baseMapper = new EquipViceLampMapper(context);
    }


    public EquipViceLamp selectOne(Map<String, Object> condition){
        return baseMapper.selectOne(condition);
    }
}
