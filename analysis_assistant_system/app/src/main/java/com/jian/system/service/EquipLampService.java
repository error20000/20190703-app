package com.jian.system.service;

import android.content.Context;

import com.jian.system.dao.EquipLampMapper;
import com.jian.system.entity.EquipLamp;

import java.util.Map;

public class EquipLampService {

    private EquipLampMapper baseMapper;

    public EquipLampService(Context context){
        baseMapper = new EquipLampMapper(context);
    }


    public EquipLamp selectOne(Map<String, Object> condition){
        return baseMapper.selectOne(condition);
    }
}
