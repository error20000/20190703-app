package com.jian.system.service;

import android.content.Context;

import com.jian.system.dao.EquipSolarEnergyMapper;
import com.jian.system.entity.EquipSolarEnergy;

import java.util.Map;

public class EquipSolarEnergyService {

    private EquipSolarEnergyMapper baseMapper;

    public EquipSolarEnergyService(Context context){
        baseMapper = new EquipSolarEnergyMapper(context);
    }


    public EquipSolarEnergy selectOne(Map<String, Object> condition){
        return baseMapper.selectOne(condition);
    }
}
